package com.ogbongefriends.com.ogbonge.how_about_we;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.DB.DB.Table.user_master;
import com.ogbongefriends.com.api.EventAct;
import com.ogbongefriends.com.api.EventCommentsApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.EndlessScrollListener;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class EventDetails extends Fragment implements Runnable,MyLocationListener {

	private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
		private LocationHelper mLocationHelper;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	
	private int points,item_points;
	private GridView user_of_city;
	private DisplayImageOptions options;
	private EventCommentsApi Event_comment_api; 
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	private ArrayList<HashMap<String, String>> data_map;
	
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;  //7800416090 8858486044
	Cursor placedatacursor;
	View rootView;
	private String url;
	private Button closeEvent;
	private static Button propose;
	private Button submit_button;
	private EditText comment;
	private DB db;
	private CustomLoader p;
	private ImageLoader imageLoader;
	Notification nt;
	int count = 0;
	public TextView event,desc,location,details,date,userName,age,location_user,last_seen;
	public ImageView userImage,rate1,rate2,rate3,rate4,rate5,gift,chat;
   private static Context _ctx; 
  public HashMap<String, String> urls;
  private JsonObject _jsondata;
  private int event_id;
  private ProgressBar pb;
  private EventAct Event_act;
  private CommentAdapter commentAdapter;
  private RelativeLayout propose_parent;
  private static String Eventtype="",Event_Closed="";
  private static Preferences pref;
  private user_profile_api user_profile_info;
  private ArrayList<HashMap<String, String>> commentData;
	public EventDetails(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		if(rootView==null){
			rootView = inflater.inflate(R.layout.eventitem, container, false);
		
		
		
			p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
			pref=new Preferences(_ctx);
			_jsondata=new JsonObject();
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			fragmentManager = getFragmentManager();
		p.show();
		
		event=(TextView)rootView.findViewById(R.id.event);
		desc=(TextView)rootView.findViewById(R.id.how_about_we_text);
		location=(TextView)rootView.findViewById(R.id.location);
		details=(TextView)rootView.findViewById(R.id.cate);
		date=(TextView)rootView.findViewById(R.id.dateText);
		userImage=(ImageView)rootView.findViewById(R.id.userImage);
		userName=(TextView)rootView.findViewById(R.id.userName);
		listView=(ListView)rootView.findViewById(R.id.comment_list);
		age=(TextView)rootView.findViewById(R.id.age);
		location_user=(TextView)rootView.findViewById(R.id.loc_name);
		last_seen=(TextView)rootView.findViewById(R.id.last_seen);
		pb=(ProgressBar)rootView.findViewById(R.id.userImageProgress);
		closeEvent=(Button)rootView.findViewById(R.id.close_event);
		propose=(Button)rootView.findViewById(R.id.propose);
		gift=(ImageView)rootView.findViewById(R.id.send_gift);
		propose_parent=(RelativeLayout)rootView.findViewById(R.id.propose_parent);
		submit_button=(Button)rootView.findViewById(R.id.submit_button);
		comment=(EditText)rootView.findViewById(R.id.comment);
		
	
		propose_parent.setVisibility(View.GONE);
		
		
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(comment.getText().length()>0){
					
					p.show();
					
					
					Event_comment_api=new EventCommentsApi(_ctx, db, p){

						@Override
						protected void onResponseReceived(InputStream is) {
							// TODO Auto-generated method stub
							super.onResponseReceived(is);
							if(Event_comment_api.resCode==1)
							{
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										
										propose_parent.setVisibility(View.GONE);
										comment.setText("");
										p.cancel();
										pref.set("clicked_event_id", event_id).commit();
										((DrawerActivity) getActivity()).displayView(34);
									}
								});
							}
							}

						@Override
						protected void updateUI() {
							// TODO Auto-generated method stub
							callOnUpdaUI();
							super.updateUI();
						}

						@Override
						protected void onDone() {
							// TODO Auto-generated method stub
							super.onDone();
							p.cancel();
						}
						 
					 };
					
					HashMap<String, String>dataact=new HashMap<String, String>();
					
					
					dataact.put("uuid", pref.get(Constants.KeyUUID));
					dataact.put("auth_token", pref.get(Constants.kAuthT));
					dataact.put("type", "1");
					dataact.put("event_master_id", String.valueOf(event_id));
					
					dataact.put("event_comment",comment.getText().toString());
					dataact.put("time_stamp", "");
					
					
					
					Event_comment_api.setPostData(dataact);	
						callApi(Event_comment_api);
					
				}
				else{
					Utils.same_id("Message", "Proposal box can't be empty", _ctx);
				}
			}
		});
		
		propose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				propose_parent.setVisibility(View.VISIBLE);
				
			}
		});
		
		
		closeEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				p.show();
				Event_act=new EventAct(_ctx, db, p){

					@Override
					protected void onResponseReceived(InputStream is) {
						// TODO Auto-generated method stub
						super.onResponseReceived(is);
						
						if(Event_act.resCode==1){
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								closeEvent.setEnabled(false);
								closeEvent.setBackgroundResource(R.drawable.closed_event);
								closeEvent.setText("Event closed");
								p.cancel();
							}
						});
							
							
						}
					}

					@Override
					protected void onDone() {
						// TODO Auto-generated method stub
						super.onDone();
						
//						closeEvent.setEnabled(false);
//						closeEvent.setBackgroundResource(R.color.grey);
//						closeEvent.setText("Event closed");
						p.cancel();
					}

					@Override
					protected void onError(Exception e) {
						// TODO Auto-generated method stub
						super.onError(e);
					}

					@Override
					protected void updateUI() {
						// TODO Auto-generated method stub
						super.updateUI();
					}
					
					
				};
				
				HashMap<String, String>dataact=new HashMap<String, String>();
				
				
				dataact.put("uuid", pref.get(Constants.KeyUUID));
				dataact.put("auth_token", pref.get(Constants.kAuthT));
				dataact.put("type", "2");
				dataact.put("event_master_id", String.valueOf(event_id));
				
				dataact.put("comment_master_id", "");
				
				Event_act.setPostData(dataact);	
					callApi(Event_act);
				
//				
//				 "uuid": "ob_541692f14a1a1",
//			     "auth_token": "GO1ykYnRA1Qc65ZtsxZerHA4kfhXKte2SAKEqhpTv1nhoZAm4w",
//			    "type": "1",
//			    "event_master_id": "51",
//			    "comment_master_id": "33"
				
			}
		});
		
		user_profile_info=new user_profile_api(getActivity(), db, p){

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
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				 getEventData(event_id);
				super.updateUI();
			//	
			}
	    	 
	     };
		
		commentData = new ArrayList<HashMap<String, String>>();
		

		commentAdapter = new CommentAdapter(getActivity(), commentData) {

			@Override
			protected void onItemClick(View v, int event_id,int comment_id) {
				// TODO Auto-generated method stub
				// if (Constants.otherUserProfile) {

				// edit.putLong("other_user", str).commit();
				// } else {

				// }
				
				((DrawerActivity) getActivity()).displayView(37);
			}
		};
		
		listView.setAdapter(commentAdapter);
		
		 File cacheDir = StorageUtils.getCacheDirectory(_ctx);
	     options = new DisplayImageOptions.Builder()
	     .displayer(new RoundedBitmapDisplayer(15))
	        .resetViewBeforeLoading(false)  // default
	        .delayBeforeLoading(1000)
	        .cacheInMemory(true) // default
	        .cacheOnDisk(true) // default
	        .considerExifParams(false) // default
	        .build();
	  
	  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(_ctx)
				
				
				
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(26) // default
		        .diskCacheSize(1000 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .imageDownloader(new BaseImageDownloader(_ctx)) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(options) // default
		        .writeDebugLogs()
		        .build();
				
			
	 
				
				ImageLoader.getInstance().init(config);
					
				 imageLoader = ImageLoader.getInstance();
		
		rate1=(ImageView)rootView.findViewById(R.id.rate1);
		rate2=(ImageView)rootView.findViewById(R.id.rate2);
		rate3=(ImageView)rootView.findViewById(R.id.rate3);
		rate4=(ImageView)rootView.findViewById(R.id.rate4);
		rate5=(ImageView)rootView.findViewById(R.id.rate5);
		
		
		
		
//		manageLocation();
	 event_id=pref.getInt("clicked_event_id");
	 
	  Cursor getuserdata=db.findCursor(Table.Name.event_master, Table.event_master.id.toString()+" = "+event_id, null, null);
	  getuserdata.moveToFirst();
	 
	 
	 HashMap<String, String>map=new HashMap<String, String>();
     map.put("uuid",pref.get(Constants.KeyUUID));
     map.put("auth_token", pref.get(Constants.kAuthT));
     map.put("other_user_uuid", getuserdata.getString(getuserdata.getColumnIndex(Table.event_master.user_master_id.toString())));
     map.put("time_stamp", "");  
     user_profile_info.setPostData(map);
     
     
     
     callApi(user_profile_info);
	 
	 
	
		
	
			 

			
			 
		
		
		}
	return 	rootView;
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	

	
	
	
	
	public void getEventData(int eventId){
		
		final Cursor data=db.findCursor(Table.Name.event_master, Table.event_master.id.toString()+" = "+eventId, null, null);
		data.moveToFirst();
		
		if(data.getString(data.getColumnIndex(Table.event_master.status.toString())).equalsIgnoreCase("2")){
			closeEvent.setEnabled(false);
			closeEvent.setBackgroundResource(R.drawable.closed_event);
			closeEvent.setText("Event closed");
			propose.setVisibility(View.GONE);
			Event_Closed="2";
		}
		else{
			
			Event_Closed="1";
		}
		
		//event.setText(data.getString(data.getColumnIndex(Table.event_master.eve)))
		desc.setText(data.getString(data.getColumnIndex(Table.event_master.event_description.toString())));
		location.setText(data.getString(data.getColumnIndex(Table.event_master.place.toString()))+","+data.getString(data.getColumnIndex(Table.event_master.city.toString())));    
		date.setText(CelUtils.getDateFromTimestamp(Long.parseLong(data.getString(data.getColumnIndex(Table.event_master.event_datetime.toString())))));
		
		
		
		
		String ttt=Table.event_categories.id+" IN("+data.getString(data.getColumnIndex(Table.event_master.event_category_id.toString()))+","+
				data.getString(data.getColumnIndex(Table.event_master.event_subcategory_id
										.toString()))+")";
				
				Log.d("arv", "arv"+ttt);
				
				Cursor temp=db.findCursor(Table.Name.event_categories,ttt ,null,null);
				
					if(temp.moveToFirst()){
					
					if(Integer.parseInt(temp.getString(temp
									.getColumnIndex(Table.event_categories.category_id.toString())))>0){
						event.setText(temp.getString(temp
								.getColumnIndex(Table.event_categories.category_name
										.toString())));
						temp.moveToNext();
						
						details.setText(temp.getString(temp
								.getColumnIndex(Table.event_categories.category_name.toString())));
					
					
					
					
					}
					else{
						
						
						details.setText(temp.getString(temp
								.getColumnIndex(Table.event_categories.category_name
										.toString())));
						temp.moveToNext();
						
						event.setText(temp.getString(temp
								.getColumnIndex(Table.event_categories.category_name.toString())));
					}
					}
					
					gift.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							pref.set(Constants.OtherUser, data.getString(data.getColumnIndex(Table.event_master.user_master_id.toString())));
							pref.commit();
							((DrawerActivity) getActivity()).displayView(47);	
						}
					});	
					
					
			if(!data.getString(data.getColumnIndex(Table.event_master.user_master_id.toString())).equalsIgnoreCase(pref.get(Constants.KeyUUID))){
			if(Event_Closed.equalsIgnoreCase("2")){
				propose.setVisibility(View.GONE);
			}else{
				propose.setVisibility(View.VISIBLE);
			}
				
				closeEvent.setVisibility(View.GONE);
				gift.setVisibility(View.VISIBLE);
				Eventtype="3";
			}
			else{
				propose.setVisibility(View.GONE);
				closeEvent.setVisibility(View.VISIBLE);
				gift.setVisibility(View.GONE);
				Eventtype="2";
			}
					
	final Cursor	temp_user=db.findCursor(Table.Name.user_master, Table.user_master.uuid.toString()+" = '"+data.getString(data.getColumnIndex(Table.event_master.user_master_id.toString()))+"'", null, null);			
	Log.d("arv", "arv"+temp_user.getCount());
	
	
	temp_user.moveToFirst();
	Log.d("arv", "arv First"+temp_user.getString(temp_user.getColumnIndex(Table.user_master.first_name.toString())));
	Log.d("arv", "arv Rirst "+temp_user.getString(temp_user.getColumnIndex(Table.user_master.last_name.toString())));
	
	
	
	userImage.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			pref.set(Constants.OtherUser,data.getString(data.getColumnIndex(Table.event_master.user_master_id.toString())));
			pref.commit();
			((DrawerActivity) getActivity()).displayView(46);	
		}
	});
	
	gift.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pref.set(Constants.OtherUser, data.getString(data.getColumnIndex(Table.event_master.user_master_id.toString())));
			pref.set(Constants.OtherUserName, temp_user.getString(temp_user.getColumnIndex(Table.user_master.first_name.toString()))+" "+temp_user.getString(temp_user.getColumnIndex(Table.user_master.last_name.toString())));
			pref.commit();
			((DrawerActivity) getActivity()).displayView(47);	
		}
	});	
	
	userName.setText(temp_user.getString(temp_user.getColumnIndex(Table.user_master.first_name.toString()))+" "
	+temp_user.getString(temp_user.getColumnIndex(Table.user_master.last_name.toString()))+", "
			+CelUtils.getAgeStrFromDOB(temp_user.getString(temp_user.getColumnIndex(user_master.date_of_birth.toString())) ));
	location_user.setText(temp_user.getString(temp_user.getColumnIndex(Table.user_master.location.toString())));
	last_seen.setText(temp_user.getString(temp_user.getColumnIndex(Table.user_master.last_seen.toString())));
	
	url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp_user.getString(temp_user.getColumnIndex(Table.user_master.server_id.toString()))+"/photos_of_you/"+temp_user.getString(temp_user.getColumnIndex(Table.user_master.profile_pic.toString()));
			
			
	
	imageLoader.displayImage(url, userImage,options, new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
		}
		
		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
		}
		
		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
		}
	});
	
	hitGetEventsAPI(1);
	}
	
	
	void hitGetEventsAPI(int page) {
	
	Event_comment_api=new EventCommentsApi(_ctx, db, p){

		@Override
		protected void onResponseReceived(InputStream is) {
			// TODO Auto-generated method stub
			super.onResponseReceived(is);
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			callOnUpdaUI();
			super.updateUI();
		}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
			p.cancel();
		}
		 
	 };
	  
	 
	 HashMap<String, String>mapData=new HashMap<String, String>();
	 mapData.put("uuid", pref.get(Constants.KeyUUID));
	 mapData.put("auth_token", pref.get(Constants.kAuthT));
	 mapData.put("type", Eventtype);
	 mapData.put("event_master_id", String.valueOf(event_id));
	 mapData.put("time_stamp", "");
	 mapData.put("event_comment", "");
	 
	  Event_comment_api.setPostData(mapData);
	  callApi(Event_comment_api);
	
	}
	
	
	
	protected void getDataInBackground(int type) {
		// TODO Auto-generated method stub
		String sql="";
		
//		if(!Eventtype.equalsIgnoreCase("2")){
		if(Eventtype.equalsIgnoreCase("2")){
		sql="SELECT * from event_comment where event_master_id = "+event_id;		
		}
		
		else{
				sql="SELECT * from event_comment where event_master_id = "+event_id+" AND "+Table.event_comment.user_master_id+" = "+"'"+ pref.get(Constants.KeyUUID) +"'";		
				
		}
		
		db.open();
		data = db.findCursor(sql, null);
		loadPage(0, type);

	}
	
	private synchronized void loadPage(int page, int type) {
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

					///while (status && i < NUM_OF_ROWS_PER_PAGE) {
					while (status) {
						commentData.add(getEventHashMap(db, data));
						i++;
						status = data.moveToNext();

					}

					commentAdapter.notifyDataSetChanged();

					if (commentAdapter.getCount() > 0) {
					//	empty.setVisibility(View.GONE);
					} else {
				//		empty.setVisibility(View.VISIBLE);
					}

				}
			});

		} catch (Exception e) {

		}

	}
	
	
	public static HashMap<String, String> getEventHashMap(DB db, Cursor c) {

		
		HashMap<String, String> row = new HashMap<String, String>();
		
		row.put(Table.event_comment.event_master_id.toString(), c.getString(c
				.getColumnIndex(Table.event_comment.event_master_id.toString())));
		
		row.put(Table.event_comment.id.toString(), c.getString(c
				.getColumnIndex(Table.event_comment.id.toString())));
		
		
		row.put("event_closed_status", Event_Closed);
		
		
		row.put(Table.event_comment.status.toString(), c.getString(c
				.getColumnIndex(Table.event_comment.status.toString())));

		row.put(Table.event_comment.event_comment.toString(),
				c.getString(c
						.getColumnIndex(Table.event_comment.event_comment
								.toString())));

		row.put(Table.event_comment.add_date.toString(),
				c.getString(c
						.getColumnIndex(Table.event_comment.add_date
								.toString())));
		if(c.getString(c
				.getColumnIndex(Table.event_comment.user_master_id
						.toString())).equalsIgnoreCase(pref.get(Constants.KeyUUID))){
			propose.setVisibility(View.GONE);
			
		}
		
		
		Cursor tepmcursor = db.findCursor(Table.Name.user_master,
				Table.user_master.uuid + " = " +"'"+c.getString(c
						.getColumnIndex(Table.event_comment.user_master_id
								.toString()))+"'", null, null);

		if (tepmcursor != null && tepmcursor.moveToFirst()) {
			row.put(Table.user_master.profile_pic.toString(),
					_ctx.getString(R.string.urlString)+"userdata/image_gallery/"+tepmcursor.getString(tepmcursor.getColumnIndex(Table.user_master.server_id.toString()))+"/photos_of_you/"+
							tepmcursor.getString(tepmcursor.getColumnIndex(Table.user_master.profile_pic.toString())));
		
			row.put(Table.user_master.first_name.toString(),
					tepmcursor.getString(tepmcursor.getColumnIndex(Table.user_master.first_name.toString()))+" "+
							tepmcursor.getString(tepmcursor.getColumnIndex(Table.user_master.last_name.toString()))+" "+CelUtils.getAgeStrFromDOB(tepmcursor.getString(tepmcursor
									.getColumnIndex(user_master.date_of_birth.toString()))));
			
			row.put(Table.user_master.uuid.toString(),
					tepmcursor.getString(tepmcursor.getColumnIndex(Table.user_master.uuid.toString())));
			
			if(Eventtype.equalsIgnoreCase("2")){
				
			}
		}
				Log.v("hash map", row + "");

		return row;
	}
	protected void callOnUpdaUI() {
		// TODO Auto-generated method stub

		android.util.Log.v("on update UI", "----->>");
		commentData = new ArrayList<HashMap<String, String>>();
		getAllEventdata(1);
		commentAdapter = new CommentAdapter(getActivity(), commentData) {
			@Override
			protected void onItemClick(View v, int event_id, int comment_id) {
				// TODO Auto-generated method stub
				AcceptComment(event_id, comment_id);
				//((DrwaerActivity) getActivity()).displayView(61);
			}
		};

		listView.setAdapter(commentAdapter);
		commentAdapter.notifyDataSetChanged();
		}
	
	
public void	AcceptComment(int eve_id,int com_id){
	
	Event_act=new EventAct(_ctx, db, p){

		@Override
		protected void onResponseReceived(InputStream is) {
			// TODO Auto-generated method stub
			super.onResponseReceived(is);
			
			if(Event_act.resCode==1){
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
//					closeEvent.setEnabled(false);
//					closeEvent.setBackgroundResource(R.color.grey);
//					closeEvent.setText("Event closed");
					Utils.same_id("Message", EventAct.resMsg, _ctx);
					
					p.cancel();
					
					pref.set("clicked_event_id", event_id).commit();
					((DrawerActivity) getActivity()).displayView(34);
				}
			});
				
				
			}
		}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
			p.cancel();
		}

		@Override
		protected void onError(Exception e) {
			// TODO Auto-generated method stub
			super.onError(e);
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			super.updateUI();
		}
		
		
	};
	
	HashMap<String, String>dataact=new HashMap<String, String>();
	
	
	dataact.put("uuid", pref.get(Constants.KeyUUID));
	dataact.put("auth_token", pref.get(Constants.kAuthT));
	dataact.put("type", "1");
	dataact.put("event_master_id", String.valueOf(eve_id));
	
	dataact.put("comment_master_id", String.valueOf(com_id));
	
	
	Event_act.setPostData(dataact);	
		callApi(Event_act);
	
}
	
	
	private void getAllEventdata(final int type) {
		try {
			commentData.clear();
			p.show();

			listView.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void loadPage(int page) {
					// TODO Auto-generated method stub
					Log.v("On Scroll Listener Load Page Called ====>>> ", ""
							+ page);
//					if (getEventApi.currentPage > 0
//							&& getEventApi.currentPage != 1) {
//						hitGetEventsAPI(getEventApi.currentPage + 1);
//					}
					EventDetails.this.loadPage(page, type);
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						getDataInBackground(type);
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

	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}
	
}




