package com.jy.xinlangweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.activity.StatusDetailsActivity;
import com.jy.xinlangweibo.constant.CustomConstant;
import com.jy.xinlangweibo.utils.DateUtils;
import com.jy.xinlangweibo.utils.ImageLoadeOptions;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.StringUtils;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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

public  class UserInfoAdapter<T> extends ParallaxRecyclerAdapter<T> {

	private Context context;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserInfoAdapter(List<T> data, Context context) {
		super(data);
		this.context = context;
	}

	@Override
	public void onBindViewHolderImpl(ViewHolder viewHolder,
			ParallaxRecyclerAdapter<T> adapter, int i) {
		RecyleViewHolder vh = (RecyleViewHolder) viewHolder;
		TextView statusName = vh.getView(R.id.tv_pubname);
		TextView sourceText = vh.getView(R.id.tv_from);
		ImageView headIv = vh.getView(R.id.iv_head);

		TextView statusText = vh.getView(R.id.tv_statuses_content);
		ImageView statusIv = vh.getView(R.id.iv_statuses_singlecontent);
		GridView statusGv = vh.getView(R.id.gv_statuses_contents);

		TextView bottomretweetedText = vh
				.getView(R.id.tv_statuses_bottom_reweet);
		TextView bottomcommentText = vh
				.getView(R.id.tv_statuses_bottom_comment);
		TextView bottomunlikeText = vh.getView(R.id.tv_statuses_bottom_unlike);
		// 点赞的特殊处理
		final View ll_status_unlike = vh.getView(R.id.ll_status_unlike);
		final ImageView status_unlikebtn = vh.getView(R.id.status_unlikebtn);
		ll_status_unlike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				status_unlikebtn
						.setImageResource(R.drawable.timeline_icon_like);
				status_unlikebtn.startAnimation(AnimationUtils.loadAnimation(
						context, R.anim.scale_unlike));
			}
		});

		// bind data
		final Status status = (Status) adapter.getData().get(i);
		User user = status.user;

		View itemStatus = vh.getView(R.id.ll_item_status);
		itemStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, StatusDetailsActivity.class);
				intent.putExtra("Status", status);
				context.startActivity(intent);
			}
		});

		imageLoader.displayImage(user.avatar_hd, headIv,
				ImageLoadeOptions.getIvHeadOption());
		String from = DateUtils.getDate(status.created_at) + " 来自  "
				+ Html.fromHtml(status.source);
		sourceText.setText(StringUtils.get2KeyText(context, from, sourceText));
		statusName.setText(user.screen_name);

		statusText.setText(StringUtils.getKeyText(context, status.text,
				statusText));
		setImage(status, statusIv, statusGv);

		bottomretweetedText
				.setText((CharSequence) (status.reposts_count > 0 ? ""
						+ status.reposts_count : "转发"));
		bottomcommentText
				.setText((CharSequence) (status.comments_count > 0 ? ""
						+ status.comments_count : "评论"));
		bottomunlikeText
				.setText((CharSequence) (status.attitudes_count > 0 ? ""
						+ status.attitudes_count : "赞"));

		if (status.retweeted_status != null) {
			LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
			TextView retweetedText = vh.getView(R.id.tv_retweeted_content);
			ImageView retweetedIv = vh.getView(R.id.iv_retweeted_singlecontent);
			GridView retweetedGv = vh.getView(R.id.gv_retweeted_contents);

			retweeted.setVisibility(View.VISIBLE);
			String retweetedname = "作者已删除该微博";
			// 这里要判断 转发的微博作者是否为空 即转发的微博是否被作者删除。
			if (status.retweeted_status.user != null) {
				if (status.retweeted_status.user.screen_name != null) {
					retweetedname = status.retweeted_status.user.screen_name;
				}
			}
			String tempString = "@" + retweetedname + ":"
					+ status.retweeted_status.text;
			retweetedText.setText(StringUtils.getKeyText(context, tempString,
					retweetedText));
			setImage(status.retweeted_status, retweetedIv, retweetedGv);
		} else {
			LinearLayout retweeted = vh.getView(R.id.ll_retweeted);
			retweeted.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBindHeadViewHolder(ViewHolder viewHolder,
			ParallaxRecyclerAdapter<T> adapter) {
		Logger.showLog("绑定headview数据", "onBindHeadViewHolder");
		if(user == null)  {
			Logger.showLog("user为空。。。。。", "onBindHeadViewHolder");
			return;
		}
		ParallaxRecyclerAdapter.HeadViewHolder holder = (ParallaxRecyclerAdapter.HeadViewHolder) viewHolder;
		ImageView iv_userhead = (ImageView) holder.getView(R.id.iv_userhead);
		TextView tv_mentions = (TextView) holder.getView(R.id.tv_mentions);
		TextView tv_follows = (TextView) holder.getView(R.id.tv_follows);
		TextView tv_description = (TextView) holder.getView(R.id.tv_description);
		Logger.showLog("关注数"+user.friends_count+"粉丝数"+user.followers_count+"描述"+user.description , "onBindHeadViewHolder");
		imageLoader.displayImage(user.avatar_hd, iv_userhead,
				ImageLoadeOptions.getIvHeadOption());
		tv_mentions.setText("关注数:  "+user.friends_count);
		tv_follows.setText("粉丝数:  "+user.followers_count);
		tv_description.setText(user.description);
	}

	@Override
	public ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup,
			ParallaxRecyclerAdapter<T> adapter, int i) {
		return RecyleViewHolder.getViewHolder(((Activity) context)
				.getLayoutInflater().inflate(R.layout.item_card_profile,
						viewGroup, false));
	}

	@Override
	public int getItemCountImpl(ParallaxRecyclerAdapter<T> adapter) {
		return mData.size();
	}

	private void setImage(Status status, ImageView iv, GridView gv) {
		ArrayList<Object> pic_ids = status.pic_urls;
		String thumbnail_pic = status.original_pic;
		// 多图处理
		if (pic_ids != null && pic_ids.size() > 1) {
			iv.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			gv.setAdapter(new GridIvAdapter(pic_ids));
		}
		// 单图处理 此处判断的thumbnail_pic为空字符串，并非空引用！所以不能使用thumbnail_pic！=null来判断
		else if (!TextUtils.isEmpty(thumbnail_pic)) {
			gv.setVisibility(View.GONE);
			iv.setVisibility(View.VISIBLE);
			imageLoader.displayImage(thumbnail_pic, iv, ImageLoadeOptions
					.getCommonIvOption(CustomConstant.getContext()));
		}
		// 没图处理
		else {
			iv.setVisibility(View.GONE);
			gv.setVisibility(View.GONE);

		}
	}
}
