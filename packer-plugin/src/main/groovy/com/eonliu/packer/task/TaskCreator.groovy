package com.eonliu.packer.task

import com.android.build.gradle.AppExtension
import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Project
import org.gradle.process.ExecSpec

/**
 * @author Eon Liu
 */
class TaskCreator {

    static final TASK_PREFIX_PUBLISH = "publish"
    static final TASK_PREFIX_UPLOAD = "upload"
    static final TASK_SUFFIX_APK = "Apk"
    static final TASK_SUFFIX_APKS = "Apks"

    /**
     * 创建task
     * @param project
     * @param isPublish true对apk进行加固、重新签名、多渠道打包，false不进行加固。
     */
    static void createTasks(Project project, boolean isPublish) {
        project.afterEvaluate {
            def appExt = project.extensions.getByType(AppExtension)
            // 遍历variant
            appExt.applicationVariants.forEach { variant ->
                def variantName = variant.name.capitalize()
                def taskPrefix = isPublish ? TASK_PREFIX_PUBLISH : TASK_PREFIX_UPLOAD
                def taskSuffix = isPublish ? TASK_SUFFIX_APKS : TASK_SUFFIX_APK
                // 任务名
                def taskName = taskPrefix + variantName + taskSuffix
                // 创建task
                project.tasks.create(taskName) {
                    // 依赖assembleRelease/assembleDebug...
                    def dependsTask = "assemble" + variantName
                    dependsOn(dependsTask)
                    // 指定任务组
                    group = "packer"
                    doLast {
                        def packerExt = project.extensions.getByType(PackerExtension)
                        def apkDirectory = packerExt.apkDirectory
                        if (apkDirectory == null || apkDirectory == "") {
                            apkDirectory = "${project.projectDir}/build/outputs/apk/" + variant.getDirName() + "/"
                            project.logger.lifecycle("> Packer: apk directory is : $apkDirectory")
                            new File(apkDirectory).list().each {
                                if (it.endsWith(".apk")) {
                                    def apkFilePath = apkDirectory + it

                                    if (isPublish) {
                                        // 加固包处理
                                        handleJiaguApk(project, apkFilePath)
                                    } else {
                                        // 不加固包处理
                                        uploadApk(project, isPublish, apkFilePath)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static void execAndLog(Project project, GString command) {
        def process = command.execute()
        def processOutput = new StringBuilder()
        def processError = new StringBuilder()
        process.waitForProcessOutput(processOutput, processError)
        project.logger.lifecycle(processOutput.toString())
        project.logger.error(processError.toString())
    }

    static void handleJiaguApk(Project project, GString apkFilePath) {
        def packerExt = project.extensions.getByType(PackerExtension)
        def jiaguUserName = packerExt.jiagu.userName
        def jiaguPassword = packerExt.jiagu.password
        def jiaguChannelsPath = packerExt.jiagu.channelsPath
        def keystorePath = packerExt.sign.keystorePath
        def keystorePassword = packerExt.sign.keystorePassword
        def alias = packerExt.sign.alias
        def aliasPassword = packerExt.sign.aliasPassword
        def out = new ByteArrayOutputStream()
        // jiagu.sh apk路径 apk输出路径
        def cmd = "./jiagu/jiagu.sh $jiaguUserName $jiaguPassword $apkFilePath $jiaguChannelsPath $keystorePath $keystorePassword $alias $aliasPassword"
        project.exec {
            ExecSpec execSpec ->
                executable 'bash'
                args '-c', cmd
                standardOutput = out
        }
        println(out.toString())

        def outputPath = "${project.projectDir}/jiagu/output/$jiaguUserName/"

        project.logger.lifecycle("> Packer: 加固apk输出路径：$outputPath")

        new File(outputPath).list().each {
            if (it.endsWith(".apk")) {
                uploadApk(project, true, outputPath + it)
            }
        }
    }

    static void uploadApk(Project project, boolean isPublish, String apkFilePath) {
        def appExt = project.extensions.getByType(AppExtension)
        def packerExt = project.extensions.getByType(PackerExtension)
        def ftpUserName = packerExt.ftp.ftpUserName
        def ftpPwd = packerExt.ftp.ftpPassword
        def ftpUrl = ""
        if (packerExt.ftp.ftpUrl != null) {
            ftpUrl = packerExt.ftp.ftpUrl
        }
        def rootDir = project.getRootProject().name
        if (isPublish) {
            if (packerExt.ftp.publishDir != null) {
                rootDir = packerExt.ftp.publishDir
            }
        } else {
            if (packerExt.ftp.uploadDir != null) {
                rootDir = packerExt.ftp.uploadDir
            }
        }
        def realFtpUrl
        if (packerExt.ftp.autoCreateDir) {
            realFtpUrl = ftpUrl + rootDir + "/v" + appExt.defaultConfig.versionName + "/"
        } else {
            realFtpUrl = ftpUrl
        }

        project.logger.lifecycle("> Packer: 上传加固apk：" + apkFilePath)

        // 上传文件命令(如果目录不存在自动创建）
        def command = "curl -u $ftpUserName:$ftpPwd -T $apkFilePath $realFtpUrl --ftp-create-dirs"
        execAndLog(project, command)
        project.logger.lifecycle("> Packer: ftp路径：" + realFtpUrl)
    }
}