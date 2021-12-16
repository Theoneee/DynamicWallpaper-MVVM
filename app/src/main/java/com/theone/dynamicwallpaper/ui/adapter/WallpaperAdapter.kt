package com.theone.dynamicwallpaper.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.databinding.ItemWallpaperBinding
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter

class WallpaperAdapter:TheBaseQuickAdapter<Wallpaper, ItemWallpaperBinding>(R.layout.item_wallpaper){


    init {
        val callBack: DiffUtil.ItemCallback<Wallpaper> = object : DiffUtil.ItemCallback<Wallpaper>() {

            override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
                oldItem.path == newItem.path

            override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
                oldItem.size == newItem.size

        }
        setDiffCallback(callBack)
    }


}