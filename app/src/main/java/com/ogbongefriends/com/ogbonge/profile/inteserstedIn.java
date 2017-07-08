package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gson.JsonArray;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Custom_list_Adapter;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.RangeSeekBar;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.RangeSeekBar.OnRangeSeekBarChangeListener;

@SuppressLint("NewApi") public class inteserstedIn extends Fragment implements OnClickListener,OnCheckedChangeListener {

	// private ListView listView;
	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;
	private JsonArray searchedata;
	private Uri imageUri;

	private Uri selectedImage;
//	private SharedPreferences pref;

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
private Spinner stateNclub_header1,stateNclub_header2,SNC_spinner1,SNC_spinner2;
private LinearLayout seekbar_parent;
private TextView age_text;
private int with_value,for_value;

private String age="";
private ImageView selected_image,men_radio,men,radio_men,women,radio_women,radio_both,both ;
private TextView selected_text,save;
private Custom_list_Adapter custom_list_adapter;
private Dialog dialog;
private LinearLayout date_lay1,for_parent;
private int minage,maxage,interestedin_master_id,interestedin_purpose_master_id;
private LinearLayout men_parent,women_parent,bothmf_parent;
private updateProfileApi updateProfile;
private int starsAndclubsType1=0,starsAndclubsType2=0,starsAndclubsid1=0,starsAndclubsid2=0;
private ArrayList<String> snc1_list,snc2_list,snc_header1;
private int first_index=0,second_index=0;
private ArrayList<Integer> snc1_list_id,snc2_list_id;
private ListView modeList;
private RangeSeekBar<Integer> seekBar;
private updateProfileApi update_profile;
	
	public inteserstedIn(Context ctx) {
		_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(_ctx);
		 modeList = new ListView(_ctx);
		rootView = inflater.inflate(R.layout.interested_in, container, false);
		stateNclub_header1=(Spinner)rootView.findViewById(R.id.sncheader1);
		stateNclub_header2=(Spinner)rootView.findViewById(R.id.sncheader2);
		SNC_spinner1=(Spinner)rootView.findViewById(R.id.snc1);
		SNC_spinner2=(Spinner)rootView.findViewById(R.id.snc2);
		seekbar_parent=(LinearLayout)rootView.findViewById(R.id.seekbar_parent);
		for_parent=(LinearLayout)rootView.findViewById(R.id.for_parent);
		age_text=(TextView)rootView.findViewById(R.id.age_text);
		selected_image=(ImageView)rootView.findViewById(R.id.selected_image);
		selected_text=(TextView)rootView.findViewById(R.id.selected_text);
		date_lay1=(LinearLayout)rootView.findViewById(R.id.date_lay1);
		
		men_parent=(LinearLayout)rootView.findViewById(R.id.men_parent);
		men=(ImageView)rootView.findViewById(R.id.men);
		radio_men=(ImageView)rootView.findViewById(R.id.radio_men);
		
		women_parent=(LinearLayout)rootView.findViewById(R.id.women_parent);
		women=(ImageView)rootView.findViewById(R.id.women);
		radio_women=(ImageView)rootView.findViewById(R.id.radio_women);
		
		bothmf_parent=(LinearLayout)rootView.findViewById(R.id.both_parent);
		both=(ImageView)rootView.findViewById(R.id.men);
		radio_both=(ImageView)rootView.findViewById(R.id.radio_both);
		save=(TextView)rootView.findViewById(R.id.save_btn_setting);
		
		save.setOnClickListener(this);
		men_parent.setOnClickListener(this);
		women_parent.setOnClickListener(this);
		bothmf_parent.setOnClickListener(this);
		men.setOnClickListener(this);
		women.setOnClickListener(this);
		both.setOnClickListener(this);

		snc1_list=new ArrayList<String>();
		snc2_list=new ArrayList<String>();
		
		snc1_list_id=new ArrayList<Integer>();
		snc2_list_id=new ArrayList<Integer>();
		snc_header1=new ArrayList<String>();
		pref=new Preferences(_ctx);
		 seekBar = new RangeSeekBar<Integer>(18, 80, _ctx);
		setSpinners();
		
		

		age_text.setText("Show me people aged "+minage+" to "+maxage);
		
		stateNclub_header1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				starsAndclubsType1=position;
				if(position>0){
					setStarandClub1(position);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		stateNclub_header2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				starsAndclubsType2=position;
				if(position>0){
					setStarandClub2(position);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		SNC_spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				first_index=position;
				starsAndclubsid1=snc1_list_id.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		SNC_spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				second_index=position;
				starsAndclubsid2=snc2_list_id.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		 update_profile=new updateProfileApi(getActivity(), db, p){

				@Override
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
				}

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					p.cancel();
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
					p.cancel();
					//Utils.same_id("Message", "Profile Updated Successfully", getActivity());
				Toast.makeText(_ctx, "Interested In Details Updated Successfully", Toast.LENGTH_SHORT).show();
				getActivity().onBackPressed();
				}
				 
				 
			 };
		
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		        @Override
		        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
		                // handle changed range values
		                Log.i("arvi", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
		                age="Show me people aged";
		                age_text.setText("Show me people aged "+minValue+" to "+maxValue);
		                minage=minValue;
		                maxage=maxValue;
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
		builder.setTitle("I'm here to Find Ogbonge friends");
	

		
		final String[] stringArray = new String[] { "Select For","Long term friendship", "Friends to marriage","Casual friends","Chat sports" };
		final Integer[] stringArrayImages = new Integer[] {R.drawable.for_status, R.drawable.long_term_friendship,R.drawable.friends_to_marriage,R.drawable.casual_dates ,R.drawable.chat_sports};
		
		custom_list_adapter=new Custom_list_Adapter(_ctx, stringArray,stringArrayImages) {
			
			@Override
			protected void onItemLongClick(View v, int string) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			protected void onItemClick(View v, int string) {
				// TODO Auto-generated method stub
				selected_image.setImageResource(stringArrayImages[string]);
				interestedin_purpose_master_id=string;
				selected_text.setText(stringArray[string]);
				dialog.cancel();
			}
		};
		
		if(interestedin_purpose_master_id>0){
		selected_image.setImageResource(stringArrayImages[interestedin_purpose_master_id]);
		selected_text.setText(stringArray[interestedin_purpose_master_id]);
		}
		else{
			selected_image.setImageResource(stringArrayImages[interestedin_purpose_master_id]);
			selected_text.setText(stringArray[interestedin_purpose_master_id]);
		}
		modeList.setAdapter(custom_list_adapter);

		builder.setView(modeList);
		 dialog = builder.create();
	return 	rootView;
		

	}

	
	public void setLokingFor(int i){
		switch (i) {
		case 1:
			
			radio_men.setImageResource(R.drawable.selected_radio);
			radio_women.setImageResource(R.drawable.unselected_radio);
			radio_both.setImageResource(R.drawable.unselected_radio);
			break;
		case 2:
			radio_women.setImageResource(R.drawable.selected_radio);
			radio_men.setImageResource(R.drawable.unselected_radio);
			radio_both.setImageResource(R.drawable.unselected_radio);
		
			break;
			
		case 3:
			radio_both.setImageResource(R.drawable.selected_radio);
			radio_men.setImageResource(R.drawable.unselected_radio);
			radio_women.setImageResource(R.drawable.unselected_radio);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	
		super.onStart();
	}

	
	
	
public void setStarandClub1(int id) {
	db.open();
	p.show();
	data = db.findCursor(DB.Table.Name.starsandclubs_master, DB.Table.starsandclubs_master.sac_type.toString()+" = "+id, null, null);
	snc1_list_id.clear();
	snc1_list.clear();
	if(data.moveToNext()){
	data.moveToFirst();
	do{
		snc1_list_id.add(data.getInt(data.getColumnIndex(DB.Table.starsandclubs_master.id.toString())));
		snc1_list.add(data.getString(data.getColumnIndex(DB.Table.starsandclubs_master.sac_title.toString())));
	}	while(data.moveToNext());
	SNC_spinner1.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc1_list));
	if(first_index<snc1_list.size()){
	SNC_spinner1.setSelection(first_index);
	}
	
	db.close();
	p.cancel();
}
}
	
	public void setStarandClub2(int id) {
		db.open();
		p.show();
		data = db.findCursor(DB.Table.Name.starsandclubs_master, DB.Table.starsandclubs_master.sac_type.toString()+" = "+id, null, null);
		snc2_list_id.clear();
		snc2_list.clear();
		if(data.moveToNext()){
			data.moveToFirst();	
			do{
					snc2_list_id.add(data.getInt(data.getColumnIndex(DB.Table.starsandclubs_master.id.toString())));
					snc2_list.add(data.getString(data.getColumnIndex(DB.Table.starsandclubs_master.sac_title.toString())));
				}while(data.moveToNext());
				SNC_spinner2.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc2_list));
				if(second_index<snc2_list.size()){
				SNC_spinner2.setSelection(second_index);
		}
		}
		
				db.close();
				p.cancel();
	}

	
	
private void setSpinners(){
		p.show();
		
Cursor starclub=db.findCursor(DB.Table.Name.starsandclubs_category_master, DB.Table.starsandclubs_category_master.status.toString()+" = 1", null, null);
	if(starclub.getCount()>0){	
		snc_header1.clear();
		snc_header1.add(0, "Select Option");
		starclub.moveToFirst();
		do{
			snc_header1.add(starclub.getString(starclub.getColumnIndex(DB.Table.starsandclubs_category_master.category_name.toString())));
			
		}while(starclub.moveToNext());
		
		
		
//		snc_header1.add(1, "Players");
//		snc_header1.add(2, "Clubs");
//		snc_header1.add(3, "Nollyword Stars");
//		snc_header1.add(4, "Naija Artistes");
//		snc_header1.add(5, "International Artistes");
		
		
		stateNclub_header1.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc_header1));
		stateNclub_header1.setSelection(0);
		stateNclub_header2.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc_header1));
		stateNclub_header2.setSelection(1);
		setStarandClub1(3);
		setStarandClub2(2);
		fetchUserData(pref.get(Constants.KeyUUID));	
		seekBar.setSelectedMinValue(minage);
		seekBar.setSelectedMaxValue(maxage);
	}
	}


HashMap<String, String> fetchUserData(String id)
{
	
	HashMap<String, String>	userDescriptionsData = new HashMap<String, String>();
	
	if (id!=null && !id.equals("")) {
		db.open();
		String whereClause= DB.Table.user_master.uuid.toString()+" = \""+id+"\"";
		Log.d("whereClause Circle", "whereClause "+whereClause);
		Cursor userDescriptionsCur = db.findCursor(DB.Table.Name.user_master, whereClause, null, null);
		Log.d("userDescriptionsCur.getCount() Circle", "userDescriptionsCur.getCount() "+userDescriptionsCur.getCount());
		if(userDescriptionsCur.moveToNext()){
			
			
			
		interestedin_master_id	=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.interestedin_master_id.toString())));
					
					setLokingFor(interestedin_master_id);	
					
				if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.meet_min_age.toString()))!=null){
		minage=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.meet_min_age.toString())));
				}
				if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.meet_max_age.toString()))!=null){
		maxage=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.meet_max_age.toString())));
				}
		
		if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.interestedin_purpose_master_id.toString()))!=null){
		interestedin_purpose_master_id=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.interestedin_purpose_master_id.toString())));


			modeList.setSelection(interestedin_purpose_master_id);

			//starsAndclubsid1=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.s.toString())));
		
		}
		}
		
		userDescriptionsCur = db.findCursor(DB.Table.Name.user_starsandclubs, DB.Table.user_starsandclubs.user_master_id+" = '"+id+"'", null, null);
		Log.d("COunt ", ""+userDescriptionsCur.getCount());
		userDescriptionsCur.moveToFirst();
		starsAndclubsid1=userDescriptionsCur.getInt(userDescriptionsCur.getColumnIndex(DB.Table.user_starsandclubs.starsandclubs_master_id.toString()));
		starsAndclubsType1=userDescriptionsCur.getInt(userDescriptionsCur.getColumnIndex(DB.Table.user_starsandclubs.type.toString()));
		setStarandClub1(starsAndclubsType1);
		for(int i=0;i<snc1_list_id.size();i++){
			if(starsAndclubsid1==snc1_list_id.get(i)){
				first_index=i;
				SNC_spinner1.setSelection(i);
				break;
			}
			else{
				continue;
			}
		}
		
		if(userDescriptionsCur.moveToNext()){
		
		//userDescriptionsCur.moveToNext();
		starsAndclubsid2=userDescriptionsCur.getInt(userDescriptionsCur.getColumnIndex(DB.Table.user_starsandclubs.starsandclubs_master_id.toString()));
		starsAndclubsType2=userDescriptionsCur.getInt(userDescriptionsCur.getColumnIndex(DB.Table.user_starsandclubs.type.toString()));
		setStarandClub2(starsAndclubsType2);
		stateNclub_header1.setSelection(starsAndclubsType1);
		stateNclub_header2.setSelection(starsAndclubsType2);
	
		for(int i=0;i<snc2_list_id.size();i++){
			if(starsAndclubsid2==snc2_list_id.get(i)){
				second_index=i;
				SNC_spinner2.setSelection(i);
				break;
			}
			else{
				continue;
			}
		}
		}
		
		//SNC_spinner1.setSelection(starsAndclubsid1);
		}
	
	Log.e("userDescriptionsData MAP ", "userDescriptionsData "+userDescriptionsData);
	return userDescriptionsData;
}



	private void updateProfile(HashMap<String, String> map){
	//	p.show();
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
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				p.cancel();
				
				
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
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.men_parent:
		case R.id.men:
		case R.id.radio_men:	
			interestedin_master_id=1;
				radio_men.setImageResource(R.drawable.selected_radio);
				radio_women.setImageResource(R.drawable.unselected_radio);
				radio_both.setImageResource(R.drawable.unselected_radio);
				
		
			break;

case R.id.women_parent:
case R.id.women:
case R.id.radio_women:
			
	interestedin_master_id=2;
		radio_women.setImageResource(R.drawable.selected_radio);
		radio_men.setImageResource(R.drawable.unselected_radio);
		radio_both.setImageResource(R.drawable.unselected_radio);
		
	
			break;	
			
case R.id.both_parent:
case R.id.both:
case	R.id.radio_both:
			
	interestedin_master_id=3;
		radio_both.setImageResource(R.drawable.selected_radio);
		radio_women.setImageResource(R.drawable.unselected_radio);
		radio_men.setImageResource(R.drawable.unselected_radio);
	
			break;				
			
	
case R.id.save_btn_setting:
	setData();
	break;
	

		default:
			break;
		}
	}

	
	public void hitAPI(final HashMap<String, String> map) {

		p.show();
		update_profile.setPostData(map);
		callApi(update_profile);

	}
	
	
private void setData(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		
		data.put(DB.Table.user_master.interestedin_master_id.toString(), String.valueOf(interestedin_master_id));
		data.put(DB.Table.user_master.interestedin_purpose_master_id.toString(), String.valueOf(interestedin_purpose_master_id));
		
		
		//data.put("meet_min_age", String.valueOf(minage));
		//data.put("meet_max_age", String.valueOf(maxage));
		
		
		data.put("age_range", String.valueOf(minage)+","+String.valueOf(maxage));
		data.put("starsAndclubsType1", String.valueOf(starsAndclubsType1));
		data.put("starsAndclubsid1", String.valueOf(starsAndclubsid1));
		data.put("starsAndclubsType2", String.valueOf(starsAndclubsType2));
		data.put("starsAndclubsid2", String.valueOf(starsAndclubsid2));
		data.put("type", "3");
		
		
		
		
//		  "starsAndclubsType1" : "2",
//		    "starsAndclubsid1" : "22",
//		    "starsAndclubsType2" : "1",
//		    "starsAndclubsid2" : "6"
		
		hitAPI(data);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		
	}
	
}


