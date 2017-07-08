package com.ogbongefriends.com.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogbongefriends.com.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
//import org.apache.commons.httpclient.methods.multipart.FilePart;
//import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
//import org.apache.commons.httpclient.methods.multipart.Part;
//import org.apache.commons.httpclient.methods.multipart.StringPart;
//import com.community.dcp.sync.SyncService;

public class Utils {

    private static Dialog alert;

    public static void setGradient(int Color1, int Color2, Button vu) {
        Shader textShader = new LinearGradient(0, 0, 0, 20, Color1, Color2,
                TileMode.CLAMP);
        vu.getPaint().setShader(textShader);
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    // ==================================================================

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    // ==================================================================

    public static void exception(String tag, Exception exp, String msg) {
        Log.d(tag, exp.getMessage() + " at " + msg);
    }

    // ===================================================================

    public static String getCompleteApiUrl(Context ctx, int api) {

        return "http://" + ctx.getString(R.string.server) + "/"
                + ctx.getString(R.string.api_intermediary_path) + "/"
                + ctx.getString(api);
    }



    public static   InputStream ResizeAndCompressImage(File sourceFile, int compress_ratio) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            Bitmap bm = BitmapFactory.decodeFile(sourceFile.getPath());
            bm = ExifRotate.rotateBitmap(sourceFile.getPath(), bm);
            InputStream in=null;

            int width = bm.getWidth();
            int height = bm.getHeight();


            if (width > Constants.DEFAULT_IMAGE_MAX_SIZE || height > Constants.DEFAULT_IMAGE_MAX_SIZE) {

                int scaleWidth = 0;//((float) Utility.DEFAULT_IMAGE_MAX_SIZE) / width;
                int scaleHeight = 0;//((float) Utility.DEFAULT_IMAGE_MAX_SIZE) / height;

                if (width > height) {

                    scaleWidth = (Constants.DEFAULT_IMAGE_MAX_SIZE);
                    scaleHeight = (Constants.DEFAULT_IMAGE_MAX_SIZE) * height / width;

                } else if (height > width) {
                    scaleHeight = (Constants.DEFAULT_IMAGE_MAX_SIZE);
                    scaleWidth = (Constants.DEFAULT_IMAGE_MAX_SIZE) * width / height;

                } else {

                    scaleWidth = (Constants.DEFAULT_IMAGE_MAX_SIZE);
                    scaleHeight = (Constants.DEFAULT_IMAGE_MAX_SIZE);
                }

                bm=  Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, false);


            }

            if(compress_ratio!=100 && compress_ratio<100){
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, compress_ratio, blob);
                // in = new ByteArrayInputStream(blob.toByteArray());

               // byte[] bitmapdata = blob.toByteArray();
               // bm= BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
                return  in;
            }
            else{
                return in;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getUrl(Context ctx) {

        return "http://" + ctx.getString(R.string.server) + "/"
                + ctx.getString(R.string.api_intermediary_path) + "/";
    }


    public static String GetCountryZipCode(Context ctx) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = ctx.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

//	public static void advanceSearchInitialization(final Context c,
//			final Activity act, final ArrayList<SearchFieldDetail> searchData) {
//
//		final CustomLoader p = new CustomLoader(act,
//				android.R.style.Theme_Translucent_NoTitleBar);
//		p.show();
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				try {
//
//					// final ArrayList<SearchFieldDetail> searchData =
//					// getSearchData();
//
//					act.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							new SearchResult.AdvanceSearch(act, searchData) {
//
//								@Override
//								protected void onButtonClick(Button btn,
//										EditText txt) {
//									// TODO Auto-generated method stub
//
//								}
//
//								@Override
//								protected void columnValuePair(
//										HashMap<String, String> data) {
//									// TODO Auto-generated method stub
//
//								}
//							}.show();
//
//							
//						}
//					});
//
//				} catch (Exception e) {
//					// TODO: handle exception
//					Log.d("", e.getMessage() + "at People Management Module");
//				} finally {
//					p.dismiss();
//				}
//
//			}
//
//		}).start();
//
//	}

    // =============notifications===========
//
//	public static int ShowMessages(DB db, Context ctx) {
//
//		// TODO Auto-generated method stub
//		Preferences pref = new Preferences(ctx);
//		long lsTS = 0;
//		try {
//			lsTS = Long.parseLong(pref.get(Constants.lastshownmessageTM));
//		} catch (Exception e) {
//
//		}
//
//		int i = db.getCount(Table.Name.message_master, message_master.status
//				+ " = 1 AND message_master.send_time > " + lsTS
//				+ " AND  recipient_id = " + Utils.getCurrentUserID(ctx));
//
//		// if (Constants.isMsgNotification) {
//		// return 0;
//		// }
//
//		// pref.set(Constants.lastshowneventTS, Utils.getmiliTimeStamp());
//
//		return i;
//	}

    // ================================
//	public static int showNotification(DB db, Context ctx) {
//
//		// TODO Auto-generated method stub
//		try {
//
//			Preferences pref = new Preferences(ctx);
//			long lsTS = 0;
//			try {
//				lsTS = Long.parseLong(pref.get(Constants.lastshownfeedTS));
//				// pref.set(Constants.lastshownfeedTS,
//				// Utils.getmiliTimeStamp());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			String sql1 = "SELECT user_feed_master.id , user_feed_master.feed_text ,"
//					+ " user_feed_master.like_count , user_feed_master.add_date , user_feed_master.image_exist_status , uy_users.first_name ,"
//					+ "uy_users.first_name , uy_users.fb_user_id , user_feed_master.user_id, uy_users.image  FROM "
//					+ Table.Name.user_feed_master
//					+ " user_feed_master , "
//					+ Table.Name.uy_users
//					+ " uy_users WHERE user_feed_master.feed_liked IN (select user_feed_master.id from user_feed_master"
//					+ " where user_id="
//					+ Utils.getCurrentUserID(ctx)
//					+ ")"
//					+ " AND user_feed_master.user_id != "
//					+ Utils.getCurrentUserID(ctx)
//					+ " AND user_feed_master.user_id = uy_users.id AND user_feed_master.add_date > "
//					+ lsTS;
//
//			Cursor data = db.findCursor(sql1, null);
//
//			int i = data.getCount();
//
//			// int i = db.getCount(Table.Name.user_feed_master,
//			// "user_feed_master.action = 1 AND user_feed_master.add_date > "
//			// + lsTS + " AND user_feed_master.user_id <> "
//			// + Utils.getCurrentUserID(ctx));
//
//			Utils.log("notification count", i + "");
//
//			// if (Constants.isFeedNotification) {
//			// return 0;
//			// }
//
//			return i;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

    // =============================================

    // public static int showNotification(DB db) {
    //
    // // TODO Auto-generated method stub
    // int i = db.getCount(Table.Name.user_feed_master,
    // "user_feed_master.action = 1 ");
    // if (Constants.isFeedNotification) {
    // return 0;
    // }
    // return i;
    // }

    // ===========================================

//	public static int showCheckIn(DB db, Context ctx) {
//		// TODO Auto-generated method stub
//		Preferences pref = null;
//		long lsTS = 0;
//		try {
//			pref = new Preferences(ctx);
//
//			lsTS = Long.parseLong(pref.get(Constants.lastCheckInClickTS));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		String sql = "SELECT event_master.id, event_master.name, "
//				+ "event_master.event_start_time FROM "
//				+ Table.Name.event_attendee_master
//				+ " event_attendee_master , "
//				+ Table.Name.event_master
//				+ " event_master  WHERE "
//				+ "event_attendee_master.event_id = event_master.id AND event_attendee_master.user_id = "
//				+ Utils.getCurrentUserID(ctx)
//				+ " AND event_attendee_master.status = 1 AND "
//				+ event_master.event_start_time + " > "
//				+ (Long.parseLong(Utils.getmiliTimeStamp()) - 43200) + " AND "
//				+ event_master.event_start_time + " < "
//				+ (Long.parseLong(Utils.getmiliTimeStamp()) + 43200)
//				+ " ORDER BY  event_master.id";
//
//		Cursor data = db.findCursor(sql, null);
//
//		int i = data.getCount();
//
//		// int i = db.getCount(
//		// Table.Name.event_master,
//		// event_master.event_start_time + " > "
//		// + (Long.parseLong(Utils.getmiliTimeStamp()) - 86400)
//		// + " AND " + event_master.event_start_time + " < "
//		// + (Long.parseLong(Utils.getmiliTimeStamp()) + 86400));
//
//		// =====reset time if not get any event======
//		// if(i == 0){
//		// pref.set(Constants.lastCheckInClickTS, 0).commit();
//		// }
//		// =====badge disable for 12 hour======
//		long Cseconds = 0;
//		try {
//			Cseconds = Long.parseLong(getmiliTimeStamp());
//		} catch (Exception e) {
//
//		}
//
//		if (lsTS > (Cseconds - 43200)) {
//			return 0;
//		}
//
//		return i;
//	}

    // ==============show full size image=================

//	public static void showImage(Activity ctx, ArrayList<String> PhotoURLS,
//			int pos) {
//		final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.dialog_image_gallery);
//
//		Gallery gallery = (Gallery) dialog.findViewById(R.id.gallery1);
//
//		GalleryAdapter imageAdapter = new GalleryAdapter(ctx, PhotoURLS, 2);
//
//		imageAdapter.notifyDataSetChanged();
//		gallery.setAdapter(imageAdapter);
//		gallery.setSelection(pos);
//		gallery.setSpacing(10);
//
//		// new ImageLoadingTask(dialogProfileImage, ctx).execute(String
//		// .valueOf(obj));
//
//		dialog.show();
//	}

    // ======================================================================
    public static void onError(Context ctx, Exception e) {
        showToast(ctx, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG);
        Log.d("", "Error =====> " + e.getMessage());
    }

    // ======================================================================
    public static void NoInternet(final Context ctx) {
        ((Activity) ctx).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                same_id("Error", ctx.getString(R.string.no_internet), ctx);

            }
        });
    }

    public static void same_id(String title, String message, Context ctx) {

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        TextView title_msg = (TextView) dialog.findViewById(R.id.title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);


        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        title_msg.setText(title);
        msg.setText(message);
        //title_msg.setText("Confirmation..");
        //String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
        //msg.setText(str);
        dialog.show();

    }


    public static void same_idBACK(String title, String message, Context ctx) {

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        TextView title_msg = (TextView) dialog.findViewById(R.id.title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);


        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();

            }
        });

        title_msg.setText(title);
        msg.setText(message);
        //title_msg.setText("Confirmation..");
        //String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
        //msg.setText(str);
        dialog.show();

    }

    // ======================================================================
    public static void showToast(Context ctx, String text, int duration) {
        Toast.makeText(ctx, text, duration).show();
    }

    // ======================================================================
    public static void showToastOnMainThread(final Context ctx,
                                             final String text, final int duration) {

        ((Activity) ctx).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showToast(ctx, text, duration);
            }
        });

    }

    // ======================================================================
    public static String getDeviceID(Context ctx) {
        // return "123456";
        return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    }

    // ======================================================================

    public static HashMap<String, String> getMapFromJsonObject(JsonObject json) {

        HashMap<String, String> result = new HashMap<String, String>();

        for (Iterator<Entry<String, JsonElement>> it = json.entrySet()
                .iterator(); it.hasNext(); ) {
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) it
                    .next();
            // CelUtils.log(Constants.kApiTag,
            // "Key == "+entry.getKey()+" Value == "+entry.getValue().getAsString());
            result.put(entry.getKey(), entry.getValue().getAsString());
        }

        return result;

    }

    // ======================================================================
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
    }

    // ======================================================================
    public static void SoftKeyBoard(Context c, boolean state) {

        try {
            if (!state) {
                InputMethodManager imm = (InputMethodManager) c
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (((Activity) c).getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(((Activity) c)
                            .getCurrentFocus().getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

        }

    }

    // ======================================================================
    public static void SoftKeyBoard(Context c, View v, boolean state) {
        try {
            if (!state) {
                InputMethodManager imm = (InputMethodManager) c
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // ================================================================
    public static void hideKeyboard(Activity activity) {

        try {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // ================================================================
    public static void ShowKeyboard(Activity activity) {

        try {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Service.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // ================================================================
    public static void alert(Context c, int titleRes, String message) {

        if (alert != null && alert.isShowing()) {
            // ======Do nothing
        } else {

//			x  d
//			final Dialog dialog = new Dialog(c);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.setContentView(R.layout.custom);
//			//dialog.setTitle("Message");
//			
//			Drawable d = new ColorDrawable(Color.BLACK);
//			d.setAlpha(130);
//			dialog.getWindow().setBackgroundDrawable(d);
//			//dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.alpha(6)));
//			// set the custom dialog components - text, image and button
//			TextView text1 = (TextView) dialog.findViewById(R.id.message);
//			text1.setText("Message");
//			TextView text = (TextView) dialog.findViewById(R.id.text);
//			text.setText(message);
//			dialog.setCanceledOnTouchOutside(true);
//			
//			//ImageView image = (ImageView) dialog.findViewById(R.id.image);
//			//image.setImageResource(R.drawable.ic_launcher);
//
////			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
////			// if button is clicked, close the custom dialog
////			dialogButton.setOnClickListener(new OnClickListener() {
////				@Override
////				public void onClick(View v) {
////					dialog.dismiss();
////				}
////			});
//
//			dialog.show();


            final Dialog dialog = new Dialog(c);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alert_dialog);
            TextView title_msg = (TextView) dialog.findViewById(R.id.title);
            TextView msg = (TextView) dialog.findViewById(R.id.message);
            Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);


            ok_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });

            title_msg.setText("Message");
            msg.setText(message);
            //title_msg.setText("Confirmation..");
            //String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
            //msg.setText(str);
            dialog.show();

//			 Dialog connectionDialog = new Dialog(c, R.style.CustomAlertDialog);
//	         //   connectionDialog.setContentView(R.layout.login);
//	            connectionDialog.show();
//	            connectionDialog.setTitle(message);
//	            connectionDialog.setCanceledOnTouchOutside(true);
//	            connectionDialog.show();
//			
//			alert = new AlertDialog.Builder(c)
//			 //.setIcon(R.drawable.)
//					.setTitle(titleRes).setMessage(message).create();
//			Drawable d = new ColorDrawable(Color.BLACK);
//			d.setAlpha(120);
//			alert.getWindow().setBackgroundDrawable(d);
////
//			alert.setCanceledOnTouchOutside(true);
//			alert.show();
        }
    }

    // ================================================================
    public static void alert(Context c, String message) {
        // alert(c, R.string.error, message);
        //alert(c, R.string.app_name, message);
//	vdv
//
//		final Dialog dialog = new Dialog(c);
//		dialog.setContentView(R.layout.custom);
//		dialog.setTitle("Title...");
//
//		// set the custom dialog components - text, image and button
//		TextView text = (TextView) dialog.findViewById(R.id.text);
//		text.setText("Android custom dialog example!");
//		ImageView image = (ImageView) dialog.findViewById(R.id.image);
//		image.setImageResource(R.drawable.ic_launcher);
//
//	//	Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//		// if button is clicked, close the custom dialog
////		dialogButton.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				dialog.dismiss();
////			}
////		});
//
//		dialog.show();
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        TextView title_msg = (TextView) dialog.findViewById(R.id.title);
        TextView msg = (TextView) dialog.findViewById(R.id.message);
        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);


        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        title_msg.setText("Message");
        msg.setText(message);
        //title_msg.setText("Confirmation..");
        //String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
        //msg.setText(str);
        dialog.show();

    }

    // ======================================================================
    public static void showAlertOnMainThread(final Context ctx,
                                             final int titleRes, final String text) {

        ((Activity) ctx).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                alert(ctx, titleRes, text);
            }
        });

    }

    // ======================================================================

    public static boolean isNetworkConnectedMainThred(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
            return false;
        else
            return true;
    }

    // ================================================================
    public static String decodeToBase64(String str) {
        String retStr = null;
        try {
            retStr = new String(Base64.decode(str, Base64.DEFAULT));

        } catch (Exception e) {
            // TODO: handle exception
        }
        return (retStr == null) ? "" : retStr;
    }

    // ================================================================
    public static String encodeToBase64(String str) {
        String retStr = null;
        try {
            retStr = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));

        } catch (Exception e) {
            // TODO: handle exception
        }
        return (retStr == null) ? "" : retStr.replaceAll("\n", "");
    }

    // ================================================================

	/*
     * public static boolean isNetworkConnected(Context ctx) {
	 * 
	 * //new CheckNet(ctx).execute("hiii"); //boolean st =
	 * isWalledGardenConnection();
	 * 
	 * return true; }
	 */

    // ================================================================

    // public static boolean isWalledGardenConnection() {}

    // ================================================================

    /**
     * Post Http data and returns final string and status on network
     */

    // ================================================================

    // public static class CheckNet extends AsyncTask<String, String, Boolean> {
    //
    // private static final String mWalledGardenUrl = "http://m.google.com";
    // private static final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 5000;
    // Context context;
    // Activity activity;
    // public AsyncResponse delegate=null;
    //
    // public CheckNet(Context c) {
    //
    // context = c;
    // activity = (Activity) context;
    // }
    //
    // @Override
    // protected void onPreExecute() {
    //
    // }
    //
    // @Override
    // protected Boolean doInBackground(String... params) {
    //
    // final String mWalledGardenUrl = "http://m.google.com";
    // final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 5000;
    //
    // HttpURLConnection urlConnection = null;
    // try {
    // URL url = new URL(mWalledGardenUrl); //
    // "http://clients3.google.com/generate_204"
    // urlConnection = (HttpURLConnection) url.openConnection();
    // urlConnection.setInstanceFollowRedirects(false);
    // urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
    // urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
    // urlConnection.setUseCaches(false);
    // urlConnection.getInputStream();
    // // We got a valid response, but not from the real google
    // return urlConnection.getResponseCode() != 204;
    // } catch (IOException e) {
    //
    // Log.v("Walled garden check - probably not a portal: exception ",
    // e.getMessage());
    //
    // return false;
    // } finally {
    // if (urlConnection != null) {
    // urlConnection.disconnect();
    // }
    // }
    //
    //
    // }
    //
    // @Override
    // protected void onPostExecute(Boolean st) {
    //
    // android.util.Log.v("ash", st+"");
    // delegate.processFinish(st);
    // }
    //
    // }

    // public interface AsyncResponse {
    // void processFinish(Boolean st);
    // }

    // =================================================================
    public static Boolean CNet() {

        final String mWalledGardenUrl = "http://m.google.com";
        final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 15000;

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mWalledGardenUrl); // "http://clients3.google.com/generate_204"
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
            urlConnection.setUseCaches(false);
            urlConnection.getInputStream();
            // We got a valid response, but not from the real google
            return urlConnection.getResponseCode() != 204;
        } catch (IOException e) {

            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    // =================================================================
    public static int dayDifference(Date date) {

        int days = 0;

        long currMilliSec = Calendar.getInstance().getTimeInMillis();
        long dateMilliSec = date.getTime();

        long diff = Math.abs(currMilliSec - dateMilliSec);

        days = (int) Math.abs(diff / (1000 * 60 * 60 * 24));

        Log.d("", "Difference == " + diff + "Days == " + days + " End Date == "
                + Utils.DateToString(Constants.kTimeStampFormat, date));

        return days;

    }

    // ================================================================

    public static String getDeviceType(Context context) {
        String device;

        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            device = "Android-Tablet";
        } else {
            device = "Android-Phone";
        }

        return device;
    }

    // ================================================================

    public static int getDeviceOrientation(Activity ctx) {

        int getOrient = ctx.getResources().getConfiguration().orientation;

        return getOrient; // return value 1 is portrait and 2 is Landscape Mode

    }

    // ================================================================

    public static int getDevicewidth(Activity ctx) {

        Display mDisplay = ctx.getWindowManager().getDefaultDisplay();
        int width = mDisplay.getWidth();

        return width;

    }

    // ================================================================

    public static int getDeviceheight(Activity ctx) {

        Display mDisplay = ctx.getWindowManager().getDefaultDisplay();
        int height = mDisplay.getHeight();

        return height;

    }

    // ================================================================
    // public static int getPriority(int remainDays){
    //
    // int priority = Constants.kNormalPr;
    //
    // try{
    //
    // if(remainDays < 3){
    // priority = Constants.kHighPr;
    // }
    // else if(remainDays < 5){
    // priority = Constants.kMediumPr;
    // }
    //
    // Log.d("Remaining Days == "+remainDays);
    //
    // }
    // catch(Exception e){
    // Log.d(Constants.kApiExpTag,e.getMessage()+"==== At Get Priority Module");
    // }
    //
    // return priority;
    //
    // }

    // ================================================================

    @SuppressLint("SimpleDateFormat")
    public static String DateToString(String format, Date date) {
        String temp = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            temp = dateFormat.format(date);
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        return temp;
    }

    // ================================================================

    public static void clearPrefrenceOnLogout(Context ctx) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        Editor edit = pref.edit();

        edit.putLong("id", -1).commit();
        edit.putString("auth_token", "").commit();
        edit.putString("fb_id", "").commit();
        edit.putLong("apiUserID", -1).commit();
        edit.putString("client_id", "").commit();
        edit.clear().commit();

    }

    // ================================================================

    public static String getCurrentAuthToken(Context ctx) {
//		SharedPreferences pref = PreferenceManager
//				.getDefaultSharedPreferences(ctx);
        Preferences pref = new Preferences(ctx);

        String token = pref.get(Constants.kAuthT);
        return token;
    }

    // ================================================================

    public static String getCurrentFBID(Context ctx) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String fb_id = pref.getString("fb_id", "");
        return fb_id;
    }

    // ================================================================

//	public static String getCurrentUserName(Context ctx, long UUID) {
//		try {
//
//			DB db = new DB(ctx);
//			Cursor c = db.findCursor(Table.Name.uy_users, uy_users.uuid+ " = " + UUID, null, null);
//			if (c != null && c.moveToFirst()) {
//				String name = c.getString(c.getColumnIndex(uy_users.first_name.toString()))
//						+ " "+ c.getString(c.getColumnIndex(uy_users.last_name.toString()));
//				return name;
//			}
//
//			return null;
//
//		} catch (Exception e) {
//
//		}
//		return null;
//	}

    // ================================================================

//	public static String getCurrentUserAddress(Context ctx, long UUID, DB db) {
//		try {
//			// DB db = new DB(ctx);
//			Cursor c = db.findCursor(Table.Name.uy_users, uy_users.uuid+ " = " + UUID, null, null);
//			if (c != null && c.moveToFirst()) {
//				String address = c.getString(c.getColumnIndex(uy_users.ministry_state.toString()))
//						+ " "+ c.getString(c.getColumnIndex(uy_users.ministry_country.toString()));
//
//				return address;
//			}
//		} catch (Exception e) {
//
//		}
//
//		return null;
//	}

    // ================================================================

    public static long getCurrentUserID(Context ctx) {
//		SharedPreferences pref = PreferenceManager
//				.getDefaultSharedPreferences(ctx);

        Preferences pref = new Preferences(ctx);

        String user_id = pref.get(Constants.KeyUUID);
        return Long.valueOf(user_id);
    }

    // ================================================================

//	public static String getUserImage(Context ctx, long id) {
//
//		DB db = new DB(ctx);
//
//		Cursor c = db.findCursor(Table.Name.uy_users, uy_users.uuid + " = "
//				+ id, null, null);
//		if (c != null && c.moveToFirst()) {
//			return c.getString(c.getColumnIndex(uy_users.profile_image.toString()));
//		}
//
//		return null;
//	}

    // ================================================================

    public static String getCurrentClientId(Context ctx) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        String user_id = pref.getString("client_id", null);
        return user_id;
    }

    // ================================================================
    @SuppressLint("SimpleDateFormat")
    public static Date StringToDate(String format, String date) {
        Date d = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            d = dateFormat.parse(date);
            // Log.d("Date === "+d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            // TODO: handle exception
        }
        return d;
    }

    // ================================================================
    public static String convertDate(String fromFormat, String toFormat,
                                     String date) {

        String retDate = null;
        try {

            Date d = StringToDate(fromFormat, date);
            retDate = DateToString(toFormat, d);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return retDate;

    }

    // ================================================================
    public static int compareDate(Date d1, Date d2) {

        d1.setHours(0);
        d1.setMinutes(0);
        d1.setSeconds(0);

        d2.setHours(0);
        d2.setMinutes(0);
        d2.setSeconds(0);

        return d1.compareTo(d2);

    }

    // ================================================================
//	public static String currentUserName(DB db, long str, Activity ctx) {
//
//		db = new DB(ctx);
//
//		Cursor c = db.findCursor(Table.Name.uy_users, uy_users.uuid + " = "
//				+ str, null, null);
//		if (c != null && c.moveToNext()) {
//			String fname = c.getString(c.getColumnIndex(uy_users.first_name
//					.toString()));
//			String lname = c.getString(c.getColumnIndex(uy_users.last_name
//					.toString()));
//			String name = fname + " " + lname;
//			return name;
//		}
//		return null;
//
//	}

    // ================================================================

    public static void showQuitAlert(final Context ctx) {

        new AlertDialog.Builder(ctx)
                .setCancelable(false)
                .setTitle(ctx.getString(R.string.exit_msg))
                .setInverseBackgroundForced(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // endSession(ctx);
                                quitApp();

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    // ================================================================
    public static void quitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // ================================================================
    public static String getCurrentLocale(Context ctx) {
        return Locale.getDefault().getDisplayLanguage();

    }

    // ================================================================
    public static ProgressDialog getProgress(Context ctx) {

        return ProgressDialog.show(ctx, "Loading", "Please Wait....", true);

    }

    // ================================================================
//	public static CustomLoader getCustomProgress(Context ctx) {
//
//		CustomLoader loader = new CustomLoader(ctx,
//				android.R.style.Theme_Translucent_NoTitleBar);
//		loader.show();
//		return loader;
//
//	}

    // ================================================================
    public static String getCurrTimeStamp() {

        return Utils.DateToString(Constants.kTimeStampFormat, Calendar
                .getInstance().getTime());
    }

    // ================================================================
    @SuppressWarnings("unused")
    public static String getmiliTimeStamp() {

        long LIMIT = 10000000000L;

        long t = Calendar.getInstance().getTimeInMillis();

        return String.valueOf(t).substring(0, 10);
    }

    // ================================================================
    public static String getmilisecTimeStamp() {

        long t = Calendar.getInstance().getTimeInMillis();

        return String.valueOf(t);
    }

    // ===================================================================

    public static String getMediaUrl(Context ctx, int api) {

        return "http://" + ctx.getString(R.string.server) + "/"
                + ctx.getString(api);
    }

    // //================================================================
    // public static boolean uploadImage(Context ctx, String url, final byte[]
    // imageData, String filename ,String message) throws Exception{
    //
    // String responseString = null;
    //
    // PostMethod method;
    //
    // /*String auth_token = Preference.getAuthToken(ctx);*/
    //
    //
    // /* method = new PostMethod("http://10.0.2.20/"+ "upload_image/"
    // +Config.getApiVersion()
    // + "/" +auth_token); */
    //
    // method = new PostMethod(url);
    // org.apache.commons.httpclient.HttpClient client = new
    // org.apache.commons.httpclient.HttpClient();
    // client.getHttpConnectionManager().getParams().setConnectionTimeout(
    // 100000);
    //
    // FilePart photo = new FilePart("icon",
    // new ByteArrayPartSource( filename, imageData));
    //
    // photo.setContentType("image/png");
    // photo.setCharSet(null);
    // String s = new String(imageData);
    // Part[] parts = {
    // new StringPart("message_text", message),
    // new StringPart("template_id","1"),
    // photo
    // };
    //
    // method.setRequestEntity(new
    // MultipartRequestEntity(parts, method.getParams()));
    // client.executeMethod(method);
    // responseString = method.getResponseBodyAsString();
    // method.releaseConnection();
    //
    // Log.e("httpPost", "Response status: " + responseString);
    //
    // if (responseString.equals("SUCCESS")) {
    // return true;
    // } else {
    // return false;
    // }
    // }
    //
	/*
	 * public static void uploadImageNew(Context ctx, String url, File file,
	 * String filename ,String message){
	 * 
	 * HttpClient httpClient = new DefaultHttpClient(); StringBuilder builder =
	 * new StringBuilder(); try { HttpPost request = new HttpPost(url);
	 * 
	 * MultipartEntity entity = new
	 * MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	 * entity.addPart("String_Data_Parameter_Name", new
	 * StringBody("String_Value"));
	 * entity.addPart("Numeric_Data_Parameter_Name", new StringBody(
	 * NumericValue +""));
	 * 
	 * 
	 * 
	 * File f = new File(Environment.getExternalStorageDirectory() +
	 * File.separator + "test.jpg"); f.createNewFile(); //write the bytes in
	 * file FileOutputStream fo = new FileOutputStream(f);
	 * fo.write(outStream.toByteArray()); // remember close the FileOutput
	 * fo.close(); FileBody picBody = new FileBody(file, "image/jpeg");
	 * 
	 * 
	 * 
	 * 
	 * entity.addPart("File_Parameter_Name", picBody);
	 * request.setEntity(entity); HttpResponse response =
	 * httpClient.execute(request); StatusLine statusLine =
	 * response.getStatusLine(); int statusCode = statusLine.getStatusCode();
	 * 
	 * if (statusCode == 200) { HttpEntity responseEntity =
	 * response.getEntity(); InputStream content = responseEntity.getContent();
	 * BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(content));
	 * 
	 * String line; while ((line = reader.readLine()) != null) {
	 * builder.append(line); } json = builder.toString(); Tapabook.d(
	 * "SubeTapaAsincrono json : " + json);
	 * 
	 * }
	 * 
	 * } catch (IllegalStateException e) { Log.e(
	 * "FileUpload IllegalStateException ", e.getMessage() ); } catch
	 * (IOException e) { Log.e( "FileUpload IOException ", e.getMessage() ); }
	 * catch (ParseException e) { Log.e( "FileUpload ParseException " +
	 * e.getMessage() );
	 * 
	 * } finally { // close connections
	 * 
	 * httpClient.getConnectionManager().shutdown(); }
	 * 
	 * }
	 */
    // ================================================================
    public static void endSession(Context context) {

        Preferences pref = new Preferences(context);
        pref.removeKey(Constants.kloginStaffID);
        pref.removeKey(Constants.kloginStaffGrade);
        pref.removeKey(Constants.kUsrName);
        pref.commit();
    }

    // ================================================================
    public static int getAgeFromDOB(String dob) {

        int age = 0;

        try {

            if (dob.length() > 0 && !dob.equals("0000-00-00")) {

                Date dobDate = Utils.StringToDate(Constants.kDOBFormat, dob);
                Date currDate = Calendar.getInstance().getTime();

                age = currDate.getYear() - dobDate.getYear();

            }

        } catch (Exception e) {
            Log.d(Constants.kApiExpTag, e.getMessage()
                    + "at Get Age From DOB mehtod.");
        }

        return age;

    }

    // ================================================================
    public static int getAgeFromDOB(Date dobDate) {

        int age = 0;

        try {

            if (dobDate != null) {

                Date currDate = Calendar.getInstance().getTime();

                // Log.d("Curr year === "+currDate.getYear()+" DOB Date == "+dobDate.getYear());
                age = currDate.getYear() - dobDate.getYear();
                // Log.d("Calculated Age == "+age);

            }

        } catch (Exception e) {
            Log.d(Constants.kApiExpTag, e.getMessage()
                    + "at Get Age From DOB mehtod.");
        }

        return age;

    }

    // ================================================================
    public static Date getDOBFromAge(int year) {

        Date date = Calendar.getInstance().getTime();
        date.setYear(1900 + date.getYear() - year);

        return date;

    }

    // ================================================================
    public static String getDOBFromAgeStr(int year) {

        Date date = Calendar.getInstance().getTime();
        date.setYear(date.getYear() - year);

        return Utils.DateToString(Constants.kDOBFormat, date);

    }

    // ================================================================


    // ================================================================
    public static String getLocaleValue(Cursor c, String defColName,
                                        String locColName) {
        String value = c.getString(c.getColumnIndex(locColName));
        if (value == null) {
            value = c.getString(c.getColumnIndex(defColName));
        }
        return value;
    }


    // =================================================================

    @SuppressLint("SimpleDateFormat")
    public static String getDateFromMilisecond(long milliSeconds,
                                               String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    // =================================================================

    @SuppressLint("SimpleDateFormat")
    public static String getDateFromSecond(long Seconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Seconds * 1000);
        return formatter.format(calendar.getTime());
    }

    // ================================================================
    public static Date getFirstDayOfWeek() {

        Calendar cal = Calendar.getInstance();

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        dow = (dow > 1) ? dow - 2 : 6;

        cal.add(Calendar.DAY_OF_WEEK, -dow);

        Date d = cal.getTime();

        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);

        return d;

    }

    // ================================================================
    public static Date getLastDayOfWeek() {

        Calendar cal = Calendar.getInstance();

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        dow = (dow > 1) ? 7 - dow : -1;

        cal.add(Calendar.DAY_OF_WEEK, dow);

        Date d = cal.getTime();

        d.setHours(23);
        d.setMinutes(59);
        d.setSeconds(59);

        return d;

    }

    // ================================================================

	/*public static Bitmap getImageFromFB(Context ctx, String fbID) {
		String imageURL;
		Bitmap bitmap = null;

		imageURL = "http://graph.facebook.com/" + fbID + "/picture?type=large";
		try {
			// bitmap = BitmapFactory.decodeStream((InputStream) new
			// URL(imageURL)
			// .getContent());

			HttpGet httpRequest = new HttpGet(URI.create(imageURL));
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			bitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
			httpRequest.abort();

		} catch (Exception e) {
			Log.d("TAG", "Loading Picture FAILED" + imageURL);
			e.printStackTrace();
		}
		return bitmap;
	}*/

    // ================================================================

	/*public static Bitmap getThumbImageFromFB(Context ctx, String fbID) {
		String imageURL;
		Bitmap bitmap = null;
		imageURL = "http://graph.facebook.com/" + fbID + "/picture?type=";
		try {
			// bitmap = BitmapFactory.decodeStream((InputStream) new
			// URL(imageURL)
			// .getContent());

			HttpGet httpRequest = new HttpGet(URI.create(imageURL));
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			bitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
			httpRequest.abort();

		} catch (Exception e) {
			Log.d("TAG", "Loading Picture FAILED" + imageURL);
			e.printStackTrace();
		}
		return bitmap;
	}*/

    // ================================================================

    public static Bitmap getImageFromLocal(Context ctx, String text) {
        InputStream imageStream = null;
        try {
            imageStream = ctx.getContentResolver().openInputStream(
                    Uri.parse(text));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap preview_bitmap = BitmapFactory.decodeStream(imageStream, null,
                options);
        return preview_bitmap;
    }

    // ================================================================
    // public static String getStaffSiteQuery(String loginStaffID){
    //
    // String query
    // ="SELECT "+Table.StaffStudySiteElement.site_element_master_id+" FROM "+Table.Name.StaffStudySiteElement+" WHERE "+Table.StaffStudySiteElement.staff_master_id+" = "+loginStaffID+" AND "+Table.StaffStudySiteElement.status+" = 1";
    // Log.d("Staff Site Query ===>>> "+query);
    // return query;
    //
    // }

    // ================================================================
    public static Bitmap getPortraitViewBitmap(String filePath)
            throws IOException {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap resizedBitmap = BitmapFactory.decodeFile(filePath, options);
            if (resizedBitmap != null) {
                ExifInterface exif = new ExifInterface(filePath);
                String orientString = exif
                        .getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer
                        .parseInt(orientString)
                        : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                    rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                    rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                    rotationAngle = 270;

                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle,
                        (float) resizedBitmap.getWidth() / 2,
                        (float) resizedBitmap.getHeight() / 2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0,
                        resizedBitmap.getWidth(), resizedBitmap.getHeight(),
                        matrix, true);

                return rotatedBitmap;
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;
    }

    // ========================================================================

    @SuppressWarnings("unused")
    public static int getScaleedImage(Bitmap bitmap, Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int i = display.getHeight();
        int j = display.getWidth();

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        float scale = (float) j / originalWidth;
        int newHeight = (int) Math.round(originalHeight * scale);

        return newHeight;

    }

    // ==================================================================

    public static String getpath(Uri imageUri, Context context) {
        String res = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(imageUri, proj,
                    null, null, null);
            if (cursor.moveToFirst()) {
                ;
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } catch (Exception e) {

        }

        return res;
    }

    // ===================================================

    public static Bitmap getThumbImage(String rowImage, Context context)
            throws IOException {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(Uri.parse(rowImage),
                proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 32;
        Bitmap resizedBitmap = BitmapFactory.decodeFile(res, options);

        ExifInterface exif = new ExifInterface(res);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString)
                : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            rotationAngle = 270;

        Log.d("Rotation Angle == ", "" + rotationAngle);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) resizedBitmap.getWidth() / 2,
                (float) resizedBitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0,
                resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix,
                true);

        return rotatedBitmap;

    }

	/*
	 * // ====================================================== public static
	 * int FeedForPublicFollow(DB db, Context context, String userId, String
	 * status, String text, String followerId, String followerType) {
	 * 
	 * db = new DB(context);
	 * 
	 * int c = db.isRecordExist(Table.Name.user_feed_master,
	 * user_feed_master.user_id + " = " + userId + " AND " +
	 * user_feed_master.feed_type_id + " = " + followerId, null);
	 * 
	 * if (c != 0) { HashMap<String, String> map = new HashMap<String,
	 * String>(); map.put("status", status); map.put("add_date",
	 * Utils.getCurrTimeStamp());
	 * 
	 * db.update(Table.Name.user_feed_master, map, user_feed_master.user_id +
	 * " = " + userId + " AND " + user_feed_master.feed_type_id + " = " +
	 * followerId, null);
	 * 
	 * } else { HashMap<String, String> map = new HashMap<String, String>();
	 * 
	 * map.put("feed_text", text); map.put("user_id", userId); map.put("status",
	 * status); map.put("feed_type_id", "1"); map.put("add_date",
	 * Utils.getCurrTimeStamp()); map.put("image_id", "");
	 * 
	 * db.insert(Table.Name.user_feed_master, map); } return 0;
	 * 
	 * }
	 */
    // //===============================================================
    //
    // public static int StopPublicFollow(DB db, Context context, String userId,
    // String status, String text, String followerId, String followerType) {
    //
    // db = new DB(context);
    // int c = db.isRecordExist(Table.Name.user_feed_master,
    // user_feed_master.user_id + " = " + userId + " AND "
    // + user_feed_master.feed_type_id + " = 1", null);
    //
    // if (c != 0) {
    // Cursor data = db.findCursor(Table.Name.user_feed_master,
    // user_feed_master.user_id + " = " + userId + " AND "
    // + user_feed_master.feed_type_id + " = 1", null,
    // null);
    //
    // HashMap<String, String> map = new HashMap<String, String>();
    // map.put("status", status);
    // map.put("add_date", Utils.getCurrTimeStamp());
    //
    // db.update(Table.Name.user_feed_master, map,
    // user_feed_master.user_id + " = " + userId + " AND "
    // + user_feed_master.feed_type_id + " = 1", null);
    // }
    // HashMap<String, String> map = new HashMap<String, String>();
    // map.put("feed_text", text);
    // map.put("user_id", userId);
    // map.put("status", status);
    // map.put("feed_type_id", "1");
    // map.put("add_date", Utils.getCurrTimeStamp());
    // // map.put("image_id",String.valueOf(selectedImage));
    // db.insert(Table.Name.user_feed_master, map);
    //
    // return 0;
    //
    // }

    // ===============================================================

//	public static int FeedForTonightUR(DB db, Context context, String userId,
//			String status, String text) {
//
//		db = new DB(context);
//		HashMap<String, String> map = new HashMap<String, String>();
//
//		map.put("feed_text", text);
//		map.put("user_id", userId);
//		map.put("status", status);
//		map.put("add_date", Utils.getCurrTimeStamp());
//		map.put("image_exist_status", "");
//
//		db.insert(Table.Name.user_feed_master, map);
//
//		return 0;
//
//	}

    // ===============================================================


    // =======navigation==========


    // =======download inmage from BF=======

    public static class LongOperation extends AsyncTask<String, Void, Bitmap> {

        private ImageView imv;
        private String path;
        Context context;

        public LongOperation(ImageView leftimage, Context c, Object fbID) {
            // TODO Auto-generated constructor stub
            this.imv = leftimage;
            this.path = fbID.toString();
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imv.setImageResource(R.drawable.ic_launcher);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap btm = null;
            //arvind	btm = Utils.getThumbImageFromFB(context, path);
            return btm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imv.setImageBitmap(result);
            } else {
                imv.setImageResource(R.drawable.ic_launcher);
            }

        }
    }

    // ================

    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    // =======set listview height dinamicly=======

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
                MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    // ========flush table==========

//	public static void flushTable(DB db) {
//		db.clear(Table.Name.uy_users, null);
//		db.clear(Table.Name.user_feed_master, null);
//		db.clear(Table.Name.user_follower_master, null);
//		db.clear(Table.Name.event_attendee_master, null);
//		db.clear(Table.Name.event_feed_images, null);
//		db.clear(Table.Name.event_feed_master, null);
//		db.clear(Table.Name.event_image_master, null);
//		db.clear(Table.Name.event_master, null);
//		db.clear(Table.Name.feed_image_master, null);
//		db.clear(Table.Name.place_master, null);
//		db.clear(Table.Name.place_feed_images, null);
//		db.clear(Table.Name.place_feed_master, null);
//		db.clear(Table.Name.place_follower_master, null);
//		db.clear(Table.Name.place_image_master, null);
//		db.clear(Table.Name.message_master, null);
//		db.clear(Table.Name.someone_u_like_master, null);
//	}

    // =============date & time diff========
    public static void TimeDiff() {
        Calendar calendar = Calendar.getInstance();
        TimeZone fromTimeZone = TimeZone.getTimeZone("CST+2");
        TimeZone toTimeZone = TimeZone.getTimeZone("CST+5.30");

        calendar.setTimeZone(fromTimeZone);
        calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
        if (fromTimeZone.inDaylightTime(calendar.getTime())) {
            calendar.add(Calendar.MILLISECOND, calendar.getTimeZone()
                    .getDSTSavings() * -1);
        }

        calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
        if (toTimeZone.inDaylightTime(calendar.getTime())) {
            calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
        }

        Log.v("time log", calendar.getTime() + "");
    }

    // =============================================

    public static String datedifrence(long d1) {

        String timedif = "";

        try {

            // in seconds

            long diff = Long.parseLong(Utils.getmiliTimeStamp()) - d1;

            long diffSeconds = diff;
            long diffMinutes = diff / 60;
            long diffHours = diff / 3600;
            long diffDays = diff / 86400;
            long diffMonth = diff / 2592000;

            if (diffMonth > 0) {
                if (diffMonth > 1) {
                    timedif = String.valueOf(diffMonth) + " months ago";
                } else {
                    timedif = String.valueOf(diffMonth) + " month ago";
                }
            } else {
                if (diffDays > 0) {
                    if (diffDays > 1) {
                        timedif = String.valueOf(diffDays) + " days ago";
                    } else {
                        timedif = String.valueOf(diffDays) + " day ago";
                    }

                } else {
                    if (diffHours > 0) {
                        if (diffHours > 1) {
                            timedif = String.valueOf(diffHours) + " hours ago";
                        } else {
                            timedif = String.valueOf(diffHours) + " hour ago";
                        }

                    } else {
                        if (diffMinutes > 0) {
                            if (diffMinutes > 1) {
                                timedif = String.valueOf(diffMinutes)
                                        + " minutes ago";
                            } else {
                                timedif = String.valueOf(diffMinutes)
                                        + " minute ago";
                            }

                        } else {
                            if (diffSeconds > 1) {
                                timedif = String.valueOf(diffSeconds)
                                        + " seconds ago";
                            } else {
                                if (diffSeconds > 0) {
                                    timedif = String.valueOf(diffSeconds)
                                            + " second ago";
                                } else {
                                    timedif = "0 second ago";
                                }

                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return timedif;
    }
}
