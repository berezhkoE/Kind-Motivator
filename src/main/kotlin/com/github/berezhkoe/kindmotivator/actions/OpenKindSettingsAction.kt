package com.github.berezhkoe.kindmotivator.actions

import com.github.berezhkoe.kindmotivator.settings.KindConfigurable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction

class OpenKindSettingsAction: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.project!!, KindConfigurable::class.java)
    }
}