package com.jy.xinlangweibo.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;

//单例设计模式
public class FragmentController {
	private static FragmentController controller;
	private FragmentManager fm;
	private int containerId;
	private Fragment[] fragments;
	

	private FragmentController(Activity fa,int id,Fragment[] fragments) {
		fm = fa.getFragmentManager();
		this.containerId = id;
		this.fragments = fragments;
		initView();
	}

	private void initView() {
		FragmentTransaction ft;
		ft = fm.beginTransaction();
		if (fragments == null) {
			System.out.println("fragments 空");
		} else {
			for (int i = 0;i<4;i++) {
					switch (i) {
					case 0:
//						fragments.set(i, new HomeFragment() );
						if (!(fragments[0] instanceof HomeFragment)) {
							fragments[0] = new HomeFragment();
							ft.add(containerId, fragments[0]);
						}
						break;
					case 1:
						if (!(fragments[1] instanceof MessageFragment)) {
							fragments[1] = new MessageFragment();
							ft.add(containerId, fragments[1]);
						}
						break;
					case 2:
						if (!(fragments[2] instanceof DiscoverFragment)) {
							fragments[2] = new DiscoverFragment();
							ft.add(containerId, fragments[2]);
						}
						break;
					case 3:
						if (!(fragments[3] instanceof ProfileFragment)) {
							fragments[3] = new ProfileFragment();
							ft.add(containerId, fragments[3]);
						}
						break;
						
					default:
						break;
					}
			}
		}

//		for (Fragment fg: fragments) {
//			ft.add(containerId, fg);
//		}
		ft.commit();
	}
	public static void onDestroy() {
		controller = null;
	}
	public static FragmentController getInstance(Activity fa, int containerid,Fragment[] fragments) {
		if (controller == null) {
			controller = new FragmentController(fa,containerid,fragments);
		}
		return controller;
	}

	public void hide() {
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fg:fragments) {
			if(ft != null) {
			ft.hide(fg);
			}
		}
		ft.commit();
	}
	public void show (int position) {
		hide();
		FragmentTransaction ft = fm.beginTransaction();
//		Fragment fg = fragments.get(position);
		Fragment fg = fragments[position];
		ft.show(fg);
		ft.commit();
	}
}
