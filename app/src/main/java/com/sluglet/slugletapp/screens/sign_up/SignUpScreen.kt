package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.common.composables.EmailField
import com.sluglet.slugletapp.common.composables.PasswordField
import com.sluglet.slugletapp.common.composables.RoundedTextField
import com.sluglet.slugletapp.ui.theme.WaveDarkOrange
import com.sluglet.slugletapp.R.drawable as AppIcon

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    openAndPopUp: (String, String) -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = SignUpUiState()
    )
    SignUpScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        onSignInClick =  { viewModel.onSignInClick(openAndPopUp) }
    )

}
@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = WaveDarkOrange,
        disabledContentColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.primary,
    )
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Welcome to Sluglet!",
                fontSize = 11.em,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        EmailField(modifier = Modifier.padding(5.dp), value = uiState.email, onNewValue = onEmailChange)
        PasswordField(modifier = Modifier.padding(5.dp), value = uiState.password, onNewValue = onPasswordChange)
        Row (
            modifier = Modifier.fillMaxWidth().padding(start = 55.dp, end = 55.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(
                onClick = {
                    onSignUpClick()
                },
                modifier = Modifier,
                colors = buttonColors
            )
            {
                Text("Register")
            }
            OutlinedButton(
                onClick = { onSignInClick() },
                modifier = Modifier,
                colors = buttonColors
            )
            {
                Text("Login")
            }
        }
}
}

@Preview(showBackground = true)
@Composable
fun UserLoginPreview() {
    val uiState = SignUpUiState()
    SignUpScreenContent(
        uiState = uiState,
        onEmailChange = { },
        onPasswordChange = { },
        onSignUpClick = { },
        onSignInClick = { }
    )
}