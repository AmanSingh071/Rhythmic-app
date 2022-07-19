package com.example.ryhtmicapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ryhtmicapp.databinding.ActivityMusciViewBinding

import com.example.ryhtmicapp.databinding.ActivityUploadBinding
import com.example.socialapp.uploadAdapter

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.net.URI
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class upload_activity : AppCompatActivity() {
    private lateinit var toogle: ActionBarDrawerToggle



     lateinit var songName: String

    var uploadIntent=Intent(
    )
    private val mystorageref=FirebaseStorage.getInstance().reference


    lateinit var urlSong:Uri
    private lateinit var dbref:DatabaseReference

    lateinit var songnamec :String
    var durationC by Delegates.notNull<Long>()
   lateinit var pathC :String
    lateinit var songurlc :String
    lateinit var albumC :String
      var idc :Int=0
    private var postId: Int=0
    var maxid:Int=9
    private  var position : Int = 0
    lateinit var albumidC  :String
    private var db:FirebaseFirestore=FirebaseFirestore.getInstance()
    private var ref:CollectionReference=db.collection("songs")
    lateinit var uriC :Uri

    private lateinit var postDao: PostDao
    lateinit var artistC:String
    lateinit var backuriC  :Uri
    lateinit var imguri:Uri
    lateinit var imguric:String
    lateinit var backartUric:String
 lateinit var urlsong:URI
    var templist= ArrayList<Song>()

    companion object{
        var MusicListActivity:ArrayList<Song> = ArrayList()
        lateinit var uri2 : Uri

    }
    private lateinit var binding: ActivityUploadBinding
    lateinit var binding2: ActivityMusciViewBinding
     lateinit var uri : Uri


    val resultcontract=  registerForActivityResult(ActivityResultContracts.GetContent()) {

        uri = it


          var mycursor: Cursor? = applicationContext.contentResolver.query(uri,null,null,null,null)
        var indexname= mycursor?.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)



        if (mycursor != null) {
            if (mycursor.moveToFirst())
                do{
                    songName= indexname?.let { it1 -> mycursor.getString(it1) }.toString()

                }while (mycursor.moveToNext())
            mycursor.close()

        }


     /*  val selection =

            MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val projection = arrayOf(

            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION

        )
        var cursor = this.contentResolver.query(
           uri,
            projection,
            selection,
            null, null

        )
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {


                    songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    durationC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))











                    //make local object


                } while (cursor.moveToNext())
            cursor.close()
        }*/
            // Use an ID column from the projection to get
            // a URI representing the media item itself.


            uploadsongtostorage()



    }
    val resultcontract2=  registerForActivityResult(ActivityResultContracts.GetContent()) {

        uri2 = it
          binding2.songimage.setImageURI(uri2)


        var storageref: StorageReference? =
            uri2.lastPathSegment?.let { FirebaseStorage.getInstance().getReference().child(it) }

        if (storageref != null) {
            storageref.putFile(uri2).addOnSuccessListener {
                var uriTask:Task<Uri> = it.storage.downloadUrl
                uriTask.addOnCompleteListener {
                    if(it.isSuccessful) {
                        var dowloaduri: Uri = it.result
                        imguric = dowloaduri.toString()
                        picksongfromstorage()
                    }}}}



    }


    private fun uploadsongtostorage() {
        val firebaseuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val rootref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val userid: String = firebaseuser?.uid!!
        var likes=0
      /*  with(templist[position]) {
            postId = id
        }*/

        var currtime=System.currentTimeMillis()
        var storageref: StorageReference? =
            uri.lastPathSegment?.let { FirebaseStorage.getInstance().getReference().child(it) }
        if (storageref != null) {
            storageref.putFile(uri).addOnSuccessListener {
                var uriTask:Task<Uri> = it.storage.downloadUrl
                uriTask.addOnCompleteListener {
                    if(it.isSuccessful) {
                        var dowloaduri: Uri = it.result
                       songurlc = dowloaduri.toString()
                      /*  var id: String? = "",
                        var songname: String? = "",
                        var duration: Long= 0,
                        var album: String? = "",
                        var path: String? = "",
                        val artist:String? = "",
                        var songurl: String="",
                        var imageurl: String? = ""*/

                        val music= Song(songName,songurlc ,maxid,imguric)

                        val songobj: Song = music
                        var ref11: DatabaseReference =FirebaseDatabase.getInstance().getReference("Songs")
                        ref11.addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                               if(snapshot.exists()){
                                   maxid=(snapshot.childrenCount.toInt())

                               }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                       /* if (ref11 != null) {*/

                            ref11.child((maxid).toString()).setValue(songobj).addOnCompleteListener {
                                if(it.isSuccessful()){
                                    Toast.makeText(this@upload_activity,"Song Uploaded",Toast.LENGTH_SHORT).show()


                                }
                            }.addOnFailureListener {
                                Toast.makeText(this@upload_activity,it.message.toString(),Toast.LENGTH_SHORT).show()
                            }



                        Toast.makeText(this,"song uploaded",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }











    }




    var checkPermission :Boolean=false
    val permissions=Manifest.permission.READ_EXTERNAL_STORAGE



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivityMusciViewBinding.inflate(layoutInflater)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toogle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navbar.setNavigationItemSelectedListener {

            when (it.itemId)
            {
                R.id.settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                R.id.feedback -> Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show()
                R.id.about -> Toast.makeText(this, "about", Toast.LENGTH_SHORT).show()

                R.id.exit -> {
                    Toast.makeText(this, "log out successfully", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent (this, loginActivity::class.java)
                    startActivity(intent)
                    musicActivity.musicServices!!.mediaPlayer!!.reset()
                }
                R.id.upload->{
                    Toast.makeText(this, "Local Music", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this,  frontpage::class.java)
                    startActivity(intent)


                }


            }
            true

        }

        /*val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Song>().setQuery(ref, Song::class.java).build()
        uploadAdapter=PostAdapter(recyclerViewOptions)
        binding.recyclerviewid.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewid.adapter=uploadAdapter*/


        var slider:ImageSlider=binding.imageSlider
        var imgaes:ArrayList<SlideModel> = ArrayList()
        FirebaseDatabase.getInstance().getReference().child("Songs").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userSnapshot in snapshot.children){
                    imgaes.add(SlideModel(userSnapshot.child("imguri").getValue().toString(),userSnapshot.child("songname").getValue().toString(),ScaleTypes.FIT))
                }
                slider.setImageList(imgaes,ScaleTypes.FIT)
                slider.setItemClickListener(object :ItemClickListener{
                    override fun onItemSelected(position: Int) {

                        val intent= Intent(this@upload_activity, musicActivityforupload::class.java)
                        intent.putExtra("index",position)
                        intent.putExtra("class","uploadAdapter")
                        ContextCompat.startActivity(this@upload_activity,intent,null)
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        callrecycleview()


    }



    private fun callrecycleview() {
       /* val musicupload_list=ArrayList<String>()
        frontpage.search =false
        frontpage.MusicListActivity =getsong()*/

       binding.recyclerviewid.layoutManager = LinearLayoutManager(this)
        MusicListActivity= arrayListOf<Song>()



        getuserdata()




    }

    private fun getuserdata() {
        dbref=FirebaseDatabase.getInstance().getReference("Songs")
        dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                MusicListActivity.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        userSnapshot.getValue(Song::class.java)
                            ?.let { MusicListActivity.add(it) }

                    }
                }

                binding.recyclerviewid.adapter= uploadAdapter(this@upload_activity,
                    MusicListActivity)
                /*postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("songname", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Song>().setQuery(query, Song::class.java).build()

        uploadAdapter = PostAdapter(recyclerViewOptions, this)


        binding.recyclerviewid.layoutManager = LinearLayoutManager(this)*/

            }

              override fun onCancelled(error: DatabaseError) {
                  TODO("Not yet implemented")
              }

          })



    }

    private fun     getImage()
    {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.upload_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
    override fun   onOptionsItemSelected(item: MenuItem):Boolean {

        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.itemId ==R.id.nav_upload) {
            Toast.makeText(this,"upload",Toast.LENGTH_SHORT).show()
            if(validatePermission())
            {
           pickimgaefromstorage()

            }

        }
        return super.onOptionsItemSelected(item);
    }


    private fun validatePermission(): Boolean {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object :PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                checkPermission=true
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
              checkPermission=false
            }

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                token: PermissionToken?
            ) {
                if (token != null) {
                    token.continuePermissionRequest()
                }
            }

        }).check()
        return checkPermission
    }
    private fun picksongfromstorage() {

        uploadIntent.type="audio/*"
        uploadIntent.action=Intent.ACTION_GET_CONTENT


      resultcontract.launch("audio/mpeg",)




    }

    private fun pickimgaefromstorage() {

        resultcontract2.launch("image/*")




    }




}