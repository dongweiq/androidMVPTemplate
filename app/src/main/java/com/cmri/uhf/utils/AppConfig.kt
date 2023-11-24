package com.cmri.uhf.utils

import android.content.Context
import com.elvishew.xlog.XLog
import org.json.JSONObject
import java.io.File

class AppConfig(private val context: Context) {

    // 默认配置
    var deviceId: String = "unknown"
    var mqttHost: String = ""
    var mode: String = ""
    var httpHost: String = ""
    var logkeep: Int = 10

    // 文件路径
    private val configFilePath: String = "/sdcard/cmriRfid/config.json"

    init {
        // 初始化时读取配置文件
        loadConfig()
    }

    // 读取配置文件
    private fun loadConfig() {
        try {
            val configFile = File(configFilePath)
            if (configFile.exists()) {
                val jsonString = configFile.readText()
                val json = JSONObject(jsonString)
                deviceId = json.optString("deviceId", "")
                mqttHost = json.optString("mqttHost", "")
                mode = json.optString("mode", "")
                httpHost = json.optString("httpHost", "")
                logkeep = json.optInt("logkeep", 0)
            } else {
                // 如果文件不存在，则写入默认配置
                saveConfig()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 读取异常时写入默认配置
            saveConfig()
        }
    }

    // 保存配置文件
    private fun saveConfig() {
        try {
            val configFile = File(configFilePath)
            val json = JSONObject()
            json.put("deviceId", deviceId)
            json.put("mqttHost", mqttHost)
            json.put("mode", mode)
            json.put("httpHost", httpHost)
            json.put("logkeep", logkeep)

            configFile.writeText(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e(e.message)
        }
    }

    // 提供公共方法用于修改字段的值
    fun updateDeviceId(newDeviceId: String) {
        deviceId = newDeviceId
        saveConfig()
    }

    fun updateMqttHost(newMqttHost: String) {
        mqttHost = newMqttHost
        saveConfig()
    }

    fun updateMode(newMode: String) {
        mode = newMode
        saveConfig()
    }

    fun updateHttpHost(newHttpHost: String) {
        httpHost = newHttpHost
        saveConfig()
    }

    fun updateLogkeep(newLogkeep: Int) {
        logkeep = newLogkeep
        saveConfig()
    }
}