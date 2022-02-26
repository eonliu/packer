package com.eonliu.packer.task

import org.gradle.api.Project

/**
 * @author Eon Liu
 */
class BaseTask {

    static void execAndLog(Project project, GString command) {
        def process = command.execute()
        def processOutput = new StringBuilder()
        def processError = new StringBuilder()
        process.waitForProcessOutput(processOutput, processError)
        project.logger.lifecycle(processOutput.toString())
        project.logger.error(processError.toString())
    }
}