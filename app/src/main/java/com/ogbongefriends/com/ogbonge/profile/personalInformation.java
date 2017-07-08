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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class personalInformation extends Fragment implements OnClickListener{

	private Spinner height_spinner,weight_spinner,body_spinner;
	private Button save;
private Preferences pref;
	private DB db;
	private CustomLoader p;
	private updateProfileApi update_profile;
	private HashMap<String, String> userDescMap;
	private View rootView;
	private Context _ctx;
	private Cursor data;
	private int height_selected=0, weight_selected=0,body_selected=0;
	private ArrayList<String> height_list,weight_list,body_list;
	private ArrayList<Integer> height_list_id,weight_list_id,body_list_id;
	public personalInformation(Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	 rootView = inflater.inflate(R.layout.personal_information, container, false);
db=new DB(getActivity());

height_spinner=(Spinner)rootView.findViewById(R.id.height_list);
weight_spinner=(Spinner)rootView.findViewById(R.id.weight_list);
body_spinner=(Spinner)rootView.findViewById(R.id.bodytype_list);
		save=(Button)rootView.findViewById(R.id.save);
	
	
		save.setOnClickListener(this);
		
		
		height_list=new ArrayList<String>();
		weight_list=new ArrayList<String>();
		body_list=new ArrayList<String>();
		height_list_id=new ArrayList<Integer>();
		weight_list_id=new ArrayList<Integer>();
		body_list_id=new ArrayList<Integer>();
		pref=new Preferences(getActivity());
		pref=new Preferences(_ctx);
		p= DrawerActivity.p;
		p.show();
	
	 
	 
		height_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			height_selected=height_list_id.get(position);
			Log.d("arv country_selected", "arv "+height_selected);
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
				weight_selected=weight_list_id.get(position);
				Log.d("arv country_selected", "arv "+weight_selected);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	
		body_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				body_selected=body_list_id.get(position);
				Log.d("arv country_selected", "arv "+body_selected);
				
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
				//Utils.same_id("Message", "About Me Updated Successfully", getActivity());
				
			Toast.makeText(_ctx, "Personal Information Updated Successfully", Toast.LENGTH_SHORT).show();
			getActivity().onBackPressed();
			}
			 
			 
		 };
	 
//	 DrawerActivity.edit_btn.setOnClickListener(new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			education_spinner.setEnabled(true);
//			job_spinner.setEnabled(true);
//			save.setEnabled(true);
//			my_handle.setEnabled(true);
//			about_me.setEnabled(true);
//		}
//	});
	 
			return rootView;
	}
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		setSpinners();
		super.onStart();
		
	}

	public void hitAPI(final HashMap<String, String> map) {

		p.show();
		update_profile.setPostData(map);
		callApi(update_profile);

	}
	
	
private void setSpinners(){
		
		// for Education
		db.open();
		data = db.findCursor(DB.Table.Name.height_master, null, null, null);
	
		while(data.moveToNext()){
			height_list_id.add(data.getInt(data.getColumnIndex(DB.Table.height_master.id.toString())));
			height_list.add(data.getString(data.getColumnIndex(DB.Table.height_master.length.toString())));
		}
		height_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, height_list));
		height_spinner.setSelection(0);
		
		// for job
		data = db.findCursor(DB.Table.Name.weight_master, null, null, null);
		while(data.moveToNext()){
			weight_list_id.add(data.getInt(data.getColumnIndex(DB.Table.weight_master.id.toString())));
			weight_list.add(data.getString(data.getColumnIndex(DB.Table.weight_master.weight.toString())));
		}
		weight_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, weight_list));
		weight_spinner.setSelection(0);
		
		data = db.findCursor(DB.Table.Name.bodytype_master, null, null, null);
		while(data.moveToNext()){
			body_list_id.add(data.getInt(data.getColumnIndex(DB.Table.bodytype_master.id.toString())));
			body_list.add(data.getString(data.getColumnIndex(DB.Table.bodytype_master.bodytype_content.toString())));
		}
		body_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, body_list));
		body_spinner.setSelection(0);
		
	
		db.close();
		
		 fetchUserData(pref.get(Constants.KeyUUID));	
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

	
	HashMap<String, String> fetchUserData(String id)
	{
		
		HashMap<String, String>	userDescriptionsData = new HashMap<String, String>();
		
		if (id!=null && !id.equals("")) {
			String whereClause= DB.Table.user_master.uuid.toString()+" = \""+id+"\"";
			Log.d("whereClause Circle", "whereClause "+whereClause);
			db.open();
			Cursor userDescriptionsCur = db.findCursor(DB.Table.Name.user_master, whereClause, null, null);
			Log.d("userDescriptionsCur.getCount() Circle", "userDescriptionsCur.getCount() "+userDescriptionsCur.getCount());
			if(userDescriptionsCur.moveToNext()){
				userDescriptionsCur.moveToFirst();	
			
if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.height_master_id.toString()))!=null){
height_selected=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.height_master_id.toString())));
}
else{
	height_selected=0;
}

if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.weight_master_id.toString()))!=null){
weight_selected=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.weight_master_id.toString())));
}
else{
	weight_selected=0;
}

if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.bodytype_master_id.toString()))!=null){
body_selected=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.bodytype_master_id.toString())));
}
else{
	body_selected=0;
}


				for(int i=0;i<height_list_id.size();i++){
					if(height_list_id.get(i)==height_selected){
						height_spinner.setSelection(i);
					}
					else{
						continue;
					}
				}
				

				for(int i=0;i<weight_list_id.size();i++){
					if(weight_list_id.get(i)==weight_selected){
						weight_spinner.setSelection(i);
					}
					else{
						continue;
					}
				}
				
				
				for(int i=0;i<body_list_id.size();i++){
					if(body_list_id.get(i)==body_selected){
						body_spinner.setSelection(i);
					}
					else{
						continue;
					}
				}
				
			}
		}
		
		Log.e("userDescriptionsData MAP ", "userDescriptionsData "+userDescriptionsData);
		return userDescriptionsData;
	}

	
	private void setData(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		
			data.put(DB.Table.user_master.height_master_id.toString(), String.valueOf(height_selected));
			data.put(DB.Table.user_master.weight_master_id.toString(),String.valueOf(weight_selected));
			data.put(DB.Table.user_master.bodytype_master_id.toString(),String.valueOf(body_selected));
		data.put("type", "1");
		
		hitAPI(data);
	}
	
	
	

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.update_profile_btn:
			
			
			break;

		
			


		
case R.id.save:
	
	setData();
	
	break;
		default:
			break;
		}
	}
}
