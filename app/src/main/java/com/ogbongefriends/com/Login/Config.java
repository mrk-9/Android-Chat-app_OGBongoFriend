package com.ogbongefriends.com.Login;

public class Config {

	public static String LINKEDIN_CONSUMER_KEY = "75e08t3y5spbdy";
	public static String LINKEDIN_CONSUMER_SECRET = "MaLMfp7vPvng8RqZ";
	public static String scopeParams = "rw_nus+r_basicprofile";
	
	public static String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static String OAUTH_CALLBACK_HOST = "callback";
	public static String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}
