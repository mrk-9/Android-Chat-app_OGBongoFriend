package com.ogbongefriends.com.ogbonge.photos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.R;

public abstract class fb_album_adapter extends BaseAdapter {
	
	// ******* DECLARING VARIABLES *******
		
		//int count;
		Context context;
		
	// ******* DECLARING IMAGE LOADER ******
		
		public ImageLoader imageLoader;
		private DisplayImageOptions options;
		
	// ******* DECLARING HASH MAP *******
		
		HashMap<String, String>row;
		
	// ******* DECLARING ARRAYLIST *******
		
		private ArrayList<HashMap<String, String>> data;

	// ******* DECLARING LAYOUT INFLATER ******

		LayoutInflater inflater = null;

	// ******* DECLARING CLICK LISTENER ******
		
		public OnClickListener itemClickListener;
		

		public fb_album_adapter(Context context, ArrayList<HashMap<String, String>> placeData) {
			
			Log.d("Data In Place data", "" + placeData);
			
			this.context = context;
			this.data = placeData;
			row=new HashMap<String, String>();
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
			
			Log.d("this.data.......", "" + this.data);
			
			inflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			itemClickListener = new OnClickListener() {
			
				@Override
				public void onClick(View v) {
			
					onItemClick(v, String.valueOf(v.getTag()));
				}
			};
		}
		
		@Override
		public View getView(int index, View view, final ViewGroup parent) {

			final int pos = index;

			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fb_album_item, parent, false);
			}
			
			row = data.get(index);
ImageView img=(ImageView)view.findViewById(R.id.imageView1);
TextView name=(TextView)view.findViewById(R.id.name);
TextView count=(TextView)view.findViewById(R.id.count);
img.setScaleType(ScaleType.FIT_XY);
name.setText(row.get("name"));
count.setText(row.get("count"));
imageLoader.displayImage(row.get("url")+""+row.get("cover_photo")+"/picture?type=album&access_token="+row.get("outh"), img, new ImageLoadingListener() {
	
	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}
});
			
view.setTag(row.get("id"));
view.setOnClickListener(itemClickListener);
			
			return view;
		}
		
		@Override
		public int getCount() {

			return data.size();
		}

		@Override
		public Object getItem(int position) {

			return data.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		protected abstract void onItemClick(View v, String string);
	}


