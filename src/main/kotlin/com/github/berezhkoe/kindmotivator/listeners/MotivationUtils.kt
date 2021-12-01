package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

internal fun motivateFail(project: Project, title: String? = null) {
    invokeLater {
        MotivationShower.showRandomMeme(MotivationType.Support, title ?:"Крепись...", project)
    }
}

internal fun motivateSuccess(project: Project, title: String? = null) {
    invokeLater {
        MotivationShower.showRandomMeme(MotivationType.Praise, title ?: "Ура!", project)
    }
}

internal fun motivateEarly(project: Project) {
    invokeLater {
        MotivationShower.showRandomMeme(MotivationType.Early, "С добрым утром, трудяга!", project)
    }
}

internal fun motivateLate(project: Project) {
    invokeLater {
        MotivationShower.showRandomMeme(MotivationType.Late, "Ты заработался, друг :)", project)
    }
}