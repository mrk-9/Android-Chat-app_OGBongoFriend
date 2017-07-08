package com.ogbongefriends.com.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.Login.LoginActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.RegistrationApi;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.TextDrawable;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.Validation;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class Registration extends Activity implements OnClickListener, LocationHelper.MyLocationListener, OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private EditText username, phone, email, con_email, password, con_password, dob, last_name;
    private int mYear, mMonth, mDay;
    private Spinner gender_spinner;
    private Button sign_up;
    private ImageView radio_men, radio_women;
    private ImageView men, women;
    private TextView terms_condition;
    private Validation val;
    private LocationHelper mLocationHelper;
    private CustomLoader p;
    private TextView dodtext;
    private Editor edit;
    private int checker = 0, gender = -1;
    private int selectedGender = 0;
    private CheckBox terms_conditions;
    private DB db;

    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;

    int min = 1;
    int max = 9999999;
    Random r;
//private String[] vals = {"Gender", "Male","Female" };

    private SharedPreferences pref;
    private Preferences preff;
    private RegistrationApi registrationapi;

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        username = (EditText) findViewById(R.id.username);
        last_name = (EditText) findViewById(R.id.last_name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email_id);
        con_email = (EditText) findViewById(R.id.con_email_id);
        password = (EditText) findViewById(R.id.password);
        con_password = (EditText) findViewById(R.id.confirm_password);
        terms_conditions = (CheckBox) findViewById(R.id.terms_condition);
        sign_up = (Button) findViewById(R.id.sign_up);
        dodtext = (TextView) findViewById(R.id.textView1);
        dob = (EditText) findViewById(R.id.dob);
        dob.setEnabled(false);
        radio_men = (ImageView) findViewById(R.id.radio_men);
        radio_women = (ImageView) findViewById(R.id.radio_women);
        men = (ImageView) findViewById(R.id.men);
        women = (ImageView) findViewById(R.id.women);
        terms_condition = (TextView) findViewById(R.id.term_condition_link);
        preff = new Preferences(Registration.this);
        manageLocation();
        r = new Random();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//		session = new SessionManager(getApplicationContext());

        val = new Validation(Registration.this);

        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);

        pref = PreferenceManager.getDefaultSharedPreferences(Registration.this);

        edit = pref.edit();
        db = new DB(this);



        terms_condition.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

					Intent i = new Intent(Registration.this, Terms_And_Condition.class);
					startActivity(i);


            }
        });


        registrationapi = new RegistrationApi(this, db, p) {

            @Override
            protected void updateUI() {
                if (RegistrationApi.resCode == 1) {
                    p.cancel();

                    final Dialog dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.confirmation_dialog);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    TextView msg = (TextView) dialog.findViewById(R.id.message);
                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                    ok_btn.setText("OK");
                    cancel_btn.setVisibility(View.GONE);
                    title.setText(getResources().getText(R.string.rstep1_title));

                    msg.setText(getResources().getText(R.string.rstep1_msg));
                    dialog.show();

                    ok_btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            Intent i = new Intent(Registration.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                } else {

                    if (RegistrationApi.resCode == 700) {


                        preff.set(Constants.TempKeyUUID, String.valueOf(RegistrationApi.userID));
                        preff.set(Constants.TempkAuthT, RegistrationApi.resOuth_token);
                        preff.commit();
                        Intent i = new Intent(Registration.this, Registration_2.class);
                        startActivity(i);
                        finish();
                    } else {
                        String msg = (RegistrationApi.resMsg != null ? RegistrationApi.resMsg : getResources().getString(R.string.unknown_error));
                        p.cancel();
                        Utils.same_id("Error", msg, Registration.this);
                    }
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


        radio_women.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                womenClicked();
            }
        });

        men.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                menClicked();
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


        final String code = Utils.GetCountryZipCode(this);

        phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable("+" + code), null, null, null);
        phone.setCompoundDrawablePadding(code.length() * 20);

        final TelephonyManager manager = (TelephonyManager) Registration.this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso

		/*phone.addTextChangedListener(new TextWatcher() {
            @Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (checker != 1) {
					checker = 1;
					PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
					try {
						Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(phone.getText().toString(), manager.getSimCountryIso().toUpperCase());
						String formatedNumber = phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
						phone.setText(formatedNumber.replace("+" + swissNumberProto.getCountryCode(), ""));
					} catch (NumberParseException e) {
						System.err.println("NumberParseException was thrown: " + e.toString());
					} catch (Exception e) {
						System.err.println("Exception thrown: " + e.toString());
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				checker = 0;
				phone.setSelection(phone.getText().length());
			}

		});*/

        sign_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (username.getText().length() > 0) {
                    if (last_name.getText().length() > 0) {
                        if (phone.getText().length() > 0) {
                            if (phone.getText().length() > 9) {
                                if (phone.getText().length() < 11) {
                                    if (email.getText().length() > 0) {
                                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                                            if (email.getText().toString().equals(con_email.getText().toString())) {
                                                if (password.getText().length() > 0) {
                                                    if (val.checkMinLength(password, 6, "Password")) {
                                                        if (con_password.getText().length() > 0) {
                                                            if (password.getText().toString().equals(con_password.getText().toString())) {
                                                                if (gender > 0) {
                                                                    if (dob.getText().length() > 0) {
                                                                        if (CelUtils.getAgeFromDOB(dob.getText().toString()) >= 18) {
                                                                            if (terms_conditions.isChecked()) {
                                                                                HashMap<String, String> map = new HashMap<String, String>();
                                                                                map.put("first_name", username.getText().toString());
                                                                                map.put("last_name", last_name.getText().toString());
                                                                                map.put("gender", String.valueOf(gender));
                                                                                map.put("date_of_birth", dob.getText().toString());
                                                                                map.put("phone_no", phone.getText().toString());
                                                                                map.put("email", email.getText().toString());
                                                                                map.put("password", password.getText().toString());
                                                                            //    mLocationHelper = null;
                                                                                if (mLocationHelper.getMyLocation() != null) {
                                                                                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation().getLatitude()));
                                                                                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation().getLongitude()));
                                                                                } else {
                                                                                    if (lastKnownLocation != null) {
                                                                                        map.put("latitude", String.valueOf(lastKnownLocation.getLatitude()));
                                                                                        map.put("longitude", String.valueOf(lastKnownLocation.getLongitude()));
                                                                                    } else {
                                                                                        map.put("latitude", "");
                                                                                        map.put("longitude", "");
                                                                                    }
                                                                                }
                                                                                map.put("affiliate", "");

                                                                                if (preff.getInt("from_social") == 1) {
                                                                                    preff.set("from_social", 0);

                                                                                    if (LoginActivity.userData.containsKey("email")) {
                                                                                        map.put("verified", "1");
                                                                                        email.setEnabled(false);
                                                                                    } else {
                                                                                        map.put("verified", "0");
                                                                                    }
                                                                                    map.put("social_network_type", "facebook");
                                                                                    map.put("social_network_id", LoginActivity.userData.get("social_network_id"));
                                                                                } else {
                                                                                    if (preff.getInt("from_social") == 3) {

                                                                                        Log.d("arvind", "arvi " + LoginActivity.userData);
                                                                                        if (LoginActivity.userData.containsKey("email")) {
                                                                                            email.setEnabled(false);
                                                                                            map.put("verified", "1");
                                                                                        } else {
                                                                                            map.put("verified", "0");
                                                                                        }
                                                                                        preff.set("from_social", 0);
                                                                                        map.put("social_network_type", "google");
                                                                                        map.put("social_network_id", LoginActivity.userData.get("social_network_id"));
                                                                                    } else {
                                                                                        map.put("verified", "0");
                                                                                        map.put("social_network_type", "");
                                                                                        map.put("social_network_id", "");
                                                                                    }
                                                                                }

//											map.put("social_network_type","");
//											map.put("social_network_id","");


                                                                                map.put("platform_type", "1");
                                                                                map.put("device_id", Utils.getDeviceID(Registration.this));
                                                                                map.put("device_type", "1");
                                                                                hitAPI(map);
                                                                            } else {
                                                                                Utils.alert(Registration.this, getString(R.string.accept_terms_conditions_to_register));
                                                                            }
                                                                        } else {
                                                                            Utils.alert(Registration.this, getString(R.string.dob_verification));
                                                                        }
                                                                    } else {
                                                                        Utils.alert(Registration.this, getString(R.string.dob));
                                                                    }
                                                                } else {

                                                                    Utils.alert(Registration.this, getString(R.string.gender));
                                                                }


                                                            } else {
                                                                Utils.alert(Registration.this, getString(R.string.password_mismatch));
                                                            }
                                                        } else {
                                                            Utils.alert(Registration.this, getString(R.string.enter_conpassword));
                                                        }
                                                    } else {
                                                        Utils.alert(Registration.this, getString(R.string.enter_pass_length));
                                                    }
                                                } else {
                                                    Utils.alert(Registration.this, getString(R.string.enter_password));
                                                }
                                            } else {
                                                Utils.alert(Registration.this, getString(R.string.enter_conemail));
                                            }

                                        } else {
                                            Utils.alert(Registration.this, getString(R.string.enter_email_format));
                                        }
                                    } else {
                                        Utils.alert(Registration.this, getString(R.string.enter_email));
                                    }
                                } else {
                                    Utils.alert(Registration.this, getString(R.string.phone_length_limit));
                                }
                            } else {
                                Utils.alert(Registration.this, getString(R.string.phone_length));
                            }
                        } else {
                            Utils.alert(Registration.this, getString(R.string.phone));
                        }
                    } else {
                        Utils.alert(Registration.this, getString(R.string.enter_last_name));
                    }
                } else {
                    Utils.alert(Registration.this, getString(R.string.enter_first_name));
                }
            }
        });


        //db.close();
        if (preff.getInt("from_social") == 1 || preff.getInt("from_social") == 2 || preff.getInt("from_social") == 3 || preff.getInt("from_social") == 4) {

            Log.d("arv", "arv" + LoginActivity.userData);

            //	{, gender=male, email=alcanzarsoft123@gmail.com}

            if (LoginActivity.userData.containsKey("first_name")) {
                username.setText("" + LoginActivity.userData.get("first_name"));
            }

            if (LoginActivity.userData.containsKey("last_name")) {
                last_name.setText("" + LoginActivity.userData.get("last_name"));
            }

            if (LoginActivity.userData.containsKey("email")) {
                email.setText("" + LoginActivity.userData.get("email"));
                con_email.setText("" + LoginActivity.userData.get("email"));
            }

            if (LoginActivity.userData.containsKey("gender")) {
                if (LoginActivity.userData.get("gender").equalsIgnoreCase("male")) {
                    radio_men.setSelected(true);
                    gender = 1;
                }
                if (LoginActivity.userData.get("gender").equalsIgnoreCase("female")) {
                    radio_women.setSelected(true);
                    gender = 2;
                }
            }

            if (LoginActivity.userData.containsKey("birthday")) {
                if (LoginActivity.userData.get("birthday").length() > 0) {

                    String tempd[] = LoginActivity.userData.get("birthday").split("/");

                    dob.setText(tempd[2] + "=" + tempd[1] + "-" + tempd[0]);

                }
            }


//			username = (EditText) findViewById(R.id.username);
//			last_name= (EditText) findViewById(R.id.last_name);
//			phone = (EditText) findViewById(R.id.phone);
//			email = (EditText) findViewById(R.id.email_id);
//			con_email = (EditText) findViewById(R.id.con_email_id);
//			password = (EditText) findViewById(R.id.password);
//			con_password = (EditText) findViewById(R.id.confirm_password);
//			terms_conditions=(CheckBox)findViewById(R.id.terms_condition);
//			sign_up=(Button)findViewById(R.id.sign_up);
//			dodtext=(TextView)findViewById(R.id.textView1);
//			dob=(EditText)findViewById(R.id.dob);
//			dob.setEnabled(false);
//			radio_men=(ImageView)findViewById(R.id.radio_men);
//			radio_women=(ImageView)findViewById(R.id.radio_women);
        }

    }

    private void manageLocation() {
        mLocationHelper = new LocationHelper(Registration.this);
        //mLocationHelper.startLocationUpdates(this);
    }




    public void hitAPI(final HashMap<String, String> map) {

        p.show();
        registrationapi.setPostData(map);
        callApi(registrationapi);
    }

    public void menClicked() {
        if (gender == 2 || gender == -1) {
            gender = 1;
            radio_men.setImageResource(R.drawable.radio_selected);
            radio_women.setImageResource(R.drawable.radio_unselected);
        }
    }

    public void womenClicked() {
        if (gender == 1 || gender == -1) {
            gender = 2;
            radio_men.setImageResource(R.drawable.radio_unselected);
            radio_women.setImageResource(R.drawable.radio_selected);
        }
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


    private void callApi(Runnable r) {

        if (!Utils.isNetworkConnectedMainThred(this)) {
            Log.v("Internet Not Conneted", "");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    p.cancel();
                    Utils.same_id("Error", getString(R.string.no_internet), Registration.this);
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
    protected void onDestroy() {
        googleApiClient.disconnect();
        super.onDestroy();
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
    public void onLocationUpdate(Location location) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("onConnected", "onConnected");
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnownLocation != null) {
            Log.e("lastKnownLocation", "lastKnownLocation " + lastKnownLocation.getLongitude() + " " + lastKnownLocation.getLatitude());
        } else Log.e("lastKnownLocation", "null");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("onConnectionSuspended", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "onConnectionFailed");
    }




}
