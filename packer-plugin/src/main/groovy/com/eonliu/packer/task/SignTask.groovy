package com.eonliu.packer.task

import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Project

/**
 * apk签名
 * @author Eon Liu
 */
class SignTask extends BaseTask {

    static void sign(Project project) {
        def packerExt = project.extensions.getByType(PackerExtension)


    }

}