package com.example.ryhtmicapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ryhtmicapp.databinding.*


class facvoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacvoriteBinding
    private lateinit var binding2: ActivityFavViewBinding
    private lateinit var binding3: ActivityMusicBinding

    private lateinit var adapter: fav_Adapter
    companion object{
        var favouritesong:ArrayList<storemusicclass> = ArrayList()
        var favouriteschange:Boolean=false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacvoriteBinding.inflate(layoutInflater)
        binding2 = ActivityFavViewBinding.inflate(layoutInflater)
        binding3=ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backarrow.setOnClickListener {
            val intent = Intent(this, frontpage::class.java)
            startActivity(intent)

        }

        binding.recyclerviewid2.layoutManager = GridLayoutManager(this, 4)

        adapter = fav_Adapter(this, favouritesong)
        binding.recyclerviewid2.adapter = adapter

        binding.shuffle.setOnClickListener {
            val intent=Intent(this,musicActivity::class.java)
            intent.putExtra("index",0)
            intent.putExtra("class","FavouriteShuffle")
            startActivity(intent)
        }






    }



}