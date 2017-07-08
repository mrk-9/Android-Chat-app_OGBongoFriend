package com.ogbongefriends.com.common;

/**
 * Created by hp-hp on 10-02-2016.
 */

/*
When anyone sends me a 'propose' comment  	= 1
// When a new Match is found  					= 2
// When a new Message is received  				= 3
// When a Backstage photo is rated  	 		= 4
// When a Backstage photo is commented   		= 5
// When someone has liked you  					= 6
// When someone has fav'd you  					= 7
// When someone sends you a gift  				= 8
// When event created within 300KM  			= 9*/



public class NotificationType {
    public static int send_me_propose_notification=0;//1
    public static int new_match_notification=0;//2
    public static int new_message_notification=0;//3
    public static int backstage_photo_rated_notification=0;//4
    public static int backstage_photo_commented_notification=0;//5
    public static int someone_liked_you_notification=0;//6
    public static int someone_favd_you_notification=0;//7
    public static int someone_send_gift_notification=0;//8
    public static int event_created_within300km_notification=0;//9
    public static int breaking_news_notification=0;//10



    public static String UserPoints="";
    public static String server_id="";
    public static String pass_7days_expiry_time="";
    public static String name="";
    public static String date_of_birth="";
    public static String profile_pic="";



}


