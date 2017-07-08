package com.ogbongefriends.com.ogbonge.how_about_we;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.AddEventApi;
import com.ogbongefriends.com.api.confirm_pointsApi;
import com.ogbongefriends.com.api.getTransactionApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.weekpicker.SlideDayTimeListener;


@SuppressLint("NewApi") public class CreateEvent extends Fragment{
	
	Preferences pref;
	private int points,item_points;
	
	private Editor edit;
	private int str;
	private int place_id;
	private DB db;
	private boolean show_prompt_flag=false;
	private Context _ctx;
	private EditText description,city;
	private AutoCompleteTextView where;
	private Button save,when;
	private Spinner category,event;
	private TextView char_count;
	private int char_total=380;
	private int selected_cate=0,selected_event=0;
	private int char_gain=0;
	private int mYear,mMonth,mDay,hourOfDay,minute;
	private ImageView profile;
	private AddEventApi add_event_api;
	private CustomLoader p;
	private int category_id=0;
	private int pass_7_day_time;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView userImage;
	private ArrayList<String> categoryList;
	private ArrayList<String> eventList;
	private ArrayList<Integer>categoryListId;
	private ArrayList<Integer> eventListId;
	private user_profile_api user_profile_info;
	confirm_pointsApi confirm_points_api;
	public JsonObject _jsondata;
	
	 private getTransactionApi get_transaction_api;
	 private Calendar cal;
	 private String[] categoryContent = { 
	          "Food", "Cafe","Drinks",
	          "Show","Play", "Explore", 
	          "Volunteer", "Shop","Other"
	          }; 
	 private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
		private static final String OUT_JSON = "/json";

		private static final String API_KEY = "AIzaSyDkEUodGqjvuk5Z52-avQweRZV9ptdkKM8";
		private static final String LOG_TAG = "ExampleApp";
	 
	 private String[] time = { 
	          "Anytime", "Today","Specific Date"
	          }; 
	 
	 private Integer[] mThumbIds = { 
//             R.drawable.food,R.drawable.cafe,R.drawable.drink,
//             R.drawable.show,R.drawable.play, R.drawable.explore, 
//             R.drawable.volunteer, R.drawable.shop,R.drawable.other
             };
	 HashMap<String, String> hash_data=new HashMap<String, String>();
	 
	public CreateEvent(Context ctx) {
	_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.create_event, container,false);
		categoryList=new ArrayList<String>();
		eventList=new ArrayList<String>();
		cal=Calendar.getInstance();
	 	categoryListId=new ArrayList<Integer>();
		eventListId=new ArrayList<Integer>();
		description=(EditText)rootView.findViewById(R.id.description);
		category=(Spinner)rootView.findViewById(R.id.category);
		event=(Spinner)rootView.findViewById(R.id.event);
		where=(AutoCompleteTextView)rootView.findViewById(R.id.where);
		when=(Button)rootView.findViewById(R.id.when);
		char_count=(TextView)rootView.findViewById(R.id.char_count);
		save=(Button)rootView.findViewById(R.id.save);
		city=(EditText)rootView.findViewById(R.id.city);
		userImage=(ImageView)rootView.findViewById(R.id.userImage);
		p= DrawerActivity.p;
		db = new DB(getActivity());
		pref=new Preferences(_ctx);
		where.setAdapter(new PlacesAutoCompleteAdapter(_ctx, R.layout.listitem));
		initCategories();
		 File cacheDir = StorageUtils.getCacheDirectory(_ctx);
		 
		 final SlideDayTimeListener listener = new SlideDayTimeListener() {

	            @Override
	            public void onDayTimeSet(int day, int hour, int minute)
	            {
	                Toast.makeText(
	                        getActivity(),
	                        "day = " + day + "\nhour = " + hour + "\nminute = " + minute,
	                        Toast.LENGTH_LONG).show();
	            }

	            @Override
	            public void onDayTimeCancel()
	            {
	                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
	            }
	        };   
	        
	        
		options = new DisplayImageOptions.Builder()
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .displayer(new RoundedBitmapDisplayer(15))
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
		p.show();
		getUserAndCategory(pref.get(Constants.KeyUUID));
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
						getUserProfileFromApi();
						
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
		
		where.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		//		Toast.makeText(_ctx, "Where", Toast.LENGTH_LONG).show();
			}
		});
		
		when.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
//				new SlideDayTimePicker.Builder(getActivity().getFragmentManager())
//                .setListener(listener)
//                .setInitialDay(1)
//                .setInitialHour(13)
//                .setInitialMinute(30)
//                //.setIs24HourTime(false)
//                //.setCustomDaysArray(getResources().getStringArray(R.array.days_of_week))
//                //.setTheme(SlideDayTimePicker.HOLO_DARK)
//                //.setIndicatorColor(Color.parseColor("#990000"))
//                .build()
//                .show();
				
		        
		        
				
				// TODO Auto-generated method stub
		//		Toast.makeText(_ctx, "when", Toast.LENGTH_LONG).show();
				
				final Dialog dialog = new Dialog(_ctx);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
				dialog.setContentView(R.layout.date_time_picker);
				
				final TextView date=(TextView)dialog.findViewById(R.id.setdate);
				final TextView time=(TextView)dialog.findViewById(R.id.settimeedit);
				Button set=(Button)dialog.findViewById(R.id.set);
				Button cancel=(Button)dialog.findViewById(R.id.cancel);
				dialog.show();
				
				
				set.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(date.getText().length()>0){
						
							if(time.getText().length()>0){
						when.setText(date.getText().toString()+" "+time.getText().toString());
						dialog.dismiss();
						}
							else{
								Utils.same_id("Message", "Please Enter the Time", _ctx);
							}
						}
						else{
							Utils.same_id("Message", "Please Enter the Date",_ctx);
						}
					}
				});
				
				

				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				date.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Calendar c = Calendar.getInstance();
			            mYear = c.get(Calendar.YEAR);
			            mMonth = c.get(Calendar.MONTH);
			            mDay = c.get(Calendar.DAY_OF_MONTH);
					DateDialog(date); 
					}
				});
				
				time.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Calendar c = Calendar.getInstance();
			            hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			            minute = c.get(Calendar.MINUTE);
			            if(date.getText().length()>0){
			            
					TimeDialog(time); 
			           }
			           else{
			        	   Utils.same_id("Message", "Please select date first", _ctx);
			           }
					}
				});
				
			
				
			}
		});


save.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	//	Toast.makeText(_ctx, "save", Toast.LENGTH_LONG).show();
		db.open();
		
		Cursor	data = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid + " = '"+ pref.get(Constants.KeyUUID)+"'", null, null);
			if (data != null && data.moveToNext()) {
				data.moveToFirst();
				
				pass_7_day_time=data.getInt(data.getColumnIndex(DB.Table.user_master.pass_7days_expiry_time.toString()));
				
			}
			db.close();
		postEvents();
			
	}
});

category.setOnItemSelectedListener(new OnItemSelectedListener() {

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position>0){
		selected_cate=categoryListId.get((position));
		
		initEvents(selected_cate);
	}
		else{
			//CelUtils.same_id("Message", "Please Select a Category First", _ctx);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}


});
		

event.setOnItemSelectedListener(new OnItemSelectedListener() {

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		selected_event=eventListId.get((position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

});

		description.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				char_gain=description.getText().length();
				char_count.setText(char_gain+"/"+char_total);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		
		
		
	
		
		
		
		return rootView;

	}
	
	
	ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;
	    
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false&key=" + API_KEY);
	        sb.append("&components=country:ind");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e("LOG_TAG", "Cannot process JSON results", e);
	    }
	    
	    return resultList;
	}
	
	
	
	private void postEvents(){
		add_event_api=new AddEventApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();

				if(add_event_api.resCode==1){
				Utils.same_id("Message", add_event_api.resMsg, _ctx);
				description.setText("");
				where.setText("");
				city.setText("");
				category.setSelection(0);
				when.setText("");
				event.setSelection(0);
				}
				else{
					Utils.same_id("Message", add_event_api.resMsg, _ctx);
				}
				p.cancel();
			}
			
		};
		
		
	
		if(description.getText().length()>0){
			if(selected_cate>0){
				if(selected_event>0){
			if(where.getText().length()>0){
				if(city.getText().length()>0){
					if(when.getText().length()>0){
						
						hash_data.put("uuid", pref.get(Constants.KeyUUID));
						hash_data.put("auth_token", pref.get(Constants.kAuthT));
						hash_data.put("event_description", description.getText().toString());
						hash_data.put("place", where.getText().toString());
						hash_data.put("event_category_id", String.valueOf(selected_cate));
						hash_data.put("event_subcategory_id", String.valueOf(selected_event));
						hash_data.put("city", city.getText().toString());
						hash_data.put("event_datetime", String.valueOf(CelUtils.StringToDate(Constants.kTimeStampFormat, when.getText().toString()).getTime()/1000));
				
						Log.d("arv ",pass_7_day_time+"arv "+String.valueOf(cal.getTimeInMillis()/100));
						p.show();
						if(pass_7_day_time-(cal.getTimeInMillis()/1000)>0){
						
						add_event_api.setPostData(hash_data);
						callApi(add_event_api);
						}
						else{
							
							callPropmtForPass();
							
						}
					
				}
				else{
					Utils.same_id("Message", "Please Enter Day and time", _ctx);
				}
			}
			else{
				Utils.same_id("Message", "City can't be blacnk", _ctx);
			}
				
			}
			else{
				Utils.same_id("Message", "Location can't be blacnk", _ctx);
			}
			}
			else{

				Utils.same_id("Message", "Please Select a Event", _ctx);
			}
		}
		else{
			
			Utils.same_id("Message", "Please Select a Category", _ctx);
		}
		
		
		
		}
		else{
			Utils.same_id("Message", "Discription can't be blank", _ctx);
		}
	
		
	}

	private void callPropmtForPass(){
		
		
		show_prompt_flag=false;
		getPointInfo();
		
		
		
		
		
			
	}
	
	
	private void getPointInfo() {
		// TODO Auto-generated method stub
		
		
		
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
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(show_prompt_flag==false){
						 showPrompt(_jsondata);
						 show_prompt_flag=true;
						}
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

	
	private void showPrompt(JsonObject jsn) {
		// TODO Auto-generated method stub
		

		Log.d("arv", "arv"+jsn.get("points").getAsString()+"  "+jsn.get("item_points").getAsString());
		points=Integer.parseInt(jsn.get("points").getAsString());
		item_points=Integer.parseInt(jsn.get("item_points").getAsString());
		p.cancel();
		if(points>item_points){
		
		

			final Dialog dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
            dialog.setContentView(R.layout.confirmation_dialog);
           TextView title=(TextView)dialog.findViewById(R.id.title);
           TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			title.setText("OgbongeFriends");
			String str="You have "+points+ " points, You are about to spend "+item_points+" points in order to activate 7 days pass.";
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
				     p.show();
				     get_transaction_api.setPostData(map);
				     callApi(get_transaction_api);
//				     if(getTransactionApi.resCode==1)
//				     {
//				    	 
//				    	 postEvents();
//				     }
				     
				     
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
			title.setText("OgbongeFriends");
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
				
				getActivity().runOnUiThread(new Runnable() {
					  public void run() {
						 // pass_7_day_time=get_transaction_api.pass_7days_expiry_time;
						  add_event_api.setPostData(hash_data);
						  callApi(add_event_api);
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
	
	
	private void initCategories(){
		db.open();
		Cursor catdata=db.findCursor(DB.Table.Name.event_categories, DB.Table.event_categories.category_id+" = "+0, null, null);
		//catdata.moveToFirst();
		categoryList.clear();
		categoryListId.clear();
		categoryList.add("Select a Category");
		categoryListId.add(0);
		while(catdata.moveToNext()){
		categoryListId.add(catdata.getInt(catdata.getColumnIndex(DB.Table.event_categories.id.toString())));
		categoryList.add(catdata.getString(catdata.getColumnIndex(DB.Table.event_categories.category_name.toString())));
	
		}
		
		
		category.setAdapter(new CustomArrayAdapter(_ctx,categoryList) {
			
			@Override
			protected void onItemClick(View v, int position) {
				// TODO Auto-generated method stub
				
			}
		});
		//category.setAdapter(new ArrayAdapter<String>(_ctx, R.layout.lightgrey_listitem, categoryList));
		
		category.setSelection(0);
		db.close();
	}
	private void initEvents(int cat_id){
	
		db.open();
		eventList.clear();
		eventListId.clear();
		Cursor catdata=db.findCursor(DB.Table.Name.event_categories, DB.Table.event_categories.category_id+" = "+cat_id, null, null);
		//catdata.moveToFirst();
		eventList.add("Select an Event");
		eventListId.add(0);
		while(catdata.moveToNext()){
		eventListId.add(catdata.getInt(catdata.getColumnIndex(DB.Table.event_categories.id.toString())));
		eventList.add(catdata.getString(catdata.getColumnIndex(DB.Table.event_categories.category_name.toString())));
	
		}
		//event.setAdapter(new ArrayAdapter<String>(_ctx, R.layout.lightgrey_listitem, eventList));
		event.setAdapter(new CustomArrayAdapter(_ctx,eventList) {
			
			@Override
			protected void onItemClick(View v, int position) {
				// TODO Auto-generated method stub
				
			}
		} );
		event.setSelection(0);
		db.close();
		
		
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
		
	
	public void getUserAndCategory(String uuid){
		

db.open();
		
	Cursor	data = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid + " = '"+ uuid+"'", null, null);
try{
		
		if (data != null && data.moveToNext()) {
			data.moveToFirst();
			
			String path = data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
			pass_7_day_time=data.getInt(data.getColumnIndex(DB.Table.user_master.pass_7days_expiry_time.toString()));
			if(path.length()>1){
				
				imageLoader.displayImage(getString(R.string.profile_image_url)+path, userImage,options, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			else{
				userImage.setImageResource(R.drawable.profile);
			}
			p.cancel();
			
				return;
			}
			else {
				p.cancel();
				return;

			}

		}

catch(Exception e){
e.printStackTrace();
}

		p.cancel();


		
	}
	
	
	public void DateDialog(final TextView date){
	    DatePickerDialog dpDialog=new DatePickerDialog(_ctx, new OnDateSetListener(){
	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
	        {
	        	
	        	date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
	        }
	        }, mYear,mMonth,mDay);
	    dpDialog.show();
	}
	
	public void TimeDialog(final TextView date){
	
	TimePickerDialog tmDialog=new TimePickerDialog(_ctx, new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			
			
			date.setText(" "+hourOfDay+":"+minute+":00");
		}
	}, hourOfDay, minute, true);
	tmDialog.show();
	}
	
	
	public class ImageAdapter extends BaseAdapter { 
        private Context mContext;
        private LayoutInflater mInflater;
        public ImageAdapter(Context c) {
         mInflater = LayoutInflater.from(c);
            mContext = c; 
        } 
        public int getCount() { 
            return mThumbIds.length; 
        } 
        public Object getItem(int position) { 
            return null; 
        } 
        public long getItemId(int position) { 
            return 0; 
        } 
        // create a new ImageView for each item referenced by the 
        public View getView(int position, View convertView, ViewGroup parent) { 
         ViewHolder holder;
            if (convertView == null) {  // if it's not recycled, 
                 convertView = mInflater.inflate(R.layout.event_category_row, null);
                 holder = new ViewHolder();
                 holder.title = (TextView) convertView.findViewById(R.id.textView1);
                 holder.icon = (ImageView )convertView.findViewById(R.id.imageView1);
                 convertView.setTag(holder);
             } else {
                 holder = (ViewHolder) convertView.getTag();
             }
    holder.icon.setAdjustViewBounds(true);
    holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP); 
    holder.icon.setPadding(5, 5, 5, 5);
    holder.title.setText(categoryContent[position]);
    holder.icon.setImageResource(mThumbIds[position]);
    return convertView; 
        } 
        class ViewHolder {
            TextView title;
            ImageView icon;
        }
        // references to our images 
       

     } 
   

	
	public abstract class CustomArrayAdapter extends BaseAdapter {

		private ArrayList<String> data;
		Context context;
		public CustomArrayAdapter(Context context,
				ArrayList<String> placeData) {
			this.context = context;
			this.data = placeData;
			
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
					view = inflater.inflate(R.layout.lightgrey_listitem, parent, false);
				}
				
				TextView tv=(TextView)view.findViewById(R.id.tv);
				tv.setText(data.get(pos));

			if (pos == 0) {
				view.setBackgroundColor(getResources().getColor(R.color.light_blue));
			} else {
				view.setBackgroundColor(getResources().getColor(R.color.background_grey));
			}

			
			return view;
			
			
			
		}

		// =============================================

		private String getCatName(String cat_id) {
			// TODO Auto-generated method stub
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

}


