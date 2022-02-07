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

    void execAndLog(GString command) {
        def process = command.execute()
        def processOutput = new StringBuilder()
        def processError = new StringBuilder()
        process.waitForProcessOutput(processOutput, processError)
        logger.lifecycle(processOutput.toString())
        logger.error(processError.toString())
    }
}