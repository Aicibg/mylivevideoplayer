
package com.app.mylivevideoplayer.activity;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mylivevideoplayer.R;

public class VideoPlayActivity extends Activity implements OnCompletionListener,OnInfoListener{
   private String mpath;
   private VideoView mvideoView;
   private LinearLayout layoutloading;
   private FrameLayout fChange;
   private TextView tvoperationpercent,tvoperationfull;
   private TextView tvVoiceAndbrightChange;
   private AudioManager audioManager;
   //音量
   private int mMaxvolume;
   //当前音量
   private int mVolune=-1;
   //当前亮度
   private float mBrightness=-1f;
   //当前缩放模式
   private int mlayout=VideoView.VIDEO_LAYOUT_ZOOM;
   //滑动监听
   private GestureDetector mgestureDetector;
   private MediaController medioController;
   private static final int UPDATE_SCREEN_BRIGHT=1;
   private static final int UPDATE_VOICE=2;
   private static final int ADD=11;
   private static final int MINUS=12;
   //定时隐藏
   private Handler mhandler=new Handler(){
	   public void handleMessage(android.os.Message msg) {
//		   switch (msg.what) {
//		case UPDATE_SCREEN_BRIGHT:
//			tvVoiceAndbrightChange.setText((String) msg.obj);
//			break;
//
//		case UPDATE_VOICE:
//			tvVoiceAndbrightChange.setText((String) msg.obj);
//			break;
//		}
	   };
   };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(getApplicationContext());
		setContentView(R.layout.activity_video_play);
		initViews();
		mpath=getIntent().getStringExtra("path");
		Log.i("test","path="+mpath);
		//绑定事件
		mvideoView.setOnCompletionListener(this);
		mvideoView.setOnInfoListener(this);
		//绑定数据
		audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//获取最大音量值
		mMaxvolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if(mpath.startsWith("http:")){
			mvideoView.setVideoURI(Uri.parse(mpath));
		}else{
			mvideoView.setVideoPath(mpath);
		}
		//播放控制器，暂停，播放，快进
		medioController=new MediaController(getApplicationContext());
		medioController.setFileName(mpath);
		mvideoView.setMediaController(medioController);
		mvideoView.requestFocus();
		
		mgestureDetector=new GestureDetector(getApplicationContext(), new MygestureDetector());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
	}
    
	@Override
		protected void onPause() {
			super.onPause();
			if(mvideoView!=null){
				mvideoView.pause();
			}
		}
	
	 @Override
		protected void onResume() {
			super.onResume();
			if(mvideoView!=null){
				mvideoView.start();
			}
		}
	  @Override
		protected void onDestroy() {
			super.onDestroy();
			if(mvideoView!=null){
				mvideoView.stopPlayback();
			}
		}
	  
	private void initViews() {
		mvideoView=(VideoView) findViewById(R.id.videoview);
		layoutloading=(LinearLayout) findViewById(R.id.video_loading);
		fChange=(FrameLayout) findViewById(R.id.operation_volume_brightness);
		tvVoiceAndbrightChange=(TextView) findViewById(R.id.tv_operation_change);
		tvoperationpercent=(TextView) findViewById(R.id.tv_operation_percent);
		tvoperationfull=(TextView) findViewById(R.id.tv_operation_full);
	}
    
	 @Override
		public boolean onTouchEvent(MotionEvent event) {
			if(mgestureDetector.onTouchEvent(event)){
				return true;
			}
			switch (event.getAction()&MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
				endGesture();
				break;

			}
			return super.onTouchEvent(event);
		}
	 
	private void endGesture() {
		mVolune=-1;
		mBrightness=-1f;
		
		//隐藏
		mhandler.removeMessages(0);
		mhandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				fChange.setVisibility(View.GONE);
			}
		}, 500);
	}
	
	public class MygestureDetector extends SimpleOnGestureListener{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if(mlayout==VideoView.VIDEO_LAYOUT_ZOOM){
				mlayout=VideoView.VIDEO_LAYOUT_ORIGIN;
			}else{
				mlayout++;
			}
			if(mvideoView!=null){
				mvideoView.setVideoLayout(mlayout, 0);
			}
			return true;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float oldX=e1.getX(),oldY=e1.getY();
			int y=(int) e2.getRawY();
			//int x=(int) e2.getX();
			DisplayMetrics display=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(display);;
			int windowWidth=display.widthPixels;
			int windowHeight=display.heightPixels;
//			//向左滑动
//			if(oldX>x&(oldX-x)/windowWidth>windowWidth/5){
//				onvolumeslide((oldX-x)/windowWidth,MINUS);
//			}else if(oldX<x&(x-oldX)/windowWidth>windowWidth/5){//向右滑动
//				onvolumeslide((x-oldX)/windowWidth,ADD);
//			}
//			//向上滑动
//			if(oldY>y&(oldY-y)/windowHeight>windowHeight/4){
//				onbrightnessSlide((oldY-y)/windowHeight,MINUS);
//			}else if(oldY<y&(y-oldY)/windowHeight>windowHeight/4){//向下滑动 
//				onbrightnessSlide((y-oldY)/windowHeight,ADD);
//			}
			 if (oldX > windowWidth * 4.0 / 5)// 右边滑动  
				 onvolumeslide((oldY - y) / windowHeight);  
	            else if (oldX< windowWidth / 5.0)// 左边滑动  
	            	onbrightnessSlide((oldY - y) / windowHeight);
			 return super.onScroll(e1, e2, distanceX, distanceY); 
		}
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		//播放完成
	}



	public void onbrightnessSlide(float f) {
		if(mBrightness==-1){
			mBrightness=getWindow().getAttributes().screenBrightness;
			if(mBrightness<=0.00f){
				mBrightness=0.50f;
			}
			if(mBrightness<0.01f){
				mBrightness=0.01f;
			}
			fChange.setVisibility(View.VISIBLE);
		}
		//获得当前屏幕亮度
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		
		//判断是加是减
//		if((arg-ADD)==0){
//			lp.screenBrightness=f+mBrightness;
//		}else if((arg-MINUS)==0){
//			lp.screenBrightness=mBrightness-f;
//		}
		lp.screenBrightness=f+mBrightness;
		if(lp.screenBrightness<0.01f){
			lp.screenBrightness=0.01f;
		}else if(lp.screenBrightness>1.0f){
			lp.screenBrightness=1.0f;
		}
		//设置屏幕亮度
		getWindow().setAttributes(lp);
//		Message msg=Message.obtain();
//		msg.what=UPDATE_SCREEN_BRIGHT;
//		msg.obj="亮度："+lp.screenOrientation/1.0f*100+"%";
//		mhandler.sendMessage(msg);
		//得到tvoperationpercent控件的LayoutParams()，LayoutParams包含了子控件对父控件的期望，告诉父控件
		//子控件想要的宽，高等信息
		ViewGroup.LayoutParams lpa=tvoperationpercent.getLayoutParams();
		lpa.width=(int) (findViewById(R.id.tv_operation_full).getLayoutParams().width*lp.screenBrightness);
		//子控件setLayoutParams()，最后设置的是子控件自身的大小，也就是告诉父控件我想要变成什么样
		tvoperationpercent.setLayoutParams(lpa);
		tvoperationfull.setText("亮度");
	}

	public void onvolumeslide(float f) {
		if(mVolune==-1){
			mVolune=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if(mVolune<0){
				mVolune=0;
			}
			fChange.setVisibility(View.VISIBLE);
		}
		int index=(int) ((f*mMaxvolume)+mVolune);
		//判断是加是减
//		if((arg-ADD)==0){
//			 index=(int) ((f*mMaxvolume)+mVolune);
//		}else if((arg-MINUS)==0){
//			index=(int) (mVolune-(f*mMaxvolume));
//		}
		
		if(index>mMaxvolume){
			index=mMaxvolume;
		}else if(index<0){
			index=0;
		}
		//变更音量
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
		//变更进度
//		Message msg=Message.obtain();
//		msg.what=UPDATE_VOICE;
//		msg.obj="音量："+index/mMaxvolume*100+"%";
//		mhandler.sendMessage(msg);
		ViewGroup.LayoutParams lp=tvoperationpercent.getLayoutParams();
		lp.width=findViewById(R.id.tv_operation_full).getLayoutParams().width*index/mMaxvolume;
		tvoperationpercent.setLayoutParams(lp);
	    tvoperationfull.setText("音量");
	}
   //横竖屏切换
	@Override
		public void onConfigurationChanged(Configuration newConfig) {
			if(mvideoView!=null){
				//设置屏幕模式
				mvideoView.setVideoLayout(mlayout, 0);
			}
			super.onConfigurationChanged(newConfig);
		}
	
	public void stopPlay(){
		if(mvideoView!=null){
			mvideoView.pause();
		}
	}
	
	public void startPlay(){
		if(mvideoView!=null){
			mvideoView.start();
		}
	}
	
	public boolean isPlaying(){
		return mvideoView!=null&&mvideoView.isPlaying();
	}
	//是否需要重复播放
	private boolean needResume;
	
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			//缓存，暂停播放
			if(isPlaying()){
				stopPlay();
				needResume=true;
			}
			layoutloading.setVisibility(View.VISIBLE);
			break;

		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			//缓存完成，继续播放
			if(needResume){
				startPlay();
			}
			layoutloading.setVisibility(View.GONE);
			break;
			
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			//缓存进度
			Log.i("test", "download:"+extra);
			break;
		}
		return true;
	}
}
