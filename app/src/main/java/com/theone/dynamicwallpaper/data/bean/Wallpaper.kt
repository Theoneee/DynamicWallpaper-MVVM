package com.theone.dynamicwallpaper.data.bean

import com.luck.picture.lib.config.PictureConfig
import com.theone.common.callback.IImageUrl
import com.theone.dynamicwallpaper.app.ext.formatTime

data class Wallpaper(
    var path: String = "",
    var thumbPath: String = "",
    var size: Long = 0,
    var addDate: Long = 0,
    var name: String = "",
    var type: Int = PictureConfig.TYPE_VIDEO
) : IImageUrl {

    fun getDuration(): String = size.formatTime()

    override fun getHeight(): Int  = 0

    override fun getImageUrl(): String = path

    override fun getRefer(): String? = null

    override fun getThumbnail(): String = thumbPath

    override fun getWidth(): Int = 0

    override fun isVideo(): Boolean = type == PictureConfig.TYPE_VIDEO

}