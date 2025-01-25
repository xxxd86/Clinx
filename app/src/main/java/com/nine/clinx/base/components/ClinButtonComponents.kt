package com.nine.clinx.base.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.base.data.UIEvent
import com.nine.clinx.base.model.DynamicViewModel

//各种Button特殊样式
@Composable
fun DynamicButton(viewModel: DynamicViewModel, component: UIComponent) {
    val text = component.properties["text"] as? String ?: "Click Me"
    val padding = component.properties["padding"] as? Int ?: 16
    val color = component.properties["color"] as? String ?: "#000000"

    Button(
        onClick = {
            viewModel.triggerEvent(UIEvent.ButtonClicked(component.id))
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                android.graphics.Color.parseColor(
                    color
                )
            )
        ),
        modifier = Modifier.padding(padding.dp)
    ) {
        Text(text)
    }
}

