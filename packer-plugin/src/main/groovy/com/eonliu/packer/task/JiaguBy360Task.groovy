package com.eonliu.packer.task

import com.android.build.gradle.AppExtension
import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.tasks.TaskAction

/**
 * 360加固
 * @author Eon Liu
 */
class JiaguBy360Task extends BaseTask {

    JiaguBy360Task() {
        super()
        loginAndJiagu()
    }

    @TaskAction
    def task() {

    }

    /**
     * 登录360并进行加固
     * @param packerExt
     */
    private void loginAndJiagu() {
        project.afterEvaluate {
            def appExt = project.extensions.getByType(AppExtension)
            appExt.applicationVariants.forEach { variant ->
                def variantName = variant.name.capitalize()
                // 任务名
                def taskName = "jiagu" + variantName + "Apk"
                project.tasks.create(taskName) {
                    def dependsTask = "assemble" + variantName
                    dependsOn(dependsTask)
                    // 指定任务组
                    group = "packer"
                    doLast {
                        def packerExt = project.extensions.getByType(PackerExtension)
                        def apkDirectory = packerExt.apkDirectory
                        if (apkDirectory == null || apkDirectory == "") {
                            apkDirectory = "${project.projectDir}/build/outputs/apk/" + variant.getDirName() + "/"
                        }
                        project.logger.lifecycle("> Packer: jiagu apk directory is : $apkDirectory")
                        new File(apkDirectory).list().each {
                            if (it.endsWith(".apk")) {
                                def inputAPKPath = apkDirectory + it

                                def jiaguJarPath = packerExt.jiagu.jiaguJarPath
                                if (jiaguJarPath == null || jiaguJarPath.isEmpty()) return
                                def jiaguUserName = packerExt.jiagu.userName
                                def jiaguPassword = packerExt.jiagu.password
                                // 登录360加固
                                def loginCommand = "java -jar ${jiaguJarPath}/jiagu.jar -login ${jiaguUserName} ${jiaguPassword}"
                                execAndLog(loginCommand)
                                // 查看当前加固服务配置
                                def showConfigCommand = "java -jar ${jiaguJarPath}/jiagu.jar -showconfig"
                                execAndLog(showConfigCommand)
                                def outputPath = "${project.projectDir}/outputs/apk"
                                new File(outputPath).mkdirs()
                                def jiaguCommand = "java -jar ${jiaguJarPath}/jiagu.jar -jiagu $inputAPKPath $outputPath"
                                execAndLog(jiaguCommand)
                            }
                        }
                    }
                }
            }
        }
    }
}