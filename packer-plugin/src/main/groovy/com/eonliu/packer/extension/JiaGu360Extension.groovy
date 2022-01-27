package com.eonliu.packer.extension

/**
 * 360加固相关配置
 *
 * @author Eon Liu
 */
class JiaGu360Extension {

    /**
     * 360加固登录用户名
     */
    String userName
    /**
     * 360加固登录密码
     */
    String password
    /**
     * 360加固加固目录,"jiagu"路径
     */
    String jiaguJarPath
    /**
     * 签名文件路径
     */
    String keystorePath
    /**
     * 签名文件密码
     */
    String keystorePassword
    /**
     * 签名文件alias
     */
    String alias
    /**
     * 签名文件alias密码
     */
    String aliasPassword
}