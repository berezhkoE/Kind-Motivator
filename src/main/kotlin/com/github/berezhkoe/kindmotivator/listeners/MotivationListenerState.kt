package com.github.berezhkoe.kindmotivator.listeners

import com.intellij.openapi.components.BaseState

class MotivationListenerState: BaseState() {
    /**
     * сколько раз программа не запустилась
     */
    var notStartedCount by property(0)
    /**
     * сколько раз прога завершилась с ненулевым кодом
     */
    var terminatedCount by property(0)
}