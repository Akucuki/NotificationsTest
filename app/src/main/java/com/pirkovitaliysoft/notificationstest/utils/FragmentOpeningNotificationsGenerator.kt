package com.pirkovitaliysoft.notificationstest.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pirkovitaliysoft.notificationstest.CHANNEL_ID
import com.pirkovitaliysoft.notificationstest.FRAGMENT_POSITION
import com.pirkovitaliysoft.notificationstest.MainActivity
import com.pirkovitaliysoft.notificationstest.R

object FragmentOpeningNotificationsGenerator {
    fun generateNotificationForFragment(context: Context, fragmentPosition: Int) {
        createNotificationChannel(context)

        val intent = getIntentForFragmentPosition(context, fragmentPosition)
        val pendingIntent = PendingIntent.getActivity(context, fragmentPosition, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val notification = buildNotification(context, fragmentPosition, pendingIntent)

        NotificationManagerCompat.from(context).notify(fragmentPosition, notification)
    }

    fun removeNotificationForFragment(context: Context, fragmentPosition: Int) {
        NotificationManagerCompat.from(context).cancel(fragmentPosition)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val name = context.getString(R.string.app_notifications_channel_name)
        val descriptionText = context.getString(R.string.app_notifications_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getNotificationManager(context)
        notificationManager.createNotificationChannel(channel)
    }

    private fun getIntentForFragmentPosition(
        context: Context,
        fragmentPosition: Int
    ): Intent {
        return Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(FRAGMENT_POSITION, fragmentPosition)
        }
    }

    private fun buildNotification(
        context: Context,
        fragmentPosition: Int,
        pendingIntent: PendingIntent?
    ): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_notifications_title))
            .setContentText("${context.getString(R.string.app_notifications_text)} ${fragmentPosition + 1}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        return builder.build()
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}