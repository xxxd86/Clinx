package com.nine.clinx.base.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.base.data.UIEvent
import com.nine.clinx.base.model.DynamicViewModel

//引入Savable能力
@Composable
fun DynamicText(viewModel: DynamicViewModel, component: UIComponent, savable: Boolean = false) {
    val text = component.properties["text"] as? String ?: "Hello, World!"
    val color = component.properties["color"] as? String ?: "#000000"
    var editedText by remember { mutableStateOf(text) }
    Text(
        text = editedText.takeIf { savable } ?: text,
        color = Color(android.graphics.Color.parseColor(color)),
        modifier = Modifier.padding(16.dp)
    )

    // 触发文本变化事件
    TextField(
        value = editedText.takeIf { savable } ?: text,
        onValueChange = {
            editedText = it.takeIf { savable } ?: ""
            viewModel.triggerEvent(UIEvent.TextChanged(component.id, it))
        },
        modifier = Modifier.padding(16.dp)
    )
}