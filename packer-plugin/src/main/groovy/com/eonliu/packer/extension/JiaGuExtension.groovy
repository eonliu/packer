package com.eonliu.packer.extension

/**
 * 360加固相关配置
 *
 * @author Eon Liu
 */
class JiaGuExtension {

    /**
     * 360加固登录用户名
     */
    String userName
    /**
     * 360加固登录密码
     */
    String password
    /**
     * 多渠道配置文件路径，txt格式
     */
    String channelsPath
    /**
     * 360加固工具路径
     */
    String jiaguPath
    /**
     * 默认使用360打多渠道包，如果设为true则使用美团walle打多渠道包
     */
    Boolean useWalle
}