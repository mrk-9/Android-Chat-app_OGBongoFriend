package com.ogbongefriends.com.ogbonge.setting;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.UpdateNotificationApi;
import com.ogbongefriends.com.api.getNotificationApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class NotificationSetting extends Fragment implements OnClickListener, Runnable {

	// private ListView listView;
	private ArrayList<HashMap<String, String>> feedData;
	private ArrayList<HashMap<String, String>> placeData;
	// private EventAdapter eventAdapter;
	private ArrayList<HashMap<String, String>> eventsData;

	@SuppressWarnings("unused")
	private EditText posttetx;
	@SuppressWarnings("unused")
	private Button post;
	private Button attchbtn;

	private Uri imageUri;

	private Uri selectedImage;
//	private SharedPreferences pref;

	private long str;

	Cursor data;
	Cursor eventdatacorsor;
	Cursor followerdatacorsor;
	Cursor followingdatacorsor;
	Cursor secfollowingdatacorsor;

	private ImageView attchment_img;
	private Button remave_img_btn;
	private ImageView profileImage;
	private ImageView dialogProfileImage;

	private Button showPlaceBtn;
	private Button showEventBtn;
//	private Editor edit;
	private long other_user;

	private long loginId;

	private CheckBox chfollow;
	private CheckBox chsecretfollow;
	private final int NUM_OF_ROWS_PER_PAGE = 10;
	// EventAdapter showfollowerlist;
	ArrayList<HashMap<String, String>> followerdata;
	FragmentManager fragmentManager;
	@SuppressLint("NewApi")
	Fragment fragment;
	Cursor placedatacursor;
	private String followStatus;
//	private String followintType;
	View rootView;
	private String imgname;
	private TextView notificationText;
	private TextView chatText;
	private TextView checkInText;
	private Button notificationBtn;
	private Button save;
	private ImageView push1,email1,push2,email2,push3,email3,push4,email4,push5,email5,push6,email6,push7,email7;
	private int pushV1=0,emailV1=0,pushV2=0,emailV2=0,pushV3=0,emailV3=0,pushV4=0,emailV4=0,pushV5=0,emailV5=0,pushV6=0,emailV6=0,pushV7=0,emailV7=0;
	private Button checkInBtn;
	private TextView Save;
	Notification nt;
	private UpdateNotificationApi updateNotificationApi;
	private DB db;
	private getNotificationApi getNotificationApi;
	private Context _ctx;
	Preferences pref;
	// getEventApi geteventapi;
	int count = 0;
private CustomLoader p;
	
	
	public NotificationSetting(Context ctx) {
_ctx=ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		pref=new Preferences(getActivity());
		rootView = inflater.inflate(R.layout.push_notification_setting, container, false);
		push1=(ImageView)rootView.findViewById(R.id.push1);
		email1=(ImageView)rootView.findViewById(R.id.email1);
		db=new DB(_ctx);
		p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
		push2=(ImageView)rootView.findViewById(R.id.push2);
		email2=(ImageView)rootView.findViewById(R.id.email2);
		
		push3=(ImageView)rootView.findViewById(R.id.push3);
		email3=(ImageView)rootView.findViewById(R.id.email3);
		
		push4=(ImageView)rootView.findViewById(R.id.push4);
		email4=(ImageView)rootView.findViewById(R.id.email4);
		
		push5=(ImageView)rootView.findViewById(R.id.push5);
		email5=(ImageView)rootView.findViewById(R.id.email5);
		
		push6=(ImageView)rootView.findViewById(R.id.push6);
		email6=(ImageView)rootView.findViewById(R.id.email6);
		
		push7=(ImageView)rootView.findViewById(R.id.push7);
		email7=(ImageView)rootView.findViewById(R.id.email7);
		
		
		Save=(TextView)rootView.findViewById(R.id.save);
		push1.setOnClickListener(this);
		email1.setOnClickListener(this);
		
		push2.setOnClickListener(this);
		email2.setOnClickListener(this);
		
		push3.setOnClickListener(this);
		email3.setOnClickListener(this);
		
		push4.setOnClickListener(this);
		email4.setOnClickListener(this);
		
		push5.setOnClickListener(this);
		email5.setOnClickListener(this);
		
		push6.setOnClickListener(this);
		email6.setOnClickListener(this);
		
		push7.setOnClickListener(this);
		email7.setOnClickListener(this);
		
		Save.setOnClickListener(this);
		
	//	save=(Button)rootView.findViewById(R.id.button1);
		getNotification();
		
		pref=new Preferences(getActivity());
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// ============= manage image height================

		fragmentManager = getFragmentManager();
	
		// ==================================================

		/*pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		edit = pref.edit();*/
		

		
		 
		followerdata = new ArrayList<HashMap<String, String>>();
		
		
	return 	rootView;
		

	}

	public void getNotification(){
		p.show();
		getNotificationApi=new getNotificationApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				p.cancel();
			}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
				p.cancel();
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fetchData();
					}
				});
				
				
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				p.cancel();
			}
			
		};
		
		HashMap<String, String>key=new HashMap<String, String>();
		key.put("uuid", pref.get(Constants.KeyUUID));
		key.put("auth_token", pref.get(Constants.kAuthT));
		key.put("time_stamp",""); 
		    
		getNotificationApi.setPostData(key);
		     callApi(getNotificationApi);
	}
	
	
	public void fetchData(){
		
		db.open();
		data = db.findCursor(DB.Table.Name.user_notification_master,Table.user_notification_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'",null, null);
	Log.d("data sizee", ""+data.getCount());
		data.moveToFirst();
		
			if(data.getCount()>0){	
			
pushV1=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_commentonpost.toString())));
emailV1=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_commentonpost.toString())));		
			
pushV2=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_newmatchfriend.toString())));
emailV2=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_newmatchfriend.toString())));


pushV3=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_newchatmessage.toString())));
emailV3=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_newchatmessage.toString())));		
			
pushV4=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_rateonphoto.toString())));
emailV4=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_rateonphoto.toString())));




pushV5=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_someonelikeyou.toString())));
emailV5=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_someonelikeyou.toString())));


pushV6=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_someonefavyou.toString())));
emailV6=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_someonefavyou.toString())));		
			
pushV7=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.push_someonesendgift.toString())));
emailV7=Integer.parseInt(data.getString(data.getColumnIndex(Table.user_notification_master.email_someonesendgift.toString())));

setImages();	
p.cancel();
			}
			
		
			
			
			p.cancel();
	}
	
	public void setImages(){
		
		if(pushV1==1){
				push1.setImageResource(R.drawable.push_notification);
				pushV1=1;
		}	
			else{
				push1.setImageResource(R.drawable.blue_push);
				pushV1=0;
			}
			
		
		
		if(pushV2==1){
			push2.setImageResource(R.drawable.push_notification);
			pushV2=1;
	}	
		else{
			push2.setImageResource(R.drawable.blue_push);
			pushV2=0;
		}
		
		
		if(pushV3==1){
			push3.setImageResource(R.drawable.push_notification);
			pushV3=1;
	}	
		else{
			push3.setImageResource(R.drawable.blue_push);
			pushV3=0;
		}
		
		
		
		
		if(pushV4==1){
			push4.setImageResource(R.drawable.push_notification);
			pushV4=1;
	}	
		else{
			push4.setImageResource(R.drawable.blue_push);
			pushV4=0;
		}
		
		
		
		if(pushV5==1){
			push5.setImageResource(R.drawable.push_notification);
			pushV5=1;
	}	
		else{
			push5.setImageResource(R.drawable.blue_push);
			pushV5=0;
		}
		
		
		
		
		if(pushV6==1){
			push6.setImageResource(R.drawable.push_notification);
			pushV6=1;
	}	
		else{
			push6.setImageResource(R.drawable.blue_push);
			pushV6=0;
		}
		
		
		
		
		if(pushV7==1){
			push7.setImageResource(R.drawable.push_notification);
			pushV7=1;
	}	
		else{
			push7.setImageResource(R.drawable.blue_push);
			pushV7=0;
		}
		
		
		
		
		
		
		
		
		
		if(emailV1==1){
			email1.setImageResource(R.drawable.email);
			emailV1=1;
		}
		else{
			email1.setImageResource(R.drawable.blue_email);
			emailV1=0;
		}
		
		
		if(emailV2==1){
			email2.setImageResource(R.drawable.email);
			emailV2=1;
		}
		else{
			email2.setImageResource(R.drawable.blue_email);
			emailV2=0;
		}
		
		
		
		if(emailV3==1){
			email3.setImageResource(R.drawable.email);
			emailV3=1;
		}
		else{
			email3.setImageResource(R.drawable.blue_email);
			emailV3=0;
		}
		
		
		if(emailV4==1){
			email4.setImageResource(R.drawable.email);
			emailV4=1;
		}
		else{
			email4.setImageResource(R.drawable.blue_email);
			emailV4=0;
		}
		
		
		
		if(emailV5==1){
			email5.setImageResource(R.drawable.email);
			emailV5=1;
		}
		else{
			email5.setImageResource(R.drawable.blue_email);
			emailV5=0;
		}
		
		
		if(emailV6==1){
			email6.setImageResource(R.drawable.email);
			emailV6=1;
		}
		else{
			email6.setImageResource(R.drawable.blue_email);
			emailV6=0;
		}
		
		
		if(emailV7==1){
			email7.setImageResource(R.drawable.email);
			emailV7=1;
		}
		else{
			email7.setImageResource(R.drawable.blue_email);
			emailV7=0;
		}
		
		}
	
	public void UpdateNotification(){
		p.show();
		updateNotificationApi=new UpdateNotificationApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				if(updateNotificationApi.resCode==1){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							p.cancel();
							Utils.same_id("Success", updateNotificationApi.resMsg,_ctx);
							getActivity().onBackPressed();
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
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				p.cancel();
			}
			
		};
		
		
		HashMap<String, String>data=new HashMap<String, String>();
		data.put(Table.user_notification_master.push_commentonpost.toString(), String.valueOf(pushV1));
		data.put(Table.user_notification_master.email_commentonpost.toString(), String.valueOf(emailV1));
		data.put(Table.user_notification_master.push_newmatchfriend.toString(), String.valueOf(pushV2));
		data.put(Table.user_notification_master.email_newmatchfriend.toString(), String.valueOf(emailV2));
		data.put(Table.user_notification_master.push_newchatmessage.toString(), String.valueOf(pushV3));
		data.put(Table.user_notification_master.email_newchatmessage.toString(), String.valueOf(emailV3));
		data.put(Table.user_notification_master.push_rateonphoto.toString(), String.valueOf(pushV4));
		data.put(Table.user_notification_master.email_rateonphoto.toString(), String.valueOf(emailV4));
		
		data.put(Table.user_notification_master.push_someonelikeyou.toString(), String.valueOf(pushV5));
		data.put(Table.user_notification_master.email_someonelikeyou.toString(), String.valueOf(emailV5));
		data.put(Table.user_notification_master.push_someonefavyou.toString(), String.valueOf(pushV6));
		data.put(Table.user_notification_master.email_someonefavyou.toString(), String.valueOf(emailV6));
		data.put(Table.user_notification_master.push_someonesendgift.toString(), String.valueOf(pushV7));
		data.put(Table.user_notification_master.email_someonesendgift.toString(), String.valueOf(emailV7));
	
		
		updateNotificationApi.setPostData(data);
	     callApi(updateNotificationApi);
		
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
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch (v.getId()) {
		case R.id.push1:
			if(pushV1==0){
				
				push1.setImageResource(R.drawable.push_notification);
				pushV1=1;
			}
			else{
				push1.setImageResource(R.drawable.blue_push);
				pushV1=0;
			}
			
			break;

case R.id.email1:
			
	if(emailV1==0){
		email1.setImageResource(R.drawable.email);
		
		emailV1=1;
	}
	else{
		email1.setImageResource(R.drawable.blue_email);
		emailV1=0;
	}
	
			break;
			
case R.id.push2:
	if(pushV2==0){
		push2.setImageResource(R.drawable.push_notification);
		pushV2=1;
	}
	else{
		
		push2.setImageResource(R.drawable.blue_push);
		pushV2=0;
	}
	break;
	
case R.id.email2:
	
	if(emailV2==0){
		
		email2.setImageResource(R.drawable.email);
		emailV2=1;
	}
	else{
		email2.setImageResource(R.drawable.blue_email);
		emailV2=0;
	}
	
	break;
	
case R.id.push3:
	
	if(pushV3==0){
		push3.setImageResource(R.drawable.push_notification);
		pushV3=1;
	}
	else{
		push3.setImageResource(R.drawable.blue_push);
		
		pushV3=0;
	}
	break;
	
case R.id.email3:
	
	if(emailV3==0){
		email3.setImageResource(R.drawable.email);
		emailV3=1;
	}
	else{
		
		email3.setImageResource(R.drawable.blue_email);
		emailV3=0;
	}
	
	break;
	
case R.id.push4:
	
	
	if(pushV4==0){
		push4.setImageResource(R.drawable.push_notification);
		pushV4=1;
	}
	else{
		
		push4.setImageResource(R.drawable.blue_push);
		pushV4=0;
	}
	break;
	
case R.id.email4:
	
	if(emailV4==0){
		email4.setImageResource(R.drawable.email);
		emailV4=1;
	}
	else{
		
		email4.setImageResource(R.drawable.blue_email);
		emailV4=0;
	}
	
	break;
	
case R.id.push5:
	if(pushV5==0){
		push5.setImageResource(R.drawable.push_notification);
		pushV5=1;
	}
	else{
		
		push5.setImageResource(R.drawable.blue_push);
		pushV5=0;
	}
	break;
	
case R.id.email5:
	
	if(emailV5==0){
		email5.setImageResource(R.drawable.email);
		emailV5=1;
	}
	else{
		
		email5.setImageResource(R.drawable.blue_email);
		emailV5=0;
	}
	
	break;
	
case R.id.push6:
	if(pushV6==0){
		push6.setImageResource(R.drawable.push_notification);
		pushV6=1;
	}
	else{
		
		push6.setImageResource(R.drawable.blue_push);
		pushV6=0;
	}
	break;
	
case R.id.email6:
	
	if(emailV6==0){
		email6.setImageResource(R.drawable.email);
		emailV6=1;
	}
	else{
		
		email6.setImageResource(R.drawable.blue_email);
		emailV6=0;
	}
	
	break;
	
case R.id.push7:
	if(pushV7==0){
		push7.setImageResource(R.drawable.push_notification);
		
		pushV7=1;
	}
	else{
		
		
		push7.setImageResource(R.drawable.blue_push);
		pushV7=0;
	}
	break;
	
case R.id.email7:
	
	if(emailV7==0){
		email7.setImageResource(R.drawable.email);
		
		emailV7=1;
	}
	else{
		
		email7.setImageResource(R.drawable.blue_email);
		emailV7=0;
	}
	
	break;
	

			
case R.id.save:
	
	UpdateNotification();
	
	
	
	
	
	break;
			
		default:
			break;
		}
		
	}	
	
	
	
	
	
}
