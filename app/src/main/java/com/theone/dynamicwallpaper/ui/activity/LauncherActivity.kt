package com.theone.dynamicwallpaper.ui.activity

import android.view.View
import androidx.annotation.StringDef
import com.theone.common.ext.startActivity
import com.theone.dynamicwallpaper.databinding.ActivityLauncherBinding
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.widge.pullrefresh.WWLoadingView

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
class LauncherActivity : BaseCoreActivity<BaseViewModel, ActivityLauncherBinding>() {

    companion object {
        private const val START_ANIM = "startAnim"
        private const val STOP_ANIM = "startAnim"
    }

    @StringDef(START_ANIM, STOP_ANIM)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Action

    /**
     * 使用反射开启和关闭动画
     * @param action String
     */
    private fun loadingView(@Action action: String) {
        val clazz = WWLoadingView::class.java
        val declaredMethod = clazz.getDeclaredMethod(action)
        declaredMethod.isAccessible = true
        declaredMethod.invoke(mBinding.loadingView)
    }

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        loadingView(START_ANIM)
        getContentView().postDelayed({
            startActivity(MainActivity::class.java, true)
        }, 2000)
    }

    override fun createObserver() {

    }

    override fun onStop() {
        loadingView(STOP_ANIM)
        super.onStop()
    }

}