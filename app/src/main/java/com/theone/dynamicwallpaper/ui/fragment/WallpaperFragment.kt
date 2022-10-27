package com.theone.dynamicwallpaper.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theone.common.ext.dp2px
import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.app.ext.setAdapterAnimation
import com.theone.dynamicwallpaper.app.ext.startWallPaper
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.widge.SpaceItemDecoration
import com.theone.dynamicwallpaper.ui.activity.MainActivity
import com.theone.dynamicwallpaper.ui.adapter.WallpaperAdapter
import com.theone.dynamicwallpaper.viewmodel.WallpaperViewModel
import com.theone.loader.callback.Callback
import com.theone.mvvm.core.app.widge.pullrefresh.PullRefreshLayout
import com.theone.mvvm.core.base.callback.CoreOnPermission
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.base.loader.callback.LoadingCallback
import com.theone.mvvm.core.data.enum.LayoutManagerType
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding

class WallpaperFragment :
    BasePagerPullRefreshFragment<Wallpaper, WallpaperViewModel, BasePullFreshFragmentBinding>() {

    override fun getLayoutManagerType(): LayoutManagerType = LayoutManagerType.GRID

    override fun getSpanCount(): Int = 3

    override fun getItemSpace(): Int = 5

    override fun createAdapter(): BaseQuickAdapter<Wallpaper, *> = WallpaperAdapter()

    override fun getRecyclerView(): RecyclerView = getDataBinding().recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = getDataBinding().refreshLayout

    override fun loaderDefaultCallback() = LoadingCallback::class.java

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
        setTitle("本地视频")
    }

    override fun initData() {
        XXPermissions.with(requireActivity())
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .constantRequest()
            .request(object : CoreOnPermission(mActivity) {
                override fun hasPermission(granted: MutableList<String>?, all: Boolean) {
                    requestServer()
                }
            })
    }

    override fun initAdapter() {
        super.initAdapter()
        getAdapter().setAdapterAnimation()
    }

    override fun initRecyclerView() {
        super.initRecyclerView()
        val space = dp2px(getItemSpace())
        getRecyclerView().setPadding(space, space, space, space)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return SpaceItemDecoration(dp2px(getItemSpace()))
    }

    override fun onRefreshSuccess(data: List<Wallpaper>) {
        setRefreshLayoutEnabled(true)
        getAdapter().getDiffer().submitList(data.toMutableList()) {
            getRecyclerView().scrollToPosition(0)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = adapter.getItem(position) as Wallpaper
        WallpaperManager.getInstance().setVideoPath(data.path)
        (activity as MainActivity?)?.startWallPaper(WallpaperUtil.getNextWallpaperService())
    }

    override fun createObserver() {
        super.createObserver()
        WallpaperManager.getInstance().getRefreshLiveData().observe(this) {
            // 下载完后延迟下，本地数据还没刷新
            getContentView().postDelayed({
                onAutoRefresh()
            }, 1000)
        }
    }

}