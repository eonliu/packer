package com.eonliu.packer.extension

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil

/**
 * 配置选项
 * @author Eon Liu
 */
class PackerExtension {

    /**
     * apk路径，默认是projectDir/build/outputs/apk/variantDir/
     */
    String apkDirectory
    /**
     * 360加固相关配置
     */
    JiaGu360Extension jiagu = new JiaGu360Extension()
    /**
     * FTP相关配置
     */
    FtpExtension ftp = new FtpExtension()

    void jiagu(Action<FtpExtension> action) {
        action.execute(jiagu)
    }

    void jiagu(Closure c) {
        ConfigureUtil.configure(c, jiagu)
    }

    void ftp(Action<FtpExtension> action) {
        action.execute(ftp)
    }

    void ftp(Closure c) {
        ConfigureUtil.configure(c, ftp)
    }
}