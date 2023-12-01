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
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.SlugletActivity
import com.sluglet.slugletapp.model.service.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class NotificationServiceImpl @Inject constructor(
    private val context: Context,
) : NotificationService {

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

    private  val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // FIXME: maybe move to slugletactivity instead of init
    init {
        createNotificationChannel()
    }
     fun createNotificationChannel() {
         val notificationChannel = NotificationChannel(
             COURSE_CHANNEL_ID,
             "Course Notification",
             NotificationManager.IMPORTANCE_HIGH
         )
         val notificationManager =
             context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.createNotificationChannel(notificationChannel)
     }

    fun showNotification(){
        val activityIntent = Intent(context, SlugletActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, COURSE_CHANNEL_ID)
            .setSmallIcon(R.drawable.slug)
            .setContentTitle("Course Notification")
            .setContentText("You have class soon!")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotificationAtTime() {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 42)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    companion object {
        const val COURSE_CHANNEL_ID = "COURSE_CHANNEL"
        const val CHANNEL_NAME = "SlugNotification"
        const val REQUEST_CODE = 123
    }

}
