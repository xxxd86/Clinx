package com.nine.clinx.base.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nine.clinx.base.components.StaggeredGridData

class SimpleSource : PagingSource<Int, StaggeredGridData>() {
    override fun getRefreshKey(state: PagingState<Int, StaggeredGridData>): Int? =null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaggeredGridData> {
        return try {
            val nextPage = params.key ?: 1
            val list = mutableListOf<StaggeredGridData>().apply {
                repeat(60) {
                    this.add(StaggeredGridData(name = "name:$it", height = (200..300).random()))
                }
            }
            val datas = list
            LoadResult.Page(
                data = datas,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage < 100) nextPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
