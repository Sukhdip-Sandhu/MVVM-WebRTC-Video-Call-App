package com.example.webrtc_videocallapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private var incomingCallerUsername = MutableLiveData<String>()
    fun getIncomingCallerUsername(): LiveData<String> {
        return incomingCallerUsername
    }

    private var javascriptFunctionString = MutableLiveData<String>()
    fun runJavascriptFunction(): LiveData<String> {
        return javascriptFunctionString
    }

    private var toggleAudio = MutableLiveData<Boolean>()
    fun getToggleAudio(): LiveData<Boolean> {
        return toggleAudio
    }

    private var toggleVideo = MutableLiveData<Boolean>()
    fun getToggleVideo(): LiveData<Boolean> {
        return toggleVideo
    }

    init {
        toggleAudio.value = true
        toggleVideo.value = true
    }


    fun setFirebaseValueEventListener() {
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

        firebaseRef.child(myUsername).child("userCalling").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (myUsername != snapshot.value) {
                    incomingCallerUsername.value = snapshot.value.toString()
                }
            }
        })

    }

    fun setMyUsername(username: String) {
        myUsername = username
    }

    fun initPeerClient(username: String) {
        resetUserCalling(username)
        javascriptFunctionString.value = "javascript:init(\"${username}\")"
    }

    fun startCall(usernameToCall: String) {
        javascriptFunctionString.value = "javascript:startCall(\"${usernameToCall}\")"
    }

    fun hangUpCall() {
        javascriptFunctionString.value = "javascript:hangUpCall()"
    }

    fun makeOutgoingCall(usernameToCall: String) {
        firebaseRef.child(usernameToCall).child("userCalling").setValue(myUsername)
    }

    private fun resetUserCalling(username: String) {
        firebaseRef.child(username).child("userCalling").setValue(username)
    }

    fun toggleAudioButton() {
        toggleAudio.value = !toggleAudio.value!!
        javascriptFunctionString.value = "javascript:toggleAudio(\"${toggleAudio.value}\")"
    }

    fun toggleVideoButton() {
        toggleVideo.value = !toggleVideo.value!!
        javascriptFunctionString.value = "javascript:toggleVideo(\"${toggleVideo.value}\")"

    }

}