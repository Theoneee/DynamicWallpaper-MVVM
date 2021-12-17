package com.theone.dynamicwallpaper.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.view.View
import com.hjq.toast.ToastUtils
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.theone.common.constant.BundleConstant
import com.theone.common.ext.*
import com.theone.dynamicwallpaper.R
import com.theone.dynamicwallpaper.app.ext.getClipDataAndCheck
import com.theone.dynamicwallpaper.data.bean.Wallpaper
import com.theone.dynamicwallpaper.databinding.ActivityShortVideoParseBinding
import com.theone.dynamicwallpaper.viewmodel.ShortVideoParseViewModel
import com.theone.mvvm.core.base.activity.BaseCoreActivity
import com.theone.mvvm.core.data.entity.QMUIItem
import com.theone.mvvm.core.ext.qmui.OnGridBottomSheetItemClickListener
import com.theone.mvvm.core.ext.qmui.showGridBottomSheet
import com.theone.mvvm.core.util.DownloadUtil

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

    private val mActions = mutableListOf<QMUIItem>(
        QMUIItem(TAG_SAVE, R.drawable.svg_operation_save),
        QMUIItem(TAG_LINK, R.drawable.svg_lover_link),
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
                    mViewModel.getResponseLiveData().value?.let {
                        showVideoActionDialog(it)
                    }
                }
                invisible()
            }
        }

        mBinding.videoPlayer.run {
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
        mViewModel.shareLink.set(url)
        mViewModel.requestServer()
    }

    private fun checkClipData(link: String? = null) {
        link?.let {
            if(it == mViewModel.shareLink.get()){
                return
            }
        }
        getClipDataAndCheck(link, {
            mBinding.videoPlayer.release()
            mActionBtn?.invisible()
        }, {
            startParse(it)
        })
    }

    override fun createObserver() {
        addLoadingObserve(mViewModel)
        mViewModel.getResponseLiveData().observeInActivity(this) {
            mBinding.videoPlayer.run {
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
            listener = object : OnGridBottomSheetItemClickListener {
                override fun onGridBottomSheetItemClick(
                    dialog: QMUIBottomSheet,
                    itemView: View,
                    tag: String
                ) {
                    dialog.dismiss()
                    when (tag) {
                        TAG_SAVE -> DownloadUtil.startDownload(
                            this@ShortVideoParseActivity,
                            file
                        )
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

    override fun onResume() {
        super.onResume()
        mBinding.videoPlayer.onVideoResume()
        checkClipData()
    }

    override fun onPause() {
        super.onPause()
        mBinding.videoPlayer.onVideoPause()
    }

    override fun onDestroy() {
        mBinding.videoPlayer.release()
        orientationUtils?.releaseListener()
        super.onDestroy()
    }

    override fun doOnBackPressed() {
        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mBinding.videoPlayer.fullscreenButton.performClick()
            return
        }
        //释放所有
        mBinding.videoPlayer.setVideoAllCallBack(null)
        super.doOnBackPressed()
    }

}