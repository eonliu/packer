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
                project.exec {
                    ExecSpec execSpec ->
                        executable 'cmd'
                        args '/c', command
                }
                println("> Packer: Windows Uploading...")
            } else {
                project.exec {
                    ExecSpec execSpec ->
                        executable 'bash'
                        args '-c', command
                }
                println("> Packer: Mac Uploading...")
            }
            closure.call()
        } catch (Exception e) {
            e.printStackTrace()
            println("> Packer: Upload error > ${e.message}")
        }
    }

}