package com.xpsa.screen_on_view


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import io.flutter.FlutterInjector

class ScreenService : Service() {
    var receiver: ScreenReceiver? = null;
    private val ANDROID_CHANNEL_ID = "screen_on_view"
    private val NOTIFICATION_ID = 9999
    fun LockScreen_registerReciver(name:String?){
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        receiver = ScreenReceiver()
        registerReceiver(receiver, filter)
        val intent = Intent(this,ScreenReceiver::class.java)
        this.sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmModel =intent?.getParcelableExtra<AlarmModel>("alarmModel")
        if (receiver == null) {
            LockScreen_registerReciver(intent?.getStringExtra("entryPointName"))
        }

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ANDROID_CHANNEL_ID,
                "screen_on_view",
                NotificationManager.IMPORTANCE_MIN
            )
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
            .setContentTitle(alarmModel?.title?:"screen_on_view")
            .setContentText(alarmModel?.content?:"background running")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setOngoing(true)


        startForeground(NOTIFICATION_ID, notification.build())


        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}