package com.submission.myservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.submission.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        service (MyService) jenis ini berjalan pada main thread, dijalankan dengan perintah startService() dengan sebelumnya melakukan inisiasi
        val mStartService = Intent(this, MyService::class.java), setelah perintah startService() dijalankan maka metode onStartCommand()
        pada MyService dijalankan
         */
        binding.btnStartService.setOnClickListener {
            val mStartService = Intent(this, MyService::class.java)
            startService(mStartService)
        }

        binding.btnStartJobIntentService.setOnClickListener {
            val mStartJobIntentService = Intent(this, MyJobIntentService::class.java)
            mStartJobIntentService.putExtra(MyJobIntentService.EXTRA_DURATION, 5000L)
            MyJobIntentService.enqueWork(this, mStartJobIntentService)
        }

        binding.btnStartBoundService.setOnClickListener {

        }

        binding.btnStopBoundService.setOnClickListener {

        }
    }
}