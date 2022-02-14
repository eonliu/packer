# Packer

[![license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://github.com/eonliu/packer/blob/master/LICENSE)
[![Release Version](https://img.shields.io/badge/release-0.2.1-red.svg)](https://github.com/eonliu/packer/releases)

Packer设计初衷是通过Gradle Task进行自动打包、加固、并上传到指定FTP地址，供其他人员使用APK，优点是可以节省开发人员的打包时间、方便测试等同学使用最新apk、可以在FTP上查找历史版本APK。

## Feature

- [x] 支持variants。

- [x] 支持自定义ftp。

- [x] 支持自定义上传路径。

- [x] 支持自定义本地apk目录。

- [x] 对每一个variant都会生成对应的上传task。

- [x] 支持360加固。

- [x] 支持多渠道打包。

## Install

```groovy
dependencies {
    classpath "com.eonliu.packer:packer:1.0.0"
}
```

## Usage

在主工程中添加如下配置代码。

```groovy
apply plugin: 'com.eonliu.packer'


packer {

    jiagu {
        userName = '360加固用户名'
        password = '360加固密码'
        jiaguJarPath = '/***/360加固助手/jiagu' // 360加固文件夹路径
        channelsPath = '/***/channels.txt' // 多渠道配置文件，参考360加固多渠道配置模板
    }

    sign {
        keystorePath = '/***/sign.keystore' // 签名
        keystorePassword = '***' // 签名文件密码
        alias = '***' // 别名
        aliasPassword = '***' // 别名密码
    }

    ftp {
        ftpUserName = '***' // ftp用户名
        ftpPassword = 'toolsftp' // ftp密码
        ftpUrl = 'ftp://***/app/' // ftp地址
    }

}
```

Sync Gradle之后在`Android Studio->Gradle->app->packer`task组中可以查看并使用packer所有task。

`注：`因为360加固限制，需要将`360加固助手/jiagu`文件夹中的`java`文件夹及内容复制到工程的`app`路径下，否则执行task会失败。

`task说明：`以`upload`开头的task是未加固的，每次会上传一个apk。以`publish`开头的task会进行360加固、重新签名、多渠道打包，会上传多个apk，取决于`channels.txt`中配置多少个渠道。

## License

[![license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://github.com/eonliu/packer/blob/master/LICENSE)
