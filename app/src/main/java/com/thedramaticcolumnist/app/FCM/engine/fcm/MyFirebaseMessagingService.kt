package com.thedramaticcolumnist.app.FCM.engine.fcm

import android.annotation.TargetApi
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.ui.SplashScreen


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseService"

    }

    var NOTIFICATION_CHANNEL_ID = 1234
    var title = ""
    var subject: String? = ""
    var body: String? = ""
    var ewouid: String? = ""
    var type: String? = ""
    var defaultSoundUri: Uri? = null
    var pendingIntent: PendingIntent? = null
    var intent: Intent? =null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "onMessageReceived: $remoteMessage")

        receiveData(remoteMessage)
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message Notification Body: ${notification.body}")
            Log.d(TAG, "Message Notification Message: ${remoteMessage.data.getValue("Message")}")
            Log.d(TAG, "Message Notification type: ${remoteMessage.data.getValue("type")}")
        }
    }


    private fun receiveData(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            title = remoteMessage.data["Message"]!!
            type = remoteMessage.data["type"]

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationAboveOreo(1)
            } else {
                createNotificationBelowOreo(1)
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationAboveOreo(Flag: Int) {
        try {
            if (Flag == 1) {
                defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
                r.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        intent = Intent(this, SplashScreen::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        pendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_CHANNEL_ID,
            intent,
            PendingIntent.FLAG_ONE_SHOT)
        val name: CharSequence = "EasyButor"
        val description = "Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID.toString(), name, importance)
        channel.description = description
        val notificationManage = getSystemService(
            NotificationManager::class.java)
        notificationManage.createNotificationChannel(channel)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID.toString())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(subject + "\n" + body))
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_CHANNEL_ID, builder.build())
    }

    private fun createNotificationBelowOreo(Flag: Int) {
        try {
            if (Flag == 1) {
                defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
                r.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        intent = Intent(this, SplashScreen::class.java)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        pendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_CHANNEL_ID,
            intent,
            PendingIntent.FLAG_ONE_SHOT)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(resources.getColor(R.color.amazon))
            .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subject + "\n" + body))
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        mNotificationManager.notify(NOTIFICATION_CHANNEL_ID, mBuilder.build())
    }
}