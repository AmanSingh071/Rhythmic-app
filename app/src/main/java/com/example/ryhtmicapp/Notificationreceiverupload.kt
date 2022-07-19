package com.example.ryhtmicapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ryhtmicapp.Notification.Companion.CHANNEL_ID
import com.example.ryhtmicapp.musicActivityforupload.Companion.songposition
import com.bumptech.glide.Glide

class Notificationreceiverupload : BroadcastReceiver() {
    override fun onReceive( context: Context?, intent: Intent?) {
    when(intent?.action)
    {
        Notification2.PREV-> {
            nextandprevsongposition(false,context=context!!)
        }
        Notification2.NEXT->{
            nextandprevsongposition(true,context=context!!)
        }
        Notification2.PLAY-> if(musicActivityforupload.isplaying) pausemusic() else playmusic()
        Notification2.exit-> {
            musicActivityforupload.musicServices!!.stopForeground(true)
            musicActivityforupload.musicServices=null


        }
    }

}
private fun playmusic()
{
    musicActivityforupload.isplaying=true
    musicActivityforupload.musicServices!!.mediaPlayer?.start()
    musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
    musicActivityforupload.binding2.pauseplayer.setImageResource((R.drawable.ic_baseline_pause_24))

}
private fun pausemusic()
{
    musicActivityforupload.isplaying=false
    musicActivityforupload.musicServices!!.mediaPlayer?.pause()
    musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_play_arrow_24)
    musicActivityforupload.binding2.pauseplayer.setImageResource(R.drawable.ic_baseline_play_arrow_24)
}

private fun nextandprevsongposition(increment:Boolean,context: Context)
{
    setsongposition2(increment=increment)

    musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
    musicActivityforupload.musicServices!!.mymediaplayer()
    Glide.with(context).load(musicActivityforupload.listmusic[musicActivityforupload.songposition].imguri).apply { placeholder(R.drawable.ic_baseline_music_note_24) }.into(
        musicActivityforupload.binding2.songinimageplayer)
    Glide.with(context).load(musicActivityforupload.listmusic[musicActivityforupload.songposition].imguri).apply { placeholder(R.drawable.guitar )}.into(
        musicActivityforupload.binding2.bg)

    musicActivityforupload.binding2.songnameplayer.text= musicActivityforupload.listmusic[musicActivityforupload.songposition].songname
    playmusic()/*
    musicActivityforupload.favindex= musicActivityforupload.listmusic[musicActivityforupload.songposition].id?.let {
        checkFavourite(
            it
        )
    }!!
    if(musicActivity.isFavourite)
        musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
    else  musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)*/

}
}