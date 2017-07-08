package com.ogbongefriends.com.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.Login.LoginActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.PostContactDetails;
import com.ogbongefriends.com.api.RegistrationApi;
import com.ogbongefriends.com.api.Registration_2_Api;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.PostImage;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.TextDrawable;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class Registration_2 extends Activity implements OnClickListener, OnItemSelectedListener, Runnable {
    private EditText username, phone, email, con_email, password, con_password, dob;
    private int mYear, mMonth, mDay;
    private Spinner height_spinner, weight_spinner, body_spinner, stateNclub_header1, stateNclub_header2, SNC_spinner1, SNC_spinner2,
            for_spinner, my_education, my_job, country_spinner, state_spinner, city_spinner;

    private int height_selected = 0, weight_selected = 0, body_selected = 0, lookin_for_selected = 0, for_selected = 0, education_selected = 0, job_selected, starNclub1 = 0, starNclub2 = 0, country_selected = 0, state_selected = 0, city_selected = 0;
    private ArrayList<String> height_list, weight_list, body_list, for_list, education_list, job_list, country_list, state_list, city_list;

    private ArrayList<Integer> height_list_id, weight_list_id, body_list_id, for_list_id, education_list_id, job_list_id, country_list_id, state_list_id, city_list_id;

    private ImageView selected_image, men_radio, menf, radio_menf, womenf, radio_womenf, radio_both, both;
    private LinearLayout men_parent, women_parent, bothmf_parent;
    private ArrayList<String> snc1_list, snc2_list, snc_header1;
    private ArrayList<Integer> snc1_list_id, snc2_list_id;
    private int starsAndclubsType1 = 0, starsAndclubsType2 = 0, starsAndclubsid1 = 0, starsAndclubsid2 = 0, fileSelected = 0;
    private int first_index, second_index, interested_in = 0, cityPosition = -1, statePosition = -1;
    private EditText state_box, city_box, my_handle, about_me;
    public final int SELECT_FILE = 100;
    private ImageView radio_men, radio_women;
    private ImageView men, women;
    private TextView terms_condition, totalchar;
    private Validation val;
    String tempPath = "";
    private CustomLoader p;
    private RelativeLayout first, second;
    private Button continue_btn, sign_up, back, chooseFile, step;
    private LinearLayout upload_layout;
    private TextView dodtext, selectedPhoto;
    private Editor edit;
    private int selectedGender = 0;
    private CheckBox terms_conditions;
    private DB db;
    private Cursor data;
    private Registration_2_Api registration_2_api;
    private PostContactDetails postContactDetail;
    int min = 1;
    private Context _ctx;
    int max = 9999999;
    Random r;
    //private String[] vals = {"Gender", "Male","Female" };

    public Preferences pref;
    private RegistrationApi registrationapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2);
        _ctx = Registration_2.this;
        first = (RelativeLayout) findViewById(R.id.first_part);
        second = (RelativeLayout) findViewById(R.id.second_part);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        back = (Button) findViewById(R.id.back_btn);
        sign_up = (Button) findViewById(R.id.submit);
        upload_layout = (LinearLayout) findViewById(R.id.submit_parent);
        my_handle = (EditText) findViewById(R.id.my_handle);
        about_me = (EditText) findViewById(R.id.about_me);
        totalchar = (TextView) findViewById(R.id.totalchar);
        state_box = (EditText) findViewById(R.id.state_box);
        city_box = (EditText) findViewById(R.id.city_box);
        chooseFile = (Button) findViewById(R.id.chooseFile);
        men_parent = (LinearLayout) findViewById(R.id.men_parent);
        men = (ImageView) findViewById(R.id.men);
        step = (Button) findViewById(R.id.step);
        radio_men = (ImageView) findViewById(R.id.radio_men);
        selectedPhoto = (TextView) findViewById(R.id.selectedPhoto);
        stateNclub_header1 = (Spinner) findViewById(R.id.stars_header1);
        stateNclub_header2 = (Spinner) findViewById(R.id.stars_header2);
        SNC_spinner1 = (Spinner) findViewById(R.id.stars1);
        SNC_spinner2 = (Spinner) findViewById(R.id.stars2);
        for_spinner = (Spinner) findViewById(R.id.for_list);
        my_education = (Spinner) findViewById(R.id.education_list);
        my_job = (Spinner) findViewById(R.id.job_list);
        country_spinner = (Spinner) findViewById(R.id.country_list);
        state_spinner = (Spinner) findViewById(R.id.state_list);
        city_spinner = (Spinner) findViewById(R.id.city_list);

        women_parent = (LinearLayout) findViewById(R.id.women_parent);
        women = (ImageView) findViewById(R.id.women);


        radio_women = (ImageView) findViewById(R.id.radio_women);

        bothmf_parent = (LinearLayout) findViewById(R.id.both_parent);
        both = (ImageView) findViewById(R.id.bothmf);
        radio_both = (ImageView) findViewById(R.id.radio_both);

        radio_men.setImageResource(R.drawable.selected_radio);
        radio_women.setImageResource(R.drawable.unselected_radio);
        radio_both.setImageResource(R.drawable.unselected_radio);

        men_parent.setOnClickListener(this);
        women_parent.setOnClickListener(this);
        bothmf_parent.setOnClickListener(this);
        men.setOnClickListener(this);
        women.setOnClickListener(this);
        both.setOnClickListener(this);

        second.setVisibility(View.GONE);
        upload_layout.setVisibility(View.GONE);
        snc1_list = new ArrayList<String>();
        snc2_list = new ArrayList<String>();

        snc1_list_id = new ArrayList<Integer>();
        snc2_list_id = new ArrayList<Integer>();
        snc_header1 = new ArrayList<String>();

        height_spinner = (Spinner) findViewById(R.id.height_list);
        weight_spinner = (Spinner) findViewById(R.id.weight_list);
        body_spinner = (Spinner) findViewById(R.id.bodytype_list);


        height_list = new ArrayList<String>();
        weight_list = new ArrayList<String>();
        body_list = new ArrayList<String>();
        for_list = new ArrayList<String>();
        education_list = new ArrayList<String>();
        job_list = new ArrayList<String>();
        country_list = new ArrayList<String>();
        state_list = new ArrayList<String>();
        city_list = new ArrayList<String>();

        height_list_id = new ArrayList<Integer>();
        weight_list_id = new ArrayList<Integer>();
        body_list_id = new ArrayList<Integer>();
        state_list_id = new ArrayList<Integer>();
        city_list_id = new ArrayList<Integer>();


        education_list_id = new ArrayList<Integer>();
        job_list_id = new ArrayList<Integer>();
        country_list_id = new ArrayList<Integer>();
        for_list_id = new ArrayList<Integer>();

        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email_id);
        con_email = (EditText) findViewById(R.id.con_email_id);
        password = (EditText) findViewById(R.id.password);
        con_password = (EditText) findViewById(R.id.confirm_password);
        terms_conditions = (CheckBox) findViewById(R.id.terms_condition);
        dodtext = (TextView) findViewById(R.id.textView1);
        dob = (EditText) findViewById(R.id.dob);
        dob.setEnabled(false);
        radio_men = (ImageView) findViewById(R.id.radio_men);
        radio_women = (ImageView) findViewById(R.id.radio_women);
        men = (ImageView) findViewById(R.id.men);
        women = (ImageView) findViewById(R.id.women);
        terms_condition = (TextView) findViewById(R.id.term_condition_link);
        db = new DB(this);
        val = new Validation(Registration_2.this);

        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);

        pref = new Preferences(Registration_2.this);


        setSpinners();
        about_me.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //	totalchar=description.getText().length();
                totalchar.setText(about_me.getText().length() + "/" + 200);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        r = new Random();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//			session = new SessionManager(getApplicationContext());

        Log.d("arvi", "arvi" + pref.get(Constants.TempKeyUUID + " , " + pref.get(Constants.TempkAuthT)));


        registration_2_api = new Registration_2_Api(_ctx, db, p) {

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();
                if (registration_2_api.resCode == 1) {

p.cancel();
                    Registration_2.this.runOnUiThread(new Runnable() {
                        public void run() {


                            final Dialog dialog = new Dialog(Registration_2.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.confirmation_dialog);
                            TextView title = (TextView) dialog.findViewById(R.id.title);
                            TextView msg = (TextView) dialog.findViewById(R.id.message);
                            Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                            ok_btn.setText("OK");
                            title.setText("Message");
                            pref.setBoolean("comming_from_registration", true).commit();
                            msg.setText(registration_2_api.resMsg);
                            dialog.show();

                            ok_btn.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    p.show();
                                    postContacts();
                                }
                            });
                            cancel_btn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });


                          /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    Registration_2.this);

                            alertDialogBuilder.setTitle("Message");
                            pref.setBoolean("comming_from_registration", true).commit();
                            pref.setBoolean("comming_from_registration", true).commit();
                            // set dialog message
                            alertDialogBuilder.setMessage().setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            *//*Intent intent = new Intent(Registration_2.this,
                                                    DrawerActivity.class);
                                            startActivity(intent);
                                            finish();*//*

                                            // UpdateProfile.this.finish();
                                        }
                                    });
                            alertDialogBuilder.show();*/

                        }

                    });
                }
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
            }
        };
        height_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //height_selected=height_list_id.get(position);
                height_selected = height_list_id.get(position);
                Log.d("arv country_selected", "arv " + height_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        chooseFile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        SELECT_FILE);
            }
        });

        weight_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //weight_selected=weight_list_id.get(position);
                weight_selected = weight_list_id.get(position);
                Log.d("arv country_selected", "arv " + weight_selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        body_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //body_selected=body_list_id.get(position);
                body_selected = body_list_id.get(position);
                Log.d("arv country_selected", "arv " + body_selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        stateNclub_header1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                starsAndclubsType1 = position;
                if (position > 0) {
                    setStarandClub1(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        stateNclub_header2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                starsAndclubsType2 = position;
                if (position > 0) {
                    setStarandClub2(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        SNC_spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                first_index = position;
                starsAndclubsid1 = snc1_list_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        SNC_spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                second_index = position;
                starsAndclubsid2 = snc2_list_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        for_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                for_selected = for_list_id.get(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });


        state_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                state_selected = state_list_id.get(position);
                statePosition = position;
                Log.d("arv state", "arv " + state_selected);
                Log.d("arv state", "arv " + state_list.get(position));
                setCitySpinner(String.valueOf(state_list_id.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                city_selected = city_list_id.get(position);
                cityPosition = position;
                Log.d("arv city", "arv " + city_selected);
                Log.d("arv city", "arv " + city_list.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        my_education.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                education_selected = education_list_id.get(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });


        my_job.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                job_selected = job_list_id.get(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });


        postContactDetail = new PostContactDetails(Registration_2.this, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                p.cancel();
                super.onResponseReceived(is);
            }

            @Override
            protected void onError(Exception e) {
                // TODO Auto-generated method stub
                p.cancel();
                super.onError(e);
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                super.onDone();

                Intent intent = new Intent(Registration_2.this,
                        DrawerActivity.class);
                startActivity(intent);
                finish();
            }


        };


        country_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                country_selected = country_list_id.get(arg2);

                Log.d("arv country_selected", "arv " + country_selected);
                if (country_list_id.get(arg2) == 159) {
                    state_box.setVisibility(View.GONE);
                    city_box.setVisibility(View.GONE);
                    state_spinner.setVisibility(View.VISIBLE);
                    city_spinner.setVisibility(View.VISIBLE);

                } else {
                    state_box.setVisibility(View.VISIBLE);
                    city_box.setVisibility(View.VISIBLE);
                    state_spinner.setVisibility(View.GONE);
                    city_spinner.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }


        });


        continue_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (height_selected > 0) {
                    if (weight_selected > 0) {
                        if (body_selected > 0) {
                            if (for_selected > 0) {
                                if (education_selected > 0) {
                                    if (job_selected > 0) {
                                        if (country_selected > 0) {

                                            first.setVisibility(View.GONE);
                                            continue_btn.setVisibility(View.GONE);
                                            step.setText("Step-3");
                                            second.setVisibility(View.VISIBLE);
                                            upload_layout.setVisibility(View.VISIBLE);

                                        } else {
                                            Utils.alert(Registration_2.this, getString(R.string.select_country));
                                        }
//									
                                    } else {
                                        Utils.alert(Registration_2.this, getString(R.string.select_job));
                                    }
                                } else {
                                    Utils.alert(Registration_2.this, getString(R.string.select_education));
                                }
                            } else {
                                Utils.alert(Registration_2.this, getString(R.string.select_for));
                            }
                        } else {
                            Utils.alert(Registration_2.this, getString(R.string.select_body));
                        }
                    } else {
                        Utils.alert(Registration_2.this, getString(R.string.select_weight));
                    }
                } else {
                    Utils.alert(Registration_2.this, getString(R.string.select_height));
                }

            }
        });


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                first.setVisibility(View.VISIBLE);
                step.setText("Step-2");
                continue_btn.setVisibility(View.VISIBLE);

                second.setVisibility(View.GONE);
                upload_layout.setVisibility(View.GONE);
            }
        });

        registrationapi = new RegistrationApi(this, db, p) {

            @Override
            protected void updateUI() {
                if (RegistrationApi.resCode == 1 || RegistrationApi.resCode == 900) {
                    p.cancel();
                  /*  AlertDialog alertDialog = new AlertDialog.Builder(
                            Registration_2.this).create();
                    alertDialog.setTitle("Registration Successful");
                    alertDialog.setMessage("Activation link has been sent to your Email. Please verify it from there");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent i = new Intent(Registration_2.this, LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                    });
                    alertDialog.show();*/

                    final Dialog dialog = new Dialog(Registration_2.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_dialog);
                    TextView title_msg = (TextView) dialog.findViewById(R.id.title);
                    TextView msg = (TextView) dialog.findViewById(R.id.message);
                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);


                    ok_btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            Intent i = new Intent(Registration_2.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                    title_msg.setText("Registration Successful");
                    msg.setText("Activation link has been sent to your Email. Please verify it from there");
                    //title_msg.setText("Confirmation..");
                    //String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
                    //msg.setText(str);
                    dialog.show();
                } else {
                    String msg = (RegistrationApi.resMsg != null ? RegistrationApi.resMsg : getResources().getString(R.string.unknown_error));
                    p.cancel();
                    same_id("Error", msg);
                }

            }

            @Override
            protected void onError(Exception e) {

            }

            @Override
            protected void onDone() {


            }

        };

        radio_men.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menClicked();
            }
        });
        men.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menClicked();
            }
        });


        radio_both.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bothClicked();
            }
        });
        both.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bothClicked();
            }
        });


        radio_women.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                womenClicked();
            }
        });


        women.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                womenClicked();
            }
        });

        dodtext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DateDialog();

            }
        });


        String code = Utils.GetCountryZipCode(this);

        phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        phone.setCompoundDrawablePadding(code.length() * 10);

        sign_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("arv", "arv h" + height_selected + " w" + weight_selected + " b" + body_selected + " f" + for_selected + " E" + education_selected + " j" + job_selected + " C" + country_selected + " S" + state_selected + " C" + city_selected);

                //private int height_selected=0, weight_selected=0,body_selected=0,lookin_for_selected=0,for_selected=0,education_selected=0,job_selected,starNclub1=0,starNclub2=0,country_selected=0,state_selected=0,city_selected=0;

//					// TODO Auto-generated method stub
                if (my_handle.getText().length() > 0) {
                    if (about_me.getText().length() > 0) {
                        if (fileSelected > 0) {
                            p.show();
                            PostCurrentImage(tempPath);

                        } else {
                            Utils.alert(Registration_2.this, getString(R.string.select_file));
                        }
                    } else {
                        Utils.alert(Registration_2.this, getString(R.string.select_myself));
                    }
                } else {
                    Utils.alert(Registration_2.this, getString(R.string.select_handle));
                }
            }
        });

    }


    public void setCitySpinner(String state_id) {
        db.open();
        city_list_id.clear();
        city_list.clear();
        data = db.findCursor(Table.Name.nigiria_city_master, Table.nigiria_city_master.state_id.toString() + " = " + state_id, null, null);
        while (data.moveToNext()) {
            city_list_id.add(data.getInt(data.getColumnIndex(Table.nigiria_city_master.id.toString())));
            city_list.add(data.getString(data.getColumnIndex(Table.nigiria_city_master.city_name.toString())));
        }
        city_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, city_list));

        db.close();

    }


    public void postContacts() {
        JSONObject AllContact = new JSONObject();
        db.open();
        Cursor contactData = db.findCursor(Table.Name.user_contact, null, null, null);
        while (contactData.moveToNext()) {

            HashMap<String, String> cont_data = new HashMap<String, String>();
            cont_data.put("contact_name", contactData.getString(contactData.getColumnIndex(Table.user_contact.name.toString())));
            cont_data.put("contact_phone", contactData.getString(contactData.getColumnIndex(Table.user_contact.number.toString())));
            cont_data.put("contact_email", contactData.getString(contactData.getColumnIndex(Table.user_contact.email.toString())));
            JSONObject cont = new JSONObject(cont_data);


            try {
                AllContact.put(contactData.getString(contactData.getColumnIndex(Table.user_contact.id.toString())), cont);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Log.d("arvind", "arvi " + AllContact);

        postContactDetail.setPostData(AllContact);
        callApi(postContactDetail);

    }

    public void HitAPi_2(final HashMap<String, String> map) {

        p.show();
        registration_2_api.setPostData(map);
        callApi(registration_2_api);
    }


    public void hitAPI(final HashMap<String, String> map) {

        p.show();
        registrationapi.setPostData(map);
        callApi(registrationapi);
    }

    public void menClicked() {
        if (interested_in != 1) {
            interested_in = 1;
            radio_men.setImageResource(R.drawable.radio_selected);
            radio_women.setImageResource(R.drawable.radio_unselected);
            radio_both.setImageResource(R.drawable.radio_unselected);
        }
    }


    public void womenClicked() {
        if (interested_in != 2) {
            interested_in = 2;
            radio_men.setImageResource(R.drawable.radio_unselected);
            radio_women.setImageResource(R.drawable.radio_selected);
            radio_both.setImageResource(R.drawable.radio_unselected);
        }
    }

    public void bothClicked() {
        if (interested_in != 3) {
            interested_in = 3;
            radio_men.setImageResource(R.drawable.radio_unselected);
            radio_women.setImageResource(R.drawable.radio_unselected);
            radio_both.setImageResource(R.drawable.radio_selected);
        }
    }

    public void setStarandClub1(int id) {
        db.open();
        p.show();
        data = db.findCursor(Table.Name.starsandclubs_master, Table.starsandclubs_master.sac_type.toString() + " = " + id, null, null);
        snc1_list_id.clear();
        snc1_list.clear();
        if (data.moveToNext()) {
            data.moveToFirst();
            while (data.moveToNext()) {
                snc1_list_id.add(data.getInt(data.getColumnIndex(Table.starsandclubs_master.id.toString())));
                snc1_list.add(data.getString(data.getColumnIndex(Table.starsandclubs_master.sac_title.toString())));
            }
            SNC_spinner1.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc1_list));
            SNC_spinner1.setSelection(first_index);
            db.close();
            p.cancel();
        }
    }

    public void setStarandClub2(int id) {
        db.open();
        p.show();
        data = db.findCursor(Table.Name.starsandclubs_master, Table.starsandclubs_master.sac_type.toString() + " = " + id, null, null);
        snc2_list_id.clear();
        snc2_list.clear();
        if (data.moveToNext()) {
            data.moveToFirst();
            while (data.moveToNext()) {
                snc2_list_id.add(data.getInt(data.getColumnIndex(Table.starsandclubs_master.id.toString())));
                snc2_list.add(data.getString(data.getColumnIndex(Table.starsandclubs_master.sac_title.toString())));
            }
            SNC_spinner2.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc2_list));
            SNC_spinner2.setSelection(second_index);
        }

        db.close();
        p.cancel();
    }


    public void same_id(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Registration_2.this);

        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // UpdateProfile.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void DateDialog() {
        DatePickerDialog dpDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        dpDialog.show();
    }


    protected void PostCurrentImage(String imagePost) {

        String upload_type = String.valueOf(1);
        String type = "image/jpg";
        File file = new File(imagePost);
        String url = Registration_2.this.getString(R.string.urlString) + "api/uploadPhoto/index";
        PostImage callbackservice = new PostImage(file, url, imagePost, pref.get(Constants.TempKeyUUID)/*, Feedid*/, type, upload_type, Registration_2.this) {

            @Override
            public void receiveData() {

                if (PostImage.resCode == 1) {

                    Binddata();

                } else {
                    p.cancel();
                    Utils.alert(Registration_2.this, PostImage.resMsg);
                }
            }

            @Override
            public void receiveError() {
                p.cancel();
                Registration_2.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Utils.alert(Registration_2.this, "Error in uploading Image");
                    }
                });
            }
        };
        callbackservice.execute(url, null, null);
    }

    private void Binddata() {

        Log.d("uploadedddddd*********", "successfully");
        p.cancel();
        HashMap<String, String> send_data = new HashMap<String, String>();
        send_data.put("height_master_id", String.valueOf(height_selected));
        send_data.put("weight_master_id", String.valueOf(weight_selected));
        send_data.put("bodytype_master_id", String.valueOf(body_selected));
        send_data.put("interestedin_master_id", String.valueOf(interested_in));
        send_data.put("interestedin_purpose_master_id", String.valueOf(for_selected));
        send_data.put("education_master_id", String.valueOf(education_selected));
        send_data.put("job_master_id", String.valueOf(job_selected));
        send_data.put("starsAndclubsType1", String.valueOf(starsAndclubsType1));
        send_data.put("starsAndclubsid1", String.valueOf(starsAndclubsid1));
        send_data.put("starsAndclubsType2", String.valueOf(starsAndclubsType2));
        send_data.put("starsAndclubsid2", String.valueOf(starsAndclubsid2));
        send_data.put("country_master_id", String.valueOf(country_selected));
        if (country_selected == 159) {
            send_data.put("city", String.valueOf(city_list.get(cityPosition)));
            send_data.put("state", String.valueOf(state_list.get(statePosition)));
        } else {
            send_data.put("city", city_box.getText().toString());
            send_data.put("state", state_box.getText().toString());
        }
        send_data.put("handle_description", my_handle.getText().toString());
        send_data.put("about_myself", about_me.getText().toString());
        send_data.put("platform_type", "1");
        send_data.put("device_type", "1");
        send_data.put("device_id", "1234");
        send_data.put("uuid", pref.get(Constants.TempKeyUUID));
        send_data.put("auth_token", pref.get(Constants.TempkAuthT));
        HitAPi_2(send_data);
    }

    private void setSpinners() {

        // for Education
        height_list_id.clear();
        height_list.clear();
        weight_list_id.clear();
        weight_list.clear();
        for_list_id.clear();
        for_list.clear();
        education_list_id.clear();
        education_list.clear();
        job_list_id.clear();
        job_list.clear();
        country_list_id.clear();
        country_list.clear();
        state_list_id.clear();
        state_list.clear();
        city_list_id.clear();
        city_list.clear();
        body_list_id.clear();
        body_list.clear();


        db.open();

        data = db.findCursor(Table.Name.height_master, null, null, null);
        height_list.add("Select Height");
        height_list_id.add(0);
        while (data.moveToNext()) {
            height_list_id.add(data.getInt(data.getColumnIndex(Table.height_master.id.toString())));
            height_list.add(data.getString(data.getColumnIndex(Table.height_master.length.toString())));
        }
        height_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, height_list));
        height_spinner.setSelection(0);

        // for weight
        weight_list.add("Select Weight");
        weight_list_id.add(0);
        data = db.findCursor(Table.Name.weight_master, null, null, null);
        while (data.moveToNext()) {
            weight_list_id.add(data.getInt(data.getColumnIndex(Table.weight_master.id.toString())));
            weight_list.add(data.getString(data.getColumnIndex(Table.weight_master.weight.toString())));
        }
        weight_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, weight_list));
        weight_spinner.setSelection(0);

        // for for

        for_list.add("Select Looking For");
        for_list_id.add(0);
        data = db.findCursor(Table.Name.interestedin_purpose_master, null, null, null);
        while (data.moveToNext()) {
            for_list_id.add(data.getInt(data.getColumnIndex(Table.interestedin_purpose_master.id.toString())));
            for_list.add(data.getString(data.getColumnIndex(Table.interestedin_purpose_master.purpose.toString())));
        }
        for_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, for_list));
        for_spinner.setSelection(0);
        // for Education

        education_list.add("Select Educational Status");
        education_list_id.add(0);
        data = db.findCursor(Table.Name.education_master, null, null, null);
        while (data.moveToNext()) {
            education_list_id.add(data.getInt(data.getColumnIndex(Table.education_master.id.toString())));
            education_list.add(data.getString(data.getColumnIndex(Table.education_master.name.toString())));
        }
        my_education.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, education_list));
        my_education.setSelection(0);


        // for Job
        job_list.add("Select Job Status");
        job_list_id.add(0);
        data = db.findCursor(Table.Name.job_master, null, null, null);
        while (data.moveToNext()) {
            job_list_id.add(data.getInt(data.getColumnIndex(Table.job_master.id.toString())));
            job_list.add(data.getString(data.getColumnIndex(Table.job_master.name.toString())));
        }
        my_job.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, job_list));
        my_job.setSelection(0);

        // For Country
        country_list.add("Select Country");
        country_list_id.add(0);
        data = db.findCursor(Table.Name.country_master, null, null, null);

        while (data.moveToNext()) {
            country_list_id.add(data.getInt(data.getColumnIndex(Table.country_master.id.toString())));
            country_list.add(data.getString(data.getColumnIndex(Table.country_master.country_name.toString())));
        }
        country_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, country_list));
        country_spinner.setSelection(0);


        data = db.findCursor(Table.Name.nigiria_state_master, null, null, null);
        while (data.moveToNext()) {
            state_list_id.add(data.getInt(data.getColumnIndex(Table.nigiria_state_master.id.toString())));
            state_list.add(data.getString(data.getColumnIndex(Table.nigiria_state_master.state_name.toString())));
        }
        state_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, state_list));
        state_spinner.setSelection(0);

        // for city
        data = db.findCursor(Table.Name.nigiria_city_master, null, null, null);

        while (data.moveToNext()) {
            city_list_id.add(data.getInt(data.getColumnIndex(Table.nigiria_city_master.id.toString())));
            city_list.add(data.getString(data.getColumnIndex(Table.nigiria_city_master.city_name.toString())));
        }
        city_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, city_list));
        city_spinner.setSelection(0);

        //for bodyType
        body_list.add("Select Body type");
        body_list_id.add(0);
        data = db.findCursor(Table.Name.bodytype_master, null, null, null);
        while (data.moveToNext()) {
            body_list_id.add(data.getInt(data.getColumnIndex(Table.bodytype_master.id.toString())));
            body_list.add(data.getString(data.getColumnIndex(Table.bodytype_master.bodytype_content.toString())));
        }
        body_spinner.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, body_list));
        body_spinner.setSelection(0);


        Cursor starclub = db.findCursor(Table.Name.starsandclubs_category_master, Table.starsandclubs_category_master.status.toString() + " = 1", null, null);
        if (starclub.getCount() > 0) {
            snc_header1.clear();
            snc_header1.add(0, "Select Option");
            starclub.moveToFirst();
            do {
                snc_header1.add(starclub.getString(starclub.getColumnIndex(Table.starsandclubs_category_master.category_name.toString())));

            } while (starclub.moveToNext());


            stateNclub_header1.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc_header1));
            stateNclub_header1.setSelection(0);
            stateNclub_header2.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_dropdown_item_1line, snc_header1));
            stateNclub_header2.setSelection(1);
            setStarandClub1(3);
            setStarandClub2(2);

            db.close();

            p.cancel();
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


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub

        super.onStart();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                try {
                    Uri selectedImageUri = data.getData();
                    //	p.show();
                    tempPath = CelUtils.getpath(selectedImageUri, Registration_2.this);
                    Log.d("Image Path Select", "" + tempPath);
                    selectedPhoto.setText(tempPath);
                    String[] Img_char = tempPath.split("/");
                    //imageName=Img_char[Img_char.length-1];
                    String[] Img_size = {MediaStore.Images.Media.SIZE};
                    fileSelected = 1;
//					Bitmap bm;
//					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//					bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//					prof.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub

    }


}
