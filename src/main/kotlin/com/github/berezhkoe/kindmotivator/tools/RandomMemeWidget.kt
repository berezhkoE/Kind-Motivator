package com.github.berezhkoe.kindmotivator.tools

import com.github.berezhkoe.kindmotivator.actions.ShowMemeAction
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.Icon

class RandomMemeWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = RandomMemeWidgetFactory::class.qualifiedName!!

    override fun getDisplayName() = "Random Meme Widget Factory"

    override fun isAvailable(project: Project): Boolean = project.isOpen && !project.isDisposed

    override fun createWidget(project: Project) = RandomMemeWidget(project)

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

    override fun canBeEnabledOn(statusBar: StatusBar) = true
}

class RandomMemeWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.IconPresentation {
    override fun dispose() {
    }

    override fun ID() = RandomMemeWidget::class.qualifiedName!!

    override fun install(statusBar: StatusBar) {
        statusBar.updateWidget(ID())
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun getTooltipText() = "Show random meme"

    override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
        val actionEvent = AnActionEvent.createFromDataContext(ActionPlaces.STATUS_BAR_PLACE, null) {
            if (it == CommonDataKeys.PROJECT.name) {
                project
            } else null
        }
        ShowMemeAction().actionPerformed(actionEvent)
    }

    override fun getIcon(): Icon = AllIcons.General.InspectionsEye // FIXME
}