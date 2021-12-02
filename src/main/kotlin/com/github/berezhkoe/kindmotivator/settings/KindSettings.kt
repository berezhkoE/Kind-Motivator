package com.github.berezhkoe.kindmotivator.settings

import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@State(name = "Kind Settings", storages = [(Storage("kind-settings.xml"))])
class KindSettings : SimplePersistentStateComponent<KindState>(KindState()) {
    companion object {
        fun getInstance(): KindSettings {
            return service()
        }
    }

    var motivationFrequencyAfterExecFail
        get() = state.motivationFrequencyAfterExecFail
        set(value) {
            state.motivationFrequencyAfterExecFail = value
        }

    var motivationFrequencyAfterNonZeroExitCode
        get() = state.motivationFrequencyAfterNonZeroExitCode
        set(value) {
            state.motivationFrequencyAfterNonZeroExitCode = value
        }

    var motivationAfterContinuousWork
        get() = state.motivationAfterContinuousWork
        set(value) {
            state.motivationAfterContinuousWork = value
        }

    var idleThreshold
        get() = state.idleThreshold
        set(value) {
            state.idleThreshold = value
        }

    var dontShowManyMemes
        get() = state.dontShowManyMemes
        set(value) {
            state.dontShowManyMemes = value
        }
}