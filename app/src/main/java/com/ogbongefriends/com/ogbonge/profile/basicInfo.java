package com.ogbongefriends.com.ogbonge.profile;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.DB.DB;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi") public class basicInfo extends Fragment implements OnClickListener{

	private EditText first_name,last_name,phone,email,con_email,password,con_password;
	private int mYear,mMonth,mDay;
	private ImageView men,women;
	private LinearLayout men_parent,women_parent,dob_parent;
	private int menValue=0;
	private TextView dodtext,dob;
	private Spinner gender_spinner;
	private Button save,update_profile_btn;
private Preferences pref;
	private DB db;
	private CustomLoader p;
	private updateProfileApi update_profile;
	private  ArrayAdapter<String> adapter;
	private String[] vals = {};
	
	private HashMap<String, String> userDescMap;
	private View rootView;
	private int selectedGender=0;
	private String dob_date="";
	private Context _ctx;
	public basicInfo(Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	 rootView = inflater.inflate(R.layout.user_basic_info, container, false);
db=new DB(getActivity());

	first_name = (EditText)rootView.findViewById(R.id.first_name);
	last_name=(EditText)rootView.findViewById(R.id.last_name);
		dob=(TextView)rootView.findViewById(R.id.dob);
		men=(ImageView)rootView.findViewById(R.id.radio_men);
		women=(ImageView)rootView.findViewById(R.id.radio_women);
		men_parent=(LinearLayout)rootView.findViewById(R.id.men_parent);
		women_parent=(LinearLayout)rootView.findViewById(R.id.women_parent);
		save=(Button)rootView.findViewById(R.id.button1);
		update_profile_btn=(Button)rootView.findViewById(R.id.update_profile_btn);
		dob_parent=(LinearLayout)rootView.findViewById(R.id.dob_parent);
		
		men_parent.setOnClickListener(this);
		women_parent.setOnClickListener(this);
		update_profile_btn.setOnClickListener(this);
		dob.setOnClickListener(this);
		dob_parent.setOnClickListener(this);
		save.setOnClickListener(this);
		pref=new Preferences(getActivity());
		pref=new Preferences(_ctx);
		 p= DrawerActivity.p;
		 fetchUserData(pref.get(Constants.KeyUUID));
		 
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
				Toast.makeText(_ctx, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
				//Utils.same_id("Message", "Profile Updated Successfully", getActivity());
			getActivity().onBackPressed();
			}
			 
			 
		 };
	 
	 DrawerActivity.edit_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			first_name.setEnabled(true);
			last_name.setEnabled(true);
			women.setEnabled(true);
			men_parent.setEnabled(true);
			women_parent.setEnabled(true);
			dob.setEnabled(true);
			save.setEnabled(true);
			dob_parent.setEnabled(true);
		}
	});
	 
			return rootView;
	}
	
	
	
	public void hitAPI(final HashMap<String, String> map) {

		p.show();
		update_profile.setPostData(map);
		callApi(update_profile);

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
			String whereClause= DB.Table.user_master.uuid.toString()+" = "+"'"+ id +"'";
			Log.d("whereClause Circle", "whereClause "+whereClause);
			Cursor userDescriptionsCur = db.findCursor(DB.Table.Name.user_master, whereClause, null, null);
			Log.d("userDescriptionsCur.getCount() Circle", "userDescriptionsCur.getCount() "+userDescriptionsCur.getCount());
			if(userDescriptionsCur.moveToNext()){
				
				first_name.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.first_name.toString())));
				last_name.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.last_name.toString())));
				menValue=Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.gender.toString())));

				if(menValue==2){
					men.setImageResource(R.drawable.unselected_radio);
					women.setImageResource(R.drawable.selected_radio);
				}
				else{
					men.setImageResource(R.drawable.selected_radio);
					women.setImageResource(R.drawable.unselected_radio);	
				}
				
				if(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.date_of_birth.toString()))!=null){
				dob.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.date_of_birth.toString())));
				
				
				dob_date=userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.date_of_birth.toString()));
				}
				else{
					dob.setText("");
					dob_date="";
					
				}
				
				
			}
		}
		
		Log.e("userDescriptionsData MAP ", "userDescriptionsData "+userDescriptionsData);
		return userDescriptionsData;
	}

	
	private void setData(){
		
		HashMap<String, String>data=new HashMap<String, String>();
		data.put(DB.Table.user_master.first_name.toString(), first_name.getText().toString());
		data.put(DB.Table.user_master.last_name.toString(), last_name.getText().toString());
		data.put(DB.Table.user_master.gender.toString(), String.valueOf(menValue));
		data.put(DB.Table.user_master.date_of_birth.toString(), dob.getText().toString());
		data.put("type", "1");
		
		
		hitAPI(data);
	}
	
	
	public void DateDialog(){
		DatePickerDialog dpDialog;
		Calendar cal=Calendar.getInstance(); 
		if(vals.length>1){
			dpDialog =new DatePickerDialog(_ctx, new OnDateSetListener(){
	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
	        {
	         dob.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
	        }
	        }, Integer.parseInt(vals[0]),(Integer.parseInt(vals[1])-1),Integer.parseInt(vals[2]));
		}
		
		else{
			dpDialog =new DatePickerDialog(_ctx, new OnDateSetListener(){
		        @Override
		        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
		        {
		         dob.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
		        }
		        },cal.getTime().getYear(),(cal.getTime().getMonth()-1),cal.getTime().getDay() );	
		}
		
		
	    dpDialog.show();
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.update_profile_btn:
			
			first_name.setEnabled(true);
			last_name.setEnabled(true);
			women.setEnabled(true);
			men_parent.setEnabled(true);
			women_parent.setEnabled(true);
			dob.setEnabled(true);
			save.setEnabled(true);
			break;

		case R.id.men_parent:			
			menValue=1;
			men.setImageResource(R.drawable.selected_radio);
			women.setImageResource(R.drawable.unselected_radio);
			break;
			
case R.id.women_parent:			
	menValue=2;
	men.setImageResource(R.drawable.unselected_radio);
	women.setImageResource(R.drawable.selected_radio);
			break;
			
case R.id.dob:
case R.id.dob_parent:	
	if(dob_date.length()>2){
	vals=dob_date.split("-");
	}
	DateDialog(); 
	
		break;	
		
case R.id.button1:
	
	setData();
	
	break;
		default:
			break;
		}
	}
}
