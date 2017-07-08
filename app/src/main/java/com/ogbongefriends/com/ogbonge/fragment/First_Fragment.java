package com.ogbongefriends.com.ogbonge.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.api.sendFriendRequestApi;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.ImageAdapter;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class First_Fragment extends Fragment implements Runnable, OnClickListener, MyLocationListener {

    // private ListView listView;
    private ArrayList<HashMap<String, String>> feedData;
    @SuppressWarnings("unused")
    private EditText posttetx;
    @SuppressWarnings("unused")
    private Button post;
    private Button attchbtn;
    private Uri imageUri;
    private int current_gallery_position=-1;
    private Uri selectedImage;
    private LinearLayout ll_user;
    private RelativeLayout user_info_layout;
    private ProgressBar pb1;
    private long str;
    private TextView username_age, img_count;
    Cursor data;
    Cursor eventdatacorsor;
    Cursor followerdatacorsor;
    Cursor followingdatacorsor;
    Cursor secfollowingdatacorsor;
    private LocationHelper mLocationHelper;
    private ImageView skip_image, liked_image, promote_yourself;
    private Button remave_img_btn;
    private ImageView profileImage, next_image, prev_image;
    private ImageView dialogProfileImage;
   private Runnable runn;
    LinearLayout.LayoutParams lp;
    private Button showPlaceBtn;
    private Button showEventBtn;
    private long other_user;
    private int total_img = 0, current_img = 0;
    private String age;
    private JsonArray unfriendsUsers;
    private long loginId;

    public Integer[] Imgid = {R.drawable.app_icon, R.drawable.add_me_here, R.drawable.blue_favourite,
            R.drawable.blue_like};

    private ArrayList<String> user_photo_url;
    private ArrayList<Integer> user_photo_id;
    private CheckBox chfollow;
    private CheckBox chsecretfollow;
    private final int NUM_OF_ROWS_PER_PAGE = 10;
    ArrayList<HashMap<String, String>> followerdata;
    FragmentManager fragmentManager;
    @SuppressLint("NewApi")
    Fragment fragment;
    Cursor placedatacursor;
    private ImageView rate1, rate2, rate3, rate4, rate5, gift_images;
    private int ratingValue, current_user = 1;
    private String selected_user, current_user_server_id;
    private TextView profile_user_name, grab_his, loading;
    View rootView;
    Notification nt;
    int count = 0;
    private Gallery userGallery;
    private RelativeLayout nophotos;

    private int position = 0, unfrienduser_page_index = 1, unfrienduser_page_index_available;

    private Button show_full_prof_btn;
    public static CustomLoader p;
    private DB db;
    int image_index = 0;
    private Preferences pref;
    private Location location = null;
    private getAllUserProfileAPI get_all_user_pfro_api;
    private sendFriendRequestApi sendFriendRequest;
    private Context _ctx;
    private getAlbumApi get_album_api;
    private RelativeLayout grab_attention_layout;
    private JsonArray photos_of_you;
    private ImageAdapter imageAdapter;
    Animation anim;
    Handler handler;
    ImageView iv;
    int countUrl;
    Runnable galleryAnimRunnable;
    private ArrayList<String> GiftUrls = new ArrayList<String>();
    public First_Fragment(){

    }
    public First_Fragment(Context ctx) {
        _ctx = ctx;
        selected_user = "";
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.first_fragment, container, false);
            // FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
            // LikeView likeView =
            // (LikeView)rootView.findViewById(R.id.like_view);
            // likeView.setObjectIdAndType( "https://www.facebook.com/you_page",
            // LikeView.ObjectType.PAGE);
            // https://www.facebook.com/pujashukla1293
            p = DrawerActivity.p;
            db = new DB(getActivity());
            p.show();
            user_photo_id = new ArrayList<Integer>();
            user_photo_url = new ArrayList<String>();
            photos_of_you = new JsonArray();
            unfriendsUsers = new JsonArray();
            pb1=(ProgressBar)rootView.findViewById(R.id.progressBar);
            userGallery = (Gallery) rootView.findViewById(R.id.gallery1);
            next_image = (ImageView) rootView.findViewById(R.id.next_btn);
            prev_image = (ImageView) rootView.findViewById(R.id.prev_btn);
            show_full_prof_btn = (Button) rootView.findViewById(R.id.show_fullprofile_btn);
            ll_user = (LinearLayout) rootView.findViewById(R.id.parent_user_images);
            profile_user_name = (TextView) rootView.findViewById(R.id.profile_user_name);
            img_count = (TextView) rootView.findViewById(R.id.img_count);
            loading = (TextView) rootView.findViewById(R.id.loading);
            rate1 = (ImageView) rootView.findViewById(R.id.rate1_img);
            rate2 = (ImageView) rootView.findViewById(R.id.rate2_img);
            rate3 = (ImageView) rootView.findViewById(R.id.rate3_img);
            rate4 = (ImageView) rootView.findViewById(R.id.rate4_img);
            rate5 = (ImageView) rootView.findViewById(R.id.rate5_img);
            gift_images = (ImageView) rootView.findViewById(R.id.imageView1);
            promote_yourself = (ImageView) rootView.findViewById(R.id.promote_yourself);
            username_age = (TextView) rootView.findViewById(R.id.username_age);
            grab_his = (TextView) rootView.findViewById(R.id.grab_his);
            skip_image = (ImageView) rootView.findViewById(R.id.skip_image);
            liked_image = (ImageView) rootView.findViewById(R.id.liked_image);
            nophotos = (RelativeLayout) rootView.findViewById(R.id.nophoto);
            grab_attention_layout = (RelativeLayout) rootView.findViewById(R.id.grab_attention_layout);
            user_info_layout = (RelativeLayout) rootView.findViewById(R.id.user_info_layout);
            show_full_prof_btn.setOnClickListener(this);
            next_image.setOnClickListener(this);
            prev_image.setOnClickListener(this);
            promote_yourself.setOnClickListener(this);
            rate1.setOnClickListener(this);
            rate2.setOnClickListener(this);
            rate3.setOnClickListener(this);
            rate4.setOnClickListener(this);
            rate5.setOnClickListener(this);
            grab_attention_layout.setOnClickListener(this);
            liked_image.setOnClickListener(this);
            profile_user_name.setSelected(true);
            profile_user_name.setTypeface(null, Typeface.BOLD);
            profile_user_name.setSingleLine();
            profile_user_name.setEllipsize(TruncateAt.MARQUEE);
            profile_user_name.setHorizontallyScrolling(true);

            manageLocation();
            pref = new Preferences(getActivity());

            final Dialog dialog = new Dialog(_ctx);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            if (pref.getBoolean("comming_from_registration") == true) {
                dialog.setContentView(R.layout.congrates_with_coin_20);
            } else {
                dialog.setContentView(R.layout.congrates_with_coin_5);
            }

            getGiftUrl();

            handler = new Handler();
            runn = new Runnable() {
                @Override
                public void run() {

                    //Log
                    if (image_index >= 0 && GiftUrls.size()>0) {
                        Glide.with(_ctx).load(GiftUrls.get(image_index)).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(gift_images);

                        image_index++;
                        if (image_index >= (GiftUrls.size() - 1)) {
                            image_index = 0;
                        }

                        handler.postDelayed(this, 2000); // set to go off again
                        // in 3 seconds.
                    }
                }
            };

            handler.post(runn);

            Button google = (Button) dialog.findViewById(R.id.button1);
            final int ii = 0;



/*
            getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					if (image_index >= 0) {
						Glide.with(_ctx).load(GiftUrls.get(image_index)).diskCacheStrategy(DiskCacheStrategy.ALL)
								.into(gift_images);

						image_index++;
						if (image_index >= GiftUrls.size() - 1) {
							image_index = 0;
						}

						handler.postDelayed(this, 2000); // set to go off again
						// in 3 seconds.
					}

				}
			});

			handler.postDelayed(this, 10);*/

            google.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });

            if (!pref.getBoolean("Points")) {

                dialog.show();
                pref.setBoolean("Points", true);
                pref.commit();
            }

            selected_user = pref.get(Constants.selected_user_id);
            File cacheDir = StorageUtils.getCacheDirectory(getActivity());

            skip_image.setOnClickListener(this);

            DrawerActivity.popBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ((DrawerActivity) getActivity()).displayView(90);
                }
            });

            get_album_api = new getAlbumApi(_ctx, db, p) {

                @Override
                protected void onResponseReceived(InputStream is) {
                    // TODO Auto-generated method stub
                    super.onResponseReceived(is);
                    p.cancel();
                    photos_of_you = get_album_api.photos_of_you;
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
                    p.cancel();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (photos_of_you != null) {
                                getUrls(photos_of_you);
                            }
                        }
                    });
                    Log.d("arv", "arv" + user_photo_url.toString());
                }

                @Override
                protected void updateUI() {
                    // TODO Auto-generated method stub
                    super.updateUI();
                }

            };

            getAllUsers();

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            // ============= manage image height==============

            fragmentManager = getFragmentManager();

            // ==================================================

            userGallery.setSpacing(1);
            // userGallery.setAdapter(new GalleryImageAdapter(getActivity()));

            followerdata = new ArrayList<HashMap<String, String>>();
        }
        return rootView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.clear(gift_images);
        handler.removeCallbacks(runn);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runn);
    }


    @Override
    public void onResume() {
        super.onResume();
        handler.post(runn);
    }

    private void sendRequestDialog() {
        Bundle params = new Bundle();
        params.putString("message", "Learn how to make your Android apps social");

        WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(getActivity(), Session.getActiveSession(),
                params)).setOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(Bundle values, FacebookException error) {
                if (error != null) {
                    if (error instanceof FacebookOperationCanceledException) {
                        Toast.makeText(getActivity().getApplicationContext(), "Request cancelled",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Network Error",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    final String requestId = values.getString("request");
                    if (requestId != null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Request sent",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Request cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }).build();
        requestsDialog.show();

    }




    public void getGiftUrl() {

        Cursor GiftData = db.findCursor(DB.Table.Name.gift_master, null, null, null);
        Log.d("data sizee", "" + GiftData.getCount());
        GiftData.moveToFirst();

        for (int i = 0; i < GiftData.getCount(); i++) {
            if (GiftData.getString(GiftData.getColumnIndex(DB.Table.gift_master.gift_image.toString())).length() > 3) {

                String url = getActivity().getString(R.string.urlString) + "userdata/gift_gallery/"
                        + GiftData.getString(GiftData.getColumnIndex(DB.Table.gift_master.gift_image.toString()));

                GiftUrls.add(i, url);
            }
            GiftData.moveToNext();
        }



        /*if (GiftUrls.size()>0) {
            Glide.with(_ctx).load(GiftUrls.get(0)).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gift_images);


        }*/



    }

    public void getUrls(JsonArray urls) {

        JsonObject objJson;
        user_photo_id.clear();
        user_photo_url.clear();

        for (int i = 0; i < urls.size(); i++) {
            objJson = urls.get(i).getAsJsonObject();
            user_photo_id.add(objJson.get("id").getAsInt());
            user_photo_url.add(objJson.get("photo_pic").getAsString());
        }

        if (user_photo_url.size() > 0) {
            // arvimg_count.setText("1/"+user_photo_url.size());
            nophotos.setVisibility(View.GONE);
            prev_image.setVisibility(View.VISIBLE);
            next_image.setVisibility(View.VISIBLE);
        } else {
            img_count.setText("0/" + user_photo_url.size());
            nophotos.setVisibility(View.VISIBLE);
            prev_image.setVisibility(View.GONE);
            next_image.setVisibility(View.GONE);
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.next_btn:

                position = userGallery.getSelectedItemPosition() + 1;
                if (userGallery.getCount() > 0)
                    img_count.setText("" + position + "/" + userGallery.getCount());
                if (position >= userGallery.getCount())
                    return;
                userGallery.setSelection(position);

                break;

            case R.id.prev_btn:

                position = userGallery.getSelectedItemPosition() - 1;
                if (userGallery.getCount() > 0 && position >= 0)
                    img_count.setText(position + 1 + "/" + userGallery.getCount());
                if (position < 0)
                    return;
                userGallery.setSelection(position);

                break;

            case R.id.show_fullprofile_btn:

                // pref.set(Constants.OtherUser, v.getTag().toString());
                // pref.set(Constants.OtherUserName,
                // profile_user_name.getText().toString());
                // pref.commit();
                // ((DrawerActivity) getActivity()).displayView(46);

                pref.set(Constants.OtherUser, v.getTag().toString());
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(46);

                break;

            case R.id.rate1_img:
                ratingValue = 1;
                rate1.setImageResource(R.drawable.rating);
                rate2.setImageResource(R.drawable.no_rating);
                rate3.setImageResource(R.drawable.no_rating);
                rate4.setImageResource(R.drawable.no_rating);
                rate5.setImageResource(R.drawable.no_rating);
                break;

            case R.id.rate2_img:
                ratingValue = 2;
                rate1.setImageResource(R.drawable.rating);
                rate2.setImageResource(R.drawable.rating);
                rate3.setImageResource(R.drawable.no_rating);
                rate4.setImageResource(R.drawable.no_rating);
                rate5.setImageResource(R.drawable.no_rating);
                break;

            case R.id.rate3_img:
                ratingValue = 3;
                rate1.setImageResource(R.drawable.rating);
                rate2.setImageResource(R.drawable.rating);
                rate3.setImageResource(R.drawable.rating);
                rate4.setImageResource(R.drawable.no_rating);
                rate5.setImageResource(R.drawable.no_rating);
                break;

            case R.id.rate4_img:
                ratingValue = 4;
                rate1.setImageResource(R.drawable.rating);
                rate2.setImageResource(R.drawable.rating);
                rate3.setImageResource(R.drawable.rating);
                rate4.setImageResource(R.drawable.rating);
                rate5.setImageResource(R.drawable.no_rating);
                break;

            case R.id.rate5_img:
                ratingValue = 5;
                rate1.setImageResource(R.drawable.rating);
                rate2.setImageResource(R.drawable.rating);
                rate3.setImageResource(R.drawable.rating);
                rate4.setImageResource(R.drawable.rating);
                rate5.setImageResource(R.drawable.rating);
                break;

            case R.id.skip_image:
                p.show();
                loadUser_whenSkip(current_user);
                current_user++;
                break;

            case R.id.liked_image:

                current_user++;
                p.show();
                sendFriendReq();

                break;

            case R.id.promote_yourself:

                // Intent in=new Intent(getActivity(),
                // FriendPickerSampleActivity.class);
                // startActivity(in);
                // sendRequestDialog();
                ((DrawerActivity) getActivity()).displayView(93);

                break;

            case R.id.grab_attention_layout:

                pref.set(Constants.OtherUser, show_full_prof_btn.getTag().toString());
                pref.commit();
                // ((DrawerActivity) getActivity()).displayView(46);
                pref.set(Constants.OtherUserName, profile_user_name.getText().toString()).commit();
                ((DrawerActivity) getActivity()).displayView(47);

                // pref.set(Constants.OtherUser,
                // show_full_prof_btn.getTag().toString());
                // pref.set(Constants.OtherUserName,
                // profile_user_name.getText().toString());
                // pref.commit();
                // ((DrawerActivity) getActivity()).displayView(47);

                break;
            default:
                break;
        }

    }

    public void getAllUsers() {

        get_all_user_pfro_api = new getAllUserProfileAPI(getActivity(), db, p) {

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub

                if ((get_all_user_pfro_api.getUserdata()).size() > 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            showuser(get_all_user_pfro_api.getUserdata());

                        }


                    });
                    getAllUnfriendUsers(unfrienduser_page_index);
                }
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
        map.put("time_stamp", "");
        map.put("type", "6");
        map.put("distance", "2");
        map.put("page_index", "1");
        Log.d("arv", "" + mLocationHelper.getMyLocation());
        if (mLocationHelper.getMyLocation() == null) {
            map.put("latitude", "1234");
            map.put("longitude", "1234");
        } else {
            map.put("latitude", String.valueOf(mLocationHelper.getMyLocation().getLatitude()));
            map.put("longitude", String.valueOf(mLocationHelper.getMyLocation().getLongitude()));
        }

        get_all_user_pfro_api.setPostData(map);
        callApi(get_all_user_pfro_api);
    }

    public void getAllUnfriendUsers(int index) {
        unfriendsUsers = null;

        get_all_user_pfro_api = new getAllUserProfileAPI(getActivity(), db, p) {

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                super.onError(e);
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();
                unfriendsUsers = get_all_user_pfro_api.getUserdata();
                unfrienduser_page_index_available = get_all_user_pfro_api.index;

                // showuser(get_all_user_pfro_api.userdata);
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
                try {
                    loadUser_whenSkip(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("time_stamp", "");
        map.put("type", "7");
        map.put("distance", "2");
        map.put("page_index", String.valueOf(index));
        Log.d("arv", "" + mLocationHelper.getMyLocation());
        if (mLocationHelper.getMyLocation() == null) {
            map.put("latitude", "1234");
            map.put("longitude", "1234");
        } else {
            map.put("latitude", String.valueOf(mLocationHelper.getMyLocation().getLatitude()));
            map.put("longitude", String.valueOf(mLocationHelper.getMyLocation().getLongitude()));
        }

        get_all_user_pfro_api.setPostData(map);
        callApi(get_all_user_pfro_api);
    }

    public void setStar(float val) {

        if (val == 0) {
            rate1.setImageResource(R.drawable.no_rating);
            rate2.setImageResource(R.drawable.no_rating);
            rate3.setImageResource(R.drawable.no_rating);
            rate4.setImageResource(R.drawable.no_rating);
            rate5.setImageResource(R.drawable.no_rating);
        }

        if (val <= 1 && val > 0) {
            rate1.setImageResource(R.drawable.small_half_star);
            rate2.setImageResource(R.drawable.no_rating);
            rate3.setImageResource(R.drawable.no_rating);
            rate4.setImageResource(R.drawable.no_rating);
            rate5.setImageResource(R.drawable.no_rating);
        }
        if (1 < val && val <= 2) {
            rate1.setImageResource(R.drawable.rating);
            rate2.setImageResource(R.drawable.small_half_star);
            rate3.setImageResource(R.drawable.no_rating);
            rate4.setImageResource(R.drawable.no_rating);
            rate5.setImageResource(R.drawable.no_rating);
        }

        if (2 < val && val <= 3) {
            rate1.setImageResource(R.drawable.rating);
            rate2.setImageResource(R.drawable.rating);
            rate3.setImageResource(R.drawable.small_half_star);
            rate4.setImageResource(R.drawable.no_rating);
            rate5.setImageResource(R.drawable.no_rating);
        }

        if (3 < val && val <= 4) {
            rate1.setImageResource(R.drawable.rating);
            rate2.setImageResource(R.drawable.rating);
            rate3.setImageResource(R.drawable.rating);
            rate4.setImageResource(R.drawable.small_half_star);
            rate5.setImageResource(R.drawable.no_rating);
        }

        if (4 < val && val <= 5) {
            rate1.setImageResource(R.drawable.rating);
            rate2.setImageResource(R.drawable.rating);
            rate3.setImageResource(R.drawable.rating);
            rate4.setImageResource(R.drawable.rating);
            rate5.setImageResource(R.drawable.small_half_star);
        }

        if (val == 5) {
            rate1.setImageResource(R.drawable.rating);
            rate2.setImageResource(R.drawable.rating);
            rate3.setImageResource(R.drawable.rating);
            rate4.setImageResource(R.drawable.rating);
            rate5.setImageResource(R.drawable.rating);
        }
    }

    public void getAlbumApi(String other_user_uuid) {
       final LayoutInflater inflater = (LayoutInflater) _ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageAdapter = new ImageAdapter(getActivity(), user_photo_url, current_user_server_id) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                total_img = super.getCount();
                return super.getCount();

            }

            @Override
            public long getItemId(int position)
            {
                com.ogbongefriends.com.common.Log.d("ID of the position", "" + position);

                return position;
            }


            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                current_img = position;

                return user_photo_url.size();
            }

            // @Override
            public View getView(int arg0, View arg1, ViewGroup arg2) {
                // TODO Auto-generated method stub
                img_count.setText("" + (arg0 + 1) + "/" + total_img);
                ImageView imageView = new ImageView(_ctx);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT, android.widget.Gallery.LayoutParams.FILL_PARENT));
                String full_image_path=_ctx.getString(R.string.urlString)+"userdata/image_gallery/"+current_user_server_id+"/photos_of_you/"+user_photo_url.get(arg0);
                AnimationDrawable animPlaceholder = (AnimationDrawable)_ctx.getResources().getDrawable(R.drawable.image_loading_animation);
                animPlaceholder.start();

                Glide.with(_ctx).load(full_image_path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(animPlaceholder).thumbnail(0.01f).into(imageView);


              /*  Glide.with(_ctx).load(full_image_path).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        com.ogbongefriends.com.common.Log.e("Resource Ready11", "Resource Ready11");
                        pb1.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.01f).into(imageView);
*/



                return imageView;
            }

        };

        userGallery.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        HashMap<String, String> reqdata = new HashMap<String, String>();
        reqdata.put("uuid", pref.get(Constants.KeyUUID));
        reqdata.put("other_uuid", other_user_uuid);
        reqdata.put("auth_token", pref.get(Constants.kAuthT));
        reqdata.put("time_stamp", "");

        get_album_api.setPostData(reqdata);
        callApi(get_album_api);

    }

    public void sendFriendReq() {

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

            }

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
                if (sendFriendRequest.resCode == 1) {
                    Log.d("arv Friend Req send", "arv Friend Req send");
                }
                p.cancel();
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
                // showuser();
                loadUser_whenSkip(current_user);
            }

        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uuid", pref.get(Constants.KeyUUID));
        map.put("auth_token", pref.get(Constants.kAuthT));
        map.put("receiver_uuid", selected_user);
        map.put("type", "1");
        sendFriendRequest.setPostData(map);
        callApi(sendFriendRequest);

    }

    private void manageLocation() {
        mLocationHelper = new LocationHelper(_ctx);
       // mLocationHelper.startLocationUpdates(this);
    }

    private void showuser(final JsonArray jsnData) {

        for (int i = 0; i < jsnData.size(); i++) {
            JsonObject obj = jsnData.get(i).getAsJsonObject();

            if (obj.get(DB.Table.user_master.profile_pic.toString()).getAsString().length() > 3) {
                RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.FILL_PARENT);
                RelativeLayout.LayoutParams topbar_param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                        30);

                RelativeLayout parent = new RelativeLayout(getActivity());

                RelativeLayout top_bar = new RelativeLayout(getActivity());
                top_bar.setBackgroundResource(R.drawable.top_element);

                parent.setLayoutParams(parent_param);

                if (i == 0) {
                    parent.setBackgroundColor(Color.DKGRAY);
                }

                parent.setTag(obj.get(DB.Table.user_master.uuid.toString()).getAsString());
                parent.setTag(R.id.account_setting_text, i);
                parent.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        /// loadUser(v,(Integer)
                        /// v.getTag(R.id.account_setting_text));
                        current_user = (Integer) v.getTag(R.id.account_setting_text);
                        // data.moveToPosition(current_user);
                        pref.set(Constants.OtherUser, (jsnData.get(current_user).getAsJsonObject()
                                .get(DB.Table.user_master.uuid.toString()).getAsString()));
                        pref.commit();
                        ((DrawerActivity) getActivity()).displayView(37);

                    }
                });
                String url = getActivity().getString(R.string.urlString) + "userdata/image_gallery/"
                        + obj.get(DB.Table.user_master.id.toString()).getAsString() + "/photos_of_you/"
                        + obj.get(DB.Table.user_master.profile_pic.toString()).getAsString();
                ImageView imagView = new ImageView(getActivity());
                imagView.setScaleType(ScaleType.FIT_XY);
                ll_user.setOrientation(LinearLayout.HORIZONTAL);
                // LinearLayout.LayoutParams lp = new
                // LinearLayout.LayoutParams(140, 140);

                int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

                String toastMsg;
                switch (screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        toastMsg = "Large screen";

                        lp = new LinearLayout.LayoutParams(190, LayoutParams.FILL_PARENT);

                        break;
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        toastMsg = "Normal screen";

                        lp = new LinearLayout.LayoutParams(150, LayoutParams.FILL_PARENT);
                        lp.setMargins(5, 0, 0, 0);
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                        toastMsg = "Small screen";
                        break;
                    default:
                        toastMsg = "Screen size is neither large, normal or small";
                }

                imagView.setLayoutParams(lp);
                ProgressBar p = new ProgressBar(_ctx);
                p.setLayoutParams(lp);
                //	imagView.setTag(url);
                imagView.setImageResource(R.drawable.profile);
                // imageLoader.displayImage(url, imagView);
                loading.setVisibility(View.GONE);
                Glide.with(_ctx).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(imagView);
                /*
                 * imageLoader.displayImage(url, imagView, new
				 * ImageLoadingListener() {
				 * 
				 * @Override public void onLoadingStarted(String arg0, View
				 * arg1) { // TODO Auto-generated method stub
				 * 
				 * }
				 * 
				 * @Override public void onLoadingFailed(String arg0, View arg1,
				 * FailReason arg2) { // TODO Auto-generated method stub
				 * //if(i==data.getCount()-1){ loading.setVisibility(View.GONE);
				 * // } }
				 * 
				 * @Override public void onLoadingComplete(String arg0, View
				 * arg1, Bitmap arg2) { // TODO Auto-generated method stub
				 * loading.setVisibility(View.GONE); }
				 * 
				 * @Override public void onLoadingCancelled(String arg0, View
				 * arg1) { // TODO Auto-generated method stub
				 * loading.setVisibility(View.GONE); } });
				 */

                RelativeLayout.LayoutParams online_param = new RelativeLayout.LayoutParams(15, 15);
                online_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                ImageView online_image = new ImageView(getActivity());
                online_image.setLayoutParams(online_param);
                online_image.setImageResource(R.drawable.online_dot);
                TextView tv = new TextView(getActivity());
                RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.FILL_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                tv.setText(obj.get(DB.Table.user_master.first_name.toString()).getAsString());
                tv.setTextColor(Color.WHITE);
                tv.setLayoutParams(text_param);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                parent.addView(imagView);
                parent.addView(top_bar, topbar_param);
                parent.addView(tv);
                // parent.addView(online_image);

                ll_user.addView(parent);

            }

        }
        p.cancel();
    }

    private void loadUser_whenSkip(int i) {
        p.show();
        if (unfriendsUsers.size() > 0)
            if (i < unfriendsUsers.size()) {
                JsonObject shoUser = unfriendsUsers.get(i).getAsJsonObject();
                // data.moveToPosition(i);

                // p.show();
                age = CelUtils.getAgeStrFromDOB(shoUser.get(DB.Table.user_master.date_of_birth.toString()).getAsString());
                profile_user_name.setText(shoUser.get(DB.Table.user_master.first_name.toString()).getAsString() + " "
                        + shoUser.get(DB.Table.user_master.last_name.toString()).getAsString() + ", " + age);
                // profile_user_name.setText(data.getString(data.getColumnIndex(Table.user_master.first_name.toString()))+"
                // "+data.getString(data.getColumnIndex(Table.user_master.last_name.toString()))+"
                // "+data.getString(data.getColumnIndex(Table.user_master.status.toString())));
                show_full_prof_btn.setTag(shoUser.get(DB.Table.user_master.uuid.toString()).getAsString());

                if (shoUser.has(DB.Table.user_master.rating.toString())) {
                    setRating(Float.parseFloat(shoUser.get(DB.Table.user_master.rating.toString()).getAsString()));
                } else {
                    setRating(0);
                }

                if (shoUser.has(DB.Table.user_master.gender.toString())) {
                    if (shoUser.get(DB.Table.user_master.gender.toString()).getAsInt() == 2) {
                        grab_his.setText("Grab her attention");
                    } else {
                        grab_his.setText("Grab his attention");
                    }
                }
                Log.d("arv", "arv position " + i);
                current_user_server_id = shoUser.get(DB.Table.user_master.id.toString()).getAsString();
                selected_user = shoUser.get(DB.Table.user_master.uuid.toString()).getAsString();
                getAlbumApi(shoUser.get(DB.Table.user_master.uuid.toString()).getAsString());
                // p.cancel();
            } else {
                if (unfrienduser_page_index_available > unfrienduser_page_index) {
                    current_user = 1;
                    unfrienduser_page_index++;
                    getAllUnfriendUsers(unfrienduser_page_index);
                } else {
                    p.cancel();
                    Utils.same_id("Ogbonge", "No More User Found!", getActivity());
                }
            }
        p.cancel();
    }

    private void setRating(float f) {
        Log.d("arv", "arv " + f);
        setStar(f);
    }

    private void loadUser_whenLiked(int i) {
        if (i < data.getCount()) {
            data.moveToPosition(i);
            age = CelUtils
                    .getAgeStrFromDOB(data.getString(data.getColumnIndex(DB.Table.user_master.date_of_birth.toString())));
            profile_user_name.setText(data.getString(data.getColumnIndex(DB.Table.user_master.first_name.toString())) + " "
                    + data.getString(data.getColumnIndex(DB.Table.user_master.last_name.toString())) + ", " + age);
            // profile_user_name.setText(data.getString(data.getColumnIndex(Table.user_master.first_name.toString()))+"
            // "+data.getString(data.getColumnIndex(Table.user_master.last_name.toString()))+"
            // "+data.getString(data.getColumnIndex(Table.user_master.status.toString())));
            if (data.getInt(data.getColumnIndex(DB.Table.user_master.gender.toString())) == 2) {
                grab_his.setText("Grab her attention");
            } else {
                grab_his.setText("Grab his attention");
            }
            Log.d("arv", "arv position " + i);
            getAlbumApi(data.getString(data.getColumnIndex(DB.Table.user_master.uuid.toString())));
        }
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
                    Utils.same_id("Error", getString(R.string.no_internet), getActivity());
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
    public void onLocationUpdate(Location location) {
        // TODO Auto-generated method stub

    }

}
