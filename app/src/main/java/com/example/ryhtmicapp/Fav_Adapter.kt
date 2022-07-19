package com.example.ryhtmicapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ryhtmicapp.databinding.ActivityFavViewBinding




 class fav_Adapter (private val context: Context, private var musicList:ArrayList<storemusicclass>) : RecyclerView.Adapter<fav_Adapter.MyHolder>() {

    class MyHolder(binding:  ActivityFavViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var image=binding.favImage
        var name=binding.favSongName
        val root=binding.root



    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fav_Adapter.MyHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        return MyHolder(ActivityFavViewBinding.inflate(LayoutInflater.from(context),parent,false))



    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: fav_Adapter.MyHolder, position: Int) {
        holder.name.text=musicList[position].title
        Glide.with(context).load(musicList[position].arturi).apply { RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24) }.into(
            holder.image)
        holder.root.setOnClickListener {
                val intent= Intent(context, musicActivity::class.java)
                intent.putExtra("index",position)
                intent.putExtra("class","FavouriteAdapter")
                ContextCompat.startActivity(context,intent,null)
            }




    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return musicList.size
    }




    // Holds the views for adding it to image and text

}