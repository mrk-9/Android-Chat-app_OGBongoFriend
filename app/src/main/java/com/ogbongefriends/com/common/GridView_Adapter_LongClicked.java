package com.ogbongefriends.com.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
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

public abstract class GridView_Adapter_LongClicked extends BaseAdapter{
 
    String [] result;
    Context context;
   // ArrayList<HashMap<String, String>> _url;
 int [] imageId;
 private Double UserRating=0.0;
 private ImageLoader imageLoader;
 private ArrayList<HashMap<String, String>> _data;
 private OnClickListener detailPageListener;
 private OnLongClickListener longClicked;
 private DisplayImageOptions options;
 
      private static LayoutInflater inflater=null;
    public GridView_Adapter_LongClicked(Context mainActivity, ArrayList<HashMap<String, String>> data) {
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
        TextView tv;
        TextView photo_count,rating_text,location_text;
        ImageView img,r1,r2,r3,r4,r5,verified_img,online;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        HashMap<String, String> row = _data.get(position);
        View rowView;
 String path;
             rowView = inflater.inflate(R.layout.program_list, null);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
             holder.tv=(TextView)rowView.findViewById(R.id.user_name);
             holder.photo_count=(TextView)rowView.findViewById(R.id.photo_count);
             holder.rating_text=(TextView)rowView.findViewById(R.id.rating_text);
             holder.location_text=(TextView)rowView.findViewById(R.id.location_text);
             holder.online=(ImageView)rowView.findViewById(R.id.green_dot);
             holder.img.setScaleType(ScaleType.FIT_XY);
             
             
             holder.r1=(ImageView) rowView.findViewById(R.id.rate1);
             holder.r2=(ImageView) rowView.findViewById(R.id.rate2);
             holder.r3=(ImageView) rowView.findViewById(R.id.rate3);
             holder.r4=(ImageView) rowView.findViewById(R.id.rate4);
             holder.r5=(ImageView) rowView.findViewById(R.id.rate5);
             holder.verified_img=(ImageView)rowView.findViewById(R.id.verified_img);
             UserRating=Double.parseDouble(row.get(Table.user_master.rating.toString()));
             
             final ProgressBar pb=(ProgressBar)rowView.findViewById(R.id.progressBar1);
 path=row.get("Image_url");
 if(row.get(Table.user_master.last_seen.toString()).length()>0){
 if((Long.parseLong(row.get(Table.user_master.last_seen.toString()))/1000)<(5*60)){
	 holder.online.setBackgroundResource(R.drawable.green_dot);
 }
 
 else{
	 
 }
 }
 holder.tv.setText(row.get("user_name"));
 holder.location_text.setText(row.get(Table.user_master.city.toString()));
 holder.photo_count.setText(row.get("photos"));
 holder.rating_text.setText(row.get("rate_count"));
 rowView.setTag(row.get(Table.user_master.uuid.toString()));
 
 switch(Integer.parseInt(row.get(Table.user_master.verification_level.toString()))){
 
 case 0:
	 holder.verified_img.setVisibility(View.GONE);
	 break;
	 
 case 1:
	 holder.verified_img.setVisibility(View.VISIBLE);
	 holder.verified_img.setImageResource(R.drawable.verified);
	 break;
	 
 case 2:
	 holder.verified_img.setVisibility(View.VISIBLE);
	 holder.verified_img.setImageResource(R.drawable.double_verified);
	 break;
	 
 case 3:
	 holder.verified_img.setVisibility(View.VISIBLE);
	 holder.verified_img.setImageResource(R.drawable.confirmed);
	 break;
 }
 pb.setVisibility(View.GONE);
 if(path.length()>3){
 imageLoader.displayImage(path, holder.img,options, new ImageLoadingListener() {
 
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
});
 }
 else{
	 holder.img.setBackgroundResource(R.drawable.profile);
 }
 
 
 
 
 if(UserRating==0){
	 holder.r1.setImageResource(R.drawable.no_rating);
	 holder.r2.setImageResource(R.drawable.no_rating);
	 holder.r3.setImageResource(R.drawable.no_rating);
	 holder.r4.setImageResource(R.drawable.no_rating);
	 holder.r5.setImageResource(R.drawable.no_rating);
	}

if(UserRating<1 && UserRating>0){
	 holder.r1.setImageResource(R.drawable.small_half_star);
	 holder.r2.setImageResource(R.drawable.no_rating);
	 holder.r3.setImageResource(R.drawable.no_rating);
	 holder.r4.setImageResource(R.drawable.no_rating);
	 holder.r5.setImageResource(R.drawable.no_rating);
	}
if(UserRating==1){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.no_rating);
	holder.r3.setImageResource(R.drawable.no_rating);
	holder.r4.setImageResource(R.drawable.no_rating);
	holder.r5.setImageResource(R.drawable.no_rating);
	}

if(1<UserRating && UserRating<2){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.small_half_star);
	holder.r3.setImageResource(R.drawable.no_rating);
	holder.r4.setImageResource(R.drawable.no_rating);
	holder.r5.setImageResource(R.drawable.no_rating);
	}

if(UserRating==2){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.no_rating);
	holder.r4.setImageResource(R.drawable.no_rating);
	holder.r5.setImageResource(R.drawable.no_rating);
	}

if(2<UserRating && UserRating<3){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.small_half_star);
	holder.r4.setImageResource(R.drawable.no_rating);
	holder.r5.setImageResource(R.drawable.no_rating);
}

if(UserRating==3){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.rating);
	holder.r4.setImageResource(R.drawable.no_rating);
	holder.r5.setImageResource(R.drawable.no_rating);
}


if(3<UserRating && UserRating<4){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.rating);
	holder.r4.setImageResource(R.drawable.small_half_star);
	holder.r5.setImageResource(R.drawable.no_rating);
}

if(UserRating==4){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.rating);
	holder.r4.setImageResource(R.drawable.rating);
	holder.r5.setImageResource(R.drawable.no_rating);
}

if(4<UserRating && UserRating<5){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.rating);
	holder.r4.setImageResource(R.drawable.rating);
	holder.r5.setImageResource(R.drawable.small_half_star);
}

if(UserRating==5){
	holder.r1.setImageResource(R.drawable.rating);
	holder.r2.setImageResource(R.drawable.rating);
	holder.r3.setImageResource(R.drawable.rating);
	holder.r4.setImageResource(R.drawable.rating);
	holder.r5.setImageResource(R.drawable.rating);
}	
 
 



             
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, String string);
    protected abstract void onItemLongClick(View v, String string);

}