/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ogbongefriends.com.ogbonge.invite;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;

import com.ogbongefriends.com.ogbonge.push_notification.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

// We use a custom Application class to store our minimal state data (which users have been selected).
// A real-world application will likely require a more robust data model.
public class FriendPickerApplication extends MultiDexApplication {
    private List<GraphUser> selectedUsers;
    
    private  final int MAX_ATTEMPTS = 5;
    private  final int BACKOFF_MILLI_SECONDS = 2000;
    private  final Random random = new Random();
     
    
    

    public List<GraphUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<GraphUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
    
    
    

    // Register this account with the server.
   public void register(final Context context, String name, String email, final String regId) {
         
       Log.i(Config.TAG, "registering device (regId = " + regId + ")");
        
       String serverUrl = Config.YOUR_SERVER_URL;
        
       Map<String, String> params = new HashMap<String, String>();
       params.put("regId", regId);
       params.put("name", name);
       params.put("email", email);
        
       long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        
       // Once GCM returns a registration id, we need to register on our server
       // As the server might be down, we will retry it a couple
       // times.
       for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            
           Log.d(Config.TAG, "Attempt #" + i + " to register");
            
           try {
               //Send Broadcast to Show message on screen
               displayMessageOnScreen(context,
                       "Server Registering");
                
               // Post registration values to web server
               post(serverUrl, params);
                
               GCMRegistrar.setRegisteredOnServer(context, true);
                
               //Send Broadcast to Show message on screen
               String message = "Server Registered";
               displayMessageOnScreen(context, message);
                
               return;
           } catch (IOException e) {
                
               // Here we are simplifying and retrying on any error; in a real
               // application, it should retry only on unrecoverable errors
               // (like HTTP error code 503).
                
               Log.e(Config.TAG, "Failed to register on attempt " + i + ":" + e);
                
               if (i == MAX_ATTEMPTS) {
                   break;
               }
               try {
                    
                   Log.d(Config.TAG, "Sleeping for " + backoff + " ms before retry");
                   Thread.sleep(backoff);
                    
               } catch (InterruptedException e1) {
                   // Activity finished before we complete - exit.
                   Log.d(Config.TAG, "Thread interrupted: abort remaining retries!");
                   Thread.currentThread().interrupt();
                   return;
               }
                
               // increase backoff exponentially
               backoff *= 2;
           }
       }
        
       String message = "Server Registering Error";
        
       //Send Broadcast to Show message on screen
       displayMessageOnScreen(context, message);
   }

    // Unregister this account/device pair within the server.
    public void unregister(final Context context, final String regId) {
         
       Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");
        
       String serverUrl = Config.YOUR_SERVER_URL + "/unregister";
       Map<String, String> params = new HashMap<String, String>();
       params.put("regId", regId);
        
       try {
           post(serverUrl, params);
           GCMRegistrar.setRegisteredOnServer(context, false);
           String message = "Server Unregistered";
           displayMessageOnScreen(context, message);
       } catch (IOException e) {
            
           // At this point the device is unregistered from GCM, but still
           // registered in the our server.
           // We could try to unregister again, but it is not necessary:
           // if the server tries to send a message to the device, it will get
           // a "NotRegistered" error message and should unregister the device.
            
           String message = "Error";
           displayMessageOnScreen(context, message);
       }
   }

   // Issue a POST request to the server.
   private static void post(String endpoint, Map<String, String> params)
           throws IOException {    
        
       URL url;
       try {
            
           url = new URL(endpoint);
            
       } catch (MalformedURLException e) {
           throw new IllegalArgumentException("invalid url: " + endpoint);
       }
        
       StringBuilder bodyBuilder = new StringBuilder();
       Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        
       // constructs the POST body using the parameters
       while (iterator.hasNext()) {
           Entry<String, String> param = iterator.next();
           bodyBuilder.append(param.getKey()).append('=')
                   .append(param.getValue());
           if (iterator.hasNext()) {
               bodyBuilder.append('&');
           }
       }
        
       String body = bodyBuilder.toString();
        
       Log.v(Config.TAG, "Posting '" + body + "' to " + url);
        
       byte[] bytes = body.getBytes();
        
       HttpURLConnection conn = null;
       try {
            
           Log.e("URL", "> " + url);
            
           conn = (HttpURLConnection) url.openConnection();
           conn.setDoOutput(true);
           conn.setUseCaches(false);
           conn.setFixedLengthStreamingMode(bytes.length);
           conn.setRequestMethod("POST");
           conn.setRequestProperty("Content-Type",
                   "application/x-www-form-urlencoded;charset=UTF-8");
           // post the request
           OutputStream out = conn.getOutputStream();
           out.write(bytes);
           out.close();
            
           // handle the response
           int status = conn.getResponseCode();
            
           // If response is not success
           if (status != 200) {
                
             throw new IOException("Post failed with error code " + status);
           }
       } finally {
           if (conn != null) {
               conn.disconnect();
           }
       }
     }
    
    
    
   // Checking for all possible internet providers
   public boolean isConnectingToInternet(){
        
       ConnectivityManager connectivity = 
                            (ConnectivityManager) getSystemService(
                             Context.CONNECTIVITY_SERVICE);
         if (connectivity != null)
         {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null)
                 for (int i = 0; i < info.length; i++)
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }
 
         }
         return false;
   }
    
  // Notifies UI to display a message.
  public void displayMessageOnScreen(Context context, String message) {
         
       Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
       intent.putExtra(Config.EXTRA_MESSAGE, message);
        


       // Send Broadcast to Broadcast receiver with message
       context.sendBroadcast(intent);
        
   }
    
    
  //Function to display simple Alert Dialog
  public void showAlertDialog(Context context, String title, String message,
           Boolean status) {
       AlertDialog alertDialog = new AlertDialog.Builder(context).create();

       // Set Dialog Title
       alertDialog.setTitle(title);

       // Set Dialog Message
       alertDialog.setMessage(message);

       if(status != null)
           // Set alert dialog icon
           alertDialog.setIcon((status) ? R.drawable.btn_plus : R.drawable.btn_minus);

       // Set OK Button
       alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
                
           }
       });

       // Show Alert Message
       alertDialog.show();
   }
    
   private PowerManager.WakeLock wakeLock;
    
   public  void acquireWakeLock(Context context) {
       if (wakeLock != null) wakeLock.release();

       PowerManager pm = (PowerManager) 
                         context.getSystemService(Context.POWER_SERVICE);
        
       wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
               PowerManager.ACQUIRE_CAUSES_WAKEUP |
               PowerManager.ON_AFTER_RELEASE, "WakeLock");
        
       wakeLock.acquire();
   }

   public  void releaseWakeLock() {
       if (wakeLock != null) wakeLock.release(); wakeLock = null;
   }
}
