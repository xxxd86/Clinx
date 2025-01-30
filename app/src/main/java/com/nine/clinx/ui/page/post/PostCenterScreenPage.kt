package com.nine.clinx.ui.page.post

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.nine.clinx.ui.page.home.HomeViewModel

@Composable
inline fun PostCenterScreenPage (
    content: @Composable () -> Unit) {
    content.invoke()
}