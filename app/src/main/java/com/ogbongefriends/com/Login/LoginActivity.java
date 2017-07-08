package com.ogbongefriends.com.Login;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.LoginApi;
import com.ogbongefriends.com.api.PostContactDetails;
import com.ogbongefriends.com.api.postPush;
import com.ogbongefriends.com.api.verification_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Log;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.common.Validation;
import com.ogbongefriends.com.common.WebViewActivity;
import com.ogbongefriends.com.register.Registration;
import com.ogbongefriends.com.register.Registration_2;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class LoginActivity extends Activity implements SensorEventListener, ConnectionCallbacks, MyLocationListener, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    //**************twitter information****************
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_USER_NAME = "twitter_user_name";
    private static final String PREF_NAME = "sample_twitter_pref";
    private static final int RC_SIGN_IN = 0;
    public static CustomLoader p;
    public static HashMap<String, String> hashMap;
    public static HashMap<String, String> userData;
    static String TWITTER_CONSUMER_KEY = "3SMIYxTxeOwMGxb01dKFvSPhp"; // place your cosumer key here
    static String TWITTER_CONSUMER_SECRET = "74YZESJa891d88kJ1dGUN1J9bO4jB2qpEGSiNbUF2zdlRLFJ5t"; // place your consumer secret here
    static String PREFERENCE_NAME = "twitter_oauth";
    private static Twitter twitter;
    private static RequestToken requestToken;
    private static SharedPreferences mSharedPreferences;
    int FACEBOOK_REQUEST_CODE = 1;
    int TWITTER_REQUEST_CODE = 2;
    int LINKEDIN_REQUEST_CODE = 3;
    int GOOGLE_REQUEST_CODE = 4;
    ProgressDialog pDialog;
    GoogleSignInOptions gso;
    //******************************************************************
    private LocationHelper mLocationHelper;
    private ProgressDialog progressDialog;
    private ImageView red_crystal, green_crystal, pinc_crystal;
    private GoogleApiClient mGoogleApiClient;
    private float xr = 135, yr = 3, xg = 360, yg = 70, xp = 270, yp = 200;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private EditText email, pass;
    private Button login;
    private ImageView facebook_signUp, LinkedLogin, TwitterLogin, google_signUp, yahoo;//dots;
    private TextView forget_password, create_account;
    private Context ctx;
    private int social_media = 0;
    private LoginApi loginapi;
    private DB db;
    private Preferences pref;
    private postPush postPushApi;
    private boolean mIntentInProgress;
    private AccessToken accessToken;
    private SocialAuthAdapter adapter;
    private boolean mSignInClicked;
    private verification_api verification;
    private PostContactDetails postContactDetail;
    private ConnectionResult mConnectionResult;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        setContentView(R.layout.login_temp);
        email = (EditText) findViewById(R.id.editText1);
        yahoo = (ImageView) findViewById(R.id.yahoo);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userData = new HashMap<String, String>();
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        if (!Utils.isNetworkConnectedMainThred(LoginActivity.this)) {
            // Internet Connection is not present
            Utils.alert(LoginActivity.this, R.string.error, "Please connect to working Internet connection");
            // stop executing code by return
            return;
        }
        manageLocation();

        pass = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.login);
        facebook_signUp = (ImageView) findViewById(R.id.facebook_signUp);
        LinkedLogin = (ImageView) findViewById(R.id.linkedIn);
        TwitterLogin = (ImageView) findViewById(R.id.twiter);
        google_signUp = (ImageView) findViewById(R.id.google);
        forget_password = (TextView) findViewById(R.id.forgetPass);
        create_account = (TextView) findViewById(R.id.createAccount);
        // dots=(ImageView)findViewById(R.id.dots);
        ctx = LoginActivity.this;
        db = new DB(ctx);
        pref = new Preferences(ctx);

     /*   gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //  .requestIdToken(getString(R.string.server_client_id))
                .requestProfile()
                .build();*/

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               // .requestEmail()
                .build();

       mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


       /* mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .build();*/


        create_account.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(LoginActivity.this, Registration.class);
                startActivity(in);
                finish();
            }
        });

        //TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        red_crystal = (ImageView) findViewById(R.id.red_crystal);
        green_crystal = (ImageView) findViewById(R.id.gerrn_crystal);
        pinc_crystal = (ImageView) findViewById(R.id.pinc_crystal);
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
        adapter = new SocialAuthAdapter(new ResponseListener());
        Intent intent = getIntent();


        senSensorManager = (SensorManager) getSystemService(homePage.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


//	    dots.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			
//				final Dialog dialog = new Dialog(ctx);
//				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//				dialog.setContentView(R.layout.social_media_icons);
//				
//				//dialog.setTitle("Message");
//				
//				ImageView google=(ImageView)dialog.findViewById(R.id.google_images);
//
//				ImageView fb=(ImageView)dialog.findViewById(R.id.facebook_images);
//
//				ImageView twitter=(ImageView)dialog.findViewById(R.id.twitter_images);
//
//				ImageView linkedIn=(ImageView)dialog.findViewById(R.id.linkedIn_images);
//
//				ImageView yahoo=(ImageView)dialog.findViewById(R.id.yahoo_images);
//
//
//
//				google.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				// TODO Auto-generated method stub
////					dialog.cancel();
////					startActivity(new Intent(LoginActivity.this, googleSignUp.class));
////					finish();
//					dialog.cancel();
//					startActivityForResult(new Intent(LoginActivity.this,googleSignUp.class), GOOGLE_REQUEST_CODE);	
//					
//				}
//
//				});
//
//
//				fb.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//
//				public void onClick(View v) {
//
//				// TODO Auto-generated method stub
//					dialog.cancel();
//					startActivityForResult(new Intent(LoginActivity.this,FacebookSignUp.class), FACEBOOK_REQUEST_CODE);
//				
//				}
//
//				});
//
//
//
//				twitter.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//
//				public void onClick(View v) {
//
//				// TODO Auto-generated method stub
//					dialog.cancel();
//					//p.show();
//					startActivity(new Intent(LoginActivity.this, MainActivity.class));
//					finish();
//
//				}
//
//				});
//
//
//
//				linkedIn.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//
//				public void onClick(View v) {
//
//				// TODO Auto-generated method stub
//					dialog.cancel();
//					//p.show();
//					startActivityForResult(new Intent(LoginActivity.this, LinkedInSignup.class), LINKEDIN_REQUEST_CODE);
//			
//				}
//				});
//
//
//				yahoo.setOnClickListener(new OnClickListener() {
//
//
//				@Override
//
//				public void onClick(View v) {
//
//				// TODO Auto-generated method stub
//					dialog.cancel();
//
//				}
//
//				});
//				
//				dialog.show();
//			}
//		});
//	    
//	    
        google_signUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                adapter.authorize(LoginActivity.this, Provider.GOOGLEPLUS);
              //  googlePlusLogin();
                //	startActivityForResult(new Intent(LoginActivity.this,googleSignUp.class), GOOGLE_REQUEST_CODE);
            }
        });

        facebook_signUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                adapter.authorize(LoginActivity.this, Provider.FACEBOOK);

                //startActivityForResult(new Intent(LoginActivity.this,FacebookSignUp.class), FACEBOOK_REQUEST_CODE);
            }
        });


        yahoo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                adapter.authorize(LoginActivity.this, Provider.YAHOO);
            }
        });

        TwitterLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                adapter.authorize(LoginActivity.this, Provider.TWITTER);

                //	loginToTwitter();

                //startActivity(new Intent(LoginActivity.this, twitterLogin.class));
                //	finish();
            }
        });


        forget_password.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this, Forget_Password.class));
            }
        });

        LinkedLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.authorize(LoginActivity.this, Provider.LINKEDIN);
            }
        });

        loginapi = new LoginApi(this, db, p) {
            @Override
            protected void onError(Exception e) {
                p.cancel();
            }

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        if (LoginApi.resCode == 1) {
                            pref.set(Constants.KeyUUID, String.valueOf(LoginApi.userID));
                            pref.set(Constants.kAuthT, LoginApi.resOuth_token);
                            pref.set(Constants.kClintId, LoginApi.client_id);
                            pref.commit();
                            pref.setBoolean("comming_from_registration", false).commit();
                            postContacts();


                        } else {

                            if (LoginApi.resCode == 700) {
                                p.cancel();
                                pref.set(Constants.TempKeyUUID, String.valueOf(LoginApi.userID));
                                pref.set(Constants.TempkAuthT, LoginApi.resOuth_token);
                                pref.commit();
                                Log.d("arvi", "arvi" + pref.get(Constants.TempKeyUUID + " , " + pref.get(Constants.TempkAuthT)));
                                Intent inte = new Intent(LoginActivity.this, Registration_2.class);
                                startActivity(inte);

                            } else {
                                p.cancel();

                                if (LoginApi.resCode == 0 && (social_media == 1 || social_media == 2 || social_media == 3 || social_media == 4)) {
                                    pref.set("from_social", social_media).commit();
                                    Intent inte = new Intent(LoginActivity.this, Registration.class);
                                    startActivity(inte);
                                } else {
                                    same_id("Error", LoginApi.resMsg);
                                }


                            }
                        }

                    }
                });

            }

        };


            postContactDetail = new PostContactDetails(LoginActivity.this, db, p) {

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
                postPush();
            }


        };

        verification = new verification_api(LoginActivity.this, db, p) {

            @Override
            protected void onDone() {
                // TODO Auto-generated method stub
                p.cancel();
                Intent intent = new Intent(LoginActivity.this,
                        DrawerActivity.class);
                startActivity(intent);
                finish();


                super.onDone();
            }

            @Override
            protected void updateUI() {
                // TODO Auto-generated method stub
                super.updateUI();
            }


        };


        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Validation val = new Validation(ctx);


                if (email.getText().toString().length() > 0) {
                    if (val.validEmail(email, "Invalid Email ID")) {
                        if (pass.getText().toString().length() > 0) {

                            if (Utils
                                    .isNetworkConnectedMainThred(getApplicationContext())) {
                                // Start a new thread that will download all the

                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("email", email.getText().toString().toLowerCase());
                                map.put("password", pass
                                        .getText().toString());

                                map.put("platform_type", "1");
                                map.put("device_type", "1");
                                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                                if(mLocationHelper.getMyLocation()!=null) {
                                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                                            .getLatitude()));
                                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                                            .getLongitude()));
                                }
                                else{
                                    map.put("latitude", "");
                                    map.put("longitude", "");
                                }
                                map.put("social_network_type", "");
                                map.put("social_network_id", "");


                                hitAPI(map);
                            } else {
                                //same_id("Error", "No internet connection");
                                Utils.alert(LoginActivity.this, "No internet connection");
                            }
                        } else {
                            Utils.alert(LoginActivity.this, "Password can't be blank");
                            //same_id("Error", "Password can't be blank");
                        }
                    } else {
                        Utils.alert(LoginActivity.this, "Email not valid");
                        //same_id("Error", "Email not valid");
                    }

                } else {
                    Utils.alert(LoginActivity.this, "Email can't be blank");
                }

            }

        });
    }

    private void manageLocation() {
        mLocationHelper = new LocationHelper(LoginActivity.this);
        // arvind  mLocationHelper.startLocationUpdates(this);
    }

    private void loginToTwitter() {
        boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
        if (!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */

                //this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
                //	startActivityForResult(new Intent(Intent.ACTION_VIEW,Uri.parse(requestToken.getAuthenticationURL())), TWITTER_REQUEST_CODE);
                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, TWITTER_REQUEST_CODE);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {

            //loginLayout.setVisibility(View.GONE);
            //shareLayout.setVisibility(View.VISIBLE);
        }
    }


    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();


    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    private void googlePlusLogin() {
      /*  Log.e("", "In onclick of Login");
        generateWaitingDialog("Login with Gmail. Please wait ...");
        if (!mGoogleApiClient.isConnecting()) {
            Log.d("mGAClient.isConnecting", "" + mGoogleApiClient.isConnecting());
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.connect();
        }*/
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void generateWaitingDialog(String message) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        } else {
            mGoogleApiClient.connect();
        }
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
//			    
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

    //======================================================================


    private void setDataForRegistration(HashMap<String, String> userData) {

        HashMap<String, String> dataFromSocio = new HashMap<String, String>();
        Log.d("Logined successfull", "arvind");

        if (userData.get("first_name").length() > 0) {
            dataFromSocio.put("first_name", userData.get("first_name"));
        } else {
            dataFromSocio.put("first_name", "");
        }

        //*******************************************

        if (userData.get("last_name").length() > 0) {
            dataFromSocio.put("last_name", userData.get("last_name"));
        } else {
            dataFromSocio.put("last_name", "");
        }

        //********************************************


        if (userData.get("gender").length() > 0) {
            dataFromSocio.put("gender", userData.get("gender"));
        } else {
            dataFromSocio.put("gender", "");
        }

        //************************************************

        if (userData.get("birthday").length() > 0) {
            dataFromSocio.put("date_of_birth", userData.get("birthday"));
        } else {
            dataFromSocio.put("date_of_birth", "");
        }

        //*****************************************************

        if (userData.get("email").length() > 0) {
            dataFromSocio.put("email", userData.get("email"));
        } else {
            dataFromSocio.put("email", "");
        }

        //*******************************************************

        if (userData.get("fb_id").length() > 0) {
            dataFromSocio.put("social_network_id", userData.get("fb_id"));
            dataFromSocio.put("social_network_type", userData.get("fb"));
        } else {
            dataFromSocio.put("social_network_id", "");
            dataFromSocio.put("social_network_type", "");
        }


//			map.put("email", email.getText().toString());
//			map.put("password", password.getText().toString());
//			map.put("latitude", "");
//			map.put("longitude", "");
//			map.put("affiliate", "");
//			map.put("social_network_type","");
//			map.put("social_network_id","");
//			map.put("platform_type","1");
//			map.put("device_id",Utils.getDeviceID(Registration.this));
//			map.put("device_type","1");


    }


    //========================================================================


    //========================================================================

    public void postPush() {
        postPushApi = new postPush(this, db, p) {

            @Override
            protected void onResponseReceived(InputStream is) {
                // TODO Auto-generated method stub
                super.onResponseReceived(is);
            }

            @Override
            protected void onDone() {

                // TODO Auto-generated method stub

                if (social_media > 0 && userData.get("email").length() > 3) {

                    HashMap<String, String> veriData = new HashMap<String, String>();
                    veriData.put("type", "1");
                    veriData.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
                    veriData.put("auth_token", pref.get(Constants.kAuthT));
                    veriData.put("social_network_type", userData.get("social_network_type"));
                    veriData.put("social_network_id", userData.get("social_network_id"));
                    veriData.put("verification_code", "");
                    veriData.put("phone_no", "");
                    veriData.put("email", userData.get("email"));
                    verification.setPostData(veriData);
                    callApi(verification);


                } else {
                    p.cancel();
                    Intent intent = new Intent(LoginActivity.this,
                            DrawerActivity.class);
                    startActivity(intent);
                    finish();
                }

                super.onDone();
            }


        };

        HashMap<String, String> sendData = new HashMap<String, String>();
        sendData.put(Constants.KeyUUID, pref.get(Constants.KeyUUID));
        sendData.put("auth_token", pref.get(Constants.kAuthT));
        sendData.put("regid", pref.get(Constants.PROPERTY_REG_ID));
        sendData.put("device_type", "1");

        postPushApi.setPostData(sendData);
        callApi(postPushApi);

    }


    // ========================================================================

    public void same_id(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ctx);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        // UpdateProfile.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    // =============================================================


    public void postContacts() {
        JSONObject AllContact = new JSONObject();
        db.open();
        Cursor contactData = db.findCursor(DB.Table.Name.user_contact, null, null, null);
        while (contactData.moveToNext()) {

            HashMap<String, String> cont_data = new HashMap<String, String>();
            cont_data.put("contact_name", contactData.getString(contactData.getColumnIndex(DB.Table.user_contact.name.toString())));
            cont_data.put("contact_phone", contactData.getString(contactData.getColumnIndex(DB.Table.user_contact.number.toString())));
            cont_data.put("contact_email", contactData.getString(contactData.getColumnIndex(DB.Table.user_contact.email.toString())));
            JSONObject cont = new JSONObject(cont_data);


            try {
                AllContact.put(contactData.getString(contactData.getColumnIndex(DB.Table.user_contact.id.toString())), cont);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        android.util.Log.d("arvind", "arvi " + AllContact);

        postContactDetail.setPostData(AllContact);
        callApi(postContactDetail);

    }


    // ========================================================================

    public void hitAPI(HashMap<String, String> map) {

        p.show();
        loginapi.setPostData(map);
        callApi(loginapi);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == FACEBOOK_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                // String result=data.getStringExtra("result");
                hashMap = (HashMap<String, String>) data.getSerializableExtra("map");

                //setDataForRegistration(hashMap);
                social_media = 1;
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("email", hashMap.get("email"));
                map.put("password", "");
                map.put("platform_type", "1");
                map.put("device_type", "1");
                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                if(mLocationHelper.getMyLocation()!=null) {
                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLatitude()));
                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLongitude()));
                }
                else{
                    map.put("latitude", "");
                    map.put("longitude", "");
                }
                map.put("social_network_type", "facebook");
                map.put("social_network_id", hashMap.get("id"));


                userData.put("email", hashMap.get("email"));
                userData.put("password", "");
                userData.put("platform_type", "1");
                userData.put("device_type", "1");
                userData.put("device_id", Utils.getDeviceID(LoginActivity.this));
                userData.put("social_network_type", "facebook");
                userData.put("social_network_id", hashMap.get("id"));

                hitAPI(map);

            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


          /*  if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personGooglePlusProfile = currentPerson.getUrl();
            }*/

            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //handleSignInResult(result);
        }

    }


    private void handleSignInResult(GoogleSignInResult result) {
        android.util.Log.d("Testttttttttttt", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            android.util.Log.d("Testttttttttttt", "handleSignInResult:" + acct.getDisplayName()+""+acct.getEmail());

        } else {
            android.util.Log.d("Testttttttttttt", "handleSignInResult: EROORRRRRRR");

        }
    }
    private void saveTwitterInfo(AccessToken accessToken) {

        long userID = accessToken.getUserId();

        User user;
        try {
            user = twitter.showUser(userID);

            String username = user.getName();

		/* Storing oAuth tokens to shared preferences */
            Editor e = mSharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e1) {
            e1.printStackTrace();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub
        progressDialog.dismiss();

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            resolveSignInError();
        }
    }



    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);

                Log.d("DAta From G+", "" + currentPerson.toString());
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.d("arvi", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                //    txtName.setText(personName);
                //  txtEmail.setText(email);

                String json = currentPerson.toString();
                try {
                    JSONObject profile = new JSONObject(json);
                    Log.d("Arvind Profile Data", "" + profile.toString());

                    hashMap = new HashMap<String, String>();

                    hashMap.put("first_name", profile.getJSONObject("name").getString("givenName"));

                    hashMap.put("last_name", profile.getJSONObject("name").getString("familyName"));

                    hashMap.put("id", profile.getString("id"));

                    hashMap.put("gender", profile.getString("gender"));
                    hashMap.put("email", "" + Plus.AccountApi.getAccountName(mGoogleApiClient));


                    social_media = 3;


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("email", "" + Plus.AccountApi.getAccountName(mGoogleApiClient));
                    map.put("password", "");
                    map.put("platform_type", "1");
                    map.put("device_type", "1");
                    map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                    if(mLocationHelper.getMyLocation()!=null) {
                        map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                                .getLatitude()));
                        map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                                .getLongitude()));
                    }
                    else{
                        map.put("latitude", "");
                        map.put("longitude", "");
                    }
                    map.put("social_network_type", "google");
                    map.put("social_network_id", profile.getString("id"));


                    userData.put("email", "" + Plus.AccountApi.getAccountName(mGoogleApiClient));
                    userData.put("password", "");
                    userData.put("platform_type", "1");
                    userData.put("device_type", "1");
                    userData.put("device_id", Utils.getDeviceID(LoginActivity.this));
                    userData.put("social_network_type", "google");
                    userData.put("social_network_id", profile.getString("id"));


                    hitAPI(map);


//				try {
//
//				hashMap.put("email", profile.getString("first_name"));
//
//				} catch (Exception e) {
//				}
//				try {
//
//				hashMap.put("birthday", profile.getString("first_name"));
//
//				} catch (Exception e) {
//				}
//				Intent returnIntent = new Intent();
//				returnIntent.putExtra("map", hashMap);
//				setResult(RESULT_OK,returnIntent);
                    //finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        progressDialog.dismiss();
        mSignInClicked = false;
        // Toast.makeText(this, "User is connected!",Toast.LENGTH_LONG).show();

        // Get user's information
        p.show();
        getProfileInformation();

        // Update the UI after signin
        //updateUI(true);


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationUpdate(Location location) {
        // TODO Auto-generated method stub

    }

    private final class ResponseListener implements DialogListener {
        public void onComplete(Bundle values) {

            adapter.getUserProfileAsync(new ProfileDataListener());
        }

        @Override
        public void onBack() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(SocialAuthError arg0) {
            // TODO Auto-generated method stub

        }
    }

    private final class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onError(SocialAuthError arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onExecute(String arg0, Profile arg1) {
            // TODO Auto-generated method stub
            Profile profileMap = arg1;


            if(arg0.equalsIgnoreCase("googleplus")){


                social_media = 3;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("password", "");
                map.put("platform_type", "1");
                map.put("device_type", "1");
                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                if(mLocationHelper.getMyLocation()!=null) {
                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLatitude()));
                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLongitude()));
                }
                else{
                    map.put("latitude", "");
                    map.put("longitude", "");
                }




                map.put("social_network_type", "google");
                map.put("social_network_id", profileMap.getValidatedId());
                String nm[] = profileMap.getFullName().split(" ");
                if (nm[0] != null) {
                    userData.put("first_name", "" + nm[0]);
                } else {
                    userData.put("first_name", "");
                }
                if(nm.length>1) {
                    if (nm[1] != null) {
                        userData.put("last_name", "" + nm[1]);
                    } else {
                        userData.put("last_name", "");
                    }
                }
                else{
                    userData.put("last_name", "");
                }

                if (profileMap.getEmail() != null) {
                    userData.put("email", "" + profileMap.getEmail());
                    map.put("email", "" + profileMap.getEmail());
                } else {
                    userData.put("email", "");
                    map.put("email", "");
                }


                if (profileMap.getGender() != null) {
                    userData.put("gender", "" + profileMap.getGender());
                } else {
                    userData.put("gender", "");
                }

                if (profileMap.getDob() != null) {
                    userData.put("birthday", "" + profileMap.getDob());
                } else {
                    userData.put("birthday", "");
                }


                hitAPI(map);

            }


            if (arg0.equalsIgnoreCase("twitter")) {

//			 //type 'facebook','twitter','linkedin','google','yahoo'

                social_media = 2;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("password", "");
                map.put("platform_type", "1");
                map.put("device_type", "1");
                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                if(mLocationHelper.getMyLocation()!=null) {
                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLatitude()));
                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLongitude()));
                }
                else{
                    map.put("latitude", "");
                    map.put("longitude", "");
                }
                map.put("social_network_type", "twitter");
                map.put("social_network_id", profileMap.getValidatedId());
                String nm[] = profileMap.getFullName().split(" ");
                if (nm[0] != null) {
                    userData.put("first_name", "" + nm[0]);
                } else {
                    userData.put("first_name", "");
                }
                if(nm.length>1) {
                    if (nm[1] != null) {
                        userData.put("last_name", "" + nm[1]);
                    } else {
                        userData.put("last_name", "");
                    }
                }
                else{
                    userData.put("last_name", "");
                }

                if (profileMap.getEmail() != null) {
                    userData.put("email", "" + profileMap.getEmail());
                    map.put("email", "" + profileMap.getEmail());
                } else {
                    userData.put("email", "");
                    map.put("email", "");
                }


                if (profileMap.getGender() != null) {
                    userData.put("gender", "" + profileMap.getGender());
                } else {
                    userData.put("gender", "");
                }

                if (profileMap.getDob() != null) {
                    userData.put("birthday", "" + profileMap.getDob());
                } else {
                    userData.put("birthday", "");
                }


                hitAPI(map);
            }


            if (arg0.equalsIgnoreCase("linkedin")) {
                p.show();
                social_media = 4;
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("email", "" + profileMap.getEmail());
                map.put("password", "");
                map.put("platform_type", "1");
                map.put("device_type", "1");
                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                if(mLocationHelper.getMyLocation()!=null) {
                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLatitude()));
                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLongitude()));
                }
                else{
                    map.put("latitude", "");
                    map.put("longitude", "");
                }
                map.put("social_network_type", "linkedin");
                map.put("social_network_id", profileMap.getValidatedId());


                if (profileMap.getFirstName() != null) {
                    userData.put("first_name", "" + profileMap.getFirstName());
                } else {
                    userData.put("first_name", "");
                }
                if (profileMap.getLastName() != null) {
                    userData.put("last_name", "" + profileMap.getLastName());
                } else {
                    userData.put("last_name", "");
                }

                if (profileMap.getEmail() != null) {
                    userData.put("email", "" + profileMap.getEmail());
                    map.put("email", "" + profileMap.getEmail());
                } else {
                    userData.put("email", "");
                    map.put("email", "");
                }


                if (profileMap.getGender() != null) {
                    userData.put("gender", "" + profileMap.getGender());
                } else {
                    userData.put("gender", "");
                }

                if (profileMap.getDob() != null) {
                    userData.put("birthday", "" + profileMap.getDob());
                } else {
                    userData.put("birthday", "");
                }

                hitAPI(map);
            }


            if (arg0.equalsIgnoreCase("facebook")) {

                social_media = 1;
                HashMap<String, String> map = new HashMap<String, String>();


                map.put("email", "" + profileMap.getEmail());
                map.put("password", "");
                map.put("platform_type", "1");
                map.put("device_type", "1");
                map.put("device_id", Utils.getDeviceID(LoginActivity.this));
                if(mLocationHelper.getMyLocation()!=null) {
                    map.put("latitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLatitude()));
                    map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
                            .getLongitude()));
                }
                else{
                    map.put("latitude", "");
                    map.put("longitude", "");
                }
                map.put("social_network_type", "facebook");
                map.put("social_network_id", profileMap.getValidatedId());


                if (profileMap.getFirstName() != null) {
                    userData.put("first_name", "" + profileMap.getFirstName());
                } else {
                    userData.put("first_name", "");
                }
                if (profileMap.getLastName() != null) {
                    userData.put("last_name", "" + profileMap.getLastName());
                } else {
                    userData.put("last_name", "");
                }

                if (profileMap.getEmail() != null) {
                    userData.put("email", "" + profileMap.getEmail());
                    map.put("email", "" + profileMap.getEmail());
                } else {
                    userData.put("email", "");
                    map.put("email", "");
                }


                if (profileMap.getGender() != null) {
                    userData.put("gender", "" + profileMap.getGender());
                } else {
                    userData.put("gender", "");
                }

                if (profileMap.getDob() != null) {
                    userData.put("birthday", "" + profileMap.getDob());
                } else {
                    userData.put("birthday", "");
                }

                hitAPI(map);


            }


        }

    }


}
