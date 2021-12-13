package com.theone.dynamicwallpaper.service

import android.media.MediaPlayer
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.theone.dynamicwallpaper.app.util.WallpaperManager
import com.theone.dynamicwallpaper.app.util.WallpaperUtil

/**
 * thx for https://blog.csdn.net/lmj623565791/article/details/72170299
 * <p>
 * 为什么要写两个Wallpaper Service？
 * 如果是一个，第一次启动了设置了壁纸。如果点击另外一个视频，是无法再次跳转到预览界面，只能是直发送广播更改视频地址，这样的效果不是想要的。
 * 直到有一次从其他APP里设置了，然后又回到这里点开后又到了预览界面，说明启动不同的服务就可以了。
 */
abstract class BaseWallpaperService : WallpaperService() {

    inner class VideoEngine : Engine() {
        private var mMediaPlayer: MediaPlayer? = null
        private var mVideoPath: String? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            WallpaperManager.getInstance().getVideoPathLiveData().observeForever {
                mVideoPath = it
            }
            WallpaperManager.getInstance().getVideoVolumeLiveData().observeForever {
                setVideoVolume(it)
            }
        }

        private fun setVideoVolume(isOpen: Boolean) {
            val volume = if (isOpen) 1.0f else 0f
            mMediaPlayer?.setVolume(volume, volume)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            mMediaPlayer = MediaPlayer().apply {
                setSurface(holder?.surface)
            }
            mMediaPlayer?.run {
                if (!mVideoPath.isNullOrEmpty()) {
                    try {
                        setDataSource(mVideoPath)
                        isLooping = true
                        setVideoVolume(WallpaperUtil.isWallpaperVideoVolumeOpen())
                        prepareAsync()
                        setOnPreparedListener {
                            it.start()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            mMediaPlayer?.let {
                if (visible) it.start() else it.pause()
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            mMediaPlayer?.release()
            mMediaPlayer = null
        }

    }

}