package com.csgm.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

private const val CHANNEL_ID="ForegroundService"

class ForegroundService: Service () {
    private val mHandler = Handler()
    private lateinit var mRunnable: Runnable
    private var count = 0

    companion object {
        var running = false
        private lateinit var handlerCallback: Handler
        fun startService(
            context: Context, message: String, handler:
            Handler
        ) {
            val startIntent = Intent(
                context, ForegroundService::class.java
            )
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
            running = true
            handlerCallback = handler
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(
                context, ForegroundService::class.java
            )
            context.stopService(stopIntent)
            running = false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int):
            Int {
        Log.d( "ForegroundService" , "on Start Command" )
        val input = intent?.getStringExtra( "inputExtra" ) ?: ""
        createNotificationChannel(input)
        runTask()
        return Service.START_NOT_STICKY
    }

    private fun createNotificationChannel(input: String) {
        Log.d( "ForegroundService" , "create notif channel" )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager:: class . java
            )
            manager!!.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent( this , MainActivity:: class . java )
        val pendingIntent = PendingIntent.getActivity(
            this , // Context
            0 , // request code
            notificationIntent, // Intent con la notificación
            0
        )
        val notification = NotificationCompat.Builder( this , CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground( 1 , notification)
    }
    private fun runTask() {
        Log.d( "ForegroundService" , "runtask" )
        val delayTime=1000*7L
        mRunnable = Runnable {
            count += 1
            notifyNextEvent()
            mHandler.postDelayed(mRunnable, delayTime)
        }
        mHandler.postDelayed(mRunnable, delayTime)
    }

    private fun notifyNextEvent() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
        Log.d("ForegroundService", "onDestroy: Ejecutando OnDestroy del proceso en primer plano")
    }
}