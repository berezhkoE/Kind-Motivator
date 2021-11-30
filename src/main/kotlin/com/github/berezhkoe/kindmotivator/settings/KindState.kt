package com.github.berezhkoe.kindmotivator.settings

import com.intellij.openapi.components.BaseState

class KindState: BaseState() {
    /**
     * частота поддержки при незапуске MyExecutionListener
     */
    var motivationFrequencyAfterExecFail by property(5)
    /**
     * частота поддержки при ненулевом коде
     */
    var motivationFrequencyAfterNonZeroExitCode by property(5)

    /**
     * мотивация за непрерывную работу, мин
     */
    var motivationAfterContinuousWork by property(2)

    var idleThreshold by property(1)
}