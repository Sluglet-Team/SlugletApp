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
import javax.inject.Inject
import com.sluglet.slugletapp.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService
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

    /**
     * Calls services to link Anonymous user to an account with the provided credentials
     * @param openAndPopUp Indicates which screen to open and pops this screen from the
     * backstack.
     */
    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!(email).isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!(password).isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        launchCatching {
            accountService.linkAccounts(_uiState.value.email, _uiState.value.password)
            openAndPopUp(HOME_SCREEN, SIGNUP_SCREEN)
        }
    }

    /**
     * Calls services to log a user into their account.
     * @param openAndPopUp Indicates which screen to open and pops this screen from the
     * backstack.
     */
    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
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