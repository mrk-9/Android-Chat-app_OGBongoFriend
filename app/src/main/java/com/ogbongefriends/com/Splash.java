package com.ogbongefriends.com;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ogbongefriends.com.ogbonge.push_notification.Config;
import com.ogbongefriends.com.ogbonge.push_notification.ShortcutBadgeException;
import com.ogbongefriends.com.ogbonge.push_notification.ShortcutBadger;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.Login.LoginActivity;
import com.ogbongefriends.com.Login.homePage;
import com.ogbongefriends.com.api.getEntityApi;
import com.ogbongefriends.com.api.settingAccountApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Log;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Splash extends Activity {

    public static final String EXTRA_MESSAGE = "message";
    //Used when storing the registration id and app version to the shared preferences
    public static final String PROPERTY_REG_ID = "registration_id";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static int SPLASH_TIME_OUT = 1000;
    private static Context context;
    Preferences pref;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    /**
     * Class variable for the registration id
     * Either pulled from shared preferences or retrieved from Google
     */
    String regid;
    private DB db;
    private getEntityApi getEntity;
    private settingAccountApi setting_account_api;
    private CustomLoader p;

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the Title bar of this activity screen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        db = new DB(Splash.this);
        p = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar);
        pref = new Preferences(Splash.this);
        context = getApplicationContext();
        try {
            ShortcutBadger.setBadge(Splash.this, 0);
        } catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ShortcutBadgeException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        gcm = new GoogleCloudMessaging();
        new addContact().execute();
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
            if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            pref.set(PROPERTY_REG_ID, regid).commit();
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.d(TAG, "No valid Google Play Services APK found.");
        }


//		Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
//		intent.putExtra("enabled", true);
//		sendBroadcast(intent);


        //Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		intent.putExtra("enabled", true);
//	     Splash.this.sendBroadcast(intent);


        //File mydir = new File(Evironment.getExternalStorageDirectory() + "/mydir/");

        File mydir1 = new File(Environment.getExternalStorageDirectory() + "/OgbongeDir/" + Constants.MyOgbongePhotos + "/");
        File mydir2 = new File(Environment.getExternalStorageDirectory() + "/OgbongeDir/" + Constants.MyPrivateOgbongePhotos + "/");

        if (!mydir1.exists())
            mydir1.mkdirs();
        else
            Log.d("error", "MyOgbongePhotos. already exists");

        if (!mydir2.exists())
            mydir2.mkdirs();
        else
            Log.d("error", "MyPrivateOgbongePhotos. already exists");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        getEntity = new getEntityApi(Splash.this, db, p) {
            @Override
            protected void onError(Exception e) {

            }

            @Override
            protected void onDone() {

            }

            @Override
            protected void updateUI() {
                // Auto-generated method stub
//db.extract(Splash.this);

//				Log.d("arvind", ""+pref.get(Constants.KeyUUID)+"  "+pref.get(Constants.kAuthT));
//
//				if(pref.get(Constants.KeyUUID) != null && !pref.get(Constants.KeyUUID).equals("")){
//					startActivity(new Intent(Splash.this, DrawerActivity.class));
//					finish();
//				}else{
//
//					Intent i = new Intent(Splash.this, homePage.class);
//					startActivity(i);
//					finish();
//				}


                new Handler().postDelayed(new Runnable() {

                    //
                    @Override
                    public void run() {
//

                        db.extract(Splash.this);
                        if (pref.get(Constants.KeyUUID) != null && pref.get(Constants.KeyUUID).toString().length() > 2) {
                            startActivity(new Intent(Splash.this, DrawerActivity.class));
                            finish();
                        } else {

                            Intent i = new Intent(Splash.this, homePage.class);
                            	//Intent i = new Intent(Splash.this, Registration_2.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }, SPLASH_TIME_OUT);
            }
        };

        //if(!pref.getBoolean("entitySynced")){
        pref.setBoolean("entitySynced", true);
        pref.commit();
        HashMap<String, String> items = new HashMap<String, String>();
        items.put(Constants.kTimeStamp, pref.get(Constants.getEntityTime));
        getEntity.setPostData(items);
        callApi(getEntity);
        //	}
        //else{


        //}


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.d(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        PushAsyncTask task = new PushAsyncTask();
        task.execute();

        /**
         * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
         * or CCS to send messages to your app. Not needed for this demo since the
         * device sends upstream messages to a server that echoes back the message
         * using the 'from' address in the message.
         */
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.d(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void callApi(Runnable r) {

      /*  if (!Utils.isNetworkConnectedMainThred(this)) {
            Log.syncMsg("Internet Not Conneted");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    p.cancel();
                    Utils.same_id("Error", getString(R.string.no_internet), Splash.this);
                }
            });
            return;
        } else {
            Log.syncMsg("Internet Conneted");
        }*/

        Thread t = new Thread(r);
        t.setName(r.getClass().getName());
        t.start();
    }

    private class PushAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            try {
                gcm = GoogleCloudMessaging.getInstance(context);
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(Config.GOOGLE_SENDER_ID);
                pref.set(PROPERTY_REG_ID, regid).commit();
                msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                // Your implementation here.
                Log.d("asdf", msg);

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void getContacts() {


        Cursor phones = Splash.this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        db.open();
        Cursor emailCursor;
        while (phones.moveToNext()) {

            HashMap<String, String> contact_data = new HashMap<String, String>();

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contact_data.put(DB.Table.user_contact.number.toString(), phoneNumber);
            contact_data.put(DB.Table.user_contact.name.toString(), name);
            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            ContentResolver contentResolver = getContentResolver();
            String _ID = ContactsContract.Contacts._ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;
            String contact_id = phones.getString(phones.getColumnIndex(_ID));


            emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                    EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

            while (emailCursor.moveToNext()) {
                String _email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                if (_email.length() > 0) {
                    contact_data.put(DB.Table.user_contact.email.toString(), _email);
                } else {
                    contact_data.put(DB.Table.user_contact.email.toString(), "");
                }
            }


            db.autoInsertUpdate(DB.Table.Name.user_contact, contact_data, DB.Table.user_contact.number.toString() + " = '" + phoneNumber + "'", null);

        }
        //emailCursor.close();
        phones.close();
        db.close();
    }

    private class addContact extends AsyncTask<Void, Object, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // mActivity.hideAnimationLoader();
            super.onPostExecute(aVoid);
        }
    }

}
