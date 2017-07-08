package com.ogbongefriends.com.ogbonge.how_about_we;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.custom.ImageLoaderBlur;


public abstract class EventDetailActivityAdapter extends BaseAdapter {

	private static Context ctx;
	public int accessorImg;
	public int identifierImg;

	private ArrayList<HashMap<String, String>> data;
	// private ArrayList<Boolean> ownedStatus;
	private OnClickListener itemListener;
	private OnClickListener likeListener;
	private OnClickListener imageListner;
	private OnClickListener identifiereListner;
	private OnClickListener commentLisner;
	
	private DB db;
	HashMap<String, String> row;

	long diffSeconds;
	long diffMinutes;
	long diffHours;
	long diffDays;

	private long recId;
	private Calendar today;

	private ImageView identifier;

	private SharedPreferences pref;
	private Editor edit;
	private String imgUrl;
	public ImageLoaderBlur imageLoader;

	ArrayList<String> PhotoURLS;
	private OnItemClickListener glistener;
	private Activity activity;
	
	public EventDetailActivityAdapter(Context c,
			ArrayList<HashMap<String, String>> data, int img, int ideImg) {
		// TODO Auto-generated constructor stub
		this.ctx = c;
		this.data = data;
		// this.ownedStatus = ownedStatus;
		accessorImg = img;
		identifierImg = ideImg;
		db = new DB(c);
		pref = PreferenceManager.getDefaultSharedPreferences(c);
		edit = pref.edit();
		recId = pref.getLong("id", -1);
		imageLoader = new ImageLoaderBlur(ctx);
		activity = (Activity) ctx;

		glistener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.v("inside adapter", arg0 + "===" + arg1 + "====" + arg2
						+ "====" + arg3);
				glistenerforView(arg0, arg2, arg0.getTag());
			}
		};

		itemListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, (Integer) v.getTag());
			}
		};

		commentLisner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentLisner(v, v.getTag());
			}
		};

		likeListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				likeListener(v, v.getTag(R.string.first_tag),
//						v.getTag(R.string.Second_tag),
//						v.getTag(R.string.Third_tag));
//				updatelike(Integer.parseInt(v.getTag(R.string.first_tag) + ""));

			}
		};

		identifiereListner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				identifiereListner(v, v.getTag());
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
//			convertView = LayoutInflater.from(ctx).inflate(
//					R.layout.user_list_item, parent, false);

		}
		final int pos = position;
		if (pos % 2 == 1) {
			convertView.setBackgroundColor(Color.parseColor("#f7f7f7"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		convertView.setTag(position);

		row = data.get(position);
		PhotoURLS = new ArrayList();
		Log.v("view log", position + "");
	
		// ----------------------------------------------------------
		identifier = (ImageView) convertView.findViewById(R.id.imageView1);

		String rowImage = row.get(DB.Table.user_master.profile_pic.toString());
		String rowUserID = row.get("user_id");
	//	String rowFeedID = row.get(Table.event_feed_master.id.toString());

		
		// ===message button======
		Button msgBtn = (Button) convertView.findViewById(R.id.button2);
		msgBtn.setVisibility(View.GONE);

		// ===message button======
		Button commentBtn = (Button) convertView.findViewById(R.id.button3);
		commentBtn.setOnClickListener(commentLisner);
	//	commentBtn.setTag(rowFeedID);
		commentBtn.setVisibility(View.GONE);
		
		
		try {
			String fbID = row.get(DB.Table.user_master.fb_user_id.toString());
			if (rowImage.length() > 5) {

				imageLoader.DisplayImage(
						ctx.getString(R.string.profile_image_url) + rowImage,
						identifier);

			} else {
				if (fbID.length() > 0) {

					//identifier.setTag(R.string.image_tag, fbID);
//					new Utils.LongOperation(identifier, ctx)
//							.execute();
//					
//					Bitmap btm = Utils.getImageFromFB(ctx, fbID);
//
//					identifier.setImageBitmap(btm);
				} else {
					identifier.setImageResource(R.drawable.ic_launcher);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			identifier.setImageResource(R.drawable.ic_launcher);
		}
		//
		// // --------------------------------------
		identifier.setOnClickListener(identifiereListner);
		identifier.setTag(rowUserID);
		// -----------------------------------
		TextView index = (TextView) convertView.findViewById(R.id.textView2);

//		try {
//			String timeDiff = Utils.datedifrence(Long
//					.parseLong(row.get(Table.event_feed_master.add_date
//							.toString())));
//			index.setText(timeDiff);
//			index.setTextColor(Color.BLACK);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// -----------------------------------
		TextView username = (TextView) convertView.findViewById(R.id.textView6);
		String fname = row.get(DB.Table.user_master.first_name.toString());
		username.setText(fname + " : ");
		username.setTextColor(Color.BLUE);
		// -----------------------------------
	//	TextView tv = (TextView) convertView.findViewById(R.id.tv_string_data);

//		try {
//			if (row.get(Table.event_feed_master.post_comment.toString())
//					.contains("@")
//					|| row.get(Table.event_feed_master.post_comment.toString())
//							.contains("#")) {
//				tv.setMovementMethod(LinkMovementMethod.getInstance());
//				tv.setText(Utils.addClickablePart(row
//						.get(Table.event_feed_master.post_comment.toString()),
//						ctx), BufferType.SPANNABLE);
//
//			} else {
//				tv.setText(row.get(Table.event_feed_master.post_comment
//						.toString()));// data.get(position));
//			}
//		} catch (Exception e) {
//
//		}

		// -----------------------------------
		TextView like = (TextView) convertView.findViewById(R.id.textView3);
		like.setText(row.get(Constants.LIKE_COUNT));// data.get(position));
		like.setTextColor(Color.BLACK);

		// -----------------------------------
//
//		Gallery gallery = (Gallery) convertView
//				.findViewById(R.id.feed_image_gallery);

		try {
			String imgResouce = row.get(Constants.InnerMap);
			if (imgResouce.length() > 0) {
				String[] CurrentImage;
				CurrentImage = imgResouce.split(Pattern.quote("/"));

//				for (int i = 0; i < CurrentImage.length; i++) {
//					PhotoURLS.add(ctx.getString(R.string.feed_image_url)
//							+ CurrentImage[i]);
//
//				}

//				imageAdapter = new GalleryAdapter(ctx, PhotoURLS);
//				imageAdapter.notifyDataSetChanged();
//				gallery.setAdapter(imageAdapter);
//				gallery.setSpacing(10);
//				gallery.setVisibility(gallery.VISIBLE);
//				gallery.setTag(position);

			} else {
			//	gallery.setVisibility(gallery.GONE);
			}

		} catch (Exception e) {

		//	gallery.setVisibility(gallery.GONE);
		}

	//	gallery.setOnItemClickListener(glistener);

		TextView likeStaus = (TextView) convertView.findViewById(R.id.button1);
		// likeStaus.setTextColor(Color.BLACK);
		likeStaus.setOnClickListener(likeListener);
		String status = row.get(Constants.LIKE_STATUS);
		likeStaus.setText((status.equals("0") ? "Like" : "Unlike"));
//		likeStaus.setTag(R.string.first_tag, position);
//		likeStaus.setTag(R.string.Second_tag, likeStaus.getText().toString());
//		likeStaus.setTag(R.string.Third_tag, rowFeedID);

		convertView.setOnClickListener(itemListener);

		return convertView;
	}

	// ===========================================

	
	
	//============================================
	
	public static String datedifrence(Date d1) {

		String timedif = "";

		try {

			Date d2 = Calendar.getInstance().getTime();

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				if (diffDays > 1) {
					timedif = String.valueOf(diffDays) + " days ago";
				} else {
					timedif = String.valueOf(diffDays) + " day ago";
				}

			} else {
				if (diffHours > 0) {
					if (diffHours > 1) {
						timedif = String.valueOf(diffHours) + " hours ago";
					} else {
						timedif = String.valueOf(diffHours) + " hour ago";
					}

				} else {
					if (diffMinutes > 0) {
						if (diffMinutes > 1) {
							timedif = String.valueOf(diffMinutes)
									+ " minutes ago";
						} else {
							timedif = String.valueOf(diffMinutes)
									+ " minute ago";
						}

					} else {
						if (diffSeconds > 1) {
							timedif = String.valueOf(diffSeconds)
									+ " seconds ago";
						} else {
							timedif = String.valueOf(diffSeconds)
									+ " second ago";
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return timedif;
	}

	private void updatelike(int index) {

		HashMap<String, String> row = data.get(index);

		int likeCount = Integer.parseInt(row.get(Constants.LIKE_COUNT));
		String likeStatus = row.get(Constants.LIKE_STATUS);

		if (likeStatus.equals("0")) {

			likeCount++;

		} else {
			likeCount--;

		}

		row.put(Constants.LIKE_COUNT, String.valueOf(likeCount));
		row.put(Constants.LIKE_STATUS, likeStatus.equals("0") ? "1" : "0");

		notifyDataSetChanged();
	}

	protected void glistenerforView(AdapterView<?> arg0, int position,
			Object object) {
		// TODO Auto-generated method stub
		Log.v("inside adapter", arg0.toString() + "====" + object.toString());
		try {
			String imgResouce = data.get(Integer.parseInt(object.toString()))
					.get(Constants.InnerMap);
			if (imgResouce.length() > 0) {
				String[] CurrentImage;
				CurrentImage = imgResouce.split(Pattern.quote("/"));
				PhotoURLS.clear();
//				for (int i = 0; i < CurrentImage.length; i++) {
//					PhotoURLS.add(ctx.getString(R.string.feed_image_url)
//							+ CurrentImage[i]);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Utils.showImage(activity, PhotoURLS, position);

	}

	protected abstract void onItemClick(View v, int position);

	protected abstract void identifiereListner(View v, Object object);

	protected abstract void likeListener(View v, Object object, Object object1,
			Object object2);
	protected abstract void commentLisner(View v, Object object);

	// ====================================================

}
