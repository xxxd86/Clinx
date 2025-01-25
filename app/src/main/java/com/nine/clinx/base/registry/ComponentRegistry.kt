package com.nine.clinx.base.registry

import com.nine.clinx.base.data.UIComponent

//Flow 可以使用的组件注册
class ComponentRegistry {

    private val componentFactories =
        mutableMapOf<String, (String, Map<String, Any>) -> UIComponent>()

    fun registerComponent(type: String, factory: (String, Map<String, Any>) -> UIComponent) {
        componentFactories[type] = factory
    }

    fun createComponent(type: String, id: String, properties: Map<String, Any>): UIComponent? {
        return componentFactories[type]?.invoke(id, properties)
            ?: componentFactories["Spacer"]?.invoke(id, properties)
    }

}



