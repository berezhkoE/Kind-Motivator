package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.execution.testframework.Filter
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter
import com.intellij.execution.testframework.sm.runner.SMTestProxy

class MotivationSMTRunnerEventsListener: SMTRunnerEventsAdapter() {
    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        super.onTestingFinished(testsRoot)
        val allTests = Filter.LEAF.select(testsRoot.allTests)
        val failed = Filter.DEFECTIVE_LEAF.select(allTests)
        val notStarted = Filter.NOT_PASSED.select(allTests)
        notStarted.removeAll(failed)
        val ignored = Filter.IGNORED.select(allTests)
        notStarted.removeAll(ignored)
        failed.removeAll(ignored)
        val failedCount = failed.size
        val notStartedCount = notStarted.size + ignored.size
        val passedCount = allTests.size - failedCount - notStartedCount
        TODO("implement tests count logic")
    }
}