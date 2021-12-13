package com.theone.dynamicwallpaper.data.repository

import com.alibaba.fastjson.JSONObject
import com.luck.picture.lib.config.PictureConfig
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import org.jsoup.Jsoup
import rxhttp.toOkResponse
import rxhttp.toStr
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.RxHttpNoBodyParam

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
 * @date 2021-09-13 16:30
 * @describe 短视频解析
 * @email 625805189@qq.com
 * @remark
 */
class ShortVideoParseRepository {

    private fun request(url: String?): RxHttpNoBodyParam {
        return RxHttp
            .get(url)
            .addHeader(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36"
            )
            .setAssemblyEnabled(false)
            .setCacheMode(CacheMode.ONLY_NETWORK)
    }

    private suspend fun requestStringResponse(url: String?): String {
        return request(url)
            .toStr()
            .await()
    }

    private suspend fun requestKS(link: String):Wallpaper{
        val response = requestStringResponse(link)
        return parseKSVideo(response)
    }

    private suspend fun requestDouYin(url: String?): Wallpaper {
        // 这里抖音请求的是 https://v.douyin.com/R9218N5/
        val response = request(url)
            .toOkResponse()
            .await()
        // 然后得到重定向后的地址：https://www.iesdouyin.com/share/video/7030626978512309511/?region=...
        val location = response.request.url.toString()
        // 得到中间的视频ID：7030626978512309511 , 拼接成一个请求
        val path =
            "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=${location.getVideoId()}"
        val str = requestStringResponse(path)
        // 下面就是解析Json了
        return parseDouYinVideo(str)
    }

    private fun parseKSVideo(response: String): Wallpaper {
        Jsoup.parse(response).run {
            val videoElement = select("video[id=video-player]").first()
            val thumbnail = videoElement.attr("poster")
            val path = videoElement.attr("src")
            val title = videoElement.attr("alt")
            return Wallpaper(path, thumbnail, name = title, type = PictureConfig.TYPE_VIDEO)
        }
    }

    private fun parseDouYinVideo(response: String): Wallpaper {
        val json: JSONObject = JSONObject.parseObject(response)
        val item = json.getJSONArray("item_list").getJSONObject(0);
        val video = item.getJSONObject("video")
        val title = item.getJSONObject("share_info").getString("share_title")
        val path = video.getJSONObject("play_addr").getJSONArray("url_list")[0].toString()
        val thumbnail = video.getJSONObject("cover").getJSONArray("url_list")[0].toString()
        return Wallpaper(path, thumbnail, name = title, type = PictureConfig.TYPE_VIDEO)

    }

    private fun String.getVideoId(): String {
        val startStr = "/video/"
        val start = indexOf(startStr) + startStr.length
        val end = lastIndexOf("/?")
        return substring(start, end)
    }

    suspend fun parseVideo(link: String): Wallpaper {
        return if (link.contains("kuaishou")) {
            requestKS(link)
        } else {
            requestDouYin(link)
        }
    }

}


