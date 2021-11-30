package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.settings.KindSettings
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

internal class MotivationExecutionListener : ExecutionListener {

    private val settings
        get() = MotivationListenersCounters.getInstance()

    private val notStartedCountNeeded
        get() = KindSettings.getInstance().motivationFrequencyAfterExecFail

    private val terminatedCountNeeded
        get() = KindSettings.getInstance().motivationFrequencyAfterNonZeroExitCode

    override fun processNotStarted(executorId: String, env: ExecutionEnvironment) {
        super.processNotStarted(executorId, env)
        settings.notStartedCount++
        if (settings.notStartedCount % notStartedCountNeeded == 0) {
            motivateFail(env.project)
        }
    }

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        super.processStarted(executorId, env, handler)
        if (settings.notStartedCount >= notStartedCountNeeded) {
            motivateSuccess(env.project)
        }
        settings.notStartedCount = 0
    }

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        super.processTerminated(executorId, env, handler, exitCode)
        if (exitCode == 0) {
            if (settings.terminatedCount >= terminatedCountNeeded) {
                motivateSuccess(env.project)
            }
            settings.terminatedCount = 0
        } else {
            settings.terminatedCount++
            if (settings.terminatedCount % terminatedCountNeeded == 0) {
                motivateFail(env.project)
            }
        }
    }

}
