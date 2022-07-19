package com.example.ryhtmicapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class Notification2:Application() {
    companion object
    {
        const val CHANNEL_ID="channel"
        const val PLAY="play"
        const val exit="Exit"

        const val NEXT="next"
        const val PREV="previous"



    }
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel



            val myChannel = NotificationChannel(CHANNEL_ID, "Song Playing", NotificationManager.IMPORTANCE_HIGH)
            myChannel.description = "Important Channel"
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(myChannel)
        }

    }
}