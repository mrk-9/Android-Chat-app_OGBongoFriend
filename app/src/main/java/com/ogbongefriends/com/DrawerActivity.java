package com.ogbongefriends.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.getNotificationCountApi;
import com.ogbongefriends.com.api.logOut;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.chat.ChatWebView;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.GetBackFragment;
import com.ogbongefriends.com.common.NotificationType;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.RangeSeekBar;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.ogbonge.backstage_album.AlbumGallery;
import com.ogbongefriends.com.ogbonge.backstage_album.Circle_img_Gallery;
import com.ogbongefriends.com.ogbonge.backstage_album.MyBackstageAlbum;
import com.ogbongefriends.com.ogbonge.breakingnews.breakingList;
import com.ogbongefriends.com.ogbonge.breakingnews.newsWebView;
import com.ogbongefriends.com.ogbonge.fragment.CashForPayment;
import com.ogbongefriends.com.ogbonge.fragment.Favorities;
import com.ogbongefriends.com.ogbonge.fragment.Feedback;
import com.ogbongefriends.com.ogbonge.fragment.First_Fragment;
import com.ogbongefriends.com.ogbonge.fragment.MyOgbongeFriendList;
import com.ogbongefriends.com.ogbonge.fragment.UserGiftList;
import com.ogbongefriends.com.ogbonge.fragment.VirtualGifts;
import com.ogbongefriends.com.ogbonge.fragment.WhoFavYou;
import com.ogbongefriends.com.ogbonge.fragment.promote_fragment;
import com.ogbongefriends.com.ogbonge.fragment.setting;
import com.ogbongefriends.com.ogbonge.fragment.verification;
import com.ogbongefriends.com.ogbonge.fragment.whoIsOnline;
import com.ogbongefriends.com.ogbonge.fragment.whoLikedyou;
import com.ogbongefriends.com.ogbonge.how_about_we.CreateEvent;
import com.ogbongefriends.com.ogbonge.how_about_we.EventDetails;
import com.ogbongefriends.com.ogbonge.how_about_we.ShowEvents;
import com.ogbongefriends.com.ogbonge.invite.FriendPickerApplication;
import com.ogbongefriends.com.ogbonge.payment.PaymentView;
import com.ogbongefriends.com.ogbonge.payment.transactionDetails;
import com.ogbongefriends.com.ogbonge.photos.AddPhotoOptionClass;
import com.ogbongefriends.com.ogbonge.photos.MemberAlbum;
import com.ogbongefriends.com.ogbonge.photos.UserFullProfileAlbum;
import com.ogbongefriends.com.ogbonge.photos.addPhotoOption;
import com.ogbongefriends.com.ogbonge.photos.albums;
import com.ogbongefriends.com.ogbonge.profile.AboutMe;
import com.ogbongefriends.com.ogbonge.profile.ContactDetails;
import com.ogbongefriends.com.ogbonge.profile.MemberProfile;
import com.ogbongefriends.com.ogbonge.profile.ProfilePage;
import com.ogbongefriends.com.ogbonge.profile.UserBasicInfo;
import com.ogbongefriends.com.ogbonge.profile.basicInfo;
import com.ogbongefriends.com.ogbonge.profile.full_users_profile;
import com.ogbongefriends.com.ogbonge.profile.inteserstedIn;
import com.ogbongefriends.com.ogbonge.profile.personalInformation;
import com.ogbongefriends.com.ogbonge.push_notification.Config;
import com.ogbongefriends.com.ogbonge.push_notification.GcmIntentService;
import com.ogbongefriends.com.ogbonge.search.Custom_search;
import com.ogbongefriends.com.ogbonge.search.Search;
import com.ogbongefriends.com.ogbonge.setting.Account;
import com.ogbongefriends.com.ogbonge.setting.AccountSettings;
import com.ogbongefriends.com.ogbonge.setting.Change_password;
import com.ogbongefriends.com.ogbonge.setting.NotificationSetting;
import com.ogbongefriends.com.ogbonge.setting.StandardSearchSetting;
import com.ogbongefriends.com.ogbonge.setting.WhoIsOnlineSetting;
import com.ogbongefriends.com.ogbonge.setting.setting_web_pages;


import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@SuppressLint("NewApi")
public class DrawerActivity extends Activity implements OnClickListener, Runnable, OnSharedPreferenceChangeListener {


    Button draweBtn, export, add_people, video_chat, msg_btn;
    public static Button popBtn;
    public static ImageView audio_rec, video_rec;
    public static Button backbtn;
    private LinearLayout trans_layer;
    public String name, pass_7_day_long, points;
    RelativeLayout poplayout, top_menu;
    public static TextView account_setting, save_btn_setting, cancel_btn_setting;
    private LinearLayout makfrnd_lay, chat_lay, date_lay;
    DrawerLayout mDrawerLayout;
    private logOut log_out_api;
    ListView mDrawerList, poplist, list_for_touchSupport;
    private long days = 0, hours = 0, mins = 0, secs = 0, mseconds = 0;
    private ImageLoader imageLoader;
    private static String imgbt;
    private static String user_id;
    public static Button edit_btn;
    public static LinearLayout chat_attachment, attachment_layer, gallery_layout, photo_layout, audio_layout, video_layout;
    private boolean account_setting_flag = false, to_make_new_frnds = false, to_chat = false, to_date = false, with_boys = false, with_girls = false;
    private RelativeLayout setting_panel;
    public static RelativeLayout attachment_menu;
    private TextView minAge, maxAge;
    public Preferences pref;
    private LinearLayout seekbar_parent, red_bubble,red_bubble_haw;
    public static DB db;
    public static TextView dashboard_points;
    public static CustomLoader p;
    private int screenSize = 0;
    public static int current_page = -1;
    Fragment fragment = null;
    private Calendar c;
    private user_profile_api user_profile_info;
    private DisplayImageOptions options, options_rounded;
    FriendPickerApplication aController;
    public static ImageView imageView, userPhoto;
    AsyncTask<Void, Void, Void> mRegisterTask;
    private TextView pass_7_days, pass_7_day;
    public static TextView chat_noti,haw_noti;
    private getNotificationCountApi getNotificationCount;
    private Calendar cal;
    // ******* DECLARING ADAPTERS *******
    BroadcastReceiver receiver;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    SharedPreferences prefs;
    LocationManager locationManager;

    MenuAdapter ba;

    @SuppressWarnings("unused")
    private Handler mHandler;

    String[] listContent = {"Profile", "Push Notification", "E-Mail Notification", "SMS Notification", "Log out"};

    public DrawerActivity() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == Crop.REQUEST_CROP) {
            addPhotoOption addPhotoOptionObj = (addPhotoOption) getSupportFragmentManager().findFragmentByTag(FragmentTag.EDIT_PERSONAL_DETAIL);
            if (addPhotoOptionObj != null) {
                addPhotoOptionObj.handleCrop(resultCode, data);
            }
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = getSharedPreferences(Constants.SOCIAL_MEDIA, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("server", "serverName").commit();


        db = new DB(DrawerActivity.this);


        File cacheDir = StorageUtils.getCacheDirectory(DrawerActivity.this);
        c = Calendar.getInstance();
        mseconds = c.getTimeInMillis() / 1000;

        aController = (FriendPickerApplication) getApplicationContext();
        cal = Calendar.getInstance();
        db.delete(DB.Table.Name.event_master, null, null);
        GCMRegistrar.checkDevice(this);

        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {

            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

        } else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {

                // Skips registration.
                Toast.makeText(getApplicationContext(),
                        "Already registered with GCM Server",
                        Toast.LENGTH_LONG).
                        show();
            } else {
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {

                        // Register on our server
                        // On server creates a new user
                        aController.register(context, "arvind", "arvind.yadav@alcanzarsoft.com", regId);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };

                // execute AsyncTask
                mRegisterTask.execute(null, null, null);
            }

        }

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .build();


        options_rounded = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(15))
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DrawerActivity.this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(26) // default
                .diskCacheSize(1000 * 1024 * 1024)
                .diskCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(DrawerActivity.this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);


        imageLoader = ImageLoader.getInstance();
        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
        pref = new Preferences(DrawerActivity.this);
        poplayout = (RelativeLayout) findViewById(R.id.popuplaout);
        popBtn = (Button) findViewById(R.id.popBtn);
        top_menu = (RelativeLayout) findViewById(R.id.top_menu);
        trans_layer = (LinearLayout) findViewById(R.id.trans_layer);
        edit_btn = (Button) findViewById(R.id.edit_btn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        account_setting = (TextView) findViewById(R.id.account_setting_text);
        account_setting.setVisibility(View.GONE);
        draweBtn = (Button) findViewById(R.id.drawerbtn);
        add_people = (Button) findViewById(R.id.add_people);
        seekbar_parent = (LinearLayout) findViewById(R.id.seekbar_parent);
        video_chat = (Button) findViewById(R.id.video_chat);
        msg_btn = (Button) findViewById(R.id.msg_btn);
        minAge = (TextView) findViewById(R.id.min_age_setting);
        maxAge = (TextView) findViewById(R.id.max_age_setting);
        audio_rec = (ImageView) findViewById(R.id.audio_rec);
        video_rec = (ImageView) findViewById(R.id.video_rec);
        save_btn_setting = (TextView) findViewById(R.id.save_btn_setting);
        cancel_btn_setting = (TextView) findViewById(R.id.cancel_btn_setting);
        setting_panel = (RelativeLayout) findViewById(R.id.setting_panel);
        list_for_touchSupport = (ListView) findViewById(R.id.list_for_touch_support);
        poplist = (ListView) findViewById(R.id.poplist);
        makfrnd_lay = (LinearLayout) findViewById(R.id.makefrnd_lay);
        chat_lay = (LinearLayout) findViewById(R.id.chat_lay);
        date_lay = (LinearLayout) findViewById(R.id.date_lay);
        chat_noti = (TextView) findViewById(R.id.chat_noti);
        haw_noti= (TextView) findViewById(R.id.haw_noti);
        gallery_layout = (LinearLayout) findViewById(R.id.gallery_layout);
        photo_layout = (LinearLayout) findViewById(R.id.photo_layout);
        audio_layout = (LinearLayout) findViewById(R.id.audio_layout);
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
        chat_attachment = (LinearLayout) findViewById(R.id.chat_attachment);
        attachment_menu = (RelativeLayout) findViewById(R.id.attachment_menu);
        attachment_layer = (LinearLayout) findViewById(R.id.attachment_layer);
        red_bubble = (LinearLayout) findViewById(R.id.red_bubble);
        red_bubble_haw = (LinearLayout) findViewById(R.id.red_bubble_haw);
        attachment_menu.setVisibility(View.GONE);
        makfrnd_lay.setOnClickListener(this);
        chat_lay.setOnClickListener(this);
        date_lay.setOnClickListener(this);

        //	chat_noti.setVisibility(View.GONE);


        if(NotificationType.event_created_within300km_notification==0){
            red_bubble_haw.setVisibility(View.GONE);
        }
        else{
            red_bubble_haw.setVisibility(View.VISIBLE);
            haw_noti.setText(String.valueOf(NotificationType.event_created_within300km_notification));
        }

        if (prefs.getInt(Constants.chat_notification_count, 0) >= 1) {
            red_bubble.setVisibility(View.VISIBLE);
            red_bubble.setVisibility(View.VISIBLE);
            chat_noti.setText(String.valueOf(prefs.getInt(Constants.chat_notification_count, 0)));
        } else {
            red_bubble.setVisibility(View.GONE);
        }
        attachment_layer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                attachment_menu.setVisibility(View.GONE);
            }
        });

        add_people.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                displayView(12);
            }
        });


        msg_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Constants.chat_notification_count, 0).commit();
                chat_noti.setText("");
                red_bubble.setVisibility(View.GONE);
                displayView(4);
            }
        });

        add_people.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                haw_noti.setText("");
                red_bubble_haw.setVisibility(View.GONE);
               NotificationType.event_created_within300km_notification=0;
                displayView(12);
            }
        });

        setting_panel.setVisibility(View.GONE);
        poplayout.setVisibility(View.GONE);


        poplayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (poplayout.getVisibility() == View.VISIBLE) {
                    poplayout.setVisibility(View.GONE);
                } else {
                    poplayout.setVisibility(View.VISIBLE);
                }
            }
        });


        save_btn_setting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setting_panel.setVisibility(View.GONE);
                trans_layer.setVisibility(View.GONE);
            }
        });

        trans_layer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                trans_layer.setVisibility(View.GONE);
                setting_panel.setVisibility(View.GONE);
                poplayout.setVisibility(View.GONE);
            }
        });

        user_id = pref.get(Constants.KeyUUID);
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(10, 100, DrawerActivity.this);
        seekBar.setSelectedMinValue(30);
        seekBar.setSelectedMaxValue(55);

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i("arvi", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                minAge.setText(minValue.toString());
                maxAge.setText(maxValue.toString());
            }
        });

        seekbar_parent.addView(seekBar);

        p.show();

        getNotificationCount = new getNotificationCountApi(DrawerActivity.this, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
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
            }

        };


        HashMap<String, String> dataForNotification = new HashMap<String, String>();
        dataForNotification.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
        dataForNotification.put("auth_token", pref.get(Constants.kAuthT));
        dataForNotification.put("time_stamp", String.valueOf(cal.getTimeInMillis()));
        getNotificationCount.setPostData(dataForNotification);
        callApi(getNotificationCount);

        db.open();
        getUserProfile();


        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        draweBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    openDrawer();
                } else {
                    closeDrawer();
                }
                poplayout.setVisibility(View.GONE);
            }
        });


        poplist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.d("Position ", "" + position);
                poplayout.setVisibility(View.GONE);
                displayView(position + 40);

            }
        });

        // ===============================
        ba = new MenuAdapter();
        //	ba.addcustum("null");
        Log.d("arv", "arv111" + name);
        ba.addItem(name + "-" + String.valueOf(R.drawable.com_facebook_profile_default_icon));//0
        ba.addItem("Who is online-" + String.valueOf(R.drawable.who_is_online));//1
        ba.addItem("Search-" + String.valueOf(R.drawable.search));//2
        ba.addItem("Custom Search-" + String.valueOf(R.drawable.custom_search));//3
        ba.addItem("chat-" + String.valueOf(R.drawable.text_chat));//4
        ba.addItem("Find Ogbonge Friend-" + String.valueOf(R.drawable.find_ogbonge_friend));//5
        ba.addHeader("YOUR CONNECTION");//6
        ba.addItem("My Favourites-" + String.valueOf(R.drawable.favous));//7
        ba.addItem("Who Fav'd Me-" + String.valueOf(R.drawable.favd_you));//8
        ba.addItem("Who Liked Me-" + String.valueOf(R.drawable.liked_you_ipad));//9
        ba.addItem("Ogbonge Friend's List-" + String.valueOf(R.drawable.ogbonge_friend_list));//10
        ba.addItem("Earn/Get Ogbonge Points-" + String.valueOf(R.drawable.earn_points));//11
        ba.addItem("How About We-" + String.valueOf(R.drawable.how_about_we));//12
        ba.addItem("My Backstage Album-" + String.valueOf(R.drawable.my_backstage_stage));//13
        ba.addItem("Invite Friends-" + String.valueOf(R.drawable.invite_friend));//14
        ba.addItem("Breaking News-" + String.valueOf(R.drawable.breaking_news));//15
        ba.addItem("Settings-" + String.valueOf(R.drawable.privacy_setting_h));//16
        //ba.addItem("Invite-"  	   + String.valueOf(R.drawable.message));//16
        mDrawerList.setAdapter(ba);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        displayView(5);

        red_bubble.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Constants.chat_notification_count, 0).commit();
                chat_noti.setText("");
                red_bubble.setVisibility(View.GONE);
                displayView(4);

            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(GcmIntentService.COPA_MESSAGE);
                chat_noti.setText("1");
            }
        };

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(final SharedPreferences prefs, final String key) {

                Log.d("Settings key changed: ", "" + key);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (key.equalsIgnoreCase(Constants.chat_notification_count)) {
                            red_bubble.setVisibility(View.VISIBLE);
                            chat_noti.setText(String.valueOf(prefs.getInt(Constants.chat_notification_count, 1)));
                        }
                    }
                });

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
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
        // TODO Auto-generated method stub

			/*locationManager	= (LocationManager) getSystemService(LOCATION_SERVICE);

	        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
	           
	        }else{
        	   Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	             startActivity(viewIntent);
	        }*/
        super.onResume();
    }

    public void getprofiledata() {
        if (NotificationType.pass_7days_expiry_time.length() > 0) {
            pass_7_day_long = String.valueOf(Long.parseLong(NotificationType.pass_7days_expiry_time) - mseconds);
            if (Long.parseLong(pass_7_day_long) > 0) {
                CounterClass timer = new CounterClass(Long.parseLong(pass_7_day_long) * 1000, 1000);
                timer.start();
                //   pass_7_day.setVisibility(View.VISIBLE);
                //pass_7_days.setVisibility(View.VISIBLE);

                //	advertise_time.setText(""+((((Long.parseLong(advertise_long) / 60) / 60) / 24)  % 7)+"day,"+" "+(((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) / 60) % 24)+"hours, "+((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) % 60)+"mins ");

            }
            points = NotificationType.UserPoints;
        }
        name = NotificationType.name + ", " + CelUtils.getAgeStrFromDOB(NotificationType.date_of_birth);
        String profileImage = NotificationType.profile_pic;
        if (NotificationType.profile_pic.trim().length() > 0) {

            imgbt = NotificationType.server_id + "/photos_of_you/" + profileImage;
            ba.notifyDataSetChanged();
            return;
        } else {
            imgbt = null;
            return;
        }

    }

    public void setprofile(String imageName) {
        imgbt = NotificationType.server_id + "/photos_of_you/" + imageName;
    }

    private void getUserProfile() {

        // p.show();
        user_profile_info = new user_profile_api(DrawerActivity.this, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);


            }

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
                getprofiledata();
                super.updateUI();
            }
        };


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("other_user_uuid", "");
        map.put("time_stamp", "");
        user_profile_info.setPostData(map);
        callApi(user_profile_info);


    }


    // ******* THREAD API CALL *******


    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            switch (position) {
                case 3:
                    pref.set(Constants.Custom_search_key, "");
                    pref.set(Constants.Custom_search_type, "");
                    pref.commit();
                    break;

                default:
                    break;
            }


            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    @SuppressLint("NewApi")
    public void displayView(int position) {

        // update the com.ogbonge content by replacing fragments
        account_setting_flag = false;
        top_menu.setVisibility(View.VISIBLE);
        account_setting.setVisibility(View.GONE);
        add_people.setVisibility(View.VISIBLE);
        video_chat.setVisibility(View.VISIBLE);
        msg_btn.setVisibility(View.VISIBLE);
        chat_attachment.setVisibility(View.GONE);
        switch (position) {

            case 99:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                fragment = new First_Fragment(DrawerActivity.this);
                current_page = 99;
                break;

            case 0:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                fragment = new ProfilePage(DrawerActivity.this);
                current_page = 0;

                break;

            case 1:
                GetBackFragment.Addpos(position);
                GetBackFragment.Addpos(position);
                pref.set("wio_interestedin_purpose_master_id", 0);
                pref.set("wio_interestedin_master_id",0 );
                pref.set("wio_meet_min_age", 0);
                pref.set("wio_meet_max_age", 0);
                pref.commit();
                fragment = new whoIsOnline(DrawerActivity.this);
                current_page = 1;



                break;

            case 2:
                GetBackFragment.Addpos(position);
                fragment = new Search(DrawerActivity.this);
                current_page = 2;
                break;

            case 3:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                fragment = new Custom_search(DrawerActivity.this);
                current_page = 3;
                break;


            case 4:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
//					Bundle b=new Bundle();
//					b.putString("uuid", pref.get(Constants.KeyUUID));
//					Intent intent = new Intent(DrawerActivity.this, ChatWebView.class);
//					intent.putExtras(b);
//					startActivity(intent);
                current_page = 4;
                fragment = new ChatWebView(DrawerActivity.this);
                break;
            case 5:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 5;
                fragment = new First_Fragment(DrawerActivity.this);
                break;

            case 7:
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("server", "Arvind").commit();
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 7;
                fragment = new Favorities(DrawerActivity.this);

                break;

            case 8:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 8;
                fragment = new WhoFavYou(DrawerActivity.this);

                break;

            case 9:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 9;
                fragment = new whoLikedyou(DrawerActivity.this);

                break;


            case 10:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 10;
                fragment = new MyOgbongeFriendList(DrawerActivity.this);

                break;

            case 11:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 11;
                fragment = new PaymentView(DrawerActivity.this);
                break;

            case 12:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 12;
                fragment = new ShowEvents(DrawerActivity.this);
                break;


            case 13:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 13;
                fragment = new MyBackstageAlbum(DrawerActivity.this);

                break;


            case 14:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                current_page = 14;
                String shareBody = " You are invited on Ogbonge friends, Favourite Hangout for Naija. http://www.ogbongefriends.com/invites/friendSignup/"+pref.get(Constants.KeyUUID);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "OgbongeFriend");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case 15:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 15;
                fragment = new breakingList(DrawerActivity.this);

                break;


            case 16:
                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 16;
                fragment = new AccountSettings(DrawerActivity.this);
                break;


            case 33:

//				popBtn.setVisibility(View.VISIBLE);
//				GetBackFragment.Addpos(position);
//				fragment = new NewsDetails(DrawerActivity.this);


                popBtn.setVisibility(View.VISIBLE);
                GetBackFragment.Addpos(position);
                current_page = 33;
                fragment = new newsWebView(DrawerActivity.this);

                break;


            case 34:
                GetBackFragment.Addpos(position);
                current_page = 34;
                fragment = new EventDetails(DrawerActivity.this);

                break;

            case 35:
                GetBackFragment.Addpos(position);
                current_page = 35;
                fragment = new Circle_img_Gallery(DrawerActivity.this);

                break;


            case 36:
                GetBackFragment.Addpos(position);
                current_page = 36;
                fragment = new AlbumGallery(DrawerActivity.this);

                break;


            case 37:
                GetBackFragment.Addpos(position);
                current_page = 37;
                fragment = new MemberProfile(DrawerActivity.this);

                break;


            case 38:
                GetBackFragment.Addpos(position);
                current_page = 38;
                fragment = new CreateEvent(DrawerActivity.this);
                break;

            case 39:
                GetBackFragment.Addpos(position);
                current_page = 39;
                fragment = new UserFullProfileAlbum(DrawerActivity.this);

                break;


            case 40:
                GetBackFragment.Addpos(position);
                msg_btn.setVisibility(View.GONE);
                current_page = 40;
                fragment = new AccountSettings(DrawerActivity.this);
                break;

            case 41:
                GetBackFragment.Addpos(position);
                current_page = 41;
                fragment = new NotificationSetting(DrawerActivity.this);
                break;

            case 42:
                GetBackFragment.Addpos(position);
                current_page = 42;
                fragment = new Account(DrawerActivity.this);
                break;

            case 43:
                GetBackFragment.Addpos(position);
                current_page = 43;
                fragment = new Change_password(DrawerActivity.this);
                break;

            case 44:
                current_page = 44;
                GetBackFragment.Addpos(position);
                fragment = new albums(DrawerActivity.this);

                break;

            case 45:
                current_page = 45;
                GetBackFragment.Addpos(position);
                fragment = new UserBasicInfo(DrawerActivity.this);
                break;


            case 46:
                current_page = 46;
                GetBackFragment.Addpos(position);
                fragment = new full_users_profile(DrawerActivity.this);
                break;


            case 47:
                current_page = 47;
                GetBackFragment.Addpos(position);
                fragment = new VirtualGifts(DrawerActivity.this);

                break;


            case 48:
                current_page = 48;
                GetBackFragment.Addpos(position);
                fragment = new UserGiftList(DrawerActivity.this);

                break;


            case 49:
                current_page = 49;
                GetBackFragment.Addpos(position);
                fragment = new addPhotoOption(DrawerActivity.this);

                break;

            case 50:
                current_page = 50;
                GetBackFragment.Addpos(position);
                fragment = new basicInfo(DrawerActivity.this);

                break;


            case 51:
                current_page = 51;
                GetBackFragment.Addpos(position);
                fragment = new ContactDetails(DrawerActivity.this);

                break;

            case 52:
                current_page = 52;
                GetBackFragment.Addpos(position);
                fragment = new AboutMe();


                break;

            case 53:
                current_page = 53;
                GetBackFragment.Addpos(position);
                fragment = new inteserstedIn(DrawerActivity.this);


                break;

            case 54:
                current_page = 54;
                GetBackFragment.Addpos(position);
                fragment = new personalInformation(DrawerActivity.this);

                break;


            case 55:
                current_page = 55;
                GetBackFragment.Addpos(position);
                //  fragment = new Earn_Get_Points(DrawerActivity.this);

                break;


            case 56:
                current_page = 56;
                GetBackFragment.Addpos(position);
                fragment = new addPhotoOption(DrawerActivity.this);

                break;


            case 57:
                current_page = 57;
                GetBackFragment.Addpos(position);
                fragment = new MemberAlbum(DrawerActivity.this);
                break;

            case 58:
                current_page = 58;
                GetBackFragment.Addpos(position);
                fragment = new verification(DrawerActivity.this);
                break;

            case 59:
                current_page = 59;
                GetBackFragment.Addpos(position);
                fragment = new Feedback(DrawerActivity.this);
                break;

            case 60:
                current_page = 60;
                GetBackFragment.Addpos(position);
                Intent inte = new Intent(DrawerActivity.this, AddPhotoOptionClass.class);
                startActivity(inte);
                break;


            case 61:
                current_page = 61;
                GetBackFragment.Addpos(position);
                fragment = new setting_web_pages(DrawerActivity.this);
                break;


            case 62:
                current_page = 62;
                GetBackFragment.Addpos(position);
                fragment = new CashForPayment(DrawerActivity.this);
                break;


            case 63:
                current_page = 63;
                GetBackFragment.Addpos(position);
                fragment = new transactionDetails(DrawerActivity.this);
                break;

            case 90:
                current_page = 90;
                GetBackFragment.Addpos(position);
                fragment = new setting(DrawerActivity.this);

                break;

            case 91:
                current_page = 91;
                GetBackFragment.Addpos(position);
                fragment = new WhoIsOnlineSetting(DrawerActivity.this);

                break;

            case 92:
                current_page = 92;
                GetBackFragment.Addpos(position);
                fragment = new StandardSearchSetting(DrawerActivity.this);

                break;


            case 93:
                current_page = 93;
                GetBackFragment.Addpos(position);
                fragment = new promote_fragment(DrawerActivity.this);

                break;

            default:
                current_page = 94;
                account_setting_flag = false;
                account_setting.setVisibility(View.GONE);

                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
            fragmentManager.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out);
            fragmentManager.replace(R.id.frame_container, fragment).addToBackStack(null).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    //================
    private void callApi(Runnable r) {
        try {
            if (!Utils.isNetworkConnectedMainThred(this)) {
                Log.v("", "Internet Not Conneted");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        p.cancel();

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

    //==================================================


    // ================================================
    public class MenuAdapter extends HeaderListAdapter implements View.OnClickListener {

        private View v;

        // ================================================
        @Override
        public View getHeaderView(int pos, View cv, ViewGroup vg, Object o) {

            View v = cv;
            if (cv == null)
                v = LayoutInflater.from(vg.getContext()).inflate(R.layout.menu_header_row, vg, false);
            TextView textView = (TextView) v.findViewById(R.id.textView1);
            textView.setText(String.valueOf(o));
            textView.setTextColor(Color.LTGRAY);

            return v;
        }

        // =============================================
        @Override
        public View getItemView(int pos, View cv, ViewGroup vg, Object o) {

            // final Object n = o;
            v = cv;

            if (pos == 0) {
                v = LayoutInflater.from(vg.getContext()).inflate(
                        R.layout.menu_item_profile_row, vg, false);
                userPhoto = (ImageView) v.findViewById(R.id.imageView1);
                dashboard_points = (TextView) v.findViewById(R.id.points);
                pass_7_days = (TextView) v.findViewById(R.id.pass_7_days);
                pass_7_day = (TextView) v.findViewById(R.id.pass_7_day_date);
                dashboard_points.setText("" + NotificationType.UserPoints);
            } else {
                v = LayoutInflater.from(vg.getContext()).inflate(
                        R.layout.menu_item_row, vg, false);
                imageView = (ImageView) v.findViewById(R.id.imageView1);
            }
            String[] separated = String.valueOf(o).split("-");

            // Log.v("adapter value", String.valueOf(o));
            TextView textView = (TextView) v.findViewById(R.id.textView1);


            textView.setText(separated[0]);


            TextView notifications_count = (TextView) v.findViewById(R.id.notification_block);
            switch (pos) {

                case 0:
                    dashboard_points.setText(NotificationType.UserPoints);

//				mseconds = c.getTimeInMillis()/1000;
//				Log.d("arvi", "arvi"+user_info.getString(user_info.getColumnIndex(Table.user_master.pass_7days_expiry_time.toString())));
//				pass_7_day_long=String.valueOf(Long.parseLong(user_info.getString(user_info
//						.getColumnIndex(user_master.pass_7days_expiry_time.toString())))-mseconds);
//				if(Long.parseLong(pass_7_day_long)>0){
//				pass_7_day.setText(""+((((Long.parseLong(pass_7_day_long) / 60) / 60) / 24)  % 7)+"day,"+" "+(((Long.parseLong(pass_7_day_long) / 60) / 60) % 24)+"hours, "+((Long.parseLong(pass_7_day_long) / 60) % 60)+"mins ");
//				pass_7_days.setVisibility(View.VISIBLE);
//				}
//				
//				else{
//					pass_7_days.setVisibility(View.GONE);
//					
//				}
                    break;

                case 3:

                    notifications_count.setVisibility(View.GONE);
                    notifications_count.setText("4");
                    break;

                case 6:
                    notifications_count.setVisibility(View.GONE);
                    notifications_count.setText("2");
                    break;
                case 7:
                    break;

                case 8:
                    notifications_count.setVisibility(View.VISIBLE);
                    if (NotificationType.someone_favd_you_notification > 0) {
                        notifications_count.setVisibility(View.VISIBLE);
                        notifications_count.setText(String.valueOf(NotificationType.someone_favd_you_notification));
                    } else {
                        notifications_count.setVisibility(View.GONE);
                    }
                    break;
                case 9:
                    notifications_count.setVisibility(View.VISIBLE);

                    if (NotificationType.someone_liked_you_notification > 0) {
                        notifications_count.setVisibility(View.VISIBLE);
                        notifications_count.setText(String.valueOf(NotificationType.someone_liked_you_notification));
                    } else {
                        notifications_count.setVisibility(View.GONE);
                    }
                    break;


                case 15:
                    //notifications_count.setVisibility(View.VISIBLE);
                  /* *//* Cursor dd_news*//* = db.findCursor(Table.Name.red_bubble_master, Table.notification_master.uuid.toString() + " = '" + pref.get(Constants.KeyUUID) + "'", null, null);

                    if (dd_news.getCount() > 0) {
                        dd_news.moveToFirst();
                        Log.d("abc", "abc" + dd_news.getString(dd_news.getColumnIndex(Table.red_bubble_master.newNewsCount.toString())));
                        if (Integer.parseInt(dd_news.getString(dd_news.getColumnIndex(Table.red_bubble_master.newNewsCount.toString()))) > 0) {
                            notifications_count.setVisibility(View.VISIBLE);
                            notifications_count.setText(String.valueOf(dd_news.getString(dd_news.getColumnIndex(Table.red_bubble_master.newNewsCount.toString()))));
                        } else {
                            notifications_count.setVisibility(View.GONE);
                        }

                    }*/
                    if (NotificationType.breaking_news_notification > 0) {
                        notifications_count.setVisibility(View.VISIBLE);
                        notifications_count.setText(String.valueOf(NotificationType.breaking_news_notification));
                    } else {
                        notifications_count.setVisibility(View.GONE);
                    }

                    break;

                default:

                    break;
            }

            try {
                if (pos == 0) {

                    userPhoto.setImageResource(R.drawable.profile);
                    final ProgressBar pb = new ProgressBar(DrawerActivity.this);

                    pb.setVisibility(View.GONE);
                    try {

                        String imgname = "123";
                        String check_name = name;
                        textView.setText(name);


//							final Handler h = new Handler();
//						    h.post(new Runnable() {
//						        @Override
//						        public void run() {
//
//						           //  long millis =(long)c.getTime()
//									mseconds = c.getTimeInMillis()/1000;
//									Log.d("arv", "arv  "+Long.parseLong(data.getString(data
//											.getColumnIndex(user_master.pass_7days_expiry_time.toString()))));
//									pass_7_day_long=String.valueOf(Long.parseLong(data.getString(data
//											.getColumnIndex(user_master.pass_7days_expiry_time.toString())))-mseconds);
//									if(Long.parseLong(pass_7_day_long)>0){
//									pass_7_day.setText(""+((((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) / 60) / 24)  % 7)+"day,"+" "+(((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) / 60) % 24)+"hours, "+((Long.parseLong(DrawerActivity.pass_7_day_long) / 60) % 60)+"mins ");
//									pass_7_days.setVisibility(View.VISIBLE);
//									}
//						           //  dateAndTime.setText(getDate(millis, "dd/MM/yyyy hh:mm:ss.SSS"));
//
//						             h.postDelayed(this, 1000);
//						        }
//						    });


                        // dashboard_points.setText(""+DrawerActivity.points);

                        try {
                            imgname = separated[1];
                        } catch (Exception e) {

                        }
                        if (imgname.length() > 5 && imgbt != null) {

                            try {

                                imageLoader.displayImage(getString(R.string.profile_image_url) + imgbt, userPhoto, options_rounded, new ImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(String arg0, View arg1) {
                                        // TODO Auto-generated method stub
                                        // pb.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                                        // TODO Auto-generated method stub
                                        pb.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                                        // TODO Auto-generated method stub
                                        pb.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onLoadingCancelled(String arg0, View arg1) {
                                        // TODO Auto-generated method stub
                                        pb.setVisibility(View.GONE);
                                    }
                                });

                                userPhoto.setScaleType(ScaleType.FIT_XY);

                                switch (screenSize) {
                                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                                        userPhoto.getLayoutParams().width = 150;
                                        userPhoto.getLayoutParams().height = 160;

                                        break;
                                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                                        userPhoto.getLayoutParams().width = 100;
                                        userPhoto.getLayoutParams().height = 110;
                                        break;
                                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                                        userPhoto.getLayoutParams().width = 90;
                                        userPhoto.getLayoutParams().height = 100;
                                        break;
                                    default:
                                        userPhoto.getLayoutParams().width = 100;
                                        userPhoto.getLayoutParams().height = 110;
                                }


                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            // imageView.setImageBitmap(Utils.getImageFromLocal(
                            // getApplicationContext(), imgbt));
                        } else {
                            userPhoto.setImageResource(R.drawable.profile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    imageView.getLayoutParams().width = (int) getResources()
                            .getDimension(R.dimen.slider_icon_width);
                    imageView.getLayoutParams().height = (int) getResources()
                            .getDimension(R.dimen.slider_icon_height);
                    imageView.setImageResource(Integer.parseInt(separated[1]));
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                }
            } catch (Exception e) {

            }
            return v;
        }

        // =============================================
        @Override
        public View getCustView(int pos, View cv, ViewGroup vg, Object o) {

            // final Object n = o;
            v = cv;
            if (cv == null)
                v = LayoutInflater.from(vg.getContext()).inflate(
                        R.layout.menu_custom_row, vg, false);

            EditText editText = (EditText) v.findViewById(R.id.rgisemail);
            editText.setHint("Search");

            editText.setPadding(10, 0, 10, 0);
            editText.clearFocus();
            editText.setInputType(InputType.TYPE_NULL);

            editText.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {


                }
            });
            // v.setOnClickListener(editViewListener);

            return v;
        }

        @Override
        public void onClick(View arg0) {


        }

        // =============================================

    }

    public void openDrawer() {

        db.open();
        poplayout.setVisibility(View.GONE);
        setting_panel.setVisibility(View.GONE);
        trans_layer.setVisibility(View.GONE);
        Utils.hideSoftKeyboard(DrawerActivity.this);
        mDrawerLayout.openDrawer(mDrawerList);
        ba.notifyDataSetChanged();

    }

    public void closeDrawer() {

        mDrawerLayout.closeDrawer(mDrawerList);

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (Constants.isWebLoading == false) {
            DrawerActivity.chat_attachment.setVisibility(View.GONE);
            DrawerActivity.popBtn.setVisibility(View.VISIBLE);
            if (MemberProfile.isLikeOpen == false) {
                try {
                    int ast = GetBackFragment.Lastpos();

                    if (ast == -1) {

                        ExitAlert();

                    } else {
                        if (ast == 1) {
                            long g = GetBackFragment.LastUUID();
                            if (g != 0) {
//							edit.putLong("other_user", g).commit();
//							newpref.set(Constants.OtherUser, String.valueOf(g)).commit();
                                GetBackFragment.Removelast();
                            }

                        }
                        //	displayView(ast);
                        super.onBackPressed();
                        //GetBackFragment.Removepos();
                        GetBackFragment.Removepos();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                MemberProfile.isLikeOpen = false;
                MemberProfile.bottom_layout.setVisibility(View.GONE);
            }
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            default:
                break;
        }

    }


    private void ExitAlert() {

        final Dialog dialog = new Dialog(DrawerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        ok_btn.setText("YES");
        cancel_btn.setText("NO");
        title.setText(getString(R.string.app_name));
        msg.setText("Would you like to Exit ?");
        dialog.show();
        cancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                CelUtils.turnGPSDisable(DrawerActivity.this);


                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        // show it
        dialog.show();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());


            Toast.makeText(getApplicationContext(),
                    "Got Message: " + newMessage,
                    Toast.LENGTH_LONG).show();

            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
          //  unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.e("UnRegister Receiver Error",
                    "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            pass_7_day.setText("");
            pass_7_days.setText("");
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02dday: %02dhour: %02dmin: %02dsec", TimeUnit.MILLISECONDS.toDays(millis), TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            if (pass_7_day != null) {
                pass_7_days.setVisibility(View.VISIBLE);
                pass_7_day.setText(hms);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) {
        // TODO Auto-generated method stub

        Toast.makeText(DrawerActivity.this, "Changed", Toast.LENGTH_LONG).show();

    }

}