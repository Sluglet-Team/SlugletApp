package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sluglet.slugletapp.common.composables.CourseText

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)

@Composable
fun UserLogin(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredWidth(width = 322.dp)
            .requiredHeight(height = 300.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color(0xfffbfdfb))

    ) {
        val email = rememberSaveable { mutableStateOf("Email") }
        val password = rememberSaveable { mutableStateOf("Password") }
        BasicTextField(
            value = email.value,
            onValueChange = { email.value = it }
        )
        BasicTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .offset(y = (35).dp)
        )
        Text(
            text = "Welcome!\nPlease Enter Your Username and Password",
            modifier = Modifier
                .offset(y = (-50).dp)
        )
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .offset (y = (85).dp))
        {
            Text("Login")
        }
    }

}


@Preview(showBackground = true)
@Composable
fun UserLoginPreview() {
    val input = rememberSaveable { mutableStateOf("") }
    BasicTextField(
        value = input.value,
        onValueChange = { input.value = it }
    )
    Text(input.value)
}