package com.example.webrtc_videocallapp.javascript

import android.webkit.JavascriptInterface
import com.example.webrtc_videocallapp.CallActivity
import com.example.webrtc_videocallapp.HomeActivity

class JavascriptInterface2(val callActivity: CallActivity) {

    @JavascriptInterface
    public fun onPeerConnected() {
        callActivity.onPeerConnected()
    }

}