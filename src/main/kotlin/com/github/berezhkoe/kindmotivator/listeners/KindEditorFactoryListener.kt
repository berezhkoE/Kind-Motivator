package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MemeNotificationService
import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.github.berezhkoe.kindmotivator.settings.KindSettings
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.util.concurrency.EdtExecutorService
import java.awt.EventQueue
import java.nio.file.Path
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class KindEditorFactoryListener : EditorFactoryListener {
    enum class TimeTrackingStatus {
        RUNNING, STOPPED
    }

    private var editor: Editor? = null

    @Volatile
    private var status = TimeTrackingStatus.STOPPED

    private var totalTimeMs: Long = 0
    private var statusStartedMs = System.currentTimeMillis()
    private var lastTickMs = System.currentTimeMillis()

    @Volatile
    private var lastActivityMs = System.currentTimeMillis()

    private var ticker: ScheduledFuture<*>? = null

    val kindSettings = KindSettings.getInstance()

    private val TICK_DELAY_UNIT = TimeUnit.MINUTES
    private val TICK_JUMP_DETECTION_THRESHOLD_MS: Long =
        TICK_DELAY_UNIT.toMillis(kindSettings.motivationAfterContinuousWork.toLong())

    private val idleThresholdMs: Long = TICK_DELAY_UNIT.toMillis(kindSettings.idleThreshold.toLong())

    override fun editorCreated(event: EditorFactoryEvent) {
        super.editorCreated(event)
        saveTime()
        editor = event.editor
        editor!!.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(e: DocumentEvent) {
                if (status == TimeTrackingStatus.RUNNING) {
                    val now = System.currentTimeMillis()
                    val msInState = 0L.coerceAtLeast(now - statusStartedMs)
                    if (status == TimeTrackingStatus.RUNNING) {
                        totalTimeMs = 0L.coerceAtLeast(totalTimeMs + msInState)
                    }
                    lastActivityMs = now
                } else {
                    EventQueue.invokeLater {
                        if (e.document == editor!!.document) {
                            setStatus(TimeTrackingStatus.RUNNING)
                        }
                    }
                }
            }
        })
    }

    @Synchronized
    private fun saveTime() {
        if (status == TimeTrackingStatus.RUNNING) {
            val now = System.currentTimeMillis()
            val msInState = 0L.coerceAtLeast(now - statusStartedMs)
            statusStartedMs = now
            totalTimeMs = 0L.coerceAtLeast(totalTimeMs + msInState)
        }
    }

    @Synchronized
    fun setStatus(status: TimeTrackingStatus) {
        setStatus(status, System.currentTimeMillis())
    }

    private fun setStatus(status: TimeTrackingStatus, now: Long) {
        if (this.status == status) {
            return
        }
        if (ticker != null) {
            ticker!!.cancel(false)
            ticker = null
        }
        val msInState = 0L.coerceAtLeast(now - statusStartedMs)
        if (this.status == TimeTrackingStatus.RUNNING) {
            totalTimeMs = 0L.coerceAtLeast(totalTimeMs + msInState)
        }
        statusStartedMs = now
        lastTickMs = now
        lastActivityMs = now
        this.status = status
        if (status == TimeTrackingStatus.RUNNING) {
            ticker = EdtExecutorService.getScheduledExecutorInstance().scheduleWithFixedDelay(
                { this.tick() },
                idleThresholdMs,
                idleThresholdMs,
                TimeUnit.MILLISECONDS
            )
        }
    }

    @Synchronized
    private fun tick() {
        if (status != TimeTrackingStatus.RUNNING) {
            return
        }
        val now = System.currentTimeMillis()
        val sinceLastTickMs = now - lastTickMs
        val lastActivityMs = lastActivityMs
        val sinceLastActivityMs = now - lastActivityMs
        if (sinceLastActivityMs <= idleThresholdMs && sinceLastTickMs >= TICK_JUMP_DETECTION_THRESHOLD_MS) {
            lastTickMs = now
            MotivationShower.showRandomMeme(MotivationType.Support,
                "${kindSettings.motivationAfterContinuousWork} minutes passed!",
                editor!!.project!!
            )
        } else if (sinceLastActivityMs > idleThresholdMs) {
            val lastValidTimeMs = lastActivityMs + idleThresholdMs
            setStatus(
                TimeTrackingStatus.STOPPED,
                lastValidTimeMs
            )
        }
    }
}