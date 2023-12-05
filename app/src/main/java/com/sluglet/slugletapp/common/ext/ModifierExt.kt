package com.sluglet.slugletapp.common.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// This is where custom modifiers live

fun Modifier.basicRow(): Modifier {
    return this
        .padding(start = 10.dp, end = 10.dp)
        .fillMaxWidth()
        .shadow(
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp)
        )
        .background(
            color = Color.White,
            shape = RoundedCornerShape(size = 20.dp)
        )
}
fun Modifier.smallSpacer(): Modifier {
    return this.padding(10.dp)
}
fun Modifier.largeSpacer(): Modifier {
    return this.padding(40.dp)
}