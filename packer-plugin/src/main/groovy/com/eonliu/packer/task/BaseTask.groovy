package com.eonliu.packer.task

import org.gradle.api.DefaultTask

/**
 * @author Eon Liu
 */
class BaseTask extends DefaultTask {

    String defaultGroup = "packer"

    BaseTask() {
        super()
        group defaultGroup
    }

}