package com.eonliu.packer

import com.eonliu.packer.extension.PackerExtension
import com.eonliu.packer.task.JiaguBy360Task
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

        // step1:加固任务
        project.tasks.create("jiaguBy360", JiaguBy360Task)
        // step2:重新签名
//            SignTask.sign(project)
        // step3:多渠道打包
//            ChannelTask.channel(project)
        // step4:上传ftp
//        project.tasks.add("uploadApk", UploadApkTask)
    }

}