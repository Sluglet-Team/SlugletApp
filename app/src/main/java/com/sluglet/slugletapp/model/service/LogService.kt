package com.sluglet.slugletapp.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}