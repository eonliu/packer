package com.eonliu.packer.demo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.meituan.android.walle.WalleChannelReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 通过美团Walle获取渠道信息
        val channel = WalleChannelReader.getChannel(this.applicationContext)
        findViewById<TextView>(R.id.tvChannel).text = "渠道为：$channel"
        Log.d("Packer", "Packer channel : $channel")
    }
}