package com.nine.clinx.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.nine.clinx.base.components.DynamicButton
import com.nine.clinx.base.components.DynamicStaggerGrid
import com.nine.clinx.base.components.DynamicText
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.base.data.UIEvent
import com.nine.clinx.base.model.DynamicViewModel

@Composable
fun DynamicScreen(viewModel: DynamicViewModel) {
    // 观察 ViewModel 中的 UIState
    val uiState by viewModel.uiState.collectAsState()

    // 在组件首次加载时异步加载组件数据
    LaunchedEffect(Unit) {
        viewModel.loadComponents()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        uiState.components.forEach { component ->
            when (component.type) {
                "Button" -> DynamicButton(viewModel, component)
                "Text" -> DynamicText(viewModel, component)
                "CustomButton" -> DynamicCustomButton(viewModel, component)  // 渲染自定义按钮
                "Spacer" -> Spacer(modifier = Modifier
                    .height(0.dp)
                    .width(0.dp))
                "StaggerGrid"->DynamicStaggerGrid(viewModel,viewModel.projects.collectAsLazyPagingItems())
                else -> Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun DynamicCustomButton(viewModel: DynamicViewModel, component: UIComponent) {
    // 假设 CustomButton 有额外的属性，如 "icon" 或 "background"
    val text = component.properties["text"] as? String ?: "Custom Button"
    val icon = component.properties["icon"] as? String
    val color = component.properties["color"] as? String ?: "#000000"
    val backgroundColor = component.properties["backgroundColor"] as? String ?: "#FF5733"

    // 显示一个自定义的按钮
    Button(
        onClick = {
            // 触发自定义按钮点击事件
            viewModel.triggerEvent(UIEvent.ButtonClicked(component.id))
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                android.graphics.Color.parseColor(
                    color
                )
            )
        ),
        modifier = Modifier.padding(16.dp)
    ) {
        icon?.let {
            Icon(Icons.Filled.Add, contentDescription = null) // 使用传入的 icon
        }
        Text(text)
    }
}

@Composable
fun DynamicLazyColumn(viewModel: DynamicViewModel, component: UIComponent) {
    val text = component.properties["text"] as? String ?: "Custom Button"
    val icon = component.properties["icon"] as? String
    val color = component.properties["color"] as? String ?: "#000000"
    val backgroundColor = component.properties["backgroundColor"] as? String ?: "#FF5733"
    LaunchedEffect(Unit) {
        //加载Colum数据

    }
    LazyColumn {

    }
}


