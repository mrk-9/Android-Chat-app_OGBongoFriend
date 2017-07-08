package com.ogbongefriends.com.ogbonge.search;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.fragment.searchApi;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.confirm_pointsApi;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.api.getTransactionApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi") public class Custom_search extends Fragment implements Runnable,MyLocationListener {

	private ArrayList<HashMap<String, String>> feedData;
	private ArrayList<HashMap<String, String>> placeData;
	private ArrayList<HashMap<String, String>> eventsData;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;
	private Uri selectedImage;
	private long str;
	private int category_type=1,search_page_index=1;
	private LocationHelper mLocationHelper;
	Cursor data;
	private ImageView promote_yourself;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;
	private LinearLayout ll_user;
	private GridView user_of_city;
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	private ArrayList<HashMap<String, String>> data_map;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	Cursor placedatacursor;
	View rootView;
	private CheckBox ch1,ch2,ch3,ch4,ch5,ch6;
	private EditText search_key;
	private ImageView search_btn;
	private DB db;
	private int current_user=0;
	private TextView textView1;
	private searchApi searchApi;
	private CustomLoader p;
	private ImageLoader imageLoader;
	Notification nt;
	private ImageView filter;
	private ListView poplist;
	private LinearLayout search_layout;
	private RelativeLayout popup_layout;
	private Button filter_btn;
	String[] listContent = { "Phone-No", "Email", "Full Name"};
	private TextView nodata_found;
	private PullToRefreshGridView mPullRefreshGridView;
	private Gridview_adapter gridViewAdapter;
	private getAllUserProfileAPI get_all_user_pfro_api;
	static LinearLayout.LayoutParams lp;
	private user_profile_api user_profile_info;
	public JsonObject _jsondata;
	private int points,item_points;
	private confirm_pointsApi confirm_points_api;
	private getTransactionApi get_transaction_api;
	// getEventApi geteventapi;
	int count = 0;
//	public static int [] prgmImages={R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.profile,
//		R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.profile};
   private Context _ctx; 
  public HashMap<String, String> urls;
	
	
	public Custom_search(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView==null){
		 
			rootView = inflater.inflate(R.layout.search, container, false);
			p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(getActivity());
			_jsondata=new JsonObject();
		
		
			promote_yourself=(ImageView)rootView.findViewById(R.id.promote_yourself);
		mPullRefreshGridView=(PullToRefreshGridView)rootView.findViewById(R.id.user_of_city);
		user_of_city=mPullRefreshGridView.getRefreshableView();
		//user_of_city=(GridView)rootView.findViewById(R.id.user_of_city);
		textView1=(TextView)rootView.findViewById(R.id.title);
		textView1.setText("Custom Search");
		poplist   = (ListView) rootView.findViewById(R.id.custom_search_option_list);
		search_key=(EditText)rootView.findViewById(R.id.key_to_search);
		search_btn=(ImageView)rootView.findViewById(R.id.search_btn);
		filter=(ImageView)rootView.findViewById(R.id.filter);
		popup_layout=(RelativeLayout)rootView.findViewById(R.id.popuplaout);
		nodata_found=(TextView)rootView.findViewById(R.id.nodata_text);
		nodata_found.setVisibility(View.GONE);
		search_layout=(LinearLayout)rootView.findViewById(R.id.search_layout);
		popup_layout.setVisibility(View.GONE);
		ll_user=(LinearLayout)rootView.findViewById(R.id.parent_user_images);
		filter_btn=(Button)rootView.findViewById(R.id.filter_btn);
		data_map=new ArrayList<HashMap<String,String>>();
		
		
		promote_yourself.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DrawerActivity) getActivity()).displayView(93);
			}
		});
		
		
		filter_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(search_layout.getVisibility()==View.VISIBLE){
					search_layout.setVisibility(View.GONE);
				}
				else{
					search_layout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(popup_layout.getVisibility()==View.VISIBLE){
					popup_layout.setVisibility(View.GONE);
				}
				else{
					popup_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		pref=new Preferences(getActivity());
		manageLocation();
	//	Log.d("MSG", "MSG "+pref.getInt(Constants.Custom_search_type));
		if(pref.get(Constants.Custom_search_type).toString().length()>0){
		if(Integer.parseInt(pref.get(Constants.Custom_search_type))>0){
			poplist.setSelection(Integer.parseInt(pref.get(Constants.Custom_search_type))-1);
			category_type=Integer.parseInt(pref.get(Constants.Custom_search_type));
		}
		}
		File cacheDir = StorageUtils.getCacheDirectory(getActivity());
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
				
				
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .diskCacheSize(50 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .imageDownloader(new BaseImageDownloader(getActivity())) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
				
				DisplayImageOptions options = new DisplayImageOptions.Builder()
		        .resetViewBeforeLoading(false)  // default
		        .delayBeforeLoading(1000)
		        .cacheInMemory(false) // default
		        .cacheOnDisk(false) // default
		        .considerExifParams(false) // default
		        .build();
				
				ImageLoader.getInstance().init(config);
				
				search_btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(checkPass()){
						if(search_key.getText().toString().length()>0){
						pref.set(Constants.Custom_search_key, search_key.getText().toString());
						pref.set(Constants.Custom_search_type, String.valueOf(category_type));
						//pref.set(Constants.Custom_search_type, category_type);
						pref.commit();
						p.show();
						search_page_index=1;
						data_map.clear();
							get_resulted_data(search_page_index);
						}
					}
					else{
						
						getPointInfo();
					}
					}
				});
				
			 imageLoader = ImageLoader.getInstance();
			
			 gridViewAdapter=new Gridview_adapter(_ctx, data_map)
				{

					@Override
					protected void onItemClick(View v, String string) {
						// TODO Auto-generated method stub
						pref.set(Constants.OtherUser,string );
						pref.commit();
						//((DrawerActivity) getActivity()).displayView(46);	
						((DrawerActivity) getActivity()).displayView(37);
					}
					
				};
				
				 mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// TODO Auto-generated method stub
							Log.d("arvi", "arvi  DOWN");
							mPullRefreshGridView.onRefreshComplete();
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// TODO Auto-generated method stub
							Log.d("arvi", "arvi  UP");
							if(search_page_index>0){
								
								get_resulted_data(search_page_index);
							}
							mPullRefreshGridView.onRefreshComplete();
							mPullRefreshGridView.getRefreshableView().setSelection(data_map.size());
						}

					});
			
				 
				 
				 
				 user_of_city.setAdapter(gridViewAdapter);

				int screenSize = getResources().getConfiguration().screenLayout &
				        Configuration.SCREENLAYOUT_SIZE_MASK;

				String toastMsg;
				switch(screenSize) {
				    case Configuration.SCREENLAYOUT_SIZE_LARGE:
				        toastMsg = "Large screen";
				        
				        user_of_city.setNumColumns(3);
				        
				        break;
				    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				        toastMsg = "Normal screen";
				        
				        user_of_city.setNumColumns(2);
				        break;
				    case Configuration.SCREENLAYOUT_SIZE_SMALL:
				        toastMsg = "Small screen";
				        break;
				    default:
				        toastMsg = "Screen size is neither large, normal or small";
				}
				
				
			 user_of_city.setAdapter(gridViewAdapter);
			 
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(_ctx,R.layout.popuplist_item, listContent);
				poplist.setAdapter(adapter);
		
		
				poplist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						
						category_type=position+1;
						popup_layout.setVisibility(View.GONE);
					}
				});
		
				popup_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(popup_layout.getVisibility()==View.VISIBLE){
							popup_layout.setVisibility(View.GONE);
						}
						else{
							popup_layout.setVisibility(View.VISIBLE);
						}
					}
				});
				
				 get_transaction_api=new getTransactionApi(_ctx, db, p){
						@Override
						protected void onError(Exception e) {
							// TODO Auto-generated method stub
							super.onError(e);
						}
						@Override
						protected void onDone() {
							// TODO Auto-generated method stub
							super.onDone();
							if(get_transaction_api.resCode==1){
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										getUpdatedUserProfile();
									}
								});
								
								
								
							}
							else{
							}
						}
						@Override
						protected void onResponseReceived(InputStream is) {
							// TODO Auto-generated method stub
							super.onResponseReceived(is);
						}

						@Override
						protected void updateUI() {
							// TODO Auto-generated method stub
							super.updateUI();
						
						}
						
					};
				
				
//		View header = inflater.inflate(R.layout.profile_hadder, null);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// ============= manage image height================

		fragmentManager = getFragmentManager();
	
		// ==================================================

		/*pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		edit = pref.edit();*/
		
		getAllUsersFroBanner();
		}
	return 	rootView;
		

	}

	
	
	public void getPointInfo(){
		confirm_points_api=new confirm_pointsApi(_ctx, db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				
				getActivity().runOnUiThread(new Runnable() {
					  public void run() {
						  showPrompt(_jsondata);
					  }
					});
				
				
			}

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				
				_jsondata=confirm_pointsApi.jsondata;
				
				
				
				
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
	     map.put("other_uuid","");  
	     map.put("time_stamp",""); 
	     map.put("type","1"); 
	    
	   
	     confirm_points_api.setPostData(map);
	     callApi(confirm_points_api);
		
	}
	
	
public void	showPrompt(JsonObject jsn){
		
		Log.d("arv", "arv"+jsn.get("points").getAsString()+"  "+jsn.get("item_points").getAsString());
		points=Integer.parseInt(jsn.get("points").getAsString());
		item_points=Integer.parseInt(jsn.get("item_points").getAsString());
		
		if(points>item_points){
		
		

			final Dialog dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
            dialog.setContentView(R.layout.confirmation_dialog);
           TextView title=(TextView)dialog.findViewById(R.id.title);
           TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			title.setText(getString(R.string.app_name));
			String str="You have "+points+ " points,"+ item_points +"points will be deducted from your account";
			msg.setText(str);
            dialog.show();
            cancel_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
            
            ok_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					 HashMap<String, String>map=new HashMap<String, String>();
				     map.put("uuid", pref.get(Constants.KeyUUID));
				     map.put("auth_token", pref.get(Constants.kAuthT));
				     map.put("other_uuid","");  
				     map.put("time_stamp",""); 
				     map.put("type","1"); 
				
				     
				     get_transaction_api.setPostData(map);
				     callApi(get_transaction_api);
				}
			});
			
				// show it
            dialog.show();
		
		}
		
		else{
			
			
			
			final Dialog dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
            dialog.setContentView(R.layout.confirmation_dialog);
           TextView title=(TextView)dialog.findViewById(R.id.title);
           TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			title.setText(getString(R.string.app_name));
			String str="You do not have sufficient points in your account. Would you like to purchase more points ?.";
			
			msg.setText(str);
            dialog.show();
            cancel_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
            
            ok_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					((DrawerActivity) getActivity()).displayView(11);
				    
				}
			});
			
				// show it
            dialog.show();
			
				}
		

	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		poplist.setVisibility(View.GONE);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		poplist.setVisibility(View.GONE);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		poplist.setVisibility(View.GONE);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	
	
	
	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	
	
	
	private void getUpdatedUserProfile(){
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
				
				getActivity().runOnUiThread(new Runnable() {
					  public void run() {
						  showuserForBanner(get_all_user_pfro_api.getUserdata());
					  }
					});
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
	
	
private boolean checkPass(){
	Calendar cal=Calendar.getInstance();
	db.open();
	Cursor pass_data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" == '"+pref.get(Constants.KeyUUID)+"'", null, null);
	pass_data.moveToFirst();
	if(Long.parseLong(pass_data.getString(pass_data.getColumnIndex(DB.Table.user_master.pass_7days_expiry_time.toString())))>(cal.getTimeInMillis()/1000))
	{
	return true;	
	}
	else{
		return false;
	}
	
	
}
	
	
	
public void get_resulted_data(int page_index){
		
	
searchApi=new searchApi(_ctx, db, p){

		@Override
		protected void onError(Exception e) {
			// TODO Auto-generated method stub
			super.onError(e);
		}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
			p.cancel();
		}

		@Override
		protected void onResponseReceived(InputStream is) {
			// TODO Auto-generated method stub
			super.onResponseReceived(is);
			searchedata=searchApi.userdata;
			search_page_index=searchApi.page_index;
			Log.d("JSON Array",String.valueOf(searchApi.userdata));
			Log.d("JSON Array",String.valueOf(searchedata));
			
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			super.updateUI();
			showuser(searchedata);
		}
		
	};
	
	
	if(pref.get(Constants.Custom_search_key).toString().length()>0){
	 HashMap<String, String>map=new HashMap<String, String>();
     map.put("uuid", pref.get(Constants.KeyUUID));
     map.put("auth_token", pref.get(Constants.kAuthT));
     map.put("category",String.valueOf(category_type));  
     map.put("keyword",pref.get(Constants.Custom_search_key).toString()); 
     map.put("time_stamp",""); 
     map.put("type","1"); 
     map.put("page_index",String.valueOf(page_index)); 
     searchApi.setPostData(map);
     callApi(searchApi);
	}
}


private void showuserForBanner(final JsonArray jsnData){
	
	for(int i=0;i<jsnData.size();i++){
	JsonObject obj=jsnData.get(i).getAsJsonObject();
		
	
	if(obj.get(DB.Table.user_master.profile_pic.toString()).getAsString().length()>3){
		RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams topbar_param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 30);
		
		RelativeLayout parent = new RelativeLayout(getActivity());
		
		
		RelativeLayout top_bar = new RelativeLayout(getActivity());
		top_bar.setBackgroundResource(R.drawable.top_element);
		
		
		parent.setLayoutParams(parent_param);
			 
		 if(i==0){
			 parent.setBackgroundColor(Color.DKGRAY);
		 }
		 
	parent.setTag(obj.get(DB.Table.user_master.uuid.toString()).getAsString());
	parent.setTag(R.id.account_setting_text, i);
	parent.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			///loadUser(v,(Integer) v.getTag(R.id.account_setting_text));
			current_user=(Integer) v.getTag(R.id.account_setting_text);
			//data.moveToPosition(current_user);
			pref.set(Constants.OtherUser,(jsnData.get(current_user).getAsJsonObject().get(DB.Table.user_master.uuid.toString()).getAsString()));
			pref.commit();
			((DrawerActivity) getActivity()).displayView(37);	
			
			
		}
	});
		String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+obj.get(DB.Table.user_master.id.toString()).getAsString()+"/photos_of_you/"+obj.get(DB.Table.user_master.profile_pic.toString()).getAsString();
		ImageView imagView = new ImageView(getActivity());
		imagView.setScaleType(ScaleType.FIT_XY);
		ll_user.setOrientation(LinearLayout.HORIZONTAL);
		//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
		

		int screenSize = getResources().getConfiguration().screenLayout &
		        Configuration.SCREENLAYOUT_SIZE_MASK;

		String toastMsg;
		switch(screenSize) {
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		        toastMsg = "Large screen";
		        
		        lp = new LinearLayout.LayoutParams(190, LayoutParams.FILL_PARENT);
		        
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		        toastMsg = "Normal screen";
		        
		        lp = new LinearLayout.LayoutParams(150, LayoutParams.FILL_PARENT);
		        lp.setMargins(5, 0, 0, 0);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		        toastMsg = "Small screen";
		        break;
		    default:
		        toastMsg = "Screen size is neither large, normal or small";
		}
		
		

		    imagView.setLayoutParams(lp);
		    ProgressBar p=new ProgressBar(_ctx);
		    p.setLayoutParams(lp);
		    imagView.setTag(url);
		    imagView.setImageResource(R.drawable.profile);
		 //   imageLoader.displayImage(url, imagView);
		    
		    imageLoader.displayImage(url, imagView, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					//if(i==data.getCount()-1){
					//loading.setVisibility(View.GONE);
				//	}
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
				//	loading.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
				//	loading.setVisibility(View.GONE);
				}
			});

		RelativeLayout.LayoutParams online_param = new RelativeLayout.LayoutParams(15, 15);
		online_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		    ImageView online_image = new ImageView(getActivity());
		    online_image.setLayoutParams(online_param);
		    online_image.setImageResource(R.drawable.online_dot);
		    TextView tv = new TextView(getActivity());
		    RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		    tv.setText(obj.get(DB.Table.user_master.first_name.toString()).getAsString());
		    tv.setTextColor(Color.WHITE);
		    tv.setLayoutParams(text_param);
		    tv.setGravity(Gravity.CENTER_HORIZONTAL);
		    
		    parent.addView(imagView);
		    parent.addView(top_bar, topbar_param);
		    parent.addView(tv);
		   // parent.addView(online_image);
		    
		   
		    ll_user.addView(parent);
		    
	}
	
	}
	p.cancel();
}





public void getAllUsersFroBanner(){
	
	

	
	get_all_user_pfro_api=new getAllUserProfileAPI(getActivity(), db, p){

		@Override
		protected void onError(Exception e) {
			// TODO Auto-generated method stub
			super.onError(e);
			}

		@Override
		protected void onDone() {
			// TODO Auto-generated method stub
			super.onDone();
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(get_all_user_pfro_api.getUserdata().size()>0){
						
						getUpdatedUserProfile();
						//get_resulted_data(search_page_index);
						
					}
				}
			});
		}

		@Override
		protected void updateUI() {
			// TODO Auto-generated method stub
			super.updateUI();
			//if(get_all_user_pfro_api.getUserdata()!=null){
			
		}
		
	};
	
	
	 HashMap<String, String>map=new HashMap<String, String>();
     map.put("uuid", pref.get(Constants.KeyUUID));
     map.put("auth_token", pref.get(Constants.kAuthT));
     map.put("time_stamp", "");  
     map.put("type", "6"); 
     map.put("distance", "2");  
     map.put("page_index", "1"); 
     Log.d("arv", ""+mLocationHelper.getMyLocation());
     if (mLocationHelper.getMyLocation() == null) {
    	 map.put("latitude","1234"); 
	     map.put("longitude", "1234"); 
		}
     else{
    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
					.getLatitude())); 
	     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
					.getLongitude())); 
     }
    
     
     get_all_user_pfro_api.setPostData(map);
     callApi(get_all_user_pfro_api);

	
     
//	
//	get_all_user_pfro_api=new getAllUserProfileAPI(getActivity(), db, p){
//
//		@Override
//		protected void onError(Exception e) {
//			// TODO Auto-generated method stub
//			super.onError(e);
//		}
//
//		@Override
//		protected void onDone() {
//			// TODO Auto-generated method stub
//			super.onDone();
//		}
//
//		@Override
//		protected void updateUI() {
//			// TODO Auto-generated method stub
//			super.updateUI();
//			
//			showuserForBanner();
//			
//			
//			
//		}
//		
//	};
//	
//	
//	 HashMap<String, String>map=new HashMap<String, String>();
//     map.put("uuid", pref.get(Constants.KeyUUID));
//     map.put("auth_token", pref.get(Constants.kAuthT));
//     map.put("time_stamp", "");  
//     map.put("type", "1"); 
//     map.put("page_index", "1"); 
//     
//     Log.d("arv", ""+mLocationHelper.getMyLocation());
//     
//     if (mLocationHelper.getMyLocation() == null) {
//    	 map.put("latitude","1234"); 
//	     map.put("longitude", "1234"); 
//		}
//     else{
//    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
//					.getLatitude())); 
//	     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
//					.getLongitude())); 
//     }
//    
//     
//     get_all_user_pfro_api.setPostData(map);
//     callApi(get_all_user_pfro_api);
}




private void showuser(JsonArray usersData){
// TODO Auto-generated method stub
	
		if(usersData.size()>0){
		
			nodata_found.setVisibility(View.GONE);
for(int i=0; i<usersData.size();i++){
	
	JsonObject form = usersData.get(i).getAsJsonObject();
	urls=new HashMap<String, String>();
	if(!(form.get(DB.Table.user_master.profile_pic.toString()).getAsString().equals(""))){
		String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+form.get(DB.Table.user_master.id.toString()).getAsString()+"/photos_of_you/"+form.get(DB.Table.user_master.profile_pic.toString()).getAsString();
			
		urls.put("Image_url", url);
		urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
		urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
}
	else{
		urls.put("Image_url","");
		urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
		urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
	}
	urls.put("photos", form.get("photos").getAsString());
	urls.put(DB.Table.user_master.rating.toString(), form.get(DB.Table.user_master.rating.toString()).getAsString());
	urls.put(DB.Table.user_master.rate_count.toString(), form.get(DB.Table.user_master.rate_count.toString()).getAsString());
	urls.put(DB.Table.user_master.city.toString(), form.get(DB.Table.user_master.city.toString()).getAsString());
	urls.put(DB.Table.user_master.verification_level.toString(), form.get(DB.Table.user_master.verification_level.toString()).getAsString());
	urls.put(DB.Table.user_master.login_status.toString(), form.get(DB.Table.user_master.login_status.toString()).getAsString());
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//df.setTimeZone(TimeZone.getTimeZone("EDT"));
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date timestamp = null;
    
	if(form.get(DB.Table.user_master.last_seen.toString()).getAsString().length()>0){
	    try {
			timestamp = df.parse(form.get(DB.Table.user_master.last_seen.toString()).getAsString());
			timestamp.setHours(timestamp.getHours()+4);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    timestamp.getTime();
	    
	    String parsed = df.format(timestamp);
	    long tim = 0;
	    try {
		 tim=df.parse(parsed).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.println("New = " + parsed);
	    long millis = 
	    Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
	    Log.d("ARVI", ""+(millis-tim)/(1000*60)+"min");
	    
	    urls.put(DB.Table.user_master.last_seen.toString(), String.valueOf((millis-tim)));
	    
}
else{
	 urls.put(DB.Table.user_master.last_seen.toString(), "");
}
	
	
	
	
	
	data_map.add(urls);
	
}
nodata_found.setVisibility(View.GONE);
	
	
		}
		else{
			//user_of_city.setVisibility(View.GONE);
			nodata_found.setVisibility(View.VISIBLE);
		}
		gridViewAdapter.notifyDataSetChanged();
	p.cancel();
}

private void callApi(Runnable r) {

	if (!Utils.isNetworkConnectedMainThred(getActivity())) {
		Log.v("Internet Not Conneted", "");
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Thread.currentThread().setPriority(1);
				p.cancel();
				Utils.same_id("Error", getString(R.string.no_internet),
						getActivity());
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
	

	private void saveSetting(){
		
		if(ch1.isChecked()){
			pref.setBoolean(Constants.profSetting1, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting1, false);
			pref.commit();
		}
		
		if(ch2.isChecked()){
			pref.setBoolean(Constants.profSetting2, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting2, false);
			pref.commit();
		}
		
		if(ch3.isChecked()){
			pref.setBoolean(Constants.profSetting3, true);
			pref.commit();
		}
		else {
			pref.setBoolean(Constants.profSetting3, false);
			pref.commit();
		}
		if(ch4.isChecked()){
			pref.setBoolean(Constants.profSetting4, true);
			pref.commit();
		}
		
		else {
			pref.setBoolean(Constants.profSetting4, false);
			pref.commit();
		}
		
		if(ch5.isChecked()){
			pref.setBoolean(Constants.profSetting5, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting5, false);
			pref.commit();
		}
		
		if(ch6.isChecked()){
			pref.setBoolean(Constants.profSetting6, true);
			pref.commit();
		}
		else{
			pref.setBoolean(Constants.profSetting6, false);
			pref.commit();
		}
	}

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}
	
}

