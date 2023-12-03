package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.R.string as AppText
import com.sluglet.slugletapp.common.ext.isValidEmail
import com.sluglet.slugletapp.common.ext.isValidPassword
import com.sluglet.slugletapp.common.ext.passwordMatches
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.screens.SlugletViewModel
import com.sluglet.slugletapp.screens.sign_up.SignUpUiState
import com.sluglet.slugletapp.SlugletAppState
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.StorageService
import android.util.Log
import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.SIGNUP_SCREEN

@HiltViewModel
class SignUpViewModel @Inject constructor(
    logService: LogService,
    savedStateHandle: SavedStateHandle,
    private val accountService: AccountService,
    private val storageService: StorageService
) : SlugletViewModel(logService) {

    private var _uiState = mutableStateOf(SignUpUiState())
    val uiState: State<SignUpUiState> = _uiState

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        _uiState.value = uiState.value.copy(email = newValue)

    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        _uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!(_uiState.value.email).isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!(_uiState.value.password).isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        /*
        Tries to authenticate, and if the call succeeds,
        it proceeds to the next screen (the SettingsScreen).
        As you are executing these calls inside a launchCatching block,
        if an error happens on the first line,
        the exception will be caught and handled,
        and the second line will not be reached at all.
         */
        launchCatching {
            accountService.linkAccounts(_uiState.value.email, _uiState.value.password)
            accountService.createAccount(_uiState.value.email, _uiState.value.password)
            openAndPopUp(HOME_SCREEN, SIGNUP_SCREEN)
        }
    }
    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    /*
    Tries to authenticate, and if the call succeeds,
    it proceeds to the next screen (the SettingsScreen).
    As you are executing these calls inside a launchCatching block,
    if an error happens on the first line,
    the exception will be caught and handled,
    and the second line will not be reached at all.
     */
        if (!(_uiState.value.email).isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        launchCatching {
            accountService.logIn(_uiState.value.email, _uiState.value.password)
            openAndPopUp(HOME_SCREEN, SIGNUP_SCREEN)
        }
    }
    fun onTestClick() {
        /*
        Tries to authenticate, and if the call succeeds,
        it proceeds to the next screen (the SettingsScreen).
        As you are executing these calls inside a launchCatching block,
        if an error happens on the first line,
        the exception will be caught and handled,
        and the second line will not be reached at all.
         */
    }
}