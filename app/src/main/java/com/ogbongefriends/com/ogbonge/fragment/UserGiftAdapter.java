package com.ogbongefriends.com.ogbonge.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.ogbongefriends.com.DB.DB.Table;

public abstract class UserGiftAdapter extends BaseAdapter{
 
    String [] result;
    Context context;
   // ArrayList<HashMap<String, String>> _url;
 int [] imageId;
 private ImageLoader imageLoader;
 private ArrayList<HashMap<String, String>> _data;
 private OnClickListener detailPageListener;
 private OnLongClickListener longClicked;
 public ArrayList<String>_selectedGift;
 private DisplayImageOptions options;
 
      private static LayoutInflater inflater=null;
    public UserGiftAdapter(Context mainActivity, ArrayList<HashMap<String, String>> data,ArrayList<String>selectedGift) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        _data=data;
        _selectedGift=selectedGift;
        
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
	         .displayer(new RoundedBitmapDisplayer(15))
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
        TextView userName;
        TextView description,gift_name,gift_cost;
        ImageView userimg;
        ImageView giftimg;
        CheckBox redeem_box;
        
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        HashMap<String, String> row = _data.get(position);
        View rowView;
 String path;
             rowView = inflater.inflate(R.layout.gift_with_user_row, null);
             holder.userimg=(ImageView) rowView.findViewById(R.id.user_image);
             holder.giftimg=(ImageView) rowView.findViewById(R.id.gift_image);
             holder.userName=(TextView)rowView.findViewById(R.id.user_name);
             holder.description=(TextView)rowView.findViewById(R.id.description);
             holder.gift_name=(TextView)rowView.findViewById(R.id.gift_name);
             holder.gift_cost=(TextView)rowView.findViewById(R.id.gift_points);
             holder.redeem_box=(CheckBox)rowView.findViewById(R.id.redeem_box);
             holder.giftimg.setScaleType(ScaleType.FIT_XY);
             holder.userimg.setScaleType(ScaleType.FIT_XY);
             
             
             
             final ProgressBar pb=(ProgressBar)rowView.findViewById(R.id.progressBar1);
             final ProgressBar userPb=(ProgressBar)rowView.findViewById(R.id.progressBar2);
             
             
 path=row.get("User_Image_url");
 holder.userName.setText(row.get("user_name"));
 holder.gift_name.setText(row.get(Table.gift_master.gift_title.toString()));
 holder.gift_cost.setText(row.get(Table.gift_sent_to_user.gift_cost.toString())+" points");
 //rowView.setTag(row.get(Table.user_master.uuid.toString()));
 holder.description.setText(row.get(Table.gift_sent_to_user.description.toString()));
 holder.redeem_box.setTag(row.get(Table.gift_sent_to_user.gitf_id.toString()));
 
 if(Integer.parseInt(row.get("gift_type"))!=1){
	 
	 holder.redeem_box.setVisibility(View.GONE);
	 holder.description.setVisibility(View.GONE);
 }
 
 if(_selectedGift.contains(String.valueOf(holder.redeem_box.getTag()))){
	 holder.redeem_box.setChecked(true);
 }
 else{
	 holder.redeem_box.setChecked(false);
 }
 
 holder.redeem_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			_selectedGift.add(String.valueOf(buttonView.getTag()));
		}
		else{
			_selectedGift.remove(String.valueOf(buttonView.getTag()));
		}
	}
});
 
 pb.setVisibility(View.GONE);
 
 if(path.length()>3){
 imageLoader.displayImage(row.get("User_Image_url").toString(), holder.userimg,options, new ImageLoadingListener() {
 
	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub
		userPb.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub
		userPb.setVisibility(View.GONE);
	}
	
	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		// TODO Auto-generated method stub
		userPb.setVisibility(View.GONE);
	}
	
	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub
		userPb.setVisibility(View.GONE);
	}
});
 }
 else{
	 holder.userimg.setBackgroundResource(R.drawable.profile);
 }
 
 imageLoader.displayImage(row.get("gift_image").toString(), holder.giftimg,options, new ImageLoadingListener() {
	 
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
 
 
        // holder.img.setImageResource(imageId[position]);
 
             
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, String string);
    protected abstract void onItemLongClick(View v, String string);

}