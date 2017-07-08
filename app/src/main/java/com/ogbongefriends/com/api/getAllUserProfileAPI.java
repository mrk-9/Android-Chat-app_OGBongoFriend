package com.ogbongefriends.com.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.DB.DB;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class getAllUserProfileAPI extends BasicApi implements Runnable {

// type=1 All User 	
// type=2 User Friends List
// type=3 Who LIked Me List	
// type=4 My Favourites List
// type=5 Who Favd Me List
// type=6 Advertise users list	
// type=7 User UnFriends List

    @SuppressWarnings("unused")
    private DB db;
    private String url;
    private String postData;
    private Context ctx;
    HashMap<String, String> map1;
    @SuppressWarnings("unused")
//	private SharedPreferences pref;
    public static String auth_token;
    public static int resCode;
    public static int index;
    private int type = 0;
    public static String resMsg;
    private static JsonArray userdata = null;
    CustomLoader p;
    Preferences pref;

    public getAllUserProfileAPI(Context ctx, DB db, CustomLoader p) {
        super(ctx, db);
        this.db = db;
        this.ctx = ctx;
        this.p = p;
        // url = "http://tmwtg.com/tmwtg/Cpanels/signup";
        url = Utils.getCompleteApiUrl(ctx, R.string.get_all_users);
        // postData = getPostData();
        Log.d("URL ", "" + url);
        pref = new Preferences(ctx);
    }

    public getAllUserProfileAPI(Context ctx, DB db, String page, CustomLoader p) {
        super(ctx, db);
        this.db = db;
        this.ctx = ctx;
        this.p = p;
        // url = "http://tmwtg.com/tmwtg/Cpanels/signup";
        url = Utils.getCompleteApiUrl(ctx, R.string.get_all_users);
        // postData = getPostData();
        Log.d("URL ", "" + url);
        pref = new Preferences(ctx);
    }

    // =====
    public void setPostData(HashMap<String, String> map) {

        map1 = map;

        postData = getPostData(map1);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (url == null) {
            Utils.log(Constants.kApiTag, "Url Not Found in Registration Api");
        } else {
            if (Utils.CNet()) {
                postData(url, postData);
            } else {
                p.cancel();
                Utils.NoInternet(ctx);
            }

        }
    }

    @Override
    protected void onResponseReceived(InputStream is) {
        // TODO Auto-generated method stub
        try {
            String res = getString(is);
            Log.d("Responce>>>>>>>>>>>>"+url ,res);
            JsonParser p = new JsonParser();
            JsonElement jele = p.parse(res);
            JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject() : null;

            if (obj == null) {
                onError(new ApiException(Constants.kApiError));
            } else {

                JsonObject objJson = obj.get(Constants.kappTag).getAsJsonObject();
                resCode = objJson.get("res_code").getAsInt();

                resMsg = objJson.get("res_msg").getAsString();
                if (!objJson.get("Data").isJsonNull() && objJson.get("Data").isJsonArray()) {
                    setUserdata(objJson.get("Data").getAsJsonArray());

                    try {
                        if (type != 6 && type != 7) {
                            index = objJson.get("page_index").getAsInt();
                            db.open();
                            HashMap<String, String> userMap = new HashMap<String, String>();
                            for (int i = 0; i < getUserdata().size(); i++) {
                                userMap.put(DB.Table.user_master.uuid.toString(), getUserdata().get(i).getAsJsonObject().get("uuid").getAsString());
                                userMap.put(DB.Table.user_master.first_name.toString(), getUserdata().get(i).getAsJsonObject().get("first_name").getAsString());
                                userMap.put(DB.Table.user_master.last_name.toString(), getUserdata().get(i).getAsJsonObject().get("last_name").getAsString());
                                userMap.put(DB.Table.user_master.email.toString(), getUserdata().get(i).getAsJsonObject().get("email").getAsString());
                                userMap.put(DB.Table.user_master.gender.toString(), getUserdata().get(i).getAsJsonObject().get("gender").getAsString());
                                userMap.put(DB.Table.user_master.profile_pic.toString(), getUserdata().get(i).getAsJsonObject().get("profile_pic").getAsString());
                                userMap.put(DB.Table.user_master.latitude.toString(), getUserdata().get(i).getAsJsonObject().get("latitude").getAsString());
                                userMap.put(DB.Table.user_master.date_of_birth.toString(), getUserdata().get(i).getAsJsonObject().get("date_of_birth").getAsString());
                                userMap.put(DB.Table.user_master.longitude.toString(), getUserdata().get(i).getAsJsonObject().get("longitude").getAsString());
                                userMap.put(DB.Table.user_master.points.toString(), getUserdata().get(i).getAsJsonObject().get("points").getAsString());
                                userMap.put(DB.Table.user_master.status.toString(), getUserdata().get(i).getAsJsonObject().get("status").getAsString());
                                userMap.put(DB.Table.user_master.server_id.toString(), getUserdata().get(i).getAsJsonObject().get("id").getAsString());
                                userMap.put(DB.Table.user_master.phone_number.toString(), "");
                                userMap.put(DB.Table.user_master.handle_description.toString(), "");
                                userMap.put(DB.Table.user_master.address.toString(), "");
                                userMap.put(DB.Table.user_master.state.toString(), "");
                                userMap.put(DB.Table.user_master.city.toString(), "1");
                                userMap.put(DB.Table.user_master.country_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.pin_code.toString(), "226022");
                                userMap.put(DB.Table.user_master.hide_account.toString(), "0");
                                userMap.put(DB.Table.user_master.interestedin_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.bodytype_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.social_network_id.toString(), "1");
                                userMap.put(DB.Table.user_master.social_network_type.toString(), "");
                                userMap.put(DB.Table.user_master.auth_token.toString(), "");
                                userMap.put(DB.Table.user_master.add_date.toString(), "");
                                userMap.put(DB.Table.user_master.modify_date.toString(), "");
                                userMap.put(DB.Table.user_master.relationship_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.sexuality_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.height_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.weight_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.hair_color_master_id.toString(), "1");
                                userMap.put(DB.Table.user_master.eye_color_master_id.toString(), "2");

                                db.autoInsertUpdate(DB.Table.Name.user_master, userMap, DB.Table.user_master.uuid + " = '" + getUserdata().get(i).getAsJsonObject().get("uuid").getAsString() + "'", null);
                            }
                            db.close();

                            //			userMap.put("email_validate", "1");
//							userMap.put("password", "1");
//							userMap.put(Table.user_master.date_of_birth.toString(), userdata.get("date_of_birth").getAsString());
//							
//							

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            onError(e);

        }
    }

    @Override
    protected void onError(final Exception e) {
        // TODO Auto-generated method stub
        if (ctx instanceof Activity) {
            ((Activity) ctx).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Utils.onError(ctx, e);
                }
            });
        }
    }

    @Override
    protected void onDone() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateUI() {
        // TODO Auto-generated method stub

    }

    private String getPostData(HashMap<String, String> map12) {

        // Preferences p = new Preferences(ctx);

        Log.v("registration/update", map12 + "");

        JSONObject json = new JSONObject();

        JSONObject json2 = new JSONObject();

        try {

            json2.put("uuid", map12.get("uuid"));
            json2.put("auth_token", map12.get("auth_token"));
            json2.put("time_stamp", map12.get("time_stamp"));

            json2.put("latitude", map12.get("latitude"));
            json2.put("longitude", map12.get("longitude"));

            json2.put("type", map12.get("type"));
            type = Integer.parseInt(map12.get("type"));
            json2.put("distance", "2");
            json2.put("page_index", map12.get("page_index"));


            json.put(Constants.kappTag, json2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            json.toString(3);
            Log.v("signmap", json.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }

    public static JsonArray getUserdata() {
        return getAllUserProfileAPI.userdata;
    }

    public static void setUserdata(JsonArray userdata) {
        getAllUserProfileAPI.userdata = userdata;
    }

}
