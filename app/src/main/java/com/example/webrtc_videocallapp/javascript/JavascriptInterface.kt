package com.example.webrtc_videocallapp.javascript

import android.webkit.JavascriptInterface
import com.example.webrtc_videocallapp.CallActivity
import com.example.webrtc_videocallapp.HomeActivity

class JavascriptInterface(val homeActivity: HomeActivity) {

    @JavascriptInterface
    public fun onPeerConnected() {
        homeActivity.onPeerConnected()
    }

}