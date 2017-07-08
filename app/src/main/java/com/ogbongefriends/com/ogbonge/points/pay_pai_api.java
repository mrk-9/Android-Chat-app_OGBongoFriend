package com.ogbongefriends.com.ogbonge.points;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.ApiException;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.PayPalApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class pay_pai_api extends PayPalApi implements Runnable {

    @SuppressWarnings("unused")
    private DB db;
    private String url;
    private String postData;
    private Context ctx;
    HashMap<String, String> map1;
    @SuppressWarnings("unused")
//	private SharedPreferences pref;
    public static String auth_token;
    public static String resMsg;
    public static int resCode;
    CustomLoader p;
    Preferences pref;

    public pay_pai_api(Context ctx, DB db, CustomLoader p) {
        super(ctx, db);
        this.db = db;
        this.ctx = ctx;
        this.p = p;
        url = "https://api.sandbox.paypal.com/v1/payments/payment";


        //url = Utils.getCompleteApiUrl(ctx, R.string.getNotification);
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

            JsonParser p = new JsonParser();
            JsonElement jele = p.parse(res);
            JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject() : null;

            if (obj == null) {
                onError(new ApiException(Constants.kApiError));
            } else {


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
        JSONObject json = new JSONObject();
        JSONObject payer_info = new JSONObject();
        JSONArray funding_instruments = new JSONArray();
        JSONObject credit_card = new JSONObject();
        JSONObject credit_card_wrapper = new JSONObject();
        JSONObject payer = new JSONObject();
        JSONObject details = new JSONObject();
        JSONObject amount = new JSONObject();
        JSONObject description = new JSONObject();
        JSONArray transactions = new JSONArray();


        try {
            payer_info.put("email", map12.get("email"));
            credit_card.put("number", map12.get("number"));
            credit_card.put("type", map12.get("type"));
            credit_card.put("expire_month", map12.get("expire_month"));
            credit_card.put("expire_year", map12.get("expire_year"));
            credit_card.put("cvv2", map12.get("cvv2"));
            credit_card.put("first_name", map12.get("first_name"));
            credit_card.put("last_name", map12.get("last_name"));
            credit_card_wrapper.put("credit_card", credit_card);
            funding_instruments.put(credit_card_wrapper);

            details.put("subtotal", map12.get("subtotal"));
            details.put("tax", map12.get("tax"));
            details.put("shipping", map12.get("shipping"));


            payer.put("payment_method", map12.get("payment_method"));
            payer.put("payer_info", payer_info);
            payer.put("funding_instruments", funding_instruments);
            amount.put("details", details);
            amount.put("total", map12.get("total"));
            amount.put("currency", map12.get("currency"));

            description.put("amount", amount);
            description.put("description", map12.get("description"));
            transactions.put(description);

            json.put("intent", map12.get("intent"));
            json.put("payer", payer);
            json.put("transactions", transactions);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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


}