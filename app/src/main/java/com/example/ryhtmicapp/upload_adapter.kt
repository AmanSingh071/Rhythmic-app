package com.example.socialapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ryhtmicapp.*
import com.example.ryhtmicapp.R
import com.example.ryhtmicapp.databinding.ActivityMusciViewBinding

import com.google.firebase.database.DatabaseReference



import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList


class uploadAdapter (private val context: Context, private var musicList:ArrayList<Song>) : RecyclerView.Adapter<uploadAdapter.MyHolder>() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var likeref: DatabaseReference
    private lateinit var musicList2:ArrayList<Song>

    class MyHolder(binding: ActivityMusciViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun getlikebuttonstatus(audioid: Int, userid: String) {


        }

        fun fun2(key: Int) {
            var user: FirebaseUser? =FirebaseAuth.getInstance().currentUser
            var userid:String=user!!.uid
            ref1=FirebaseDatabase.getInstance().getReference("likes")
            var Songs:String="Songs"
            ref1.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(key!!.toString()).hasChild(userid)){
                        favtxt.text = snapshot.child(key.toString()).childrenCount.toString()
                        fav.setImageResource(R.drawable.ic_baseline_favorite_24)




                    }
                    else
                    {
                        fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                        favtxt.text = snapshot.child(key.toString()).childrenCount.toString()


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


        val title = binding.songname
        val album=binding.createdAt
        val image=binding.songimage
        val duration=binding.songduration
        val fav:ImageView=binding.fav



        val favtxt: TextView = itemView.findViewById(R.id.favtxt)
        var favtxtcount:Int=0

        val rootref:DatabaseReference= FirebaseDatabase.getInstance().getReference(adapterPosition.toString())
        private lateinit var musicList2:ArrayList<Song>
        lateinit var likeref:DatabaseReference

        var testclick:Boolean=false

        val root=binding.root
        lateinit var ref1: DatabaseReference





    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        firestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        return MyHolder(ActivityMusciViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.title.text=musicList[position].songname
        holder.album.text= musicList[position].songname.toString()





        Glide.with(context).load(musicList[position].imguri).apply { placeholder(R.drawable.ic_baseline_music_note_24) }.into(holder.image)

        holder.root.setOnClickListener {
            setintetnonclickitem(ref = "uploadAdapter", pos = position)
        }

        val userid = mAuth.currentUser?.uid


        var post=musicList[position]



      /*  holder.fav.setOnClickListener {

            if (  holder.testclick) {

                holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)


                holder.votecount.text =
                    (holder.votecount.text.toString().toInt() + 1).toString()


          *//* var ref1: DatabaseReference =FirebaseDatabase.getInstance().getReference("Songs")
                var ref2: DatabaseReference= ref1.child(musicList[position].id.toString())
                ref2.child("votesCount").setValue(0)*//*
                firestore.collection("Songs").document(post.id.toString()).update(
                    "votesCount", FieldValue.increment(1))






                holder.testclick=false




            } else { holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24)


                holder.votecount.text =
                    (holder.votecount.text.toString().toInt() - 1).toString()
             *//*   var ref1: DatabaseReference =FirebaseDatabase.getInstance().getReference("Songs")
                var ref2: DatabaseReference= ref1.child(musicList[position].id.toString())
                ref2.child("votesCount").setValue(1)*//*
                firestore.collection("Songs").document(post.id.toString()).update(
                    "votesCount", FieldValue.increment(-1))

                Toast.makeText(context, "Voted Post", Toast.LENGTH_SHORT).show()


                holder.testclick=true










            }
            holder.votecount.text = post.votesCount.toString()
        }*/
var audioid=musicList[position].id


       /* var key: String? =FirebaseDatabase.getInstance().getReference(musicList[position].toString()).key*/
       holder.fun2(audioid)

    /*   likeref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(userid?.let { snapshot.child(audioid.toString()).hasChild(it) } == true){
                    val likecount: Int =snapshot.child(audioid.toString()).childrenCount.toInt()
                   holder.votecount.setText(likecount)
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
                else
                {
                    val likecount: Int =snapshot.child(audioid.toString()).childrenCount.toInt()
                  holder.votecount.setText(likecount)
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
*/

        holder.fav.setOnClickListener {
            holder.testclick=true
            holder.ref1.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                 if (holder.testclick.equals(true)) {

                     if (snapshot.child(audioid!!.toString()).hasChild(userid!!) ) {

                         holder.ref1.child(audioid!!.toString()).child(userid).removeValue()
                         holder.testclick = false
                     }
                     else{

                         holder.ref1.child(audioid!!.toString()).child(userid).setValue(true)
                         holder.testclick = false
                     }



                 }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }



        // sets the image to the imageview from our itemHolder class


    }




    // return the number of the items in the list
    override fun getItemCount(): Int {
        return musicList.size
    }


    private fun setintetnonclickitem(ref:String,pos:Int)
    {
        val intent= Intent(context, musicActivityforupload::class.java)
        intent.putExtra("index",pos)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context,intent,null)
    }


    // Holds the views for adding it to image and text

}

/*
class PostAdapter(options: FirestoreRecyclerOptions<Song>) : FirestoreRecyclerAdapter<Song, PostAdapter.MyHolder>(
    options
) {


    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.songimage)
        val image: ImageView = itemView.findViewById(R.id.songimage)
        val root: TextView = itemView.findViewById(com.firebase.ui.auth.R.id.root)
        val time:TextView=itemView.findViewById((R.id.createdAt))

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return  MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_musci_view, parent, false))



    }

    override fun onBindViewHolder(holder: MyHolder, position: Int, model: Song) {
        holder.title.text =model.songname

        */
/*    holder.album.text=musicuploadList[position].album
            holder.duration.text= musicuploadList[position].duration?.let { formatduration(it) }*//*


        Glide.with(holder.image.context).load( model.imguri).apply { placeholder(R.drawable.ic_baseline_music_note_24) }.into(holder.image)
        holder.time.text= model.time.toString()


           */
/* setintetnonclickitem(ref = "uploadAdapter", pos = position)*//*

        }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

      */
/*  val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_liked))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unliked))
        }*//*


    }




   */
/* private fun setintetnonclickitem(ref:String,pos:Int)
    {
        val intent= Intent(context, musicActivityforupload::class.java)
        intent.putExtra("index",pos)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context,intent,null)
    }*//*



interface IPostAdapter {
    fun onLikeClicked(postId: String)
}*/
