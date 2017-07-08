package com.ogbongefriends.com;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.ogbongefriends.com.DB.DB.Table;


public abstract class showFriendListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	Context context;
	@SuppressWarnings("unused")
	private static LayoutInflater inflater = null;

	public ImageDownloader imageLoader;
	public int count;
	HashMap<String, String> row;
	private OnClickListener detailPageListener;

	public showFriendListAdapter(Context context,
			ArrayList<HashMap<String, String>> placeData) {
		Log.d("Data In Place data", "" + placeData);
		this.context = context;

		this.data = placeData;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader = new ImageLoaderConfiguration.

		detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, String.valueOf(v.getTag()));
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
			long diffMonth = diff / (30L * 24L * 60L * 60L * 1000L) % 365;
			long diffYear = diff / (365L * 30L * 24L * 60L * 60L * 1000L);

			if (diffYear > 0) {
				if (diffYear > 1) {
					timedif = String.valueOf(diffYear) + " years ago";
				} else {
					timedif = String.valueOf(diffYear) + " year ago";
				}

			} else {
				if (diffMonth > 0) {
					if (diffMonth > 1) {
						timedif = String.valueOf(diffMonth) + " months ago";
					} else {
						timedif = String.valueOf(diffMonth) + " month ago";
					}

				} else {
					if (diffDays > 0) {
						if (diffDays > 1) {
							timedif = String.valueOf(diffDays) + " days ago";
						} else {
							timedif = String.valueOf(diffDays) + " day ago";
						}

					} else {
						if (diffHours > 0) {
							if (diffHours > 1) {
								timedif = String.valueOf(diffHours)
										+ " hours ago";
							} else {
								timedif = String.valueOf(diffHours)
										+ " hour ago";
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
									if (diffSeconds > 0) {
										timedif = String.valueOf(diffSeconds)
												+ " second ago";
									} else {
										timedif = "0 second ago";
									}

								}

							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timedif;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View vi = convertView;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			// vi = inflater.inflate(R.layout.place_list_item, null);
			vi = inflater.inflate(R.layout.messagestrip, null);
		}

		row = data.get(position);
		try {
			TextView ReceiverName = (TextView) vi
					.findViewById(R.id.msgreceiverName);
			TextView msgText = (TextView) vi.findViewById(R.id.msgText);
			TextView sendtime = (TextView) vi.findViewById(R.id.msgSendTime);
			sendtime.setTextColor(Color.BLACK);

			ImageView receiverImage = (ImageView) vi
					.findViewById(R.id.msgReceiverImage);

			ReceiverName.setText(row.get("first_name")+" "+row.get("last_name"));
			ReceiverName.setTextColor(Color.BLACK);

			msgText.setText(row.get(Table.user_master.handle_description.toString()));
			msgText.setTextColor(Color.BLACK);

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
//
//				Long secondss = Long.parseLong(row
//						.get(Table.message_master.send_time.toString()));
//
//				String dateString = format.format(new Date(secondss));
//
//				String timeDiff = datedifrence(Utils.StringToDate(
//						Constants.kTimeStampFormat, dateString));
//
//				sendtime.setText(timeDiff);

			} catch (Exception e) {
				e.printStackTrace();
			}

			String rowImage = row.get(Table.user_master.profile_pic.toString());
			/*String fbID = row.get(Table.uy_users.fb_user_id.toString());*/

			try {
//				if (rowImage.length() > 5) {
//
//					imageLoader.DisplayImage(
//							context.getString(R.string.profile_image_url)
//									+ rowImage, receiverImage);
//
//				} 
			} catch (Exception e) {
				e.printStackTrace();
				receiverImage.setImageResource(R.drawable.ic_launcher);

			}

//			try {
//				if (row.get(Table.message_master.sender_id.toString())
//						.equalsIgnoreCase(
//								String.valueOf(Utils.getCurrentUserID(context)))) {
//					vi.setTag(row.get(Table.message_master.recipient_id
//							.toString()));
//				} else {
//					vi.setTag(row.get(Table.message_master.sender_id.toString()));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			vi.setOnClickListener(detailPageListener);
		} catch (Exception e) {

		}
		return vi;
	}

	protected abstract void onItemClick(View v, String string);
}
