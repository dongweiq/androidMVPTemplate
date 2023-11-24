package com.cmri.uhf.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CrashLogHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        handleCrashLog(throwable)
        restartApp()
    }

    private fun handleCrashLog(throwable: Throwable) {
        try {
            if (isExternalStorageWritable()) {
                val logDirPath = context.getExternalFilesDir(null)?.absolutePath + "/cmriRfid"
                val logDir = File(logDirPath)
                if (!logDir.exists()) {
                    logDir.mkdirs()
                }

                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val logFileName = "crash_log_$timestamp.txt"

                val logFile = File(logDir, logFileName)
                logFile.createNewFile()

                val writer = PrintWriter(FileWriter(logFile))
                writer.println("Crash Log:")
                throwable.printStackTrace(writer)
                writer.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun restartApp() {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        System.exit(2)
    }

    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}