package com.ogbongefriends.com.ogbonge.how_about_we;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.R;

import com.ogbongefriends.com.custom.ImageLoaderBlur;
import com.ogbongefriends.com.DB.DB;


public abstract class EventDetailsAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	Context context;
	HashMap<String, String> row;
	private OnClickListener detailPageListener;
	public ImageLoaderBlur imageLoader;
	public EventDetailsAdapter(Context context,
			ArrayList<HashMap<String, String>> placeData) {
		this.context = context;
		this.data = placeData;
		  imageLoader=new ImageLoaderBlur(this.context);
		//Log.d("data in EventdetailAdapter",""+placeData.toString());
		
		detailPageListener=new OnClickListener() {
			
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
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View view, final ViewGroup parent) {

		final int pos = index;

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		//	view = inflater.inflate(R.layout.place_list_item, parent, false);
		}
		
		row = data.get(index);

		TextView txtPlaceName = (TextView) view.findViewById(R.id.textView1);
		//txtPlaceName.setText(row.get(Table.event_master.name.toString()));
		String eventId=row.get(DB.Table.event_master.id.toString());
		txtPlaceName.setTag(eventId);
		txtPlaceName.setOnClickListener(detailPageListener);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		String rowImage = row.get(DB.Table.event_image_master.image_name.toString());
		try {
			if (rowImage.length() > 5) {
				
				 imageLoader.DisplayImage(rowImage, imageView);
				//new ImageLoadingTask(imageView, context).execute(rowImage);

			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	
		imageView.setTag(eventId);
		imageView.setOnClickListener(detailPageListener);
		
		return view;
	}
	protected abstract void onItemClick(View v, int position);
}