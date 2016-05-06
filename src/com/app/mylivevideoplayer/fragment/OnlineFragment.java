package com.app.mylivevideoplayer.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mylivevideoplayer.R;
/**
 * ‘⁄œﬂ ”∆µfragment
 * @author dh
 *
 */
public class OnlineFragment extends Fragment{
	private Activity mActivity;
     @Override
   public void onAttach(Context context) {
	  super.onAttach(context);
	  mActivity=(Activity) context;
    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view=inflater.inflate(R.layout.fragment_online, null);
    	return view;
    }
}
