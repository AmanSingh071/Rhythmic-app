package com.example.ryhtmicapp

import android.media.MediaMetadataRetriever
import io.grpc.InternalChannelz.id
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

data class storemusicclass(
    val id: String?="",
    val title: String?="",
    val duration: Long=0,
    val album: String?="",
    val path: String?="",
    val artist: String?="",
    val arturi:String?="",
    val backarturi:String?="",
    var userId: String?="",
    var votesCount : Int = 0,
    )
fun formatduration(duration: Long):String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MILLISECONDS))
    return String.format("%02d:%02d",minutes,seconds)
}
fun getimgart(path: String): ByteArray? {
    val retriever=MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun setsongposition(increment:Boolean)
{
    if(increment)
    {
        if(musicActivity.songposition == musicActivity.musicListPA.size-1)
            musicActivity.songposition =0
        else
            ++musicActivity.songposition
    }
    else
        if(musicActivity.songposition ==0)
            musicActivity.songposition = musicActivity.musicListPA.size-1
        else
            --musicActivity.songposition

}
fun checkFavourite(id: String):Int{
    musicActivity.isFavourite=false
    facvoriteActivity.favouritesong.forEachIndexed { index, storemusicclass ->
        if(id==storemusicclass.id){
            musicActivity.isFavourite=true
            return index
    }

    }
    return -1

}