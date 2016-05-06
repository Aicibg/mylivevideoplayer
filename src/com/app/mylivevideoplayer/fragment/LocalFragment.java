package com.app.mylivevideoplayer.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.mylivevideoplayer.R;
import com.app.mylivevideoplayer.activity.VideoPlayActivity;
import com.app.mylivevideoplayer.adapter.VideoListAdapter;
import com.app.mylivevideoplayer.db.AbstructProvider;
import com.app.mylivevideoplayer.db.VideoProvider;
import com.app.mylivevideoplayer.entity.LoadedImage;
import com.app.mylivevideoplayer.entity.Videos;
/**
 * 本地视频fragment
 * @author dh
 *
 */
public class LocalFragment extends Fragment{
	private Activity mActivity;
	private ListView lvVideos;
	private List<Videos> lists;
	private VideoListAdapter adapter;
	private int videosize;
    @Override
  public void onAttach(Context context) {
	  super.onAttach(context);
	  mActivity=(Activity) context;
   }
    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
   		Bundle savedInstanceState) {
   	View view=inflater.inflate(R.layout.fragment_local, null);
   	
   	lvVideos=(ListView) view.findViewById(R.id.lv_local_video);
   	AbstructProvider abstruct=new VideoProvider(mActivity);
   	lists=abstruct.getVideoList();
   	videosize=lists.size();
   	adapter=new VideoListAdapter(mActivity, lists);
   	lvVideos.setAdapter(adapter);
   	lvVideos.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent(mActivity,VideoPlayActivity.class);
			intent.putExtra("path",lists.get(position).getPath());
			startActivity(intent);
		}
	});
   	loadImage();
   	return view;
   }
    
	private void loadImage() {
		new loadImageFromSdcard().execute();
	}
	
	/*
	 * 获取视频缩略图
	 */
	public Bitmap getVideoThumbnail(String videopath,int width,int height,int kind){
		Bitmap bitmap=null;
		bitmap=ThumbnailUtils.createVideoThumbnail(videopath, kind);
		bitmap=ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
		
	}
	
	private void addImage(LoadedImage...value){
		for(LoadedImage image:value){
			adapter.addPhoto(image);
			adapter.notifyDataSetChanged();
		}
	}
	
	class loadImageFromSdcard extends AsyncTask<Object, LoadedImage, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap=null;
			for(int i=0;i<videosize;i++){
				bitmap=getVideoThumbnail(lists.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
				if(bitmap!=null){
					publishProgress(new LoadedImage(bitmap));
				}
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(LoadedImage... values) {
			addImage(values);
		}
		
	}
	
}
