package com.sluglet.slugletapp.model.service.impl

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.SlugletActivity
import com.sluglet.slugletapp.model.service.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

class NotificationServiceImpl @Inject constructor(
    private val context: Context,
) : NotificationService {

    private  val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmManager = context.getSystemService(AlarmManager::class.java)

    // FIXME: maybe move to slugletactivity instead of init
    init {
        createNotificationChannel()
    }
     override fun createNotificationChannel() {
         val notificationChannel = NotificationChannel(
             COURSE_CHANNEL_ID,
             NOTIFICATION_NAME,
             NotificationManager.IMPORTANCE_HIGH
         )
         val notificationManager =
             context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.createNotificationChannel(notificationChannel)
     }

    override fun showNotification(){
        Log.v("HERE", "HERE")
        val activityIntent = Intent(context, SlugletActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            2,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, COURSE_CHANNEL_ID)
            .setSmallIcon(R.drawable.slug)
            .setContentTitle(NOTIFICATION_NAME)
            .setContentText(NOTIFICATION_MESSAGE)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleNotificationAtTime(hour: Int, minute: Int) {
        val intent = Intent(context, BroadcastReceiverImpl::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        Log.v("Calendar millis", "${calendar.timeInMillis}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    companion object {
        const val COURSE_CHANNEL_ID = "COURSE_CHANNEL"
        const val CHANNEL_NAME = "SlugNotification"
        const val NOTIFICATION_NAME = "Course Notification"
        const val NOTIFICATION_MESSAGE = "You have class soon!"
        const val REQUEST_CODE = 123
    }

}
