package com.example.lab4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MediaReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("!!!", "RECEIVED URL")
        intent?.extras?.keySet()?.forEach {
            Log.d("!!!", it)
        }
        Log.d("!!!", intent?.extras?.getString("CHOSEN_COMPONENT").toString())
    }
}