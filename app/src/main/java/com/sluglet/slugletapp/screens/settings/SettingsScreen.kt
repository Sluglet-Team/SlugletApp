package com.sluglet.slugletapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.sluglet.slugletapp.common.composables.DialogCancelButton
import com.sluglet.slugletapp.common.composables.DialogConfirmButton
import com.sluglet.slugletapp.common.ext.basicRow
import com.sluglet.slugletapp.common.ext.smallSpacer
import com.sluglet.slugletapp.R.string as AppText

@Composable
fun SettingsScreen (
    openScreen: (String) -> Unit,
    restartApp: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))
    SettingsScreenContent(
        onReturnClick = viewModel::onReturnClick,
        openScreen = openScreen,
        uiState = uiState,
        onLoginClick = { viewModel.onLoginClick(openScreen) },
        onSignUpClick = { viewModel.onSignUpClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) }
    )
}
@Composable
fun SettingsScreenContent (
    onReturnClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {},
    uiState: SettingsUiState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ReturnButton(
                onReturnClick = onReturnClick,
                openScreen = openScreen
            )
        }
        if (uiState.isAnonymousAccount){
            TextButton (onClick = onSignUpClick, text = "Sign Up")
            TextButton(onClick = onLoginClick, text = "Login")
        }else {
            TextButton (onClick = onSignOutClick, text = "Log Out")
            TextButton (onClick = onDeleteMyAccountClick, text = "Delete Account", isDeleteButton = true)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextButton(
    onClick: () -> Unit,
    text: String,
    isDeleteButton: Boolean = false
){
    if (!isDeleteButton) {
        Card(
            onClick = { onClick() },
            modifier = Modifier.padding(5.dp).basicRow()
        ) {
            Text(text = text, color = Color.Black, modifier = Modifier.smallSpacer())
        }
    }
    else  {
        var showWarningDialog by remember { mutableStateOf(false) }
        Card(
            onClick = { showWarningDialog = true },
            modifier = Modifier.padding(5.dp).basicRow()
        ) {
            Text(text = text, color = Color.Black, modifier = Modifier.smallSpacer())
        }
        if (showWarningDialog) {
            AlertDialog(
                title = { Text(stringResource(AppText.delete_account_title)) },
                text = { Text(stringResource(AppText.delete_account_description)) },
                dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
                confirmButton = {
                    DialogConfirmButton(AppText.delete_my_account) {
                        onClick()
                        showWarningDialog = false
                    }
                },
                onDismissRequest = { showWarningDialog = false }
            )
        }
    }
}

@Composable
fun ReturnButton(
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
        ) { }
    }
}
