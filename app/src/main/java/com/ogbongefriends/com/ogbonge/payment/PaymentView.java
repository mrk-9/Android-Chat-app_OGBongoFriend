package com.ogbongefriends.com.ogbonge.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.EarnPointAPI;
import com.ogbongefriends.com.api.getAccountAPI;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentView extends android.app.Fragment {
    private View rootView;
    Preferences pref;
    WebView webView;
    private ProgressBar progressBar1;
    private ImageView cash_to_points;
    private RelativeLayout account_layout, transaction_details, quickteller_layout;
    private Context _ctx;
    private ScrollView scrollView1;
    private LinearLayout bottom_menu;
    private View btview;
    private TextView s_no, amount, status, transaction_ref, payment_ref, date, mode, action, prev, Next, textView1;
    private int current_transaction = 0;
    private DB db;
    private CustomLoader p;
    private GridView redeemPoints;
    private ListView account_list;
    private getAccountAPI get_account_api;
    private ArrayList<HashMap<String, String>> AccountData;
    private Calendar cal;
    private AccoundDataAdapter account_data_adapter;
    private Button available_plans, redeem_gift, transaction_detail, card, account, quickteller;
    private EarnPointAPI earn_point_api;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    public PaymentView(Context ctx) {
        _ctx = ctx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        pref = new Preferences(getActivity());

        Bundle bundle = getActivity().getIntent().getExtras();

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.paymentwebview, container,
                    false);
            progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
            db = new DB(_ctx);
            scrollView1 = (ScrollView) rootView.findViewById(R.id.scrollView1);
            p = DrawerActivity.p;
            String url = "http://www.ogbongefriends.com/payment/paymentWebview/" + pref.get(Constants.KeyUUID);
            webView = (WebView) rootView.findViewById(R.id.webView1);
            AccountData = new ArrayList<HashMap<String, String>>();
            available_plans = (Button) rootView.findViewById(R.id.available_plans);
            redeem_gift = (Button) rootView.findViewById(R.id.redeem_gift);
            transaction_detail = (Button) rootView.findViewById(R.id.transaction_detail);
            btview = (View) rootView.findViewById(R.id.buttonview);
            redeemPoints = (GridView) rootView.findViewById(R.id.redeemPoints);
            //=============CASH FOR POINTS================================
            //	about_points=(TextView)rootView.findViewById(R.id.about_points);
            //	basic_steps=(TextView)rootView.findViewById(R.id.basic_steps);
            bottom_menu = (LinearLayout) rootView.findViewById(R.id.bottom_menu);
            cash_to_points = (ImageView) rootView.findViewById(R.id.cash_to_points);
            card = (Button) rootView.findViewById(R.id.card);
            account = (Button) rootView.findViewById(R.id.account);
            quickteller = (Button) rootView.findViewById(R.id.quickteller);

            //=====================Transaction================================

            transaction_details = (RelativeLayout) rootView.findViewById(R.id.transaction_details);
            s_no = (TextView) rootView.findViewById(R.id.s_no);
            amount = (TextView) rootView.findViewById(R.id.amount);
            status = (TextView) rootView.findViewById(R.id.status);
            transaction_ref = (TextView) rootView.findViewById(R.id.transaction_ref);
            payment_ref = (TextView) rootView.findViewById(R.id.payment_ref);
            date = (TextView) rootView.findViewById(R.id.date);
            mode = (TextView) rootView.findViewById(R.id.mode);
            action = (TextView) rootView.findViewById(R.id.action);
            prev = (TextView) rootView.findViewById(R.id.prev);
            Next = (TextView) rootView.findViewById(R.id.next);
            textView1 = (TextView) rootView.findViewById(R.id.textView1);
            //=================quickteller_layout=============================
            textView1.setVisibility(View.GONE);
            quickteller_layout = (RelativeLayout) rootView.findViewById(R.id.quickteller_layout);
            cal = Calendar.getInstance();
            card.setTextColor(R.color.blue);
            Spannable WordtoSpan = new SpannableString("Currently you have 8493 points in your Ogbonge's Account. Ogbonge system would be able to convert your Ogbonge's Points into Cash Money.");
            WordtoSpan.setSpan(new ForegroundColorSpan(R.color.green_btn), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //	about_points.setText(WordtoSpan);


            action.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    pref.set("transaction_idex", current_transaction).commit();
                    ((DrawerActivity) getActivity()).displayView(63);
                }
            });


            prev.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    current_transaction--;
                    if (current_transaction >= 0) {
                        p.show();
                        textView1.setVisibility(View.GONE);
                        prev.setVisibility(View.VISIBLE);
                        Next.setVisibility(View.VISIBLE);
                        showTransactionDetails(EarnPointAPI.transaction_array, current_transaction);
                    } else {
                        prev.setVisibility(View.GONE);
                        Next.setVisibility(View.VISIBLE);
                        //Utils.same_id("Ogbonge", "No More data Available", getActivity());
                    }
                }
            });


            Next.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    current_transaction++;
                    textView1.setVisibility(View.GONE);
                    if (current_transaction < EarnPointAPI.transaction_array.size()) {
                        p.show();
                        Next.setVisibility(View.VISIBLE);
                        prev.setVisibility(View.VISIBLE);
                        scrollView1.setVisibility(View.VISIBLE);
                        showTransactionDetails(EarnPointAPI.transaction_array, current_transaction);
                    } else {
                        Next.setVisibility(View.GONE);
                        prev.setVisibility(View.VISIBLE);
                        scrollView1.setVisibility(View.GONE);
                        Utils.same_id("Ogbonge", "No More data Available", getActivity());
                    }
                }
            });

            //=====================ACCOUNT=======================================
            account_layout = (RelativeLayout) rootView.findViewById(R.id.account_layout);
            account_list = (ListView) rootView.findViewById(R.id.account_list);
            account_layout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            bottom_menu.setVisibility(View.VISIBLE);
            transaction_details.setVisibility(View.GONE);
            quickteller_layout.setVisibility(View.GONE);
            cash_to_points.setOnClickListener(new OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //	cash_to_points.setBackgroundColor(R.color.light_blue);
                    Constants.isWebLoading = false;
                    ((DrawerActivity) getActivity()).displayView(62);

                }
            });

            earn_point_api = new EarnPointAPI(_ctx, db, p) {

                @Override
                protected void onDone() {
                    // TODO Auto-generated method stub
                    p.cancel();

                    if (EarnPointAPI.resCode == 1) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

//										if(EarnPointAPI.type.equalsIgnoreCase("1")){
//									CashForPayment.this.reference_id.setText(EarnPointAPI.reference_id);
//									current_points.setText(points);
//									CashForPayment.this.reference_id.setEnabled(false);
//										}

                                if (EarnPointAPI.type.equalsIgnoreCase("3")) {
                                    current_transaction = 0;
                                    if (EarnPointAPI.transaction_array.size() > 0) {
                                        scrollView1.setVisibility(View.VISIBLE);
                                        textView1.setVisibility(View.GONE);
                                        showTransactionDetails(EarnPointAPI.transaction_array, 0);

                                    } else {
                                        scrollView1.setVisibility(View.GONE);
                                        textView1.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                scrollView1.setVisibility(View.GONE);
                                textView1.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    super.onDone();
                }

                @Override
                protected void updateUI() {
                    // TODO Auto-generated method stub
                    super.updateUI();
                }


            };


            account.setOnClickListener(new OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Constants.isWebLoading = false;
                    webView.setVisibility(View.GONE);
                    redeemPoints.setVisibility(View.GONE);
                    account_layout.setVisibility(View.VISIBLE);
                    transaction_details.setVisibility(View.GONE);
                    quickteller_layout.setVisibility(View.GONE);
                    getAccountInfo();

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            account.setTextColor(getActivity().getResources().getColor(R.color.blue));
                            card.setTextColor(getActivity().getResources().getColor(R.color.white));
                            quickteller.setTextColor(getActivity().getResources().getColor(R.color.white));
                        }
                    });

                    //quickteller.setTextColor(R.color.white);
                }
            });

            card.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //account.setTextColor(R.color.white);
                    Constants.isWebLoading = false;
                    card.setTextColor(R.color.blue);
                    webView.setVisibility(View.VISIBLE);
                    redeemPoints.setVisibility(View.GONE);
                    account_layout.setVisibility(View.GONE);
                    quickteller_layout.setVisibility(View.GONE);
                    // TODO Auto-generated method stub
                    account.setTextColor(getActivity().getResources().getColor(R.color.white));
                    card.setTextColor(getActivity().getResources().getColor(R.color.blue));
                    quickteller.setTextColor(getActivity().getResources().getColor(R.color.white));

                }
            });


            quickteller.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Constants.isWebLoading = false;

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            account.setTextColor(getActivity().getResources().getColor(R.color.white));
                            card.setTextColor(getActivity().getResources().getColor(R.color.white));
                            quickteller.setTextColor(getActivity().getResources().getColor(R.color.blue));
                            webView.setVisibility(View.GONE);
                            redeemPoints.setVisibility(View.GONE);
                            account_layout.setVisibility(View.GONE);
                            quickteller_layout.setVisibility(View.VISIBLE);
                        }
                    });


                }
            });

            webView.setWebChromeClient(new MyWebChromeClient());
            webView.setWebViewClient(new MyWebViewClient());
            webView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    // The code of the hiding goest here, just call hideSoftKeyboard(View v);
                    hideSoftKeyboard(v);
                    return false;
                }
            });

            webView.getSettings().setJavaScriptEnabled(true);
            //	webView.getSettings().setPluginsEnabled(true);
            progressBar1.setVisibility(View.VISIBLE);
            webView.loadUrl(url);

            transaction_detail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.isWebLoading = false;
                    // TODO Auto-generated method stub
                    webView.setVisibility(View.GONE);
                    redeemPoints.setVisibility(View.GONE);
                    account_layout.setVisibility(View.GONE);
                    quickteller_layout.setVisibility(View.GONE);
                    transaction_details.setVisibility(View.VISIBLE);

                    RulrForBtn(transaction_detail.getId());


                    HashMap<String, String> dataToProcess = new HashMap<String, String>();
                    dataToProcess.put("uuid", pref.get(Constants.KeyUUID));
                    dataToProcess.put("auth_token", pref.get(Constants.kAuthT));
                    dataToProcess.put("type", "3");

                    dataToProcess.put("convert_points", "");
                    dataToProcess.put("reference_id", "");
                    dataToProcess.put("gain_money", "");
                    dataToProcess.put("bank_name", "");
                    dataToProcess.put("account_holder", "");
                    dataToProcess.put("account_number", "");
                    p.show();
                    earn_point_api.setPostData(dataToProcess);
                    callApi(earn_point_api);

                }
            });


            account_data_adapter = new AccoundDataAdapter(getActivity(), AccountData) {

                @Override
                protected void onItemLongClick(View v, String string) {
                    // TODO Auto-generated method stub

                }

                @Override
                protected void onItemClick(View v, String string) {
                    // TODO Auto-generated method stub

                }
            };


            account_list.setAdapter(account_data_adapter);


            redeem_gift.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //RulrForBtn(redeem_gift.getId());
                    pref.set(Constants.OtherUser, "").commit();
                    ((DrawerActivity) getActivity()).displayView(48);
                }
            });

            available_plans.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    RulrForBtn(available_plans.getId());
                    card.setTextColor(R.color.blue);
                    webView.setVisibility(View.VISIBLE);
                    redeemPoints.setVisibility(View.GONE);
                    account_layout.setVisibility(View.GONE);
                    transaction_details.setVisibility(View.GONE);
                    quickteller_layout.setVisibility(View.GONE);
                    // TODO Auto-generated method stub
                    account.setTextColor(getActivity().getResources().getColor(R.color.white));
                    card.setTextColor(getActivity().getResources().getColor(R.color.blue));
                    quickteller.setTextColor(getActivity().getResources().getColor(R.color.white));

                }
            });


            get_account_api = new getAccountAPI(_ctx, db, p) {

                @Override
                protected void onDone() {
                    // TODO Auto-generated method stub

                    if (getAccountAPI.accountdata.size() > 0) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                setAccountData(getAccountAPI.accountdata);
                            }
                        });

                    }

                    super.onDone();
                }

                @Override
                protected void updateUI() {
                    // TODO Auto-generated method stub

                    super.updateUI();
                }


            };

        }

        return rootView;
    }


    private void showTransactionDetails(JsonArray transactionData, int count) {
        if (transactionData.size() > 0 && transactionData.size() > count) {

            textView1.setVisibility(View.GONE);
            JsonObject data = transactionData.get(count).getAsJsonObject();

            s_no.setText(String.valueOf(count + 1));
            amount.setText(data.get("payment_amount").getAsString());
            status.setText(data.get("payment_status").getAsString());
            transaction_ref.setText(data.get("txn_ref").getAsString());
            payment_ref.setText(data.get("pay_ref").getAsString());
            date.setText(getDate(Long.parseLong(data.get("transaction_date").getAsString()) * 1000, "dd/MM/yyyy hh:mm:ss.SSS"));


            mode.setText(data.get("payment_details").getAsString());
            //action.setText(data.get("Detail").getAsString());

        } else {

            textView1.setVisibility(View.VISIBLE);
        }
        p.cancel();
    }


    private void setAccountData(JsonArray accountData) {
        AccountData.clear();
        HashMap<String, String> accountInfo;
        for (int i = 0; i < accountData.size(); i++) {
            accountInfo = new HashMap<String, String>();
            JsonObject obj = accountData.get(i).getAsJsonObject();
            accountInfo.put("bank_name", obj.get("bank_name").getAsString());
            accountInfo.put("account_name", obj.get("account_name").getAsString());
            accountInfo.put("account", obj.get("account").getAsString());

            AccountData.add(i, accountInfo);


        }
        p.cancel();
        account_data_adapter.notifyDataSetChanged();


    }

    private void getAccountInfo() {

        HashMap<String, String> adata = new HashMap<String, String>();

        adata.put("uuid", pref.get(Constants.KeyUUID));
        adata.put("auth_token", pref.get(Constants.kAuthT));
        adata.put("time_stamp", "" + cal.getTimeInMillis());
        p.show();
        get_account_api.setPostData(adata);
        callApi(get_account_api);

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


    private void RulrForBtn(int id) {


//		 available_plans=(Button)rootView.findViewById(R.id.available_plans);
//		  redeem_gift=(Button)rootView.findViewById(R.id.redeem_gift);
//		  transaction_detail=(Button)rootView.findViewById(R.id.transaction_detail);

        LayoutParams param;
        param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 8);

        if (id == available_plans.getId()) {

            // param.addRule(RelativeLayout.RIGHT_OF, eventMoreInfoId);
            param.addRule(RelativeLayout.LEFT_OF, redeem_gift.getId());
            bottom_menu.setVisibility(View.VISIBLE);

        } else if (id == redeem_gift.getId()) {

            param.addRule(RelativeLayout.LEFT_OF, transaction_detail.getId());
            param.addRule(RelativeLayout.RIGHT_OF, available_plans.getId());
            bottom_menu.setVisibility(View.GONE);
        } else {
            if (id == transaction_detail.getId()) {
                param.addRule(RelativeLayout.RIGHT_OF, redeem_gift.getId());
                bottom_menu.setVisibility(View.GONE);
            }
        }

        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btview.setBackgroundColor(Color.parseColor("#0000FF"));
        btview.setLayoutParams(param);

    }

    public void hideSoftKeyboard(View v) {
        Activity activity = (Activity) v.getContext();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
            //ChatWebView.this.setTitle("Loading...");
            getActivity().setProgress(newProgress * 100); //Make the bar disappear after URL is loaded

            // Return the app name after finish loading
//                if(newProgress == 100)
//                	progressBar1.setVisibility(View.GONE);
        }


    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //return super.shouldOverrideUrlLoading(view, url);
            boolean shouldOverride = false;
            // We only want to handle requests for mp3 files, everything else the webview
            // can handle normally
            if (url.endsWith(".mp3") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".pdf")) {
                shouldOverride = true;
                Uri source = Uri.parse(url);

                // Make a new request pointing to the mp3 url
                DownloadManager.Request request = new DownloadManager.Request(source);
                // Use the same file name for the destination
                File destinationFile = new File(Environment.getExternalStorageDirectory(), source.getLastPathSegment());
                request.setDestinationUri(Uri.fromFile(destinationFile));
                // Add it to the manager
                // manager.enqueue(request);
            } else {
                Constants.isWebLoading = true;
                view.loadUrl(url);
            }
            return shouldOverride;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            Constants.isWebLoading = false;
            progressBar1.setVisibility(View.GONE);
        }
    }

}
