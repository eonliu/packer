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
     * 要加固APK的路径
     */
    String inputAPKPath
    /**
     * 加固后apk的输出路径
     */
    String outputPath
}