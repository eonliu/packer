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

                                project.logger.lifecycle("> Packer: jiagu inputAPKPath is : $inputAPKPath")

                                def jiaguJarPath = packerExt.jiagu.jiaguJarPath
                                project.logger.lifecycle("> Packer: jiagu jiaguJarPath is : $jiaguJarPath")
                                if (jiaguJarPath == null || jiaguJarPath.isEmpty()) return
                                def jiaguUserName = packerExt.jiagu.userName
                                def jiaguPassword = packerExt.jiagu.password

                                def jiaguJarCommand = "${jiaguJarPath}/java/bin/java -jar ${jiaguJarPath}/jiagu.jar"

                                // 登录360加固
                                def loginCommand = "$jiaguJarCommand -login ${jiaguUserName} ${jiaguPassword}"
                                execAndLog(loginCommand)
                                // 配置加固选项
                                def configCommand = "$jiaguJarCommand -config "
                                execAndLog(configCommand)
                                // 配置多渠道信息，txt格式
                                def channelsCommand = "$jiaguJarCommand -importmulpkg ${packerExt.jiagu.channelsPath}"
                                execAndLog(channelsCommand)
                                // 查看多渠道信息
                                def showChannelsCommand = "$jiaguJarCommand -showmulpkg"
                                execAndLog(showChannelsCommand)
                                // 设置签名
                                def signConfigCommand = "$jiaguJarCommand -importsign ${packerExt.sign.keystorePath} ${packerExt.sign.keystorePassword} ${packerExt.sign.alias} ${packerExt.sign.aliasPassword}"
                                execAndLog(signConfigCommand)
                                // 查看签名信息
                                def showSignCommand = "$jiaguJarCommand -showsign"
                                execAndLog(showSignCommand)
                                // 查看当前加固服务配置
                                def showConfigCommand = "$jiaguJarCommand -showconfig"
                                execAndLog(showConfigCommand)
                                def outputPath = "${project.projectDir}/outputs/apk"
                                def outputDir = new File(outputPath)
                                if (!outputDir.exists()) {
                                    outputDir.mkdirs()
                                }
                                project.logger.lifecycle("> Packer: jiagu outputPath is : $outputPath")
                                def jiaguCommand = "$jiaguJarCommand -jiagu $inputAPKPath $outputPath -autosign -automulpkg"
                                execAndLog(jiaguCommand)
                            }
                        }
                    }
                }
            }
        }
    }
}