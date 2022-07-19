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
import kotlinx.coroutines.Runnable


class musicActivity : AppCompatActivity(),ServiceConnection {


    private lateinit var runnable:Runnable
    companion object
    {
        lateinit var musicListPA:ArrayList<storemusicclass>
        lateinit var listmusic:ArrayList<Song>
        var songposition:Int=0
        var mediaPlayer:MediaPlayer?=null
        var musicServices:MusicServices?=null
        var isplaying : Boolean=false
        var isplayingactivity : Boolean=false
        var islooping : Boolean=false
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMusicBinding
        lateinit var binding2: ActivityUploadBinding
        public var isFavourite:Boolean=false
        var favindex:Int=-1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        binding2 = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
//for starting background services
        val intent = Intent(this, MusicServices::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initialiszelayout()

        binding.backarrow.setOnClickListener {
            val intent = Intent(this, frontpage::class.java)

            startActivity(intent)

        }
        //this will play song using media player



        binding.pauseplayer.setOnClickListener {
            if(isplaying)
            {
                pausemusic()

            }
            else
            {
                playmusic()
            }
        }
        binding.repeat.setOnClickListener {
            if(islooping)
            {
                stoploop()

            }
            else
            {
                startloop()
            }
        }
      binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
          override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromuser: Boolean) {
              if(fromuser) musicServices!!.mediaPlayer!!.seekTo(progress)

          }

          override fun onStartTrackingTouch(seekBar: SeekBar?)= Unit



          override fun onStopTrackingTouch(seekbar: SeekBar?)=Unit



      })
        binding.nextsongbutton.setOnClickListener {
            nextsong(true)
        }
        binding.prevsongbutton.setOnClickListener {
            nextsong(false)
        }
        binding.addfavbtn.setOnClickListener {
            favindex= musicListPA[songposition].id?.let { it1 -> checkFavourite(it1) }!!
            if(isFavourite)
            {
                isFavourite=false
                binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                facvoriteActivity.favouritesong.removeAt(favindex)
            }
            else
            {
                isFavourite=true
                binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
                facvoriteActivity.favouritesong.add(musicListPA[songposition])
            }

        }
        binding.sharebtn.setOnClickListener {
            val shareIntent=Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.type="audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(musicListPA[songposition].path))
            startActivity(Intent.createChooser(shareIntent,"Sharing Music File"))
        }


        

    }
    private fun  initialiszelayout() {
        songposition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "musicAdapterSearch" -> {

                musicListPA = ArrayList()
                musicListPA.addAll(frontpage.searchmusiclist)
                setplayerlayout() //this will display name and song image



            }
            "musicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(frontpage.MusicListActivity)
                setplayerlayout() //this will display name and song image


            }
            "uploadAdapter" -> {
                listmusic  = ArrayList()
                listmusic.addAll(upload_activity.MusicListActivity)
               setplayerlayout2()


                //this will display name and song image


            }
            "FavouriteAdapter"->{
                musicListPA = ArrayList()
                musicListPA.addAll( facvoriteActivity.favouritesong)
                setplayerlayout()

            }
            "FavouriteShuffle"->{

                musicListPA= ArrayList()
                musicListPA.addAll(facvoriteActivity.favouritesong)
                musicListPA.shuffle()
                setplayerlayout()
                mymediaplayer()
            }
            "Frontpage"->{
                musicListPA= ArrayList()
                musicListPA.addAll(frontpage.MusicListActivity)
                musicListPA.shuffle()
                setplayerlayout()
                mymediaplayer()
            }


        }
    }
    private fun setplayerlayout()
    { favindex= musicListPA[songposition].id?.let { checkFavourite(it) }!!
        Glide.with(this).load(musicListPA[songposition].arturi).apply { RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24) }.into(binding.songinimageplayer)
        Glide.with(this).load(musicListPA[songposition].backarturi).apply { RequestOptions().placeholder(R.drawable.guitar) }.override(25,25).into(binding.bg)
        binding.songnameplayer.text= musicListPA[songposition].title
        if(isFavourite) binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            binding.addfavbtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }
    public fun setplayerlayout2()
    {
        Glide.with(this).load(listmusic[songposition].imguri).apply { RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24) }.into(binding.songinimageplayer)

        Glide.with(this).load(listmusic[songposition].imguri).apply { RequestOptions().placeholder(R.drawable.guitar) }.override(25,25).into(binding.bg)
        binding.songnameplayer.text= listmusic[songposition].songname
        mymediaplayer2()

    }
    private fun mymediaplayer()
    {
        try {
            if(musicServices!!.mediaPlayer==null) musicServices!!.mediaPlayer= MediaPlayer()
            if(musicActivityforupload.isplayingupload)musicActivityforupload.musicServices!!.mediaPlayer!!.reset()
            musicServices!!.mediaPlayer!!.reset()
            musicServices!!.mediaPlayer!!.setDataSource(musicListPA[songposition].path)
            musicServices!!.mediaPlayer!!.prepare()
            musicServices!!.mediaPlayer!!.start()
            isplaying=true
            isplayingactivity=true
            binding.pauseplayer.setImageResource(R.drawable.ic_baseline_pause_24)
            musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
            binding.seekbarstarttime.text= formatduration(musicServices!!.mediaPlayer!!.currentPosition.toLong())
            binding.seebarendtime.text= formatduration(musicServices!!.mediaPlayer!!.duration.toLong())
            binding.seekbar.progress=0 //initial progress
            binding.seekbar.max= musicServices!!.mediaPlayer!!.duration
        }
        catch (e:Exception)
        {
            Toast.makeText(this,"turn on install from unknown resources in the app settings",Toast.LENGTH_SHORT).show()
            return

        }
    }
    private fun mymediaplayer2()
    {
        try {
            if(mediaPlayer==null) mediaPlayer= MediaPlayer()
          mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(listmusic[songposition].songurl)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

        }
        catch (e:Exception)
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            return

        }
    }
    private fun playmusic()
    {
        binding.pauseplayer.setImageResource((R.drawable.ic_baseline_pause_24))
        musicServices!!.shownotification(R.drawable.ic_baseline_pause_24)
        isplaying=true
        musicServices!!.mediaPlayer!!.start()
    }
    private fun pausemusic()
    {
        binding.pauseplayer.setImageResource((R.drawable.ic_baseline_play_arrow_24))
        musicServices!!.shownotification(R.drawable.ic_baseline_play_arrow_24)
        isplaying=false
        musicServices!!.mediaPlayer!!.pause()

    }
    private fun startloop()
    {
        musicServices!!.mediaPlayer!!.isLooping = true
        musicServices!!.mediaPlayer!!.start()
        islooping=true


        Toast.makeText(this,"Loop Activated",Toast.LENGTH_SHORT).show()

    }
    fun runnableseekbar()
    {
        runnable= kotlinx.coroutines.Runnable {
            binding.seekbarstarttime.text =
                formatduration(musicActivity.musicServices!!.mediaPlayer!!.currentPosition.toLong())
            binding.seekbar.progress = musicActivity.musicServices!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }
    private fun stoploop()
    {
        musicServices!!.mediaPlayer!!.isLooping = false
        musicServices!!.mediaPlayer!!.start()
        islooping=false

        Toast.makeText(this,"Loop stopped",Toast.LENGTH_SHORT).show()

    }

    private fun nextsong(increment:Boolean)
    {
        if(increment)
        {
           setsongposition(true)
            setplayerlayout()
            mymediaplayer()
        }
        else
        {
            setsongposition(false)
            setplayerlayout()
            mymediaplayer()
        }

    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder= service as MusicServices.MyBinder
        musicServices=binder.currentservice()
      //this will call mediaplayer to play song
        mymediaplayer()
        runnableseekbar()





    }

    override fun onServiceDisconnected(name: ComponentName?) {
         musicServices=null
    }

}


