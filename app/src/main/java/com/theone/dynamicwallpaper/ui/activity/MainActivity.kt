package com.theone.dynamicwallpaper.ui.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.theone.common.constant.BundleConstant
import com.theone.dynamicwallpaper.app.ext.getClipDataAndCheck
import com.theone.dynamicwallpaper.app.ext.goHome
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.data.constatnt.WallpaperConstant
import com.theone.dynamicwallpaper.ui.fragment.MainFragment
import com.theone.mvvm.base.activity.BaseFragmentActivity
import com.theone.mvvm.core.service.DownloadService
import com.theone.mvvm.ext.qmui.showSuccessTipsDialog

@DefaultFirstFragment(MainFragment::class)
class MainActivity : BaseFragmentActivity() {

    private val mFilter: IntentFilter by lazy {
        IntentFilter().apply {
            addAction(DownloadService.DOWNLOAD_OK)
        }
    }

    private var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                DownloadService.DOWNLOAD_OK -> {
                    // 下载完成后通知刷新本地视频数据
                    WallpaperManager.getInstance().setRefresh(true)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == WallpaperConstant.REQUEST_LIVE_PAPER){
            if(WallpaperUtil.isLiveWallpaperChanged()){
                if(WallpaperUtil.isSuccessGoHome()){
                    goHome()
                }else{
                    showSuccessTipsDialog("更换壁纸成功")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, mFilter)
        getClipDataAndCheck {
            startActivity(Intent(this, ShortVideoParseActivity::class.java).apply {
                putExtra(BundleConstant.DATA, it)
            })
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

}
