package com.theone.dynamicwallpaper.ui.fragment

import android.content.Context
import android.view.View
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.common.ext.dp2px
import com.theone.dynamicwallpaper.R
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.app.ext.qmui.addTab
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.core.data.entity.QMUIItemBean
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator

class MainFragment : BaseTabInTitleFragment<BaseViewModel>() {

    override fun isExitPage(): Boolean = true

    override fun isStatusBarLightMode(): Boolean = true

    override fun QMUITopBarLayout.initTopBar() {
        addLeftImageButton(R.drawable.svg_restrict, R.id.topbar_left_button).setOnClickListener {
            ToastUtils.show("开发中...")
        }
        addRightImageButton(
            R.drawable.mz_titlebar_ic_setting_dark,
            R.id.topbar_right_view
        ).setOnClickListener {
            startFragment(SettingFragment())
        }
        setCenterView(getMagicIndicator())
    }

    override fun initView(root: View) {
        super.initView(root)
        getMagicIndicator().setPadding(0, 0, 0, dp2px(10))
    }

    override fun initTabAndFragments(
        tabs: MutableList<QMUIItemBean>,
        fragments: MutableList<QMUIFragment>
    ) {
        with(tabs) {
            addTab("本地视频")
        }

        with(fragments) {
            add(WallpaperFragment())
        }
    }

    override fun getNavIndicator(context: Context): IPagerIndicator? = null

}