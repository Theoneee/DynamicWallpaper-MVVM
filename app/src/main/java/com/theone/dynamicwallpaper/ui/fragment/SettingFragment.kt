package com.theone.dynamicwallpaper.ui.fragment

import android.view.View
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.theone.common.ext.startWebView
import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.app.ext.joinQQGroup
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.databinding.FragmentGroupListViewBinding
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseCoreFragment
import com.theone.mvvm.ext.qmui.addToGroup
import com.theone.mvvm.ext.qmui.createItem
import com.theone.mvvm.ext.qmui.createSwitchItem
import com.theone.mvvm.ext.qmui.setTitleWitchBackBtn

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
 * @date 2021-12-10 15:36
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class SettingFragment : BaseCoreFragment<BaseViewModel, FragmentGroupListViewBinding>(),
    View.OnClickListener {

    private lateinit var mVolume: QMUICommonListItemView
    private lateinit var mGoHome: QMUICommonListItemView
    private lateinit var mProject: QMUICommonListItemView
    private lateinit var mJoinUs: QMUICommonListItemView

    override fun initView(root: View) {
        setTitleWitchBackBtn("设置")
        getDataBinding().groupListView.run {
            mVolume = createSwitchItem(
                "视频声音",
                drawable = R.drawable.svg_volume,
                isCheck = WallpaperUtil.isWallpaperVideoVolumeOpen()
            ) { _, isOpen ->
                WallpaperManager.getInstance().setVideoVolume(isOpen)
                WallpaperUtil.setWallpaperVideoVolume(isOpen)
            }

            mGoHome = createSwitchItem(
                "设置成功后跳转至桌面",
                drawable = R.drawable.svg_phone_home,
                isCheck = WallpaperUtil.isSuccessGoHome()
            ) { _, goHome ->
                WallpaperUtil.setSuccessGoHome(goHome)
            }

            mProject = createItem("项目地址", "DynamicWallpaper", R.drawable.svg_project)
            mJoinUs = createItem("加入我们", "QQ群：761201022", R.drawable.svg_qq)
            addToGroup(mVolume, mGoHome, title = "壁纸服务")
            addToGroup(mProject, mJoinUs, title = "项目", listener = this@SettingFragment)
        }
    }

    override fun createObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            mProject -> startWebView("https://github.com/Theoneee/DynamicWallpaper-MVVM")
            mJoinUs -> context?.joinQQGroup("26hK_GKmpQJbBHpfPIMlJztVmzTRyzZp")
            else -> {
            }
        }
    }

}