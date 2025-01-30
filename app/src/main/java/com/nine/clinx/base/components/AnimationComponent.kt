package com.nine.clinx.base.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nine.clinx.R

@Composable
fun FlippableCardCarousel(modifier:Modifier) {
    // List of cards (can be any data you want to display)
    val cards = remember { mutableStateListOf("Card 1", "Card 2", "Card 3", "Card 4") }
    var currentCardIndex by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }
    var isFlipped by remember { mutableStateOf(false) }

    // Gesture detection for swipe
    Box(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    // Update offset based on drag amount
                    offsetX += dragAmount
                    // Limit the offset
                    if (offsetX > 300) offsetX = 300f
                    else if (offsetX < -300) offsetX = -300f
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .padding(16.dp)
                .graphicsLayer {
                    // Apply rotation and perspective for the 3D model
                    this.rotationY = 0f  // Rotate around Y-axis for a 3D effect
                    this.scaleX = 0.7f  // Make the model smaller
                    this.scaleY = 0.7f
                }
        ) {
            //预留3D图片
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Transparent)
            ) {
//                BasicText(
//                    text = "3D Model",
//                    modifier = Modifier.align(Alignment.Center),
//                    style = TextStyle(color = Color.White)
//                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Rotate the card as it is dragged to simulate the corner flip
                    rotationX = if (offsetX > 0) {
                        (offsetX / 300) * 45f // Maximum 45 degrees rotation
                    } else 0f
                    shadowElevation = 8.dp.toPx()
                    clip = true
                    translationX = offsetX // Move the card horizontally
                }
                .zIndex(1f),
            shape = RoundedCornerShape(CornerSize(15.dp)), // Rounded corners
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(
                0xFFE6E0EF
            )
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(painterResource(R.drawable.foodshow), contentDescription = null, modifier = Modifier.fillMaxSize())
//                BasicText(
//                    text = cards[currentCardIndex],
//                    style = TextStyle(color = Color.White)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//
//                BasicText(
//                    text = "Swipe left to flip to the next card.",
//                    style = TextStyle(color = Color.White)
//                )
            }
        }
    }

    // Detect when the card has been swiped left enough to change the card
    if (offsetX <= -300f) { // Swipe left threshold
        // Move to the next card
        if (currentCardIndex < cards.size - 1) {
            currentCardIndex++
        } else {
            currentCardIndex = 0 // Loop back to the first card
        }
        // Reset offset for the next card
        offsetX = 0f
    }
    if (offsetX >= 300f) { // Swipe left threshold
//        // Move to the next card
//        if (currentCardIndex > 0) {
//            currentCardIndex--
//        } else {
//            currentCardIndex = 0 // Loop back to the first card
//        }
//        // Reset offset for the next card
//        offsetX = 0f
        //展示3D图
    }

}