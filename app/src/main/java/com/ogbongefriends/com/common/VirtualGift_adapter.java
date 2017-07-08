package com.ogbongefriends.com.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.R;

import com.ogbongefriends.com.DB.DB;

public abstract class VirtualGift_adapter extends BaseAdapter{
 
    String [] result;
    Context context;
   // ArrayList<HashMap<String, String>> _url;
 int [] imageId;
 private ImageLoader imageLoader;
 private ArrayList<HashMap<String, String>> _data;
 private OnClickListener detailPageListener;
 private OnLongClickListener longClicked;
 private DisplayImageOptions options;
      private static LayoutInflater inflater=null;
    public VirtualGift_adapter(Context mainActivity, ArrayList<HashMap<String, String>> data) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        _data=data;
        detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, String.valueOf(v.getTag()));
			}
		};
		
		longClicked=new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				onItemLongClick(v, String.valueOf(v.getTag()));
				return true;
			}
		};
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
         
         File cacheDir = StorageUtils.getCacheDirectory(context);
 				
         options = new DisplayImageOptions.Builder()
	     .resetViewBeforeLoading(false)  // default
	     .delayBeforeLoading(1000)
	     .displayer(new RoundedBitmapDisplayer(150))
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
 
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public class Holder
    {
        TextView tv,gift_name;
        ImageView img;
        
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
      final   Holder holder=new Holder();
        HashMap<String, String> row = _data.get(position);
        View rowView;
 String path;
             rowView = inflater.inflate(R.layout.gift_row, null);
             holder.img=(ImageView) rowView.findViewById(R.id.gift_image);
             holder.tv=(TextView)rowView.findViewById(R.id.gift_cost);
             holder.gift_name=(TextView)rowView.findViewById(R.id.gift_name);
             final ProgressBar pb=(ProgressBar)rowView.findViewById(R.id.progressBar1);
             holder.img.setScaleType(ScaleType.FIT_XY);
            
        if(row.get("type").toString().equalsIgnoreCase("0")){
        	pb.setVisibility(View.GONE);
        	holder.img.setBackgroundResource(R.drawable.coin_point);
        	holder.gift_name.setText("Give points as Gift");
        	holder.tv.setText("");
        	 rowView.setTag("-900");
        }
        else{
        	
        	pb.setVisibility(View.VISIBLE);
        	path=row.get(DB.Table.gift_master.gift_image.toString());
        	 holder.tv.setText(row.get(DB.Table.gift_master.cost.toString())+" points");
        	 try
        	 {
        	 holder.gift_name.setText(row.get(DB.Table.gift_master.gift_title.toString()));
        	 }
        	 catch(Exception e)
        	 {
        	 
        	 }
        	 
        	 //rowView.setTag(row.get(Table.user_master.uuid.toString()));
        	 pb.setVisibility(View.GONE);
        	 rowView.setTag(row.get(DB.Table.gift_master.id.toString()));
        	 if(path.length()>3){


				 Glide.with(context).load(path).asBitmap().centerCrop().placeholder(R.drawable.gift).thumbnail(0.01f).into(new BitmapImageViewTarget(holder.img) {
					 @Override
					 protected void setResource(Bitmap resource) {
						 RoundedBitmapDrawable circularBitmapDrawable =
								 RoundedBitmapDrawableFactory.create(context.getResources(), resource);
						 circularBitmapDrawable.setCircular(true);
						 holder.img.setImageDrawable(circularBitmapDrawable);
					 }
				 });

        		 /*imageLoader.displayImage(path, holder.img, options, new ImageLoadingListener() {
        			
        			@Override
        			public void onLoadingStarted(String arg0, View arg1) {
        				// TODO Auto-generated method stub
        				pb.setVisibility(View.VISIBLE);
        			}
        			
        			@Override
        			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
        				// TODO Auto-generated method stub
        				pb.setVisibility(View.GONE);
        			}
        			
        			@Override
        			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
        				// TODO Auto-generated method stub
        				pb.setVisibility(View.GONE);
        			}
        			
        			@Override
        			public void onLoadingCancelled(String arg0, View arg1) {
        				// TODO Auto-generated method stub
        				pb.setVisibility(View.GONE);
        			}
        		});*/
        		 
        	 }
        	 
        	 
        	 else{
        		 holder.img.setBackgroundResource(R.drawable.profile);
        	 }
        }


 
        // holder.img.setImageResource(imageId[position]);
 
             
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, String string);
    protected abstract void onItemLongClick(View v, String string);

}