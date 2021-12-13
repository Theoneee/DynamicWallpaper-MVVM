package com.theone.dynamicwallpaper.ui.fragment

import android.view.View
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.theone.common.callback.OnKeyBackClickListener
import com.theone.common.ext.dp2px
import com.theone.dynamicwallpaper.R
import com.theone.mvvm.base.viewmodel.BaseViewModel
import com.theone.mvvm.core.base.fragment.BaseTabInTitleFragment
import com.theone.mvvm.core.data.entity.QMUITabBean
import com.theone.mvvm.core.ext.qmui.addTab
import com.theone.mvvm.ext.qmui.showMsgDialog

class MainFragment : BaseTabInTitleFragment<BaseViewModel>() {

    override fun isExitPage(): Boolean = true

    override fun isStatusBarLightMode(): Boolean = true

    override fun initTopBar() {
        getTopBar()?.run {
            addRightImageButton(
                R.drawable.mz_titlebar_ic_setting_dark,
                R.id.topbar_right_view
            ).setOnClickListener {
                startFragment(SettingFragment())
            }
        }
        super.initTopBar()
    }

    override fun initView(root: View) {
        initTopBar()
        requestPermission()
        getMagicIndicator()?.setPadding(0,0,0,dp2px(10))
    }

    override fun initTabAndFragments(
        tabs: MutableList<QMUITabBean>,
        fragments: MutableList<QMUIFragment>
    ) {
        with(tabs) {
            addTab("本地视频")
        }

        with(fragments) {
            add(WallpaperFragment())
        }
    }

    /**
     * 请求权限
     */
    private fun requestPermission() {
        XXPermissions.with(requireActivity())
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .constantRequest()
            .request(object : OnPermission {

                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {
                    if (all) {
                        startInit()
                    }
                }

                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    context?.showMsgDialog(
                        "提示",
                        "权限被禁止，请在设置里打开权限",
                        leftAction = null,
                        rightAction = "打开权限",
                        listener = QMUIDialogAction.ActionListener { dialog, _ ->
                            dialog.dismiss()
                            XXPermissions.startPermissionActivity(requireContext(), denied)

                        },
                        prop = QMUIDialogAction.ACTION_PROP_NEGATIVE
                    )?.run {
                        setCanceledOnTouchOutside(false)
                        setOnKeyListener(OnKeyBackClickListener())
                    }
                }
            })
    }

}