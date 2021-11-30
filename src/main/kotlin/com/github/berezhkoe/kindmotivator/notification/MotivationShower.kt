package com.github.berezhkoe.kindmotivator.notification

import com.intellij.openapi.project.Project


object MotivationShower {
    fun showMeme(motivationType: MotivationType, title: String, motivationFileName: String, project: Project) {
        MemeNotificationService.getInstance().showMeme(
                title,
                MemeNotificationService.memePath(motivationType.pathBase, motivationFileName),
                project
        )
    }
}

enum class MotivationType(val pathBase: String) {
    Praise("praise"), Rest("rest"), Support("support")
}

