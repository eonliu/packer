package com.eonliu.packer.extension

/**
 * FTP相关配置
 * @author Eon Liu
 */
class FtpExtension {
    /**
     * FTP地址（包含端口号）
     */
    String ftpUrl
    /**
     * 登录FTP的用户名
     */
    String ftpUserName
    /**
     * 登录FTP的密码
     */
    String ftpPassword
    /**
     * 文件将上传到指定的ftpUrl的dir路径中
     */
    String ftpDir
    /**
     * 是否自动创建文件路径。只针对upload**Task，publish不受此参数影响。
     * 默认会创建 projectName/variantName/versionName/ 目录，
     * 如果为false，则直接使用ftpUrl
     */
    boolean autoCreateDir
}