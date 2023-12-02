package com.sluglet.slugletapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.ext.basicRow
import com.sluglet.slugletapp.common.ext.smallSpacer

@Composable
fun SettingsScreen (
    openScreen: (String) -> Unit,
    restartApp: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))
    SettingsScreenContent(
        uiState = uiState,
        onReturnClick = viewModel::onReturnClick,
        openScreen = openScreen,
        onLoginClick = { viewModel.onLoginClick(openScreen) },
        onSignUpClick = { viewModel.onSignUpClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) }

    )
}
@Composable
fun SettingsScreenContent (
    modifier: Modifier = Modifier,
    onReturnClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {},
    uiState: SettingsUiState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column (
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ReturnButton(
                modifier = Modifier
                    .align(Alignment.TopStart),
                onReturnClick = onReturnClick,
                openScreen = openScreen
            )
        }
        if (uiState.isAnonymousAccount){
            TextButton (onClick = onSignUpClick, openScreen = openScreen, text = "Sign Up")
            TextButton(onClick = onLoginClick, openScreen = openScreen, text = "Login")
        }else {
            TextButton (onClick = onSignOutClick, openScreen = openScreen, text = "Log Out")
            TextButton (onClick = onDeleteMyAccountClick, openScreen = openScreen, text = "Delete Account")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextButton(
    onClick: () -> Unit,
    openScreen: (String) -> Unit,
    text: String
){
    Card(
        onClick = { onClick() },
        modifier = Modifier.padding(5.dp).basicRow()
    ) {
        Text(text = text, color = Color.Black, modifier = Modifier.smallSpacer())
    }
}
@Composable
fun ReturnButton(
    modifier: Modifier = Modifier,
    onReturnClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {}
) {
    Icon(
        Icons.Filled.ArrowBack,
        contentDescription = "home",
        modifier = Modifier
            .clickable {
                if (onReturnClick != null) {
                    onReturnClick(openScreen)
                }
            }
            .smallSpacer()
    )
}
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val uiState = SettingsUiState(true)
    SlugletAppTheme {
        SettingsScreenContent(
            onReturnClick = { },
            uiState = uiState,
            onLoginClick = { },
            onSignUpClick = { },
            onSignOutClick = { },
            onDeleteMyAccountClick = { },
        )
    }
}
