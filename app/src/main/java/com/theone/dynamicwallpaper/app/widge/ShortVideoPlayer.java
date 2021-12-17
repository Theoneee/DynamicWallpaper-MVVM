package com.theone.dynamicwallpaper.app.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.theone.common.callback.IImageUrl;
import com.theone.dynamicwallpaper.R;
import com.theone.mvvm.core.util.FileDirectoryManager;

import java.io.File;

public class ShortVideoPlayer extends StandardGSYVideoPlayer {

    private ImageView mCover;
    private TextView mTvSpeed;
    private String[] mSpeedItems = new String[]{"0.5X", "正常", "1.5X", "2.0X"};

    public ShortVideoPlayer(Context context) {
        super(context);
    }

    public ShortVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mCover = findViewById(R.id.thumbImage);
        mTvSpeed = findViewById(R.id.speed);
        mTvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeedDialog();
            }
        });
        findViewById(R.id.fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWindowFullscreen(getContext(),false,true);
            }
        });
        setAutoFullWithSize(true);
        setReleaseWhenLossAudio(true);
        setIsTouchWiget(false);
        setLooping(true);
        setNeedShowWifiTip(false);
        setRotateViewAuto(true);
        setLockLand(false);
        setReleaseWhenLossAudio(false);
    }

    private void showSpeedDialog( ) {
        String current = mTvSpeed.getText().toString();
        int selectIndex = -1;
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getContext());
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                .setTitle("播放速度")
                .setAddCancelBtn(false)
                .setAllowDrag(true)
                .setNeedRightMark(true)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        setVideoSpeed(position);
                    }
                });
        for (int i = 0; i < mSpeedItems.length; i++) {
            builder.addItem(mSpeedItems[i]);
            if (current.equals(mSpeedItems[i])) {
                selectIndex = i;
            }
        }
        builder.setCheckedIndex(selectIndex);
        builder.build().show();
    }

    private void setVideoSpeed(int position){
        setSpeed((position + 1) * 0.5f);
        mTvSpeed.setText(mSpeedItems[position]);
    }

    public void setVideoData(IImageUrl file, String title) {
        loadCoverImage(file);
        setVideoSpeed(1);
        mPlayTag = file.getImageUrl();
        setUpLazy(file.getImageUrl(), false, new File(FileDirectoryManager.INSTANCE.getVideoPath()), null, title);
    }

    private void loadCoverImage(IImageUrl file) {
        Glide.with(getContext())
                .load(file.getThumbnail())
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.image_snap_loading_bg)
                        .override(file.getWidth(), file.getHeight())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                )
                .into(mCover);
    }

    @Override
    public int getLayoutId() {
        return R.layout.welfare_video_player_layout;
    }

    @Override
    protected void hideAllWidget() {
//        setViewShowState(getTitleTextView(), VISIBLE);
    }

    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();
        setViewShowState(getTitleTextView(), INVISIBLE);
    }

    @Override
    protected void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        setViewShowState(getTitleTextView(), VISIBLE);
        setViewShowState(mCover, INVISIBLE);
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        setViewShowState(mCover, VISIBLE);
//        setViewShowState(getTitleTextView(), VISIBLE);
    }

    @Override
    public void release() {
        super.release();
        dismissProgressDialog();
        dismissVolumeDialog();
    }

}
