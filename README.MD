# 无源rfid终端
## 简介
```
主要功能
1. 程序随安卓系统启动
2. 作为服务
3. 判断传感器，当运动时读取周围所有的rfid标签，静止时不读取
4. 通过mqtt上传所有rfid标签
```

## 框架说明
- 使用mvp架构
- 使用viewBinding获取viewId
- 使用kotlin作为主要代码编写
- 使用EventBus作为通信桥梁
- 使用retrofit作为网络请求框架
- 使用mqtt与后端长连接通讯
- 使用bulgy做应用监测
- 使用crashHandler保存崩溃日志
- 使用xlog记录日志
- 使用双进程守护进行包活(launcher的话不需要)