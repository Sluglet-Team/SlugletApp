package com.sluglet.slugletapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sluglet.slugletapp.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage

open class SlugletViewModel(private val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}