package com.submission.myservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MyBoundService : Service() {

    companion object {
        private val TAG = MyBoundService::class.java.simpleName
    }

    private val mBinder = MyBinder()
    private val startTime = System.currentTimeMillis()

    /*
    kelas ini dipanggil oleh metode onServiceConnected di class MainActivity untuk memanggil kelas servis,
    fungsinya untuk mengikat kelas servis. Kelas MyBinder yang diberi turunan kelas binder, mempunyai fungsi untuk melakukan mekanisme
    pemanggilan prosedur jarak jauh
     */
    internal inner class MyBinder : Binder() {
        val getService: MyBoundService = this@MyBoundService
    }

    /*
    Ketika CountDownTimer dijalankan akan berjalan sampai 100.000/100detik dengan setiap intervalnya adalah 1000/1 detik menampilkan log
    hitungan ini berfungsi untuk melihat proses terikatnya MyBoundService ke MainActivity
     */
    private val mTimer: CountDownTimer = object : CountDownTimer(100000, 1000) {
        override fun onTick(p0: Long) {
            val elapsedTime = System.currentTimeMillis() - startTime
            Log.d(TAG, "onTick: $elapsedTime")
        }

        override fun onFinish() {
            TODO("Not yet implemented")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        mTimer.start()
        return mBinder
    }

    /*
    Metode ini berfungsi untuk melakukan penghapusan kelas MyBoundService dari memori, setelah service terlepas dari
    MainActivity ia juga akan terlepas dari memori perangkat
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    /*
    Dipanggil oleh metode onDestroy di class MainActivity
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        mTimer.cancel()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
    }
}