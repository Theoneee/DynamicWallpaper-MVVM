package com.theone.dynamicwallpaper.data.repository

import android.provider.MediaStore
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.mvvm.base.appContext
import kotlinx.coroutines.*
import java.util.*

object DataRepository {

    suspend fun getVideoList(): MutableList<Wallpaper> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<Wallpaper>()
            val columns = arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
            )
            appContext.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, null
            )?.let { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val size =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        val width =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH))
                        val height =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT))
                        // 限制时长在5-60秒之内，既然是做视频壁纸，也要限制下视频的宽高，拿常见的 1920*1080 算
                        if (size/1000 in 5..60 && height > width*1.6) {
                            val path =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                            val thumbPath =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
                            val addDate =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                            list.add(Wallpaper(path, thumbPath, size, addDate.toLong(), ""))
                        }
                    } while (cursor.moveToNext())
                }

            }
            if (list.size > 0) {
                Collections.sort<Wallpaper>(list, comp)
            }
            list
        }
    }

    private var comp = Comparator<Wallpaper> { p1, p2 ->
            val time1: Long = p1.addDate
            val time2: Long = p2.addDate
            when {
                time1 < time2 -> 1
                time1 > time2 -> -1
                else -> 0
            }
        }

}