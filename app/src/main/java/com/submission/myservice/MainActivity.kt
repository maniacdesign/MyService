package com.submission.myservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.submission.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService

    // sebuah listener untuk menerima callback dari ServiceConnection
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService = myBinder.getService
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }
    }
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

        /*
        Menjalankan JobIntentService dengan membawa data melalui main activity, dengan menentukan berapa lama service berjalan
         */
        binding.btnStartJobIntentService.setOnClickListener {
            val mStartJobIntentService = Intent(this, MyJobIntentService::class.java)
            mStartJobIntentService.putExtra(MyJobIntentService.EXTRA_DURATION, 5000L)
            MyJobIntentService.enqueueWork(this, mStartJobIntentService)
        }

        binding.btnStartBoundService.setOnClickListener {
            /*
            - mBoundServiceIntent adalah sebuah intent eksplisit untuk menjalankan komponen dari dalam sebuah aplikasi
            - Penggunaan bindService sebagai pengikat class MyBoundService ke class MainActivity
            - mServiceConnection adalah ServiceConnection yg berfungsi sebagai callback dari class MyBoundService
            - BIND_AUTO_CREATE, fungsinya adalah mengaktifkan service jika belum aktif, fungsi-fungsi lain yang memungkinkan adalah:
                1. BIND_ABOVE_CLIENT : yang digunakan ketika sebuah service lebih penting daripada aplikasi itu sendiri.
                2. BIND_ADJUST_WITH_ACTIVITY : saat mengikat sebuah service dari activity,
                    maka ia akan mengizinkan untuk menargetkan service mana yang lebih penting berdasarkan activity yang terlihat oleh pengguna.
                3. BIND_ALLOW_OOM_MANAGEMENT : memungkinkan untuk mengikat service hosting untuk mengatur memori secara normal.
                4. BIND_AUTO_CREATE : secara otomatis membuat service selama binding-nya aktif.
                5. BIND_DEBUG_UNBIND : berfungsi sebagai bantuan ketika debug mengalami masalah pada pemanggilan unBind.
                6. BIND_EXTERNAL_SERVICE : merupakan service yang terikat dengan service eksternal yang terisolasi.
                7. BIND_IMPORTANT : service ini sangat penting bagi klien, jadi harus dibawa ke tingkat proses foreground.
                8. BIND_NOT_FOREGROUND : pada service ini tak disarankan untuk mengubah ke tingkat proses foreground.
                9. BIND_WAIVE_PRIORITY : service ini tidak akan mempengaruhi penjadwalan atau prioritas manajemen memori dari target proses layanan hosting.
             */
            val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
            bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
        }

        binding.btnStopBoundService.setOnClickListener {
            unbindService(mServiceConnection)
        }
    }

    /*
    Akan memanggil unbindService atau melakukan pelepasan service dari Activity dimana secara tidak langsung memanggi metode onUnbind di class MyBoundService
    ,tujuannya untuk mencegah memory leaks dari bound servis

     */
    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBound) {
            unbindService(mServiceConnection)
        }
    }
}