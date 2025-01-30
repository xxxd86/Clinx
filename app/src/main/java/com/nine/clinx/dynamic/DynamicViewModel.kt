package com.nine.clinx.dynamic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.nine.clinx.base.data.CustomButtonState
import com.nine.clinx.base.data.OriginComponentState
import com.nine.clinx.base.data.SimpleSource
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.base.data.UIEvent
import com.nine.clinx.base.data.UIState
import com.nine.clinx.base.model.BaseViewModel
import com.nine.clinx.base.registry.ComponentRegistry
import com.nine.clinx.base.registry.ComponentRegistryInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


abstract class DynamicViewModel  internal constructor() : BaseViewModel<ViewIntent, ViewState, SingleEvent>()  {
    override val viewStateFlow: StateFlow<ViewState> = MutableStateFlow(ViewState.initial()).asStateFlow()

    val componentRegistry = ComponentRegistry()
    open var state = 1
    val _uiState = MutableStateFlow(UIState(emptyList()))
    val uiState: StateFlow<UIState> get() = _uiState
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // UI Events (流)
    private val _uiEvent = MutableSharedFlow<UIEvent>(extraBufferCapacity = 1)  // 可以发出事件
    val uiEvent = _uiEvent.asSharedFlow()
    val projects = Pager(PagingConfig(pageSize = 2, initialLoadSize = 6, // 第一次加载数量，如果不设置的话是 pageSize * 2
        prefetchDistance = 2)){
        SimpleSource()
    }.flow.cachedIn(viewModelScope)

    init {
        val initialVS = ViewState.initial()
            //最好能够在UI加载之前将这个组件注册
        uiScope.launch(Dispatchers.Main) {
                ComponentRegistryInitializer.initializeComponentRegistry(componentRegistry)
        }
            // 监听 UI 事件并响应
            viewModelScope.launch {
                uiEvent.collect { event ->
                    handleEvent(event)
                }
            }


    }

    // 加载 JSON 数据并解析
    fun loadComponentsFromJson(json: String) {
        val components = parseJsonToComponents(json)
        _uiState.value = UIState(components)
    }

    // 触发 UI 事件
    fun triggerEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    // 处理事件
    fun handleEvent(event: UIEvent) {
        when (event) {
            is UIEvent.ButtonClicked -> {
                handleButtonClicked(event)
            }

            is UIEvent.TextChanged -> {
                handleTextChanged(event)
            }
        }
    }
    private fun loadJsonData2():String {
        return """
        [
          {
    "id": "btn_2",
    "type": "EditorScreen",
    "properties": {
      "text": "Custom Button",
      "icon": "add_icon",
      "backgroundColor": "#FF5733"
    }
  }
        ]
        """
    }
    private fun loadJsonData1():String {
        return """
        [
          {
    "id": "btn_2",
    "type": "StaggerGrid",
    "properties": {
      "text": "Custom Button",
      "icon": "add_icon",
      "backgroundColor": "#FF5733"
    }
  }
        ]
        """
    }
    private fun loadJsonData(): String {
        return """
        [
          {
            "id": "btn_1",
            "type": "FlippableCardCarousel",
            "properties": {
              "text": "Click Me",
              "padding": 20
            }
          },
          {
            "id": "txt_1",
            "type": "Column",
            "properties": {
              "text": "Hello, World!",
              "color": "#FF5733"
            }
          }
        ]
        """
    }

    fun loadComponents() {
        viewModelScope.launch {
            // 异步加载组件数据
            val components = fetchComponentsFromServer()
            _uiState.value = _uiState.value.copy(components = components)
        }
    }

    private fun fetchComponentsFromServer(): List<UIComponent> {
        var jsonData = ""
        if (state == 1) {
            jsonData = loadJsonData1()
        } else {
            if (state == 2) {
                jsonData = loadJsonData()
            }
            if (state == 3) {
                jsonData = loadJsonData2()
            }

        }
        val components = parseJsonToComponents(jsonData)
        return components
    }

    private fun handleButtonClicked(event: UIEvent.ButtonClicked) {
        val updatedComponents = _uiState.value.components.map { component ->
            if (component.id == event.componentId) {
                // 强制转换为 ComponentState
                val updatedProperties = component.properties + ("color" to "#FF0000")  // 更新按钮颜色
                if (component is OriginComponentState) {
                    component.copy(properties = updatedProperties)
                } else {
                    if (component is CustomButtonState) {
                        component.copy(properties = updatedProperties) // 如果不是 ComponentState 类型，直接返回原组件
                    } else {
                        component
                    }
                }
            } else {
                component
            }
        }

        // 更新 UI 状态
        _uiState.value = _uiState.value.copy(components = updatedComponents)
    }

    private fun handleTextChanged(event: UIEvent.TextChanged) {
        val updatedComponents = _uiState.value.components.map { component ->
            if (component.id == event.componentId && component is OriginComponentState) {
                // 更新文本组件的文本
                val updatedProperties = component.properties + ("text" to event.newText)
                component.copy(properties = updatedProperties)
            } else {
                component
            }
        }

        // 更新 UI 状态
        _uiState.value = _uiState.value.copy(components = updatedComponents)
    }

    private fun parseJsonToComponents(json: String): List<UIComponent> {
        val jsonArray = JSONArray(json)
        val components = mutableListOf<UIComponent>()

        for (i in 0 until jsonArray.length()) {
            val componentJson = jsonArray.getJSONObject(i)
            val id = componentJson.getString("id")
            val type = componentJson.getString("type")
            val properties = parseProperties(componentJson.getJSONObject("properties"))
            val component = componentRegistry.createComponent(type, id, properties)
            component.takeIf { component != null }?.let { components.add(it) }
        }

        return components
    }

    private fun parseProperties(propertiesJson: JSONObject): Map<String, Any> {
        return propertiesJson.keys().asSequence().associateWith { propertiesJson[it] }
    }
}


