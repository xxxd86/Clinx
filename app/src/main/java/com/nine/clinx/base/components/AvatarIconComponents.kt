package com.nine.clinx.base.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AuthorAvatarRound(
    modifier: Modifier = Modifier,
    url: Any,
    shape: RoundedCornerShape = RoundedCornerShape(0),
    border: BorderStroke? = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.35f)),
    contentScale: ContentScale = ContentScale.Fit,
) {
    Surface(
        shape = shape,
        border = border,
    ) {
        // 使用coil加载图片
        AsyncImage(
            model = url,
            modifier = modifier,
            contentDescription = null,
            placeholder = null,
            error = null,
            contentScale = contentScale,
        )
    }
}