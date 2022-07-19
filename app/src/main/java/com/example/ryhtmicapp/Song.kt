package com.example.ryhtmicapp

import android.media.MediaMetadataRetriever
import android.net.Uri
import com.google.firebase.storage.UploadTask
import java.util.concurrent.TimeUnit


data class Song(

    var songname: String? = "",
    var songurl: String?="",
    var id: Int=0,
    var imguri: String?="",
    var userId: String?="",
    var likes: Int = 0,



    )



fun formatduration2(duration: Long):String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)-minutes* TimeUnit.SECONDS.convert(1, TimeUnit.MILLISECONDS))
    return String.format("%02d:%02d",minutes,seconds)
}

fun setsongposition2(increment:Boolean)
{
    if(increment)
    {
        if(musicActivityforupload.songposition == musicActivityforupload.listmusic.size-1)
            musicActivityforupload.songposition =0
        else
            ++musicActivityforupload.songposition
    }
    else
        if(musicActivityforupload.songposition ==0)
            musicActivityforupload.songposition = musicActivityforupload.listmusic.size-1
        else
            --musicActivityforupload.songposition

}
/*fun checkFavourite2(id: String):Int{
    musicActivityforupload.isFavourite=false
    facvoriteActivity.favouritesong.forEachIndexed { index, storemusicclass ->
        if(id==storemusicclass.id){
            musicActivity.isFavourite=true
            return index
        }

    }
    return -1

}*/








