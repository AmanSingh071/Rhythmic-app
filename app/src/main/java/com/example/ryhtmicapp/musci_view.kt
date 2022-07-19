package com.example.ryhtmicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.ryhtmicapp.databinding.ActivityMusciViewBinding

class musci_view : AppCompatActivity() {
    lateinit var binding: ActivityMusciViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMusciViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}