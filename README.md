# packer

Packer是Android打包工具插件，支持360加固、多渠道打包、将APK上传到FTP等功能。

# 功能介绍

-[x] 支持variants。
-[x] 支持自定义ftp。
-[x] 支持自定义上传路径。
-[x] 对每一个variant都会生成对应的上传task。 -[] 支持360加固。 -[] 支持多渠道打包。

# 使用Packer

## 1.添加packer插件依赖

在项目根目录的```build.gradle```文件中添加packer插件依赖。

```groovy
dependencies {
    classpath "com.eonliu.packer:packer:0.1.1"
}
```

## 2.应用packer插件

在主工程的`build.gradle`中应用packer插件。

```groovy
plugins {
    id 'com.eonliu.packer'
}
```

## 3.配置packer说明

在主工程中添加如下配置代码。

```groovy
packer {
    ftpExtension {
        ftpUserName = 'ftp用户名'
        ftpPassword = 'ftp密码'
        ftpUrl = 'ftp:xxx.xxx.xxx/app/' // ftp上传地址
        ftpDir = 'apks' // apk保存文件夹，默认使用项目名（上传路径为：ftp:xxx.xxx.xxx/app/apks)
    }
}
```

## 4.packer上传task

在`Android Studio->Gradle->app->packer`task组中可以查看并使用packer所有task。