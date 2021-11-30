package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

internal fun motivateOpen(project: Project) {
    invokeLater {
        MotivationShower.showMeme(MotivationType.Praise, "Штош, за работу!", "original.gif", project)
    }
}

internal fun motivateFail(project: Project) {
    invokeLater {
        MotivationShower.showMeme(MotivationType.Praise, "Крепись...", "original.gif", project)
    }
}

internal fun motivateSuccess(project: Project) {
    invokeLater {
        MotivationShower.showMeme(MotivationType.Praise, "Ура!", "original.gif", project)
    }
}