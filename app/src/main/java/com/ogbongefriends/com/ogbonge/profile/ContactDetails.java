package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class ContactDetails extends Fragment implements OnClickListener{

	private Spinner country_spinner,state_spinner,city_spinner;
	private EditText state_edit,city_edit,phone_number;
	
	private Button save;
private Preferences pref;
	private DB db;
	private CustomLoader p;
	int city_index=-1;
	private updateProfileApi update_profile;
	private  ArrayAdapter<String> adapter;
	private String[] vals = {};
	
	private HashMap<String, String> userDescMap;
	private View rootView;
	private int selectedGender=0;
	private String dob_date="";
	private Context _ctx;
	private Cursor data;
	private int country_selected=0, state_selected=0,city_selected=0;
	private ArrayList<String> country_list,state_list,city_list;
	private ArrayList<Integer> country_list_id,state_list_id,city_list_id;
	public ContactDetails(Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	 rootView = inflater.inflate(R.layout.user_contact_details, container, false);
     db=new DB(getActivity());

	country_spinner=(Spinner)rootView.findViewById(R.id.country_list);
	state_spinner=(Spinner)rootView.findViewById(R.id.state_list);
	city_spinner=(Spinner)rootView.findViewById(R.id.city_list);
	state_edit=(EditText)rootView.findViewById(R.id.state_box);
	city_edit=(EditText)rootView.findViewById(R.id.city_box);
	phone_number=(EditText)rootView.findViewById(R.id.user_phone);
	
		save=(Button)rootView.findViewById(R.id.button1);
	//	update_profile_btn=(Button)rootView.findViewById(R.id.update_profile_btn);
	
	
		save.setOnClickListener(this);
		
		country_list=new ArrayList<String>();
		state_list=new ArrayList<String>();
		city_list=new ArrayList<String>();
		
		country_list_id=new ArrayList<Integer>();
		state_list_id=new ArrayList<Integer>();
		city_list_id=new ArrayList<Integer>();
		pref=new Preferences(getActivity());
		
		pref=new Preferences(_ctx);
	 p= DrawerActivity.p;
	 p.show();
	
	 
	 
	 country_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			country_selected=country_list_id.get(position);
			Log.d("arv country_selected", "arv "+country_selected);
			if(country_selected==159){
				state_edit.setVisibility(View.GONE);
				city_edit.setVisibility(View.GONE);
				state_spinner.setVisibility(View.VISIBLE);
				city_spinner.setVisibility(View.VISIBLE);
				
			}
			else{
				state_edit.setVisibility(View.VISIBLE);
				city_edit.setVisibility(View.VISIBLE);
				state_spinner.setVisibility(View.GONE);
				city_spinner.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	});
	 
	 
	 state_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//state_selected=state_list_id.get(position);
				state_selected=position;
				Log.d("arv country_selected", "arv "+state_selected);
				Log.d("arv country_selected", "arv "+state_list.get(position));
				setCitySpinner(String.valueOf(state_list_id.get(position)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	
	 
	 city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			//city_selected=city_list_id.get(position);
			city_selected=position;
			Log.d("arv country_selected", "arv "+city_selected);
			Log.d("arv country_selected", "arv "+city_list.get(position));
			
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
			Toast.makeText(_ctx, "Contact Details Updated Successfully", Toast.LENGTH_SHORT).show();
			getActivity().onBackPressed();
			}
			 
			 
		 };
	 
	 DrawerActivity.edit_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			city_spinner.setEnabled(true);
			country_spinner.setEnabled(true);
			state_spinner.setEnabled(true);
			city_spinner.setEnabled(true);
			save.setEnabled(true);
			state_edit.setEnabled(true);
			phone_number.setEnabled(true);
			city_edit.setEnabled(true);
		}
	});
	 setSpinners();
			return rootView;
	}
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
		
	}

	public void hitAPI(final HashMap<String, String> map) {

		p.show();
		update_profile.setPostData(map);
		callApi(update_profile);

	}
	
	
private void setSpinners(){
		
		// for country
		db.open();
		data = db.findCursor(DB.Table.Name.country_master, null, null, null);
	
		while(data.moveToNext()){
			country_list_id.add(data.getInt(data.getColumnIndex(DB.Table.country_master.id.toString())));
			country_list.add(data.getString(data.getColumnIndex(DB.Table.country_master.country_name.toString())));
		}
		country_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));
		country_spinner.setSelection(0);
		
		// for state
		data = db.findCursor(DB.Table.Name.nigiria_state_master, null, null, null);
		while(data.moveToNext()){
			state_list_id.add(data.getInt(data.getColumnIndex(DB.Table.nigiria_state_master.id.toString())));
			state_list.add(data.getString(data.getColumnIndex(DB.Table.nigiria_state_master.state_name.toString())));
		}
		state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, state_list));
		state_spinner.setSelection(0);
		
		// for city
		
		//setCitySpinner(String.valueOf(state_list_id.get(0)));
		 fetchUserData(pref.get(Constants.KeyUUID));	
		db.close();
		
	}
	
	
public void setCitySpinner(String state_id){
	db.open();
	city_list_id.clear();
	city_list.clear();
	data = db.findCursor(DB.Table.Name.nigiria_city_master, DB.Table.nigiria_city_master.state_id.toString()+" = "+state_id, null, null);
	while(data.moveToNext()){
		city_list_id.add(data.getInt(data.getColumnIndex(DB.Table.nigiria_city_master.id.toString())));
		city_list.add(data.getString(data.getColumnIndex(DB.Table.nigiria_city_master.city_name.toString())));
	}
	city_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, city_list));

	if(city_index>=0){
		city_spinner.setSelection(city_index);
	}
	else{
		city_spinner.setSelection(0);
	}
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

	
	HashMap<String, String> fetchUserData(String id)
	{
		
		HashMap<String, String>	userDescriptionsData = new HashMap<String, String>();
		db.open();
		if (id!=null && !id.equals("")) {
			String whereClause= DB.Table.user_master.uuid.toString()+" = \""+id+"\"";
			Log.d("whereClause Circle", "whereClause "+whereClause);
			Cursor userDescriptionsCur = db.findCursor(DB.Table.Name.user_master, whereClause, null, null);
			Log.d("userDescriptionsCur.getCount() Circle", "userDescriptionsCur.getCount() "+userDescriptionsCur.getCount());
			if(userDescriptionsCur.moveToNext()){
				
				phone_number.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.phone_number.toString())));
				country_selected=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.country_master_id.toString())));
				
				
				for(int i=0;i<country_list_id.size();i++){
					if(country_list_id.get(i)==country_selected){
						country_spinner.setSelection(i);
					}
					else{
						continue;
					}
				}
				
				if(country_selected==159){
					for(int i=0;i<state_list.size();i++){
					if(state_list.get(i).toString().equalsIgnoreCase(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.state.toString())))){
						state_spinner.setSelection(i);
						setCitySpinner(String.valueOf(state_list_id.get(i)));
					}
					}
					
					
					
					for(int i=0;i<city_list.size();i++){
						if(city_list.get(i).toString().equalsIgnoreCase(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.city.toString())))){
							city_index=i;
							city_spinner.setSelection(i);
						}
						}
					
				}
				
				else{
					
					state_edit.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.state.toString())));
					city_edit.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.city.toString())));
				}
				
							dob_date=userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.date_of_birth.toString()));
//				userDescriptionsData.put(Table.user_master.gender.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.gender.toString())));
//				userDescriptionsData.put(Table.user_master.date_of_birth.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.date_of_birth.toString())));
//				
//				
//				userDescriptionsData.put(Table.user_master.address.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.address.toString())));
//				userDescriptionsData.put(Table.user_master.city.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.city.toString())));
//				
//				userDescriptionsData.put(Table.user_master.state.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.state.toString())));
//				userDescriptionsData.put(Table.user_master.country_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.country_master_id.toString())));
//				
//				userDescriptionsData.put(Table.user_master.pin_code.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.pin_code.toString())));
//				userDescriptionsData.put(Table.user_master.handle_description.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.handle_description.toString())));
//				userDescriptionsData.put(Table.user_master.interestedin_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.interestedin_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.relationship_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.relationship_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.sexuality_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.sexuality_master_id.toString())));
//				
//			
//				
//				userDescriptionsData.put(Table.user_master.height_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.height_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.weight_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.weight_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.bodytype_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.bodytype_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.hair_color_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.hair_color_master_id.toString())));
//				userDescriptionsData.put(Table.user_master.eye_color_master_id.toString(),userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(Table.user_master.eye_color_master_id.toString())));			
				
				
				
				
			}
		}
		
		Log.e("userDescriptionsData MAP ", "userDescriptionsData "+userDescriptionsData);
		p.cancel();
		return userDescriptionsData;
	}

	
	private void setData(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		data.put(DB.Table.user_master.country_master_id.toString(), String.valueOf(country_selected));
		data.put(DB.Table.user_master.phone_number.toString(), phone_number.getText().toString());
		if(country_selected==159){
			data.put(DB.Table.user_master.state.toString(), state_list.get(state_selected));
			data.put(DB.Table.user_master.city.toString(),city_list.get(city_selected));
			
		}
		else{
			data.put(DB.Table.user_master.state.toString(), state_edit.getText().toString());
			data.put(DB.Table.user_master.city.toString(),city_edit.getText().toString());
			
		}
		data.put("type","1");
		
		hitAPI(data);
	}
	
	
	

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.update_profile_btn:
			
			city_spinner.setEnabled(true);
			country_spinner.setEnabled(true);
			state_spinner.setEnabled(true);
			city_spinner.setEnabled(true);
			save.setEnabled(true);
			state_edit.setEnabled(true);
			phone_number.setEnabled(true);
			city_edit.setEnabled(true);
			break;

		
			


		
case R.id.button1:
	
	setData();
	
	break;
		default:
			break;
		}
	}
}
