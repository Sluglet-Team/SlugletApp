package com.sluglet.slugletapp.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
// TODO(CAMDEN): return text
fun SearchTextField (
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = "Search") },
        modifier = Modifier.basicRow(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            disabledLabelColor = Color.Black,
            disabledLeadingIconColor = Color.Black,
            disabledTrailingIconColor = Color.Black,
            disabledTextColor = Color.Black,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White
        ),
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
                        onClick = { text = TextFieldValue("") }
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

@Preview (showBackground = true)
@Composable
fun SearchTextFieldPreview(

) {
    SearchTextField()
}