package com.example.kotlin_assignment_eighteen.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        Log.d("TAG", remoteMessage.data.toString())
        val notificationBuilder: NotificationCompat.Builder

        // handle notif from console firebase
        if (remoteMessage.data.isNullOrEmpty()) {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(remoteMessage.notification?.title)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentText(remoteMessage.notification?.body)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        } else { //handle notif from user input
            val dataNotif = JSONObject(remoteMessage.data.toString()).getJSONObject("data")
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(dataNotif.getString("title"))
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentText(dataNotif.getString("message"))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        }

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "tes",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, notificationBuilder.build())
    }

}