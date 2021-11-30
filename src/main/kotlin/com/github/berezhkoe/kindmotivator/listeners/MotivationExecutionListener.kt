package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

internal class MotivationExecutionListener : ExecutionListener {

    companion object {
        private const val notStartedCountName = "not_started_times"
        private const val terminatedCountName = "terminated_times"
        private const val notStartedCountNeeded = 5
        private const val terminatedCountNeeded = 5
    }
    
    override fun processNotStarted(executorId: String, env: ExecutionEnvironment) {
        super.processNotStarted(executorId, env)
        env.project
        val counter = incrementCounter(notStartedCountName)
        if (counter % notStartedCountNeeded == 0) {
            motivateFail(env.project)
        }
    }

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        super.processStarted(executorId, env, handler)
        if (getCounter(notStartedCountName) >= notStartedCountNeeded) {
            motivateSuccess(env.project)
        }
        refreshCounter(notStartedCountName)
    }

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        super.processTerminated(executorId, env, handler, exitCode)
        if (exitCode == 0) {
            if (getCounter(terminatedCountName) >= terminatedCountNeeded) {
                motivateSuccess(env.project)
            }
            refreshCounter(terminatedCountName)
        } else {
            incrementCounter(terminatedCountName)
            if (getCounter(terminatedCountName) % terminatedCountNeeded == 0) {
                motivateFail(env.project)
            }
        }
    }

}