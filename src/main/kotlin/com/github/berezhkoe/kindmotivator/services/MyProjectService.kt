package com.github.berezhkoe.kindmotivator.services

import com.intellij.openapi.project.Project
import com.github.berezhkoe.kindmotivator.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
