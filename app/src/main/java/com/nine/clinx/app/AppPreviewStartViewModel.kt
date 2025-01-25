package com.nine.clinx.app

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.nine.clinx.constants.EventBus
import com.nine.clinx.flowkit.observeEvent
import com.nine.clinx.ui.widgets.toast.ToastModel
import com.nine.clinx.ui.widgets.toast.ToastUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * case1.App 冷启动预加载问题
 * 冷启动 Splash期间加载适量数据 case 尽量为非可见数据
 * 1.处理外置插件初始化
 * 2。创建基础Flow,如  Toast,Navigator
 */
@HiltViewModel
class AppPreviewStartViewModel @Inject internal constructor() : ViewModel() {
    //注册基础Module
    @Composable
    fun ObserveAppBaseEvents(activity:ComponentActivity) {
        val toastState = remember { ToastUIState() }
        LaunchedEffect(Unit) {
            /** toast */
            activity.let {
                activity.observeEvent(key = EventBus.ShowToast) {
                    activity.lifecycleScope.launch {
                        val data = it as ToastModel
                        toastState.show(data)
                    }
                }
            }
        }
    }

}