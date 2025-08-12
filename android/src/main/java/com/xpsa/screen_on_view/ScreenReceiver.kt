package com.xpsa.screen_on_view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission

class ScreenReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            ScreenOn(context)
        }
    }

    fun ScreenOn(context: Context){
        try {
            val i = LockScreenActivity.withCachedEngine("LockScreenEngine_id").build(context).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        } catch (e: Exception) {
            Log.e("Screen_On_View Error", e.toString())
        }
    }
}