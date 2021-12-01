package com.github.berezhkoe.kindmotivator.notification

import com.intellij.openapi.project.Project
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.streams.toList


object MotivationShower {
    fun showMeme(motivationType: MotivationType, title: String, motivationFileName: String, project: Project) {
        MemeNotificationService.getInstance().showMeme(
                title,
                MemeNotificationService.memePath(motivationType.pathBase, motivationFileName),
                project
        )
    }

    fun showRandomMeme(motivationType: MotivationType, title: String, project: Project) {
        val memesDirPath = MemeNotificationService.memePath(motivationType.pathBase)
        val memesDirUrl = MotivationShower::class.java.classLoader.getResource(memesDirPath.toString())!!

        val randomMemePath = FileSystems.newFileSystem(memesDirUrl.toURI(), emptyMap<String, Nothing>()).use {
            Files.list(it.getPath(memesDirPath.toString())).toList().randomOrNull()
        } ?: error("No memes of type $motivationType")

        showMeme(motivationType, title, randomMemePath.fileName.toString(), project)
    }
}

enum class MotivationType(val pathBase: String) {
    Praise("praise"), Rest("rest"), Support("support"), ForcePush("force_push"), Early("early"), Late("late")
}

