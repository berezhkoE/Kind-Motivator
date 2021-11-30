package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class MotivationProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val settings = MotivationListenersCounters.getInstance()
        settings.notStartedCount = 0
        settings.terminatedCount = 0
    }
}
