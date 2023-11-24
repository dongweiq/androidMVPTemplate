package com.cmri.uhf.utils

import android.content.Context
import com.cmri.uhf.App
import com.elvishew.xlog.XLog
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

object MqttHelper {

    private lateinit var mqttAndroidClient: MqttAndroidClient

    fun initialize(brokerUrl: String, clientId: String) {
        mqttAndroidClient = MqttAndroidClient(App.getAppContext(), brokerUrl, clientId)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                XLog.d("MqttHelper "+ "connectComplete: $reconnect, $serverURI")
                // 连接成功后的回调
            }

            override fun connectionLost(cause: Throwable?) {
                XLog.d("MqttHelper "+ "connectionLost: $cause")
                // 连接丢失后的回调
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                XLog.d("MqttHelper "+ "messageArrived: $topic, $message")
                // 收到消息后的回调
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // 消息发布完成后的回调
                XLog.d("MqttHelper "+ "deliveryComplete: $token")
            }
        })

        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false

        mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                // 连接成功后的回调
                XLog.d("MqttHelper "+ "onSuccess: $asyncActionToken")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                // 连接失败后的回调
                XLog.d("MqttHelper "+ "onFailure: $asyncActionToken, $exception")
            }
        })
    }

    fun publish(topic: String, message: String) {
        XLog.d("MqttHelper "+"topic:$topic, message:$message")
        val mqttMessage = MqttMessage()
        mqttMessage.payload = message.toByteArray()

        mqttAndroidClient.publish(topic, mqttMessage)
    }

    fun subscribe(topic: String, qos: Int) {
        mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                // 订阅成功后的回调
                XLog.d("MqttHelper "+"asyncActionToken: $asyncActionToken")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                // 订阅失败后的回调
                XLog.d("MqttHelper "+"asyncActionToken: $asyncActionToken")
            }
        })
    }

    fun disconnect() {
        mqttAndroidClient.disconnect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                // 断开连接成功后的回调
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                // 断开连接失败后的回调
            }
        })
    }
}
