package com.ogbongefriends.com.common;

// this is a class containing some of the common functions used in several places

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ogbongefriends.com.R;


public class CelUtils implements android.view.View.OnClickListener{
	
	private static boolean isSyncing = false;
	public static boolean PostSynced = false;
	public static boolean totalSync = false;
	private static boolean checkLogin=false;
	private static AlertDialog alert;
	public static Locale locale;
	private static String staffQuery = null; 









	// to set the Gradient of the view
	public static void setGradient(int Color1, int Color2,Button vu)
	{
		 Shader textShader = new LinearGradient(0, 0, 0, 20,Color1,Color2, TileMode.CLAMP);
		 vu.getPaint().setShader(textShader);
	}
	
	//==================================================================
	// to print the log 
	
	public static void log(String tag, String msg){
		Log.d(tag, msg);
	}
	
	
	public static String dateDifrence(long d1) {

		String timedif = "";

		try {

			// in seconds

			long diff = (getsecTimeStamp()) - d1;

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
	
	public static  String shuffleString(String string)
	{
	  List<String> letters = Arrays.asList(string.split(""));
	  Collections.shuffle(letters);
	  String shuffled = "";
	  for (String letter : letters) {
	    shuffled += letter;
	  }
	  return shuffled;
	}
	//====================================================================
	
	// copy the data from input to output stream
 
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	//===================================================================
	
	
		
	//=================================================================
	
	// return the with of the device
	public static int getDevicewidth(Activity ctx) {

		Display mDisplay = ctx.getWindowManager().getDefaultDisplay();
		int width = mDisplay.getWidth();

		return width;

	}
	
//	// general alert dialog box
//	public static void same_id(String title, String message, Context ctx) {
//		//AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
//
//		// set title
////		alertDialogBuilder.setTitle(title);
////
////		// set dialog message
////		alertDialogBuilder.setMessage(message).setCancelable(false)
////				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
////					public void onClick(DialogInterface dialog, int id) {
////						// if this button is clicked, close
////						// current activity
////						dialog.dismiss();
////						// UpdateProfile.this.finish();
////					}
////				});
////
////		// create alert dialog
////		AlertDialog alertDialog = alertDialogBuilder.create();
//
//		Dialog dialog = new Dialog(ctx);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
//        dialog.setContentView(R.layout.alert_dialog);
//       TextView title_msg=(TextView)dialog.findViewById(R.id.title);
//       TextView msg=(TextView)dialog.findViewById(R.id.message);
//		Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
//		
////		 ok_btn.setOnClickListener(new OnClickListener() {
////				
////				@Override
////				public void onClick(View v) {
////					// TODO Auto-generated method stub
//////					dialog.dismiss();
//////					sendGifttoUser(string);
////				}
////
////				@Override
////				public void onClick(DialogInterface dialog, int which) {
////					// TODO Auto-generated method stub
////					
////				}
////			});
//		
//		
//		title_msg.setText(title);
//		msg.setText(message);
//		//title_msg.setText("Confirmation..");
//		//String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
//		//msg.setText(str);
//        dialog.show();
//       
//		// show it
//		//alertDialog.show();
//
//	}

	// return the bitmap of the image from the path passed in the parameter list
	
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

	//==========================================================================
	
	
	
	
	public static DisplayImageOptions getImageOptionBlur()
	{
		
		
		DisplayImageOptions	profilepicOptions = new DisplayImageOptions.Builder()
	   .cacheInMemory()
	    .cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_4444).build();
		
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
    .resetViewBeforeLoading(false)  // default
    .delayBeforeLoading(1000)
    
    .cacheInMemory(true) // default
    .cacheOnDisk(true) // default
    .considerExifParams(false) // default
    .build();
	 
		return options;
	}
	
	
	public static DisplayImageOptions getImageOption()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
    .resetViewBeforeLoading(false)  // default
    .delayBeforeLoading(1000)
    .cacheInMemory(true) // default
    .cacheOnDisk(true) // default
    .considerExifParams(false) // default
    .build();
	 
		return options;
	}
	
	
	

	public static ImageLoaderConfiguration getImageConfigBlur(Context context){
 
 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			
			
			
	        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
	        .diskCacheExtraOptions(480, 800, null)
	        .threadPoolSize(3) // default
	        .threadPriority(Thread.NORM_PRIORITY - 2) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCacheSize(2 * 1024 * 1024)
	        .memoryCacheSizePercentage(26) // default
	        .diskCacheSize(1000 * 1024 * 1024)
	        .diskCacheFileCount(100)
	        
	        .imageDownloader(new BaseImageDownloader(context)) // default
	        .imageDecoder(new BaseImageDecoder(false)) // default
	        .defaultDisplayImageOptions(getImageOptionBlur()) // default
	        .writeDebugLogs()
	        .build();
			
		return config;
			
			
	}
	
	
	public static ImageLoaderConfiguration getImageConfig(Context context){
 
 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			
			
			
	        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
	        .diskCacheExtraOptions(480, 800, null)
	        .threadPoolSize(3) // default
	        .threadPriority(Thread.NORM_PRIORITY - 2) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCacheSize(2 * 1024 * 1024)
	        .memoryCacheSizePercentage(26) // default
	        .diskCacheSize(1000 * 1024 * 1024)
	        .diskCacheFileCount(100)
	        
	        .imageDownloader(new BaseImageDownloader(context)) // default
	        .imageDecoder(new BaseImageDecoder(false)) // default
	        .defaultDisplayImageOptions(getImageOption()) // default
	        .writeDebugLogs()
	        .build();
			
		return config;
			
			
	}
	
	
	// ========================================================================

	// retuen the height of the scalled image
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
			String[] proj = { MediaStore.Images.Media.DATA };
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
	
	
// return the path of the image from the URI	
	public static String getpathVideo(Uri imageUri, Context context) {
		String res = null;
		try {
			String[] proj = { MediaStore.Video.Media.DATA };
			Cursor cursor = context.getContentResolver().query(imageUri, proj,
					null, null, null);
			if (cursor.moveToFirst()) {
				;
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
				res = cursor.getString(column_index);
			}
			cursor.close();
		} catch (Exception e) {

		}

		return res;
	}

	// ===================================================

	// return the tuumbImage froma url of the image
	public static Bitmap getThumbImage(String rowImage, Context context)
			throws IOException {

		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
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

	
	//==================================================================
	
	public static void exception(String tag,Exception exp, String msg){
		Log.d(tag,exp.getMessage()+" at "+ msg);
	}
	
	 //===================================================================
	// return complete API URL
    public static String getCompleteApiUrl(Context ctx, int api){
        
        return "http://"+ctx.getString(R.string.server)+"/"+ctx.getString(R.string.api_intermediary_path)+"/"+ctx.getString(api);
    }
    
	 //===================================================================
 // return complete Media URL
    public static String getMediaUrl(Context ctx, int api){
        
        return "http://"+ctx.getString(R.string.server)+"/"+ctx.getString(api);
    }
    
    //===================================================================
 // return complete API URL
    public static String getCompleteApiUrl(Context ctx, int api,Object[] o){
        
        return "http://"+ctx.getString(R.string.server)+"/"+ctx.getString(R.string.api_intermediary_path)+"/"+ctx.getString(api,o);
    }
    
    //======================================================================
    public static void onError(Context ctx, Exception e){
    	showToast(ctx, "Error: "+e.getLocalizedMessage(),  Toast.LENGTH_LONG);
    	Log.d("Error =====> "+e.getMessage(), "");
    }
    
    //======================================================================
    // to show toast
    public static void showToast(Context ctx,String text, int duration){
    	Toast.makeText(ctx, text, duration).show();
    }
    
    //======================================================================
    // to show toast on com.ogbonge thread
    public static void showToastOnMainThread(final Context ctx,final String text,final int duration){
    	
    	((Activity)ctx).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showToast(ctx, text, duration);
			}
		});
    	
    }
    
    //======================================================================
    // returned unique deviceId
    public static String getDeviceID(Context ctx){
    	
    	return getIMEINumber(ctx);
//    	return "123456";
//    	return Secure.getString(ctx.getContentResolver(),
//                Secure.ANDROID_ID); 
    }
    
    // Return the EMI number of the device
    public static String getIMEINumber(Context ctx){
		
		TelephonyManager    telephonyManager;                                             
        
	    telephonyManager  =  ( TelephonyManager )ctx.getSystemService( Context.TELEPHONY_SERVICE );
	                      
	    /*
	     * getDeviceId() function Returns the unique device ID.
	     * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.  
	     */                                                                
	    return telephonyManager.getDeviceId(); 
		
	    
	    /*
	     * getSubscriberId() function Returns the unique subscriber ID,
	   * for example, the IMSI for a GSM phone.
	   */
//	    imsistring = telephonyManager.getSubscriberId();  
	    
	}
    
    //======================================================================
    // set the data for the google analytics
//    public static void hitGADimension(Context ctx, String peopleID, String formID, String taskID, String syncAPI, String errorMsg){
//    	
//    	Preferences pref = new Preferences(ctx);
//    	Tracker tracker = EasyTracker.getInstance(ctx);  // The Google Analytics tracker.
//    	
//    	MapBuilder hitParameters = MapBuilder.createAppView()
//    	.set(Fields.customDimension(1), peopleID)
//    	.set(Fields.customDimension(2), pref.get(Constants.kloginStaffID))
//    	.set(Fields.customDimension(3), formID)
//    	.set(Fields.customDimension(4), getDeviceID(ctx))
//    	.set(Fields.customDimension(5), taskID)
//    	.set(Fields.customDimension(6), syncAPI)
//    	.set(Fields.customDimension(7), errorMsg);
//
//    	tracker.send(hitParameters.build());
//    	
//    }
    
    
    //======================================================================
    // Return the hashMap of the data in json object 
    
    public static HashMap<String, String> getMapFromJsonObject(JsonObject json)
    {
    
    	HashMap<String, String> result = new HashMap<String, String>();
    	
    	 for (Iterator<Entry<String, JsonElement>> it = json.entrySet().iterator(); it.hasNext();) {
 	        Map.Entry<String,JsonElement> entry = (Map.Entry<String, JsonElement>)it.next();
 	        //CelUtils.log(Constants.kApiTag, "Key == "+entry.getKey()+" Value == "+entry.getValue().getAsString());
 	        result.put(entry.getKey(), entry.getValue().getAsString());
 	    }
    	 
    	 return result;
    	 
    }
    
    //======================================================================
    // hide soft keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null)
        	inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    
    //======================================================================
    // to show softkeboard
    public static void SoftKeyBoard(Context c,boolean state){
		
    	try{
			if(!state){
				InputMethodManager imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(((Activity)c).getCurrentFocus()!=null)
					imm.hideSoftInputFromWindow(((Activity)c).getCurrentFocus().getWindowToken(), 0);
			}
    	}
    	catch(NullPointerException e){
    		e.printStackTrace();
    		
    	}
		
			
	}
    
    //======================================================================
 // to show softkeboard
    public static void SoftKeyBoard(Context c,View v,boolean state){
		try{
			if(!state){
				InputMethodManager imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			
	}
    
	//================================================================
 // to hide softkeboard
    public static void hideKeyboard(Activity activity){
    	
    	try{
	    	activity.getWindow().setSoftInputMode(
	    		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	}
    	catch(NullPointerException e){
    		e.printStackTrace();
    	}
    }
    
    //================================================================
    // General alert box
    public static void alert(Context c, int titleRes, String message){
    	
    	if(alert!=null && alert.isShowing()){
    		//======Do nothing
    	}
    	else{
    		
    		alert = new AlertDialog.Builder(c)
			.setIcon(R.drawable.ic_launcher)
			.setTitle(titleRes)
			.setMessage(message)   
			.create();
	
			alert.setCanceledOnTouchOutside(true);
			alert.show();
    	}	
    }
    
	//================================================================
	public static void alert(Context c, String message){
		alert(c, R.string.error, message);
	}
	
	//======================================================================
	// show general alert box on com.ogbonge thread
    public static void showAlertOnMainThread(final Context ctx,final int titleRes, final String text){
    	
    	((Activity)ctx).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				alert(ctx, titleRes, text);
			}
		});
    	
    }

	
    //======================================================================
    // check for N/w connectivity
    public static boolean isNetworkConnected(Context ctx) {
        
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) return false; else return true;
    }
    
    //================================================================
    // convert a string to Base64
    public static String decodeToBase64(String str)
    {
    	String retStr = null;
    	try {
    		retStr = new String(Base64.decode(str, Base64.DEFAULT));
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return (retStr == null)?"":retStr;
    }
    
    //================================================================
    // reinitialize the query string
    public static void resetStaffQuery(){
    	
    	staffQuery = null;
    	
    }
    
    
    public static String getPassedSec_min_hour_day(long t){
    	
    	
    	int i=timeDiff(StringToDate(Constants.kTimeStampFormat,getFormattedDateFromTimestamp(t)));
    	
    	return "";
    }
    
    
 public static int timeDiff(Date date){
    	
    	int days = 0;
    	int hours = 24;
    	int min = 60;
    	int sec = 60;
    	
    	long currMilliSec = Calendar.getInstance().getTimeInMillis();
    	long dateMilliSec = date.getTime();
    	
    	long diff = Math.abs(currMilliSec - dateMilliSec);
    	
    	
    	
    	days = (int)Math.abs(diff/(1000 * 60 * 60 * 24));
    	
    	
    	
    	Log.d("Difference == "+diff+"Days == "+days+" End Date == "+CelUtils.DateToString(Constants.kTimeStampFormat, date),"");
    	
    	return days;
    	
    }
    
    
    //================================================================
    // calculate the number of days form current date to date passed in the parameter 
    public static int dayDifference(Date date){
    	
    	int days = 0;
    	
    	long currMilliSec = Calendar.getInstance().getTimeInMillis();
    	long dateMilliSec = date.getTime();
    	
    	long diff = Math.abs(currMilliSec - dateMilliSec);
    	
    	
    	
    	days = (int)Math.abs(diff/(1000 * 60 * 60 * 24));
    	
    	Log.d("Difference == "+diff+"Days == "+days+" End Date == "+CelUtils.DateToString(Constants.kTimeStampFormat, date),"");
    	
    	return days;
    	
    }
    
  //================================================================
    // calculate the number of days remained form current date to date passed in the parameter 
    public static int dayRemaining(Date date){
    	
    	int days = 0;
    	days = dayDifference(date);
    	
    	if(date.getTime()< Calendar.getInstance().getTimeInMillis()){
    		days = - days;
    	}
    	
    	return days;
    	
    }
    
    //================================================================
    // return the priority over the task
    
//    public static int getPriority(int remainDays){
//    	
//    	int priority = Constants.kNormalPr;
//    	
//    	try{
//			
//			if(remainDays < 3){
//				priority = Constants.kHighPr;
//			}
//			else if(remainDays < 5){
//				priority = Constants.kMediumPr;
//			}
//			
//			Log.d("Remaining Days == "+remainDays);
//			
//    	}
//    	catch(Exception e){
//    		Log.d(Constants.kApiExpTag,e.getMessage()+"==== At Get Priority Module");
//    	}
//    	
//    	return priority; 
//    	
//    }
    
    //===============================================================
    // return the timestamp in milli seconds
    public static Long getmiliTimeStamp() {

		return Calendar.getInstance().getTimeInMillis();
	}
    
    //===============================================================
    
    //  return the timestamp in seconds
    public static Long getsecTimeStamp() {

		return (Calendar.getInstance().getTimeInMillis()/1000);
	}
    
    
    
    //================================================================
    // convert date into a string 
  	public static String DateToString(String format,Date date){
  		String temp="";
  		SimpleDateFormat dateFormat= new SimpleDateFormat(format, Locale.ENGLISH);
  		try{
  			temp= dateFormat.format(date);
  		}
  		catch (NullPointerException e) {
  			// TODO: handle exception
  		}
  		return temp;
  	}
  	
  	
  	//================================================================
  	 // convert string into a date 
  	public static Date StringToDate(String format,String date){
		Date d=null;
		SimpleDateFormat dateFormat= new SimpleDateFormat(format, Locale.ENGLISH);
		try {
			Log.d("PARSINGG===="+date.toString(),"");
			d= dateFormat.parse(date);
			//Log.d("Date === "+d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			// TODO: handle exception
		}
		Log.d("RETURINGGGG>>>>>>>"+d.getTime(),"");
		Log.d("REtunringgg======"+d.toString(),"");
		return d;
	}

  	//================================================================
  	 // convert date into a string with specified format passed in the parameters 
  	public static String convertDate(String fromFormat, String toFormat, String date){
  		
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
  	
  	//================================================================
  	// compare two dates
  	public static int compareDate(Date d1, Date d2){
  		
  		d1.setHours(0);
  		d1.setMinutes(0);
  		d1.setSeconds(0);
  		
  		d2.setHours(0);
  		d2.setMinutes(0);
  		d2.setSeconds(0);
  		
  		return d1.compareTo(d2);
  		
  	}
  	
  	
  	//================================================================
  	// shows the alert box before quiting
  	public static void showQuitAlert(final Context ctx)
  	{
  		
  		new AlertDialog.Builder(ctx).
		setCancelable(false)
		.setTitle( ctx.getString(R.string.exit_msg))
		.setInverseBackgroundForced(true)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				//endSession(ctx);
				quitApp();
				
				
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}

  	//================================================================
  	public static void quitApp()
  	{
  		android.os.Process.killProcess(android.os.Process.myPid());
  	}
  	
  	//================================================================
  	// return the current local
  	public static String getCurrentLocale(Context ctx)
  	{
  		return Locale.getDefault().getDisplayLanguage();
  		
  	}
  	
  	//================================================================
  	// shows a progress dialog
  	public static ProgressDialog getProgress(Context ctx){
  		
  		return ProgressDialog.show(ctx, "Loading","Please Wait....",true);
  		
  	}
  	
  //================================================================
  	// shows a custom loader
//  	public static CustomLoader getCustomProgress(Context ctx){
//  		
//  		CustomLoader loader = new CustomLoader(ctx, android.R.style.Theme_Translucent_NoTitleBar);
//  		loader.show();
//  		return loader;
//  		
//  	}
  	
    //================================================================
  	// return current timestamp
  	public static String getCurrTimeStamp(){
  		
  		return CelUtils.DateToString(Constants.kTimeStampFormat, Calendar.getInstance().getTime());
  	}
  	
    //================================================================
  	// upload a image
  	public static boolean uploadImage(Context ctx, String url, final byte[] imageData, String filename ,String message) throws Exception{

  	    String responseString = null;       

  	    PostMethod method;

  	    /*String auth_token = Preference.getAuthToken(ctx);*/


  	   /* method = new PostMethod("http://10.0.2.20/"+ "upload_image/" +Config.getApiVersion()
  	           + "/"     +auth_token); */

  	    method = new PostMethod(url);
  	            org.apache.commons.httpclient.HttpClient client = new              
  	                                        org.apache.commons.httpclient.HttpClient();
  	            client.getHttpConnectionManager().getParams().setConnectionTimeout(
  	                            100000);

  	            FilePart photo = new FilePart("icon", 
  	                                                  new ByteArrayPartSource( filename, imageData));

  	            photo.setContentType("image/png");
  	            photo.setCharSet(null);
  	            String s    =   new String(imageData);
  	           Part[] parts = {
  	                            new StringPart("message_text", message),
  	                            new StringPart("template_id","1"),
  	                            photo
  	                            };

  	            method.setRequestEntity(new 
  	                                          MultipartRequestEntity(parts,     method.getParams()));
  	            client.executeMethod(method);
  	            responseString = method.getResponseBodyAsString();
  	            method.releaseConnection();

  	            Log.e("httpPost", "Response status: " + responseString);

  	    if (responseString.equals("SUCCESS")) {
  	            return true;
  	    } else {
  	            return false;
  	    }
  	} 
  	
  
    //================================================================
// ends the session
  	public static void endSession(Context context){
  		
  		Preferences pref = new Preferences(context);
  		pref.removeKey(Constants.kloginStaffID);
  		pref.removeKey(Constants.kloginStaffGrade);
  		pref.removeKey(Constants.kUsrName);
  		pref.removeKey(Constants.KeyUUID);
  		pref.removeKey(Constants.kAuthT);
  		pref.commit();
  	
  		
  	}
  	
  	//================================================================
  	// return age from DOB
  	public static int getAgeFromDOB(String dob){
  		
  		int age =-1;
  		
  		try{
  		
  			if(dob.length()>0 && !dob.equals("0000-00-00")){
  				
  				Log.d("DATE FORMAT ISS==========="+dob,"");
	  			Date dobDate = CelUtils.StringToDate(Constants.kDOBFormat, dob);
	  			Log.d("DOB>>>>>  ",""+dobDate.getTime());
	  			
	  			Date currDate = Calendar.getInstance().getTime();
	  			
	  			
	  			Log.d("DOBSSSS=====   ","   "+currDate.toString());
	  			
	  		//	age = currDate.getYear() - dobDate.getYear();	
	  			
	  			
	  			Log.d("TIME DIFFFF======", ""+(currDate.getTime()-dobDate.getTime()));
	  			long totalyear=(((((currDate.getTime()-dobDate.getTime())/1000)/60)/60)/24);
	  			
	  			age=(int)(totalyear/365);
	  			
  			}	
  			
  		}
  		catch(Exception e){
  			Log.d(Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
  		}
  		
  		return age;
  		
  	}
  	//========================================================
  	// return month from DOB
public static int getMnthFromDOB(String dob){
  		
  		int month =-1;
  		
  		try{
  		
  			if(dob.length()>0 && !dob.equals("0000-00-00")){
  				
  				Log.d("DATE FORMAT ISS==========="+dob,"");
	  			Date dobDate = CelUtils.StringToDate(Constants.kDOBFormat, dob);
	  			Log.d("DOB>>>>>  ",""+dobDate.getTime());
	  			
	  			Date currDate = Calendar.getInstance().getTime();
	  			
	  			
	  			Log.d("DOBSSSS=====   ","   "+currDate.toString());
	  			
	  			//age = currDate.getYear() - dobDate.getYear();	
	  			
	  			long totaltime=(currDate.getTime()-dobDate.getTime());
	  		long totalday=((((totaltime/1000)/60)/60)/24);
	  			
	  		month=(int)((totalday%365)/30);
	  			Log.d("TIME DIFFFF======", ""+(currDate.getTime()-dobDate.getTime()));
	  			
  			}	
  			
  		}
  		catch(Exception e){
  			Log.d(Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
  		}
  		
  		return month;
  		
  	}
  	
//===================================================================

// return week from DOB
public static int getWeekFromDOB(String dob){
  		
  		int week =-1;
  		
  		try{
  		
  			if(dob.length()>0 && !dob.equals("0000-00-00")){
  				
  				Log.d("DATE FORMAT ISS==========="+dob,"");
	  			Date dobDate = CelUtils.StringToDate(Constants.kDOBFormat, dob);
	  			Log.d("DOB>>>>>  ",""+dobDate.getTime());
	  			
	  			Date currDate = Calendar.getInstance().getTime();
	  			
	  			
	  			Log.d("DOBSSSS=====   ","   "+currDate.toString());
	  			
	  			//age = currDate.getYear() - dobDate.getYear();	
	  			

	  			
	  			
	  			long totaltime=(currDate.getTime()-dobDate.getTime());
	  		long totalday=((((totaltime/1000)/60)/60)/24);
	  			
	  		week=(int)(((totalday%365)%30)/7);
	  			Log.d("TIME DIFFFF======", ""+(currDate.getTime()-dobDate.getTime()));
	  			
  			}	
  			
  		}
  		catch(Exception e){
  			Log.d(Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
  		}
  		
  		return week;
  		
  	}

//===============================================================================================

// return time in milliseconds from DOB
public static long gettimeFromDOB(String dob){
		
		long tt =-1;
		
		try{
		
			if(dob.length()>0 && !dob.equals("0000-00-00")){
				
				Log.d("DATE FORMAT ISS==========="+dob,"");
  			Date dobDate = CelUtils.StringToDate(Constants.kDOBFormat, dob);
  			Log.d("DOB>>>>>  ",""+dobDate.getTime());
  			tt=dobDate.getTime();
  			
			}	
			
		}
		catch(Exception e){
			Log.d(Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
		}
		
		return tt;
		
	}






  	//================================================================
// return age from DOB
  	public static String getAgeStrFromDOB(String dob){
  		
  		Log.d("DATE ISSS============="+dob,"");
  		int age = getAgeFromDOB(dob);
  		return (age<0)?"N/A":age+"";
  		
  	}
  	
 //return month string from dob 	
public static String getMonthStrFromDOB(String dob){
  		
  		Log.d("DATE ISSS============="+dob,"");
  		int age = getMnthFromDOB(dob);
  		return (age <0)?"N/A":age+"";
  		
  	}
  	
//return week string from dob 	
public static String getWeekStrFromDOB(String dob){
		
		Log.d("DATE ISSS============="+dob,"");
		int age = getWeekFromDOB(dob);
		return (age <0)?"N/A":age+"";
		
	}
  	//================================================================
//return age from date object 	
  	public static int getAgeFromDOB(Date dobDate){
  		
  		int age =0;
  		
  		try{
  		
  			Log.d("In Dateeee  "+dobDate,"");
  			if(dobDate != null){
  				
	  			Date currDate = Calendar.getInstance().getTime();
	  			
	  			//Log.d("Curr year === "+currDate.getYear()+" DOB Date == "+dobDate.getYear());
	  			age = currDate.getYear() - dobDate.getYear();
	  			//Log.d("Calculated Age == "+age);
	  			
  			}	
  			
  		}
  		catch(Exception e){
  			Log.d(Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
  		}
  		
  		return age;
  		
  	}
  	
  	//================================================================
  	// get DOB from years
  	public static Date getDOBFromAge(int year){
  		
  		Date date = Calendar.getInstance().getTime();
  		date.setYear(1900 + date.getYear() - year);
  		date.setMonth(date.getMonth()+1);
  		
  		
  		return date;
  		
  	}
  	
  //================================================================
  	// get DOB from year
  	public static String getDOBFromAgeStr(int year){
  		
  		Date date = Calendar.getInstance().getTime();
  		date.setYear(date.getYear() - year);
  		
  		return CelUtils.DateToString(Constants.kDOBFormat, date);
  		
  	}
  	
    //================================================================
  	// return the local
//  	public static Locale getLocale(Context ctx){
//  		
//  		Locale locale = null;
//  		Preferences pref = new Preferences(ctx);
//  		
//  		String country = pref.get(MyLocale.CountryLbl);
//  		String lang = pref.get(MyLocale.LocaleLbl);
//  		
//  		if(country.length()>0){
//  			locale = new Locale(lang, country);
//  		}
//  		
//  		return locale;
//  		
//  	}
  
    //================================================================
//  	public static String getLocaleColumn(String colName, Locale currLocale){
//  		
//  		try{
//  			
//	  		int firstIndex = colName.indexOf("_");
//	  		
//	  		String prefix = colName.substring(0, firstIndex);;
//	  		String suffix = colName.substring(firstIndex);
//	  		
//	  		//Log.d("Prefix === "+prefix+" Suffix == "+suffix);
//	  		
//	  		if(currLocale.getLanguage().equals(MyLocale.Locales.HINDI)){
//	  			prefix = "hindi";
//	  		}
//	  		
//	  		colName = prefix+suffix;
//	  		
//	  		//Log.d("Locale Column Name === "+colName);
//  		}
//  		catch(Exception e){
//  			
//  		}
//  		
//  		return colName;
//  		
//  	}
  	
  	
  //================================================================
  	// return the local column
//  	public static String getLocaleColumn(String colName){
//  		try{
//	  		int firstIndex = colName.indexOf("_");
//	  		String prefix = colName.substring(0, firstIndex);;
//	  		String suffix = colName.substring(firstIndex);
//	  		//Log.d("Prefix === "+prefix+" Suffix == "+suffix);
//	  		if(locale.getLanguage().equals(MyLocale.Locales.HINDI)){
//	  			prefix = "hindi";
//	  		}
//	  		
//	  		colName = prefix+suffix;
//	  		//Log.d("Locale Column Name === "+colName);
//  		}
//  		catch(Exception e){
//  			
//  		}
//  		return colName;
//  	}
//  	
  	//================================================================
  	// return the local value
  	public static String getLocaleValue(Cursor c, String defColName, String locColName){
  		String value = c.getString(c.getColumnIndex(locColName));
  		//Log.d("Locale Value ====>> "+value);
  		if(value == null || value.length() == 0){
  			value = c.getString(c.getColumnIndex(defColName));
  		}
  		
  		if(value == null)
  			value = "";
  			
  		return value;
  	}
  	
  	//================================================================
  	// et the data in the log
  	public static void logPartialData(File file, String data){
		
		try {
			
			data += "\n";
			OutputStream stream = new FileOutputStream(file, true);
			stream.write(data.getBytes());
			stream.flush();
			stream.close();
			
			//Log.d("ash_file_log",file + "Logged Successfully=========>>> ");

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Log.d(Constants.kApiTag,"JSON Request == "+postData);
	}
  	
  	
  //================================================================
  // set the data in the log from the file passed in the parameter
//  	public static void logData(Context ctx, String fileName, String data){
//		
//		try {
//			
//			File f = new File(Media.getAlbumDir(ctx, Media.LOG_DIR), fileName);
//			f.createNewFile(); 
//			
//			OutputStream stream = new FileOutputStream(f);
//			stream.write(data.getBytes());
//			stream.flush();
//			stream.close();
//			
//			Log.d("ash_file_log",fileName + "Logged Successfully=========>>> ");
//
//		} 
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//Log.d(Constants.kApiTag,"JSON Request == "+postData);
//	}
//  	
  	//================================================================
  	// return the local value
//  	
  	
  
  	//================================================================
  	// return first day of the week
  	public static Date getFirstDayOfWeek(){
  		
  		Calendar cal = Calendar.getInstance();

  		int dow = cal.get(Calendar.DAY_OF_WEEK);
		dow = (dow>1)?dow - 2:6;
		
		cal.add(Calendar.DAY_OF_WEEK, -dow);
		
		Date d = cal.getTime();
		
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		
		return d;
		
  	}
  	
  	//===================================================================================
  	// return the date & timestamp in the specified format from time in miliseconds
  	public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds)
  	{
  	    Date date = new Date(); 
  	    date.setTime(timestampInMilliSeconds);
  	    String formattedDate=new SimpleDateFormat("MMM d, yyyy").format(date);
  	    return formattedDate;

  	}
  	
  	
  	
	public static String getDateFromTimestamp(long timestampInMilliSeconds)
  	{
  	    Date date = new Date(); 
  	    date.setTime(timestampInMilliSeconds*1000);
  	    String formattedDate=new SimpleDateFormat(Constants.kTimeStampFormat).format(date);
  	    return formattedDate;

  	}
  	
  	
//============================================================
  	
  	
  	// return date from year, month and weeks
  	public static String getDatefromyearMonthweeks(String format, int years, int months, int weeks){
  		Log.d("values concidered "+years+"  "+months+"   "+weeks,""); 
  		Date date = new Date(); 
  		Calendar cal=Calendar.getInstance();
  		
  		if(years>0)
  			cal.add(Calendar.YEAR, -years);
  		
  		if(months>0)
  			cal.add(Calendar.MONTH, -months);
  		
  		if(weeks>0)
  			cal.add(Calendar.DAY_OF_YEAR, (-weeks*7));
  		
  		date=cal.getTime();
  		
  		String formattedDate=new SimpleDateFormat(format).format(date);
  		
  		
  


		return formattedDate;
  		
  		
  		
  		
  	}

  	
  	
  	
  	//================================================================
  	// return last day date of the week
  	public static Date getLastDayOfWeek(){
  		
  		Calendar cal = Calendar.getInstance();

  		int dow = cal.get(Calendar.DAY_OF_WEEK);
		dow = (dow>1)? 7 - dow:-1;
		
		cal.add(Calendar.DAY_OF_WEEK, dow);
		
		Date d = cal.getTime();
		
		d.setHours(23);
		d.setMinutes(59);
		d.setSeconds(59);

  		return d;
  		
  	}
  	
  
  //Enableing GPS 
  	
  	public static void turnGPSOn(Context ctx)
  	{
  	     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
  	     intent.putExtra("enabled", true);
  	     ctx.sendBroadcast(intent);

  	}
  	
  	//disable GPS
  	public static void turnGPSOff(Context ctx)
  	{
  		
  		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        ctx.sendBroadcast(intent);
  		
  		
  	}

  	public static void turnGPSDisable(Context ctx){
        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            ctx.sendBroadcast(poke);
        }
    }
  	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
  	
  	
  
  	
  
  	
  	

  
}
