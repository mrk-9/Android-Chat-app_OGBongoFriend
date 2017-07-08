package com.ogbongefriends.com.ogbonge.profile;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AboutMe extends Fragment implements OnClickListener {

    private Spinner education_spinner, job_spinner;
    private EditText my_handle, about_me;

    private Button save, update_profile_btn;
    private Preferences pref;
    private DB db;
    private CustomLoader p;
    private updateProfileApi update_profile;
    private HashMap<String, String> userDescMap;
    private View rootView;
    private int selected_education, selected_job;
    private Cursor data;
    private int education_selected = 0, job_selected = 0;
    private ArrayList<String> education_list, job_list;
    private ArrayList<Integer> education_list_id, job_list_id;
    public AboutMe(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.user_about_me, container, false);
        db = new DB(getActivity());

        education_spinner = (Spinner) rootView.findViewById(R.id.education_list);
        job_spinner = (Spinner) rootView.findViewById(R.id.job_list);
        my_handle = (EditText) rootView.findViewById(R.id.my_handle);
        about_me = (EditText) rootView.findViewById(R.id.about_me);

        save = (Button) rootView.findViewById(R.id.button1);


        save.setOnClickListener(this);


        education_list = new ArrayList<String>();
        job_list = new ArrayList<String>();

        education_list_id = new ArrayList<Integer>();
        job_list_id = new ArrayList<Integer>();

//		education_spinner.setEnabled(false);
//		job_spinner.setEnabled(false);
//		save.setEnabled(false);
//		my_handle.setEnabled(false);
//		about_me.setEnabled(false);

        pref = new Preferences(getActivity());

        pref = new Preferences(getActivity());
        p = DrawerActivity.p;
        p.show();


        education_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                ((TextView) parent.getChildAt(0)).setTextColor(R.color.light_blue);
                education_selected = education_list_id.get(position);
                Log.d("arv country_selected", "arv " + education_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        job_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                ((TextView) parent.getChildAt(0)).setTextColor(R.color.light_blue);
                job_selected = job_list_id.get(position);
                Log.d("arv country_selected", "arv " + job_selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


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
                // TODO Auto-generated method stub
                super.updateUI();
                p.cancel();
                //Utils.same_id("Message", "About Me Updated Successfully", getActivity());

                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }


        };

        DrawerActivity.edit_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                education_spinner.setEnabled(true);
                job_spinner.setEnabled(true);
                save.setEnabled(true);
                my_handle.setEnabled(true);
                about_me.setEnabled(true);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        setSpinners();
        super.onStart();

    }

    public void hitAPI(final HashMap<String, String> map) {

        p.show();
        update_profile.setPostData(map);
        callApi(update_profile);

    }


    private void setSpinners() {

        // for Education
        db.open();
        data = db.findCursor(DB.Table.Name.education_master, null, null, null);

        while (data.moveToNext()) {
            education_list_id.add(data.getInt(data.getColumnIndex(DB.Table.education_master.id.toString())));
            education_list.add(data.getString(data.getColumnIndex(DB.Table.education_master.name.toString())));
        }
        education_spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, education_list));
        education_spinner.setSelection(0);

        // for job
        data = db.findCursor(DB.Table.Name.job_master, null, null, null);
        while (data.moveToNext()) {
            job_list_id.add(data.getInt(data.getColumnIndex(DB.Table.job_master.id.toString())));
            job_list.add(data.getString(data.getColumnIndex(DB.Table.job_master.name.toString())));
        }
        job_spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, job_list));
        job_spinner.setSelection(0);


        db.close();

        fetchUserData(pref.get(Constants.KeyUUID));
        p.cancel();
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


    HashMap<String, String> fetchUserData(String id) {

        HashMap<String, String> userDescriptionsData = new HashMap<String, String>();

        if (id != null && !id.equals("")) {
            String whereClause = DB.Table.user_master.uuid.toString() + " = \"" + id + "\"";
            Log.d("whereClause Circle", "whereClause " + whereClause);
            db.open();
            Cursor userDescriptionsCur = db.findCursor(DB.Table.Name.user_master, whereClause, null, null);
            Log.d("userDescriptionsCur.getCount() Circle", "userDescriptionsCur.getCount() " + userDescriptionsCur.getCount());
            if (userDescriptionsCur.moveToNext()) {
                userDescriptionsCur.moveToFirst();
                my_handle.setText(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.handle_description.toString())));
                about_me.setText("" + userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.about_myself.toString())));

                if (userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.education_master_id.toString())) != null) {
                    education_selected = Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.education_master_id.toString())));
                } else {
                    education_selected = 0;
                }

                if (userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.job_master_id.toString())) != null) {
                    job_selected = Integer.parseInt(userDescriptionsCur.getString(userDescriptionsCur.getColumnIndex(DB.Table.user_master.job_master_id.toString())));
                } else {
                    job_selected = 0;
                }
                for (int i = 0; i < education_list_id.size(); i++) {
                    if (education_list_id.get(i) == education_selected) {
                        education_spinner.setSelection(i);
                    } else {
                        continue;
                    }
                }


                for (int i = 0; i < job_list_id.size(); i++) {
                    if (job_list_id.get(i) == job_selected) {
                        job_spinner.setSelection(i);
                    } else {
                        continue;
                    }
                }


            }
        }

        Log.e("userDescriptionsData MAP ", "userDescriptionsData " + userDescriptionsData);
        return userDescriptionsData;
    }


    private void setData() {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(DB.Table.user_master.handle_description.toString(), my_handle.getText().toString());
        data.put(DB.Table.user_master.about_myself.toString(), about_me.getText().toString());

        data.put(DB.Table.user_master.education_master_id.toString(), String.valueOf(education_selected));
        data.put(DB.Table.user_master.job_master_id.toString(), String.valueOf(job_selected));

        data.put("type", "1");

        hitAPI(data);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.update_profile_btn:


                break;


            case R.id.button1:

                setData();

                break;
            default:
                break;
        }
    }
}
