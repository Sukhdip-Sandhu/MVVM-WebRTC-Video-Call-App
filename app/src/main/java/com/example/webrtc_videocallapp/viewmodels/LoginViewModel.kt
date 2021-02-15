package com.example.webrtc_videocallapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private var firebaseRef = Firebase.database.getReference("users")

class LoginViewModel : ViewModel() {

    fun isValidUsername(username: String): Boolean {
        return username.isNotEmpty()
    }

    fun formatUsername(username: String): String {
        return username.replace(" ", "")
    }

    fun addUsernameToFirebase(username: String) {
        firebaseRef.child(username).child("userCalling").setValue(username)
    }


}