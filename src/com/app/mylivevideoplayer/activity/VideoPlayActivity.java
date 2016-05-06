
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
   //����
   private int mMaxvolume;
   //��ǰ����
   private int mVolune=-1;
   //��ǰ����
   private float mBrightness=-1f;
   //��ǰ����ģʽ
   private int mlayout=VideoView.VIDEO_LAYOUT_ZOOM;
   //��������
   private GestureDetector mgestureDetector;
   private MediaController medioController;
   private static final int UPDATE_SCREEN_BRIGHT=1;
   private static final int UPDATE_VOICE=2;
   private static final int ADD=11;
   private static final int MINUS=12;
   //��ʱ����
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
		//���¼�
		mvideoView.setOnCompletionListener(this);
		mvideoView.setOnInfoListener(this);
		//������
		audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//��ȡ�������ֵ
		mMaxvolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if(mpath.startsWith("http:")){
			mvideoView.setVideoURI(Uri.parse(mpath));
		}else{
			mvideoView.setVideoPath(mpath);
		}
		//���ſ���������ͣ�����ţ����
		medioController=new MediaController(getApplicationContext());
		medioController.setFileName(mpath);
		mvideoView.setMediaController(medioController);
		mvideoView.requestFocus();
		
		mgestureDetector=new GestureDetector(getApplicationContext(), new MygestureDetector());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ�ƺ���
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
		
		//����
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
//			//���󻬶�
//			if(oldX>x&(oldX-x)/windowWidth>windowWidth/5){
//				onvolumeslide((oldX-x)/windowWidth,MINUS);
//			}else if(oldX<x&(x-oldX)/windowWidth>windowWidth/5){//���һ���
//				onvolumeslide((x-oldX)/windowWidth,ADD);
//			}
//			//���ϻ���
//			if(oldY>y&(oldY-y)/windowHeight>windowHeight/4){
//				onbrightnessSlide((oldY-y)/windowHeight,MINUS);
//			}else if(oldY<y&(y-oldY)/windowHeight>windowHeight/4){//���»��� 
//				onbrightnessSlide((y-oldY)/windowHeight,ADD);
//			}
			 if (oldX > windowWidth * 4.0 / 5)// �ұ߻���  
				 onvolumeslide((oldY - y) / windowHeight);  
	            else if (oldX< windowWidth / 5.0)// ��߻���  
	            	onbrightnessSlide((oldY - y) / windowHeight);
			 return super.onScroll(e1, e2, distanceX, distanceY); 
		}
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		//�������
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
		//��õ�ǰ��Ļ����
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		
		//�ж��Ǽ��Ǽ�
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
		//������Ļ����
		getWindow().setAttributes(lp);
//		Message msg=Message.obtain();
//		msg.what=UPDATE_SCREEN_BRIGHT;
//		msg.obj="���ȣ�"+lp.screenOrientation/1.0f*100+"%";
//		mhandler.sendMessage(msg);
		//�õ�tvoperationpercent�ؼ���LayoutParams()��LayoutParams�������ӿؼ��Ը��ؼ������������߸��ؼ�
		//�ӿؼ���Ҫ�Ŀ��ߵ���Ϣ
		ViewGroup.LayoutParams lpa=tvoperationpercent.getLayoutParams();
		lpa.width=(int) (findViewById(R.id.tv_operation_full).getLayoutParams().width*lp.screenBrightness);
		//�ӿؼ�setLayoutParams()��������õ����ӿؼ�����Ĵ�С��Ҳ���Ǹ��߸��ؼ�����Ҫ���ʲô��
		tvoperationpercent.setLayoutParams(lpa);
		tvoperationfull.setText("����");
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
		//�ж��Ǽ��Ǽ�
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
		//�������
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
		//�������
//		Message msg=Message.obtain();
//		msg.what=UPDATE_VOICE;
//		msg.obj="������"+index/mMaxvolume*100+"%";
//		mhandler.sendMessage(msg);
		ViewGroup.LayoutParams lp=tvoperationpercent.getLayoutParams();
		lp.width=findViewById(R.id.tv_operation_full).getLayoutParams().width*index/mMaxvolume;
		tvoperationpercent.setLayoutParams(lp);
	    tvoperationfull.setText("����");
	}
   //�������л�
	@Override
		public void onConfigurationChanged(Configuration newConfig) {
			if(mvideoView!=null){
				//������Ļģʽ
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
	//�Ƿ���Ҫ�ظ�����
	private boolean needResume;
	
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			//���棬��ͣ����
			if(isPlaying()){
				stopPlay();
				needResume=true;
			}
			layoutloading.setVisibility(View.VISIBLE);
			break;

		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			//������ɣ���������
			if(needResume){
				startPlay();
			}
			layoutloading.setVisibility(View.GONE);
			break;
			
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			//�������
			Log.i("test", "download:"+extra);
			break;
		}
		return true;
	}
}
