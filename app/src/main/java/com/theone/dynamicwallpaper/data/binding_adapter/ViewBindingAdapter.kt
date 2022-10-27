package com.theone.dynamicwallpaper.data.binding_adapter

import androidx.databinding.BindingAdapter
import com.theone.dynamicwallpaper.app.ext.START_ANIM
import com.theone.dynamicwallpaper.app.ext.STOP_ANIM
import com.theone.dynamicwallpaper.app.ext.action
import com.theone.mvvm.core.app.widge.pullrefresh.WWLoadingView

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
 * @date 2022-02-16 15:01
 * @describe 这里写好后，在任何界面，然后控制ViewModel的变量驱动xml里
 * @email 625805189@qq.com
 * @remark
 *
 *   示例：
 *          <com.theone.mvvm.core.widge.pullrefresh.WWLoadingView
 *           android:id="@+id/loading_view"
 *           android:layout_width="100dp"
 *           android:layout_height="100dp"
 *           app:animation="@{vm.animation}"/>
 *
 */
object ViewBindingAdapter {

    @BindingAdapter(value = ["animation"])
    @JvmStatic
    fun loadingView(
        loadingView: WWLoadingView,
        animation: Boolean,
    ) {
        loadingView.action(if(animation) START_ANIM else STOP_ANIM)
    }

}