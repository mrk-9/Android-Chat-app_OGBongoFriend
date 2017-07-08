package com.ogbongefriends.com.ogbonge.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.Other_Info_Api;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.util.HashMap;

public class AboutUs extends Fragment {

    private View rootView;
    private int cat = 0;
    private Other_Info_Api otherInfo;
    private TextView title, msg;
    private DB db;
    private int type;
    private CustomLoader p;
    private Preferences pref;
    private Cursor user_data;
    public AboutUs(){

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.about_us, container, false);
        db = DrawerActivity.db;
        title = (TextView) rootView.findViewById(R.id.title);

        msg = (TextView) rootView.findViewById(R.id.msg);
        p = DrawerActivity.p;
        pref = new Preferences(getActivity());
        type = pref.getInt("category");
        otherInfo = new Other_Info_Api(getActivity(), db, p) {

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();
                p.cancel();
                //if(otherInfo.resCode==1){
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        showData(otherInfo.objJson);
                    }
                });

                //}
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
            }

        };

        HitApi();
        return rootView;
    }

    private void showData(JsonObject obj) {

        JsonObject data = obj.get("Data").getAsJsonObject();
        title.setText(data.get("page_name").getAsString());
        msg.setText(data.get("page_content").getAsString());

    }


    private void HitApi() {


        p.show();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("type", String.valueOf(type));
        data.put("time_stamp", "");

        otherInfo.setPostData(data);
        callApi(otherInfo);
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

}
