package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.Popup
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.StorageService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()

) {
    val uiState by viewModel.uiState
    SignUpScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick() },
        onSignInClick = { viewModel.onSignInClick() },
        onTestClick = { viewModel.onTestClick() }
    )

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onTestClick: () -> Unit
)
{
        val localConfig = LocalConfiguration.current
        val sHeight = localConfig.screenHeightDp.dp
        val sWidth = localConfig.screenWidthDp.dp


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .requiredWidth(width = sWidth)
                .requiredHeight(height = sHeight)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color(0xfffbfdfb))

        ) {
            OutlinedTextField(
                value = uiState.email,
                placeholder = { Text(text = "Email") },
                onValueChange = onEmailChange,
                modifier = Modifier
                    .offset(y = (10).dp)
            )
            OutlinedTextField(
                value = uiState.password,
                placeholder = { Text(text = "Password") },
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .offset(y = (70).dp)
            )
            Text(
                text = "Welcome to Sluglet!",
                fontSize = 11.em,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(y = (-250).dp)
            )
            Text(
                text = "Please Enter Your Username and Password",
                fontSize = 5.em,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(y = (-100).dp)
            )
            OutlinedButton(
                onClick = { onSignInClick() },
                modifier = Modifier
                    .offset(x = (-70).dp, y = (150).dp)
            )
            {
                Text("Login")
            }
            OutlinedButton(
                onClick = {
                    onSignUpClick()
                          },
                modifier = Modifier
                    .offset(x = (70).dp, y = (150).dp)
            )
            {
                Text("Register")
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun UserLoginPreview() {
    val localConfig = LocalConfiguration.current
    val sHeight = localConfig.screenHeightDp.dp
    val sWidth = localConfig.screenWidthDp.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredWidth(width = sWidth)
            .requiredHeight(height = sHeight)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color(0xffe6e6fa))

    ) {
        OutlinedTextField(
            value = "email",
            onValueChange = {},
            modifier = Modifier
                .offset(y = (10).dp)
        )
        OutlinedTextField(
            value = "uiState.password",
            onValueChange = { },
            modifier = Modifier
                .offset(y = (70).dp)
        )
        Text(
            text = "Welcome to Sluglet!",
            fontSize = 11.em,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(y = (-250).dp)
        )
        Text(
            text = "Please Enter Your Username and Password",
            fontSize = 5.em,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(y = (-100).dp)
        )
        OutlinedButton(
            onClick = {  },
            modifier = Modifier
                .offset(x = (-70).dp, y = (150).dp)
        )
        {
            Text("Login")
        }
        OutlinedButton(
            onClick = {

            },
            modifier = Modifier
                .offset(x = (70).dp, y = (150).dp)
        )
        {
            Text("Register")
        }
    }
}