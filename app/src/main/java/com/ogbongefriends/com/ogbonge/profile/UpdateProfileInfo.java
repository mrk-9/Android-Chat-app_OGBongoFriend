package com.ogbongefriends.com.ogbonge.profile;


import java.util.ArrayList;
import java.util.HashMap;


import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class UpdateProfileInfo extends Activity implements OnClickListener,OnCheckedChangeListener,OnItemSelectedListener{

	private LinearLayout rel_noanswer_parent, rel_single_parent,rel_taken_parent, rel_open_parent, sex_no_answer_parent,sex_open_minded_parent,sex_bisexual_parent, sex_gay_parent,
	        interent_no_answer_parent, interest_men_parent,interest_women_parent,interest_both_parent;
	
	private RadioButton rel_noanswer,rel_single,rel_taken,rel_open,sex_noanswer,sex_open_minded,sex_bisexual,sex_gay,
	interest_noanswer,interest_men,interest_women,interest_both;
	
	private Spinner height_spinner, weight_spinner, body_type_spinner,eye_color_spinner,hair_color_spinner;
	private EditText about_me;
	private Preferences pref;
	HashMap<String, String> userMap,fullUserMap;
	private Button updateProfileBtn;
	private int relation_ship=0,sexuality=0,interestedIn=0;
	private int height_selected=0, weight_selected=0,body_type_selected=0,eye_color_selected=0,hair_color_selected=0;
	private ArrayList<String> height_list,weight_list,body_type_list,eye_color_list,hair_color_list;
	private ArrayList<Integer> height_list_id,weight_list_id,body_type_list_id,eye_color_list_id,hair_color_list_id;
	
	private Cursor data;
	private DB db;
	private CustomLoader p;
	private updateProfileApi updateProfile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_profile_info);
		db=new DB(UpdateProfileInfo.this);
		fullUserMap=new HashMap<String, String>();
		 p = new CustomLoader(UpdateProfileInfo.this, android.R.style.Theme_Translucent_NoTitleBar);
		pref=new Preferences(UpdateProfileInfo.this);
		p.show();
		about_me=(EditText)findViewById(R.id.update_about_me);
		updateProfileBtn=(Button)findViewById(R.id.update_profile_btn);
		rel_noanswer_parent=(LinearLayout)findViewById(R.id.rel_no_answer_parent);
		rel_single_parent=(LinearLayout)findViewById(R.id.rel_single_parent);
		rel_taken_parent=(LinearLayout)findViewById(R.id.rel_taken_parent);
		rel_open_parent=(LinearLayout)findViewById(R.id.rel_open_parent);
		
		sex_no_answer_parent=(LinearLayout)findViewById(R.id.sex_no_answer_parent);
		sex_open_minded_parent=(LinearLayout)findViewById(R.id.sex_open_minded_parent);
		sex_bisexual_parent=(LinearLayout)findViewById(R.id.sex_open_minded_parent);
		sex_gay_parent=(LinearLayout)findViewById(R.id.sex_gay_parent);
		
		
		interent_no_answer_parent=(LinearLayout)findViewById(R.id.interest_no_answer_parent);
		interest_men_parent=(LinearLayout)findViewById(R.id.interest_men_parent);
		interest_women_parent=(LinearLayout)findViewById(R.id.interest_women_parent);
		interest_both_parent=(LinearLayout)findViewById(R.id.interest_both_parent);
		
		rel_noanswer=(RadioButton)findViewById(R.id.rel_no_answer);
		rel_single=(RadioButton)findViewById(R.id.rel_single);
		rel_taken=(RadioButton)findViewById(R.id.rel_taken);
		rel_open=(RadioButton)findViewById(R.id.rel_open);
		
		sex_noanswer=(RadioButton)findViewById(R.id.sex_no_answer);
		sex_open_minded=(RadioButton)findViewById(R.id.sex_open_minded);
		sex_bisexual=(RadioButton)findViewById(R.id.sex_bisexual);
		sex_gay=(RadioButton)findViewById(R.id.sex_gay);
		
		interest_noanswer=(RadioButton)findViewById(R.id.interest_no_answer);
		interest_men=(RadioButton)findViewById(R.id.interest_men);
		interest_women=(RadioButton)findViewById(R.id.interest_women);
		interest_both=(RadioButton)findViewById(R.id.interest_both);
		
		
		height_spinner=(Spinner)findViewById(R.id.height_spinner);
		weight_spinner=(Spinner)findViewById(R.id.weight_spinner);
		body_type_spinner=(Spinner)findViewById(R.id.body_type_spinner);
		eye_color_spinner=(Spinner)findViewById(R.id.eye_color_spinner);
		hair_color_spinner=(Spinner)findViewById(R.id.hair_color_spinner);
		
		updateProfileBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				updateProfile(userMap);
			}
		});
		
		sex_noanswer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					sexuality=0;
					sex_open_minded.setChecked(false);
					sex_bisexual.setChecked(false);
					sex_gay.setChecked(false);
				}
			}
		});

		sex_open_minded.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			sexuality=1;
			sex_noanswer.setChecked(false);
			sex_bisexual.setChecked(false);
			sex_gay.setChecked(false);
			
		}
	}
});

		sex_bisexual.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			sexuality=2;
			sex_noanswer.setChecked(false);
			sex_open_minded.setChecked(false);
			sex_gay.setChecked(false);
		}
	}
});
		
		

		sex_gay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			sexuality=3;
			sex_noanswer.setChecked(false);
			sex_open_minded.setChecked(false);
			sex_bisexual.setChecked(false);
		}
	}
});
		
		
		
		
		rel_noanswer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					relation_ship=0;
					rel_single.setChecked(false);
					rel_taken.setChecked(false);
					rel_open.setChecked(false);
				}
			}
		});
		
		
		rel_single.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					relation_ship=1;
					rel_noanswer.setChecked(false);
					rel_taken.setChecked(false);
					rel_open.setChecked(false);
				}
			}
		});
		
		
		rel_taken.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					relation_ship=2;
					rel_noanswer.setChecked(false);
					rel_single.setChecked(false);
					rel_open.setChecked(false);
				}
			}
		});
		
		
		rel_open.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					relation_ship=3;
					rel_noanswer.setChecked(false);
					rel_single.setChecked(false);
					rel_taken.setChecked(false);
				}
			}
		});
		
		
		
		
		
		interest_noanswer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					interestedIn=0;
					interest_men.setChecked(false);
					interest_women.setChecked(false);
					interest_both.setChecked(false);
				}
			}
		});
		
		interest_men.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					interestedIn=1;
					interest_noanswer.setChecked(false);
					interest_women.setChecked(false);
					interest_both.setChecked(false);
				}
			}
		});

		interest_women.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			interestedIn=2;
			interest_noanswer.setChecked(false);
			interest_men.setChecked(false);
			interest_both.setChecked(false);
		}
	}
});

		interest_both.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			interestedIn=3;
			interest_noanswer.setChecked(false);
			interest_men.setChecked(false);
			interest_women.setChecked(false);
			
		}
	}
});
		
		
		
		
		height_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//height_selected=position;
				height_selected=height_list_id.get(position);
				Log.d("arv height_selected", "arv "+height_selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		
		weight_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//weight_selected=position;
				weight_selected=weight_list_id.get(position);
				
				Log.d("arv weight_selected", "arv "+weight_selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		body_type_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				body_type_selected=position;
				Log.d("arv body_type_selected", "arv "+body_type_selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		eye_color_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				eye_color_selected=position;
				Log.d("arv eye_color_selected", "arv "+eye_color_selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		
		hair_color_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				hair_color_selected=position;
				Log.d("arv hair_color_selected", "arv "+hair_color_selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		rel_noanswer_parent.setOnClickListener(this);
		rel_single_parent.setOnClickListener(this);
		rel_taken_parent.setOnClickListener(this);
		rel_open_parent.setOnClickListener(this);
		
		sex_no_answer_parent.setOnClickListener(this);
		sex_open_minded_parent.setOnClickListener(this);
		sex_bisexual_parent.setOnClickListener(this);
		sex_gay_parent.setOnClickListener(this);
		
		
		interent_no_answer_parent.setOnClickListener(this);
		interest_men_parent.setOnClickListener(this);
		interest_women_parent.setOnClickListener(this);
		interest_both_parent.setOnClickListener(this);
		
		height_list=new ArrayList<String>();
		weight_list=new ArrayList<String>();
		body_type_list=new ArrayList<String>();
		eye_color_list=new ArrayList<String>();
		hair_color_list=new ArrayList<String>();
		
		height_list_id=new ArrayList<Integer>();
		weight_list_id=new ArrayList<Integer>();
		body_type_list_id=new ArrayList<Integer>();
		eye_color_list_id=new ArrayList<Integer>();
		hair_color_list_id=new ArrayList<Integer>();
		
		
		
		p.cancel();
	}

	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		userMap = fetchUserInfo(pref.get(Constants.KeyUUID));
		setSpinners();
		initSpinner(fullUserMap);
		super.onStart();
	}
	
	
	private void initSpinner(HashMap<String, String> usermap){
		
		try{
			if(usermap.get(Table.user_master.handle_description.toString()).toString().length()>0){
				about_me.setText(usermap.get(Table.user_master.handle_description.toString()).toString());
			}
			else{
				about_me.setText("");
				
			}
		if(usermap.get(Table.user_master.interestedin_master_id.toString()).toString().length()>0){
			interestedIn=Integer.parseInt(usermap.get(Table.user_master.interestedin_master_id.toString()));
		}
		
		if(usermap.get(Table.user_master.sexuality_master_id.toString()).toString().length()>0){
			sexuality=Integer.parseInt(usermap.get(Table.user_master.sexuality_master_id.toString()));
		}
		
		if(usermap.get(Table.user_master.relationship_master_id.toString()).toString().length()>0){
			relation_ship=Integer.parseInt(usermap.get(Table.user_master.relationship_master_id.toString()));
		}
		
		if(usermap.get(Table.user_master.height_master_id.toString()).toString().length()>0){
			height_selected=height_list_id.indexOf(Integer.parseInt(usermap.get(Table.user_master.height_master_id.toString())));
			
		}
		
		if(usermap.get(Table.user_master.weight_master_id.toString()).toString().length()>0){
			weight_selected=weight_list_id.indexOf(Integer.parseInt(usermap.get(Table.user_master.weight_master_id.toString())));
		}
		
		if(usermap.get(Table.user_master.bodytype_master_id.toString()).toString().length()>0){
			body_type_selected=Integer.parseInt(usermap.get(Table.user_master.bodytype_master_id.toString()));
		}
		
		if(usermap.get(Table.user_master.eye_color_master_id.toString()).toString().length()>0){
			eye_color_selected=Integer.parseInt(usermap.get(Table.user_master.eye_color_master_id.toString()));
		}
		
		if(usermap.get(Table.user_master.hair_color_master_id.toString()).toString().length()>0){
			hair_color_selected=Integer.parseInt(usermap.get(Table.user_master.hair_color_master_id.toString()));
		}
		
		
		
		
		switch (relation_ship) {
		case 0:
			rel_noanswer.setChecked(true);
			//rel_single.setChecked(true);
			break;

		case 1:
			rel_single.setChecked(true);
			break;
			
		case 2:
			rel_taken.setChecked(true);
			break;
			
		case 3:
			rel_open.setChecked(true);
			break;
		default:
			break;
		}
		
		
		switch (sexuality) {
		case 0:
			sex_noanswer.setChecked(true);
			//sex_open_minded.setSelected(true);
			break;

		case 1:
			sex_open_minded.setChecked(true);
			break;
			
		case 2:
			sex_bisexual.setChecked(true);
			break;
			
		case 3:
			sex_gay.setChecked(true);
			break;
		default:
			break;
		}
		
		
		switch (interestedIn) {
		case 0:
			interest_both.setChecked(true);
			//interest_men.setSelected(true);
			break;

		case 1:
			interest_men.setChecked(true);
			break;
			
		case 2:
			interest_women.setChecked(true);
			break;
			
		case 3:
			interest_both.setChecked(true);
			break;
		default:
			break;
		}
		
		
		height_spinner.setSelection(height_selected);
		weight_spinner.setSelection(weight_selected);
		body_type_spinner.setSelection(body_type_selected);
		eye_color_spinner.setSelection(eye_color_selected);
		hair_color_spinner.setSelection(hair_color_selected);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

private HashMap<String, String> fetchUserInfo(String uuid){
	
	HashMap<String, String>	UserData = new HashMap<String, String>();
	db.open();
		Cursor staffData = db.findCursor(Table.Name.user_master, Table.user_master.uuid+" = '"+uuid+"'", null, null);
		Log.e("CreateList ", "staffData.getCount() "+staffData.getCount());
		
		if(staffData.moveToNext()){
			
			
			UserData.put(Table.user_master.first_name.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.first_name.toString())));
			UserData.put(Table.user_master.last_name.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.last_name.toString())));
			UserData.put(Table.user_master.gender.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.gender.toString())));				
			UserData.put(Table.user_master.date_of_birth.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.date_of_birth.toString())));
			UserData.put(Table.user_master.address.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.address.toString())));
			UserData.put(Table.user_master.city.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.city.toString())));
			UserData.put(Table.user_master.state.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.state.toString())));
			UserData.put(Table.user_master.country_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.country_master_id.toString())));
			UserData.put(Table.user_master.pin_code.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.pin_code.toString())));
			
			fullUserMap.put(Table.user_master.first_name.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.first_name.toString())));
			fullUserMap.put(Table.user_master.last_name.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.last_name.toString())));
			fullUserMap.put(Table.user_master.gender.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.gender.toString())));				
			fullUserMap.put(Table.user_master.date_of_birth.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.date_of_birth.toString())));
			fullUserMap.put(Table.user_master.address.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.address.toString())));
			fullUserMap.put(Table.user_master.city.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.city.toString())));
			fullUserMap.put(Table.user_master.state.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.state.toString())));
			fullUserMap.put(Table.user_master.country_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.country_master_id.toString())));
			fullUserMap.put(Table.user_master.pin_code.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.pin_code.toString())));
			fullUserMap.put(Table.user_master.handle_description.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.handle_description.toString())));
			fullUserMap.put(Table.user_master.interestedin_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.interestedin_master_id.toString())));
			fullUserMap.put(Table.user_master.relationship_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.relationship_master_id.toString())));				
			fullUserMap.put(Table.user_master.sexuality_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.sexuality_master_id.toString())));
			fullUserMap.put(Table.user_master.height_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.height_master_id.toString())));
			fullUserMap.put(Table.user_master.weight_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.weight_master_id.toString())));
			fullUserMap.put(Table.user_master.bodytype_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.bodytype_master_id.toString())));
			fullUserMap.put(Table.user_master.hair_color_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.hair_color_master_id.toString())));
			fullUserMap.put(Table.user_master.eye_color_master_id.toString(),staffData.getString(staffData.getColumnIndex(Table.user_master.eye_color_master_id.toString())));

			db.close();	
	}
	
	return UserData;
	
}


	private void updateProfile(HashMap<String, String> map){
		p.show();
		HashMap<String, String> profile_data=new HashMap<String, String>();
		profile_data=map;
		
		profile_data.put(Table.user_master.handle_description.toString(), about_me.getText().toString());
		profile_data.put(Table.user_master.interestedin_master_id.toString(), ""+interestedIn);
		profile_data.put(Table.user_master.relationship_master_id.toString(), ""+relation_ship);
		profile_data.put(Table.user_master.sexuality_master_id.toString(), ""+sexuality);
		profile_data.put(Table.user_master.height_master_id.toString(), ""+height_selected);
		profile_data.put(Table.user_master.weight_master_id.toString(), ""+weight_selected);
		profile_data.put(Table.user_master.bodytype_master_id.toString(), ""+body_type_selected);
		
		profile_data.put(Table.user_master.hair_color_master_id.toString(), ""+hair_color_selected);
		profile_data.put(Table.user_master.eye_color_master_id.toString(), ""+eye_color_selected);
		
		
		updateProfile=new updateProfileApi(UpdateProfileInfo.this, db, p){

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
				onBackPressed();
				
				
			}
			
			
		};
		
		updateProfile.setPostData(profile_data);
	     callApi(updateProfile);
		
	}
	
	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(this)) {
			Log.v("Internet Not Conneted", "");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					p.cancel();
					Utils.same_id("Error", getString(R.string.no_internet),UpdateProfileInfo.this);
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
	
	
	private void setSpinners(){
		
		// for height
		db.open();
		data = db.findCursor(Table.Name.height_master, Table.height_master.status+" = 1", null, null);
	
		while(data.moveToNext()){
			height_list_id.add(data.getInt(data.getColumnIndex(Table.height_master.id.toString())));
			height_list.add(data.getString(data.getColumnIndex(Table.height_master.length.toString())));
		}
		height_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, height_list));
		height_spinner.setSelection(0);
		
		// for weight
		data = db.findCursor(Table.Name.weight_master, Table.height_master.status+" = 1", null, null);
		while(data.moveToNext()){
			weight_list_id.add(data.getInt(data.getColumnIndex(Table.weight_master.id.toString())));
			weight_list.add(data.getString(data.getColumnIndex(Table.weight_master.weight.toString())));
		}
		weight_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, weight_list));
		weight_spinner.setSelection(0);
		
		// for body type
		data = db.findCursor(Table.Name.bodytype_master, null, null, null);
		while(data.moveToNext()){
			body_type_list.add(data.getString(data.getColumnIndex(Table.bodytype_master.bodytype_content.toString())));
		}
		body_type_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, body_type_list));
		body_type_spinner.setSelection(0);
		
		
		// for Eye color
				data = db.findCursor(Table.Name.eye_color_master, Table.eye_color_master.status+" = "+1, null, null);
				while(data.moveToNext()){
					eye_color_list.add(data.getString(data.getColumnIndex(Table.eye_color_master.color.toString())));
				}
				eye_color_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, eye_color_list));
				eye_color_spinner.setSelection(0);
				
				
		// For hair color
				data = db.findCursor(Table.Name.hair_color_master, Table.eye_color_master.status+" = "+1, null, null);
				while(data.moveToNext()){
					hair_color_list.add(data.getString(data.getColumnIndex(Table.hair_color_master.color.toString())));
				}
				hair_color_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, hair_color_list));
				hair_color_spinner.setSelection(0);
		db.close();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.rel_no_answer_parent:
			relation_ship=0;
			rel_noanswer.setChecked(true);
			rel_single.setChecked(false);
			rel_taken.setChecked(false);
			rel_open.setChecked(false);
			break;
			
		case R.id.rel_single_parent:
			relation_ship=1;
			rel_noanswer.setChecked(false);
			rel_single.setChecked(true);
			rel_taken.setChecked(false);
			rel_open.setChecked(false);
			break;
			
		case R.id.rel_taken_parent:
			relation_ship=2;
			rel_noanswer.setChecked(false);
			rel_single.setChecked(false);
			rel_taken.setChecked(true);
			rel_open.setChecked(false);
	break;
	
		case R.id.rel_open_parent:
		
			relation_ship=3;
			rel_noanswer.setChecked(false);
			rel_single.setChecked(false);
			rel_taken.setChecked(false);
			rel_open.setChecked(true);
	break;
	
		case R.id.sex_no_answer_parent:
			sexuality=0;
			sex_noanswer.setChecked(true);
			sex_open_minded.setChecked(false);
			sex_bisexual.setChecked(false);
			sex_gay.setChecked(false);
			
	break;
	
		
	
		case R.id.sex_open_minded_parent:
			sexuality=1;
			sex_noanswer.setChecked(false);
			sex_open_minded.setChecked(true);
			sex_bisexual.setChecked(false);
			sex_gay.setChecked(false);
	break;
	
	
		case R.id.sex_bisexual:
			
			sexuality=2;
			sex_noanswer.setChecked(false);
			sex_open_minded.setChecked(false);
			sex_bisexual.setChecked(true);
			sex_gay.setChecked(false);
			
			break;
			
		case R.id.sex_gay_parent:
			
			sexuality=3;
			sex_noanswer.setChecked(false);
			sex_open_minded.setChecked(false);
			sex_bisexual.setChecked(false);
			sex_gay.setChecked(true);
			
			break;
			
		case R.id.interest_no_answer_parent:
	
			interestedIn=0;
			interest_noanswer.setChecked(true);
			interest_men.setChecked(false);
			interest_women.setChecked(false);
			interest_both.setChecked(false);
			
	break;
	
		case R.id.interest_men_parent:
	
			interestedIn=1;
			interest_noanswer.setChecked(false);
			interest_men.setChecked(true);
			interest_women.setChecked(false);
			interest_both.setChecked(false);
	break;
	
		case R.id.interest_women_parent:
	
			interestedIn=2;
			interest_noanswer.setChecked(false);
			interest_men.setChecked(false);
			interest_women.setChecked(true);
			interest_both.setChecked(false);
			
	break;

		case R.id.interest_both_parent:
	
			interestedIn=3;
			interest_noanswer.setChecked(false);
			interest_men.setChecked(false);
			interest_women.setChecked(false);
			interest_both.setChecked(true);
	break;

	
		}
		
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
//		switch(view.getId()){
//		
//		case R.id.height_spinner:
//			
//			Log.d("arv", "arv"+position);
//			
//			break;
//			
//		case R.id.weight_spinner:
//			Log.d("arv", "arv"+position);
//			break;
//			
//		case R.id.body_type_spinner:
//			Log.d("arv", "arv"+position);
//			break;
//			
//		case R.id.eye_color_spinner:
//			Log.d("arv", "arv"+position);
//			break;
//			
//			
//		case R.id.hair_color_spinner:
//			Log.d("arv", "arv"+position);
//			break;
//		}
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub 8004226226
		
	}

	
}
