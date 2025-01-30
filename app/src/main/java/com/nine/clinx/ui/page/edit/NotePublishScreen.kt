package com.nine.clinx.ui.page.edit

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nine.clinx.constants.EventBus
import com.nine.clinx.flowkit.postEventValue
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePublishScreen() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val images = remember { mutableStateListOf<Uri>() }
    val context  = LocalContext
    LaunchedEffect (Unit){
        context.postEventValue(EventBus.TopBarContent, JSONObject().apply {
            this.put("name","激发创造~")
            this.put("isEditor",true)
        })
    }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 内容输入框
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (text.text.isEmpty()) {
                            Text(
                                text = "分享你此刻的想法...",
                                color = Color.Gray,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
//
//            // 图片选择区域
            ImageSelector(images = images) {
                if (it != null) {
                    images.add(it)
                }
            }
//            ImagePicker()
            Spacer(modifier = Modifier.height(24.dp))

            // 附加功能
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* 添加标签 */ }) {
                    Icon(Icons.Default.Add, "添加标签", tint = Color.Red)
                }

                IconButton(onClick = { /* 选择地点 */ }) {
                    Icon(Icons.Default.LocationOn, "选择地点", tint = Color.Red)
                }

                IconButton(onClick = { /* 权限设置 */ }) {
                    Icon(Icons.Default.Lock, "权限设置", tint = Color.Red)
                }
            }
        }
}
@Composable
fun ImagePicker() {
    var imageUri: Uri? by remember {
        mutableStateOf(null)
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                imageUri = uri
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Crop
        )
        Button(
            onClick = {
                launcher.launch(arrayOf("image/*"))
            }
        ) {
            Text(text = "Pick Image")
        }
    }
}

@Composable
fun ImageSelector(images: List<Uri>,selectResult:(Uri?)->Unit) {
    var imageUri: Uri? by remember {
        mutableStateOf(null)
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                imageUri = uri
                selectResult.invoke(imageUri)
            }
        }

    LazyRow (){
        items(images) { image ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(100.dp)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { /* 删除图片 */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        "删除",
                        tint = Color.White,
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    )
                }
            }
        }

        item {
            AddImageButton(
                modifier = Modifier.padding(start = if (images.isEmpty()) 0.dp else 8.dp),
                onClick = {
                    launcher.launch(arrayOf("image/*"))
                }
            )
        }
    }
}

@Composable
fun AddImageButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "添加图片",
            tint = Color.LightGray,
            modifier = Modifier.size(32.dp)
        )
    }
}