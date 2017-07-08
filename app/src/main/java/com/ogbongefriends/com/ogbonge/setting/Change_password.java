package com.ogbongefriends.com.ogbonge.setting;

import java.util.HashMap;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.ChangePasswordApi;
import com.ogbongefriends.com.api.LoginApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.Validation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint("NewApi") public class Change_password extends Fragment{

	public static DB db;
	CustomLoader p;
	ChangePasswordApi passwordApi;
	private RelativeLayout Baselayout;
	private ImageView red_crystal,green_crystal,pinc_crystal;
	private float xr=135,yr=3,xg=360,yg=70,xp=270,yp=200;
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
private View rootView;
private EditText current_password,new_password,confirm_password;
private Button Save;
private Preferences pref;
private Context _ctx;

public  Change_password(Context ctx){
	_ctx=ctx;
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
			db=new DB(getActivity());
			pref=new Preferences(_ctx);
		rootView = inflater.inflate(R.layout.change_password,container, false);
		current_password=(EditText)rootView.findViewById(R.id.old_password);
		new_password=(EditText)rootView.findViewById(R.id.new_password);
		confirm_password=(EditText)rootView.findViewById(R.id.confirm_password);
		Save=(Button)rootView.findViewById(R.id.change_password);
		
		Save.setOnClickListener(new OnClickListener() {

			Validation val= new Validation(_ctx);
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (current_password.getText().toString().length() > 0) {
					
					if(new_password.getText().toString().length() > 0){
			if(confirm_password.getText().toString().equals(new_password.getText().toString())){
						
						HashMap<String, String> map = new HashMap<String, String>();

						 map.put("uuid", pref.get(Constants.KeyUUID));
					     map.put("auth_token", pref.get(Constants.kAuthT));
						map.put("old_password",current_password.getText().toString());
						map.put("new_password",new_password.getText().toString());
						
						
							
						HitApi(map);
					}
			else{
				Utils.alert(_ctx, getString(R.string.pass_not_mached));

			}
		}
					else{
						Utils.alert(_ctx, getString(R.string.new_pass_not_null));
					}
				}else{
					Utils.alert(_ctx, getString(R.string.current_pass_not_null));
				}
			}
		});
		
		
		
		
		passwordApi = new ChangePasswordApi(_ctx, db, p) {

			private String gend = "";

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				if (passwordApi.resCode == 1) {
					p.cancel();
					
					
					AlertDialog alertDialog = new AlertDialog.Builder(
	                        _ctx).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle("Password changed successfully");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(passwordApi.resMsg);
	 
	        // Setting Icon to Dialog
	     //   alertDialog.setIcon(R.drawable.tick);
	 
	        // Setting OK Button
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.cancel();
	                	getActivity().onBackPressed();
	                
	                }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
					
					
				
					
				}
				else {
						p.cancel();
						String msg = (String) ((passwordApi.resMsg != null) ? passwordApi.resMsg
								: getResources().getString(
										R.string.unknown_error));
						Utils.same_id("Error", msg,_ctx);
					}
			}

			@Override
			protected void onError(Exception e) {
				p.cancel();
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				
				p.cancel();
				if (passwordApi.resCode == 1) {
					try {
						
					p.cancel();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.v("client details", LoginApi.client_id + "==");

					//profileInfoApi.setPostData();
					//callApi(profileInfoApi);

				} else {
					p.cancel();
					Utils.same_id("Error", passwordApi.resMsg,_ctx);

				}

			}

		};
		
		
		return rootView;
		
	}

	private void HitApi(HashMap<String, String> map) {
		p.show();
		
		passwordApi.setPostData(map);
		callApi(passwordApi);
		
		
	}

	// ======================Thread for api call=====================
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
}
