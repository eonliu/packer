package com.eonliu.packer

import com.android.build.gradle.AppExtension
import com.eonliu.packer.common.Util
import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 打包插件
 * @author Eon Liu
 */
class PackerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 创建packer扩展
        project.extensions.create("packer", PackerExtension)

        // 打包任务
//        project.tasks.create("pack", PackTask)

        project.afterEvaluate {
            createUploadApkToFtpTask(project)
        }
    }

    /**
     * 动态创建上传apk到ftp的task
     * @param project
     */
    static void createUploadApkToFtpTask(Project project) {
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

                    def ftpUserName = packerExt.ftpExtension.ftpUserName
                    def ftpPwd = packerExt.ftpExtension.ftpPassword
                    def apkDirectory = packerExt.apkDirectory
                    if (apkDirectory == null || apkDirectory == "") {
                        apkDirectory = "${project.projectDir}/build/outputs/apk/" + variant.getDirName() + "/"
                    }
                    println("> Packer: apk directory is : $apkDirectory")
                    new File(apkDirectory).list().each {
                        if (it.endsWith(".apk")) {
                            def fileUrl = apkDirectory + it
                            def ftpUrl = ""
                            def ftpDir = project.getRootProject().name
                            if (packerExt.ftpExtension.ftpUrl != null) {
                                ftpUrl = packerExt.ftpExtension.ftpUrl
                            }
                            if (packerExt.ftpExtension.ftpDir != null) {
                                ftpDir = packerExt.ftpExtension.ftpDir
                            }

                            def realFtpUrl = ftpUrl + ftpDir + "/" + variantName + "/v" + appExt.defaultConfig.versionName + "/"

                            // 上传文件命令(如果目录不存在自动创建）
                            def command = "curl -u $ftpUserName:$ftpPwd -T $fileUrl $realFtpUrl --ftp-create-dirs"
                            Util.exec(project, command) {
                                println("> Packer: ftp路径：" + realFtpUrl)
                                println("> Packer: 文件上传成功")
                            }
                        }
                    }
                }
            }
        }
    }

}