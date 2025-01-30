package com.nine.clinx.ui.page.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
inline fun HomeScreenPage(
    content: @Composable () -> Unit) {
    content.invoke()
}