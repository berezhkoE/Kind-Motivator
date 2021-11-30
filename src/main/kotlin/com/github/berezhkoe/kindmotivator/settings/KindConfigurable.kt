package com.github.berezhkoe.kindmotivator.settings

import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel

class KindConfigurable: BoundSearchableConfigurable("Kind Motivator", "Kind Motivator", _id = ID) {
    companion object {
        const val ID = "Settings.KindMotivator"
    }

    private val settings
        get() = KindSettings.getInstance()

    override fun createPanel(): DialogPanel {
        return panel {
            row {
                label("Motivation frequency after Execution Fail, times: ")
                intTextField(settings::motivationFrequencyAfterExecFail, range = IntRange(1, 100))
            }
            row {
                label("Motivation frequency after non zero Exit Code, times: ")
                intTextField(settings::motivationFrequencyAfterNonZeroExitCode, range = IntRange(1, 100))
            }.largeGapAfter()
            row {
                label("Motivation after Continuous Work, min: ")
                intTextField(settings::motivationAfterContinuousWork, range = IntRange(5, 180))
            }
            row {
                label("Idle Threshold, min: ")
                intTextField(settings::motivationAfterContinuousWork, range = IntRange(1, 180))
            }
        }
    }
}