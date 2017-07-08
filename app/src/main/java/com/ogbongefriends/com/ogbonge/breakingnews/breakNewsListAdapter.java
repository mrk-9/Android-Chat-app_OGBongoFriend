package com.ogbongefriends.com.ogbonge.breakingnews;

import java.util.ArrayList;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ogbongefriends.com.R;

import com.ogbongefriends.com.common.Preferences;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ogbongefriends.com.DB.DB;

public abstract class breakNewsListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	Context context;
	HashMap<String, String> row;
	private OnClickListener detailPageListener;
	public Preferences pref;

	public breakNewsListAdapter(Context context,
			ArrayList<HashMap<String, String>> placeData) {
		this.context = context;
		this.data = placeData;
		pref=new Preferences(context);
		
		detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())),String.valueOf(v.getTag(R.string.postEnquiry)));
			}
		};

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View view, final ViewGroup parent) {

		Holder holder;
		
		final int pos = index;

		
		
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.photo_comment_row, parent, false);
			holder = new Holder();

			holder.banner=(ImageView)view.findViewById(R.id.banner);
			holder.User_Img=(ImageView)view.findViewById(R.id.userImage);
			holder.userName=(TextView)view.findViewById(R.id.userName);
			holder.comment=(TextView)view.findViewById(R.id.comment);
			holder.last_seen=(TextView)view.findViewById(R.id.last_seen);
			holder.add=(AdView)view.findViewById(R.id.adView);
			holder.rl=(RelativeLayout)view.findViewById(R.id.relativeLayout1);
			holder.parent_relativelayout=(RelativeLayout)view.findViewById(R.id.parent_relativeLayout);
			view.setTag(holder);
			}
		else{
			holder=(Holder)view.getTag();
		}
			
		
		if (pos % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#f7f7f7"));
		} else {
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		Log.d("In EventAdapter", "" + data);
		row = data.get(index);
		holder.User_Img.setScaleType(ScaleType.FIT_XY);
		if(row.get("type").toString().equalsIgnoreCase("1")){
		holder.rl.setVisibility(View.GONE);
		if(breakingList.bannerUrls.size()>0){
			holder.banner.setVisibility(View.VISIBLE);
			holder.add.setVisibility(View.GONE);
			Glide.with(this.context).load(row.get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.01f).into(holder.banner);
			
		}
		else{
			holder.banner.setVisibility(View.GONE);
			holder.add.setVisibility(View.VISIBLE);
			 AdRequest adRequest = new AdRequest.Builder().build();
			holder.add.loadAd(adRequest);
		}
	}
	
	else{
		holder.add.setVisibility(View.GONE);
		holder.banner.setVisibility(View.GONE);
		holder.rl.setVisibility(View.VISIBLE);
		//comment.setText("PostedBy: "+row.get(Table.news_master.posted_by.toString()));
		holder.comment.setText(row.get(DB.Table.news_master.news_short_description.toString()));
		holder.last_seen.setText("Posted: "+row.get(DB.Table.news_master.add_date.toString())+" ago.");
		holder.userName.setText(row.get(DB.Table.news_master.news_title.toString()));
			holder.rl.setTag(row.get(DB.Table.event_master.id.toString()));
			holder.rl.setTag(R.string.postEnquiry,this.context.getString(R.string.urlString) + "userdata/news_media/" + row.get(DB.Table.news_media.news_image.toString()));
			holder.rl.setOnClickListener(detailPageListener);
		if(row.get(DB.Table.news_media.media_type.toString()).toString().equalsIgnoreCase("1"))
		{
			String img=this.context.getString(R.string.urlString)+"userdata/news_media/"+row.get(DB.Table.news_media.news_image.toString());
			Glide.with(this.context).load(this.context.getString(R.string.urlString) + "userdata/news_media/" + row.get(DB.Table.news_media.news_image.toString())).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.01f).into(holder.User_Img);
			
			}
			
			else{
			holder.User_Img.setImageResource(R.drawable.video_img);
			}

			holder.rl.setTag(row.get(DB.Table.news_master.id.toString()));
			holder.rl.setOnClickListener(detailPageListener);
	}

		return view;
	}

	public class Holder
	{
		ImageView banner,User_Img;
		TextView userName,comment,last_seen;
		AdView add;
		RelativeLayout rl,parent_relativelayout;
	}

	// =============================================

	private String getCatName(String cat_id) {
		// TODO Auto-generated method stub
		if (Integer.parseInt(cat_id) == 1) {
			return "Party";

		} else if (Integer.parseInt(cat_id) == 2) {
			return "Concert";
		} else {
			return "Exhibition";
		}

	}

	// =============================================

	protected abstract void onItemClick(View v, int position, String item);
}