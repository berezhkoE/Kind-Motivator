package com.github.berezhkoe.kindmotivator.notification

import com.github.berezhkoe.kindmotivator.actions.OpenKindSettingsAction
import com.github.berezhkoe.kindmotivator.actions.ShowMemeAction
import com.github.berezhkoe.kindmotivator.settings.KindSettings
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.ActiveComponent
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.concurrency.AppExecutorUtil
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JLabel

class MemeNotificationService: Disposable {
    companion object {
        fun getInstance() = service<MemeNotificationService>()

        fun memePath(vararg memePathComponents: String) = Path.of("memes", *memePathComponents)

        private const val MAX_IMAGE_HORIZONTAL_PORTION_OF_SCREEN = 4
        private const val MAX_IMAGE_VERTICAL_PORTION_OF_SCREEN = 3

        private const val TIMEOUT_SECONDS = 5L
    }

    private var shownPopup: JBPopup? = null
    private var shownMeme: ShowMemeTask? = null
    private var fadeOutPopupFuture: ScheduledFuture<*>? = null

    private val memeQueue = LinkedList<ShowMemeTask>()
    private val titlesAllowedRepeat = mutableSetOf(ShowMemeAction.TITLE)

    @Synchronized
    fun showMeme(title: String, resourcePath: Path, project: Project) {
        showMemeUnderLock(title, resourcePath, project)
    }

    @Synchronized
    fun allowTitleBeRepeated(title: String) {
        titlesAllowedRepeat += title
    }

    @Synchronized
    fun disallowTitleBeRepeated(title: String): Boolean {
        return titlesAllowedRepeat.remove(title)
    }

    @Synchronized
    fun isAllowedTitleBeRepeated(title: String): Boolean {
        return title in titlesAllowedRepeat
    }

    private fun showMemeUnderLock(title: String, resourcePath: Path, project: Project) {
        val ideFrame = WindowManager.getInstance().getIdeFrame(project) ?: error("This project has no window")
        val imageIcon = getImageIcon(resourcePath, ideFrame.component.size)

        if (shownPopup != null) {
            if (!KindSettings.getInstance().dontShowManyMemes) {
                if (shownMeme?.title != title || isAllowedTitleBeRepeated(title)) {
                    memeQueue.add(ShowMemeTask(title, resourcePath, project))
                }
            }
            return
        }

        val point = RelativePoint.getNorthEastOf(ideFrame.component).let { pointNE ->
            val shiftedPoint = Point(pointNE.point.x - imageIcon.iconWidth, pointNE.point.y)
            RelativePoint(ideFrame.component, shiftedPoint)
        }

        shownPopup = createMemePopup(title, imageIcon, project).also { it.show(point) }
        shownMeme = ShowMemeTask(title, resourcePath, project)

        fadeOutPopupFuture = AppExecutorUtil.getAppScheduledExecutorService().schedule({
            invokeLater {
                expireCurrentMeme(shownPopup)
            }
        }, TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }

    @Synchronized
    private fun expireCurrentMeme(expectedPopup: JBPopup?) {
        if (shownPopup != expectedPopup) {
            return
        }
        shownPopup?.cancel()
        shownPopup = null
        shownMeme = null
        if (memeQueue.isNotEmpty()) {
            val (title, memePath, project) = memeQueue.removeFirst()
            if (!KindSettings.getInstance().dontShowManyMemes) {
                showMemeUnderLock(title, memePath, project)
            }
        }
    }

    private fun getImageIcon(resourcePath: Path, ideFrameSize: Dimension) : ImageIcon {
        val resourceUrl = MemeNotificationService::class.java.classLoader.getResource(resourcePath.toString())
                ?: error("Resource '$resourcePath' not found")

        val imageIconOriginal = ImageIcon(resourceUrl)

        var width = ideFrameSize.width / MAX_IMAGE_HORIZONTAL_PORTION_OF_SCREEN
        var height = imageIconOriginal.iconHeight * width / imageIconOriginal.iconWidth

        if (height > ideFrameSize.height / MAX_IMAGE_VERTICAL_PORTION_OF_SCREEN) {
            height = ideFrameSize.height / MAX_IMAGE_VERTICAL_PORTION_OF_SCREEN
            width = imageIconOriginal.iconWidth * height / imageIconOriginal.iconHeight
        }

        val image = imageIconOriginal.image.getScaledInstance(width, height, Image.SCALE_DEFAULT)
        return ImageIcon(image)
    }

    private fun createMemePopup(title: String, imageIcon: ImageIcon, project: Project): JBPopup {
        val label = JLabel(imageIcon)

        val popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(label, null)
                .setMovable(true)
                .setTitle(title)
                .setProject(project)
                .setCommandButton(object : ActiveComponent.Adapter() {
                    override fun getComponent(): JComponent {
                        val buttonPresentation = Presentation()
                        buttonPresentation.icon = AllIcons.General.GearPlain

                        return ActionButton(
                                OpenKindSettingsAction(),
                                buttonPresentation,
                                ActionPlaces.NOTIFICATION, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE)
                    }
                })
                .setCancelOnClickOutside(false)
                .setBelongsToGlobalPopupStack(false)
                .createPopup()

        popup.addListener(object : JBPopupListener {
            override fun onClosed(event: LightweightWindowEvent) {
                invokeLater {
                    expireCurrentMeme(popup)
                }
            }
        })

        popup.content.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                expireCurrentMeme(popup)
            }

            override fun mouseEntered(e: MouseEvent?) {
                fadeOutPopupFuture?.cancel(true)
            }

            override fun mouseExited(e: MouseEvent?) {
                fadeOutPopupFuture = AppExecutorUtil.getAppScheduledExecutorService().schedule({
                    invokeLater {
                        expireCurrentMeme(popup)
                    }
                }, TIMEOUT_SECONDS - 2, TimeUnit.SECONDS)
            }
        })
        popup.content.cursor = Cursor(Cursor.HAND_CURSOR)
        return popup
    }

    override fun dispose() {
        memeQueue.clear()
        shownMeme = null
    }
}

private data class ShowMemeTask(val title: String, val memePath: Path, val project: Project)
