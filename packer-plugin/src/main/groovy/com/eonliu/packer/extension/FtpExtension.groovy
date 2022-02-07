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
}