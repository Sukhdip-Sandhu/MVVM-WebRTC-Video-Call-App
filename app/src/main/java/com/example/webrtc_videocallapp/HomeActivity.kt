package com.example.webrtc_videocallapp

import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.webrtc_videocallapp.databinding.ActivityHomeBinding
import com.example.webrtc_videocallapp.javascript.JavascriptInterface
import com.example.webrtc_videocallapp.ui.adapters.UsersAdapter
import com.example.webrtc_videocallapp.viewmodels.HomeViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivityHomeBinding
private lateinit var usersAdapter: UsersAdapter
private lateinit var username: String
private lateinit var homeViewModel: HomeViewModel

private var firebaseRef = Firebase.database.getReference("users")

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()

        username = intent.getStringExtra("username")!!

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.setMyUsername(username)
        homeViewModel.setFirebaseValueEventListener()

        homeViewModel.getUsers().observe(this, Observer { listOfUsers ->
            usersAdapter.setUsersList(listOfUsers)
            usersAdapter.notifyDataSetChanged()
        })

        homeViewModel.getIncomingCallerUsername().observe(this, Observer { incomingCallerUsername ->
            binding.incomingCallLayout.visibility = View.VISIBLE
            binding.incomingCallerName.text = "$incomingCallerUsername is calling"

            binding.answerCallButton.setOnClickListener {
                homeViewModel.startCall(incomingCallerUsername)
                binding.callLayout.visibility = View.VISIBLE
                binding.incomingCallLayout.visibility = View.INVISIBLE
            }

            binding.declineCallButton.setOnClickListener {
                binding.incomingCallLayout.visibility = View.INVISIBLE
            }
        })

        homeViewModel.getToggleAudio().observe(this, Observer { isAudio ->
            binding.toggleAudioButton.setImageResource(if (isAudio) R.drawable.ic_baseline_mic_24 else R.drawable.ic_baseline_mic_off_24)
        })

        homeViewModel.getToggleVideo().observe(this, Observer { isVideo ->
            binding.toggleVideoButton.setImageResource(if (isVideo) R.drawable.ic_baseline_videocam_24 else R.drawable.ic_baseline_videocam_off_24)
        })

        homeViewModel.runJavascriptFunction().observe(this, Observer { functionString ->
            callJavascriptFunction(functionString)
        })

        usersAdapter.setOnItemClickListener { usernameToCall ->
            Toast.makeText(this, "Calling $usernameToCall", Toast.LENGTH_SHORT).show()
            homeViewModel.makeOutgoingCall(usernameToCall)
        }

        binding.hangUpButton.setOnClickListener {
            binding.callLayout.visibility = View.INVISIBLE
            homeViewModel.hangUpCall()
            homeViewModel.initPeerClient(username)
        }

        binding.toggleAudioButton.setOnClickListener {
            homeViewModel.toggleAudioButton()
        }

        binding.toggleVideoButton.setOnClickListener {
            homeViewModel.toggleVideoButton()
        }

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

        val filePath = "file:android_asset/call.html"
        binding.webView.loadUrl(filePath)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                homeViewModel.initPeerClient(username)
            }
        }
    }

    private fun callJavascriptFunction(functionString: String) {
        binding.webView.post {
            binding.webView.evaluateJavascript(functionString, null)
        }
    }

    fun onPeerConnected() {
        Toast.makeText(this, "Connected to server", Toast.LENGTH_SHORT).show()
    }

    fun onAnswerCall() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.callLayout.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter()
        binding.usersRecyclerView.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    override fun onDestroy() {
        firebaseRef.child(username).setValue(null)
        super.onDestroy()
    }


}