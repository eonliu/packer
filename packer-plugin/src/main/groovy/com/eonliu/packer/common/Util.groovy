package com.eonliu.packer.common

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecSpec

/**
 * @author Eon Liu
 */
class Util {

    /**
     * 执行外部命令
     * @param project
     * @param command
     */
    static void exec(Project project, String command, Closure closure) {
        try {
            if (OperatingSystem.current().isWindows()) {
                project.logger.lifecycle("> Packer: Windows Uploading...")
                project.exec {
                    ExecSpec execSpec ->
                        executable 'cmd'
                        args '/c', command
                }
            } else {
                project.logger.lifecycle("> Packer: Mac Uploading...")
                project.exec {
                    ExecSpec execSpec ->
                        executable 'bash'
                        args '-c', command
                }
            }
            closure.call()
        } catch (Exception e) {
            e.printStackTrace()
            project.logger.lifecycle("> Packer: Upload error > ${e.message}")
        }
    }

}