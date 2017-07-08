package com.ogbongefriends.com.ogbonge.photos;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.R;

public abstract class AlbumAdapter extends BaseAdapter{
 
    Context context;
 ArrayList<PhotoOfYouVO> imageId;
 private OnClickListener detailPageListener;
    private OnLongClickListener longclick;
 private String _server_id;
 
      private static LayoutInflater inflater=null;
    public AlbumAdapter(Context mainActivity, ArrayList<PhotoOfYouVO> data,String server_id) {
        // TODO Auto-generated constructor stub
        context=mainActivity;
        imageId=data;
        _server_id=server_id;
        detailPageListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
			}
		};

        longclick=new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onlongClick(v, Integer.parseInt(String.valueOf(v.getTag())));
                return false;
            }
        };
		
		
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageId.size();
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
 String path;
             rowView = inflater.inflate(R.layout.album_row, null);
             
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
             holder.pb=(ProgressBar)rowView.findViewById(R.id.pbb);
             holder.img.setScaleType(ScaleType.FIT_XY);
            holder.chb=(CheckBox)rowView.findViewById(R.id.checkbox);
             rowView.setTag(position);
        imageId.get(position).setIsChecked(true);
        if(imageId.get(position).isSelected()){
            ((RelativeLayout)rowView.findViewById(R.id.parent_layout)).setBackgroundColor(context.getResources().getColor(R.color.light_yello));
        }
        else{
            ((RelativeLayout)rowView.findViewById(R.id.parent_layout)).setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    //         albums.checkStatus[position]=false;
        holder.pb.setVisibility(View.GONE);



             holder.chb.setOnClickListener(new OnClickListener() {

                 @Override
                 public void onClick(View v) {
                     // TODO Auto-generated method stub
                     if (holder.chb.isChecked()) {
                         Log.d("Is checkeddddd", "" + position);
                         imageId.get(position).setIsChecked(true);

                     } else {
                         imageId.get(position).setIsChecked(false);
                     }
                 }
             });
             
	    path=context.getString(R.string.urlString)+"userdata/image_gallery/"+_server_id+"/photos_of_you/"+imageId.get(position).getPhoto_pic();

        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(holder.img);


       /* Glide
                .with(context)
                .load(path)
                .asBitmap().thumbnail(0.001f).placeholder(R.drawable.profile)
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.img.setImageBitmap(resource); // Possibly runOnUiThread()
                    }
                });*/

        rowView.setOnClickListener(detailPageListener);
        rowView.setOnLongClickListener(longclick);
        return rowView;
    }

    protected abstract void onItemClick(View v, int string);
    protected abstract void onlongClick(View v, int string);

}