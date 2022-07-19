package com.example.ryhtmicapp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ryhtmicapp.databinding.ActivityMusicBinding
import com.example.ryhtmicapp.databinding.ActivityUploadBinding

class musicActivityforupload: AppCompatActivity(),ServiceConnection {
    private lateinit var runnable:Runnable
    companion object
    {

        lateinit var listmusic:ArrayList<Song>
        var songposition:Int=0
     /*   var mediaPlayer: MediaPlayer?=null*/
        var musicServices:MusciServicesforupload?=null
        var isplaying : Boolean=false
        var isplayingupload: Boolean=false
        var islooping : Boolean=false
        lateinit var binding2: ActivityMusicBinding
        @SuppressLint("StaticFieldLeak")

        lateinit var binding: ActivityUploadBinding

        public var isFavourite:Boolean=false
        var favindex:Int=-1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivityMusicBinding.inflate(layoutInflater)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        val intent = Intent(this, MusciServicesforupload::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initialiszelayout()
        binding2.backarrow.setOnClickListener {
            val intent = Intent(this, frontpage::class.java)

            startActivity(intent)

        }
        //this will play song using media player
        binding2.pauseplayer.setOnClickListener {
            if(isplaying)
            {
                pausemusic()

            }
            else
            {
                playmusic()
            }
        }
        binding2.repeat.setOnClickListener {
            if(islooping)
            {
                stoploop()

            }
            else
            {
                startloop()
            }
        }
        binding2.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromuser: Boolean) {
                if(fromuser) musicServices!!.mediaPlayer!!.seekTo(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?)= Unit



            override fun onStopTrackingTouch(seekbar: SeekBar?)=Unit



        })
        binding2.nextsongbutton.setOnClickListener {
            nextsong(true)
        }
        binding2.prevsongbutton.setOnClickListener {
            nextsong(false)
        }
      /*  binding2.addfavbtn.setOnClickListener {
            favindex = listmusic[songposition].id?.let { it1 -> checkFavourite(it1) }!!
            if(musicActivity.isFavourite)
            {
                musicActivity.isFavourite =false
                musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                facvoriteActivity.favouritesong.removeAt(musicActivity.favindex)
            }
            else
            {
                musicActivity.isFavourite =true
                musicActivity.binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                facvoriteActivity.favouritesong.add(musicActivity.musicListPA[musicActivity.songposition])
            }

        }*/
        binding2.sharebtn.setOnClickListener {
            val shareIntent=Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.type="audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(listmusic[songposition].songurl))
            startActivity(Intent.createChooser(shareIntent,"Sharing Music File"))
        }
    }
    private fun  initialiszelayout() {
        songposition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {

            "uploadAdapter" -> {
                listmusic = ArrayList()
                listmusic.addAll(upload_activity.MusicListActivity)
                setplayerlayout()


                //this will display name and song image


            }



        }
    }
    private fun setplayerlayout()
    {
        Glide.with(this).load(listmusic[songposition].imguri).apply { RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24) }.into(
            binding2.songinimageplayer)

        Glide.with(this).load(listmusic[songposition].imguri).apply { RequestOptions().placeholder(R.drawable.guitar) }.override(25,25).into(
            binding2.bg)
        binding2.songnameplayer.text= listmusic[songposition].songname


    }


    private fun mymediaplayer()
    {
        try {
         /*   if(mediaPlayer ==null)  mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
             mediaPlayer!!.setDataSource( listmusic[ songposition].songurl)
           mediaPlayer!!.prepare()
            mediaPlayer!!.start()
             binding2.seekbarstarttime.text= formatduration(musicActivity.musicServices!!.mediaPlayer!!.currentPosition.toLong())
         binding2.seebarendtime.text= formatduration(musicActivity.musicServices!!.mediaPlayer!!.duration.toLong())
            binding2.seekbar.progress=0 //initial progress
            binding2.seekbar.max= musicActivity.musicServices!!.mediaPlayer!!.duration*/
            if(musicServices!!.mediaPlayer==null) musicServices!!.mediaPlayer= MediaPlayer()
            if(musicActivity.isplayingactivity)musicActivity.musicServices!!.mediaPlayer!!.reset()
            musicServices!!.mediaPlayer!!.reset()

          musicServices!!.mediaPlayer!!.setDataSource(listmusic[songposition].songurl)
            musicServices!!.mediaPlayer!!.prepare()
           musicServices!!.mediaPlayer!!.start()
         isplaying =true
            isplayingupload=true
             binding2.pauseplayer.setImageResource(R.drawable.ic_baseline_pause_24)
            musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
             binding2.seekbarstarttime.text= formatduration(musicServices!!.mediaPlayer!!.currentPosition.toLong())
            binding2.seebarendtime.text= formatduration(musicServices!!.mediaPlayer!!.duration.toLong())
          binding2.seekbar.progress=0 //initial progress
            binding2.seekbar.max= musicServices!!.mediaPlayer!!.duration


        }
        catch (e:Exception)
        {
            Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
            return

        }
    }

    private fun playmusic()
    {
    binding2.pauseplayer.setImageResource((R.drawable.ic_baseline_pause_24))
        musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
        isplaying =true

        musicActivityforupload.musicServices!!.mediaPlayer!!.start()
    }
    private fun pausemusic()
    {
        binding2.pauseplayer.setImageResource((R.drawable.ic_baseline_play_arrow_24))
        musicActivityforupload.musicServices!!.shownotification(R.drawable.ic_baseline_play_arrow_24)

        isplaying =false
        musicActivityforupload.musicServices!!.mediaPlayer!!.pause()

    }
    private fun startloop()
    {
        musicServices!!.mediaPlayer!!.isLooping = true
        musicServices!!.mediaPlayer!!.start()
        islooping =true


        Toast.makeText(this,"Loop Activated",Toast.LENGTH_SHORT).show()

    }
    fun runnableseekbar()
    {
        runnable= kotlinx.coroutines.Runnable {
            binding2.seekbarstarttime.text =
                formatduration(musicServices!!.mediaPlayer!!.currentPosition.toLong())
            binding2.seekbar.progress = musicServices!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }
    private fun stoploop()
    {
        musicServices!!.mediaPlayer!!.isLooping = false
        musicServices!!.mediaPlayer!!.start()
        islooping =false

        Toast.makeText(this,"Loop stopped",Toast.LENGTH_SHORT).show()

    }

    private fun nextsong(increment:Boolean)
    {
        if(increment)
        {
            setsongposition2(true)
            setplayerlayout()
            mymediaplayer()
        }
        else
        {
            setsongposition2(false)
            setplayerlayout()
            mymediaplayer()
        }

    }




     override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
         val binder= service as MusciServicesforupload.MyBinder
      musicServices =binder.currentservice()
         //this will call mediaplayer to play song
         mymediaplayer()

         runnableseekbar()





     }

     override fun onServiceDisconnected(name: ComponentName?) {
          musicServices =null
     }
}