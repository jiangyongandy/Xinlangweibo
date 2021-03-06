package com.jy.xinlangweibo.base;

import org.afinal.simplecache.ACache;

import com.jy.xinlangweibo.activity.MainActivity;
import com.jy.xinlangweibo.utils.Logger;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class BaseFragment extends Fragment {
	protected MainActivity activity;
	protected ACache mCache;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.showLog("得到缓存", "BaseFragment");
//		得到缓存
		mCache = ACache.get(this.getActivity());
		
		activity = (MainActivity)getActivity();
	}
	protected void intent2Activity(Class<? extends BaseActivity> tarActivity) {
		Intent intent = new Intent(activity,tarActivity);
		startActivity(intent);
	}
}
