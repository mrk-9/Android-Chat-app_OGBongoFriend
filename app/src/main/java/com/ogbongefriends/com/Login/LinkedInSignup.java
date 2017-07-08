package com.ogbongefriends.com.Login;

import java.util.EnumSet;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
import com.ogbongefriends.com.R;

import com.ogbongefriends.com.common.CustomLoader;


public class LinkedInSignup extends Activity {

	CustomLoader p;
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
                    Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(Config.LINKEDIN_CONSUMER_KEY,
					Config.LINKEDIN_CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.linkedin_signup);

		p=LoginActivity.p;
		
		linkedInLogin();
	}

	
	private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(LinkedInSignup.this);

		LinkedinDialog d = new LinkedinDialog(LinkedInSignup.this,progressDialog);
		d.setCancelable(false);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new LinkedinDialog.OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);

					accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					client = factory.createLinkedInApiClient(accessToken);
					// client.postNetworkUpdate("Testing by Mukesh!!! LinkedIn wall post from Android app");
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getTokenSecret());
					Person p = client.getProfileForCurrentUser(EnumSet.of(
			                ProfileField.ID, ProfileField.FIRST_NAME,
			                ProfileField.LAST_NAME, ProfileField.HEADLINE,
			                ProfileField.INDUSTRY, ProfileField.PICTURE_URL,
			                ProfileField.DATE_OF_BIRTH, ProfileField.LOCATION_NAME,
			                ProfileField.MAIN_ADDRESS, ProfileField.LOCATION_COUNTRY));
					
					//================================
					
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("f_name", p.getFirstName());
					hashMap.put("l_name", p.getLastName());
					hashMap.put("id", p.getId());
					hashMap.put("gender", "");
					hashMap.put("email", "");
					
					
					hashMap.put("main_address", p.getMainAddress());
					hashMap.put("location", p.getLocation()+"");
					hashMap.put("phone_no",  p.getPhoneNumbers()+"");
					hashMap.put("DOB", p.getDateOfBirth()+"");
					
					
					
					
					
					
					
					try {
						hashMap.put("birthday", p.getDateOfBirth().toString());	
					} catch (Exception e) {

						Log.e("catch", "Exception e ");
					}
					
				
					
					Intent returnIntent = new Intent();
					returnIntent.putExtra("map", hashMap);
					setResult(RESULT_OK,returnIntent);
					finish();
					
					
					//================================
					Log.v("Data From LinkedIn",hashMap.toString());

				} catch (Exception e) {
					Log.i("LinkedinSample", "error to get verifier");
					e.printStackTrace();
				}
			}
		});
		
		d.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				try {
					if(p.isShowing()){
						p.cancel();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				finish();
				
			
			}
		});
	

		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
	
	
}
