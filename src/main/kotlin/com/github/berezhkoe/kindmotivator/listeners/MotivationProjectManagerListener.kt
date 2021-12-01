package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class MotivationProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val connection = project.messageBus.connect()
        connection.subscribe(SMTRunnerEventsListener.TEST_STATUS, MotivationSMTRunnerEventsListener(project))
    }
}
