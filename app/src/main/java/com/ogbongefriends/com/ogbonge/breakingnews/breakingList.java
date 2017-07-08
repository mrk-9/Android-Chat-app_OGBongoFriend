package com.ogbongefriends.com.ogbonge.breakingnews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.BreakingNewsApi;
import com.ogbongefriends.com.api.getBanner;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.EndlessScrollListener;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("ValidFragment")
public class breakingList extends Fragment {

	public static Preferences pref;
	private long str;
	private long other_user;
	private DB db;
	private Cursor data;
	private breakNewsListAdapter eventAdapter;
	private ArrayList<HashMap<String, String>> eventsData;
	int event_id;
	private ListView listView;
	private final int NUM_OF_ROWS_PER_PAGE = 20;
	BreakingNewsApi geteventapi;
	CustomLoader p;
	List<String> arraylist;
	int count = 0;
	FragmentManager fragmentManager;
	Fragment fragment;
	int screenSize;
	Notification nt;
	private int cat_selected=0, event_selected=0;
	private TextView empty;
	private Context _ctx;
	private View rootView;
	public static int banner_index=0,banner_position;
	private com.ogbongefriends.com.api.getBanner getBanner;
	static ArrayList<String>bannerUrls=new ArrayList<String>();
	public static Calendar cal;
	public breakingList(){

	}
	@SuppressLint("ValidFragment")
	public breakingList(Context ctx) {
		_ctx=ctx;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){
		rootView = inflater.inflate(R.layout.all_news, container,
				false);
		
		p = DrawerActivity.p;
		cal=Calendar.getInstance();
		
		Utils.hideSoftKeyboard(getActivity());
		pref=new Preferences(_ctx);
		fragmentManager = getFragmentManager();
		Constants.isPlaceOrEvent = true;
		db = new DB(getActivity());
		arraylist = new ArrayList<String>();
		empty = (TextView) rootView.findViewById(R.id.empty);
		empty.setVisibility(View.GONE);
		other_user = pref.getlong("other_user");
		
		 screenSize = getResources().getConfiguration().screenLayout &
		        Configuration.SCREENLAYOUT_SIZE_MASK;
		listView = (ListView) rootView.findViewById(R.id.place_list_data);
		listView.setDividerHeight(0);
		
		HashMap<String, String> datatoupdate=new HashMap<String, String>();
		datatoupdate.put(DB.Table.red_bubble_master.newNewsCount.toString(), "0");
		db.update(DB.Table.Name.red_bubble_master, datatoupdate, DB.Table.red_bubble_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null);
		
			eventsData = new ArrayList<HashMap<String, String>>();
			
		
		eventAdapter = new breakNewsListAdapter(getActivity(), eventsData) {

			@Override
			protected void onItemClick(View v, int position,String itemUrl) {
				// TODO Auto-generated method stub

				pref.set(Constants.NewsImageUrl, itemUrl);
						pref.set(Constants.NewsId, String.valueOf(position)).commit();
				((DrawerActivity) getActivity()).displayView(33);
			}
		};

		
		listView.setAdapter(eventAdapter);
		
		getBanner=new getBanner(_ctx, db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getBannerUrl();
				hitGetEventsAPI(1);
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
		};

		p.show();
		getbanners();
		
		}
		return rootView;

	}

	//========================================
	
	
	
	private void getbanners(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		
		data.put("auth_token", pref.get(Constants.kAuthT));
		data.put("uuid", pref.get(Constants.KeyUUID));
		
		data.put("type", "10");
				switch(screenSize) {
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		    	data.put("device_type", "3");
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	data.put("device_type", "2");
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	data.put("device_type", "2");
		        break;
		    default:
		       
		}
				
			getBanner.setPostData(data);
			callApi(getBanner);
				
	}
	
	
	
	// ====================================

	
		
	protected void getDataInBackground1(int page) {
		// TODO Auto-generated method stub
		String sql="";
		
		
			
			sql="SELECT * from news_master where status=1 ORDER BY modify_date DESC";		
			
				//sql="SELECT * from event_master where user_master_id='"+ pref.get(Constants.KeyUUID) +"' AND type=2";
				
				
				//sql="SELECT * from event_master where user_master_id='"+ pref.get(Constants.KeyUUID) +"' AND type=3";
		
db.open();
		data = db.findCursor(sql, null);
		Log.d("arv", "arv  DB_count"+data.getCount());
		loadPage1(page-1);
	}
	
	

	

	private synchronized void loadPage1(int page) {
		try {
			final int pag = page;
Log.e("Loading Data For page",""+pag+"  "+(pag*NUM_OF_ROWS_PER_PAGE));
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
				
					boolean status = data.moveToPosition(pag
							* NUM_OF_ROWS_PER_PAGE);

					int i = 0;

					while (status && i < NUM_OF_ROWS_PER_PAGE) {

						HashMap<String,String> testMap=new HashMap<String, String>();
						testMap=getEventHashMap(db, data, str);
						if(!eventsData.contains(testMap)){
							eventsData.add(testMap);
						}
						// data.moveToNext();
						i++;
						status = data.moveToNext();

					}

					eventAdapter.notifyDataSetChanged();

					if (eventAdapter.getCount() > 0) {
						empty.setVisibility(View.GONE);
					} else {
						empty.setVisibility(View.VISIBLE);
					}

				}
			});

		} catch (Exception e) {

		}

	}

	
	
	
	public  HashMap<String, String> getEventHashMap(DB db, Cursor c,
			long str2) {
		banner_index++;
		HashMap<String, String> row = new HashMap<String, String>();
		//if(banner_index%3!=0 || bannerUrls.size()<1){
		HashMap<String, String> row1 = new HashMap<String, String>();




		db.open();
		
		
		
		String currNewsId = c.getString(c.getColumnIndex(DB.Table.news_master.id
				.toString()));
		row.put("type", "0");
		row.put(DB.Table.news_master.id.toString(), currNewsId);

		row.put(DB.Table.news_master.news_title.toString(), c.getString(c
				.getColumnIndex(DB.Table.news_master.news_title.toString())));

		row.put(DB.Table.news_master.news_short_description.toString(), c.getString(c
				.getColumnIndex(DB.Table.news_master.news_short_description.toString())));
		
		row.put(DB.Table.news_master.posted_by.toString(), c.getString(c
				.getColumnIndex(DB.Table.news_master.posted_by.toString())));
		
		
	long cursec=	cal.getTimeInMillis()/1000;
		
	long timediff=cursec-(Long.parseLong(c.getString(c.getColumnIndex(DB.Table.news_master.add_date.toString()))));
	int day=(int) (((timediff / 60 / 60) / 24)  % 7);
	int hour=(int) (((timediff / 60) / 60) % 24);
	int min=(int)((timediff / 60) % 60);	
	if(day>0){
		row.put(DB.Table.news_master.add_date.toString(), (day)+"day,"+" "+(hour)+"hours, "+(min)+"mins ");
	}
	else{
		if(hour>0){
			row.put(DB.Table.news_master.add_date.toString(), (hour)+"hours, "+(min)+"mins ");
		}
		else{
			row.put(DB.Table.news_master.add_date.toString(),(min)+"mins ");
		}
	}
	
		
		
		
		String ttt= DB.Table.news_media.news_master_id+" ="+c.getString(c.getColumnIndex(DB.Table.news_master.id
				.toString()))+" AND status = "+1;
		
		Log.d("arv", "arv" + ttt);
		
		Cursor temp=db.findCursor(DB.Table.Name.news_media,ttt ,null,null);
		
			if(temp.moveToFirst()){
			
		
				row.put(DB.Table.news_media.news_image.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.news_media.news_image.toString())));
				
				row.put(DB.Table.news_media.media_type.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.news_media.media_type.toString())));
				
		}
		
		Log.v("hash map", row + "");
db.close();
		if(!eventsData.contains(row)){
			if(banner_index%3==0){

				banner_position++;
				if(banner_position>=bannerUrls.size()){
					banner_position=0;
				}
				row1.put("type",
						"1");
				if(bannerUrls.size()>banner_position){
					row1.put("image",
							bannerUrls.get(banner_position));
				}
				else{
					row1.put("image",
							"");
				}
				eventsData.add(row1);

			}
		}



		return row;

	}

	//===========================
	
	
	public void getBannerUrl(){
db.open();
		Cursor	bannerData = db.findCursor(DB.Table.Name.banner_master, DB.Table.banner_master.status.toString()+" = 1",null, null);
			Log.d("data sizee", ""+bannerData.getCount());
			bannerData.moveToFirst();
					
					
			for(int i=0; i<bannerData.getCount();i++){
				if(bannerData.getString(bannerData.getColumnIndex(DB.Table.banner_master.image.toString())).length()>3){
			
					
					
					String url="http://www.ogbongefriends.com/userdata/banners/"+bannerData.getString(bannerData.getColumnIndex(DB.Table.banner_master.image.toString()));
					
					bannerUrls.add(i, url);
			}
				bannerData.moveToNext();
			}
		}
	
	// =============================================================
	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(getActivity())) {
			Log.v("Internet Not Conneted", "");
			Utils.same_id(getString(R.string.error), getString(R.string.no_internet),_ctx);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					p.cancel();
					Utils.same_id("Error", getString(R.string.no_internet),_ctx);
				}
			});
			return;
		} else {
			Log.v("Internet Conneted", "");
		}

		Thread t = new Thread(r);
		t.setName(r.getClass().getName());
		t.start();

	}

	
	// ==========================================

	protected void callOnUpdaUI1(final int page) {
		// TODO Auto-generated method stub
		if(page==1){
			eventsData.clear();
		}
			p.show();
		
		android.util.Log.v("on update UI", "----->>");

		//eventsData = new ArrayList<HashMap<String, String>>();

		

		try {
				listView.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void loadPage(int page) {

					// TODO Auto-generated method stub
					Log.e("On Scroll Listener Load Page Called ====>>> ", ""
							+ page);
					if (BreakingNewsApi.currentPage1 > 0
							&& BreakingNewsApi.currentPage1 != 1) {
						hitGetEventsAPI(BreakingNewsApi.currentPage1);
						breakingList.this.loadPage1(BreakingNewsApi.currentPage1-1);
					}
					
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						getDataInBackground1(page);
					} catch (Exception e) {
						// TODO: handle exception
						Log.d(e.getMessage() + "at People Management Module",
								"");
					} finally {
						p.cancel();
					}
				}

			}).start();

		} catch (Exception e) {

		}

	
		
		eventAdapter.notifyDataSetChanged();
		p.cancel();
	}
	
	

	// =======google analytics========

	@Override
	public void onStart() {
		super.onStart();
//		GAnalytics.StartTrakingfor_fragments(getActivity(), getActivity(),
//				"Show events Page");

	}

	@Override
	public void onStop() {
		super.onStop();
		p.cancel();
		//GAnalytics.StopTraking(getActivity(), getActivity());

	}

	// ==================param rule for btn================

	
	// ========================================

	@Override
	public void onPause() {
		super.onPause();

		if (p != null)
			p.cancel();

	}


	
	
	void hitGetEventsAPI(final int page) {
		geteventapi = new BreakingNewsApi(getActivity(), db, p) {
			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				
			
				}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub

				p.cancel();
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				p.cancel();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						callOnUpdaUI1(page);
					}
				});
			}
		};
		
		
	HashMap<String, String>data=new HashMap<String, String>();
	
	data.put("uuid", pref.get(Constants.KeyUUID));
	data.put("auth_token", pref.get(Constants.kAuthT));
	data.put("page_index", String.valueOf(page));
	data.put("time_stamp", "");
	
	geteventapi.setPostData(data);	
		callApi(geteventapi);
	}
}