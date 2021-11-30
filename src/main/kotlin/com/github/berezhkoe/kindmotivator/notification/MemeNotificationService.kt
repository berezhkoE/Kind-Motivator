package com.github.berezhkoe.kindmotivator.notification

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.util.MinimizeButton
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import com.intellij.util.concurrency.AppExecutorUtil
import java.awt.Dimension
import java.awt.Image
import java.awt.Point
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.swing.ImageIcon
import javax.swing.JLabel

class MemeNotificationService {
    companion object {
        fun getInstance() = service<MemeNotificationService>()

        fun memePath(vararg memePathComponents: String) = Path.of("memes", *memePathComponents)

        private const val MAX_IMAGE_HORIZONTAL_PORTION_OF_SCREEN = 4
        private const val MAX_IMAGE_VERTICAL_PORTION_OF_SCREEN = 3

        private const val TIMEOUT_SECONDS = 3L

        private val shownPopup = AtomicReference<JBPopup>()
    }

    fun showMeme(title: String, resourcePath: Path, project: Project) {
        val ideFrame = WindowManager.getInstance().getIdeFrame(project) ?: error("This project has no window")
        val imageIcon = getImageIcon(resourcePath, ideFrame.component.size)
        showMeme(title, imageIcon, project)
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

    private fun showMeme(title: String, imageIcon: ImageIcon, project: Project) {
        val label = JLabel(imageIcon)

        val popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(
                        label,
                        null
                ).setTitle(title)
                .setMovable(true)
                .setProject(project)
                .setCancelButton(MinimizeButton("Hide"))
                .createPopup()

        val ideFrame = WindowManager.getInstance().getIdeFrame(project)!!

        val point = RelativePoint.getNorthEastOf(ideFrame.component).let { pointNE ->
            val shiftedPoint = Point(pointNE.point.x - imageIcon.iconWidth, pointNE.point.y)
            RelativePoint(ideFrame.component, shiftedPoint)
        }

        shownPopup.updateAndGet { existingPopup ->
            if (existingPopup != null)
                return@updateAndGet existingPopup

            popup.show(point)

            AppExecutorUtil.getAppScheduledExecutorService().schedule({
                popup.cancel()
                shownPopup.compareAndSet(popup, null)
            }, TIMEOUT_SECONDS, TimeUnit.SECONDS)

            popup
        }
    }
}
