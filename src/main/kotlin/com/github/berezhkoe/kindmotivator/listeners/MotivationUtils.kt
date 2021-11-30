package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

internal fun motivateFail(project: Project) {
    invokeLater {
        MotivationShower.showMeme(MotivationType.Support, "Крепись...", "klichko.jpg", project)
    }
}

internal fun motivateSuccess(project: Project) {
    invokeLater {
        MotivationShower.showMeme(MotivationType.Praise, "Ура!", "leonardo-dicaprio-cheers.gif", project)
    }
}