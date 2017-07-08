package com.ogbongefriends.com.ogbonge.profile;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public abstract class FullProfileBackStageAdapter extends BaseAdapter {
	
	// ******* DECLARING VARIABLES *******
		
		//int count;
		Context context;
		
	// ******* DECLARING IMAGE LOADER ******
		
		public ImageLoader imageLoader;
		private DisplayImageOptions options;
		
	// ******* DECLARING HASH MAP *******
		
		String row;
		
	// ******* DECLARING ARRAYLIST *******
		
		ArrayList <PhotoOfYouVO> data;
		ArrayList <Integer> _photo_type;

	// ******* DECLARING LAYOUT INFLATER ******

		LayoutInflater inflater = null;

	// ******* DECLARING CLICK LISTENER ******
		
		public OnClickListener itemClickListener;
		

		public FullProfileBackStageAdapter(Context context, ArrayList <PhotoOfYouVO> placeData,ArrayList <Integer> photo_type) {
			
			Log.d("Data In Place data", "" + placeData);
			
			this.context = context;
			this.data = placeData;
			this._photo_type=photo_type;
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
			//imageLoader = new ImageLoader(this.context);	
			
			itemClickListener = new OnClickListener() {
			
				@Override
				public void onClick(View v) {
			
					onItemClick(v, String.valueOf(v.getTag()));
				}
			};
		}
		
		@Override
		public View getView(int position, View convertView, final ViewGroup parent) {
			
			 ImageView imageView = new ImageView(context);
			 imageView.setLayoutParams(new Gallery.LayoutParams(200, android.widget.Gallery.LayoutParams.FILL_PARENT));
			 imageView.setScaleType(ScaleType.FIT_XY);
			   final ProgressBar p=new ProgressBar(context);
			  // imageView.setLayoutParams(new Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT,android.widget.Gallery.LayoutParams.FILL_PARENT));
			
			   row=data.get(position).getfullPhotoUrl();
					try {
						if(full_users_profile.backstage_purchase_status==0){
				if (_photo_type.get(position)==2) {
					String img_path = row;
					imageView.setImageResource(R.drawable.lock_backstage);
				}
				
				else {

					if(_photo_type.get(position)==1){
				
					//	 Picasso.with(context).load(row).into(imageView);



						Glide.with(context).load(row).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(imageView);


					/*imageLoader.displayImage(row, imageView, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
					}
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
						}
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						}
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});*/
					}
				}
						}
						else{
							
							//Picasso.with(context).load(row).networkPolicy(NetworkPolicy.OFFLINE).into(imageView);
							Glide.with(context).load(row).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(imageView);
//							imageLoader.displayImage(row, imageView, new ImageLoadingListener() {
////								
//								@Override
//								public void onLoadingStarted(String arg0, View arg1) {
////									// TODO Auto-generated method stub
////									
//							}
////							
//								@Override
//								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//									// TODO Auto-generated method stub
////									
//								}
////								
//								@Override
//								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
////									// TODO Auto-generated method stub
////									
//								}
////								
//								@Override
//								public void onLoadingCancelled(String arg0, View arg1) {
////									// TODO Auto-generated method stub
////									
//								}
//							});
						}
			} 
			catch (Exception e) {
				e.printStackTrace();
				imageView.setImageResource(R.drawable.profile);
			}
					//imageView.setOnClickListener(itemClickListener);
					imageView.setTag(position);
					
		return imageView;
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

