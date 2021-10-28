package com.example.kotlin_assignment_eighteen.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.activity.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val intentToMain = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 1, intentToMain, PendingIntent.FLAG_ONE_SHOT)
        val channelId = context?.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context!!, channelId!!)
            .setContentTitle("To-Do List App")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentText("Yuk cek To-Do Listmu Hari Ini")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "tes",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, notificationBuilder.build())
    }
}