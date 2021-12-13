package com.theone.dynamicwallpaper.ui.activity

import android.util.SparseArray
import android.view.View
import android.view.animation.Animation
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.theone.common.ext.startActivity
import com.theone.dynamicwallpaper.databinding.ActivityLauncherBinding
import com.theone.mvvm.base.activity.BaseQMUIActivity
import com.theone.mvvm.base.activity.BaseVmDbActivity
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.widge.pullrefresh.WWLoadingView
import com.theone.mvvm.entity.ProgressBean

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
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class LauncherActivity: BaseCoreActivity<BaseViewModel,ActivityLauncherBinding>(),Animation.AnimationListener{

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        loadingView("startAnim")
        getContentView().postDelayed({
            QMUIViewHelper.fadeOut(mBinding.loadingView,500,this,true)
        },2000)
    }

    private fun loadingView(action:String){
        val clazz = WWLoadingView::class.java
        val declaredMethod = clazz.getDeclaredMethod(action)
        declaredMethod.isAccessible = true
        declaredMethod.invoke(mBinding.loadingView)
    }

    override fun createObserver() {

    }

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
        loadingView("stopAnim")
        startActivity(MainActivity::class.java,true)
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

}