package com.theone.dynamicwallpaper.ui.activity

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.theone.common.constant.BundleConstant
import com.theone.dynamicwallpaper.app.ext.getClipDataAndCheck
import com.theone.dynamicwallpaper.app.ext.goHome
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.ui.fragment.MainFragment
import com.theone.dynamicwallpaper.ui.fragment.WallpaperFragment
import com.theone.mvvm.base.activity.BaseFragmentActivity
import com.theone.mvvm.core.service.DownloadService
import com.theone.mvvm.ext.qmui.showSuccessTipsDialog

// 调用不同的试试
//@DefaultFirstFragment(MainFragment::class)
@DefaultFirstFragment(WallpaperFragment::class)
class MainActivity : BaseFragmentActivity() {

    private val mFilter: IntentFilter by lazy {
        IntentFilter().apply {
            addAction(DownloadService.DOWNLOAD_OK)
        }
    }

    var registerForActivityResult: ActivityResultLauncher<Intent>?=null

    private var mReceiver = object : BroadcastReceiver() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerForActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 在设置壁纸界面，无论返回还是设置成功后都会回调此方法
            // 所以要判断当前运行的壁纸服务是否为当前设置的
            if (WallpaperUtil.isLiveWallpaperChanged()) {
                if (WallpaperUtil.isSuccessGoHome()) {
                    goHome()
                } else {
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
