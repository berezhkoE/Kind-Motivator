package com.github.berezhkoe.kindmotivator.actions

import com.github.berezhkoe.kindmotivator.notification.MemeNotificationService
import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.github.berezhkoe.kindmotivator.settings.KindSettings
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.util.text.StringUtil
import java.util.*
import java.util.concurrent.TimeUnit

class ShowMemeAction: DumbAwareAction() {
    companion object {
        const val TITLE = "Meme"

        val triggerTimeLog = LinkedList<Long>()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val now = System.currentTimeMillis()
        // cleanup the log records we won't ever need
        while (triggerTimeLog.isNotEmpty() && now - triggerTimeLog.first > TimeUnit.HOURS.toMillis(24)) {
            triggerTimeLog.removeFirst()
        }

        val hourLimit = KindSettings.getInstance().randomMemesHoursLimit.toLong()
        val memeLimit = KindSettings.getInstance().randomMemesNumberLimit
        val blockerCount = triggerTimeLog.dropWhile { now - it > TimeUnit.HOURS.toMillis(hourLimit) }.count()

        if (blockerCount < memeLimit) {
            MemeNotificationService.getInstance().allowTitleBeRepeated(TITLE)
            MotivationShower.showRandomMeme(MotivationType.Rest, TITLE, e.project!!)
            triggerTimeLog += now
        } else {
            val hoursWord = StringUtil.pluralize("hour", hourLimit.toInt())
            val hoursStr = if (hourLimit == 1L) hoursWord else "$hourLimit $hoursWord"
            NotificationGroupManager.getInstance().getNotificationGroup("Kind Notification Group")
                    .createNotification(
                            title = "Meme limit reached",
                            content = "You've already watched $memeLimit memes within last $hoursStr"
                    ).notify(e.project!!)
        }
    }
}