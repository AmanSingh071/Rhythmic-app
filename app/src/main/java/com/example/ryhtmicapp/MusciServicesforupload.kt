package com.example.ryhtmicapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import android.util.Base64
import android.util.Base64.decode
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.net.URL

class MusciServicesforupload: Service() {
    private var mybinder=MyBinder()
    var mediaPlayer: MediaPlayer?=null

    private lateinit var mediasession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder {
        mediasession= MediaSessionCompat(baseContext,"Music")
        return mybinder
    }
    inner class MyBinder: Binder(){
        fun currentservice(): MusciServicesforupload {
            return this@MusciServicesforupload
        }
    }
    lateinit var notification:NotificationCompat.Builder
    fun shownotification(playpausebutton:Int)
    {   val prevIntent=Intent(baseContext,Notificationreceiverupload::class.java).setAction(Notification2.PREV)
        val prevpendingIntent= PendingIntent.getBroadcast(baseContext,0,prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,Notificationreceiverupload::class.java).setAction(Notification2.exit)
        val exitpendingIntent= PendingIntent.getBroadcast(baseContext,0,exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,Notificationreceiverupload::class.java).setAction(Notification2.NEXT)
        val nextpendingIntent= PendingIntent.getBroadcast(baseContext,0,nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,Notificationreceiverupload::class.java).setAction(Notification2.PLAY)
        val playpendingIntent= PendingIntent.getBroadcast(baseContext,0,playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

       /* var a=Glide.with(this).load(musicActivityforupload.listmusic[musicActivityforupload.songposition].imguri).apply { RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24) }.into(
            musicActivityforupload.binding2.songinimageplayer)
        val imgart=musicActivityforupload.listmusic[musicActivityforupload.songposition].imguri?.let { getimgart(it) }
        val img=if(imgart!=null)
        {
            BitmapFactory.decodeByteArray(imgart,0,imgart.size)
        }
        else
        {
            BitmapFactory.decodeResource(resources,R.drawable.ic_baseline_music_note_24)
        }*/



        var notification= NotificationCompat.Builder(this, Notification.CHANNEL_ID)
            .setContentTitle( musicActivityforupload .listmusic[musicActivityforupload.songposition].songname)
            .setContentText( musicActivityforupload .listmusic[musicActivityforupload.songposition].songname)
            .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
            /*  .setLargeIcon(image)*/
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediasession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setLargeIcon(img())
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_baseline_arrow_back_24,"Previous",prevpendingIntent)
             .addAction(playpausebutton,"Play",playpendingIntent)
            .addAction(R.drawable.ic_baseline_arrow_forward_24,"forward",nextpendingIntent)
            .addAction(R.drawable.ic_baseline_exit_to_app_24,"exit",exitpendingIntent)
            .build()



// Notification ID cannot be 0.
        startForeground(13, notification)



    }




    fun img(): Bitmap? {

        val bitmap:Bitmap=BitmapFactory.decodeResource( resources,R.drawable.guitar)
        return bitmap
    }


    fun mymediaplayer()
    {
        try {
            if(musicActivityforupload.musicServices!!.mediaPlayer==null) musicActivityforupload.musicServices!!.mediaPlayer= MediaPlayer()
            musicActivityforupload.musicServices!!.mediaPlayer!!.reset()
            musicActivityforupload.musicServices!!.mediaPlayer!!.setDataSource(musicActivityforupload.listmusic[musicActivityforupload.songposition].songurl)
            musicActivityforupload.musicServices!!.mediaPlayer!!.prepare()

            musicActivityforupload.binding2.pauseplayer.setImageResource(R.drawable.ic_baseline_pause_24)
            musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
            musicActivityforupload.binding2.seekbarstarttime.text= formatduration(musicActivityforupload.musicServices!!.mediaPlayer!!.currentPosition.toLong())
            musicActivityforupload.binding2.seebarendtime.text= formatduration(musicActivityforupload.musicServices!!.mediaPlayer!!.duration.toLong())
            musicActivityforupload.binding2.seekbar.progress=0 //initial progress
            musicActivityforupload.binding2.seekbar.max= musicActivityforupload.musicServices!!.mediaPlayer!!.duration
        }
        catch (e: Exception)
        {
            return
        }
    }
}