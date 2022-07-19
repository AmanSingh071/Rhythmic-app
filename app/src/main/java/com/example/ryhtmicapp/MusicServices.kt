package com.example.ryhtmicapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder

import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.ryhtmicapp.Notification.Companion.CHANNEL_ID
import com.example.ryhtmicapp.musicActivity.Companion.songposition
import java.lang.Exception


class MusicServices: Service() {


    private var mybinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null
    private lateinit var mediasession:MediaSessionCompat


    override fun onBind(intent: Intent?): IBinder {
        mediasession= MediaSessionCompat(baseContext,"Music2")
        return mybinder
    }
    inner class MyBinder:Binder(){
        fun currentservice():MusicServices{
            return this@MusicServices
        }
    }
    fun shownotification(playpausebutton:Int)
    {   val prevIntent=Intent(baseContext,Notificationreceiver::class.java).setAction(Notification.PREV)
        val prevpendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,Notificationreceiver::class.java).setAction(Notification.exit)
        val exitpendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,Notificationreceiver::class.java).setAction(Notification.NEXT)
        val nextpendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,Notificationreceiver::class.java).setAction(Notification.PLAY)
        val playpendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val imgart= musicActivity.musicListPA[musicActivity.songposition].path?.let { getimgart(it) }
        val img=if(imgart!=null)
        {
            BitmapFactory.decodeByteArray(imgart,0,imgart.size)
        }
        else
        {
            BitmapFactory.decodeResource(resources,R.drawable.ic_baseline_music_note_24)
        }

        val notification= NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(musicActivity.musicListPA[musicActivity.songposition].title)
            .setContentText(musicActivity.musicListPA[musicActivity.songposition].artist)
            .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
            .setLargeIcon(img)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediasession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_baseline_arrow_back_24,"Previous",prevpendingIntent)
            .addAction(playpausebutton,"Play",playpendingIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24,"forward",nextpendingIntent)
            .addAction(R.drawable.ic_baseline_exit_to_app_24,"exit",exitpendingIntent)

            .build()

// Notification ID cannot be 0.
        startForeground(13, notification)

    }



    fun mymediaplayer()
    {
        try {
            if(musicActivity.musicServices!!.mediaPlayer==null) musicActivity.musicServices!!.mediaPlayer= MediaPlayer()
            musicActivity.musicServices!!.mediaPlayer!!.reset()
            musicActivity.musicServices!!.mediaPlayer!!.setDataSource(musicActivity.musicListPA[songposition].path)
            musicActivity.musicServices!!.mediaPlayer!!.prepare()

            musicActivity.binding.pauseplayer.setImageResource(R.drawable.ic_baseline_pause_24)
            musicActivity.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
            musicActivity.binding.seekbarstarttime.text= formatduration(musicActivity.musicServices!!.mediaPlayer!!.currentPosition.toLong())
            musicActivity.binding.seebarendtime.text= formatduration(musicActivity.musicServices!!.mediaPlayer!!.duration.toLong())
            musicActivity.binding.seekbar.progress=0 //initial progress
            musicActivity.binding.seekbar.max= musicActivity.musicServices!!.mediaPlayer!!.duration
        }
        catch (e: Exception)
        {
            return
        }
    }




}