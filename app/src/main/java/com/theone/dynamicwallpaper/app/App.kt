package com.theone.dynamicwallpaper.app

import android.app.Application
import com.theone.dynamicwallpaper.BuildConfig
import com.theone.dynamicwallpaper.ui.activity.ErrorActivity
import com.theone.dynamicwallpaper.ui.activity.LauncherActivity
import com.theone.mvvm.core.CoreApplication
import com.theone.mvvm.core.ext.initCrashConfig
import com.theone.mvvm.core.util.RxHttpManager

class App:CoreApplication() {

    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun init(application: Application) {
        initCrashConfig(LauncherActivity::class.java,ErrorActivity::class.java)
        super.init(application)
        RxHttpManager.init().setDebug(DEBUG)
    }

}