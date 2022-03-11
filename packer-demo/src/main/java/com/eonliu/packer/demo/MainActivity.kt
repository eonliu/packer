package com.eonliu.packer.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.meituan.android.walle.WalleChannelReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 通过美团Walle获取渠道信息
        val channel = WalleChannelReader.getChannel(this.applicationContext)
        Log.d("Packer", "Packer channel : $channel")
    }
}