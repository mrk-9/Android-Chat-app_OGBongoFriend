package com.ogbongefriends.com.ogbonge.backstage_album;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;



public abstract class Circle_Gallery_Adapter extends BaseAdapter {
	
// ******* DECLARING VARIABLES *******
	
	//int count;
	Context context;

// ******* DECLARING HASH MAP *******
	
	String row;
	
// ******* DECLARING ARRAYLIST *******
	
	ArrayList <PhotoOfYouVO> data;

// ******* DECLARING LAYOUT INFLATER ******

	LayoutInflater inflater = null;

// ******* DECLARING CLICK LISTENER ******
	
	public OnClickListener itemClickListener;
	

	public Circle_Gallery_Adapter(Context context, ArrayList <PhotoOfYouVO> placeData) {
		
		Log.d("Data In Place data", "" + placeData);
		
		this.context = context;
		this.data = placeData;
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
		   imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		   final ProgressBar p=new ProgressBar(context);
		   imageView.setLayoutParams(new Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT,android.widget.Gallery.LayoutParams.FILL_PARENT));
		
		   row=data.get(position).getfullPhotoUrl();
				try {
			if (row.length() > 5) {
				String img_path = row;

				Glide.with(context).load(img_path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(imageView);
			}
			else {
				imageView.setImageResource(R.drawable.profile);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			imageView.setImageResource(R.drawable.profile);
		}

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
