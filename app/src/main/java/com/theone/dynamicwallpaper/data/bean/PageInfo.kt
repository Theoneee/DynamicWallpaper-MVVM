package com.theone.dynamicwallpaper.data.bean

import com.theone.mvvm.core.net.IPageInfo

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
 * @date 2021-12-17 15:07
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class PageInfo:IPageInfo {

    override fun getPage(): Int = 1

    override fun getPageSize(): Int  =1

    override fun getPageTotalCount(): Int = 1

    override fun getTotalCount(): Int = 1
}