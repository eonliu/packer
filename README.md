# Packer

Packer设计初衷是通过Gradle Task进行自动打包、加固、并上传到指定FTP地址，供其他人员使用APK，可以节省开发人员的打包时间，同时也方便测试等同学使用最新apk。

## 功能介绍



- [x] 支持variants。

- [x] 支持自定义ftp。

- [x] 支持自定义上传路径。

- [x] 对每一个variant都会生成对应的上传task。

- [ ] 支持360加固。

- [ ] 支持多渠道打包。

## Install

```groovy
dependencies {
    classpath "com.eonliu.packer:packer:0.1.1"
}
```

## Usage
在主工程中添加如下配置代码。

```groovy
apply plugin: 'com.eonliu.packer'

packer {
    ftpExtension {
        ftpUserName = 'ftp用户名'
        ftpPassword = 'ftp密码'
        ftpUrl = 'ftp:xxx.xxx.xxx/app/' // ftp上传地址
        ftpDir = 'apks' // apk保存文件夹，默认使用项目名（上传路径为：ftp:xxx.xxx.xxx/app/apks)
    }
}
```

sync Gradle之后在`Android Studio->Gradle->app->packer`task组中可以查看并使用packer所有task。

## License

```text
Copyright 2022 Liu Yong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
