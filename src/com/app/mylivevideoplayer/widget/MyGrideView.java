package com.app.mylivevideoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * �Զ���GrideView,��������scollview����ͻ
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
	//���ò�����
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}
