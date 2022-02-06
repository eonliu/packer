package com.eonliu.packer.task

import com.eonliu.packer.extension.PackerExtension
import org.gradle.api.Project

/**
 * walle 多渠道打包
 * @author Eon Liu
 */
class ChannelTask extends BaseTask {

    static void channel(Project project) {
        def packerExt = project.extensions.getByType(PackerExtension)

        def walleJar = packerExt.walle.walleJarPath

        if (walleJar == null || walleJar.isEmpty()) {
            project.logger.lifecycle("请配置walle")
        } else {
            def command = "java -jar $walleJar -h"
            execAndLog(command)
        }
    }


}