package com.example.ryhtmicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ryhtmicapp.databinding.ActivityMusicBinding
import com.example.ryhtmicapp.databinding.ActivityPlaylistBinding

class playlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backarrow.setOnClickListener{
            val intent= Intent(this, frontpage::class.java)
            startActivity(intent)

        }
    }
}