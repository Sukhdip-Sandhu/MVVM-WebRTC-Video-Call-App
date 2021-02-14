package com.example.webrtc_videocallapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webrtc_videocallapp.databinding.ActivityHomeBinding
import com.example.webrtc_videocallapp.javascript.JavascriptInterface
import com.example.webrtc_videocallapp.ui.adapters.UsersAdapter
import com.example.webrtc_videocallapp.viewmodels.HomeViewModel

private lateinit var binding: ActivityHomeBinding

private lateinit var usersAdapter: UsersAdapter

private lateinit var username: String

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()

        usersAdapter.setOnItemClickListener { usernameToCall ->
            Log.d("LOGCAT", "onCreate: ")
            callJavascriptFunction("javascript:startCall(\"${usernameToCall}\")")
//            val intent = Intent(this, CallActivity::class.java)
//            intent.putExtra("usernameToCall", usernameToCall)
//            startActivity(intent)
        }

        username = intent.getStringExtra("username")!!

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.setMyUsername(username)

        homeViewModel.setEventListeners()

        homeViewModel.getUsers().observe(this, Observer { listOfUsers ->
            usersAdapter.setUsersList(listOfUsers)
            usersAdapter.notifyDataSetChanged()
        })

        setupWebView()

    }

    private fun setupWebView() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.mediaPlaybackRequiresUserGesture = false
        binding.webView.addJavascriptInterface(JavascriptInterface(this), "Android")

        loadVideoCall()
    }

    private fun loadVideoCall() {
        val filePath = "file:android_asset/call.html"
        binding.webView.loadUrl(filePath)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }
        }
    }

    fun initializePeer() {
        callJavascriptFunction("javascript:init(\"${username}\")")
    }

    private fun callJavascriptFunction(functionString: String) {
        binding.webView.post {
            binding.webView.evaluateJavascript(functionString, null)
        }
    }

    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter()
        binding.usersRecyclerView.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    fun onPeerConnected() {
        Log.d("LOGCAT", "onPeerConnected: ")
    }

}