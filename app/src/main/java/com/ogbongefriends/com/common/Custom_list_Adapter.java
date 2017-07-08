package com.ogbongefriends.com.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.R;

public abstract class Custom_list_Adapter extends BaseAdapter{
 
    String [] result;
    Context context;
    Integer [] imageId;
    private OnClickListener detailPageListener;
    private String[] _data;
    private static LayoutInflater inflater=null;
    public Custom_list_Adapter(Context mainActivity, String[] data, Integer [] url) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        _data=data;
        imageId=url;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
			}
		};
	
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.length;
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
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
 String path;
             rowView = inflater.inflate(R.layout.list_row_image_text, null);
             holder.img=(ImageView) rowView.findViewById(R.id.list_image);
             holder.tv=(TextView)rowView.findViewById(R.id.list_name);
             
            
             
             
 holder.tv.setText(_data[position]);
 holder.img.setImageResource(imageId[position]);
 rowView.setTag(position);
 
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, int string);
    protected abstract void onItemLongClick(View v, int string);

}