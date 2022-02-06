package com.eonliu.packer.task

import com.android.build.gradle.AppExtension
import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Project

/**
 * 上传apk到ftp
 * @author Eon Liu
 */
class UploadApkTask extends BaseTask {

    UploadApkTask() {
        super()
        createUploadApkToFtpTask(project)
    }

    /**
     * 动态创建上传apk到ftp的task
     * @param project
     */
    void createUploadApkToFtpTask(Project project) {
        project.afterEvaluate {
            def appExt = project.extensions.getByType(AppExtension)
            appExt.applicationVariants.forEach { variant ->
                def variantName = variant.name.capitalize()
                // 任务名
                def taskName = "upload" + variantName + "Apk"
                project.tasks.create(taskName) {
                    // 依赖assembleRelease/assembleDebug
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
                        project.logger.lifecycle("> Packer: apk directory is : $apkDirectory")
                        new File(apkDirectory).list().each {
                            if (it.endsWith(".apk")) {
                                def fileUrl = apkDirectory + it
                                def ftpUrl = ""
                                def ftpDir = project.getRootProject().name
                                if (packerExt.ftp.ftpUrl != null) {
                                    ftpUrl = packerExt.ftp.ftpUrl
                                }
                                if (packerExt.ftp.ftpDir != null) {
                                    ftpDir = packerExt.ftp.ftpDir
                                }

                                def realFtpUrl = ftpUrl + ftpDir + "/" + variantName + "/v" + appExt.defaultConfig.versionName + "/"

                                // 上传文件命令(如果目录不存在自动创建）
                                def command = "curl -u $ftpUserName:$ftpPwd -T $fileUrl $realFtpUrl --ftp-create-dirs"
                                execAndLog(command)
                                project.logger.lifecycle("> Packer: ftp路径：" + realFtpUrl)
//                            Util.exec(project, command) {
//                                project.logger.lifecycle("> Packer: ftp路径：" + realFtpUrl)
//                                project.logger.lifecycle("> Packer: 文件上传成功")
//                            }
                            }
                        }
                    }
                }
            }
        }
    }
}