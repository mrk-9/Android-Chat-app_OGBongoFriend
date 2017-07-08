package com.ogbongefriends.com.api;

/**Exception throws when error is returned from API
 * @author vivek *
 */
//
@SuppressWarnings("serial")
public class ApiException extends NullPointerException{

	//===
	public ApiException(String msg){ super(msg); }
	
	//===
	public ApiException(){ super(); }
}
