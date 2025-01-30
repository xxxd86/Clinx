package com.nine.clinx

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.nine.clinx.app.AppPreviewStartViewModel
import com.nine.clinx.dynamic.DynamicScreen
import com.nine.clinx.base.components.FlippableCardCarousel
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.dynamic.DynamicViewModel
import com.nine.clinx.constants.EventBus
import com.nine.clinx.constants.NavigateRouter
import com.nine.clinx.flowkit.observeEvent
import com.nine.clinx.flowkit.postEventValue
import com.nine.clinx.tools.Permission
import com.nine.clinx.ui.page.edit.AddImageButton
import com.nine.clinx.ui.page.edit.EditCenterScreenPage
import com.nine.clinx.ui.page.edit.EditScreenViewModel
import com.nine.clinx.ui.page.home.HomeScreenPage
import com.nine.clinx.ui.page.home.HomeViewModel
import com.nine.clinx.ui.page.post.PostCenterScreenPage
import com.nine.clinx.ui.page.post.PostCenterViewModel
import com.nine.clinx.ui.theme.ClinxTheme
import com.nine.clinx.ui.theme.LocalAutoWindowInfo
import com.nine.clinx.ui.widgets.toast.ToastModel
import com.nine.clinx.ui.widgets.toast.ToastUI
import com.nine.clinx.ui.widgets.toast.ToastUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private lateinit var viewModel: DynamicViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var appPreviewStartViewModel: AppPreviewStartViewModel
    private lateinit var postCenterViewModel : PostCenterViewModel
    private lateinit var editScreenViewModel: EditScreenViewModel
    private var isReady = AtomicBoolean(true)
    private fun loadJsonData1():String {
        return """
          {
    "id": "btn_2",
    "name": "Clinx",
    "iconImage":"",
    "iconMaterial":"Mine",
    "actionBar":"back"
    "properties": {
      "text": "Custom Button",
      "icon": "add_icon",
      "backgroundColor": "#FF5733"
    }
  }
        """
    }
//    private fun parseJsonToTopData(json: String): List<UIComponent> {
////
////        val jsonObject = JSONObject(json)
////        jsonObject.getJSONObject("name")
//////        for (i in 0 until jsonArray.length()) {
//////            val componentJson = jsonArray.getJSONObject(i)
//////            val id = componentJson.getString("id")
//////            val type = componentJson.getString("type")
////////            val properties = parseProperties(componentJson.getJSONObject("properties"))
////////            val component = componentRegistry.createComponent(type, id, properties)
//////            component.takeIf { component != null }?.let { components.add(it) }
//////        }
////
////        return components
//    }
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        CONTEXT = this
        enableEdgeToEdge()
        val content: View = findViewById(android.R.id.content)
        Permission.checkPermission(this);
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady.get()) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        setContent {
            val toastState = remember { ToastUIState() }
            val navigationController = rememberAnimatedNavController()
            ClinxTheme {
//                NavigationDrawer {
//                    NavContent(selectedIndex = it)
//                }
                //状态栏高度
                val statusBarHeightDp = LocalDensity.current.run {
                    WindowInsets.statusBars.getTop(this).toDp()
                }

//                viewModel = hiltViewModel()
                appPreviewStartViewModel = hiltViewModel()
                homeViewModel = hiltViewModel()
                postCenterViewModel = hiltViewModel()
                editScreenViewModel = hiltViewModel()
                postCenterViewModel.loadComponents()
                val context = LocalContext.current
                val feedback = LocalHapticFeedback.current
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                // 在组件首次加载时异步加载组件数据
                var topBarJson by rememberSaveable { mutableStateOf(loadJsonData1()) }
                var topBarName by remember { mutableStateOf("Clinx") }
                var isEditor by remember { mutableStateOf(false) }
                observeEvent(key = EventBus.TopBarContent, action = {
                    val jsonObject = it as? JSONObject
                    if (jsonObject != null) {
                        topBarName = jsonObject.getString("name")
                    }
                    if (jsonObject != null) {
                        isEditor = jsonObject.getBoolean("isEditor")
                    }

                })

                Box(modifier = Modifier.fillMaxSize()) {
                    NavigationDrawer (observeListener = { listener->
                        observeEvent(key = EventBus.DrawerController) {
                            scope.launch {
                                if (listener.isClosed ) {
                                    listener.open()
                                } else {
                                    listener.close()
                                }
                            }
                        }
                    }){ it->
                        Scaffold(
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    //判断当前Route使用不同功能
                                    if (navigationController.currentDestination?.route == "MainPage") {
                                        context.postEventValue(EventBus.NavController,"EditCenterScreenPage")
                                    }
                                }) {
                                    // 添加图标
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Add Item"
                                    )
                                }
                            },
                            topBar = {
                                TopAppBar(
                                    windowInsets = WindowInsets(top = statusBarHeightDp),
                                    title = {
                                        val avatarUrl =
                                            "https://c-ssl.dtstatic.com/uploads/blog/202304/15/20230415081411_9a88c.thumb.400_0.jpg"
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(topBarName)
                                        }
                                    },
                                    actions = {
                                        if (isEditor) {
                                            TextButton(
                                                onClick = { /* 处理发布 */ },
                                            ) {
                                                Text("发布")
                                            }
                                        }
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                context.postEventValue(
                                                    EventBus.DrawerController,
                                                    NavigateRouter.SetPage.TEST_ROUT
                                                )
                                            }
                                        ) {
                                            Image(
                                                contentDescription = "",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .aspectRatio(1f)
                                                    .clip(RoundedCornerShape(15.dp)),
                                                painter =
                                                painterResource(id = R.drawable.test)
                                            )
                                        }
                                    }

                                )

                            },
                            modifier = Modifier.fillMaxSize(),
                        ) { innerPadding ->
                            NavHost(
                                navigationController,
                                startDestination = appPreviewStartViewModel.currentRoute,
                            ) {
                                composable("MainPage") {
                                    LaunchedEffect (Unit){
                                        context.postEventValue(EventBus. TopBarContent,JSONObject().apply {
                                            this.put("name","Clinx")
                                            this.put("isEditor",false)
                                        })
                                    }
                                    HomeScreenPage {
                                        DynamicScreen(
                                            homeViewModel,
                                            innerPadding,
                                            appPreviewStartViewModel
                                        )
                                    }
                                }
                                composable("DynamicScreen") {
                                    PostCenterScreenPage {
                                        DynamicScreen(
                                            postCenterViewModel,
                                            innerPadding,
                                            appPreviewStartViewModel
                                        )
                                    }
                                }
                                composable("EditCenterScreenPage") {
                                    EditCenterScreenPage {
                                        DynamicScreen(
                                            editScreenViewModel,
                                            innerPadding,
                                            appPreviewStartViewModel
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ToastUI(toastState)
                }
            }

            observeEvent(key = EventBus.NavController) {
                val route = it as String
                if (route == "EditCenterScreenPage") {

                }
                navigationController.navigate(route)
            }
            /** toast */
            observeEvent(key = EventBus.ShowToast) {
                lifecycleScope.launch {
                    val data = it as ToastModel
                    toastState.show(data)
                }
            }
        }
    }

    val items = listOf(Icons.Default.Home, Icons.Default.Face, Icons.Default.Edit)

    @Composable
    fun NavigationDrawer(
        observeListener:(DrawerState)->Unit,
        content: @Composable (Int) -> Unit,
        ) {
        val windowWidthSizeClass =
            LocalAutoWindowInfo.current.windowSizeClass.widthSizeClass

        val selectedState = rememberSaveable { mutableStateOf(0) }
        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
            val drawerState = rememberDrawerState(DrawerValue.Closed).apply {
                observeListener.invoke(this)
            }
            ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
                DrawerContent(
                    drawerState = drawerState,
                    selectedState = selectedState,
                    windowWidthSizeClass = windowWidthSizeClass
                )
            }) { content(selectedState.value) }
        } else {
            PermanentNavigationDrawer(drawerContent = {
                DrawerContent(
                    selectedState = selectedState,
                    windowWidthSizeClass = windowWidthSizeClass
                )
            }) { content(selectedState.value) }
        }
    }

    @Composable
    fun DrawerContent(
        drawerState: DrawerState? = null,
        selectedState: MutableState<Int>,
        windowWidthSizeClass: WindowWidthSizeClass
    ) {
        val scope = rememberCoroutineScope()
        val isMedium = windowWidthSizeClass == WindowWidthSizeClass.Medium
        val isCompat = windowWidthSizeClass == WindowWidthSizeClass.Compact
        val context = LocalContext
        val sheetWidth = when{
            isCompat -> 300.dp
            isMedium -> 100.dp
            else -> 220.dp
        }

        ModalDrawerSheet(
//            drawerShape = if (isCompat) CutCornerShape(topEnd = 16.dp, bottomEnd = 16.dp,) else  RectangleShape,
            modifier = Modifier
                .width(sheetWidth)
                .fillMaxHeight()
        ) {
            Column  (verticalArrangement = Arrangement.Bottom){
                //
                Box (modifier = Modifier
                    .fillMaxHeight(0.77f)
                    .statusBarsPadding()){

                }
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = { Icon(item, contentDescription = null) },
                        label = {
                            if (isMedium){ }  else Text(item.name.replace("Filled.",""))
                        },
                        selected = index == selectedState.value,
                        onClick = {
                            scope.launch { drawerState?.close() }
                            selectedState.value = index
                            if (selectedState.value == 2) {
                               context.postEventValue(EventBus.NavController,"EditCenterScreenPage")
                            } else {
                                context.postEventValue(EventBus.NavController,"MainPage")
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavContent(selectedIndex: Int) {
        Scaffold() {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.Cyan)) {
                Text(text = "$selectedIndex", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    companion object {
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: MainActivity
    }
    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }
    @Preview(name = "name",
        group = "group",
        widthDp = 100,
        heightDp = 100,
        fontScale = 2f,
        backgroundColor = 0xFF00FF00,
        showBackground = true,
        showSystemUi = true,
        device = Devices.NEXUS_5
    )
    @Preview
    @Composable
    fun DefaultPreview() {
        Greeting( "Hello!")
    }


}
