package com.eonliu.packer.task

import com.android.build.gradle.AppExtension
import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Project

/**
 * 360加固
 * @author Eon Liu
 */
class JiaguBy360Task extends BaseTask {

    /**
     * 登录360并进行加固
     * @param packerExt
     */
    static void createTasks(Project project) {
        project.afterEvaluate {
            def appExt = project.extensions.getByType(AppExtension)
            appExt.applicationVariants.forEach { variant ->
                def variantName = variant.name.capitalize()
                // 任务名
                def taskName = "publish" + variantName + "Apks"
                project.tasks.create(taskName) {
                    def dependsTask = "assemble" + variantName
                    dependsOn(dependsTask)
                    // 指定任务组
                    group = "packer"
                    doLast {
                        def packerExt = project.extensions.getByType(PackerExtension)
                        def ftpUserName = packerExt.ftp.ftpUserName
                        def ftpPwd = packerExt.ftp.ftpPassword
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
                                execAndLog(project, loginCommand)
                                // 配置加固选项
                                def configCommand = "$jiaguJarCommand -config "
                                execAndLog(project, configCommand)
                                // 配置多渠道信息，txt格式
                                def channelsCommand = "$jiaguJarCommand -importmulpkg ${packerExt.jiagu.channelsPath}"
                                execAndLog(project, channelsCommand)
                                // 查看多渠道信息
                                def showChannelsCommand = "$jiaguJarCommand -showmulpkg"
                                execAndLog(project, showChannelsCommand)
                                // 设置签名
                                def signConfigCommand = "$jiaguJarCommand -importsign ${packerExt.sign.keystorePath} ${packerExt.sign.keystorePassword} ${packerExt.sign.alias} ${packerExt.sign.aliasPassword}"
                                execAndLog(project, signConfigCommand)
                                // 查看签名信息
                                def showSignCommand = "$jiaguJarCommand -showsign"
                                execAndLog(project, showSignCommand)
                                // 查看当前加固服务配置
                                def showConfigCommand = "$jiaguJarCommand -showconfig"
                                execAndLog(project, showConfigCommand)
                                def outputPath = "${project.projectDir}/build/packer/outputs/apk"
                                def outputDir = new File(outputPath)
                                if (outputDir.exists()) {
                                    outputDir.deleteDir()
                                    project.logger.lifecycle("> Packer: delete packer/outputs/apk dir")
                                }
                                outputDir.mkdirs()
                                project.logger.lifecycle("> Packer: jiagu outputPath is : $outputPath")
                                def jiaguCommand = "$jiaguJarCommand -jiagu $inputAPKPath $outputPath -autosign -automulpkg"
                                execAndLog(project, jiaguCommand)

                                new File(outputPath).list().each {
                                    if (it.endsWith(".apk")) {
                                        def fileUrl = outputPath + "/" + it
                                        def ftpUrl = ""
                                        def ftpDir = project.getRootProject().name
                                        if (packerExt.ftp.ftpUrl != null) {
                                            ftpUrl = packerExt.ftp.ftpUrl
                                        }
                                        if (packerExt.ftp.ftpDir != null) {
                                            ftpDir = packerExt.ftp.ftpDir
                                        }

                                        realFtpUrl = ftpUrl + ftpDir + "/" + variantName + "/v" + appExt.defaultConfig.versionName + "/"

                                        // 上传文件命令(如果目录不存在自动创建）
                                        def command = "curl -u $ftpUserName:$ftpPwd -T $fileUrl $realFtpUrl --ftp-create-dirs"
                                        execAndLog(project, command)
                                        project.logger.lifecycle("> Packer: ftp路径：" + realFtpUrl)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}