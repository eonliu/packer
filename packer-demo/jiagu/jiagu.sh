echo ==========================Packer==========================
output_apk_path=`pwd`/output/$username/

if [[ `uname` == 'Darwin' ]]; then
  jiagu_dir=~/jiagu
  echo "Mac OS"
fi

username=$1
password=$2

# 进入360加固目录
cd $jiagu_dir || exit
echo 360加固工具路径 : $(pwd)

# 登录360加固
./java/bin/java -jar jiagu.jar -version
./java/bin/java -jar jiagu.jar -login $username $password
# 签名配置
keystore_path=$5
keystore_password=$6
alias=$7
alias_password=$8
./java/bin/java -jar jiagu.jar -importsign $keystore_path $keystore_password $alias $alias_password
echo 签名文件路径 : $keystore_path
./java/bin/java -jar jiagu.jar -showsign

# 渠道配置
mulpkg_path=$4
./java/bin/java -jar jiagu.jar -importmulpkg $mulpkg_path
./java/bin/java -jar jiagu.jar -showmulpkg
echo 多渠道配置文件路径 : $mulpkg_path
# 加固服务配置
./java/bin/java -jar jiagu.jar -config
./java/bin/java -jar jiagu.jar -showconfig

input_apk_path=$3
echo apk路径: $input_apk_path

echo apk输出路径: $output_apk_path
#清空文件夹
rm -rf * $output_apk_path
mkdir $output_apk_path

# 加固apk(自动重新签名、自动打多渠道包)
./java/bin/java -jar jiagu.jar -jiagu $input_apk_path $output_apk_path -autosign -automulpkg
echo ==========================================================