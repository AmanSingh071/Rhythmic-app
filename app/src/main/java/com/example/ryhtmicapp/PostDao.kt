package com.example.ryhtmicapp

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("Songs")
    val auth = Firebase.auth




}