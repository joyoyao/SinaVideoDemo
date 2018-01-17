package com.sina.playerdemo_v2.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.sina.playerdemo_v2.R;
import com.sina.playerdemo_v2.VideoConstant;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoCompletionListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoErrorListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADEndListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDPlayerErrorInfo;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

public class TestSimpleADActivity extends Activity implements OnVDVideoInsertADListener, OnVDVideoFrameADListener,
        OnVDVideoPlaylistListener, OnVDVideoCompletionListener, OnVDVideoErrorListener, OnVDVideoInsertADEndListener {

    private VDVideoView mVDVideoView = null;
    private final static String TAG = "Test1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_test);
        // adIV是广告部分的控件，这个需要产品侧开发人员自己处理了
        ImageView adIV = (ImageView) findViewById(R.id.adFrameImageView);
        adIV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(TestSimpleADActivity.this, "点击了静帧广告", Toast.LENGTH_LONG).show();
            }
        });
        // 从layout里面得到播放器ID
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());

        VDVideoListInfo infoList = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        // 多流广告方式，就是在播放列表中配置广告流的方式
        // 单流方式：INSERTAD_TYPE_SINGLE，暂时不支持，如果设置了，会报exception
        infoList.mInsertADType = VDVideoListInfo.INSERTAD_TYPE_MULTI;
        // 如果是两个或者以上的广告流，因为没办法直接取到每条流的时间，所以需要手动设置，否则会报exception
        // BTW：ticker组件最大显示长度为两位，也就是99，如果超过99秒的广告时长设置，会一直显示99，直到当前播放时间小于99为止
        // infoList.mInsertADSecNum = 133;
        // 填充播放列表，第一个是广告，理论上，可以在任何位置
        infoList.mIsSetInsertADTime = false;
        info.mTitle = "这个是一个测试广告";

        info.mPlayUrl = VideoConstant.videoUrlList[0];
        info.mIsInsertAD = true;
        infoList.addVideoInfo(info);

        info = new VDVideoInfo();
        info.mTitle = "这就是一个测试视频0";
        info.mVideoId = "10000001";
        info.mPlayUrl = VideoConstant.videoUrlList[0];
        infoList.addVideoInfo(info);

        info = new VDVideoInfo();
        info.mTitle = "这就是一个测试视频1";
        info.mVideoId = "10000002";
        info.mPlayUrl = VideoConstant.videoUrlList[1];
        infoList.addVideoInfo(info);

        info = new VDVideoInfo();
        info.mTitle = "这就是一个测试视频2";
        info.mVideoId = "10000003";
        info.mPlayUrl = VideoConstant.videoUrlList[2];
        infoList.addVideoInfo(info);

        info = new VDVideoInfo();
        info.mTitle = "这就是一个测试视频3";
        info.mVideoId = "10000004";
        info.mPlayUrl = VideoConstant.videoUrlList[3];
        infoList.addVideoInfo(info);

        // 广告回调接口
        // FrameAD表明是一个帧间广告（点击暂停等出现的那个图）
        mVDVideoView.setFrameADListener(this);
        // InsertAD表明是一个前贴片广告，（插入广告）
        mVDVideoView.setInsertADListener(this);
        // 播放列表回调接口
        mVDVideoView.setPlaylistListener(this);
        mVDVideoView.setCompletionListener(this);
        mVDVideoView.setErrorListener(this);
        mVDVideoView.setInsertADEndListener(this);

        // 设置播放列表类型
        VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
        if (listView != null) {
            listView.onVideoList(infoList);
        }
        // 初始化播放器以及播放列表
        mVDVideoView.open(TestSimpleADActivity.this, infoList);
        // 开始播放，直接选择序号即可
        mVDVideoView.play(0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mVDVideoView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mVDVideoView.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mVDVideoView.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mVDVideoView.onStart();
    }

    @Override
    protected void onDestroy() {
        mVDVideoView.release(false);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
        }

    }

    /**
     * 播放列表里面点击了某个视频，触发外部事件
     */
    @Override
    public void onPlaylistClick(VDVideoInfo info, int p) {
        // TODO Auto-generated method stub
        if (info == null) {
            LogS.e(TAG, "info is null");
        }
        List<VDVideoInfo> list = new ArrayList<VDVideoInfo>();
        VDVideoInfo adInfo = new VDVideoInfo();
        adInfo.mTitle = "这个是一个测试广告";
        adInfo.mPlayUrl = "http://123.126.53.108:10009/2.mp4";
        adInfo.mIsInsertAD = true;
        list.add(adInfo);

        mVDVideoView.refreshInsertADList(list, info);
        mVDVideoView.play(p);
    }

    /**
     * 帧间动画传回的接口，在有帧间动画时候，然后按暂停按钮，会调用此方法，在里面写响应的逻辑，看匹配那个广告
     */

    /**
     * 视频插入广告传回的接口，表明当前的广告被点击了『了解更多』
     */
    @Override
    public void onInsertADClick(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "插入广告被点击了", Toast.LENGTH_LONG).show();
    }

    /**
     * 视频插入广告传回的接口，表明当前的广告被点击了『去掉广告』，按照其他视频逻辑，直接跳转会员页
     */
    @Override
    public void onInsertADStepOutClick(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "去掉广告被点击了", Toast.LENGTH_LONG).show();
    }

    /**
     * 静帧广告换图，从这儿来
     */
    @Override
    public void onFrameADPrepared(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "开始换图", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVDVideoError(VDVideoInfo info, int errWhat, int errExtra) {
        // TODO Auto-generated method stub
        switch (errWhat) {
            default :
            case VDPlayerErrorInfo.MEDIA_ERROR_WHAT_UNKNOWN :
                Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();
                break;
            case VDPlayerErrorInfo.MEDIA_ERROR_WHAT_M3U8_PARSER :
                if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_M3U8_NO_CONTENT) {
                    Toast.makeText(this, "m3u8内容为空", Toast.LENGTH_LONG).show();
                } else if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_M3U8_PARSE) {
                    Toast.makeText(this, "m3u8解析错误", Toast.LENGTH_LONG).show();
                }
                break;
            case VDPlayerErrorInfo.MEDIA_ERROR_WHAT_SERVER_DIED :
                if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_PLAYER_IO) {
                    Toast.makeText(this, "IO错误", Toast.LENGTH_LONG).show();
                } else if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_PLAYER_DECODER_FAIL) {
                    Toast.makeText(this, "解码错误", Toast.LENGTH_LONG).show();
                } else if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_PLAYER_UNSUPPORTED) {
                    Toast.makeText(this, "解码器不支持此格式", Toast.LENGTH_LONG).show();
                } else if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_PLAYER_MALFORMED) {
                    Toast.makeText(this, "编码不是标准格式，无法解析", Toast.LENGTH_LONG).show();
                } else if (errExtra == VDPlayerErrorInfo.MEDIA_ERROR_EXTRA_PLAYER_TIMED_OUT) {
                    Toast.makeText(this, "超时", Toast.LENGTH_LONG).show();
                }
                break;
            case VDPlayerErrorInfo.MEDIA_ERROR_WHAT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK :
                break;
        }

    }

    @Override
    public void onVDVideoCompletion(VDVideoInfo info, int status) {
        // TODO Auto-generated method stub
        if (info.mIsInsertAD) {
            Toast.makeText(this, "广告播放完了", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "正片播放完了", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInsertADEnd(VDVideoInfo info, int status) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "贴片广告播放结束,status:" + status, Toast.LENGTH_LONG).show();
    }

}
