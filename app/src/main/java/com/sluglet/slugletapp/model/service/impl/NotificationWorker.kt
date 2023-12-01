//import android.annotation.SuppressLint
//import android.app.AlarmManager
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.widget.RemoteViews
//import androidx.core.app.NotificationCompat
//import com.sluglet.slugletapp.R
//import com.sluglet.slugletapp.SlugletActivity
//import android.util.Log
//import com.sluglet.slugletapp.screens.search.SearchScreen
//import java.util.Calendar
//
//
//class NotificationWorker(private val context: Context) {
//
//    private val channelName = "SlugNotification"
//    private  val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//    fun showNotification(){
//        val activityIntent = Intent(context, SlugletActivity::class.java)
//        val activityPendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            activityIntent,
//            // FIXME(TANUJ): Current version of product makes this check redundant
//            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
//        )
//        val notification = NotificationCompat.Builder(context, COURSE_CHANNEL_ID)
//            .setSmallIcon(R.drawable.slug)
//            .setContentTitle("Course Notification")
//            .setContentText("You have class soon!")
//            .setContentIntent(activityPendingIntent)
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
//
//    // FIXME(Tanuj): This should be done with compose, I will set it up
//    private fun getRemoteView(title: String, description: String): RemoteViews {
//        val remoteView = RemoteViews(context.packageName, R.layout.notification_layout)
//        remoteView.setTextViewText(R.id.title, title)
//        remoteView.setTextViewText(R.id.description, description)
//        remoteView.setImageViewResource(R.id.app_logo, R.drawable.slug)
//        return remoteView
//    }
//
//    @SuppressLint("ScheduleExactAlarm")
//    fun scheduleNotificationAtTime() {
//
//        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            1,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 42)
//        }
//
//        alarmManager.setExact(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//    }
//
//    class NotificationBroadcastReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            if (context != null) {
//                val notificationWorker = NotificationWorker(context)
//                notificationWorker.showNotification()
//            }
//        }
//    }
//
//    companion object {
//        const val COURSE_CHANNEL_ID = "COURSE_CHANNEL"
//        const val REQUEST_CODE = 123
//    }
//
//
//
////    fun generateNotification(title: String, description: String) {
////        val intent = Intent(context, SlugletActivity::class.java)
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////
////        val pendingIntent = PendingIntent.getActivity(
////            context,
////            0,
////            intent,
////            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
////        )
////
////        var builder: NotificationCompat.Builder = NotificationCompat.Builder(context, COURSE_CHANNEL_ID)
////            .setSmallIcon(R.drawable.slug)
////            .setAutoCancel(true)
////            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
////            .setOnlyAlertOnce(true)
////            .setContentIntent(pendingIntent)
////
////        builder = builder.setContent(getRemoteView(title, description))
////
////        val notificationManager =
////            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            val notificationChannel = NotificationChannel(
////                COURSE_CHANNEL_ID,
////                channelName,
////                NotificationManager.IMPORTANCE_HIGH
////            )
////            notificationManager.createNotificationChannel(notificationChannel)
////        }
////        notificationManager.notify(0, builder.build())
////    }
//
//
//}
