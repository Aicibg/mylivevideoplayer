package com.app.mylivevideoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 自定义GrideView,不滚动与scollview不冲突
 * @author dh
 *
 */
public class MyGrideView extends GridView{

	public MyGrideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
   
	public MyGrideView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//设置不滚动
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}
