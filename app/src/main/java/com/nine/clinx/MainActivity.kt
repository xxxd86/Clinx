package com.nine.clinx

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nine.clinx.app.AppPreviewStartViewModel
import com.nine.clinx.base.DynamicScreen
import com.nine.clinx.base.components.AuthorAvatarRound
import com.nine.clinx.base.model.DynamicViewModel
import com.nine.clinx.constants.EventBus
import com.nine.clinx.constants.NavigateRouter
import com.nine.clinx.flowkit.observeEvent
import com.nine.clinx.flowkit.postEventValue
import com.nine.clinx.ui.theme.ClinxTheme
import com.nine.clinx.ui.theme.LocalAutoWindowInfo
import com.nine.clinx.ui.widgets.toast.ToastModel
import com.nine.clinx.ui.widgets.toast.ToastUI
import com.nine.clinx.ui.widgets.toast.ToastUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: DynamicViewModel
    private lateinit var appPreviewStartViewModel: AppPreviewStartViewModel
    private var isReady = AtomicBoolean(true)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        CONTEXT = this
        enableEdgeToEdge()
        val content: View = findViewById(android.R.id.content)
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
            val navigationController = rememberNavController()
            ClinxTheme {
//                NavigationDrawer {
//                    NavContent(selectedIndex = it)
//                }
                //状态栏高度
                val statusBarHeightDp = LocalDensity.current.run {
                    WindowInsets.statusBars.getTop(this).toDp()
                }
                appPreviewStartViewModel = hiltViewModel()
                viewModel = hiltViewModel()
                val context = LocalContext.current
                val feedback = LocalHapticFeedback.current
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

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
                    }){
                        NavHost(navigationController, startDestination = "mainpage") {
                            composable("mainpage") {
                                Scaffold(
                                    topBar = {
                                        TopAppBar(
                                            windowInsets = WindowInsets(top = statusBarHeightDp),
                                            modifier = Modifier.background(
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(23214),
                                                        Color.Cyan
                                                    ),
                                                    tileMode = TileMode.Clamp
                                                )
                                            ),
                                            title = {
                                                val avatarUrl = "https://c-ssl.dtstatic.com/uploads/blog/202304/15/20230415081411_9a88c.thumb.400_0.jpg"
                                                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                                    AuthorAvatarRound(
                                                        modifier = Modifier
                                                            .size(30.dp).aspectRatio(1f),
                                                        url = avatarUrl,shape = RoundedCornerShape(50), border = null, contentScale = ContentScale.Crop)
                                                    Text("小对象的炼金工坊")
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
                                                    Icon(Icons.Filled.AccountCircle, null)
                                                }
                                            }
                                        )

                                    },
                                    modifier = Modifier.fillMaxSize(),
                                ) { innerPadding ->
                                    Button(onClick = {
                                        context.postEventValue(
                                            EventBus.NavController,
                                            NavigateRouter.SetPage.TEST_ROUT
                                        )
                                    }, modifier = Modifier.padding(top = 100.dp)) {
                                        Text("跳转")
                                    }
                                }
                            }
                            composable("DynamicScreen") {
                                DynamicScreen(viewModel)
                            }
                        }
                    }

                    ToastUI(toastState)
                }
            }

            observeEvent(key = EventBus.NavController) {
                val route = it as String
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
    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)

    @OptIn(ExperimentalMaterial3Api::class)
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DrawerContent(
        drawerState: DrawerState? = null,
        selectedState: MutableState<Int>,
        windowWidthSizeClass: WindowWidthSizeClass
    ) {
        val scope = rememberCoroutineScope()
        val isMedium = windowWidthSizeClass == WindowWidthSizeClass.Medium
        val isCompat = windowWidthSizeClass == WindowWidthSizeClass.Compact

        val sheetWidth = when{
            isCompat -> 300.dp
            isMedium -> 100.dp
            else -> 220.dp
        }

        ModalDrawerSheet(
//            drawerShape = if (isCompat) CutCornerShape(topEnd = 16.dp, bottomEnd = 16.dp,) else  RectangleShape,
            modifier = Modifier.width(sheetWidth)
        ) {
            items.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    icon = { Icon(item, contentDescription = null) },
                    label = {
                        if (isMedium){ }  else Text(item.name)
                    },
                    selected = index == selectedState.value,
                    onClick = {
                        scope.launch { drawerState?.close() }
                        selectedState.value = index
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavContent(selectedIndex: Int) {
        Scaffold() {
            Box(modifier = Modifier.fillMaxSize().padding(it).background(Color.Cyan)) {
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
