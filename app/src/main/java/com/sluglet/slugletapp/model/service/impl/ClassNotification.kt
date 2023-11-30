package com.sluglet.slugletapp.model.service.impl

import NotificationWorker
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClassNotification @Inject constructor(@ApplicationContext private val context: Context) {

//    fun createClassNotification(context: Context) {
//        val notificationIntent = Intent(context, NotificationPublisher::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // Call onReceive directly to trigger the notification immediately
//        NotificationPublisher().onReceive(context, null)
//    }
//
//    class NotificationPublisher : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            Log.d("Reached on publish","Reached on publish")
//            if (context != null) {
//                val notificationWorker = NotificationWorker(context)
//                notificationWorker.generateNotification("Sluglet", "You have class soon!")
//            }
//        }
//    }

    init {
        createNotificationChannel()
    }
     fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NotificationWorker.COURSE_CHANNEL_ID,
                "Course Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
