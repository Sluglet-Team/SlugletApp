package com.sluglet.slugletapp.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.sluglet.slugletapp.common.ext.basicRow
import com.sluglet.slugletapp.R.drawable as AppIcon
import com.sluglet.slugletapp.R.string as AppText

/*
This is where composables for all textfields will go
This includes things like:
Email and password login
Search bar text fields
etc.
 */

@Composable
fun RoundedTextField (
    modifier: Modifier = Modifier,
    text: String = "",
    onSearchChange: (String) -> Unit,
    userSearch: String,
    icon: ImageVector = Icons.Rounded.Edit,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    // Define any TextFieldColors that you don't want to be default here
    val colors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedLeadingIconColor = Color.Black,
        unfocusedTrailingIconColor = Color.Black,
        focusedTextColor = Color.Black
    )
    // Keeps track of Keyboard focus
    val focusManager = LocalFocusManager.current
    TextField(
        value = userSearch,
        onValueChange = { onSearchChange(it) },
        label = { Text(text = text) },
        modifier = Modifier.basicRow(),
        colors = colors,
        leadingIcon = {
            Icon(
                icon,
                contentDescription = "chosen icon"
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
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text("Email") },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, AppText.password, onNewValue, modifier)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, AppText.repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val icon =
        if (isVisible) painterResource(AppIcon.ic_visibility_on)
        else painterResource(AppIcon.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        singleLine = true,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface
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
    RoundedTextField(userSearch = "", onSearchChange = { })
}