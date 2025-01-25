package com.nine.clinx.ui.widgets.toast

open class ToastModel(
    open val message: String,
    val type: Type = Type.Normal,
    open val durationTime: Long? = null,
) {
    enum class Type {
        Normal, Success, Info, Warning, Error,
    }
}

data class ToastModelSuccess(
    override val message: String,
) : ToastModel(message, Type.Success, null)

data class ToastModelInfo(
    override val message: String,
) : ToastModel(message, Type.Info, null)

data class ToastModelError(
    override val message: String,
) : ToastModel(message, Type.Error, null)

data class ToastModelWarning(
    override val message: String,
) : ToastModel(message, Type.Warning, null)


