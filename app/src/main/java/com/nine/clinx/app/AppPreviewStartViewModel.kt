package com.nine.clinx.app

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * case1.App 冷启动预加载问题
 * 冷启动 Splash期间加载适量数据 case 尽量为非可见数据
 * 1.处理外置插件初始化
 * 2。创建基础Flow,如  Toast,Navigator
 */
@HiltViewModel
class AppPreviewStartViewModel @Inject internal constructor() : ViewModel() {
    fun observeAppBaseEvents() {

    }

}