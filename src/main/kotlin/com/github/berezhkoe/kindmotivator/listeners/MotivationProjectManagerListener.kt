package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import java.time.LocalTime

internal class MotivationProjectManagerListener : ProjectManagerListener {
//  0 5 8
    companion object {
        private const val lateTimeHour = 0
        private const val middleTimeHour = 5
        private const val earlyTimeHour = 8
    }

    override fun projectOpened(project: Project) {
        val connection = project.messageBus.connect()

        val earlyTime = LocalTime.of(earlyTimeHour, 0, 0)
        val middleTime = LocalTime.of(middleTimeHour, 0, 0)
        val lateTime = LocalTime.of(lateTimeHour, 0, 0)
        val now = LocalTime.now()

        if (now.isAfter(lateTime) && now.isBefore(middleTime)) {
            motivateLate(project)
        }

        if (now.isAfter(lateTime) && now.isBefore(earlyTime)) {
            motivateEarly(project)
        }


        connection.subscribe(SMTRunnerEventsListener.TEST_STATUS, MotivationSMTRunnerEventsListener(project))
    }
}
