package com.nine.clinx.base.registry

import com.nine.clinx.base.data.CustomButtonState
import com.nine.clinx.base.data.OriginComponentState

object ComponentRegistryInitializer {
    private var isInitialized = false

    fun initializeComponentRegistry(componentRegistry: ComponentRegistry) {
        if (isInitialized) return  // 防止重复注册

        componentRegistry.registerComponent("Spacer") { id, properties ->
            OriginComponentState(id, "Spacer", properties)
        }

        componentRegistry.registerComponent("Text") { id, properties ->
            OriginComponentState(id, "Text", properties)
        }

        componentRegistry.registerComponent("Button") { id, properties ->
            OriginComponentState(id, "Button", properties)
        }

        componentRegistry.registerComponent("CustomButton") { id, properties ->
            CustomButtonState(id, "CustomButton", properties)
        }

        componentRegistry.registerComponent("StaggerGrid") {id, properties ->
            OriginComponentState(id, "StaggerGrid", properties)
        }

        isInitialized = true  // 标记为已初始化
    }
}

