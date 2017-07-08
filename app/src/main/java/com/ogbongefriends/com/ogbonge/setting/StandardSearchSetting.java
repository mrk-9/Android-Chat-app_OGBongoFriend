package com.ogbongefriends.com.ogbonge.setting;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.gson.JsonArray;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.ogbonge.profile.updateProfileApi;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Custom_list_Adapter;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.RangeSeekBar;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.RangeSeekBar.OnRangeSeekBarChangeListener;

public class StandardSearchSetting extends Fragment implements Runnable, MyLocationListener, OnClickListener, OnCheckedChangeListener {

    // private ListView listView;
    @SuppressWarnings("unused")
    private EditText posttetx, optional_field;
    @SuppressWarnings("unused")
    private Button post;
    private Button attchbtn;
    private JsonArray searchedata;
    private Uri imageUri;
    private int country = 0, country_selected = 0, state_selected, distance_selected = 0;
    private Uri selectedImage;
    private long str;
    private boolean spinner_flag = true;
    private LocationHelper mLocationHelper;
    Cursor data, userData;
    Cursor eventdatacorsor;
    Cursor followerdatacorsor;
    Cursor followingdatacorsor;
    Cursor secfollowingdatacorsor;
    Preferences pref;
    private Context _ctx;
    private CustomLoader p;
    private DB db;
    private View rootView;
    private GridView user_of_city;
    private LinearLayout seekbar_parent, seekbar_parent_location;
    private TextView age_text;
    private int interestedin_master_id, interestedin_purpose_master_id, meet_min_age, meet_max_age, location = 300;
    private Button save, cancel;
    private String age = "";
    private ImageView selected_image, men_radio, men, radio_men, women, radio_women, radio_both, both;
    private TextView selected_text, select_range;
    private Custom_list_Adapter custom_list_adapter;
    private Dialog dialog;
    private Spinner country_spinner, state_spinner;
    private SeekBar seekBar1;
    private LinearLayout date_lay1, optional_parent_id;
    private int men_value = 0, women_value = 0, both_mf = 0;
    private LinearLayout men_parent, women_parent, bothmf_parent;
    private updateProfileApi updateProfile;
    private user_profile_api user_profile_info;
    private RangeSeekBar<Integer> seekBar;
    private ArrayList<String> country_list, state_list, city_list;
    private ArrayList<Integer> country_list_id, state_list_id, city_list_id;
    final String[] dropdownOption = new String[]{"Country", "State", "City", "Distance in km"};
    final String[] stringArray = new String[]{"Select For", "Long term friendship", "Friends to marriage", "Casual friends", "Chat sports"};
    final Integer[] stringArrayImages = new Integer[]{R.drawable.for_status,
            R.drawable.long_term_friendship,
            R.drawable.friends_to_marriage,
            R.drawable.casual_dates,
            R.drawable.chat_sports};

    public StandardSearchSetting(Context ctx) {
        _ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
        db = new DB(_ctx);
        pref = new Preferences(_ctx);
        //p.show();
        rootView = inflater.inflate(R.layout.standard_search_setting, container, false);

        user_of_city = (GridView) rootView.findViewById(R.id.user_of_city);
        seekbar_parent = (LinearLayout) rootView.findViewById(R.id.seekbar_parent);
        age_text = (TextView) rootView.findViewById(R.id.age_text);
        optional_field = (EditText) rootView.findViewById(R.id.optional_field);
        selected_image = (ImageView) rootView.findViewById(R.id.selected_image);
        selected_text = (TextView) rootView.findViewById(R.id.selected_text);
        date_lay1 = (LinearLayout) rootView.findViewById(R.id.date_lay1);
        seekBar1 = (SeekBar) rootView.findViewById(R.id.seekBar1);
        country_spinner = (Spinner) rootView.findViewById(R.id.country_list);
        state_spinner = (Spinner) rootView.findViewById(R.id.state_list);

        country_list = new ArrayList<String>();
        state_list = new ArrayList<String>();
        city_list = new ArrayList<String>();
        country_list_id = new ArrayList<Integer>();
        state_list_id = new ArrayList<Integer>();
        city_list_id = new ArrayList<Integer>();
        optional_parent_id = (LinearLayout) rootView.findViewById(R.id.optional_parent_id);
        men_parent = (LinearLayout) rootView.findViewById(R.id.men_parent);
        men = (ImageView) rootView.findViewById(R.id.men);
        radio_men = (ImageView) rootView.findViewById(R.id.radio_men);
        select_range = (TextView) rootView.findViewById(R.id.select_range);
        women_parent = (LinearLayout) rootView.findViewById(R.id.women_parent);
        women = (ImageView) rootView.findViewById(R.id.women);
        radio_women = (ImageView) rootView.findViewById(R.id.radio_women);

        bothmf_parent = (LinearLayout) rootView.findViewById(R.id.both_parent);
        both = (ImageView) rootView.findViewById(R.id.men);
        radio_both = (ImageView) rootView.findViewById(R.id.radio_both);


        save = (Button) rootView.findViewById(R.id.save_btn_setting);
        cancel = (Button) rootView.findViewById(R.id.cancel_btn_setting);
        seekBar = new RangeSeekBar<Integer>(18, 80, _ctx);


        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        men_parent.setOnClickListener(this);
        women_parent.setOnClickListener(this);
        bothmf_parent.setOnClickListener(this);
        men.setOnClickListener(this);
        women.setOnClickListener(this);
        both.setOnClickListener(this);
        country_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, dropdownOption));
        //country_spinner.setSelection(3);


        country_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                spinner_flag = false;
                // TODO Auto-generated method stub
                country_selected = arg2 + 1;
                if (country_selected == 2 && country != 159) {
                    setSpinners(5);
                } else {
                    if (country_selected == 3 && country != 159) {
                        setSpinners(6);
                    } else {
                        if (country_selected == 4) {
                            setSpinners(3);
                        } else {
                            setSpinners(arg2);
                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });


        state_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                state_selected = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = location;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                location = progress;
                distance_selected = progresValue;
                select_range.setText("From My Location " + progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                location = progress;
                select_range.setText("From My Location " + progress + " km");
            }
        });


        seekbar_parent_location = (LinearLayout) rootView.findViewById(R.id.seekbar_parent_location);


        getUserProfile();


        seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                Log.i("arvi", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                age = "Show me people aged";
                age_text.setText("Show me people aged " + minValue + " to " + maxValue);
                meet_min_age = minValue;
                meet_max_age = maxValue;
            }
        });


        date_lay1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.show();
            }
        });


        seekbar_parent.addView(seekBar);


        AlertDialog.Builder builder = new AlertDialog.Builder(_ctx);
        builder.setTitle("I�m here to Find Ogbonge friends");


        ListView modeList = new ListView(_ctx);


        custom_list_adapter = new Custom_list_Adapter(_ctx, stringArray, stringArrayImages) {

            @Override
            protected void onItemLongClick(View v, int string) {
                // TODO Auto-generated method stub


            }

            @Override
            protected void onItemClick(View v, int string) {
                // TODO Auto-generated method stub
                selected_image.setImageResource(stringArrayImages[string]);
                selected_text.setText(stringArray[string]);
                interestedin_purpose_master_id = string;
                dialog.cancel();
            }
        };


        modeList.setAdapter(custom_list_adapter);
        modeList.setSelection(interestedin_purpose_master_id);

        builder.setView(modeList);
        dialog = builder.create();

//		dialog.show();

        return rootView;


    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }


    private void getUserProfile() {

        if (pref.getInt("interestedin_master_id") != 0) {
            interestedin_master_id = pref.getInt("interestedin_master_id");
        } else {
            interestedin_master_id = 1;
        }

        if (pref.getInt("interestedin_purpose_master_id") < 0) {
            interestedin_purpose_master_id = 1;

        } else {
            interestedin_purpose_master_id = pref.getInt("interestedin_purpose_master_id");
        }
        if (pref.getInt("meet_min_age") != 0) {
            meet_min_age = pref.getInt("meet_min_age");
        } else {
            meet_min_age = 18;
        }
        if (pref.getInt("meet_max_age") != 0) {
            meet_max_age = pref.getInt("meet_max_age");
        } else {
            meet_max_age = 35;
        }

        if (pref.getInt("location") != 0) {
            location = pref.getInt("location");
        } else {
            location = 100;
        }

        location = pref.getInt("location");
        seekBar1.setProgress(location);
        select_range.setText("From My Location " + location + " km");
        seekBar.setSelectedMinValue(meet_min_age);
        seekBar.setSelectedMaxValue(meet_max_age);
        selected_image.setImageResource(stringArrayImages[interestedin_purpose_master_id]);
        selected_text.setText(stringArray[interestedin_purpose_master_id]);
        switch (interestedin_master_id) {
            case 1:
                men_value = 1;
                radio_men.setImageResource(R.drawable.selected_radio);
                break;
            case 2:
                women_value = 1;
                radio_women.setImageResource(R.drawable.selected_radio);
                break;
            case 3:
                both_mf = 1;
                radio_both.setImageResource(R.drawable.selected_radio);
                break;
            default:
                break;
        }

        age_text.setText("Show me people aged " + meet_min_age + " to " + meet_max_age);

        data = db.findCursor(Table.Name.user_master, Table.user_master.uuid.toString() + " = '" + pref.get(Constants.KeyUUID) + "'", null, null);
        data.moveToFirst();
        country = Integer.parseInt(data.getString(data.getColumnIndex(Table.user_master.country_master_id.toString())));

        Log.d("arvind", "arvi" + pref.getInt("near_by"));

        if (pref.getInt("near_by") < 1) {
            if (Integer.parseInt(data.getString(data.getColumnIndex(Table.user_master.country_master_id.toString()))) == 159) {
                setSpinners(2);
            }
        }

        if (pref.getInt("near_by") == 1) {

            //	if(country==159){
            setSpinners(0);
//				}
//				else{
//					setSpinners(5);
//				}
        }

        if (pref.getInt("near_by") == 2) {
            if (country == 159) {
                setSpinners(1);
            } else {
                setSpinners(5);
            }
        }

        if (pref.getInt("near_by") == 3) {
            if (country == 159) {
                setSpinners(2);
            } else {
                setSpinners(6);
            }
        }
        if (pref.getInt("near_by") == 4) {
            setSpinners(3);

        }

    }


    private void setSpinners(int i) {

        country_list_id.clear();
        country_list.clear();
        db.open();


        if (i == 0) {
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.VISIBLE);
            select_range.setVisibility(View.GONE);
            optional_parent_id.setVisibility(View.GONE);
            data = db.findCursor(Table.Name.country_master, null, null, null);
            data.moveToFirst();
            if (data.moveToFirst()) {
                do {
                    country_list_id.add(data.getInt(data.getColumnIndex(Table.country_master.id.toString())));
                    country_list.add(data.getString(data.getColumnIndex(Table.country_master.country_name.toString())));
                } while (data.moveToNext());
            }

            state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));
            userData = db.findCursor(Table.Name.user_master, Table.user_master.uuid.toString() + " = '" + pref.get(Constants.KeyUUID) + "'", null, null);
            userData.moveToFirst();
            if (spinner_flag == true) {
                country_spinner.setSelection(0, true);
                if (pref.get("distance").length() > 0) {
                    for (int j = 0; j < country_list_id.size(); j++) {
                        if (Integer.parseInt(pref.get("distance")) == country_list_id.get(j)) {
                            state_spinner.setSelection(j);
                        } else {
                            continue;
                        }
                    }
                } else {
                    state_spinner.setSelection(0);
                }
            }

        }

        if (i == 1) {
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.VISIBLE);
            select_range.setVisibility(View.GONE);
            optional_parent_id.setVisibility(View.GONE);
            data = db.findCursor(Table.Name.nigiria_state_master, null, null, null);
            if (data.moveToFirst()) {

                do {
                    country_list_id.add(data.getInt(data.getColumnIndex(Table.nigiria_state_master.id.toString())));
                    country_list.add(data.getString(data.getColumnIndex(Table.nigiria_state_master.state_name.toString())));
                } while (data.moveToNext());
            }
            country_spinner.setSelection(1, true);
            state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));
            if (spinner_flag == true) {
                for (int j = 0; j < country_list_id.size(); j++) {
                    if (pref.get("distance") == country_list.get(j)) {
                        state_spinner.setSelection(j);
                    } else {
                        continue;
                    }
                }
            }


        }

        if (i == 2) {

//		// for city
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.VISIBLE);
            select_range.setVisibility(View.GONE);
            optional_parent_id.setVisibility(View.GONE);
            data = db.findCursor(Table.Name.nigiria_city_master, null, null, null);
            if (data.moveToFirst()) {

                do {
                    country_list_id.add(data.getInt(data.getColumnIndex(Table.nigiria_city_master.id.toString())));
                    country_list.add(data.getString(data.getColumnIndex(Table.nigiria_city_master.city_name.toString())));
                } while (data.moveToNext());
            }
            state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));
            if (spinner_flag == true) {
                country_spinner.setSelection(2, true);
                for (int j = 0; j < country_list_id.size(); j++) {
                    if (pref.get("distance") == country_list.get(j)) {
                        state_spinner.setSelection(j);
                    } else {
                        continue;
                    }
                }
            }


            //	state_spinner.setSelection(0);

//		}

        }

        if (i == 3) {
            state_spinner.setVisibility(View.GONE);
            seekBar1.setVisibility(View.VISIBLE);
            if (spinner_flag == true) {
                country_spinner.setSelection(3, true);
                if (pref.get("distance").length() > 0) {
                    seekBar1.setProgress(Integer.parseInt(pref.get("distance")));
                } else {
                    seekBar1.setProgress(10);
                }
            } else {
                seekBar1.setProgress(10);
            }
            select_range.setVisibility(View.VISIBLE);
            optional_parent_id.setVisibility(View.GONE);

            //seekBar1.setSecondaryProgress();

        }


        if (i == 4) {
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.VISIBLE);
            optional_parent_id.setVisibility(View.GONE);
            data = db.findCursor(Table.Name.country_master, null, null, null);
            if (data.moveToFirst()) {
                do {
                    country_list_id.add(data.getInt(data.getColumnIndex(Table.country_master.id.toString())));
                    country_list.add(data.getString(data.getColumnIndex(Table.country_master.country_name.toString())));
                } while (data.moveToNext());
            }
            if (spinner_flag == true) {
                country_spinner.setSelection(0, true);
                if (pref.get("distance").length() > 0) {
                    for (int j = 0; j < country_list_id.size(); j++) {

                        if (Integer.parseInt(pref.get("distance")) == country_list_id.get(j)) {
                            state_spinner.setSelection(j);
                        } else {
                            continue;
                        }
                    }
                } else {
                    state_spinner.setSelection(0);
                }
            }
            state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));


        }

        if (i == 5) {
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.GONE);
            optional_parent_id.setVisibility(View.VISIBLE);
            if (spinner_flag == true) {
                country_spinner.setSelection(1, true);
                optional_field.setText("" + pref.get("distance"));
            } else {
                optional_field.setText(" ");
            }


        }

        if (i == 6) {
            seekBar1.setVisibility(View.GONE);
            state_spinner.setVisibility(View.GONE);
            optional_parent_id.setVisibility(View.VISIBLE);
            if (spinner_flag == true) {
                country_spinner.setSelection(2, true);
                optional_field.setText("" + pref.get("distance"));
            } else {
                optional_field.setText(" ");
            }


        }

        db.close();

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


    private void manageLocation() {
        mLocationHelper = new LocationHelper(_ctx);
       // mLocationHelper.startLocationUpdates(this);
    }


    @Override
    public void onLocationUpdate(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.men_parent:
            case R.id.men:
            case R.id.radio_men:

                if (men_value == 0) {
                    interestedin_master_id = 1;
                    men_value = 1;
                    women_value = 0;
                    both_mf = 0;
                    radio_men.setImageResource(R.drawable.selected_radio);
                    radio_women.setImageResource(R.drawable.unselected_radio);
                    radio_both.setImageResource(R.drawable.unselected_radio);
                }

                break;

            case R.id.women_parent:
            case R.id.women:
            case R.id.radio_women:

                if (women_value == 0) {
                    interestedin_master_id = 2;
                    women_value = 1;
                    men_value = 0;
                    both_mf = 0;
                    radio_women.setImageResource(R.drawable.selected_radio);
                    radio_men.setImageResource(R.drawable.unselected_radio);
                    radio_both.setImageResource(R.drawable.unselected_radio);

                }
                break;

            case R.id.both_parent:
            case R.id.both:
            case R.id.radio_both:

                if (both_mf == 0) {
                    interestedin_master_id = 3;
                    women_value = 0;
                    men_value = 0;
                    both_mf = 1;
                    radio_both.setImageResource(R.drawable.selected_radio);
                    radio_women.setImageResource(R.drawable.unselected_radio);
                    radio_men.setImageResource(R.drawable.unselected_radio);
                }
                break;


            case R.id.save_btn_setting:
                svaeValues();

                break;

            case R.id.cancel_btn_setting:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }

    }


    public void svaeValues() {

        Log.d("arv ", "arv setting values " + interestedin_purpose_master_id + "  " + interestedin_master_id + "  " + country_selected + "  " + country + "    ");

        pref.set("interestedin_purpose_master_id", interestedin_purpose_master_id);

        pref.set("interestedin_master_id", interestedin_master_id);
        pref.set("meet_min_age", meet_min_age);
        pref.set("meet_max_age", meet_max_age);

        pref.set("near_by", country_selected);
        if (country_selected == 4) {
            pref.set("distance", String.valueOf(distance_selected));
        }

        if (country_selected == 2 || country_selected == 3) {

            if (country == 159) {
                pref.set("distance", country_list.get(state_selected));
            } else {
                pref.set("distance", optional_field.getText().toString());
            }
//				if(country_selected==2){
//					pref.set("state_text", optional_field.getText().toString());
//				}
//				if(country_selected==3){
//					pref.set("city_text", optional_field.getText().toString());
//				}
//			}
        }
        if (country_selected == 1) {
            pref.set("distance", String.valueOf(country_list_id.get(state_selected)));
        }


//		else{
//			pref.set("distance", 0);
//		}
        pref.commit();

        //	getActivity().onBackPressed();

        ((DrawerActivity) getActivity()).displayView(2);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub


    }

}


