package com.github.berezhkoe.kindmotivator.listeners

import com.github.berezhkoe.kindmotivator.notification.MotivationShower
import com.github.berezhkoe.kindmotivator.notification.MotivationType
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager

class KindSqlCreatedListener : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        if (FileDocumentManager.getInstance().getFile(event.editor.document)?.extension == "sql") {
            MotivationShower.showMeme(MotivationType.Sql, "SQL 😍", "Барашев.jpg", event.editor.project!!)
        }
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        if (FileDocumentManager.getInstance().getFile(event.editor.document)?.extension == "sql") {
            MotivationShower.showMeme(MotivationType.Sql, "SQL", "barashev-big-photo.jpg", event.editor.project!!)
        }
    }
}