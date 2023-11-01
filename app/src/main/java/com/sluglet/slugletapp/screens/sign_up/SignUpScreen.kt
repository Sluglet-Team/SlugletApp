package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sluglet.slugletapp.model.service.AccountService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun UserLogin(
    modifier: Modifier = Modifier,
    //viewModel: SignUpViewModel = hiltViewModel()
)
{
    //val uiState by viewModel.uiState
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredWidth(width = 322.dp)
            .requiredHeight(height = 270.dp)
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
            onClick = {  },
            modifier = Modifier
                .offset (x = (-50).dp,y = (85).dp))
        {
            Text("Login")
        }
        OutlinedButton(
            onClick = {  },
            modifier = Modifier
                .offset (x = (50).dp, y = (85).dp))
        {
            Text("Register")
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