package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.SIGNUP_SCREEN
import com.sluglet.slugletapp.common.ext.isValidEmail
import com.sluglet.slugletapp.common.ext.isValidPassword
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.sluglet.slugletapp.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService
) : SlugletViewModel(logService) {

    private var _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

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

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!(email).isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!(password).isValidPassword()) {
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
            // FIXME: Now with anonymous accounts, we maybe don't need this
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
        if (!(email).isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }
        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }
        launchCatching {
            accountService.logIn(email, password)
            openAndPopUp(HOME_SCREEN, SIGNUP_SCREEN)
        }
    }
}