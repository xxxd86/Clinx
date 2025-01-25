package com.nine.clinx.base.data


data class UIState(
    val components: List<UIComponent>
)

// UI 交互 Intent
sealed class UIEvent {
    data class ButtonClicked(val componentId: String) : UIEvent()
    data class TextChanged(val componentId: String, val newText: String) : UIEvent()
}

interface UIComponent {
    val id: String
    val type: String
    val properties: Map<String, Any>  // 每个组件的属性，例如文本、颜色、尺寸等
}

data class CustomButtonState(
    override val id: String,
    override val type: String = "CustomButton",
    override val properties: Map<String, Any>
) : UIComponent

// 手动实现 UIComponent 的副本方法
class OriginComponentState(
    override val id: String,
    override val type: String,
    override val properties: Map<String, Any>
) : UIComponent {
    fun copy(properties: Map<String, Any> = this.properties): OriginComponentState {
        return OriginComponentState(id, type, properties)
    }
}

data class ScaffoldComponent(
    override val id: String,
    override val type: String,
    override val properties: Map<String, Any>
) : UIComponent
