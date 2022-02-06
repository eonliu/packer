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
        showConfig(packerExt)
        jiagu(packerExt)
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
        execAndLog(loginCommand)
    }

    /**
     * 查看当前加固服务配置
     * @param packerExt
     */
    private void showConfig(PackerExtension packerExt) {
        def jiaguJarPath = packerExt.jiagu.jiaguJarPath
        def showConfigCommand = "java -jar ${jiaguJarPath}/jiagu.jar -showconfig"
        execAndLog(showConfigCommand)
    }

    /**
     * 加固
     * @param packerExt
     */
    private void jiagu(PackerExtension packerExt) {
        def jiaguJarPath = packerExt.jiagu.jiaguJarPath
        def inputAPKPath = packerExt.jiagu.inputAPKPath
        def outputPath = packerExt.jiagu.outputPath
        def jiaguCommand = "java -jar ${jiaguJarPath}/jiagu.jar -jiagu $inputAPKPath $outputPath"
        execAndLog(jiaguCommand)
    }
}