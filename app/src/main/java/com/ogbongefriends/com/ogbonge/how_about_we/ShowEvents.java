package com.ogbongefriends.com.ogbonge.how_about_we;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.getBanner;
import com.ogbongefriends.com.api.getEventApi;
import com.ogbongefriends.com.api.getPlaceApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.EndlessScrollListener;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;


@SuppressLint("NewApi") public class ShowEvents extends Fragment {

	public static Preferences pref;
	private long str;
	private long other_user;
	private DB db;
	private boolean init_mypost=false,init_event=false;
	private Cursor data;
	private EventAdapter eventAdapter,myPostAdapter,myDateAdapter;
	private ArrayList<HashMap<String, String>> eventsData,mypostData,mydateData;
	int event_id;
	private ListView listView,mypost,mydate;
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	getEventApi geteventapi;
	getPlaceApi getplaceapi;
	CustomLoader p;
	List<String> arraylist;
	int count = 0;
	FragmentManager fragmentManager;
	Fragment fragment;
	Notification nt;
	private Button findAParty;
	private Button myParties,dates;
	private int findAPartyID;
	private int myPartiesID;
	private int dateID;
	private View btview;
	private Button create_event_btn;
	private int cat_selected=0, event_selected=0;
	private TextView empty;
	private Context _ctx;
	private View rootView;
	public static int banner_index=0,banner_position;
	int screenSize;
	private com.ogbongefriends.com.api.getBanner getBanner;
	private user_profile_api user_profile_info;
	static ArrayList<String>bannerUrls=new ArrayList<String>();

	public ShowEvents(Context ctx) {
		_ctx=ctx;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){
		rootView = inflater.inflate(R.layout.all_place, container,
				false);
		
		p = DrawerActivity.p;

		Utils.hideSoftKeyboard(getActivity());
		pref=new Preferences(_ctx);
		fragmentManager = getFragmentManager();
		Constants.isPlaceOrEvent = true;
		db = new DB(getActivity());
		arraylist = new ArrayList<String>();
		empty = (TextView) rootView.findViewById(R.id.empty);
		create_event_btn=(Button)rootView.findViewById(R.id.create_event_btn);
		empty.setVisibility(View.GONE);
		mypost=(ListView)rootView.findViewById(R.id.mypost);
		mydate=(ListView)rootView.findViewById(R.id.mydate);
		other_user = pref.getlong("other_user");

		btview = (View) rootView.findViewById(R.id.buttonview);

		findAParty = (Button) rootView.findViewById(R.id.myevents);

		myParties = (Button) rootView.findViewById(R.id.attendingevents);

		dates=(Button)rootView.findViewById(R.id.date);
		
//		findAParty.setBackgroundColor(R.color.light_blue);
//		myParties.setBackgroundColor(R.color.light_blue);
//		dates.setBackgroundColor(R.color.light_blue);
		
		findAParty.setTextColor(Color.WHITE);
		myParties.setTextColor(Color.WHITE);
		dates.setTextColor(Color.WHITE);
		 screenSize = getResources().getConfiguration().screenLayout &
			        Configuration.SCREENLAYOUT_SIZE_MASK;
		findAParty.setTypeface(null, Typeface.BOLD);
		myParties.setTypeface(null, Typeface.BOLD);
		dates.setTypeface(null, Typeface.BOLD);
		
		findAParty.setText("Events");
		myParties.setText("My Posts");
		dates.setText("My Dates");

		findAPartyID = findAParty.getId();
		myPartiesID = myParties.getId();
		dateID=dates.getId();
		
		RulrForBtn(findAPartyID);

		listView = (ListView) rootView.findViewById(R.id.place_list_data);
		listView.setDividerHeight(0);
		
		mypost = (ListView) rootView.findViewById(R.id.mypost);
		mypost.setDividerHeight(0);
		mypost.setVisibility(View.GONE);
		
		mydate = (ListView) rootView.findViewById(R.id.mydate);
		mydate.setDividerHeight(0);
		mydate.setVisibility(View.GONE);

		eventsData = new ArrayList<HashMap<String, String>>();
		mypostData = new ArrayList<HashMap<String, String>>();
		mydateData = new ArrayList<HashMap<String, String>>();
		
		
		
		
		
		
		eventAdapter = new EventAdapter(getActivity(), eventsData) {

			@Override
			protected void onItemClick(View v, int position) {
				// TODO Auto-generated method stub
				// if (Constants.otherUserProfile) {

				// edit.putLong("other_user", str).commit();
				// } else {

				// }
				
				
				pref.set("clicked_event_id", position).commit();
				((DrawerActivity) getActivity()).displayView(34);
			}
		};
		listView.setAdapter(eventAdapter);
		
		
		
		
		myPostAdapter = new EventAdapter(getActivity(), mypostData) {

			@Override
			protected void onItemClick(View v, int position) {
				// TODO Auto-generated method stub

				pref.set("clicked_event_id", position).commit();
				((DrawerActivity) getActivity()).displayView(34);
			}
		};

		mypost.setAdapter(myPostAdapter);
		
		
		create_event_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(38);	
			}
		});

		listView.setAdapter(eventAdapter);

		// -====show notification=========

			
		//UpdateNotification();

		// =============open handle ======================

	
		// =============================================

		findAParty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				p.show();
				init_event=false;
				eventsData.clear();
				mydate.setVisibility(View.GONE);
				mypost.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				hitGetEventsAPI(1,1);
				RulrForBtn(findAPartyID);
			}
		});

		// ============================================

		myParties.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			
				p.show();
				mypostData.clear();
				init_mypost=false;
				mydate.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				mypost.setVisibility(View.VISIBLE);
				hitGetEventsAPI(1,2);
				RulrForBtn(myPartiesID);
			}
		});
		
		
		dates.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				p.show();
				mydateData.clear();
				listView.setVisibility(View.GONE);
				mypost.setVisibility(View.GONE);
				mydate.setVisibility(View.VISIBLE);
				//getAllEventdata3(3);
				hitGetEventsAPI(1,3);
				RulrForBtn(dateID);
			}
		});
		

		// =============================================

		

		// ======================
		

		
mydate.setVisibility(View.GONE);
mypost.setVisibility(View.GONE);

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
		getUserProfileFromApi();
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
	
	
private void getbanners(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		
		data.put("auth_token", pref.get(Constants.kAuthT));
		data.put("uuid", pref.get(Constants.KeyUUID));
		
		data.put("type", "5");
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
	
	
	private void getUserProfileFromApi(){
		 p.show();
	     user_profile_info=new user_profile_api(_ctx, db, p){
			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

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
				hitGetEventsAPI(1,1);
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				
			}
	     };
	     
	     
	     HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     user_profile_info.setPostData(map);
	     callApi(user_profile_info);
	}
	
	// ====================================

	
	
	private void getAllEventdata3() {
		try {
			eventsData.clear();
			p.show();

			mydate.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void loadPage(int page) {
					// TODO Auto-generated method stub
					Log.v("On Scroll Listener Load Page Called ====>>> ", ""
							+ page);
					if (getEventApi.currentPage3 > 0) {
						hitGetEventsAPI(getEventApi.currentPage3 + 1,3);
					}
					ShowEvents.this.loadPage3(page);
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						getDataInBackground3();
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

	}
	
	protected void getDataInBackground1() {
		// TODO Auto-generated method stub
		String sql="";
			//sql="SELECT * from event_master where type=1";		
			
			sql="SELECT * from event_master ORDER BY id DESC;";	
		
db.open();
		data = db.findCursor(sql, null);
		Log.d("arv", "arv  DB_count"+data.getCount());
		if(init_event==false){
		loadPage1(0);
		init_event=true;
		}
	}
	
	
	protected void getDataInBackground2() {
		// TODO Auto-generated method stub
		String sql="";
		sql="SELECT * from event_master where user_master_id='"+ pref.get(Constants.KeyUUID)+"' AND type=2 ORDER BY add_date DESC;";	
	db.open();
	data = db.findCursor(sql, null);
	Log.d("data count", "arv"+data.getCount());
	if(init_mypost==false){
		loadPage2(0);
		init_mypost=true;
	}
	

	}
	
	
	protected void getDataInBackground3() {
		// TODO Auto-generated method stub
		String sql="";
		sql="SELECT * from event_master where type=3";
		
db.open();
		data = db.findCursor(sql, null);

		loadPage3(0);

	}
	

	private synchronized void loadPage1(int page) {
		try {
			final int pag = page;

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// manage no record found label
Log.d("arv", "arv"+data.getCount());
					boolean status = data.moveToPosition(pag
							* NUM_OF_ROWS_PER_PAGE);

					int i = 0;

					while (status && i < NUM_OF_ROWS_PER_PAGE) {
						banner_index++;
						//if(banner_index%3!=0 || bannerUrls.size()<1){
						if(banner_index%3!=0){
						Log.d("index", "index"+data.getPosition());
						eventsData.add(getEventHashMap(db, data, str));
						i++;
						status = data.moveToNext();

						}
						else{
							eventsData.add(getBannerHashMap());
						}
						
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

	
	private synchronized void loadPage2(int page) {
		try {
			final int pag = page;

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// manage no record found label
                  Log.d("arv", "arv"+data.getCount());
                  Log.d("arv", "arv"+pag);
					boolean status = data.moveToPosition(pag
							* NUM_OF_ROWS_PER_PAGE);

					int i = 0;

					while (status && i < NUM_OF_ROWS_PER_PAGE) {
						banner_index++;
					//	if(banner_index%3!=0 || bannerUrls.size()<1){
						if(banner_index%3!=0){
					Log.d("index", "index"+data.getPosition());
						mypostData.add(getEventHashMap(db, data, str));
						i++;
						status = data.moveToNext();

					}
						else{
							mypostData.add(getBannerHashMap());
						}
					}

					myPostAdapter.notifyDataSetChanged();

					if (myPostAdapter.getCount() > 0) {
						empty.setVisibility(View.GONE);
					} else {
						empty.setVisibility(View.VISIBLE);
					}

				}
			});

		} catch (Exception e) {

		}

	}

	
	private synchronized void loadPage3(int page) {
		try {
			final int pag = page;

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// manage no record found label
						Log.d("arv", "arv"+data.getCount());
					boolean status = data.moveToPosition(pag
							* NUM_OF_ROWS_PER_PAGE);

					int i = 0;

					while (status && i < NUM_OF_ROWS_PER_PAGE) {
						banner_index++;
						//if(banner_index%3!=0 || bannerUrls.size()<1){
						if(banner_index%3!=0){
						mydateData.add(getEventHashMap(db, data, str));
						i++;
						status = data.moveToNext();
						}
						else{
							mydateData.add(getBannerHashMap());
						}

					}

					myDateAdapter.notifyDataSetChanged();

					if (myDateAdapter.getCount() > 0) {
						empty.setVisibility(View.GONE);
					} else {
						empty.setVisibility(View.VISIBLE);
					}

				}
			});

		} catch (Exception e) {

		}

	}

	
	
	public static HashMap<String, String> getEventHashMap(DB db, Cursor c,
			long str2) {

		
		HashMap<String, String> row = new HashMap<String, String>();
			db.open();
		String currEventId = c.getString(c.getColumnIndex(DB.Table.event_master.id
				.toString()));
		row.put("type",
				"0");
		row.put(DB.Table.event_master.id.toString(), currEventId);

		row.put(DB.Table.event_master.event_description.toString(), c.getString(c
				.getColumnIndex(DB.Table.event_master.event_description.toString())));

		row.put(DB.Table.event_master.comments.toString(), c.getString(c
				.getColumnIndex(DB.Table.event_master.comments.toString())));
		
		row.put(DB.Table.event_master.user_master_id.toString(), c.getString(c
				.getColumnIndex(DB.Table.event_master.user_master_id.toString())));
		
		row.put(DB.Table.event_master.status.toString(), c.getString(c
				.getColumnIndex(DB.Table.event_master.status.toString())));
		
		
		if(c.getString(c.getColumnIndex(DB.Table.event_master.user_master_id.toString())).equalsIgnoreCase(pref.get(Constants.KeyUUID))){
			
			row.put("propose_status", "2");
		}
		else{
			row.put("propose_status", "1");
		}
		
		
		
		String ttt= DB.Table.event_categories.id+" IN("+c.getString(c.getColumnIndex(DB.Table.event_master.event_category_id.toString()))+","+
		c.getString(c.getColumnIndex(DB.Table.event_master.event_subcategory_id
								.toString()))+")";
		
		Log.d("arv", "arv"+ttt);
		
		Cursor temp=db.findCursor(DB.Table.Name.event_categories,ttt ,null,null);
		
			if(temp.moveToFirst()){
			
			if(Integer.parseInt(temp.getString(temp
							.getColumnIndex(DB.Table.event_categories.category_id.toString())))>0){
				row.put(DB.Table.event_master.event_category_id.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.event_categories.category_name.toString())));
				temp.moveToNext();	
				row.put(DB.Table.event_master.event_subcategory_id.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.event_categories.category_name
										.toString())));
			}
			else{
				row.put(DB.Table.event_master.event_subcategory_id.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.event_categories.category_name.toString())));
				temp.moveToNext();	
				row.put(DB.Table.event_master.event_category_id.toString(),
						temp.getString(temp
								.getColumnIndex(DB.Table.event_categories.category_name.toString())));
			}
				
		}
			
		row.put(DB.Table.event_master.place.toString(),
				c.getString(c
						.getColumnIndex(DB.Table.event_master.place
								.toString()))+","+c.getString(c
								.getColumnIndex(DB.Table.event_master.city
										.toString())));
		
		
		
		row.put(DB.Table.event_master.event_datetime.toString(),
				c.getString(c
						.getColumnIndex(DB.Table.event_master.event_datetime
								.toString())));

		Log.v("hash map", row + "");
		return row;

	}
	
	public static HashMap<String, String> getBannerHashMap(){
		HashMap<String, String> row = new HashMap<String, String>();
		banner_position++;
		if(banner_position>=bannerUrls.size()){
			banner_position=0;
		}
		row.put("type",
				"1");
		if(bannerUrls.size()>banner_position){
		row.put("image",
				bannerUrls.get(banner_position));
		}
		else{
			row.put("image",
					"");
		}
		return row;
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
		try {
		if(page==1){
			eventsData.clear();
		}
			p.show();

			listView.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void loadPage(int page) {

					// TODO Auto-generated method stub
					Log.v("On Scroll Listener Load Page Called ====>>> ", ""
							+ page);
					if (getEventApi.currentPage1 > 0
							&& getEventApi.currentPage1 != 1) {
						hitGetEventsAPI(getEventApi.currentPage1 + 1,1);
						ShowEvents.this.loadPage1(page);
					}
					
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						getDataInBackground1();
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
	
	
	
	protected void callOnUpdaUI2(final int page) {
		// TODO Auto-generated method stub

		android.util.Log.v("on update UI", "----->>");

	//	mypostData = new ArrayList<HashMap<String, String>>();


		try {
			if(page==1){
			mypostData.clear();
			}
			p.show();

			mypost.setOnScrollListener(new EndlessScrollListener() {
				
				@Override
				public void loadPage(int page) {
					// TODO Auto-generated method stub
				
					Log.d("arvi", "arvi scrolling"+page);
					if (getEventApi.currentPage2 > 0) {
						hitGetEventsAPI(getEventApi.currentPage2 + 1,2);
						ShowEvents.this.loadPage2(page);
					}
					
				}
			});
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						getDataInBackground2();
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

	
		
		myPostAdapter.notifyDataSetChanged();
		p.cancel();
	}
	
	
	protected void callOnUpdaUI3() {
		// TODO Auto-generated method stub

		android.util.Log.v("on update UI", "----->>");

		//eventsData = new ArrayList<HashMap<String, String>>();

		getAllEventdata3();
		myDateAdapter = new EventAdapter(getActivity(), mydateData) {

			@Override
			protected void onItemClick(View v, int position) {
				// TODO Auto-generated method stub

				pref.set("clicked_event_id", position).commit();
				((DrawerActivity) getActivity()).displayView(34);
			}
		};

		mydate.setAdapter(myDateAdapter);
		myDateAdapter.notifyDataSetChanged();
		p.cancel();
	}

//	protected void same_id(String title, String message) {
//		
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				getActivity());
//
//		// set title
//		alertDialogBuilder.setTitle(title);
//
//		// set dialog message
//		alertDialogBuilder.setMessage(message).setCancelable(false)
//				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						// if this button is clicked, close
//						// current activity
//						dialog.cancel();
//						// UpdateProfile.this.finish();
//					}
//				});
//
//		// create alert dialog
//		AlertDialog alertDialog = alertDialogBuilder.create();
//
//		// show it
//		alertDialog.show();
//	}

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

	private void RulrForBtn(int id) {

		LayoutParams param;
		param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 8);

		if (id == findAPartyID) {

			// param.addRule(RelativeLayout.RIGHT_OF, eventMoreInfoId);
			param.addRule(RelativeLayout.LEFT_OF, myPartiesID);

		} else if (id == myPartiesID) {

			param.addRule(RelativeLayout.LEFT_OF, dateID);
			param.addRule(RelativeLayout.RIGHT_OF, findAPartyID);

		}
		
		else{
			if(id==dateID){
				param.addRule(RelativeLayout.RIGHT_OF, myPartiesID);
			}
		}

		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btview.setBackgroundColor(Color.parseColor("#0000FF"));
		btview.setLayoutParams(param);

	}

	// ========================================

	@Override
	public void onPause() {
		super.onPause();

		if (p != null)
			p.cancel();

	}


	
	
	void hitGetEventsAPI(final int page_index,final int type) {
		geteventapi = new getEventApi(getActivity(), db, p) {
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
						if(type==1){
							callOnUpdaUI1(page_index);
							}
							if(type==2){
								callOnUpdaUI2(page_index);
								}
							if(type==3){
								callOnUpdaUI3();
								}
					}
				});
			}
		};
		
		
	HashMap<String, String>data=new HashMap<String, String>();
	
	data.put("uuid", pref.get(Constants.KeyUUID));
	data.put("auth_token", pref.get(Constants.kAuthT));
	data.put("type", String.valueOf(type));
	data.put("page_index", String.valueOf(page_index));
	data.put("time_stamp", "");
	
	geteventapi.setPostData(data);	
		callApi(geteventapi);
	}
}