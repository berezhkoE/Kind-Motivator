package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.settings.KindSettings
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

internal class MotivationExecutionListener : ExecutionListener {
    companion object {
        private var notStartedCount = 0
        private var terminatedCount = 0
    }

    private val notStartedCountNeeded
        get() = KindSettings.getInstance().motivationFrequencyAfterExecFail

    private val terminatedCountNeeded
        get() = KindSettings.getInstance().motivationFrequencyAfterNonZeroExitCode

    override fun processNotStarted(executorId: String, env: ExecutionEnvironment) {
        super.processNotStarted(executorId, env)
        notStartedCount++
        if (notStartedCount % notStartedCountNeeded == 0) {
            motivateFail(env.project)
        }
    }

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        super.processStarted(executorId, env, handler)
        if (notStartedCount >= notStartedCountNeeded) {
            motivateSuccess(env.project)
        }
        notStartedCount = 0
    }

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        super.processTerminated(executorId, env, handler, exitCode)
        if (exitCode == 0) {
            if (terminatedCount >= terminatedCountNeeded) {
                motivateSuccess(env.project)
            }
            terminatedCount = 0
        } else {
            terminatedCount++
            if (terminatedCount % terminatedCountNeeded == 0) {
                motivateFail(env.project)
            }
        }
    }

}
