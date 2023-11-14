package com.sluglet.slugletapp.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import com.sluglet.slugletapp.common.ext.basicRow

/*
This is where composables for all textfields will go
This includes things like:
Email and password login
Search bar text fields
etc.
 */

@OptIn(ExperimentalMaterial3Api::class) // for TextField
@Composable
fun SearchTextField (
    onSearchChange: (String) -> Unit,
    userSearch: String
) {
    // Define any TextFieldColors that you don't want to be default here
    val colors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedLeadingIconColor = Color.Black,
        unfocusedTrailingIconColor = Color.Black,

    )
    // Keeps track of Keyboard focus
    val focusManager = LocalFocusManager.current
    TextField(
        value = userSearch,
        onValueChange = { onSearchChange(it) },
        label = { Text(text = "Search") },
        modifier = Modifier.basicRow(),
        colors = colors,

        leadingIcon = {
            Icon(
                Icons.Rounded.Search,
                contentDescription = "search icon"
            )
        },
        trailingIcon = {
            Icon(
                Icons.Rounded.Clear,
                contentDescription = "clear content icon",
                modifier = Modifier
                    .clickable(
                        onClick = { onSearchChange("") }
                    )
            )
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

}

@Composable
fun CourseText (
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
        text = "CSE 115A",
        color = Color.Black,
        style = MaterialTheme.typography.displayLarge
    )
}

@Preview (showBackground = true)
@Composable
fun SearchTextFieldPreview(

) {
    SearchTextField(userSearch = "", onSearchChange = { })
}