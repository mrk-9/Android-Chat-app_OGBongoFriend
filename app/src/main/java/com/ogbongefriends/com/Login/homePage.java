package com.ogbongefriends.com.Login;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonArray;
import com.ogbongefriends.com.ogbonge.Vos.UserVO;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.register.Registration;

import java.util.HashMap;

@SuppressLint("NewApi")
public class homePage extends Activity implements SensorEventListener, OnClickListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private ImageView red_crystal, green_crystal, pinc_crystal;
    private float xr = 135, yr = 3, xg = 360, yg = 70, xp = 270, yp = 200;
    private Button login, joinNow;
    private Preferences pref;
    private DB db;
    private LinearLayout ll_user;
    Cursor data;
    private Dialog dialog;
    LinearLayout.LayoutParams lp;
    LocationManager locationManager;
    private HorizontalScrollView horizontalScrollView;
    public static CustomLoader p;
    private getAllUserProfileAPI get_all_user_pfro_api;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CelUtils.hideSoftKeyboard(homePage.this);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        login = (Button) findViewById(R.id.login);
        joinNow = (Button) findViewById(R.id.joinNow);
        red_crystal = (ImageView) findViewById(R.id.red_crystal);
        green_crystal = (ImageView) findViewById(R.id.gerrn_crystal);
        pinc_crystal = (ImageView) findViewById(R.id.pinc_crystal);
        horizontalScrollView=(HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
        horizontalScrollView.setVisibility(View.GONE);
        ll_user = (LinearLayout) findViewById(R.id.parent_user_images);
        //gps_tracker=new GPSTracker(homePage.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        pref = new Preferences(homePage.this);
        db = new DB(homePage.this);
        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
        p.show();
        getAllUsers();

        dialog = new Dialog(homePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.seal);
        LinearLayout go_regis = (LinearLayout) dialog.findViewById(R.id.go_regis);
        LinearLayout go_terms = (LinearLayout) dialog.findViewById(R.id.go_terms);
        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
        go_regis.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pref.setBoolean("promo", true).commit();
                Intent i = new Intent(homePage.this, Registration.class);
                startActivity(i);
                finish();
            }
        });

        go_terms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Go Terms
            }
        });

        cross.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (pref.getBoolean("promo") == false) {
           // dialog.show();
        }

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(homePage.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        joinNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(homePage.this, Registration.class);
                startActivity(i);
                finish();
            }
        });

        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(60, 60);
        rparam.topMargin = (int) xr;
        rparam.leftMargin = (int) yr;
        red_crystal.setLayoutParams(rparam);
        RelativeLayout.LayoutParams gparam = new RelativeLayout.LayoutParams(35, 35);
        gparam.topMargin = (int) xg;
        gparam.leftMargin = (int) yg;
        green_crystal.setLayoutParams(gparam);
        RelativeLayout.LayoutParams pparam = new RelativeLayout.LayoutParams(25, 25);
        gparam.topMargin = (int) xp;
        gparam.leftMargin = (int) yp;
        pinc_crystal.setLayoutParams(gparam);
        senSensorManager = (SensorManager) getSystemService(homePage.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(viewIntent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (pref.getBoolean("promo") == false) {
          //  dialog.show();
        }
        super.onResume();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float xx = event.values[0];
            float yy = event.values[1];
            float zz = event.values[2];

            RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(60, 60);
            rparam.topMargin = (int) (yr + (yy * 2));
            rparam.leftMargin = (int) (xr + (xx * 2));
            red_crystal.setLayoutParams(rparam);

            RelativeLayout.LayoutParams gparam = new RelativeLayout.LayoutParams(35, 35);
            gparam.topMargin = (int) (yg + (yy * 2));
            gparam.leftMargin = (int) (xg + (xx * 2));
            green_crystal.setLayoutParams(gparam);

            RelativeLayout.LayoutParams pparam = new RelativeLayout.LayoutParams(25, 25);
            pparam.topMargin = (int) (yp + (yy * 2));
            pparam.leftMargin = (int) (xp + (xx * 2));
            pinc_crystal.setLayoutParams(pparam);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public void getAllUsers() {

        get_all_user_pfro_api = new getAllUserProfileAPI(homePage.this, db, p) {

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
                showuser(getAllUserProfileAPI.getUserdata());

                Log.d("In UI Update meathod", "UpdateUI");

            }

        };


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", "");
        map.put("auth_token", "");

        map.put("time_stamp", "");
        map.put("type", "1");
        map.put("page_index", "1");
//	     if(gps_tracker.canGetLocation()!=null){
//	    	 map.put("latitude",""+gps_tracker.getLatitude()); 
//		     map.put("longitude", ""+gps_tracker.getLongitude()); 
//			}
        //else{
        map.put("latitude", "12345");
        map.put("longitude", "12345");
        //}


        get_all_user_pfro_api.setPostData(map);
        callApi(get_all_user_pfro_api);
    }


    private void showuser(JsonArray usersdata) {
        // TODO Auto-generated method stub
        if(usersdata!=null) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            for (int i = 0; i < usersdata.size(); i++) {

                UserVO userVO = new UserVO(usersdata.get(i).getAsJsonObject());

                if (userVO.getProfile_pic().length() > 3) {
                    RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
                    RelativeLayout.LayoutParams topbar_param = new RelativeLayout.LayoutParams(200, 30);
                    RelativeLayout parent = new RelativeLayout(homePage.this);
                    parent.setLayoutParams(parent_param);
                    RelativeLayout top_bar = new RelativeLayout(homePage.this);
                    top_bar.setBackgroundResource(R.drawable.top_element);
                    String url = homePage.this.getString(R.string.urlString) + "userdata/image_gallery/" + userVO.getId() + "/photos_of_you/" + userVO.getProfile_pic();
                    Log.e("url", url);
                    ImageView imagView = new ImageView(homePage.this);
                    imagView.setScaleType(ScaleType.FIT_XY);
                    ll_user.setOrientation(LinearLayout.HORIZONTAL);
                    int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

                    String toastMsg;
                    switch (screenSize) {
                        case Configuration.SCREENLAYOUT_SIZE_LARGE:
                            toastMsg = "Large screen";

                            lp = new LinearLayout.LayoutParams(200, LayoutParams.FILL_PARENT);

                            break;
                        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                            toastMsg = "Normal screen";

                            lp = new LinearLayout.LayoutParams(200, LayoutParams.FILL_PARENT);
                            lp.setMargins(5, 0, 0, 0);
                            break;
                        case Configuration.SCREENLAYOUT_SIZE_SMALL:
                            toastMsg = "Small screen";
                            break;
                        default:
                            toastMsg = "Screen size is neither large, normal or small";
                    }
                    imagView.setLayoutParams(lp);
                    AnimationDrawable   animPlaceholder = (AnimationDrawable)homePage.this.getResources().getDrawable(R.drawable.image_loading_animation);
                    animPlaceholder.start();
                    Glide.with(homePage.this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(.01f)
                            .into(imagView);

                    RelativeLayout.LayoutParams online_param = new RelativeLayout.LayoutParams(15, 15);
                    online_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    ImageView online_image = new ImageView(homePage.this);
                    online_image.setLayoutParams(online_param);
                    online_image.setImageResource(R.drawable.online_dot);
                    TextView tv = new TextView(homePage.this);
                    RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(200, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(text_param);
                    tv.setText(userVO.getFirst_name());
                    tv.setTextColor(Color.WHITE);
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    parent.addView(imagView);
                    parent.addView(top_bar, topbar_param);
                    parent.addView(tv);
                    ll_user.addView(parent);
                }
            }

            p.cancel();
        }
        else{
            horizontalScrollView.setVisibility(View.VISIBLE);
        }
    }


    private void callApi(Runnable r) {

        if (!Utils.isNetworkConnectedMainThred(this)) {
            Log.v("Internet Not Conneted", "");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    p.cancel();
                    same_id("Error", getString(R.string.no_internet));
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

    public void same_id(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                homePage.this);
        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
