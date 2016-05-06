package com.app.mylivevideoplayer.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.app.mylivevideoplayer.entity.Videos;

public class VideoProvider implements AbstructProvider{
    private Context context;
  
	public VideoProvider(Context context) {
	super();
	this.context = context;
}

	@Override
	public List<Videos> getVideoList() {
		List<Videos> lists=null;
		if(context!=null){
			Cursor cursor=context.getContentResolver().
					query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
			if(cursor!=null){
				lists=new ArrayList<Videos>();
				while(cursor.moveToNext()){
					int id=cursor.getInt(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
					String title=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
					String album=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
					String artist=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
					String displayname=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
					String mimetype=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
					String path=cursor.getString(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					long duration=cursor.getLong(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					long size=cursor.getLong(
							cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
					Videos video=new Videos(id, title, album, artist, displayname, mimetype, path, size, duration);
					lists.add(video);
				}
				cursor.close();
			}
		}
		return lists;
	}

}
