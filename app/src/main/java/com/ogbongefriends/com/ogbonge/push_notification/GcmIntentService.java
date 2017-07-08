package com.ogbongefriends.com.ogbonge.push_notification;

//Copyright (c) 2012 The Board of Trustees of The University of Alabama
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer.
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//3. Neither the name of the University nor the names of the contributors
//may be used to endorse or promote products derived from this software
//without specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
//"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
//LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
//FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
//THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
//STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//OF THE POSSIBILITY OF SUCH DAMAGE.

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Log;
import com.ogbongefriends.com.common.NotificationType;
import com.ogbongefriends.com.common.Preferences;

public class GcmIntentService extends Service {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    public LocalBroadcastManager broadcaster;
    NotificationCompat.Builder builder;
    int count=0;
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
    SharedPreferences prefs;
    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";
	private Preferences pref;
   /* public GcmIntentService() {
        super("GcmIntentService");
    }*/

    /*@Override
    protected void onHandleIntent(Intent intent) {

	}*/

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 super.onStartCommand(intent, flags, startId);


			Bundle extras = intent.getExtras();
			Log.e("Notification","Notification Got it");
			broadcaster = LocalBroadcastManager.getInstance(this);
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
			// The getMessageType() intent parameter must be the intent you received
			// in your BroadcastReceiver.
			String messageType = gcm.getMessageType(intent);
			pref=new Preferences(this);
			if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
				if (GoogleCloudMessaging.
						MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
					sendNotification(0,"Send error: " + extras.toString());
				} else if (GoogleCloudMessaging.
						MESSAGE_TYPE_DELETED.equals(messageType)) {
					sendNotification(0,"Deleted messages on server: " +
							extras.toString());
					// If it's a regular GCM message, do some work.
				} else

				if (GoogleCloudMessaging.
						MESSAGE_TYPE_MESSAGE.equals(messageType)) {




					//*********************************
					//Trigger vibration here or whatever
					//*********************************
					// Get alert part of message

					Log.d("arvi", "arvi " + extras.toString());

					//{"type":"6","message":"Hi Arvi, Sachin Shrestha has liked you."}
					// When anyone sends me a 'propose' comment  	= 1
					// When a new Match is found  					= 2
					// When a new Message is received  				= 3
					// When a Backstage photo is rated  	 		= 4 working
					// When a Backstage photo is commented   		= 5 working
					//
					// When someone has fav'd you  					= 7 workingWhen someone has liked you  					= 6 working
					// When someone sends you a gift  				= 8  working
					// When event created within 300KM  			= 9 working
					// breaking_news_notification					=10

					String res=extras.getString("message");
					JsonParser p = new JsonParser();
					JsonElement jele = p.parse(res);
					JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject(): null;
					if(GcmBroadcastReceiver.sednData==false){
						GcmBroadcastReceiver.sednData=true;

						if(obj.get("type").getAsString().equalsIgnoreCase("10")){
							NotificationType.breaking_news_notification++;
							String url="http://www.ogbongefriends.com/userdata/news_media/"+obj.get("news_image").getAsString();
							sendNotificationForChat(10,obj.get("news_short_description").getAsString(),url,obj.get("news_title").getAsString());


						}

						if(obj.get("type").getAsString().equalsIgnoreCase("1")){
							sendNotification(1,obj.get("message").getAsString());
							NotificationType.send_me_propose_notification++;
						}

						if(obj.get("type").getAsString().equalsIgnoreCase("2")){
							sendNotification(2,obj.get("message").getAsString());
							NotificationType.new_match_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("3")){
							sendNotification(3,obj.get("message").getAsString());
							NotificationType.new_message_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("4")){
							sendNotification(4,obj.get("message").getAsString());
							NotificationType.backstage_photo_rated_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("5")){
							sendNotification(5,obj.get("message").getAsString());
							NotificationType.backstage_photo_commented_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("6")){
							sendNotification(6,obj.get("message").getAsString());
							NotificationType.someone_liked_you_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("7")){
							sendNotification(7,obj.get("message").getAsString());
							NotificationType.someone_favd_you_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("8")){
							sendNotification(8,obj.get("message").getAsString());
							NotificationType.someone_send_gift_notification++;
						}
						if(obj.get("type").getAsString().equalsIgnoreCase("9")){
							sendNotification(9,obj.get("message").getAsString());
							NotificationType.event_created_within300km_notification++;
						}


						else{
							try {
								ShortcutBadger.setBadge(getApplicationContext(), Integer.parseInt((pref.get(Constants.chat_notification_count)+1)));
							} catch (ShortcutBadgeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ShortcutBadger.with(getApplicationContext()).count(Integer.parseInt((pref.get(Constants.chat_notification_count)+1)));



            /*	if(obj.get("type").getAsString().equalsIgnoreCase("7")){
            		pref.set(Constants.who_fav_me_count, (pref.getInt(Constants.who_fav_me_count)+1)).commit();
            		 sendNotification(obj.get("message").getAsString());
            	}*/

							if(obj.get("type").getAsString().equalsIgnoreCase("3")){
								//{"uuid":"ob_55ca010978f44","id":"486","auth_token":"Ea9DgYZmEJO76SiBMwwmQ2xKL5RL7JJvYZGU37IiaxYyafSyxx","profile_pic":"krLNhU93ctVIq6zRDOdJ.jpg","status":1,"type":"3","message":"Hi Pappu, Arvind1 Yadav1 has sent you a message."}
								//	pref.set(Constants.chat_notification_count, (pref.getInt(Constants.chat_notification_count)+1)).commit();

								if(DrawerActivity.current_page!=4){
									prefs=getSharedPreferences(Constants.SOCIAL_MEDIA, MODE_PRIVATE);
									//	 prefs = PreferenceManager.getDefaultSharedPreferences(this);
									SharedPreferences.Editor editor1 = prefs.edit();
									editor1.putInt(Constants.chat_notification_count, (prefs.getInt(Constants.chat_notification_count, 0)+1)).commit();

									String url="http://www.ogbongefriends.com/userdata/image_gallery/"+obj.get("other_id").getAsString()+"/photos_of_you/"+obj.get("other_profile_pic").getAsString();
									sendNotificationForChat(3,obj.get("message").getAsString(),url,obj.get("other_user_name").getAsString());
								}
							}
						}
						String alertString = extras.toString();


						// Post notification of received message.

//           	if(isAppInForeground(this)){
//
//           	}
//           	else{
//
//           	}

					}


				}
			}
			// Release the wake lock provided by the WakefulBroadcastReceiver.
			GcmBroadcastReceiver.completeWakefulIntent(intent);

		return START_STICKY;
	}

	// This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(int type, String msg) {
    	
    	//if(DrawerActivity.current_page!=4){
        //Adds Default notification sound when you send the notification
        try {
        	Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
		}
		//Adds Vibrator service to the app.
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrates Device for 500 milliseconds
		v.vibrate(500);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent;
		switch (type){
			case 1:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 2:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 3:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 4:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 5:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 6:
				contentIntent = PendingIntent.getActivity(this, 9,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 7:
				contentIntent = PendingIntent.getActivity(this, 8,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 8:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			case 9:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;

			default:
				contentIntent = PendingIntent.getActivity(this, 0,
						new Intent(this, DrawerActivity.class), 0);
				break;
		}

        
        	//Bitmap bitmap = getBitmapFromURL("http://www.ogbongefriends.com/assets/js/timthumb.php?src=userdata/image_gallery/339/photos_of_you/COElKgIAtHNcvzW2oXGk.jpg");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.logo)
        .setContentTitle("OgbongeFriends")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
			mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        count++;
        android.util.Log.d("count", "count"+count);
    	//}
    }
    
   private void  sendNotificationForChat(int type,String msg,String url,String title){
    	
	   if(DrawerActivity.current_page!=4){
	        //Adds Default notification sound when you send the notification
	        try {
	        	Uri notification = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
						notification);
				r.play();
			} catch (Exception e) {
			}
			//Adds Vibrator service to the app.
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrates Device for 500 milliseconds
			v.vibrate(500);
	        mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);

	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, DrawerActivity.class), 0);

	        
	        	Bitmap bitmap = getBitmapFromURL(url);

		   RemoteViews expandedView = new RemoteViews(this.getPackageName(),
				   R.layout.notification_custom_remote);
		   expandedView.setTextViewText(R.id.text_view, msg);


	       /* NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setLargeIcon(bitmap).setSmallIcon(R.drawable.logo)
	        .setContentTitle(title)
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);
		   mBuilder.setAutoCancel(true);*/


		   Notification notification = new NotificationCompat.Builder(this)
				   .setContentIntent(contentIntent)
				   .setSmallIcon(R.drawable.ic_launcher).build();

		   notification.contentView = new RemoteViews(this.getPackageName(),
				   R.layout.notification_custom_remote);//set your custom layout

		   notification.contentView.setTextViewText(R.id.text_view,msg);//control custom layout views ex.. progressbar textview .
		   notification.contentView.setImageViewBitmap(R.id.img,bitmap);
		   mNotificationManager.notify(NOTIFICATION_ID, notification);
	        count++;
	        android.util.Log.d("count", "count"+count);
	    	}
    	
    }
    
    
    
    public static boolean isAppInForeground(Context context) {
    	  List<RunningTaskInfo> task =
    	      ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
    	          .getRunningTasks(1);
    	  if (task.isEmpty()) {
    	    return false;
    	  }
    	  return task
    	      .get(0)
    	      .topActivity
    	      .getPackageName()
    	      .equalsIgnoreCase(context.getPackageName());
    	}
    
    
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
