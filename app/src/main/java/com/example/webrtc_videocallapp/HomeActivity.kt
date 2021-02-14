package com.example.webrtc_videocallapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webrtc_videocallapp.databinding.ActivityHomeBinding
import com.example.webrtc_videocallapp.ui.adapters.UsersAdapter
import com.example.webrtc_videocallapp.viewmodels.HomeViewModel

private lateinit var binding: ActivityHomeBinding

private lateinit var usersAdapter: UsersAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()

        usersAdapter.setOnItemClickListener { usernameToCall ->
            Log.d("LOGCAT", "onCreate: ")
        }

        val username = intent.getStringExtra("username")!!

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.setMyUsername(username)

        homeViewModel.setEventListeners()

        homeViewModel.getUsers().observe(this, Observer { listOfUsers ->
            Log.d("WEBRTCAPP", "onCreate: ")
            usersAdapter.setUsersList(listOfUsers)
            usersAdapter.notifyDataSetChanged()
        })

    }

    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter()
        binding.usersRecyclerView.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

}