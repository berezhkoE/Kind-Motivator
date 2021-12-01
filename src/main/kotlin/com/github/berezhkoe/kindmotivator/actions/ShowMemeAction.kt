package com.github.berezhkoe.kindmotivator.actions

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction

class ShowMemeAction: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MotivationShower.showRandomMeme(MotivationType.Rest, "Meme", e.project!!)
    }
}