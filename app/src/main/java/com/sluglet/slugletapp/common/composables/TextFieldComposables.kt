package com.sluglet.slugletapp.common.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle

/*
This is where composables for all textfields will go
This includes things like:
Email and password login
Search bar text fields
etc.
 */

@Composable
fun SearchTextField (
    modifier: Modifier,
) {

}

@Composable
fun CourseText (
    modifier: Modifier,
    text: String,
    style: TextStyle,
    color: Color
) {
    Text(
        text = text,
        style = style,
        color = color
    )
}

@Preview(showBackground = true)
@Composable
fun CourseNumTextPreview() {
    CourseText(
        modifier = Modifier,
        text = "CSE 115A",
        color = Color.Black,
        style = MaterialTheme.typography.displayLarge
    )
}