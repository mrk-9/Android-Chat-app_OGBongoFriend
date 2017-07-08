package com.ogbongefriends.com.ogbonge.setting;

import java.io.File;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Preferences;

public class setting_web_pages extends android.app.Fragment{
	private View rootView;
	Preferences pref;
	WebView webView;
	private ProgressBar progressBar1;
	private Context _ctx;
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}
		
	public setting_web_pages(Context ctx)
	{
		_ctx=ctx;
	}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			pref=new Preferences(getActivity());
			  Constants.isWebLoading=true;
			Bundle bundle = getActivity().getIntent().getExtras();
			if(rootView==null){
				rootView = inflater.inflate(R.layout.webview, container,
						false);
				progressBar1=(ProgressBar) rootView.findViewById(R.id.progressBar1);


				String url = "http://www.ogbongefriends.com/webview/footerPage/"+ pref.get(Constants.SettingType);
				webView = (WebView) rootView.findViewById(R.id.webView1);

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
				//webView.getSettings().setPluginsEnabled(true);
				progressBar1.setVisibility(View.VISIBLE);
				webView.loadUrl(url);

			}
			
			return rootView;
		}

		public void hideSoftKeyboard(View v) {
	        Activity activity = (Activity) v.getContext();
	        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
//	                if(newProgress == 100)
//	                	progressBar1.setVisibility(View.GONE);
	              }
			
			           
	        
			
			}
		
		private class MyWebViewClient extends WebViewClient{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//return super.shouldOverrideUrlLoading(view, url);
				 boolean shouldOverride = false;
		            // We only want to handle requests for mp3 files, everything else the webview
		            // can handle normally
		            if (url.endsWith(".mp3")||url.endsWith(".png")||url.endsWith(".jpg")||url.endsWith(".pdf")) {
		                shouldOverride = true;
		                Uri source = Uri.parse(url);

		                // Make a new request pointing to the mp3 url
		                DownloadManager.Request request = new DownloadManager.Request(source);
		                // Use the same file name for the destination
		                File destinationFile = new File (Environment.getExternalStorageDirectory(), source.getLastPathSegment());
		                request.setDestinationUri(Uri.fromFile(destinationFile));
		                // Add it to the manager
		               // manager.enqueue(request);
		            }
		            
		            else{
		            	view.loadUrl(url);
		            }
		            return shouldOverride;
				
				
				
				
				
		        //  return true;
			}
			
			 @Override
			    public void onPageFinished(WebView view, String url) {
			        // TODO Auto-generated method stub
			        super.onPageFinished(view, url);
			        Constants.isWebLoading=false;
			        progressBar1.setVisibility(View.GONE);
			    }
		}
		
	}

