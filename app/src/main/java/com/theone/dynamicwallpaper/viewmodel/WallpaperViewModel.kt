package com.theone.dynamicwallpaper.viewmodel

import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.data.repository.DataRepository
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.core.net.IPageInfo

class WallpaperViewModel : BaseListViewModel<Wallpaper>() {

    override fun requestServer() {
        request({
            DataRepository.getVideoList().let { response ->
                onSuccess(response, object : IPageInfo {

                    override fun getPage(): Int = 1

                    override fun getPageSize(): Int = response.size

                    override fun getPageTotalCount(): Int = 1

                    override fun getTotalCount(): Int = response.size

                })
            }
        })
    }

}