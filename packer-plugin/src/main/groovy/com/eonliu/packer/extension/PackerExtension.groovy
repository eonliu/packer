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
     * 加固相关配置
     */
    JiaGuExtension jiagu = new JiaGuExtension()
    /**
     * FTP相关配置
     */
    FtpExtension ftp = new FtpExtension()
    /**
     * 签名配置
     */
    SignExtension sign = new SignExtension()

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

    void sign(Action<FtpExtension> action) {
        action.execute(sign)
    }

    void sign(Closure c) {
        ConfigureUtil.configure(c, sign)
    }
}