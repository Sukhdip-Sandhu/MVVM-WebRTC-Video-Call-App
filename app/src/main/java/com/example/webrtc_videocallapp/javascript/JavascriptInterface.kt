package com.example.webrtc_videocallapp.javascript

import android.webkit.JavascriptInterface
import com.example.webrtc_videocallapp.HomeActivity

class JavascriptInterface(private val homeActivity: HomeActivity) {

    @JavascriptInterface
    public fun onPeerConnected() {
        homeActivity.onPeerConnected()
    }

    @JavascriptInterface
    public fun onAnswerCall() {
        homeActivity.onAnswerCall();
    }

}