package com.sluglet.slugletapp.screens.sign_up

import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import com.sluglet.slugletapp.R.string as AppText
import com.sluglet.slugletapp.common.ext.isValidEmail
import com.sluglet.slugletapp.common.ext.isValidPassword
import com.sluglet.slugletapp.common.ext.passwordMatches


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : SlugletViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
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
            accountService.linkAccount(email, password)

            //TODO https://firebase.google.com/codelabs/build-android-app-with-firebase-compose#2

        }
    }
}