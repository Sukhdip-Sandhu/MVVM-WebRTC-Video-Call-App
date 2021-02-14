package com.example.webrtc_videocallapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var myUsername: String

private var firebaseRef = Firebase.database.getReference("users")

private val listOfUsers = ArrayList<String>()

class HomeViewModel : ViewModel() {


    private var users = MutableLiveData<ArrayList<String>>()
    fun getUsers(): LiveData<ArrayList<String>> {
        return users
    }

    fun setEventListeners() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfUsers.clear()
                snapshot.children.forEach { child ->
                    if (!child.key.equals(myUsername)) {
                        listOfUsers.add(child.key!!)
                    }
                }
                users.value = listOfUsers
            }

        })

    }

    fun setMyUsername(username: String) {
        myUsername = username
    }

}