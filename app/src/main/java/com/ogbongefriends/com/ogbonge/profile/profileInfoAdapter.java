package com.ogbongefriends.com.ogbonge.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ogbongefriends.com.R;

public abstract class profileInfoAdapter extends BaseAdapter{
 
    String [] result;
    
    Context context;
   // ArrayList<HashMap<String, String>> _url;
 private OnClickListener detailPageListener;
 
 
      private static LayoutInflater inflater=null;
    public profileInfoAdapter(Context mainActivity, String[] data) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        result=data;
      
        detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
			}
		};
		
		 inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
         
           }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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
        TextView upperText;
        TextView lowerText;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
      
        View rowView;
 String path;
 
 
 
	
	 rowView = inflater.inflate(R.layout.setting_single_text, null);
	 holder.upperText=(TextView)rowView.findViewById(R.id.upperText);
	 holder.upperText.setText(result[position]);
 
 rowView.setTag(position);
             
 rowView.setOnClickListener(detailPageListener);
 
        return rowView;
    }
    protected abstract void onItemClick(View v, Integer string);
   
}