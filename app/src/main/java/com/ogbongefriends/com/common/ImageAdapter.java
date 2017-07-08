package com.ogbongefriends.com.common;

import java.util.ArrayList;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ogbongefriends.com.R;
import com.bumptech.glide.*;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageAdapter extends BaseAdapter
{
  private Context context;
  private int itemBackground;
  private int totalimages=0;
  private ArrayList<String> urls;
  private String _user_server_id,full_image_path;
    LayoutInflater inflater=null;
  public ImageAdapter(Context c, ArrayList<String> urls,String user_uuid)
  {
	  super(); 
      context = c;
      this.urls = urls;
      _user_server_id=user_uuid;
      Log.d("URL SIZEE", ""+urls.size());
      inflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

  }
   
  
//---returns the number of images---
   public int getCount() 
   {
	   Log.d("URL size",""+urls.size());
	   totalimages=urls.size();
       return urls.size();
	 //  return mImageIds.length;
   }
       //---returns the ID of an item---
   public Object getItem(int position)
   {
       return position;
   }
   //---returns the ID of an item---
   public long getItemId(int position)
   {
	   Log.d("ID of the position", ""+position);
	  
       return position;
   }


   
   @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
	  /* ImageView imageView = new ImageView(context);
       ProgressBar b=new ProgressBar(context);
	     imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	   imageView.setLayoutParams(new Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT, android.widget.Gallery.LayoutParams.FILL_PARENT));
	   full_image_path=context.getString(R.string.urlString)+"userdata/image_gallery/"+_user_server_id+"/photos_of_you/"+urls.get(arg0);
	 Log.d("Image", full_image_path);
       AnimationDrawable animPlaceholder = (AnimationDrawable)context.getResources().getDrawable(R.drawable.image_loading_animation);
       animPlaceholder.start();
       //Glide.with(context).load(full_image_path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(0.01f).into(imageView);


       Glide.with(context).load(full_image_path).listener(new RequestListener<String, GlideDrawable>() {
           @Override
           public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
               return false;
           }

           @Override
           public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
              Log.e("Resource Ready","Resource Ready");
               return false;
           }
       }).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(0.01f).into(imageView);

*/
		   return null;
	   
	 //return imageView;
	}

   }