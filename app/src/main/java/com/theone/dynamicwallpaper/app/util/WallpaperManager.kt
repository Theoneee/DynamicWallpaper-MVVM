package com.theone.dynamicwallpaper.app.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.dynamicwallpaper.data.constatnt.WallpaperConstant

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
 * @date 2021-12-10 09:28
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class WallpaperManager {

    companion object {
        private val INSTANCE = WallpaperManager()
        fun getInstance() = INSTANCE
    }

    private val mPath: MutableLiveData<String> = MutableLiveData()
    private val mVolume: MutableLiveData<Boolean> = MutableLiveData()
    private val mRefresh: UnPeekLiveData<Boolean> = UnPeekLiveData()

    fun getVideoPathLiveData(): LiveData<String> = mPath
    fun getVideoVolumeLiveData(): LiveData<Boolean> = mVolume
    fun getRefreshLiveData(): ProtectedUnPeekLiveData<Boolean> = mRefresh

    fun setVideoPath(path:String){
        mPath.value = path
    }

    fun setVideoVolume(silence:Boolean){
        mVolume.value = silence
    }

    fun setRefresh(refresh:Boolean){
        mRefresh.value = refresh
    }

}