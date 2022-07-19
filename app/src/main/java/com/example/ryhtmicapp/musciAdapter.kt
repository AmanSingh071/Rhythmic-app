package com.example.ryhtmicapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.ryhtmicapp.databinding.ActivityMusciViewBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class musicAdapter (private val context: Context, private var musicList:ArrayList<storemusicclass>) : RecyclerView.Adapter<musicAdapter.MyHolder>() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var likes: DatabaseReference
    private lateinit var musicList2:ArrayList<storemusicclass>
    class MyHolder(binding: ActivityMusciViewBinding) : RecyclerView.ViewHolder(binding.root) {


        val title = binding.songname
        val album=binding.createdAt
        val image=binding.songimage
        val duration=binding.songduration
        val fav:ImageView=binding.fav

        val favtxt:TextView=binding.favtxt
        val s: TextView =favtxt
        val rootref:DatabaseReference= FirebaseDatabase.getInstance().getReference(adapterPosition.toString())

        lateinit var likeref:DatabaseReference

        var testclick:Boolean=false

        val root=binding.root
        lateinit var ref1: FirebaseDatabase
    /*    fun getlikebuttonstatus(audioid: String?, userid: String) {
             likeref=FirebaseDatabase.getInstance().getReference("likes")

            likeref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (audioid?.let { snapshot.child(it).hasChild(userid) } == true)
                    {
                        val likecount :Int=snapshot.child(audioid).childrenCount.toInt()
                        favtxt.text= "$likecount likes"
                        fav.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                    else{
                        val likecount : Int? = audioid?.let { snapshot.child(it).childrenCount.toInt() }
                        favtxt.text= "$likecount likes"
                        fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        }*/
 public class ViewHolder(itemView: View){


 }



    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): musicAdapter.MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        firestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        return MyHolder(ActivityMusciViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: musicAdapter.MyHolder, position: Int) {

       holder.title.text=musicList[position].title
        holder.album.text=musicList[position].album
        holder.duration.text= formatduration(musicList[position].duration)
        Glide.with(context).load(musicList[position].arturi).apply { placeholder(R.drawable.ic_baseline_music_note_24) }.into(holder.image)

        holder.root.setOnClickListener {
            when {
                //if search true
                frontpage.search -> setintetnonclickitem(ref="musicAdapterSearch", pos = position)
                else-> setintetnonclickitem(ref="musicAdapter", pos = position)
            }
        }

        val userid = mAuth.currentUser?.uid


        var post=musicList[position]
      /*  holder.fav.setOnClickListener {

            if (holder.testclick) {
                holder.testclick=false
                holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                holder.favtxt.text =
                    (holder.favtxt.text.toString().toInt() + 1).toString()

                post.id?.let { it1 ->
                    firestore.collection("Songs").document(it1).update(
                        "votesCount", FieldValue.increment(1),
                        "votersList", FieldValue.arrayUnion(userid))

                }
            } else {holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24)
                holder.testclick=true
                holder.favtxt.text =
                    (holder.favtxt.text.toString().toInt() - 1).toString()

                post.id?.let { it1 ->
                    firestore.collection("Songs").document(it1).update(
                        "votesCount", FieldValue.increment(-1),
                        "votersList", FieldValue.arrayRemove(userid)
                    )
                }

            }
        }*/

        val audioid: String? =  post.id



        /* holder.getlikebuttonstatus(audioid,userid!!)

        holder.fav.setOnClickListener {
 likes.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                 if (holder.testclick)

                     if(audioid?.let { it1 -> snapshot.child(it1).hasChild(userid) } == true)
                     {
                         likes.child(audioid).removeValue()
                         holder.testclick=false





                 }
                    else
                     {
                         holder.testclick=true
                         if (audioid != null) {
                             likes.child(audioid).child(userid).setValue(true)
                         }
                     }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }
*/
        val user=FirebaseDatabase.getInstance().getReference("Song").child("userId")
        user.child(post.id!!).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val model=snapshot.getValue(storemusicclass::class.java)
                    val updateData:MutableMap<String,Any> = HashMap()
                    model!!.votesCount=model!!.votesCount+1
                    updateData["votesCount"]=model!!.votesCount+1
                    model!!.id?.let { user.child(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        // sets the image to the imageview from our itemHolder class


    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return musicList.size
    }

    fun updatemusiclistonsearch(searchlist:ArrayList<storemusicclass>)
    {
        musicList= ArrayList() //initialse it again
        musicList.addAll(searchlist)
        notifyDataSetChanged()// after changing layout should also change

    }
    private fun setintetnonclickitem(ref:String,pos:Int)
    {
        val intent= Intent(context, musicActivity::class.java)
        intent.putExtra("index",pos)
        intent.putExtra("class",ref)
        ContextCompat.startActivity(context,intent,null)
    }


    // Holds the views for adding it to image and text

}
