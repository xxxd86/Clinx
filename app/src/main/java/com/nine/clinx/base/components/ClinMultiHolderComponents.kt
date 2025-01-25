package com.nine.clinx.base.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nine.clinx.R
import com.nine.clinx.base.data.SimpleSource
import com.nine.clinx.base.data.UIComponent
import com.nine.clinx.base.model.DynamicViewModel
import com.nine.clinx.ui.widgets.toast.ToastModel
import com.nine.clinx.ui.widgets.toast.showToast
import androidx.paging.compose.itemKey
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

@Composable
fun DynamicStaggerGrid(viewModel: DynamicViewModel,pager: LazyPagingItems<StaggeredGridData>) {
    val list = mutableListOf<StaggeredGridData>().apply {
        repeat(20) {
            this.add(StaggeredGridData(name = "name:$it", height = (200..300).random()))
        }
    }
    val rememberLazyGrid = rememberLazyListState()

    // 将 LazyPagingItems 转换为 List 进行渲染
    //val lazyPagingItems = pager.itemSnapshotList.items
    val lazyPagingItems = viewModel.projects.collectAsLazyPagingItems()
    when {
        pager.loadState.refresh is LoadState.Loading -> {
            ToastModel("刷新", ToastModel.Type.Normal).showToast()
//            item { /* 显示刷新加载指示器 */ }
        }
        pager.loadState.append is LoadState.Loading -> {
            ToastModel("添加", ToastModel.Type.Normal).showToast()
//            item { /* 显示分页加载指示器 */ }
        }
    }
    //填充数据
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), content = {
        items(
            count = lazyPagingItems.itemCount
        ){
            val staggeredGridData = lazyPagingItems[it]
            if (staggeredGridData!=null) {
                Box( modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 2.dp)
                    .height(staggeredGridData.height.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Red,
                                Color.Green,
                                Color.Blue
                            )
                        ),
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp)
                    ))
                {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 9.dp)
                            .height(staggeredGridData.height.dp)
                            .fillMaxWidth()
                        ,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .aspectRatio(4 / 3f)
                                .clip(RoundedCornerShape(9.dp))
                                .clickable {
//                                    navigationController.navigate("CommunityContentCenter")
                                }
                        )

                        Text(text = staggeredGridData.name, color = Color.Black, fontSize = 16.sp)
                    }
                }
            }

        }
//        list.forEachIndexed { index, staggeredGridData ->
//            item {
//                Box( modifier = Modifier
//                    .padding(vertical = 2.dp, horizontal = 2.dp)
//                    .height(staggeredGridData.height.dp)
//                    .fillMaxWidth()
//                    .background(
//                        color = Color.DarkGray,
//                        shape = RoundedCornerShape(5.dp)
//                    )
//                    .clip(RoundedCornerShape(5.dp))
//                    .border(
//                        brush = Brush.linearGradient(
//                            colors = listOf(
//                                Color.Red,
//                                Color.Green,
//                                Color.Blue
//                            )
//                        ),
//                        width = 1.dp,
//                        shape = RoundedCornerShape(5.dp)
//                    ))
//                {
//                    Column(
//                        modifier = Modifier
//                            .padding(vertical = 4.dp, horizontal = 9.dp)
//                            .height(staggeredGridData.height.dp)
//                            .fillMaxWidth()
//                        ,
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.app_launch),
//                            contentDescription = null,
//                            contentScale = ContentScale.FillBounds,
//                            modifier = Modifier
//                                .padding(2.dp)
//                                .fillMaxWidth()
//                                .aspectRatio(4 / 3f)
//                                .clip(RoundedCornerShape(9.dp))
//                                .clickable {
////                                    navigationController.navigate("CommunityContentCenter")
//                                }
//                        )
//
//                        Text(text = staggeredGridData.name, color = Color.Black, fontSize = 16.sp)
//                    }
//                }
//
//            }
//        }
    })
    //填充数据
//    LazyColumn {
//       items(
//           count = lazyPagingItems.itemCount,
////           key = lazyPagingItems.itemKey{it.name}
//       ) {index: Int ->
//
//           val staggeredGridData = lazyPagingItems[index]
//            if (staggeredGridData !=null) {
//                // 列表项内容
//                Box( modifier = Modifier
//                    .padding(vertical = 2.dp, horizontal = 2.dp)
//                    .height(staggeredGridData.height.dp)
//                    .fillMaxWidth()
//                    .background(
//                        color = Color.DarkGray,
//                        shape = RoundedCornerShape(5.dp)
//                    )
//                    .clip(RoundedCornerShape(5.dp))
//                    .border(
//                        brush = Brush.linearGradient(
//                            colors = listOf(
//                                Color.Red,
//                                Color.Green,
//                                Color.Blue
//                            )
//                        ),
//                        width = 1.dp,
//                        shape = RoundedCornerShape(5.dp)
//                    ))
//                {
//                    Column(
//                        modifier = Modifier
//                            .padding(vertical = 4.dp, horizontal = 9.dp)
//                            .height(staggeredGridData.height.dp)
//                            .fillMaxWidth()
//                        ,
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                            contentDescription = null,
//                            contentScale = ContentScale.FillBounds,
//                            modifier = Modifier
//                                .padding(2.dp)
//                                .fillMaxWidth()
//                                .aspectRatio(4 / 3f)
//                                .clip(RoundedCornerShape(9.dp))
//                                .clickable {
////                                    navigationController.navigate("CommunityContentCenter")
//                                }
//                        )
//
//                        Text(text = staggeredGridData.name, color = Color.Black, fontSize = 16.sp)
//                    }
//                }
//            }
//       }
//
//
//
//    }

}