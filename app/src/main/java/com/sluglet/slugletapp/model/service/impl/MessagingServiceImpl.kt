package com.sluglet.slugletapp.model.service.impl
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sluglet.slugletapp.SlugletActivity
import com.sluglet.slugletapp.R


class MessagingServiceImpl : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.getNotification()!=null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }


    fun getRemoteView(title: String, description: String): RemoteViews{

        val remoteView = RemoteViews("com.google.firebase.messaging.FirebaseMessagingService", R.layout.notification_layout)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.description,description)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.slug)

        return remoteView
    }

    fun generateNotification(title: String, description: String){

        val intent = Intent(this, SlugletActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val PendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var Builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelID)
            .setSmallIcon(R.drawable.slug)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(PendingIntent)

        Builder = Builder.setContent(getRemoteView(title,description))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, Builder.build())
    }
    companion object {
        const val channelID = "notification_channel"
        const val channelName = "SlugNotification"
    }
}
