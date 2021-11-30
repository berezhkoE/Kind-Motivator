package com.github.berezhkoe.kindmotivator.settings

import com.intellij.ide.ui.fullRow
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
                label("Motivation frequency after Execution Fail, min: ")
                intTextField(settings::motivationFrequencyAfterExecFail, range = IntRange(5, 180))
            }
            row {
                label("Motivation frequency after non zero Exit Code, min: ")
                intTextField(settings::motivationFrequencyAfterNonZeroExitCode, range = IntRange(5, 180))
            }
        }
    }
}