package com.ogbongefriends.com.ogbonge.photos;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.R;

import java.io.File;
import java.util.ArrayList;

public abstract class MemberAlbumAdapter extends BaseAdapter {

    // ******* DECLARING VARIABLES *******

    //int count;
    Context context;

    // ******* DECLARING IMAGE LOADER ******

    public ImageLoader imageLoader;
    private DisplayImageOptions options;

    // ******* DECLARING HASH MAP *******

    String row;

    // ******* DECLARING ARRAYLIST *******

    ArrayList<PhotoOfYouVO> data;

    // ******* DECLARING LAYOUT INFLATER ******

    LayoutInflater inflater = null;

    // ******* DECLARING CLICK LISTENER ******

    public OnClickListener itemClickListener;
    int screenSize;


    public MemberAlbumAdapter(Context context, ArrayList<PhotoOfYouVO> placeData, int screenSize) {

        Log.d("Data In Place data", "" + placeData);
        this.screenSize = screenSize;
        this.context = context;
        this.data = placeData;


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

        Log.d("this.data.......", "" + this.data);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        itemClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
            }
        };
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {


        ImageView imageView = new ImageView(context);


        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";

                imageView.setLayoutParams(new Gallery.LayoutParams(250, 250));

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";

                imageView.setLayoutParams(new Gallery.LayoutParams(150, android.widget.Gallery.LayoutParams.FILL_PARENT));
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }

        imageView.setScaleType(ScaleType.FIT_XY);
        //final ProgressBar p=new ProgressBar(context);
        // imageView.setLayoutParams(new Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT,android.widget.Gallery.LayoutParams.FILL_PARENT));

        row = data.get(position).getfullPhotoUrl();
        try {
            if (row.length() > 5) {
                String img_path = row;
                //imageView.setImageResource(R.drawable.lock_backstage);
                imageLoader.displayImage(img_path, imageView, new ImageLoadingListener() {
                    //
//						@Override
                    public void onLoadingStarted(String arg0, View arg1) {
//							// TODO Auto-generated method stub
                        //p.setVisibility(View.VISIBLE);
                    }

                    //
                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//							// TODO Auto-generated method stub
                        //p.setVisibility(View.GONE);
                    }

                    //
                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//							// TODO Auto-generated method stub
                        //p.setVisibility(View.GONE);
                    }

                    //
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
//							// TODO Auto-generated method stub
                        //p.setVisibility(View.GONE);
                    }
                });


            } else {
                imageView.setImageResource(R.drawable.profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.profile);
        }
        //imageView.setTag(position);
        //	imageView.setOnClickListener(itemClickListener);
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

    protected abstract void onItemClick(View v, int string);
}


