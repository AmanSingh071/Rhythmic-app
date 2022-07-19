package com.example.ryhtmicapp

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo

import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ryhtmicapp.databinding.ActivityFrontpageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlin.system.exitProcess


class frontpage : AppCompatActivity() {
    private lateinit var binding:ActivityFrontpageBinding
    private var GFG_URI = "https://www.pagalworld.pw/"
    private lateinit var list :ArrayList<storemusicclass>


    private var package_name = "com.android.chrome";
    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var MusicAdapter: musicAdapter
    companion object {
        lateinit var MusicListActivity:ArrayList<storemusicclass>

        lateinit var searchmusiclist :ArrayList<storemusicclass>
        var search:Boolean=false


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toogle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toogle)
        toogle.syncState()

        // to make the Navigation drawer icon always appear on the action bar

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(requestruntimepermission()) {
            callrecycleview()
            //   retrieving data from  device using shared preferences
            facvoriteActivity.favouritesong= ArrayList()
            val editor=getSharedPreferences("Favourite", MODE_PRIVATE)
            val jsonstring=editor.getString("FavouriteSons",null)
            val typToken=object :TypeToken<ArrayList<storemusicclass>>(){}.type
            if(jsonstring!=null)
            {
                val data:ArrayList<storemusicclass> = GsonBuilder().create().fromJson(jsonstring,typToken)
                facvoriteActivity.favouritesong.addAll(data)
            }

        }





        binding.shufflebtnfrontpage.setOnClickListener{
            val intent= Intent(this, musicActivity::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","Frontpage")
            startActivity(intent)


        }
        binding.fav.setOnClickListener{
            val intent= Intent(this, facvoriteActivity::class.java)
            startActivity(intent)


        }
        binding.navbar.setNavigationItemSelectedListener {
            val intent= Intent(this, loginActivity::class.java)
            when (it.itemId)
            {
                R.id.settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                    R.id.feedback -> Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show()
                R.id.about -> Toast.makeText(this, "about", Toast.LENGTH_SHORT).show()

                R.id.exit -> {Toast.makeText(this, "log out successfully", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent (this, loginActivity::class.java)
                    startActivity(intent)
                    musicActivity.musicServices!!.mediaPlayer!!.reset()
                        musicActivityforupload.musicServices!!.mediaPlayer!!.reset()
                }
                R.id.upload->{Toast.makeText(this, "Upload Music", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, upload_activity::class.java)
                    startActivity(intent)


                }


            }
            true

        }




        binding.playlist.setOnClickListener {
            val intent = Intent(this, playlistActivity::class.java)
            startActivity(intent)
        }
        binding.opentab.setOnClickListener {
            Toast.makeText(this, "Dowload songs from this website", Toast.LENGTH_SHORT).show()
            val builder = CustomTabsIntent.Builder()

            // to set the toolbar color use CustomTabColorSchemeParams
            // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(this, R.color.blue))
            builder.setDefaultColorSchemeParams(params.build())

            // shows the title of web-page in toolbar
            builder.setShowTitle(true)

            // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

            // To modify the close button, use
            // builder.setCloseButtonIcon(bitmap)

            // to set weather instant apps is enabled for the custom tab or not, use
            builder.setInstantAppsEnabled(true)

            //  To use animations use -
            //  builder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
            //  builder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
            val customBuilder = builder.build()

            if (this.isPackageInstalled(package_name)) {
                // if chrome is available use chrome custom tabs
                customBuilder.intent.setPackage(package_name)
                customBuilder.launchUrl(this, Uri.parse(GFG_URI))
            } else {
                // if not available use WebView to launch the url
            }
        }



            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button

            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button






    }




    private fun callrecycleview()

    { val music_list=ArrayList<String>()
        search=false
        MusicListActivity=getsong()

        binding.recyclerviewid.layoutManager = LinearLayoutManager(this)

        MusicAdapter=musicAdapter(this, MusicListActivity)
        binding.recyclerviewid.adapter=MusicAdapter
        binding.totalsongs.text="Total Songs :" + MusicAdapter.itemCount

    }
    private fun requestruntimepermission():Boolean{
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this , arrayOf(WRITE_EXTERNAL_STORAGE), 13)
            return false
        }

           return true
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                callrecycleview()


            }
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
        }
    }
    override fun   onOptionsItemSelected(item: MenuItem):Boolean {

        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    fun getsong():ArrayList<storemusicclass>
    {
        val templist= ArrayList<storemusicclass>()
        val firebaseuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val rootref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val userid: String = firebaseuser?.uid!!
        var votecount=0
       val selection =

                MediaStore.Audio.Media.IS_MUSIC +"!=0"

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID//path
        )
        val cursor=this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,null

        )
        if(cursor!=null){
            if(cursor.moveToFirst())
                do {

                    val idC= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val titleC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val durationC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val pathC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val artistC =cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val albumC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumidC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uriC = Uri.parse("content://media/external/audio/albumart")
                    val artUric=Uri.withAppendedPath(uriC,albumidC).toString()
                    val backuriC = Uri.parse("content://media/external/audio/albumart")
                    val backartUric=Uri.withAppendedPath(backuriC,albumidC).toString()
                    //make local object
                    val music= storemusicclass( id = idC,
                        title =titleC, album = albumC, artist =artistC, path = pathC, duration = durationC, arturi =artUric,
                        backarturi =backartUric, userId = userid, votesCount = votecount)
                    val file = File(music.path)
                    if(file.exists())
                        templist.add(music)

                }while (cursor.moveToNext())
                cursor.close()
                // Use an ID column from the projection to get
                // a URI representing the media item itself.

        }
        return templist

    }

    override fun onDestroy() {
        super.onDestroy()
        if(!musicActivity.isplaying && musicActivity.musicServices!=null )
        {
            exitProcess(1)
        }

    }

    override fun onRestart() {
        super.onRestart()
        //converting fav songs array list to jason and storing in device using shared prefrences
        val editor=getSharedPreferences("Favourite", MODE_PRIVATE).edit()
        val jsonstring= GsonBuilder().create().toJson(facvoriteActivity.favouritesong)
        editor.putString("FavouriteSons",jsonstring)
        editor.apply()
    }
    fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.searchbar, menu)
         val searchView: SearchView = menu?.findItem(R.id.searchBar)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean=true


            override fun onQueryTextChange(newText: String?): Boolean{
                searchmusiclist= ArrayList()
                 if(newText!=null)
                 {
                     val userinput=newText.lowercase()
                     for (song in MusicListActivity)
                         if(song.title?.lowercase()?.contains(userinput) == true)
                             searchmusiclist.add(song)
                     search=true
                     MusicAdapter.updatemusiclistonsearch(searchlist = searchmusiclist)

                 }
                 return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}