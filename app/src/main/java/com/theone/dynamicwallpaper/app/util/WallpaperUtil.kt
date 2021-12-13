package com.theone.dynamicwallpaper.app.util

import android.app.Application
import android.app.WallpaperManager
import android.content.SharedPreferences
import com.theone.dynamicwallpaper.service.WallpaperService1
import com.theone.dynamicwallpaper.service.WallpaperService2
import com.theone.mvvm.base.appContext

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
 * @date 2021-12-10 10:48
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
object WallpaperUtil {

    /**
     * 设置壁纸后记录下当前的服务名称
     */
    private const val CURRENT_WALLPAPER_SERVICE = "current_wallpaper_service"

    /**
     * 设置成功后是否直接跳转至桌面
     */
    private const val SUCCESS_GO_HOME = "success_go_home"

    /**
     * 壁纸视频声音
     */
    private const val WALLPAPER_VIDEO_VOLUME = "wallpaper_video_volume"

    fun setCurrentWallpaperService(clazz: Class<*>) {
        MMKVUtil.putString(CURRENT_WALLPAPER_SERVICE, clazz.canonicalName)
    }

    private fun getCurrentWallpaperService(): String? = MMKVUtil.getString(CURRENT_WALLPAPER_SERVICE)

    fun setSuccessGoHome(isGoHome:Boolean) {
        MMKVUtil.putBoolean(SUCCESS_GO_HOME, isGoHome)
    }

    fun isSuccessGoHome(): Boolean = MMKVUtil.getBoolean(SUCCESS_GO_HOME,true)

    fun setWallpaperVideoVolume(open:Boolean) {
        MMKVUtil.putBoolean(WALLPAPER_VIDEO_VOLUME, open)
    }

    fun isWallpaperVideoVolumeOpen(): Boolean = MMKVUtil.getBoolean(WALLPAPER_VIDEO_VOLUME,true)

    fun getNextWallpaperService(): Class<*> {
        val isService1 = getCurrentService() == WallpaperService1::class.java.canonicalName
        return if(isService1) WallpaperService2::class.java else WallpaperService1::class.java
    }

    /**
     * 获取当前运行的壁纸服务名称
     * @return String
     */
    private fun getCurrentService():String{
        val wallpaperManager = WallpaperManager.getInstance(appContext)
        val serviceName = wallpaperManager?.wallpaperInfo?.serviceName
        return serviceName?:""
    }

    /**
     * 当前运行的壁纸服务是否为当前App的
     * @return Boolean
     */
    fun isCurrentAppWallpaper():Boolean{
        return getCurrentService().let {
            it == WallpaperService1::class.java.canonicalName || it == WallpaperService2::class.java.canonicalName
        }
    }

    /**
     * 动态壁纸是否更换
     * @return Boolean
     */
    fun isLiveWallpaperChanged():Boolean{
        return getCurrentService() == getCurrentWallpaperService()
    }

}