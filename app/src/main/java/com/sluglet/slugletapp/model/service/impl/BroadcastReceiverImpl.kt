package com.sluglet.slugletapp.model.service.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sluglet.slugletapp.model.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BroadcastReceiverImpl : BroadcastReceiver() {
    @Inject
    lateinit var notificationService: NotificationService
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("In onReceive", "$context")
        if (context != null) {
            Log.v("In onReceive", "context not null")
            notificationService.showNotification()
        }
    }
}