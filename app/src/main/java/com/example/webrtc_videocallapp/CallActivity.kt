package com.example.webrtc_videocallapp

import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.webrtc_videocallapp.databinding.ActivityCallBinding
import com.example.webrtc_videocallapp.javascript.JavascriptInterface
import com.example.webrtc_videocallapp.javascript.JavascriptInterface2
import com.example.webrtc_videocallapp.viewmodels.CallViewModel

private lateinit var binding: ActivityCallBinding

private lateinit var usernameToCall: String


class CallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        usernameToCall = intent.getStringExtra("usernameToCall")!!

        val callViewModel = ViewModelProvider(this).get(CallViewModel::class.java)

        setupWebView()

    }

    private fun setupWebView() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                super.onPermissionRequest(request)
                request?.grant(request.resources)
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.mediaPlaybackRequiresUserGesture = false
        binding.webView.addJavascriptInterface(JavascriptInterface2(this), "Android")

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

    private fun initializePeer() {
        callJavascriptFunction("javascript:startCall(\"${usernameToCall}\")")
    }

    private fun callJavascriptFunction(functionString: String) {
        binding.webView.post {
            binding.webView.evaluateJavascript(functionString, null)
        }
    }

    fun onPeerConnected() {
        //
    }
}