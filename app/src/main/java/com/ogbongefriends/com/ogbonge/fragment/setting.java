package com.ogbongefriends.com.ogbonge.fragment;

import java.io.InputStream;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.ogbonge.profile.updateProfileApi;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Custom_list_Adapter;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.RangeSeekBar;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.RangeSeekBar.OnRangeSeekBarChangeListener;

@SuppressLint("NewApi") public class setting extends Fragment implements Runnable,MyLocationListener,OnClickListener,OnCheckedChangeListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;

	private Uri selectedImage;

	private long str;
	private LocationHelper mLocationHelper;
	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;
	Preferences pref;
private Context _ctx;
private CustomLoader p;
private DB db;
private View rootView;
	private GridView user_of_city;
private LinearLayout seekbar_parent,seekbar_parent_location;
private TextView age_text;
private int interestedin_master_id,interestedin_purpose_master_id,meet_min_age,meet_max_age,location=300;
private Button save,cancel;
private String age="";
private ImageView selected_image,men_radio,men,radio_men,women,radio_women,radio_both,both ;
private TextView selected_text,select_range;
private Custom_list_Adapter custom_list_adapter;
private Dialog dialog;
private SeekBar seekBar1;
private LinearLayout date_lay1;
private int men_value=0,women_value=0,both_mf=0;
private LinearLayout men_parent,women_parent,bothmf_parent;
private updateProfileApi updateProfile;
private user_profile_api user_profile_info;
private RangeSeekBar<Integer> seekBar;
//final String[] stringArray = new String[] { "Casual dates/friends","Chat sports", "Friends to marriage","Long term friendship" };
//final Integer[] stringArrayImages = new Integer[] { R.drawable.date_setting,R.drawable.inbox_over ,R.drawable.new_friends_setting,R.drawable.new_friends_setting};
	
final String[] stringArray = new String[] { "Select For","Long term friendship","Friends to marriage","Casual friends","Chat sports"};
final Integer[] stringArrayImages = new Integer[] { R.drawable.for_status,R.drawable.long_term_friendship,R.drawable.friends_to_marriage,R.drawable.casual_dates ,R.drawable.chat_sports};
public setting(Context ctx) {
		_ctx=ctx;
	}
	public setting(){

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
			pref=new Preferences(_ctx);
		//p.show();
		rootView = inflater.inflate(R.layout.setting, container, false);
		user_of_city=(GridView)rootView.findViewById(R.id.user_of_city);
		seekbar_parent=(LinearLayout)rootView.findViewById(R.id.seekbar_parent);
		age_text=(TextView)rootView.findViewById(R.id.age_text);
		selected_image=(ImageView)rootView.findViewById(R.id.selected_image);
		selected_text=(TextView)rootView.findViewById(R.id.selected_text);
		date_lay1=(LinearLayout)rootView.findViewById(R.id.date_lay1);
		seekBar1=(SeekBar)rootView.findViewById(R.id.seekBar1);
		men_parent=(LinearLayout)rootView.findViewById(R.id.men_parent);
		men=(ImageView)rootView.findViewById(R.id.men);
		radio_men=(ImageView)rootView.findViewById(R.id.radio_men);
		select_range=(TextView)rootView.findViewById(R.id.select_range);
		women_parent=(LinearLayout)rootView.findViewById(R.id.women_parent);
		women=(ImageView)rootView.findViewById(R.id.women);
		radio_women=(ImageView)rootView.findViewById(R.id.radio_women);
		bothmf_parent=(LinearLayout)rootView.findViewById(R.id.both_parent);
		both=(ImageView)rootView.findViewById(R.id.men);
		radio_both=(ImageView)rootView.findViewById(R.id.radio_both);
		save=(Button)rootView.findViewById(R.id.save_btn_setting);
		cancel=(Button)rootView.findViewById(R.id.cancel_btn_setting);
		seekBar = new RangeSeekBar<Integer>(18, 80, _ctx);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		men_parent.setOnClickListener(this);
		women_parent.setOnClickListener(this);
		bothmf_parent.setOnClickListener(this);
		men.setOnClickListener(this);
		women.setOnClickListener(this);
		both.setOnClickListener(this);
		
		
		 seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			  int progress = location;
			  
			  @Override
			  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				  progress = progresValue;
				  location=progress;
				  select_range.setText("From My Location "+ progress+" km");
			  }
			
			  @Override
			  public void onStartTrackingTouch(SeekBar seekBar) {
			  }
			
			  @Override
			  public void onStopTrackingTouch(SeekBar seekBar) {
				  location=progress;
				  select_range.setText("From My Location "+ progress+" km");
				  
			  }
		   });
		
		
		seekbar_parent_location=(LinearLayout)rootView.findViewById(R.id.seekbar_parent_location);
		
		
		
		getUserProfileFromApi();
		
		

		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		        @Override
		        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
		                // handle changed range values
		                Log.i("arvi", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
		                age="Show me people aged";
		                age_text.setText("Show me people aged "+minValue+" to "+maxValue);
		                meet_min_age=minValue;
		                meet_max_age=maxValue;
		        }
		});
		
		
		
		date_lay1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
			}
		});
		

		seekbar_parent.addView(seekBar);
	
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(_ctx);
		builder.setTitle("I�m here to Find Ogbonge friends");
	
	

		ListView modeList = new ListView(_ctx);
		
		
		custom_list_adapter=new Custom_list_Adapter(_ctx, stringArray,stringArrayImages) {
			
			@Override
			protected void onItemLongClick(View v, int string) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			protected void onItemClick(View v, int string) {
				// TODO Auto-generated method stub
				selected_image.setImageResource(stringArrayImages[string]);
				selected_text.setText(stringArray[string]);
				interestedin_purpose_master_id=string;
				dialog.cancel();
			}
		};
		
		
	
		modeList.setAdapter(custom_list_adapter);
		modeList.setSelection(interestedin_purpose_master_id-1);
	
		builder.setView(modeList);
		 dialog = builder.create();

//		dialog.show();
		
	return 	rootView;
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getUserProfile();
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
	
	
	private void getUserProfile(){
		
		Cursor c=db.findCursor(Table.Name.user_master, Table.user_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'", null, null);
		c.moveToFirst();
		interestedin_master_id=c.getInt(c.getColumnIndex(Table.user_master.interestedin_master_id.toString()));
		interestedin_purpose_master_id=c.getInt(c.getColumnIndex(Table.user_master.interestedin_purpose_master_id.toString()));
		meet_min_age=c.getInt(c.getColumnIndex(Table.user_master.meet_min_age.toString()));
		meet_max_age=c.getInt(c.getColumnIndex(Table.user_master.meet_max_age.toString()));
		location=c.getInt(c.getColumnIndex(Table.user_master.location.toString()));
		
		
		seekBar1.setProgress(location);
		 select_range.setText("From My Location "+ location+" km");
		 seekBar.setSelectedMinValue(meet_min_age);
			seekBar.setSelectedMaxValue(meet_max_age);
			selected_image.setImageResource(stringArrayImages[interestedin_purpose_master_id]);
			selected_text.setText(stringArray[interestedin_purpose_master_id]);
			switch (interestedin_master_id) {
			case 1:
				men_value=1;
				radio_men.setImageResource(R.drawable.selected_radio);
				
				
				break;
			case 2:
				women_value=1;
				radio_women.setImageResource(R.drawable.selected_radio);
				
			
				break;
				
			case 3:
				both_mf=1;
				radio_both.setImageResource(R.drawable.selected_radio);
				break;

			default:
				break;
			}
			
			age_text.setText("Show me people aged "+meet_min_age+" to "+meet_max_age);
		p.cancel();
		
	}
	
	
	private void updateProfile(HashMap<String, String> map){
		p.show();
		HashMap<String, String> profile_data=new HashMap<String, String>();
		profile_data=map;
		

		
		updateProfile=new updateProfileApi(_ctx, db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				p.cancel();
				Utils.same_id("Message", "Setting Saved Successfully!", _ctx);
				getActivity().onBackPressed();
				
			}
			
			
		};
		
		updateProfile.setPostData(profile_data);
	     callApi(updateProfile);
		
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
		
	
	
	private void manageLocation() {
		mLocationHelper = new LocationHelper(_ctx);
		//mLocationHelper.startLocationUpdates(this);
	}
	


	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.men_parent:
		case R.id.men:
		case R.id.radio_men:	
			
			if(men_value==0){
				interestedin_master_id=1;
				men_value=1;
				women_value=0;
				both_mf=0;
				radio_men.setImageResource(R.drawable.selected_radio);
				radio_women.setImageResource(R.drawable.unselected_radio);
				radio_both.setImageResource(R.drawable.unselected_radio);
			}

			break;

case R.id.women_parent:
case R.id.women:
case R.id.radio_women:
			
	if(women_value==0){
		interestedin_master_id=2;
		women_value=1;
		men_value=0;
		both_mf=0;
		radio_women.setImageResource(R.drawable.selected_radio);
		radio_men.setImageResource(R.drawable.unselected_radio);
		radio_both.setImageResource(R.drawable.unselected_radio);
		
	}
			break;	
			
case R.id.both_parent:
case R.id.both:
case	R.id.radio_both:
			
	if(both_mf==0){
		interestedin_master_id=3;
		women_value=0;
		men_value=0;
		both_mf=1;
		radio_both.setImageResource(R.drawable.selected_radio);
		radio_women.setImageResource(R.drawable.unselected_radio);
		radio_men.setImageResource(R.drawable.unselected_radio);
	}
			break;				
			
	
case R.id.save_btn_setting:
	svaeValues();
	
	break;
	
case R.id.cancel_btn_setting:
	getActivity().onBackPressed();
	break;
		default:
			break;
		}
		
	}

	
	public void svaeValues(){
		HashMap<String, String>dataToSend=new HashMap<String, String>();
		
		dataToSend.put("interestedin_master_id", String.valueOf(interestedin_master_id));
		dataToSend.put("interestedin_purpose_master_id", String.valueOf(interestedin_purpose_master_id));
		dataToSend.put("age_range", String.valueOf(meet_min_age)+","+String.valueOf(meet_max_age));
		dataToSend.put("location", String.valueOf(location));
		dataToSend.put("type", "1");
		updateProfile(dataToSend);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		
	}
	
}


