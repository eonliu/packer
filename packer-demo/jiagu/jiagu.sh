echo ==========================Packer==========================

jiagu_dir=/Users/ooyao/Development/360jiagu/jiagu
username=17611752636
password=android123
keystore_path=/Users/ooyao/Development/workspace/packer/packer-demo/packer-demo.keystore
keystore_password=packer123
alias=packer-demo
alias_password=packer123
mulpkg_path=/Users/ooyao/Development/workspace/packer/packer-demo/channels.txt
input_apk_path=/Users/ooyao/Development/360jiagu/jiagu/test.apk
ouput_apk_path=/Users/ooyao/Development/360jiagu/jiagu/output/


# 进入360加固目录
cd $jiagu_dir

echo 360加固工具路径 : `pwd`

# 登录360加固
./java/bin/java -jar jiagu.jar -version
./java/bin/java -jar jiagu.jar -login $username $password
# 签名配置
./java/bin/java -jar jiagu.jar -importsign $keystore_path $keystore_password $alias $alias_password
echo 签名文件路径 : $keystore_path
./java/bin/java -jar jiagu.jar -showsign
# 渠道配置
./java/bin/java -jar jiagu.jar -importmulpkg $mulpkg_path
echo 多渠道配置文件路径 : $mulpkg_path
./java/bin/java -jar jiagu.jar -showmulpkg
# 加固服务配置
./java/bin/java -jar jiagu.jar -config
./java/bin/java -jar jiagu.jar -showconfig
# 加固apk(自动重新签名、自动打多渠道包)
./java/bin/java -jar jiagu.jar -jiagu ./test.apk $ouput_apk_path -autosign -automulpkg
echo ==========================================================