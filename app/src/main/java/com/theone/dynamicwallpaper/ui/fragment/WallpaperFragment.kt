package com.theone.dynamicwallpaper.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.theone.common.ext.dp2px
import com.theone.dynamicwallpaper.app.ext.setAdapterAnimation
import com.theone.dynamicwallpaper.app.ext.startWallPaper
import com.theone.dynamicwallpaper.app.util.WallpaperUtil
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.widge.SpaceItemDecoration
import com.theone.dynamicwallpaper.ui.adapter.WallpaperAdapter
import com.theone.dynamicwallpaper.viewmodel.WallpaperViewModel
import com.theone.mvvm.core.base.fragment.BasePagerPullRefreshFragment
import com.theone.mvvm.core.data.enum.LayoutManagerType
import com.theone.mvvm.core.databinding.BasePullFreshFragmentBinding
import com.theone.mvvm.core.widge.pullrefresh.PullRefreshLayout

class WallpaperFragment :
        BasePagerPullRefreshFragment<Wallpaper, WallpaperViewModel, BasePullFreshFragmentBinding>() {

    override fun getLayoutManagerType(): LayoutManagerType = LayoutManagerType.GRID

    override fun getSpanCount(): Int = 3

    override fun getItemSpace(): Int = 5

    override fun createAdapter(): BaseQuickAdapter<Wallpaper, *> = WallpaperAdapter()

    override fun getRecyclerView(): RecyclerView = mBinding.recyclerView

    override fun getRefreshLayout(): PullRefreshLayout = mBinding.refreshLayout

    override fun isLazyLoadData(): Boolean = false

    override fun initAdapter() {
        super.initAdapter()
        mAdapter.setAdapterAnimation()
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
        mAdapter.getDiffer().submitList(data.toMutableList()) {
            getRecyclerView().scrollToPosition(0)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = adapter.getItem(position) as Wallpaper
        WallpaperManager.getInstance().setVideoPath(data.path)
        startWallPaper(requireActivity(), WallpaperUtil.getNextWallpaperService())
    }

    override fun createObserver() {
        super.createObserver()
        WallpaperManager.getInstance().getRefreshLiveData().observeInFragment(this) {
            // 下载完后延迟下，本地数据还没刷新
            getContentView().postDelayed({
                onAutoRefresh()
            }, 1000)
        }
    }

}