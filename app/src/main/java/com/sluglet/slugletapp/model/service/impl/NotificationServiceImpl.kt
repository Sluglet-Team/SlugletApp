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

    /**
     * Main function that handles displaying notification, It also schedules another alarm for the same time next week
     */
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
            .setSmallIcon(R.mipmap.ic_launcher_slug)
            .setContentTitle(NOTIFICATION_NAME)
            .setContentText(NOTIFICATION_MESSAGE)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)

        val nextWeek = Calendar.getInstance()
        val dayOfWeek = nextWeek.get(Calendar.DAY_OF_WEEK)
        val hourOfDay = nextWeek.get(Calendar.HOUR_OF_DAY)
        val minute = nextWeek.get(Calendar.MINUTE)
        nextWeek.add(Calendar.DAY_OF_YEAR, 7)
        scheduleNotificationAtTime(nextWeek.get(Calendar.DAY_OF_WEEK), hourOfDay, minute)

    }

    /**
     * main function that handles scheduling alarms, takes in day and time to schedule it. It schedules the
     * alarm 20 minutes before a class.
     */
    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleNotificationAtTime(day: Int, hour: Int, minute: Int) {
        val requestCode = day * 100 + hour * 10 + minute
        val intent = Intent(context, BroadcastReceiverImpl::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = Calendar.getInstance()

        val alarmTime: Calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        // TO check if the time is before the current date in the week and adds 7 to schedule it for next week
        if (alarmTime.before(now)) {
            alarmTime.add(Calendar.DATE, 7)
        }

        // Check if the alarm time is within 1 minute of the current time, this is done so that when another
        // alarm is scheduled by notification it sets it for the next week
        val timeDifferenceInMillis = alarmTime.timeInMillis - now.timeInMillis
        val isWithin1Minute = timeDifferenceInMillis in 0..(1 * 60 * 1000)

        if (isWithin1Minute) {
            // If within 1 minute, add 7 days to the alarm time
            alarmTime.add(Calendar.DATE, 7)
        }
        // sending reminder 20 min before the class
       alarmTime.add(Calendar.MINUTE, -20)

        Log.v("Calendar millis", "${alarmTime.timeInMillis}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            pendingIntent
        )
    }

    companion object {
        const val COURSE_CHANNEL_ID = "COURSE_CHANNEL"
        const val CHANNEL_NAME = "SlugNotification"
        const val NOTIFICATION_NAME = "Course Notification"
        const val NOTIFICATION_MESSAGE = "You have class in 20min!"
    }

}
