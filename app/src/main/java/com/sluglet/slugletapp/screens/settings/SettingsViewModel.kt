package com.sluglet.slugletapp.screens.settings

import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.SIGNUP_SCREEN
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import com.sluglet.slugletapp.R.string as AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val storageService: StorageService
) : SlugletViewModel(logService) {
    val uiState = MutableStateFlow(SettingsUiState(accountService.isUserAnonymous()))

    /**
     * Opens the signup screen where the user can login
     * @param openScreen The screen to open
     */
    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(SIGNUP_SCREEN)
    /**
     * Opens the signup screen where the user can sign up
     * @param openScreen The screen to open
     */
    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGNUP_SCREEN)

    /**
     * Signs a user out and restarts the app from the Home Screen
     * @param restartApp Indicates the screen to open after deletion and pops all screens from the
     * backstack, effectively restarting the app.
     */
    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(HOME_SCREEN)
        }
    }

    /**
     * Calls services to delete the current user's account from Auth and Firestore.
     * Since all services are called within a launchCatching block, any exceptions
     * that may occur will be handled and the rest of the block will be aborted.
     *
     * @param restartApp Indicates the screen to open after deletion and pops all screens from the
     * backstack, effectively restarting the app.
     */
    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            val userId = accountService.currentUserId
            accountService.deleteAccount()
            storageService.deleteUser(userId)
            restartApp(HOME_SCREEN)
            SnackbarManager.showMessage(AppText.deleted_account)
        }
    }

    fun onReturnClick(openScreen: (String) -> Unit) {
        openScreen(HOME_SCREEN)
    }
}