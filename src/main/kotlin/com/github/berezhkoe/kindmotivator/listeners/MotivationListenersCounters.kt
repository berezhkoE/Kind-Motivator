package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@State(name = "Motivation listeners counters", storages = [(Storage("motivation-listeners-counters.xml"))])
class MotivationListenersCounters  : SimplePersistentStateComponent<MotivationListenerState>(MotivationListenerState()) {
    companion object {
        fun getInstance(): MotivationListenersCounters {
            return service()
        }
    }

    var notStartedCount
        get() = state.notStartedCount
        set(value) {
            state.notStartedCount = value
        }

    var terminatedCount
        get() = state.terminatedCount
        set(value) {
            state.terminatedCount = value
        }
}