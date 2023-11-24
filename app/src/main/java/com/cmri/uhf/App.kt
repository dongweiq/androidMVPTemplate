package com.cmri.uhf

import android.app.Application
import android.content.Context
import com.cmri.uhf.utils.AppConfig
import com.cmri.uhf.utils.CrashLogHandler
import com.elvishew.xlog.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.tencent.bugly.crashreport.CrashReport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class App : Application() {
    companion object {
        private lateinit var appContext: Context

        fun getAppContext(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initXlog()
        CrashLogHandler(this)
        CrashReport.initCrashReport(getApplicationContext(), "", BuildConfig.DEBUG);
    }

    private fun initXlog() {
        val formatter = SimpleDateFormat("HH:mm:ss SSS", Locale.CHINA)
        val config = LogConfiguration.Builder().tag("cmriRfid") // 指定 TAG，默认为 "X-LOG"
            .build()
        val androidPrinter: Printer = AndroidPrinter() // 通过 android.util.Log 打印日志的打印器

        val filePrinter: Printer = FilePrinter.Builder("/sdcard/cmriRfid/") // 指定保存日志文件的路径
            .fileNameGenerator(DateFileNameGenerator()) // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
            .backupStrategy(FileSizeBackupStrategy((20 * 1024 * 1024).toLong()))
            .flattener { timeMillis: Long, logLevel: Int, tag: String, message: String ->
                formatter.format(
                    Date(timeMillis)
                ) + "|" + tag + "|" + message
            }.build()
        XLog.init(
            LogLevel.ALL,  // 指定日志级别，低于该级别的日志将不会被打印
            config,  // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
            androidPrinter,  // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter
            filePrinter
        )
    }


}