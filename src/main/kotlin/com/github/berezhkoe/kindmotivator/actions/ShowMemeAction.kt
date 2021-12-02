package com.github.berezhkoe.kindmotivator.actions

import com.github.berezhkoe.kindmotivator.notification.MemeNotificationService
import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class ShowMemeAction: DumbAwareAction() {
    companion object {
        const val TITLE = "Meme"
    }

    override fun actionPerformed(e: AnActionEvent) {
        MemeNotificationService.getInstance().allowTitleBeRepeated(TITLE)
        MotivationShower.showRandomMeme(MotivationType.Rest, TITLE, e.project!!)
    }
}