package com.app.mylivevideoplayer.activity;

import java.util.ArrayList;
import java.util.List;

import com.app.mylivevideoplayer.R;
import com.app.mylivevideoplayer.R.id;
import com.app.mylivevideoplayer.R.layout;
import com.app.mylivevideoplayer.adapter.MyPageAdapter;
import com.app.mylivevideoplayer.fragment.LocalFragment;
import com.app.mylivevideoplayer.fragment.OnlineFragment;

import io.vov.vitamio.Vitamio;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {
   private RadioButton rbOnline,rbLocal;
   private RadioGroup radiogroup;
   private ViewPager vpVideo;
   private List<Fragment> fragments=new ArrayList<Fragment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//³õÊ¼»¯¿Ø¼þ
		initViews();
		OnlineFragment onlineFragment=new OnlineFragment();
		fragments.add(onlineFragment);
		LocalFragment localFragment=new LocalFragment();
		fragments.add(localFragment);
		
		vpVideo.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fragments));
		vpVideo.setOffscreenPageLimit(2);
		radiogroup.setOnCheckedChangeListener(new MycheckedchangeListener());
		vpVideo.addOnPageChangeListener(new MypagechangeListener());
	}
	
	private void initViews() {
		rbLocal=(RadioButton) findViewById(R.id.radio_local);
		rbOnline=(RadioButton) findViewById(R.id.radio_online);
		vpVideo=(ViewPager) findViewById(R.id.viewpage);
		radiogroup=(RadioGroup) findViewById(R.id.radioGroup1);
	}
   
	class MycheckedchangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_online:
				vpVideo.setCurrentItem(0);
				rbOnline.setTextColor(Color.BLUE);
				rbLocal.setTextColor(Color.BLACK);
				break;

            case R.id.radio_local:
            	vpVideo.setCurrentItem(1);
            	rbLocal.setTextColor(Color.BLUE);
            	rbOnline.setTextColor(Color.BLACK);
				break;
			}
		}
		
	}
	
	class MypagechangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				radiogroup.check(R.id.radio_online);
				rbOnline.setTextColor(Color.BLUE);
				rbLocal.setTextColor(Color.BLACK);
				break;

			case 1:
				radiogroup.check(R.id.radio_local);
				rbLocal.setTextColor(Color.BLUE);
				rbOnline.setTextColor(Color.BLACK);
				break;
			}
		}
		
	}
   
}
