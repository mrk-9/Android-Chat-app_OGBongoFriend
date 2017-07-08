package com.ogbongefriends.com.ogbonge.how_about_we;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.R;

import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.ImageLoaderBlur;
import com.ogbongefriends.com.DB.DB;


public abstract class EventAtendeeAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	Context context;
	HashMap<String, String> row;
	private OnClickListener detailPageListener;
	private OnClickListener atendeeListner;
	private OnClickListener identifiereListner;
	// private Button atendeeImage;
	public ImageLoaderBlur imageLoader;
	private OnClickListener FollowBtnListener;

	public EventAtendeeAdapter(Context context,
			ArrayList<HashMap<String, String>> placeData) {
		this.context = context;
		this.data = placeData;
		imageLoader = new ImageLoaderBlur(context);

		identifiereListner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				identifiereListner(v, v.getTag());
			}
		};

		detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
			}
		};

		atendeeListner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				atendeeListner(v, v.getTag());
			}
		};
		FollowBtnListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				onFollowBtnClick(v,
//						String.valueOf(v.getTag(R.string.first_tag)),
//						String.valueOf(v.getTag(R.string.Second_tag)));
//				if (v.getTag(R.string.Second_tag).equals("0")) {
//					onFollowClick(v);
//				}
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

		final int pos = index;

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.place_followers_list_item, parent,
					false);
		}

		if (pos % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#f7f7f7"));
		} else {
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		row = data.get(index);

		String f_name = row.get(DB.Table.user_master.first_name.toString());
		String l_name = row.get(DB.Table.user_master.last_name.toString());

		TextView followerName = (TextView) view.findViewById(R.id.textView1);
		followerName.setText(f_name + " " + l_name);
		followerName.setTextColor(Color.BLUE);

		TextView txtEventorganiser = (TextView) view
				.findViewById(R.id.textView2);
		txtEventorganiser.setText(row.get(DB.Table.user_master.city.toString()));

		String userId = row.get(DB.Table.user_master.id.toString());
		/*
		 * atendeeImage = (Button) view.findViewById(R.id.imageView2);
		 * atendeeImage.setOnClickListener(atendeeListner);
		 * atendeeImage.setTag(userId);
		 */

		Button followbtn = (Button) view.findViewById(R.id.imageView2);
		String folowStatus = row.get(DB.Table.event_attendee_master.follow_status
				.toString());

//		followbtn.setTag(R.string.first_tag,
//				row.get(Table.user_master.id.toString()));
//		followbtn.setTag(R.string.Second_tag, folowStatus);
//
//		followbtn.setTag(R.string.Third_tag,
//				row.get(Table.event_attendee_master.event_id.toString()));

		followbtn.setOnClickListener(FollowBtnListener);

		if (folowStatus != null && !folowStatus.equals("")
				) {
			if (folowStatus.equals("1")
					&& !row.get(DB.Table.user_master.id.toString())
							.equalsIgnoreCase(
									String.valueOf(Utils
											.getCurrentUserID(context)))) {
//				followbtn
//						.setBackgroundResource(R.drawable.followers_already_added);
				followbtn.setVisibility(View.VISIBLE);

			} else if (folowStatus.equals("0")
					&& !row.get(DB.Table.user_master.id.toString())
							.equalsIgnoreCase(
									String.valueOf(Utils
											.getCurrentUserID(context)))) {
//				followbtn
//						.setBackgroundResource(R.drawable.followers_want_to_add);
				followbtn.setVisibility(View.VISIBLE);

			} else {
				followbtn.setVisibility(View.GONE);
			}
		} else {
			followbtn.setVisibility(View.GONE);
		}

		ImageView identifier = (ImageView) view.findViewById(R.id.imageView1);
		//String rowImage = row.get(Table.user_master.image.toString());
		try {

			String fbID = row.get(DB.Table.user_master.fb_user_id.toString());
			String res = null;
//			if (rowImage.length() > 5) {
//
//				imageLoader.DisplayImage(
//						context.getString(R.string.profile_image_url)
//								+ rowImage, identifier);
//
//				// new ThumbLoadingTask(identifier, ctx).execute(rowImage);
//
//			} else {
//				if (fbID.length() > 0) {
//
//				
//					// Bitmap btm = Utils.getImageFromFB(context, fbID);
//					//
//					// imageView.setImageBitmap(btm);
//				} else {
//					identifier.setImageResource(R.drawable.ic_launcher);
//				}
//
//			}
		} catch (Exception e) {
			e.printStackTrace();
			identifier.setImageResource(R.drawable.ic_launcher);
		}

		identifier.setOnClickListener(identifiereListner);
		identifier.setTag(userId);

		return view;
	}

	// ====================

	protected void onFollowClick(View v) {}

	// ====================

	protected abstract void onItemClick(View v, int position);

	protected abstract void atendeeListner(View v, Object object);

	protected abstract void identifiereListner(View v, Object object);

	protected abstract void onFollowBtnClick(View v, String string,
			String string2);

}