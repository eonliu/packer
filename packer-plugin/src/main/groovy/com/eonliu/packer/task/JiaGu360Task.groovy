package com.eonliu.packer.task

import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.tasks.TaskAction

/**
 * 360加固
 * @author Eon Liu
 */
class JiaGu360Task extends BaseTask {

    @TaskAction
    def jiaGu() {
        def packerExt = project.extensions.getByType(PackerExtension)
        def jiaguUserName = packerExt.jiagu.userName
        def jiaguPassword = packerExt.jiagu.password
        def jiaguJarPath = packerExt.jiagu.jiaguJarPath

        // 登录360加固
        def loginCommand = "java -jar ${jiaguJarPath}/jiagu.jar -login ${jiaguUserName} ${jiaguPassword}"
        def loginProcess = loginCommand.execute()
        def loginProcessOutput = new StringBuilder()
        def loginProcessError = new StringBuilder()
        loginProcess.waitForProcessOutput(loginProcessOutput, loginProcessError)
        logger.lifecycle(loginProcessOutput.toString())
        logger.error(loginProcessError.toString())
    }

}