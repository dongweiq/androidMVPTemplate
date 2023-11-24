package com.cmri.uhf.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.format.DateUtils
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.cmri.uhf.App
import com.cmri.uhf.R
import com.cmri.uhf.databinding.ActivityMainBinding
import com.cmri.uhf.event.MessageEvent
import com.cmri.uhf.mvp.XActivity
import com.cmri.uhf.utils.AppConfig
import com.elvishew.xlog.XLog
import com.tencent.bugly.crashreport.CrashReport
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity :
    XActivity<MainPresenter>() {


    lateinit var mainBinding: ActivityMainBinding
    lateinit var disposable: Disposable
    lateinit var dialog: MaterialDialog
    fun clickViews(view: View) {
        when (view.id) {
            R.id.launcher -> {
//                setDefaultLauncher()
                goLauncherSetting()

            }

            R.id.event -> {
                EventBus.getDefault().post(MessageEvent("showProgress"))
            }
            R.id.start ->{

            }
        }
    }

    private fun goLauncherSetting() {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        startActivity(intent)
    }

    private fun setDefaultLauncher() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mainBinding.logger.setLogFormatter { logContent, logType ->
            TimeUtils.getNowString(
                SimpleDateFormat("[HH:mm]", Locale.CHINA)
            ) + logContent
        }
        disposable = rxPermissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
            .subscribe { granted: Boolean ->
                if (granted) {
                    XLog.d("【initData】已获取授权，开始初始化")
                    mainBinding.logger.logNormal("【initData】已获取授权，开始初始化")
                    // 权限被授予
                    checkStoragePermission()
                    // p.initUHF()
                    AppConfig(App.getAppContext())
                } else {
                    XLog.d("【initData】权限被拒绝！")
                    mainBinding.logger.logError("【initData】权限被拒绝！")
                    // 权限被拒绝
                    // 可以给用户一些提示，或执行适当的处理
                    finish()
                }
            }
    }

    override fun getRootView(): View {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        return mainBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        // 在 Activity 销毁时取消订阅，防止内存泄漏
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun newP(): MainPresenter {
        return MainPresenter()
    }

    fun dismissProgress() {
        XLog.d("【MainActivity】隐藏对话框")
        dialog?.dismiss()
    }

    fun showProgress() {
        XLog.d("【MainActivity】显示对话框")
        dialog = MaterialDialog.Builder(this)
            .limitIconToDefaultSize()
            .title("提示")
            .content("初始化中")
            .progress(true, 0)
            .progressIndeterminateStyle(false)
            .show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msgEvent: MessageEvent) {
        XLog.d("【MainActivity】 onEvent收到消息：${msgEvent.message}")
        when (msgEvent.message) {
            "uhf" -> {

            }

            "showProgress" -> {
                showProgress()
            }

            "dismissProgress" -> {
                dismissProgress()
            }
        }
    }

}