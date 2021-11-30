package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MemeNotificationService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

internal fun motivateOpen(project: Project) {
    invokeLater {
        MemeNotificationService.getInstance().showMeme(MemeNotificationService.memePath("praise", "original.gif"), project)
    }
}

internal fun motivateFail(project: Project) {
    invokeLater {
        MemeNotificationService.getInstance().showMeme(MemeNotificationService.memePath("praise", "original.gif"), project)
    }
}

internal fun motivateSuccess(project: Project) {
    invokeLater {
        MemeNotificationService.getInstance().showMeme(MemeNotificationService.memePath("praise", "original.gif"), project)
    }
}