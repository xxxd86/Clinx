//package com.nine.clinx.base.data
//
//import android.util.Log
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.nine.clinx.base.components.StaggeredGridData
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class DemoRepository {
//    fun getData() = Pager(PagingConfig(10)) {
//        PagingResource()
//    }.flow
//}
//
//private class PagingResource : PagingSource<Int, StaggeredGridData>() {
//    private val startPage = 0
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaggeredGridData> {
//        return try {
//            val currentPage = params.key ?: startPage
//            val data = getData(currentPage)
//            val prevKey = if (currentPage > startPage) currentPage - 1 else null
//            val nextKey = if (data.isNotEmpty()) currentPage + 1 else null
//            LoadResult.Page(data, prevKey, nextKey)
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, StaggeredGridData>): Int? {
//        return null
//    }
//
//    //具体获取数据的方法。这里能更细分的对异常处理，否则在load()中合并返回后在UI中难区分。
//    //但处理后还是要抛出异常，不然load()不会返回异常，影响UI中对Paging状态判断
//    private suspend fun getData(currentPage: Int): List<StaggeredGridData> {
//        var data: List<StaggeredGridData> = emptyList()
//        runCatching {
//            withContext(Dispatchers.IO) {
//                RetrofitClient.apiService.newestArticle(currentPage.toString())
//            }
//        }.onSuccess { response ->
//            response.getData().onSuccess {
//                data = it.datas
//            }.onFailure {
//                Log.e("服务器错误", it.message.toString())
//                throw Exception("服务器错误：${it.message}")
//            }
//        }.onFailure {
//            Log.e("本地错误", it.message.toString())
//            throw Exception("本地错误：${it.message}")
//        }
//        return data
//    }
//}