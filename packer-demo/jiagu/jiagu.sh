echo ==========================Packer==========================\n
jiagu_dir=$1
username=$2
password=$3

# 进入360加固目录
cd $jiagu_dir || exit
echo 360加固工具路径 : $(pwd)

# 登录360加固
./java/bin/java -jar jiagu.jar -version
./java/bin/java -jar jiagu.jar -login $username $password

# 是否使用walle打多渠道包
useWalle=$10

if(useWalle)
then
  echo "使用Walle打多渠道包..."

  input_apk_path=$3
  echo apk路径: $input_apk_path
  output_apk_path=$jiagu_dir/output/$username/
  echo apk输出路径: $output_apk_path
  #清空文件夹
  rm -rf $output_apk_path
  mkdir $output_apk_path

  echo "加固中..."

  # 加固apk
  ./java/bin/java -jar jiagu.jar -jiagu $input_apk_path $output_apk_path

  # 重新签名

  # 使用Walle打多渠道包

else
  echo "使用360服务打多渠道包..."
  # 签名配置
  keystore_path=$6
  keystore_password=$7
  alias=$8
  alias_password=$9
  ./java/bin/java -jar jiagu.jar -importsign $keystore_path $keystore_password $alias $alias_password
  echo 签名文件路径 : $keystore_path
  ./java/bin/java -jar jiagu.jar -showsign

  # 渠道配置
  mulpkg_path=$5
  ./java/bin/java -jar jiagu.jar -importmulpkg $mulpkg_path
  ./java/bin/java -jar jiagu.jar -showmulpkg
  echo 多渠道配置文件路径 : $mulpkg_path
  # 加固服务配置
  ./java/bin/java -jar jiagu.jar -config
  ./java/bin/java -jar jiagu.jar -showconfig

  input_apk_path=$3
  echo apk路径: $input_apk_path
  output_apk_path=$jiagu_dir/output/$username/
  echo apk输出路径: $output_apk_path
  #清空文件夹
  rm -rf $output_apk_path
  mkdir $output_apk_path

  echo "加固中..."

  # 加固apk(自动重新签名、自动打多渠道包)
  ./java/bin/java -jar jiagu.jar -jiagu $input_apk_path $output_apk_path -autosign -automulpkg
fi

echo ==========================================================\n