package com.eonliu.packer.extension

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil

/**
 * 配置选项
 * @author Eon Liu
 */
class PackerExtension {

    /**
     * FTP相关配置
     */
    FtpExtension ftpExtension = new FtpExtension()

    void ftpExtension(Action<FtpExtension> action) {
        action.execute(ftpExtension)
    }

    void ftpExtension(Closure c) {
        ConfigureUtil.configure(c, ftpExtension)
    }
}