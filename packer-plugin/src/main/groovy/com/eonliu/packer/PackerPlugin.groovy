package com.eonliu.packer

import com.eonliu.packer.extension.PackerExtension
import com.eonliu.packer.task.JiaguBy360Task
import com.eonliu.packer.task.UploadApkTask
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

        project.tasks.create("uploadJiaguApk", JiaguBy360Task)
        project.tasks.create("uploadApk", UploadApkTask)
    }

}