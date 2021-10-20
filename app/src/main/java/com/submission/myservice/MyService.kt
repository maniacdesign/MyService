package com.submission.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    companion object {
        internal val TAG = MyService::class.java.simpleName
    }
    
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    /*
    - simulasi menjalankan sebuah background proses yang sulit secara asynchronus, kekurangan servis ini tidak menyediakan
        thread diluar ui thread, sehingga kita harus membuat thread sendiri
    - START_STICKY menandakan bahwa servis tersebut akan dimatikan oleh sistem jika kekurangan memori dan akan dibuat kembali
        ketika sudah tersedia memori yang cukup
    - stopSelf() berfungsi mematikan servis dari sistem android
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service dijalankan....")
        serviceScope.launch { 
            delay(3000)
            stopSelf()
            Log.d(TAG, "Service dihentikan....")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: ")
    }
}