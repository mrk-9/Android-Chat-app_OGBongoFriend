package com.ogbongefriends.com.ogbonge.push_notification;

public interface Config {
	 
    
    // CONSTANTS
    static final String YOUR_SERVER_URL =  
                          "YOUR_SERVER_URL/gcm_server_files/register.php";
     
    // Google project id
//    static final String GOOGLE_SENDER_ID = "727443607459";

   // static final String GOOGLE_SENDER_ID = "397382936961";


    static final String GOOGLE_SENDER_ID = "1044554191695";

    // Server key AIzaSyBJO8y1OI4a7D6TV97nAHnYw7n4jWISV-M  for 397382936961

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
         
     
}
