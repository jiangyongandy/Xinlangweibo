package com.jy.xinlangweibo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.adapter.GridIvAdapter;
import com.jy.xinlangweibo.adapter.ParallaxRecyclerAdapter;
import com.jy.xinlangweibo.adapter.RecyleViewHolder;
import com.jy.xinlangweibo.adapter.ParallaxRecyclerAdapter.OnParallaxScroll;
import com.jy.xinlangweibo.adapter.UserInfoAdapter;
import com.jy.xinlangweibo.api.MyWeiboapi;
import com.jy.xinlangweibo.api.SimpleRequestlistener;
import com.jy.xinlangweibo.base.BaseActivity;
import com.jy.xinlangweibo.constant.AccessTokenKeeper;
import com.jy.xinlangweibo.constant.Constants;
import com.jy.xinlangweibo.constant.CustomConstant;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.StringUtils;
import com.jy.xinlangweibo.widget.CustomActBar;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

public class UserInfoActivity extends BaseActivity {

	private RecyclerView mRecyclerView;
	private CustomActBar actbar;
	private String screen_name;
	private UserInfoAdapter<Status> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screen_name = this.getIntent().getStringExtra("SCREEN_NAME");
		setContentView(R.layout.fragment_profile);
		init();
	}

	private void init() {
		initTitle();
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		MyWeiboapi myWeiboapi = new MyWeiboapi(this, Constants.APP_KEY,
				AccessTokenKeeper.readAccessToken(this));
		myWeiboapi.users_show(0, screen_name, new SimpleRequestlistener(this,
				null) {
			@Override
			public void onComplete(String arg0) {
				super.onComplete(arg0);
				adapter.setUser(User.parse(arg0));
				adapter.notifyDataSetChanged();
			}
		});
		createCardAdapter(mRecyclerView);
	}

	private void initTitle() {
		actbar = (CustomActBar) findViewById(R.id.profile_actbar);
	}

	private void createCardAdapter(RecyclerView recyclerView) {
		final ArrayList<Status> statuses = (ArrayList<Status>) mCache
				.getAsObject("STATUES");
		if (statuses == null) {
			return;
		}
		adapter = new UserInfoAdapter<Status>(statuses, this);

		adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
			@Override
			public void onClick(View v, int position) {
				Toast.makeText(UserInfoActivity.this,
						"You clicked '" + position + "'", Toast.LENGTH_SHORT)
						.show();
			}
		});

		recyclerView.setLayoutManager(new LinearLayoutManager(
				UserInfoActivity.this));
		View header = View.inflate(UserInfoActivity.this,
				R.layout.profile_head, null);
		adapter.setParallaxHeader(header, recyclerView);
		adapter.setData(statuses);
		adapter.setOnParallaxScroll(new OnParallaxScroll() {

			@Override
			public void onParallaxScroll(float percentage, float offset,
					View parallax) {
				Drawable c = actbar.getBackground();
				c.setAlpha(Math.round(percentage * 255));
				if (percentage > 0.5) {
					actbar.getLeftImage().setImageResource(
							R.drawable.navigationbar_back_highlighted);
					actbar.getRightImage().setImageResource(
							R.drawable.userinfo_navigationbar_more_highlighted);
					actbar.getTitle().setVisibility(View.VISIBLE);
				} else {
					actbar.getTitle().setVisibility(View.GONE);
					actbar.getLeftImage().setImageResource(
							R.drawable.redpacket_navigationbar_back);
					actbar.getRightImage().setImageResource(
							R.drawable.userinfo_buttonicon_more);
				}
			}
		});
		recyclerView.setAdapter(adapter);
	}
}
