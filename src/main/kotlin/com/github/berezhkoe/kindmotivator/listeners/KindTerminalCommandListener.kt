package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.execution.Executor
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.terminal.TerminalShellCommandHandler

class KindTerminalCommandListener : TerminalShellCommandHandler {
    override fun execute(
        project: Project,
        workingDirectory: String?,
        localSession: Boolean,
        command: String,
        executor: Executor
    ): Boolean {
        return false
    }

    override fun matches(project: Project, workingDirectory: String?, localSession: Boolean, command: String): Boolean {
        if (command.startsWith("git push") && (command.contains(" -f") || command.contains("--force"))) {
            invokeLater {
                MotivationShower.showRandomMeme(
                    MotivationType.ForcePush,
                    "FORCE PUSH!11",
                    project
                )
            }
        }
        return false
    }
}