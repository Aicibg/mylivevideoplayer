package com.app.mylivevideoplayer.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter{
	private List<Fragment> fragments;
	   
   public MyPageAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments=fragments;
	}

@Override
public Fragment getItem(int arg0) {
	// TODO Auto-generated method stub
	return fragments.get(arg0);
}
@Override
public int getCount() {
	// TODO Auto-generated method stub
	return fragments.size();
}
   

}
