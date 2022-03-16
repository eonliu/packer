#!/bin/bash

echo "╔══════════════════════════════════════════════════════╗"
echo "║                                                      ║"
echo "║            ______          _                         ║"
echo "║            | ___ \        | |                        ║"
echo "║            | |_/ /_ _  ___| | _____ _ __             ║"
echo "║            |  __/ _  |/ __| |/ / _ \ '__|            ║"
echo "║            | | | (_| | (__|   <  __/ |               ║"
echo "║            \_|  \__,_|\___|_|\_\___|_|               ║"
echo "║                                                      ║"
echo "╚══════════════════════════════════════════════════════╝"

JIAGU_DIR=$1 							    # 360加固工具路径
USERNAME=$2 								  # 360账号
PASSWORD=$3 								  # 360密码
INPUT_APK_PATH=$4 						# 原始apk路径
MULPKG_PATH=$5 								# 多渠道配置文件路径
KEYSTORE_PATH=$6 							# 签名文件路径
KEYSTORE_PASSWORD=$7 					# 签名密码
KEYSTORE_ALIAS=$8 						# 签名别名
KEYSTORE_ALIAS_PASSWORD=$9 		# 签名别名密码
USE_WALLE=${10} 			  			# 实收使用walle打多渠道包
WALLE_JAR_PATH=${11}          # walle jar 路径
ZIPALIGN=${12}                # android sdk zipalign 路径
APKSIGNER=${13}               # android sdk apksigner 路径

echo "Packer > walle jar path : $WALLE_JAR_PATH, 是否使用Walle打多渠道包：$USE_WALLE"

echo "Packer > zipalign path: $ZIPALIGN"
echo "Packer > apksigner path: $APKSIGNER"

function login360(){
	./java/bin/java -jar jiagu.jar -version
	./java/bin/java -jar jiagu.jar -login "$USERNAME" "$PASSWORD"
}

function configSign() {
	if [ -a "$KEYSTORE_PATH"  ]; then
		./java/bin/java -jar jiagu.jar -importsign "$KEYSTORE_PATH" "$KEYSTORE_PASSWORD" "$KEYSTORE_ALIAS" "$KEYSTORE_ALIAS_PASSWORD"
	  echo "Packer > 签名文件路径 : $KEYSTORE_PATH"
	  ./java/bin/java -jar jiagu.jar -showsign
	fi
}

function configMulpkg() {
	./java/bin/java -jar jiagu.jar -importmulpkg "$MULPKG_PATH"
	echo "Packer > 多渠道配置文件路径 : $MULPKG_PATH"
	./java/bin/java -jar jiagu.jar -showmulpkg
}

function configJiagu() {
	# 加固服务配置
	./java/bin/java -jar jiagu.jar -config
	./java/bin/java -jar jiagu.jar -showconfig
}

function jiaguBy360() {
	echo "Packer > 源apk路径: $INPUT_APK_PATH"
	# 加固后apk输出路径
	OUTPUT_APK_PATH="$JIAGU_DIR/output/$USERNAME/" 		
	
	# 清空之前加固的apk，防止重复上传
	if [[ -d $OUTPUT_APK_PATH ]]; then
		rm -rf "$OUTPUT_APK_PATH"
	fi

	mkdir "$OUTPUT_APK_PATH"

	echo "Packer > 加固后apk输出路径: $OUTPUT_APK_PATH"
	echo "Packer > 加固中..."
	
	# 加固apk(自动重新签名、自动打多渠道包)
	if [ "$USE_WALLE" == "true" ]; then
	  ./java/bin/java -jar jiagu.jar -jiagu "$INPUT_APK_PATH" "$OUTPUT_APK_PATH"
	else
	  echo "Packer > 使用360打多渠道包"
	  ./java/bin/java -jar jiagu.jar -jiagu "$INPUT_APK_PATH" "$OUTPUT_APK_PATH" -autosign -automulpkg
	fi
}

function mulpkgBy360() {
	# 配置签名
	configSign
	# 配置多渠道信息
	configMulpkg
	# 配置加固服务
	configJiagu
	# 加固
	jiaguBy360
}

function mulpkgByWalle() {
  # 配置签名
	configSign
	# 配置加固服务
	configJiagu
	# 加固
	jiaguBy360

	# 使用walle打多渠道包
  for file  in $(ls "$OUTPUT_APK_PATH"); do
    if [ -f "$OUTPUT_APK_PATH/$file" ] && [[ "${file##*.}"x = "apk"x ]]; then
      ORIGIN_APK="$OUTPUT_APK_PATH$file"
      ZIPPED_APK="${OUTPUT_APK_PATH}zipped.apk"
      # zipalign
	    $ZIPALIGN -v 4 "$ORIGIN_APK" "$ZIPPED_APK"
      # rm -f "$ZIPPED_APK"
	    # sign v2
	    SIGNED_APK="${OUTPUT_APK_PATH}signed.apk"
      $APKSIGNER sign --ks "$KEYSTORE_PATH"  --ks-key-alias "$KEYSTORE_ALIAS"  --ks-pass pass:"$KEYSTORE_PASSWORD"  --key-pass pass:"$KEYSTORE_ALIAS_PASSWORD" --out "$SIGNED_APK" "$ZIPPED_APK"
      # walle
      echo "Packer > 使用Walle打多渠道包，源apk路径 >>> $SIGNED_APK"
      java -jar "$WALLE_JAR_PATH" batch -f "$MULPKG_PATH" "$SIGNED_APK"
    fi
  done
}

if [[ -d $JIAGU_DIR ]] && cd "$JIAGU_DIR"; then
	echo "Packer > 360加固工具路径 : $PWD"
	
	login360 || exit 
	
	if $USE_WALLE; then
		mulpkgByWalle
	else
		mulpkgBy360
	fi
else
	echo "Packer > 360加固工具路径错误：$JIAGU_DIR"
fi