package com.theone.dynamicwallpaper.viewmodel

import com.theone.dynamicwallpaper.data.bean.PageInfo
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.data.repository.DataRepository
import com.theone.mvvm.core.base.viewmodel.BaseListViewModel
import com.theone.mvvm.core.net.IPageInfo

class WallpaperViewModel : BaseListViewModel<Wallpaper>() {

    override fun requestServer() {
        request({
            onSuccess(DataRepository.getVideoList(), PageInfo())
        })
    }

}