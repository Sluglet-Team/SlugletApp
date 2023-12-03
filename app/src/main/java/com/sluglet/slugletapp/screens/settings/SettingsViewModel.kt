package com.sluglet.slugletapp.screens.settings

import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.SIGNUP_SCREEN
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService
) : SlugletViewModel(logService) {
    val uiState = MutableStateFlow(SettingsUiState(accountService.isUserAnonymous()))

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(SIGNUP_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGNUP_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(HOME_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(HOME_SCREEN)
        }
    }

    fun onReturnClick(openScreen: (String) -> Unit) {
        openScreen(HOME_SCREEN)
    }
}