package com.ogbongefriends.com.ogbonge.profile;

import java.util.ArrayList;
import java.util.HashMap;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

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
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("NewApi") public class UserApprence extends Fragment implements OnClickListener{

	

private Spinner height_spinner, weight_spinner, body_type_spinner;
private int height_selected=0, weight_selected=0,body_type_selected=0;
private ArrayList<String> height_list,weight_list,body_type_list;
private ArrayList<Integer> height_list_id,weight_list_id,body_type_list_id;
private HashMap<String, String>fullUserMap;
private Cursor data;
private View rootView;
private DB db;
private CustomLoader p;
private Context _ctx;
private Preferences pref;
private updateProfileApi updateProfile;

public UserApprence(Context ctx){
	_ctx=ctx;
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

 rootView = inflater.inflate(R.layout.update_profile_info, container, false);
 
 db=new DB(_ctx);
 
 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
pref=new Preferences(_ctx);
p.show();

fullUserMap=new HashMap<String, String>();


height_spinner=(Spinner)rootView.findViewById(R.id.height_spinner);
weight_spinner=(Spinner)rootView.findViewById(R.id.weight_spinner);
body_type_spinner=(Spinner)rootView.findViewById(R.id.body_type_spinner);




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



height_list=new ArrayList<String>();
weight_list=new ArrayList<String>();
body_type_list=new ArrayList<String>();


height_list_id=new ArrayList<Integer>();
weight_list_id=new ArrayList<Integer>();
body_type_list_id=new ArrayList<Integer>();



p.cancel();
return rootView;
}




@Override
public void onStart() {
// TODO Auto-generated method stub
	setSpinners();
fetchUserInfo(pref.get(Constants.KeyUUID));

super.onStart();
}


private void initSpinner(HashMap<String, String> usermap){

try{
	
if(usermap.get(DB.Table.user_master.height_master_id.toString()).toString().length()>0){
	height_selected=height_list_id.indexOf(Integer.parseInt(usermap.get(DB.Table.user_master.height_master_id.toString())));
	
}

if(usermap.get(DB.Table.user_master.weight_master_id.toString()).toString().length()>0){
	weight_selected=weight_list_id.indexOf(Integer.parseInt(usermap.get(DB.Table.user_master.weight_master_id.toString())));
}

if(usermap.get(DB.Table.user_master.bodytype_master_id.toString()).toString().length()>0){
	body_type_selected=Integer.parseInt(usermap.get(DB.Table.user_master.bodytype_master_id.toString()));
}





height_spinner.setSelection(height_selected);
weight_spinner.setSelection(weight_selected);
body_type_spinner.setSelection(body_type_selected);
}
catch(Exception e){
	e.printStackTrace();
}

}


private HashMap<String, String> fetchUserInfo(String uuid){

HashMap<String, String>	UserData = new HashMap<String, String>();
db.open();
Cursor staffData = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid+" = '"+uuid+"'", null, null);
Log.e("CreateList ", "staffData.getCount() "+staffData.getCount());

if(staffData.moveToNext()){
	
	fullUserMap.put(DB.Table.user_master.height_master_id.toString(),staffData.getString(staffData.getColumnIndex(DB.Table.user_master.height_master_id.toString())));
	fullUserMap.put(DB.Table.user_master.weight_master_id.toString(),staffData.getString(staffData.getColumnIndex(DB.Table.user_master.weight_master_id.toString())));
	fullUserMap.put(DB.Table.user_master.bodytype_master_id.toString(),staffData.getString(staffData.getColumnIndex(DB.Table.user_master.bodytype_master_id.toString())));
	
	db.close();	
}

return UserData;

}


private void updateProfile(HashMap<String, String> map){
p.show();
HashMap<String, String> profile_data=new HashMap<String, String>();
profile_data=map;

profile_data.put(DB.Table.user_master.height_master_id.toString(), ""+height_selected);
profile_data.put(DB.Table.user_master.weight_master_id.toString(), ""+weight_selected);
profile_data.put(DB.Table.user_master.bodytype_master_id.toString(), ""+body_type_selected);



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


private void setSpinners(){

// for height
db.open();
data = db.findCursor(DB.Table.Name.height_master, DB.Table.height_master.status+" = 1", null, null);

while(data.moveToNext()){
	height_list_id.add(data.getInt(data.getColumnIndex(DB.Table.height_master.id.toString())));
	height_list.add(data.getString(data.getColumnIndex(DB.Table.height_master.length.toString())));
}
height_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, height_list));
height_spinner.setSelection(0);

// for weight
data = db.findCursor(DB.Table.Name.weight_master, DB.Table.height_master.status+" = 1", null, null);
while(data.moveToNext()){
	weight_list_id.add(data.getInt(data.getColumnIndex(DB.Table.weight_master.id.toString())));
	weight_list.add(data.getString(data.getColumnIndex(DB.Table.weight_master.weight.toString())));
}
weight_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, weight_list));
weight_spinner.setSelection(0);

// for body type
data = db.findCursor(DB.Table.Name.bodytype_master, null, null, null);
while(data.moveToNext()){
	body_type_list.add(data.getString(data.getColumnIndex(DB.Table.bodytype_master.bodytype_content.toString())));
}
body_type_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, body_type_list));
body_type_spinner.setSelection(0);


// for Eye color
		data = db.findCursor(DB.Table.Name.eye_color_master, DB.Table.eye_color_master.status+" = "+1, null, null);
		while(data.moveToNext()){
		}
		
// For hair color
		data = db.findCursor(DB.Table.Name.hair_color_master, DB.Table.eye_color_master.status+" = "+1, null, null);
		while(data.moveToNext()){
				}
		
db.close();
}


@Override
public void onClick(View v) {
// TODO Auto-generated method stub

}








}
