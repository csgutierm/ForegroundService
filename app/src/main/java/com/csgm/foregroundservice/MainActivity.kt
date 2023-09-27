package com.csgm.foregroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.csgm.foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Handler.Callback{
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        binding.btStartStop.setOnClickListener {
//Aquí se implementan los métodos para iniciar y detener el servicio.
//Cada 7 segundos se incrementa el contador y se actualiza la vista
            if (ForegroundService.running){
                ForegroundService.stopService(this)
                binding.btStartStop.text=getString(R.string.messsage_start)
            }
            else {
                ForegroundService.startService(this,getString(R.string.message_2_user),
                    Handler(this))
                binding.btStartStop.text=getString(R.string.messsage_stop)
            }

        }
        setContentView(binding.root)
    }
    override fun handleMessage(msg: Message): Boolean {
        Log.d("MainActivity", "handleMessage: ${msg.data}")
        val count=msg.data.get("Contador")
        binding.tvCounter.text=count.toString()
        return true
    }
}