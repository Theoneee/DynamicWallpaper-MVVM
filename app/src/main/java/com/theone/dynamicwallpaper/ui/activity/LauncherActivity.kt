package com.theone.dynamicwallpaper.ui.activity

import android.view.View
import com.theone.common.ext.delay
import com.theone.common.ext.startActivity
import com.theone.dynamicwallpaper.databinding.ActivityLauncherBinding
import com.theone.dynamicwallpaper.viewmodel.LauncherViewModel
import com.theone.mvvm.core.base.activity.BaseCoreActivity

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2021-12-10 15:06
 * @describe 启动界面
 * @email 625805189@qq.com
 * @remark
 */
class LauncherActivity : BaseCoreActivity<LauncherViewModel, ActivityLauncherBinding>() {

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        getViewModel().animation.set(true)
        delay(2000) {
            startActivity(MainActivity::class.java, true)
        }
    }

    override fun createObserver() {

    }

    override fun onStop() {
        getViewModel().animation.set(false)
        super.onStop()
    }

}