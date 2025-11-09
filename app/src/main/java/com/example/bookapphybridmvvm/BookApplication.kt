package com.example.bookapphybridmvvm

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlin.system.exitProcess

@HiltAndroidApp
class BookApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Log the crash
            Log.e("GlobalCrashHandler", "Uncaught exception in thread ${thread.name}", throwable)
            Log.e("GlobalCrashHandler", "App will now exit - " + throwable.message)

            // Optional: send it to crash reporting tools like Firebase Crashlytics
            // FirebaseCrashlytics.getInstance().recordException(throwable)

            // Optionally kill app gracefully
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(2)
        }
    }
}