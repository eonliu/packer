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
        login(packerExt)
        configureSign(packerExt)
    }

    /**
     * 登录360
     * @param packerExt
     */
    private void login(PackerExtension packerExt) {
        def jiaguJarPath = packerExt.jiagu.jiaguJarPath
        def jiaguUserName = packerExt.jiagu.userName
        def jiaguPassword = packerExt.jiagu.password

        // 登录360加固
        def loginCommand = "java -jar ${jiaguJarPath}/jiagu.jar -login ${jiaguUserName} ${jiaguPassword}"
        def loginProcess = loginCommand.execute()
        def loginProcessOutput = new StringBuilder()
        def loginProcessError = new StringBuilder()
        loginProcess.waitForProcessOutput(loginProcessOutput, loginProcessError)
        logger.lifecycle(loginProcessOutput.toString())
        logger.error(loginProcessError.toString())
    }

    /**
     * 配置签名文件信息
     * @param packerExt
     */
    private void configureSign(PackerExtension packerExt) {
        def jiaguJarPath = packerExt.jiagu.jiaguJarPath
        def keystorePath = packerExt.jiagu.keystorePath
        def keystorePassword = packerExt.jiagu.keystorePassword
        def alias = packerExt.jiagu.alias
        def aliasPassword = packerExt.jiagu.aliasPassword

        // 配置签名文件信息
        def signCommand = "java -jar ${jiaguJarPath}/jiagu.jar -importsign $keystorePath $keystorePassword $alias $aliasPassword"
        def signProcess = signCommand.execute()
        def signProcessOutput = new StringBuilder()
        def signProcessError = new StringBuilder()
        signProcess.waitForProcessOutput(signProcessOutput, signProcessError)
        logger.lifecycle(signProcessOutput.toString())
        logger.error(signProcessError.toString())
    }
}