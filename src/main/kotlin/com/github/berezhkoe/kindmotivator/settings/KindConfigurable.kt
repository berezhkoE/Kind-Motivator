package com.github.berezhkoe.kindmotivator.settings

import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.titledRows

class KindConfigurable: BoundSearchableConfigurable("Kind Motivator", "Kind Motivator", _id = ID) {
    companion object {
        const val ID = "Settings.KindMotivator"
    }

    private val settings
        get() = KindSettings.getInstance()

    override fun createPanel(): DialogPanel {
        return panel {
            titledRow("Motivation Scenarios") {
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
                    intTextField(settings::motivationAfterContinuousWork, range = IntRange(2, 180))
                }
                row {
                    label("Idle Threshold, min: ")
                    intTextField(settings::idleThreshold, range = IntRange(1, 180))
                }
            }
            titledRow("Notifications") {
                row {
                    checkBox("Don't show many images one by one", settings::dontShowManyMemes)
                }
                row {
                    label("Don't show more than")
                    intTextField(settings::randomMemesNumberLimit, range = IntRange(1, 100))
                    label("memes each")
                    intTextField(settings::randomMemesHoursLimit, range = IntRange(1, 24))
                    label("hours")
                }
            }
        }
    }
}