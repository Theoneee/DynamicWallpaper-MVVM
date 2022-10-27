package com.theone.dynamicwallpaper.app.ext

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.ui.activity.MainActivity
import com.theone.mvvm.ext.qmui.showFailTipsDialog
import java.util.*


private val mFormatBuilder = StringBuilder()
private val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    mFormatBuilder.setLength(0)
    return if (hours > 0) {
        mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        mFormatter.format("%02d:%02d", minutes, seconds)
    }.toString()

}

fun Activity.getWallpaperIntent(clazz: Class<*>):Intent = Intent(android.app.WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
    putExtra(android.app.WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
        ComponentName(this@getWallpaperIntent,clazz)
    )
}

fun MainActivity.startWallPaper(clazz: Class<*>){
    WallpaperUtil.setCurrentWallpaperService(clazz)
    registerForActivityResult?.launch(getWallpaperIntent(clazz))
}

fun Context.goHome() {
    startActivity(Intent().apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_HOME)
    })
}

/**
 * 加入qq聊天群
 * https://qun.qq.com/join.html
 */
fun Context.joinQQGroup(key: String) {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        showFailTipsDialog("未安装手机QQ或安装的版本不支持")
    }
}

//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int = 2) {
    mode.let {
        //等于0，关闭列表动画 否则开启
        if (it == 0) {
            animationEnable = false
        } else {
            animationEnable = true
            setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[it - 1])
        }
    }
}