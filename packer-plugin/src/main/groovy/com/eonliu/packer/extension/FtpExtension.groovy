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
     * 加固包将上传到指定的ftpUrl的publishDir路径中
     */
    String publishDir
    /**
     * 未加固包将上传到指定的ftpUrl的uploadDir路径中
     */
    String uploadDir
    /**
     * 是否自动创建文件路径。
     * 默认会创建 projectName/versionName/ 目录，
     * 如果为false，则直接使用ftpUrl
     */
    boolean autoCreateDir
    /**
     * 是否上传build/outputs/mapping文件，默认上传
     */
    boolean uploadMapping = true
    /**
     * 是否上传build/outputs/logs，默认上传
     */
    boolean uploadLogs = true
    /**
     * 是否上传build/outputs/sdk-dependencies，默认上传
     */
    boolean uploadSdkDependencies = true
}