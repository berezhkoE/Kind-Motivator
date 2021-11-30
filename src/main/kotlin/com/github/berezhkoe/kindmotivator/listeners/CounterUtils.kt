package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MemeNotificationService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

internal fun motivateFail(project: Project) {
    invokeLater {
        MemeNotificationService.getInstance().showMeme("Молодец!", MemeNotificationService.memePath("praise", "original.gif"), project)
    }
}

internal fun motivateSuccess(project: Project) {
    invokeLater {
        MemeNotificationService.getInstance().showMeme("Молодец!", MemeNotificationService.memePath("praise", "original.gif"), project)
    }
}

internal fun getCounter(name: String) : Int {
    return properties().getValue(name, "0").toInt()
}

internal fun incrementCounter(name: String) : Int {
    val newValue = getCounter(name) + 1
    properties().setValue(name, newValue.toString())
    return newValue
}

internal fun refreshCounter(name: String) = properties().setValue(name, "0")

private fun properties() = PropertiesComponent.getInstance()