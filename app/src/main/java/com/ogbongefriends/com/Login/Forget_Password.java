package com.ogbongefriends.com.Login;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.ForgotPasswordApi;
import com.ogbongefriends.com.api.LoginApi;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.Validation;

public class Forget_Password extends Activity implements SensorEventListener{

	public static DB db;
	CustomLoader p;
	ForgotPasswordApi passwordApi;
	private RelativeLayout Baselayout;
	private ImageView red_crystal,green_crystal,pinc_crystal;
	private float xr=135,yr=3,xg=360,yg=70,xp=270,yp=200;
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_password);

		db = new DB(Forget_Password.this);
		p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);

		 red_crystal=(ImageView)findViewById(R.id.red_crystal);
			green_crystal=(ImageView)findViewById(R.id.gerrn_crystal);
			pinc_crystal=(ImageView)findViewById(R.id.pinc_crystal);
			RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(60,60);
		    rparam.topMargin=(int) xr;
		    rparam.leftMargin=(int) yr;
		    red_crystal.setLayoutParams(rparam);
		    RelativeLayout.LayoutParams gparam = new RelativeLayout.LayoutParams(35,35);
		    gparam.topMargin=(int) xg;
		    gparam.leftMargin=(int) yg;
		    green_crystal.setLayoutParams(gparam);
		    RelativeLayout.LayoutParams pparam = new RelativeLayout.LayoutParams(25,25);
		    gparam.topMargin=(int) xp;
		    gparam.leftMargin=(int) yp;
		    pinc_crystal.setLayoutParams(gparam);
		    
		
		    senSensorManager = (SensorManager) getSystemService(homePage.SENSOR_SERVICE);
		    senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		    senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
		
		 	 
		final EditText editText = (EditText) findViewById(R.id.email);
		Button button = (Button) findViewById(R.id.send_pass);

		button.setOnClickListener(new OnClickListener() {

			Validation val= new Validation(Forget_Password.this);
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editText.getText().length() > 0) {
					
					if(val.validEmail(editText, "Invalid Email ID")){
						
						HashMap<String, String> map = new HashMap<String, String>();

						map.put("email", editText.getText().toString().toLowerCase());
						map.put("platform_type","1");
						map.put("device_type","1");
						map.put("device_id",Utils.getDeviceID(Forget_Password.this));
						
							
						HitApi(map);
					}
					else{
						Utils.alert(Forget_Password.this, "Email not valid");
					}
				}else{
					Utils.alert(Forget_Password.this, getString(R.string.enter_email));
				}
			}
		});
		
		
		
		
		passwordApi = new ForgotPasswordApi(this, db, p) {

			private String gend = "";

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				if (passwordApi.resCode == 1) {
					p.cancel();
					
					
					AlertDialog alertDialog = new AlertDialog.Builder(
	                        Forget_Password.this).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle("Password sent successfully");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(passwordApi.resMsg);
	 
	        // Setting Icon to Dialog
	     //   alertDialog.setIcon(R.drawable.tick);
	 
	        // Setting OK Button
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	dialog.cancel();
	                	Intent intent = new Intent(Forget_Password.this,
								LoginActivity.class);
		
						startActivity(intent);
	                
	                }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
					
					
				
					
				}
				else {
						p.cancel();
						String msg = (String) ((LoginApi.resMsg != null) ? passwordApi.resMsg
								: getResources().getString(
										R.string.unknown_error));
						Utils.same_id("Error", msg, Forget_Password.this);
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
				if (LoginApi.resCode == 1) {
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
					Utils.same_id("Error", LoginApi.resMsg,Forget_Password.this);

				}

			}

		};
		
		
		
		
	}

	private void HitApi(HashMap<String, String> map) {
		p.show();
		p.show();
		passwordApi.setPostData(map);
		callApi(passwordApi);
		
		
	}

	// ======================Thread for api call=====================
	private void callApi(Runnable r) {
		try {
			if (!Utils.isNetworkConnectedMainThred(Forget_Password.this)) {
				Log.v("", "Internet Not Conneted");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						p.cancel();
						Utils.alert(Forget_Password.this, R.string.error,
								getString(R.string.no_internet));
					}
				});
				return;
			} else {
				Log.v("", "Internet Conneted");
			}
		} catch (Exception e) {

		}

		Thread t = new Thread(r);
		t.setName(r.getClass().getName());
		t.start();

	}

	// bg touch management
	public void setupTouchListner(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					Utils.SoftKeyBoard(Forget_Password.this, false);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupTouchListner(innerView);
			}
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		Sensor mySensor = event.sensor;
		 
	    if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	 
	    	float xx = event.values[0];
	        float yy = event.values[1];
	        float zz = event.values[2];
	        
	        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(60,60);
	        rparam.topMargin=(int) (yr+(yy*2));
	        rparam.leftMargin=(int)(xr+(xx*2));
		    red_crystal.setLayoutParams(rparam);
		    
		    RelativeLayout.LayoutParams gparam = new RelativeLayout.LayoutParams(35,35);
		    gparam.topMargin=(int) (yg+(yy*2));
		    gparam.leftMargin=(int)(xg+(xx*2));
		    green_crystal.setLayoutParams(gparam);
//		    
	    RelativeLayout.LayoutParams pparam = new RelativeLayout.LayoutParams(25,25);
		    pparam.topMargin=(int) (yp+(yy*2));
		    pparam.leftMargin=(int)(xp+(xx*2));
	        pinc_crystal.setLayoutParams(pparam);

	    }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
}
