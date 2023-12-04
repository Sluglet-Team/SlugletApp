package com.sluglet.slugletapp.model.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

interface NotificationService {
    fun createNotificationChannel()
    fun showNotification()
    fun scheduleNotificationAtTime(day: Int, hour: Int, minute: Int)

}