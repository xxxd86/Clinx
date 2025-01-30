package com.nine.clinx.base.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import com.nine.clinx.R
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.dynamic.DynamicViewModel
import com.nine.clinx.constants.EventBus
import com.nine.clinx.constants.NavigateRouter
import com.nine.clinx.flowkit.postEventValue
import com.nine.clinx.ui.theme.WordsFairyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class StaggeredGridData(val name:String,val height:Int)
@Composable
fun DynamicLazyColumn(
    viewModel: DynamicViewModel,
    component: UIComponent,
    savable: Boolean = false
) {

    LazyColumn {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DynamicStaggerGrid(viewModel: DynamicViewModel, pager: LazyPagingItems<StaggeredGridData>) {
    //先创建一个列表数据
    val list = mutableListOf<StaggeredGridData>().apply {
        repeat(20) {
            this.add(StaggeredGridData(name = "name:$it", height = (200..300).random()))
        }
    }
    var refreshing by remember {
        mutableStateOf(false)
    }
    // 用协程模拟一个耗时加载
    val scope = rememberCoroutineScope()
    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        scope.launch {
            refreshing = true
            delay(1000) // 模拟数据加载
            repeat(20) {
                list.add(StaggeredGridData(name = "name:${it + 10}", height = (200..300).random()))
            }
            refreshing = false
        }
    })
    val context = LocalContext
    Box(
        modifier = Modifier.fillMaxSize().pullRefresh(state).background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        //填充数据
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), content = {
            list.forEachIndexed { index, staggeredGridData ->
                item {
                    Box( modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 2.dp)
                        .height(staggeredGridData.height.dp)
                        .fillMaxWidth()
                        .background(
                            color = WordsFairyTheme.colors.itemBackground,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clip(RoundedCornerShape(5.dp))
                        )
                    {
                        Column(
                            modifier = Modifier
                                .height(staggeredGridData.height.div(0.7).dp)
                                .fillMaxWidth()
                            , horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //根据高度计算，如提供staggeredGridData.height则是图片高度,根据实际高度计算 staggeredGridData.height.dp / 0.8
                            Image(
                                painter = painterResource(id = R.drawable.test),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .zIndex(100f)
                                    .fillMaxHeight(0.7f)
                                    .clickable {
                                        context.postEventValue(
                                            EventBus.NavController,
                                            NavigateRouter.SetPage.TEST_ROUT
                                        )
                                    }
                            )
                            Column {
                                Text("好吃的东西", color = WordsFairyTheme.colors.textPrimary)
                                Row (horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                                    val avatarUrl = "https://img.shetu66.com/2023/04/25/1682405982916194.png"
                                    Row {
                                        Image(
                                            contentDescription = "",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier
                                                .size(30.dp).aspectRatio(1f).clip(RoundedCornerShape(15.dp)),
                                            painter =
                                            painterResource(id = R.drawable.test))
                                        Text("红薯", color = WordsFairyTheme.colors.textPrimary)
                                    }
                                    Row {
                                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "")
                                        Text("6.4万", color = WordsFairyTheme.colors.textPrimary)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        })
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }


}