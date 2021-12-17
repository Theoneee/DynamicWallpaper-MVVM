package com.theone.dynamicwallpaper.viewmodel

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.theone.dynamicwallpaper.app.ext.getUrl
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.data.repository.ShortVideoParseRepository
import com.theone.mvvm.callback.databind.StringObservableField
import com.theone.mvvm.core.base.viewmodel.BaseRequestViewModel

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
 * @date 2021-11-11 14:53
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class ShortVideoParseViewModel : BaseRequestViewModel<Wallpaper>() {

    /**
     * 分享的链接
     */
    val shareLink: StringObservableField = StringObservableField()

    override fun requestServer() {
        // 拿到分享链接里的网址
        val url = shareLink.get().getUrl()
        if(url.isEmpty()) return
        request({
            onSuccess(ShortVideoParseRepository().parseVideo(url))
        }, "解析中")
    }

}