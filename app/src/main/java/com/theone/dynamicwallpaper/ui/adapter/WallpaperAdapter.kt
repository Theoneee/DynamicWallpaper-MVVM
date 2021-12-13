package com.theone.dynamicwallpaper.ui.adapter

import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.databinding.ItemWallpaperBinding
import com.theone.mvvm.core.base.adapter.TheBaseQuickAdapter

class WallpaperAdapter:TheBaseQuickAdapter<Wallpaper, ItemWallpaperBinding>(R.layout.item_wallpaper)