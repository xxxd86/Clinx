package com.nine.clinx.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.nine.clinx.flowkit.FlowBusInitializer
import com.nine.clinx.store.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClingApplication : Application() {
    companion object {
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context

        //App是否是冷启动
        var isColdStart = false
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        isColdStart = true
        DataStoreUtils.init(this)
        FlowBusInitializer.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // Dex 分包
        MultiDex.install(this)
    }

}