package com.ogbongefriends.com.ogbonge.photos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.ogbonge.profile.updateProfileApi;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.DelPhotoAlbumApi;
import com.ogbongefriends.com.api.getAlbumApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class albums extends Fragment implements Runnable, MyLocationListener, OnClickListener, OnCheckedChangeListener {

    // private ListView listView;
    @SuppressWarnings("unused")
    private EditText posttetx;
    @SuppressWarnings("unused")
    private Button post;
    Preferences pref;
    private Context _ctx;
    private CustomLoader p;
    private Button AddMorePhoto, del_photos;
    private DB db;
    private int selected_photo_index = -1;
    private String del_item = "";
    private View rootView;
    private GridView my_photos;
    private AlbumAdapter albumAdapter;
    public static ArrayList<PhotoOfYouVO> myPhotoVos = new ArrayList<PhotoOfYouVO>();
    private updateProfileApi update_profile;
    private ArrayList<String> myPrivatePhoto = new ArrayList<String>();
    private TextView photo_of_user, private_photos_text, no_photo_found;
    private String server_id, userName;
    private LinearLayout ll;
    private DelPhotoAlbumApi delPhoto;

    private com.ogbongefriends.com.api.getAlbumApi getAlbumApi;

//private int[] urls={R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,
//		R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,
//		R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here,R.drawable.add_me_here};


    public albums(Context ctx) {
        _ctx = ctx;
    }

    public albums() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
        pref = new Preferences(_ctx);

        db = new DB(_ctx);

        p.show();
     /*   myPhoto.clear();
        myPhotoId.clear();
        myPhotoFullUrl.clear();
        myPhotoName.clear();*/
        myPhotoVos.clear();

        Cursor c;
        if (pref.get("images_of").length() > 3) {
            Log.d("pref******", "" + pref.get("images_of"));
            c = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid + " = " + "'" + pref.get("images_of") + "'", null, null);
            Log.d("pref******", "" + c.getCount());
        } else {

            c = db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid + " = " + "'" + pref.get(Constants.KeyUUID) + "'", null, null);
            Log.d("pref******", "" + c.getCount());
        }
        c.moveToFirst();
        server_id = c.getString(c.getColumnIndex(DB.Table.user_master.server_id.toString()));
        userName = c.getString(c.getColumnIndex(DB.Table.user_master.first_name.toString())) + " " + c.getString(c.getColumnIndex(DB.Table.user_master.last_name.toString()));
        DrawerActivity.account_setting.setText("Photo Of " + userName);
        rootView = inflater.inflate(R.layout.albums, container, false);
        del_photos = (Button) rootView.findViewById(R.id.del_photos);
        my_photos = (GridView) rootView.findViewById(R.id.my_photos);
        AddMorePhoto = (Button) rootView.findViewById(R.id.addMorePhoto);
        no_photo_found = (TextView) rootView.findViewById(R.id.no_photo_found);
        ll = (LinearLayout) rootView.findViewById(R.id.linearLayout1);

        AddMorePhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                pref.set("upload_type", 1);
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(56);
            }
        });

        del_photos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean test = false;
                del_item = "";
                for (int i = 0; i < myPhotoVos.size(); i++) {

                    if (myPhotoVos.get(i).isChecked()) {
                        if (test == false) {
                            test = true;
                            del_item = del_item.concat(String.valueOf(myPhotoVos.get(i).getId()));


                        } else {
                            del_item = del_item.concat("," + String.valueOf(myPhotoVos.get(i).getId()));
                        }
                    }

                }

                if (del_item.length() > 1) {
                    delPhoto(del_item);

                } else {
                    Utils.same_id("Message", "Please select at least 1 photo to delete", _ctx);
                }
                Log.d("arvinddd", "arvinn" + del_item);
            }
        });


        albumAdapter = new AlbumAdapter(_ctx, myPhotoVos, server_id) {

            @Override
            protected void onItemClick(View v, int string) {
                // TODO Auto-generated method stub
                pref.set(Constants.SelectedImage, string);
                pref.commit();
                ((DrawerActivity) getActivity()).displayView(36);
            }

            @Override
            protected void onlongClick(View v, int string) {
                for (int i = 0; i < myPhotoVos.size(); i++) {
                    myPhotoVos.get(i).setIsSelected(false);
                }
                myPhotoVos.get(string).setIsSelected(true);
                albumAdapter.notifyDataSetChanged();
                setProfileConfirmation(string);
            }
        };
        my_photos.setAdapter(albumAdapter);
        ViewGroup.LayoutParams layoutParams = my_photos.getLayoutParams();
        //layoutParams.height = 50*urls.length; //this is in pixels


        my_photos.setLayoutParams(layoutParams);
        getAlbums();

        update_profile = new updateProfileApi(getActivity(), db, p) {

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
                super.updateUI();
                p.cancel();
                //	Toast.makeText(_ctx, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                //Utils.same_id("Message", "Profile Updated Successfully", getActivity());
                Utils.same_id("Message", "Profile Picture Updated Successfully", getActivity());
                ((DrawerActivity) getActivity()).setprofile(albums.myPhotoVos.get(selected_photo_index).getPhoto_pic());
                for (int i = 0; i < myPhotoVos.size(); i++) {
                    myPhotoVos.get(i).setIsSelected(false);
                }
                albumAdapter.notifyDataSetChanged();

                //getActivity().onBackPressed();
            }
        };


        return rootView;
    }


    public void getAlbums() {

        getAlbumApi = new getAlbumApi(_ctx, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        p.cancel();
                        if (getAlbumApi.photos_of_you.size() > 0) {
                            ll.setVisibility(View.VISIBLE);
                            // del_photos.setVisibility(View.VISIBLE);
                            no_photo_found.setVisibility(View.GONE);

                            for (int i = 0; i < getAlbumApi.photos_of_you.size(); i++) {
                                PhotoOfYouVO photo_vo = new PhotoOfYouVO(getAlbumApi.photos_of_you.get(i).getAsJsonObject());
                                photo_vo.setfullPhotoUrl(getActivity().getString(R.string.urlString) + "userdata/image_gallery/" + server_id + "/photos_of_you/" + photo_vo.getPhoto_pic());
                                myPhotoVos.add(photo_vo);
                            }
                        } else {
                            ll.setVisibility(View.GONE);
                            del_photos.setVisibility(View.GONE);
                            no_photo_found.setVisibility(View.VISIBLE);
                        }
                    }
                });

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

                super.updateUI();
                albumAdapter.notifyDataSetChanged();
            }

        };
        HashMap<String, String> keydata = new HashMap<String, String>();
        keydata.put("uuid", pref.get(Constants.KeyUUID));
        keydata.put("auth_token", pref.get(Constants.kAuthT));
        keydata.put("time_stamp", "");
        if (pref.get("images_of").length() > 3) {
            keydata.put("other_uuid", pref.get("images_of"));
            AddMorePhoto.setVisibility(View.GONE);
            del_photos.setVisibility(View.GONE);
        } else {
            keydata.put("other_uuid", "");
            AddMorePhoto.setVisibility(View.VISIBLE);
            del_photos.setVisibility(View.VISIBLE);
        }

        getAlbumApi.setPostData(keydata);
        callApi(getAlbumApi);
    }

    public void setProfileConfirmation(final int _position) {
        final Dialog dialog = new Dialog(_ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        ok_btn.setText("YES");
        cancel_btn.setText("NO");
        title.setText(getString(R.string.app_name));
        msg.setText(getString(R.string.set_profile_image_msg));
        dialog.show();
        cancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
               /* for(int i=0;i<myPhotoVos.size();i++){
                    myPhotoVos.get(i).setIsSelected(false);
                }
                albumAdapter.notifyDataSetChanged();*/
            }
        });

        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

                setData(_position);

            }
        });

        // show it
        dialog.show();
    }


    public void delPhoto(String photoids) {
        p.show();
        delPhoto = new DelPhotoAlbumApi(_ctx, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        //my_photos.getChildAt(1).setVisibility(View.GONE);

                        for (int i = 0; i < myPhotoVos.size(); i++) {

                            if (myPhotoVos.get(i).isChecked()) {
                                myPhotoVos.remove(i);
                            }

                        }
                        p.cancel();
                        albumAdapter.notifyDataSetChanged();
                        Utils.same_id("message", "Photos deleted successflly", _ctx);

                    }
                });

            }


        };

        HashMap<String, String> dataTosend = new HashMap<String, String>();
        dataTosend.put("uuid", pref.get(Constants.KeyUUID));
        dataTosend.put("auth_token", pref.get(Constants.kAuthT));
        dataTosend.put("album_type", "");
        dataTosend.put("photo_id", photoids);


        delPhoto.setPostData(dataTosend);
        callApi(delPhoto);


    }

    private void setData(int sel_pos) {
        selected_photo_index = sel_pos;
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("profile_pic", myPhotoVos.get(sel_pos).getPhoto_pic());
        data.put("type", "1");
        hitAPI(data);
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

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationUpdate(Location location) {
        // TODO Auto-generated method stub

    }


}


