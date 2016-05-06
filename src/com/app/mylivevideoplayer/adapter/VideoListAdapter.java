package com.app.mylivevideoplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import com.app.mylivevideoplayer.R;
import com.app.mylivevideoplayer.entity.LoadedImage;
import com.app.mylivevideoplayer.entity.Videos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter{
  private int localposition=0;
  private List<Videos> lists;
  private ArrayList<LoadedImage> photos=new ArrayList<LoadedImage>();
  private boolean imageChange=false;
  private LayoutInflater inflater;
  
 public VideoListAdapter(Context context,List<Videos> lists) {
	inflater=LayoutInflater.from(context);
	this.lists = lists;
}
  public void addPhoto(LoadedImage image){
	  photos.add(image);
  }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.videolist_item, null);
			holder=new Viewholder();
			holder.ivImage=(ImageView) convertView.findViewById(R.id.iv_video);
			holder.tvTime=(TextView) convertView.findViewById(R.id.tv_video_duration);
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_video_title);
		    convertView.setTag(holder);
		}else{
			holder=(Viewholder) convertView.getTag();
		}
		  holder.tvTitle.setText(lists.get(position).getTitle());
		  long min=lists.get(position).getDuration()/1000/60;
		  long sec=lists.get(position).getDuration()/1000%60;
		  holder.tvTime.setText(min+":"+sec);
		  holder.ivImage.setImageBitmap(photos.get(position).getBitmap());
		return convertView;
	}
  class Viewholder{
	  ImageView ivImage;
	  TextView tvTitle;
	  TextView tvTime;
  }
}
