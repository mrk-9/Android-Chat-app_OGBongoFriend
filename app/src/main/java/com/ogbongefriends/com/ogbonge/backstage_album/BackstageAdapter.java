package com.ogbongefriends.com.ogbonge.backstage_album;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.R;

public abstract class BackstageAdapter extends BaseAdapter{
    Context context;
 ArrayList<PhotoOfYouVO> imageData;
 private OnClickListener detailPageListener;
 private OnLongClickListener longClickListener;
 private String _server_id;
 
      private static LayoutInflater inflater=null;
    public BackstageAdapter(Context mainActivity, ArrayList<PhotoOfYouVO> data,String server_id) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        imageData=data;
        _server_id=server_id;
        
        longClickListener=new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				onLongItemClick(v, Integer.parseInt(String.valueOf(v.getTag())),String.valueOf(v.getTag(R.id.image)));
				return false;
			}
		};
        
        detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())),String.valueOf(v.getTag(R.id.image)));
			}
		};
		
		
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageData.size();
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
        ImageView img;
        ProgressBar pb;
        CheckBox chb;
        String path;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
 String path = null;
             rowView = inflater.inflate(R.layout.album_row, null);
             holder.chb=(CheckBox)rowView.findViewById(R.id.checkbox);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
             holder.pb=(ProgressBar)rowView.findViewById(R.id.pbb);
             holder.img.setScaleType(ScaleType.FIT_XY);

        imageData.get(position).setIsChecked(false);
        holder.pb.setVisibility(View.GONE);
             
// if(position==0){
//	 
//	 holder.img.setImageResource(R.drawable.black_layer); 
//	 holder.pb.setVisibility(View.GONE);
// }
 //else{
	 
	 path=imageData.get(position).getfullPhotoUrl();
	 holder.chb.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
             // TODO Auto-generated method stub
             if (holder.chb.isChecked()) {
                 Log.d("Is checkeddddd", "" + position);
                 imageData.get(position).setIsChecked(true);

             } else {
                 imageData.get(position).setIsChecked(false);
             }
         }
     });

        Log.d("img path ",path);
        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.001f).into(holder.img);

 rowView.setTag(position);
 rowView.setTag(R.id.image, imageData.get(position).getId());
 
 
 
             
             
 rowView.setOnClickListener(detailPageListener);
 rowView.setOnLongClickListener(longClickListener);
        return rowView;
    }
    protected abstract void onItemClick(View v, int string,String path);
    protected abstract void onLongItemClick(View v, int string,String path);

}