package com.ogbongefriends.com.ogbonge.search;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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

import com.ogbongefriends.com.DB.DB;

//  android:numColumns="auto_fit"
public abstract class Gridview_adapter extends BaseAdapter{
 
    String [] result;
    Context context;
   // ArrayList<HashMap<String, String>> _url;
 int [] imageId;
 private ImageLoader imageLoader;
 private Double UserRating=0.0;
 private ArrayList<HashMap<String, String>> _data;
 private OnClickListener detailPageListener;
 
 
      private static LayoutInflater inflater=null;
    public Gridview_adapter(Context mainActivity, ArrayList<HashMap<String, String>> data) {
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
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
         
         File cacheDir = StorageUtils.getCacheDirectory(context);
 				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
 				
 				
 		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
 		        .diskCacheExtraOptions(480, 800, null)
 		        .threadPoolSize(3) // default
 		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
 		        .denyCacheImageMultipleSizesInMemory()
 		        .memoryCacheSize(2 * 1024 * 1024)
 		        .memoryCacheSizePercentage(13) // default
 		        .diskCacheSize(50 * 1024 * 1024)
 		        .diskCacheFileCount(100)
 		        .imageDownloader(new BaseImageDownloader(context)) // default
 		        .imageDecoder(new BaseImageDecoder(false)) // default
 		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
 		        .writeDebugLogs()
 		        .build();
 				
 				DisplayImageOptions options = new DisplayImageOptions.Builder()
 		        .resetViewBeforeLoading(false)  // default
 		        .delayBeforeLoading(1000)
 		        .cacheInMemory(false) // default
 		        .cacheOnDisk(false) // default
 		        .considerExifParams(false) // default
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
        ImageView img,r1,r2,r3,r4,r5,verified_img,online;
        TextView photo_count,rating_text,location_text;
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
             holder.img.setScaleType(ScaleType.FIT_XY);
             holder.r1=(ImageView) rowView.findViewById(R.id.rate1);
             holder.r2=(ImageView) rowView.findViewById(R.id.rate2);
             holder.r3=(ImageView) rowView.findViewById(R.id.rate3);
             holder.r4=(ImageView) rowView.findViewById(R.id.rate4);
             holder.r5=(ImageView) rowView.findViewById(R.id.rate5);
             holder.verified_img=(ImageView)rowView.findViewById(R.id.verified_img);
            holder.online=(ImageView)rowView.findViewById(R.id.green_dot);
             
             
            if(row.get(DB.Table.user_master.login_status.toString()).length()>0){
            	 if((Long.parseLong(row.get(DB.Table.user_master.login_status.toString()))==1)){
            		 holder.online.setBackgroundResource(R.drawable.green_dot);
            	 }
            	 
            	 else{
            		 holder.online.setBackgroundResource(R.drawable.grey_dot);
            	 }
            }
             
            else{
            	 holder.online.setBackgroundResource(R.drawable.grey_dot);
            }
             final ProgressBar pb=(ProgressBar)rowView.findViewById(R.id.progressBar1);
 path=row.get("Image_url");
 holder.rating_text.setText(new DecimalFormat("##.##").format(Double.parseDouble(row.get(DB.Table.user_master.rate_count.toString()))));
 //new DecimalFormat("##.##").format(i2)
 
 UserRating=Double.parseDouble(row.get(DB.Table.user_master.rating.toString()));
 
 holder.tv.setText(row.get("user_name"));
 holder.location_text.setText(row.get(DB.Table.user_master.city.toString()));
 holder.photo_count.setText(row.get("photos"));
 rowView.setTag(row.get(DB.Table.user_master.uuid.toString()));
 pb.setVisibility(View.GONE);
 
 
 switch(Integer.parseInt(row.get(DB.Table.user_master.verification_level.toString()))){
	 
 case 0:
	 holder.verified_img.setVisibility(View.GONE);
	 break;
	 
 case 1:
	 holder.verified_img.setImageResource(R.drawable.verified);
	 break;
	 
 case 2:
	 holder.verified_img.setImageResource(R.drawable.double_verified);
	 break;
	 
 case 3:
	 holder.verified_img.setImageResource(R.drawable.confirmed);
	 break;
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
 
 if(path.length()>3){
 imageLoader.displayImage(path, holder.img, new ImageLoadingListener() {
 
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
 
        // holder.img.setImageResource(imageId[position]);
 
             
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, String string);
}