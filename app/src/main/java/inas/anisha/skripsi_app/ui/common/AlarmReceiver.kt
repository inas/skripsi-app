package inas.anisha.skripsi_app.ui.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.ui.main.MainActivity
import inas.anisha.skripsi_app.ui.main.schedule.schedule.ScheduleDetailActivity
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    private var channelLow: NotificationChannel? = null
    private var channelDefault: NotificationChannel? = null
    private var channelHigh: NotificationChannel? = null

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        when (intent.action) {
            ACTION_REMINDER -> createReminderNotification(context, intent)
            ACTION_NOTIFICATION -> createNotification(context, intent)
            else -> return
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importanceLow = NotificationManager.IMPORTANCE_LOW
            channelLow = NotificationChannel(
                CHANNEL_PRIORITY_LOW,
                CHANNEL_PRIORITY_LOW,
                importanceLow
            )
            channelLow?.let { notificationManager.createNotificationChannel(it) }

            val importanceDefault = NotificationManager.IMPORTANCE_DEFAULT
            channelDefault = NotificationChannel(
                CHANNEL_PRIORITY_DEFAULT,
                CHANNEL_PRIORITY_DEFAULT,
                importanceDefault
            )
            channelDefault?.let { notificationManager.createNotificationChannel(it) }

            val importanceHigh = NotificationManager.IMPORTANCE_HIGH
            channelHigh = NotificationChannel(
                CHANNEL_PRIORITY_HIGH,
                CHANNEL_PRIORITY_HIGH,
                importanceHigh
            )
            channelHigh?.let { notificationManager.createNotificationChannel(it) }
        }
    }

    private fun createReminderNotification(context: Context, intent: Intent) {
        val scheduleTime = intent.getLongExtra(EXTRA_TIME, 0L)
        if (scheduleTime == 0L || Calendar.getInstance().timeInMillis > scheduleTime) return

        val scheduleId = intent.getIntExtra(EXTRA_SCHEDULE_ID, 0).toLong()
        val title = intent.getStringExtra(EXTRA_TITLE)
        val content = intent.getStringExtra(EXTRA_CONTENT)
        val isPopup = intent.getBooleanExtra(EXTRA_POPUP, false)

        val mainActivity = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val detailActivity = Intent(context, ScheduleDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(ScheduleDetailActivity.EXTRA_ID, scheduleId)
        }


        val pendingIntent = PendingIntent.getActivities(
            context, 154, arrayOf(mainActivity, detailActivity),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = if (isPopup) CHANNEL_PRIORITY_HIGH else CHANNEL_PRIORITY_DEFAULT

        //Build the notification
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        builder.priority =
            if (isPopup) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT

        //Deliver the notification
        notificationManager.notify(scheduleId.toInt(), builder.build())
    }

    fun createNotification(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val content = intent.getStringExtra(EXTRA_CONTENT)

        val mainActivity = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivities(
            context, 154, arrayOf(mainActivity),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Build the notification
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        builder.priority = NotificationCompat.PRIORITY_DEFAULT

        //Deliver the notification
        notificationManager.notify(CYCLE_NOTIFICATION, builder.build())
    }

    companion object {
        const val CHANNEL_PRIORITY_LOW = "CHANNEL_PRIORITY_LOW"
        const val CHANNEL_PRIORITY_DEFAULT = "CHANNEL_PRIORITY_DEFAULT"
        const val CHANNEL_PRIORITY_HIGH = "CHANNEL_PRIORITY_HIGH"

        const val ACTION_REMINDER = "ACTION_REMINDER"
        const val ACTION_NOTIFICATION = "ACTION_NOTIFICATION"

        const val EXTRA_TIME = "EXTRA_TIME"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_CONTENT = "EXTRA_CONTENT"
        const val EXTRA_POPUP = "EXTRA_POPUP"
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"

        const val CYCLE_NOTIFICATION = 371
    }
}