package com.sluglet.slugletapp.model.service.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.SlugletActivity

// FIXME(Tanuj): Find out why this is giving a warning
class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.notification !=null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }


    private fun getRemoteView(title: String, description: String): RemoteViews{

        val remoteView = RemoteViews("com.google.firebase.messaging.FirebaseMessagingService", R.layout.notification_layout)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.description,description)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.slug)

        return remoteView
    }

    private fun generateNotification(title: String, description: String){

        val intent = Intent(this, SlugletActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelID)
            .setSmallIcon(R.drawable.slug)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,description))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0, builder.build())
    }
    companion object {
        const val channelID = "notification_channel"
        const val channelName = "SlugNotification"
    }
}
