package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.dvcs.ui.DvcsBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener

class KindForcePushActionListener: AnActionListener {
    override fun beforeActionPerformed(action: AnAction, dataContext: DataContext, event: AnActionEvent) {
        super.beforeActionPerformed(action, dataContext, event)
        if (action.templateText == DvcsBundle.message("force.push.dialog.title")) {
            MotivationShower.showRandomMeme(
                MotivationType.ForcePush,
                "FORCE PUSH!11",
                event.project!!
            )
        }
    }
}