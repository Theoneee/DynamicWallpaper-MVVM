package com.theone.dynamicwallpaper.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.view.View
import com.hjq.toast.ToastUtils
import com.luck.picture.lib.config.SelectMimeType
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.theone.common.callback.IImageUrl
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.common.util.FileUtils
import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.app.ext.getClipDataAndCheck
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.databinding.ActivityShortVideoParseBinding
import com.theone.dynamicwallpaper.viewmodel.ShortVideoParseViewModel
import com.theone.mvvm.core.app.ext.qmui.OnGridBottomSheetItemClickListener
import com.theone.mvvm.core.app.ext.qmui.showGridBottomSheet
import com.theone.mvvm.core.app.util.DownloadUtil
import com.theone.mvvm.core.app.util.FileDirectoryManager
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.data.entity.DownloadBean
import com.theone.mvvm.core.data.entity.QMUIItemBean
import com.theone.mvvm.core.service.startDownloadService
import java.io.File

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
 * @date 2021-11-11 15:03
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class ShortVideoParseActivity :
    BaseCoreActivity<ShortVideoParseViewModel, ActivityShortVideoParseBinding>() {

    private var orientationUtils:OrientationUtils?=null
    private var mActionBtn: QMUIAlphaImageButton? = null

    companion object {
        val TAG_SAVE = "下载"
        val TAG_LINK = "复制链接"
        val TAG_PUBLISH = "发布"
    }

    private val mActions = mutableListOf<QMUIItemBean>(
        QMUIItemBean(TAG_SAVE, R.drawable.svg_operation_save),
        QMUIItemBean(TAG_LINK, R.drawable.svg_lover_link),
    )

    override fun translucentFull(): Boolean = true

    override fun isStatusBarLightMode(): Boolean = false

    override fun initView(root: View) {
        getTopBar()?.run {
            setBackgroundAlpha(0)
            addLeftImageButton(
                R.drawable.mz_comment_titlebar_ic_close_light,
                R.id.qmui_topbar_item_left_back
            ).setOnClickListener {
                doOnBackPressed()
            }
            mActionBtn = addRightImageButton(
                R.drawable.mz_titlebar_ic_more_light,
                R.id.topbar_right_view
            ).apply {
                setOnClickListener {
                    getViewModel().getResponseLiveData().value?.let {
                        showVideoActionDialog(it)
                    }
                }
                invisible()
            }
        }

        getDataBinding().videoPlayer.run {
            orientationUtils = OrientationUtils(this@ShortVideoParseActivity, this)
            fullscreenButton.setOnClickListener {
                orientationUtils?.resolveByClick()
            }
        }
    }

    override fun initData() {
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(Intent.EXTRA_TEXT)?.let {
            checkClipData(it)
        }
        intent?.getStringExtra(BundleConstant.DATA)?.let {
            startParse(it)
        }
    }

    private fun startParse(url: String) {
        getViewModel().shareLink.set(url)
        getViewModel().requestServer()
    }

    private fun checkClipData(link: String? = null) {
        link?.let {
            if(it == getViewModel().shareLink.get()){
                return
            }
        }
        getClipDataAndCheck(link, {
            getDataBinding().videoPlayer.release()
            mActionBtn?.invisible()
        }, {
            startParse(it)
        })
    }

    override fun createObserver() {
        addLoadingObserve(getViewModel())
        getViewModel().getResponseLiveData().observe(this) {
            getDataBinding().videoPlayer.run {
                setVideoData(it, it.name)
                startPlayLogic()
                visible()
            }
            mActionBtn?.visible()
        }
    }

    private fun showVideoActionDialog(file: Wallpaper) {
        showGridBottomSheet(
            mActions,
            listener = object : OnGridBottomSheetItemClickListener<QMUIItemBean> {
                override fun onGridBottomSheetItemClick(
                    dialog: QMUIBottomSheet,
                    itemView: View,
                    item: QMUIItemBean
                ) {
                    dialog.dismiss()
                    when (item.getItemTitle()) {
                        TAG_SAVE -> startDownload(file)
                        TAG_LINK -> {
                            file.path.setPrimaryClip(this@ShortVideoParseActivity)
                            ToastUtils.show("链接已复制到剪切板")
                        }
                        else -> {
                        }
                    }
                }
            }).show()
    }

    private fun startDownload(file:Wallpaper){
        val mineType = when (file.resType()) {
            IImageUrl.Type.VIDEO -> SelectMimeType.SYSTEM_VIDEO
            IImageUrl.Type.IMAGE -> SelectMimeType.SYSTEM_IMAGE
            else -> SelectMimeType.SYSTEM_AUDIO
        }

        val fileName = DownloadUtil.getDownloadFileName(
            file.getImageUrl(),
            file.resType() == IImageUrl.Type.VIDEO
        )

        val path = FileUtils.createExternalFileDir(this,getString(R.string.app_name)+ File.separator+"download").path

        val download = DownloadBean(
            file.getImageUrl(),
            path,
            fileName
        )
        ToastUtils.show("开始下载")
        startDownloadService(download)
    }

    override fun onResume() {
        super.onResume()
        getDataBinding().videoPlayer.onVideoResume()
        checkClipData()
    }

    override fun onPause() {
        super.onPause()
        getDataBinding().videoPlayer.onVideoPause()
    }

    override fun onDestroy() {
        getDataBinding().videoPlayer.release()
        orientationUtils?.releaseListener()
        super.onDestroy()
    }

    override fun doOnBackPressed() {
        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getDataBinding().videoPlayer.fullscreenButton.performClick()
            return
        }
        //释放所有
        getDataBinding().videoPlayer.setVideoAllCallBack(null)
        super.doOnBackPressed()
    }

}