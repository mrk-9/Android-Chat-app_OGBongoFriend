package com.ogbongefriends.com.ogbonge.backstage_album;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.common.Preferences;

public abstract class PhotoCommentAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	Context context;
	HashMap<String, String> row;
	private OnClickListener detailPageListener;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Preferences fref;
	

	public PhotoCommentAdapter(Context context,
			ArrayList<HashMap<String, String>> placeData) {
		this.context = context;
		this.data = placeData;
		fref=new Preferences(context);
		 File cacheDir = StorageUtils.getCacheDirectory(context);
	     options = new DisplayImageOptions.Builder()
	        .resetViewBeforeLoading(false)  // default
	        .delayBeforeLoading(1000)
	        .cacheInMemory(true) // default
	        .cacheOnDisk(true) // default
	        .considerExifParams(false) // default
	        .build();
	  
	  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				
				
				
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(26) // default
		        .diskCacheSize(1000 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .imageDownloader(new BaseImageDownloader(context)) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(options) // default
		        .writeDebugLogs()
		        .build();
				
			
	 
				
				ImageLoader.getInstance().init(config);
					
				 imageLoader = ImageLoader.getInstance();
		
		
		

		detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.d("arv", ""+String.valueOf(v.getTag()));
				Log.d("arv", ""+String.valueOf(v.getTag(R.id.comment)));
				
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())),Integer.parseInt(String.valueOf(v.getTag(R.id.comment))));
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
				view = inflater.inflate(R.layout.photo_comment_row, parent, false);
			}
			
			
		

		if (pos % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#f7f7f7"));
		} else {
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		Log.d("In EventAdapter", "" + data);
		row = data.get(index);
	
		ImageView User_Img=(ImageView)view.findViewById(R.id.userImage);
		TextView userName=(TextView)view.findViewById(R.id.userName);
		TextView comment=(TextView)view.findViewById(R.id.comment);
		TextView last_seen=(TextView)view.findViewById(R.id.last_seen);
		
	final ProgressBar pb=(ProgressBar)view.findViewById(R.id.userImageProgress);
		
		userName.setText(row.get(Table.user_master.first_name.toString()));
		comment.setText(row.get(Table.photo_comment.comment.toString()));
		if(row.get(Table.user_master.last_seen.toString()).length()>1){
		last_seen.setText(row.get(Table.user_master.last_seen.toString()));
		}


		imageLoader.displayImage(row.get(Table.user_master.profile_pic.toString()), User_Img, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
			//	pb.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
			//	pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
			//	pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
			//	pb.setVisibility(View.GONE);
			}
		});
		
		
		return view;
	}

	// =============================================


	// =============================================

	protected abstract void onItemClick(View v, int event_id,int comment_id );
}