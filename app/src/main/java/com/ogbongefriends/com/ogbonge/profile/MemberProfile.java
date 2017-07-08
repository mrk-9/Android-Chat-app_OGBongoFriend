package com.ogbongefriends.com.ogbonge.profile;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ogbongefriends.com.ogbonge.fragment.Get_favourites_API;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.GetUserGiftApi;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.api.getBanner;
import com.ogbongefriends.com.api.sendFriendRequestApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressLint("NewApi")
public class MemberProfile extends Fragment implements Runnable, OnClickListener, AnimationListener {

    private ArrayList<HashMap<String, String>> feedData;
    private ArrayList<HashMap<String, String>> placeData;
    private ArrayList<HashMap<String, String>> eventsData;

    @SuppressWarnings("unused")
    private EditText posttetx;
    @SuppressWarnings("unused")
    private Button post;
    private Handler mHandler = new Handler();
    private Animation slideUp, slideDown;
    private Button attchbtn;
    private boolean slideUpFlag = false;
    public static boolean isLikeOpen = false;
    private Uri imageUri;
    private int gender = 0, login_status = -1;
    private com.ogbongefriends.com.api.getBanner getBanner;
    private LinearLayout super_parent, like_parent_layout, add_favorites_parent_layout, linearLayout3, center_layout;
    public static LinearLayout bottom_layout;
    private Uri selectedImage;
    private long str;
    private TextView about_me, name_age, location, last_seen, like_text, add_favorite_bottom_text, photo_text, gift_text;
    private ImageView r1, r2, r3, r4, r5;
    Cursor data;
    Cursor eventdatacorsor;
    Cursor followerdatacorsor;
    Cursor followingdatacorsor;
    Cursor secfollowingdatacorsor;
    private ImageView profileImage, rating_larger, photo_larger, gift_larger;
    private LinearLayout gift_con, like_con, prof_con, rating_parent, gift_parent, photo_parent, chat_con;
    private RelativeLayout top_portion;
    private DB db;
    private AdView mAdView;
    private final int NUM_OF_ROWS_PER_PAGE = 10;
    ArrayList<HashMap<String, String>> followerdata;
    FragmentManager fragmentManager;
    @SuppressLint("NewApi")
    Fragment fragment;
    Cursor placedatacursor;
    View rootView;
    private int UserRating = 0;
    private Boolean bottom_visibility = false;
    Notification nt;
    private int like_status = 0, favorite_status;
    int count = 0;
    int screenSize;
    int banner_image_index = 0;
    //private Gallery userGallery;
    private Context _ctx;
    private Preferences pref;
    private CustomLoader p;
    private String otherUser_uuid = "";
    private user_profile_api user_profile_info;
    private TextView loading, rating_text, like_text_outer, user_rating;
    private ImageView user_profile, online_img;
    private Handler handler;
    private sendFriendRequestApi sendFriendRequest;
    private com.ogbongefriends.com.api.getAlbumApi getAlbumApi;
    private GetUserGiftApi getUserGift;
    private Get_favourites_API getfavoriteApi;
    private ImageView up_arrow_like, add_favorite_image, like_image, banner;
    private LinearLayout.LayoutParams buttonLayoutParams;
    private ArrayList<String> bannerUrls = new ArrayList<String>();

    public MemberProfile() {}
    public MemberProfile(Context ctx) {
        _ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.member_profile, container, false);
            //userGallery=(Gallery)rootView.findViewById(R.id.userGallery);
            rating_larger = (ImageView) rootView.findViewById(R.id.ImageView01);
            photo_larger = (ImageView) rootView.findViewById(R.id.ImageView02);
            gift_larger = (ImageView) rootView.findViewById(R.id.imageView03);
            add_favorite_image = (ImageView) rootView.findViewById(R.id.add_favorite_image);
            like_image = (ImageView) rootView.findViewById(R.id.like_image);
            center_layout = (LinearLayout) rootView.findViewById(R.id.center_layout);
            gift_con = (LinearLayout) rootView.findViewById(R.id.gift_con);
            up_arrow_like = (ImageView) rootView.findViewById(R.id.up_arrow_like);
            like_con = (LinearLayout) rootView.findViewById(R.id.like_con);
            chat_con = (LinearLayout) rootView.findViewById(R.id.chat_con);
            bottom_layout = (LinearLayout) rootView.findViewById(R.id.bottom_layout);
            rating_parent = (LinearLayout) rootView.findViewById(R.id.rating_parent);
            gift_parent = (LinearLayout) rootView.findViewById(R.id.gift_parent);
            like_parent_layout = (LinearLayout) rootView.findViewById(R.id.like_parent);
            add_favorites_parent_layout = (LinearLayout) rootView.findViewById(R.id.addfavorites_parent);
            photo_parent = (LinearLayout) rootView.findViewById(R.id.photo_parent);
            about_me = (TextView) rootView.findViewById(R.id.about_me);
            user_profile = (ImageView) rootView.findViewById(R.id.user_profile);
            loading = (TextView) rootView.findViewById(R.id.loding_text);
            prof_con = (LinearLayout) rootView.findViewById(R.id.prof_con);
            linearLayout3 = (LinearLayout) rootView.findViewById(R.id.linearLayout3);
            top_portion = (RelativeLayout) rootView.findViewById(R.id.top_portion);
            super_parent = (LinearLayout) rootView.findViewById(R.id.super_parent);
            name_age = (TextView) rootView.findViewById(R.id.name_age);
            location = (TextView) rootView.findViewById(R.id.location);
            last_seen = (TextView) rootView.findViewById(R.id.last_seen);
            rating_text = (TextView) rootView.findViewById(R.id.rating_text);
            user_rating = (TextView) rootView.findViewById(R.id.user_rating);
            like_text = (TextView) rootView.findViewById(R.id.like_bottom_text);
            like_text_outer = (TextView) rootView.findViewById(R.id.like_text);
            online_img = (ImageView) rootView.findViewById(R.id.online_img);
            add_favorite_bottom_text = (TextView) rootView.findViewById(R.id.add_favorites_bottom_text);
            photo_text = (TextView) rootView.findViewById(R.id.photo_text);
            gift_text = (TextView) rootView.findViewById(R.id.gift_text);
            banner = (ImageView) rootView.findViewById(R.id.banner);
            up_arrow_like.setVisibility(View.GONE);
            full_users_profile.backstage_purchase_status = 0;
            mAdView = (AdView) rootView.findViewById(R.id.adView);


            //buttonLayoutParams = new  LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            //buttonLayoutParams.setMargins(0, 0, 0, 5);
            //linearLayout3.setLayoutParams(buttonLayoutParams);

            r1 = (ImageView) rootView.findViewById(R.id.r1);
            r2 = (ImageView) rootView.findViewById(R.id.r2);
            r3 = (ImageView) rootView.findViewById(R.id.r3);
            r4 = (ImageView) rootView.findViewById(R.id.r4);
            r5 = (ImageView) rootView.findViewById(R.id.r5);

            slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
            slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
            screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;
            slideUp.setAnimationListener(this);
            slideDown.setAnimationListener(this);
            rating_larger.setOnClickListener(this);
            photo_larger.setOnClickListener(this);
            gift_larger.setOnClickListener(this);
            center_layout.setOnClickListener(this);
            //favourite_con.setOnClickListener(this);
            gift_con.setOnClickListener(this);
            like_con.setOnClickListener(this);
            chat_con.setOnClickListener(this);
            //add_peo_con.setOnClickListener(this);
            prof_con.setOnClickListener(this);
            photo_parent.setOnClickListener(this);
            rating_parent.setOnClickListener(this);
            gift_parent.setOnClickListener(this);
            like_parent_layout.setOnClickListener(this);
            add_favorites_parent_layout.setOnClickListener(this);
            pref = new Preferences(_ctx);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            fragmentManager = getFragmentManager();
            followerdata = new ArrayList<HashMap<String, String>>();
            p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
            db = new DB(getActivity());
            p.show();
            getBanner = new getBanner(_ctx, db, p) {

                @Override
                protected void onError(Exception e) {
                    // TODO Auto-generated method stub
                    super.onError(e);
                }

                @Override
                protected void onDone() {
                    // TODO Auto-generated method stub
                    super.onDone();
                    getBannerUrl();
                    getUserProfile();

                }

                @Override
                protected void updateUI() {
                    // TODO Auto-generated method stub
                    super.updateUI();
                }


            };
            otherUser_uuid = pref.get(Constants.OtherUser);

            getbanners();


            handler = new Handler();
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub


                    // TODO Auto-generated method stub
                    if (banner_image_index >= 0) {
                        if (bannerUrls.size() > 0) {
                            mAdView.setVisibility(View.GONE);
                            banner.setVisibility(View.VISIBLE);

                         //   imageLoader.displayImage(bannerUrls.get(banner_image_index), banner);
                            Glide.with(_ctx).load((bannerUrls.get(banner_image_index))).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(banner);

                            Log.d("arvind", "arvind grab gift");
                            banner_image_index++;
                            if (banner_image_index >= bannerUrls.size() - 1) {
                                banner_image_index = 0;
                            }

                            handler.postDelayed(this, 3000); //set to go off again in 3 seconds.
                        } else {
                            mAdView.setVisibility(View.VISIBLE);
                            banner.setVisibility(View.GONE);
                            handler.postDelayed(this, 10);
                        }
                    }
                }
            });

            handler.postDelayed(this, 10);




        rootView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //your code here
                    Log.d("arv", "arv test");
                    return false;
                } else {
                    return true;
                }
            }
        });


        final AdRequest.Builder adReq = new AdRequest.Builder();
        final AdRequest adRequest = adReq.build();

        // AdRequest adRequest = new AdRequest.Builder().build();
       /* mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));*/
        mAdView.loadAd(adRequest);
        }
        return rootView;

    }

    private void getbanners() {

        HashMap<String, String> data = new HashMap<String, String>();

        data.put("auth_token", pref.get(Constants.kAuthT));
        data.put("uuid", pref.get(Constants.KeyUUID));

        data.put("type", "5");
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                data.put("device_type", "3");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                data.put("device_type", "2");
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                data.put("device_type", "2");
                break;
            default:

        }

        getBanner.setPostData(data);
        callApi(getBanner);

    }


    public void getBannerUrl() {
        db.open();
        Cursor bannerData = db.findCursor(DB.Table.Name.banner_master, DB.Table.banner_master.status.toString() + " = 1", null, null);
        Log.d("data sizee", "" + bannerData.getCount());
        bannerData.moveToFirst();


        for (int i = 0; i < bannerData.getCount(); i++) {
            if (bannerData.getString(bannerData.getColumnIndex(DB.Table.banner_master.image.toString())).length() > 3) {


                String url = "http://www.ogbongefriends.com/userdata/banners/" + bannerData.getString(bannerData.getColumnIndex(DB.Table.banner_master.image.toString()));

                bannerUrls.add(i, url);
            }
            bannerData.moveToNext();
        }
        db.close();
    }


    private void getUserProfile() {
        user_profile_info = new user_profile_api(getActivity(), db, p) {

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
                getAlbums();
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub

                super.updateUI();
                //
            }

        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("other_user_uuid", otherUser_uuid);
        map.put("time_stamp", "");
        user_profile_info.setPostData(map);
        callApi(user_profile_info);
    }


    public void getAlbums() {

        getAlbumApi = new getAlbumApi(_ctx, db, p) {

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
                getuserGifts();


            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub

                super.updateUI();

            }

        };
        HashMap<String, String> keydata = new HashMap<String, String>();
        keydata.put("uuid", pref.get(Constants.KeyUUID));
        keydata.put("auth_token", pref.get(Constants.kAuthT));
        keydata.put("time_stamp", "");
        keydata.put("other_uuid", otherUser_uuid);
        getAlbumApi.setPostData(keydata);
        callApi(getAlbumApi);
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


    public void getuserGifts() {

        getUserGift = new GetUserGiftApi(_ctx, db, p) {

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        p.cancel();
                        //if(getAlbumApi.photos_of_you.size()>0){
                        Log.d("P{hoto Countttt", "arvi " + getUserGift.userdata.size());
                        gift_text.setText("" + getUserGift.userdata.size());

                        showuser(otherUser_uuid);

                        //}

                    }
                });

            }

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
                //getAllUsersFroBanner();
            }
        };


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("other_uuid", otherUser_uuid);
        map.put("time_stamp", "");
        map.put("type", "2");
        getUserGift.setPostData(map);
        callApi(getUserGift);
    }


    private void showuser(String uuid) {
        // TODO Auto-generated method stub
        db.open();
        Cursor temp;
        data = db.findCursor(DB.Table.Name.user_master, " uuid=" + "'" + uuid + "'", null, null);
        Log.d("data sizee", "" + data.getCount());
        data.moveToFirst();

        try {

            about_me.setText(data.getString(data.getColumnIndex(DB.Table.user_master.handle_description.toString())));

            //  name_age.setText(data.getString(data.getColumnIndex(Table.user_master.first_name.toString()))+" "+data.getString(data.getColumnIndex(Table.user_master.last_name.toString()))+","+CelUtils.getAgeStrFromDOB(data.getString(data.getColumnIndex(Table.user_master.date_of_birth.toString()))));

            name_age.setText(data.getString(data.getColumnIndex(DB.Table.user_master.first_name.toString())) + " " + data.getString(data.getColumnIndex(DB.Table.user_master.last_name.toString())) + "," + CelUtils.getAgeStrFromDOB(data.getString(data.getColumnIndex(DB.Table.user_master.date_of_birth.toString()))));
            location.setText("" + data.getString(data.getColumnIndex(DB.Table.user_master.city.toString())));
            gender = data.getInt(data.getColumnIndex(DB.Table.user_master.gender.toString()));
            login_status = Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_master.login_status.toString())));
            if (gender == 1) {

                if (data.getInt(data.getColumnIndex(DB.Table.user_master.friend_status.toString())) == 0) {
                    like_text.setText("I Like Him");
                    like_image.setImageResource(R.drawable.favourities);
                    like_status = 0;
                } else {
                    like_text.setText("I Liked Him");
                    like_image.setImageResource(R.drawable.red);
                    like_status = 1;
                }
            } else {
                if (data.getInt(data.getColumnIndex(DB.Table.user_master.friend_status.toString())) == 0) {
                    like_text.setText("I Like Her");
                    like_image.setImageResource(R.drawable.favourities);
                    like_status = 0;
                } else {
                    like_text.setText("I Liked Her");
                    like_image.setImageResource(R.drawable.red);
                    like_status = 1;
                }
            }
            if (data.getInt(data.getColumnIndex(DB.Table.user_master.favourite_status.toString())) == 0) {
                add_favorite_bottom_text.setText("Add to Favorites");
                add_favorite_image.setImageResource(R.drawable.favourite);
                favorite_status = 0;
            } else {
                add_favorite_bottom_text.setText("Added to Favorites");
                add_favorite_image.setImageResource(R.drawable.green);
                favorite_status = 1;
            }


            Log.d("arvi", "arvi " + data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString())));
            Log.d("aaaaaaaaa", "" + CelUtils.dateDifrence(CelUtils.StringToDate(Constants.kTimeStampFormat, data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString()))).getTime()));


            if (login_status == 0) {
                online_img.setImageResource(R.drawable.grey_dot);

            } else {
                //last_seen.setVisibility(View.GONE);
                online_img.setImageResource(R.drawable.green_dot);
            }


            //online_img.setVisibility(View.GONE);
            if (data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString())).length() > 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date timestamp = null;
                try {
                    timestamp = df.parse(data.getString(data.getColumnIndex(DB.Table.user_master.last_seen.toString())));
                    timestamp.setHours(timestamp.getHours() + 4);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                timestamp.getTime();

                String parsed = df.format(timestamp);
                long tim = 0;
                try {
                    tim = df.parse(parsed).getTime();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println("New = " + parsed);
                long milli =
                        Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();

                Log.d("ARVI", "" + (milli - tim) / (1000 * 60) + "min");
                long millis = milli - tim;

                String hms = "";
                if (TimeUnit.MILLISECONDS.toDays(millis) > 0) {

                    if (TimeUnit.MILLISECONDS.toDays(millis) > 14) {
                        hms = String.format("%02d weeks ", TimeUnit.MILLISECONDS.toDays(millis));
                        last_seen.setText("more than 2 weeks ago");

                    } else {
                        if (TimeUnit.MILLISECONDS.toDays(millis) > 1) {
                            hms = String.format("%02d days ", TimeUnit.MILLISECONDS.toDays(millis));
                        } else {
                            hms = String.format("%02d day ", TimeUnit.MILLISECONDS.toDays(millis));
                        }
                        last_seen.setText("Last visit " + hms + " ago");
                    }
                } else {
                    if ((TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))) > 0) {
                        hms = String.format("%02d hour ", TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)));
                    } else {
                        if ((TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) > 0) {
                            hms = String.format("%02d min ", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
                        } else {
                            if ((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))) > 0) {
                                hms = String.format("%02d sec", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                            }
                        }
                    }
                    last_seen.setText("Last visit " + hms + " ago");
                }


            }
            //}


            if (data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString())).length() > 3) {
                String url = getActivity().getString(R.string.urlString) + "userdata/image_gallery/" + data.getString(data.getColumnIndex(DB.Table.user_master.server_id.toString())) + "/photos_of_you/" + data.getString(data.getColumnIndex(DB.Table.user_master.profile_pic.toString()));
                Log.d("arv Image URL", "arv image" + url);
                UserRating = Integer.parseInt(data.getString(data.getColumnIndex(DB.Table.user_master.rate_count.toString())));
                //gift_text.setText(data.getString(data.getColumnIndex(Table.user_master.gift_count.toString())));
                //gift_text.setText(pref.getInt(Constants.size));
                photo_text.setText(data.getString(data.getColumnIndex(DB.Table.user_master.photo_count.toString())));


                user_profile.setScaleType(ScaleType.FIT_XY);
                Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(user_profile);

                /*imageLoader.displayImage(url, user_profile, options, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                        // TODO Auto-generated method stub
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                        // TODO Auto-generated method stub
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        // TODO Auto-generated method stub
                        loading.setVisibility(View.GONE);
                    }
                });*/

                //rating_text.setText(""+UserRating);
                user_rating.setText("" + UserRating);
                if (UserRating == 0) {
                    r1.setImageResource(R.drawable.no_rating);
                    r2.setImageResource(R.drawable.no_rating);
                    r3.setImageResource(R.drawable.no_rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (UserRating < 1 && UserRating > 0) {
                    r1.setImageResource(R.drawable.small_half_star);
                    r2.setImageResource(R.drawable.no_rating);
                    r3.setImageResource(R.drawable.no_rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }
                if (UserRating == 1) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.no_rating);
                    r3.setImageResource(R.drawable.no_rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (1 < UserRating && UserRating < 2) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.small_half_star);
                    r3.setImageResource(R.drawable.no_rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (UserRating == 2) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.no_rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (2 < UserRating && UserRating < 3) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.small_half_star);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (UserRating == 3) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.rating);
                    r4.setImageResource(R.drawable.no_rating);
                    r5.setImageResource(R.drawable.no_rating);
                }


                if (3 < UserRating && UserRating < 4) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.rating);
                    r4.setImageResource(R.drawable.small_half_star);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (UserRating == 4) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.rating);
                    r4.setImageResource(R.drawable.rating);
                    r5.setImageResource(R.drawable.no_rating);
                }

                if (4 < UserRating && UserRating < 5) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.rating);
                    r4.setImageResource(R.drawable.rating);
                    r5.setImageResource(R.drawable.small_half_star);
                }

                if (UserRating == 5) {
                    r1.setImageResource(R.drawable.rating);
                    r2.setImageResource(R.drawable.rating);
                    r3.setImageResource(R.drawable.rating);
                    r4.setImageResource(R.drawable.rating);
                    r5.setImageResource(R.drawable.rating);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            p.cancel();
        }
    }

    public void AddTofavorites() {
        p.show();
        getfavoriteApi = new Get_favourites_API(_ctx, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
                //getfavoriteApi.
                if (getfavoriteApi.resCode == 1) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //favorite_image.setEnabled(false);
                            //p.cancel();

                        }
                    });
                }
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (sendFriendRequest.resCode == 1) {
                            if (favorite_status == 0) {
                                Utils.same_id("Message", name_age.getText() + " has been added successfully in your favourites", _ctx);
                                favorite_status = 1;
                                add_favorite_bottom_text.setText("Added to Favorites");
                                add_favorite_image.setImageResource(R.drawable.green);
                            } else {
                                Utils.same_id("Message", name_age.getText() + " has been Removed successfully in your favourites", _ctx);
                                favorite_status = 0;
                                add_favorite_bottom_text.setText("Add to Favorites");
                                add_favorite_image.setImageResource(R.drawable.favourite);
                            }
                        }

                        p.cancel();
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


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("other_uuid", otherUser_uuid);
        map.put("time_stamp", "");
        map.put("type", "3");
        map.put("page_index", "1");

        getfavoriteApi.setPostData(map);
        callApi(getfavoriteApi);

    }


    public void sendFriendReq() {
        if (like_status == 0) {

            sendFriendRequest = new sendFriendRequestApi(getActivity(), db, p) {

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
                            if (sendFriendRequest.resCode == 1) {
                                if (like_status == 0) {
                                    Utils.same_id("Message", "You Liked Successfully", getActivity());
                                    like_status = 1;
                                    if (gender == 1) {
                                        like_text.setText("I liked him");
                                    } else {
                                        like_text.setText("I liked her");
                                    }
                                    like_image.setImageResource(R.drawable.red);


                                } else {
                                    Utils.same_id("Message", "You Unliked Successfully", getActivity());
                                    like_status = 0;

                                    if (gender == 1) {
                                        like_text.setText("I like him");
                                    } else {
                                        like_text.setText("I like her");
                                    }
                                    like_image.setImageResource(R.drawable.favourities);


                                }
                            }

                            p.cancel();
                        }
                    });
                }

                @Override
                protected void onResponseReceived(InputStream is) {
                    // TODO Auto-generated method stub
                    super.onResponseReceived(is);

                }

                @Override
                protected void updateUI() {
                    // TODO Auto-generated method stub
                    super.updateUI();
                }

            };


            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uuid", pref.get(Constants.KeyUUID));
            map.put("auth_token", pref.get(Constants.kAuthT));
            map.put("receiver_uuid", otherUser_uuid);
            if (like_status == 0) {
                map.put("type", "1");
            } else {
                map.put("type", "2");
            }
            sendFriendRequest.setPostData(map);
            p.show();
            callApi(sendFriendRequest);
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.ImageView01:

                Toast.makeText(getActivity(), "Rating Button", Toast.LENGTH_LONG).show();
                break;

            case R.id.ImageView02:
                Toast.makeText(getActivity(), "Photo Button", Toast.LENGTH_LONG).show();
                break;

            case R.id.imageView03:
                Toast.makeText(getActivity(), "Gift Button", Toast.LENGTH_LONG).show();
                break;

            case R.id.gift_con:
                pref.set(Constants.OtherUser, otherUser_uuid);
                pref.set(Constants.OtherUserName, name_age.getText().toString());
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(47);


                break;

            case R.id.chat_con:
                pref.set("chat_other", otherUser_uuid);
                //	pref.set(Constants.OtherUserName,name_age.getText().toString());
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(4);


                break;


            case R.id.center_layout:
                isLikeOpen = false;
                bottom_layout.setVisibility(View.GONE);
//			//sendFriendReq();
//			buttonLayoutParams.setMargins(0, 0, 0, 5);
//			linearLayout3.setLayoutParams(buttonLayoutParams);
//			bottom_layout.setVisibility(View.VISIBLE);
//			bottom_layout.setVisibility(View.GONE);
//			up_arrow_like.setVisibility(View.GONE);
                break;

            case R.id.like_con:
                isLikeOpen = true;
                //sendFriendReq();
//			buttonLayoutParams.setMargins(0, 0, 0, -15);
//			linearLayout3.setLayoutParams(buttonLayoutParams);
                bottom_layout.setVisibility(View.VISIBLE);
                //up_arrow_like.setVisibility(View.VISIBLE);
                break;


            case R.id.photo_parent:
                pref.set("images_of", otherUser_uuid);
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(57);

                break;

            case R.id.gift_parent:
                Log.d("arv", "arv " + otherUser_uuid);
                pref.set(Constants.OtherUser, otherUser_uuid);
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(48);

                break;

            case R.id.like_parent:

                sendFriendReq();

                break;

            case R.id.addfavorites_parent:

                AddTofavorites();
                break;

            case R.id.prof_con:
//			if(!slideUpFlag)
//			{
//				slideUpFlag=true;
//				bottom_portion.setVisibility(View.VISIBLE);
//				top_portion.startAnimation(slideUp);
//		}
//			else{
//				slideUpFlag=false;
//				top_portion.startAnimation(slideDown);
//				
//			}

                ((DrawerActivity) getActivity()).displayView(46);

                break;

            default:
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }
}
