package com.example.ryhtmicapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlin.system.exitProcess

class Notificationreceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action)
        {
            Notification.PREV-> {
                nextandprevsongposition(false,context=context!!)
            }
            Notification.NEXT->{
                nextandprevsongposition(true,context=context!!)
            }
            Notification.PLAY-> if(musicActivity.isplaying) pausemusic() else playmusic()
            Notification.exit-> {
                musicActivity.musicServices!!.stopForeground(true)
                musicActivity.musicServices=null


            }
        }

    }
    private fun playmusic()
    {
        musicActivity.isplaying=true
        musicActivity.musicServices!!.mediaPlayer?.start()
        musicActivity.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
        musicActivity.binding.pauseplayer.setImageResource((R.drawable.ic_baseline_pause_24))

    }
    private fun pausemusic()
    {
        musicActivity.isplaying=false
        musicActivity.musicServices!!.mediaPlayer?.pause()
        musicActivity.musicServices!!.shownotification(R.drawable.ic_baseline_play_arrow_24)
        musicActivity.binding.pauseplayer.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    private fun nextandprevsongposition(increment:Boolean,context: Context)
    {
     setsongposition(increment=increment)

        musicActivity.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
         musicActivity.musicServices!!.mymediaplayer()
        Glide.with(context).load(musicActivity.musicListPA[musicActivity.songposition].arturi).apply { placeholder(R.drawable.ic_baseline_music_note_24) }.into(
            musicActivity.binding.songinimageplayer)
        Glide.with(context).load(musicActivity.musicListPA[musicActivity.songposition].backarturi).apply { placeholder(R.drawable.guitar )}.into(
            musicActivity.binding.bg)

        musicActivity.binding.songnameplayer.text= musicActivity.musicListPA[musicActivity.songposition].title
        playmusic()
        musicActivity.favindex= musicActivity.musicListPA[musicActivity.songposition].id?.let {
            checkFavourite(
                it
            )
        }!!
        if(musicActivity.isFavourite)
            musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
        else  musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)

    }
}