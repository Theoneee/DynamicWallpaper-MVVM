package com.theone.dynamicwallpaper.app.ext

import androidx.annotation.StringDef
import com.theone.mvvm.core.app.widge.pullrefresh.WWLoadingView

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
 * @date 2022-02-16 14:59
 * @describe 注解和反射的使用
 * @email 625805189@qq.com
 * @remark
 */

const val START_ANIM = "startAnim"
const val STOP_ANIM = "startAnim"

@StringDef(START_ANIM, STOP_ANIM)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class Action

/**
 * 动画的开启和关闭方法是私有的，这里使用反射开启和关闭动画
 * @param action String  TODO 使用注解对传入的参数做限制
 */
fun WWLoadingView.action(@Action action: String) {
    val clazz = WWLoadingView::class.java
    val declaredMethod = clazz.getDeclaredMethod(action)
    declaredMethod.isAccessible = true
    declaredMethod.invoke(this)
}

fun WWLoadingView.startAnim(){
    action(START_ANIM)
}

fun WWLoadingView.stopAnim(){
    action(STOP_ANIM)
}