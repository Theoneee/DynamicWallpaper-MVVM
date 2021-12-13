package com.theone.dynamicwallpaper.app

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import com.theone.dynamicwallpaper.BuildConfig
import com.theone.dynamicwallpaper.ui.activity.ErrorActivity
import com.theone.dynamicwallpaper.ui.activity.LauncherActivity
import com.theone.mvvm.base.BaseApplication
import com.theone.mvvm.core.ext.initLoadSir
import com.theone.mvvm.core.util.NotificationManager

class App:BaseApplication() {

    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun init(application: Application) {
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        initCrashConfig()
        super.init(application)
        NotificationManager.getInstance().register()
        initLoadSir()
        ToastUtils.init(this)
    }

    private fun initCrashConfig() {
        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(LauncherActivity::class.java) // 重启的activity
            .errorActivity(ErrorActivity::class.java)
            .apply()
    }

}