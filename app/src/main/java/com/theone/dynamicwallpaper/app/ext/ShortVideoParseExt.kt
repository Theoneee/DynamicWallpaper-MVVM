package com.theone.dynamicwallpaper.app.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.theone.common.callback.OnKeyBackClickListener
import com.theone.common.ext.getChars
import com.theone.common.ext.setPrimaryClip
import com.theone.mvvm.ext.qmui.showMsgDialog
import java.util.regex.Pattern

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
 * @date 2021-11-15 11:27
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */

private val mFilterItems = arrayOf("https://v.kuaishouapp.com", "https://v.douyin.com")

fun Activity.getClipDataAndCheck(
    link: String?=null,
    onStart: (() -> Unit)? = null,
    onParse: ((String) -> Unit)? = null
) {
    link?.let {
        checkLink(it, onStart, onParse)
        return
    }
    window.decorView.post {
        (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).let {
            if (it.hasPrimaryClip() && it.primaryClip?.itemCount!! > 0) {
                it.primaryClip?.getItemAt(0)?.text?.let { text ->
                    checkLink(text.toString(), onStart, onParse)
                }
            }
        }
    }
}

/**
 * 检测链接是否需要处理
 * @param link String
 */
private fun Context.checkLink(
    link: String,
    onStart: (() -> Unit)? = null,
    onParse: ((String) -> Unit)? = null
) {
    if (link.isEmpty()) return
    if (link.getChars().isEmpty())
        return
    var isInFilter = false
    for (filter in mFilterItems) {
        if (filter.contains(filter)) {
            isInFilter = true
            break
        }
    }
    if (!isInFilter) return
    showParseDialog(link, onStart, onParse)
}


@SuppressLint("StaticFieldLeak")
private var mTipsDialog: QMUIDialog? = null

private fun Context.showParseDialog(
    url: String,
    onStart: (() -> Unit)? = null,
    onParse: ((String) -> Unit)? = null
) {
    if (mTipsDialog?.isShowing == true) {
        return
    }
    onStart?.invoke()
    mTipsDialog = showMsgDialog("提示", "检测到视频连接，是否去水印？", listener = { dialog, index ->
        dialog.dismiss()
        if (index > 0) {
            onParse?.invoke(url)
        }
        "".setPrimaryClip(this)
        mTipsDialog = null
    }).apply {
        setCanceledOnTouchOutside(false)
        setOnKeyListener(OnKeyBackClickListener())
    }
}

fun String.getUrl(): String {
    val pattern =
        Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]")
    val matcher = pattern.matcher(this)
    if (matcher.find()) {
        return matcher.group()
    }
    return ""
}


//    fun writeStringToFile(path: String, content: String) {
//        try {
//            val pw = PrintWriter(FileWriter(path))
//            pw.print(content)
//            pw.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
