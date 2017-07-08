package com.ogbongefriends.com.ogbonge.how_about_we;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.CelUtils;

import java.util.ArrayList;
import java.util.HashMap;

import com.ogbongefriends.com.DB.DB;


public abstract class EventAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, String>> data;
    Context context;
    HashMap<String, String> row;
    private OnClickListener detailPageListener;
    public ImageLoader imageLoader;
    private DisplayImageOptions options;

    public EventAdapter(Context context,
                        ArrayList<HashMap<String, String>> placeData) {
        this.context = context;
        this.data = placeData;
        detailPageListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
            }
        };


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

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
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

    @Override
    public View getView(int index, View view, final ViewGroup parent) {
        final int pos = index;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.events_list_item, parent, false);
        }
        view.setBackgroundColor(Color.WHITE);
        Log.d("In EventAdapter", "" + data);
        row = data.get(index);
        TextView event = (TextView) view.findViewById(R.id.event);
        TextView desc = (TextView) view.findViewById(R.id.how_about_we_text);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView details = (TextView) view.findViewById(R.id.cate);
        TextView date = (TextView) view.findViewById(R.id.dateText);
        AdView add = (AdView) view.findViewById(R.id.adView);
        RelativeLayout lr = (RelativeLayout) view.findViewById(R.id.parent_layout);
        ImageView banner = (ImageView) view.findViewById(R.id.banner);

        Button propose = (Button) view.findViewById(R.id.propose);
        Button closed_event = (Button) view.findViewById(R.id.close_event);
        propose.setText("Propose(" + row.get(DB.Table.event_master.comments.toString()) + ")");

//		if(row.get(Table.event_master.user_master_id.toString()).equalsIgnoreCase(pref.get(Constants.KeyUUID))){
//			view.findViewById(R.id.propose).setVisibility(View.GONE);
//		}


        if (row.get("type").toString().equalsIgnoreCase("1")) {

            lr.setVisibility(View.GONE);
            if (ShowEvents.bannerUrls.size() > 0) {
                banner.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
                imageLoader.displayImage(row.get("image"), banner);

            } else {
                banner.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                //AdRequest adRequest = new AdRequest.Builder().build();
                final AdRequest.Builder adReq = new AdRequest.Builder();
                final AdRequest adRequest = adReq.build();
                add.loadAd(adRequest);
            }
        } else {
            banner.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            lr.setVisibility(View.VISIBLE);
            desc.setText(row.get(DB.Table.event_master.event_description.toString()));
            event.setText(row.get(DB.Table.event_master.event_category_id.toString()));
            details.setText(row.get(DB.Table.event_master.event_subcategory_id.toString()));
            date.setText(CelUtils.getDateFromTimestamp(Long.parseLong(row.get(DB.Table.event_master.event_datetime.toString()))));
            location.setText(row.get(DB.Table.event_master.place.toString()));

            if (row.get("propose_status").toString().equalsIgnoreCase("2")) {
                propose.setVisibility(View.GONE);
            } else {
                propose.setVisibility(View.VISIBLE);
            }

            if (row.get(DB.Table.event_master.status.toString()).toString().equalsIgnoreCase("2")) {
                closed_event.setVisibility(View.VISIBLE);
            } else {
                closed_event.setVisibility(View.GONE);
            }

            view.setTag(row.get(DB.Table.event_master.id.toString()));
            view.setOnClickListener(detailPageListener);

        }

        return view;
    }

    // =============================================

    private String getCatName(String cat_id) {
        if (Integer.parseInt(cat_id) == 1) {
            return "Party";
        } else if (Integer.parseInt(cat_id) == 2) {
            return "Concert";
        } else {
            return "Exhibition";
        }
    }

    // =============================================

    protected abstract void onItemClick(View v, int position);
}