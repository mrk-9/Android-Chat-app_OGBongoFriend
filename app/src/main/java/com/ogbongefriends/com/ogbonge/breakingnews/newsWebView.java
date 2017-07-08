package com.ogbongefriends.com.ogbonge.breakingnews;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Preferences;

public class newsWebView extends android.app.Fragment implements View.OnKeyListener{
	private View rootView;
	Preferences pref;
	WebView webView;
	private Button sharebutton;
	private ProgressBar progressBar1;
	private Context _ctx;
	private String imageUrl="";
	private Bitmap theBitmap=null;
	File f;
	private String url="";
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}
	public newsWebView(){

	}
	public newsWebView(Context ctx)
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
				sharebutton=(Button)rootView.findViewById(R.id.button);
				progressBar1=(ProgressBar) rootView.findViewById(R.id.progressBar1);
				sharebutton.setVisibility(View.GONE);
			/*	if(pref.get("newurl").length()>0){
				url=pref.get("newurl");
					pref.set("newurl","").commit();
				}
				else{*/

					imageUrl=pref.get(Constants.NewsImageUrl);
					url	= "http://www.ogbongefriends.com/breakingNews/newsWebview/"+ pref.get(Constants.NewsId) +"/"+ pref.get(Constants.KeyUUID);
				//}

if(imageUrl.length()>0) {
	new AsyncTask<Void, Void, Void>() {
		@Override
		protected Void doInBackground(Void... params) {
			Looper.prepare();
			try {
				theBitmap = Glide.
						with(getActivity()).
						load(imageUrl).
						asBitmap().
						into(100, 100). // Width and height
						get();
			} catch (final ExecutionException e) {
				e.printStackTrace();

			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void dummy) {
			if (theBitmap != null) {
				createFile(theBitmap);
			}
		}
	}.execute();

}


				sharebutton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
/*if(imageUrl.length()>0){
	String shareBody = "You are invited on Ogbonge friends, Favourite Hangout for Naija "+url;
	Intent intent=new Intent();
	intent.setAction(Intent.ACTION_SEND);
	intent.setType("image*//*");//Set MIME Type
	intent.putExtra(Intent.EXTRA_TEXT, shareBody);
	intent.putExtra(Intent.EXTRA_SUBJECT, "Post Image");
	intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));// Pur Image to intent
	startActivity(Intent.createChooser(intent, "Share via"));
}*/
			//			else{
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						String shareBody = "You are invited on Ogbonge friends, Favourite Hangout for Naija. "+url;
						sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "OgbongeFriend");
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

						startActivity(Intent.createChooser(sharingIntent, "Share via"));
						}






			//		}
				});

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
					
					progressBar1.setVisibility(View.VISIBLE);
				webView.loadUrl(url);

				webView.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						switch (keyCode) {
							case KeyEvent.KEYCODE_BACK:
								if (webView.canGoBack()) {
									webView.goBack();
									return true;
								}
								break;
						}
						return false;
					}
				});


			}



			
			return rootView;
		}


	public class asyn extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		}
	}

public void createFile(Bitmap btm){

	 f = new File(Environment.getExternalStorageDirectory(),
			 "testimage.jpg");
	try {
		f.createNewFile();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		btm.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
		byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(bitmapdata);
		fos.flush();
		fos.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	progressBar1.setVisibility(View.GONE);

}


		public void hideSoftKeyboard(View v) {
	        Activity activity = (Activity) v.getContext();
	        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    }

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			WebView webView = (WebView) v;


		}
		return false;
	}


	private class MyWebChromeClient extends WebChromeClient {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {			
				//this.setValue(newProgress);
				super.onProgressChanged(view, newProgress);
				//ChatWebView.this.setTitle("Loading...");
				if(getActivity()!=null) {
					getActivity().setProgress(newProgress * 100); //Make the bar disappear after URL is loaded
				}
	             // Return the app name after finish loading
//	                if(newProgress == 100)
//	                	progressBar1.setVisibility(View.GONE);
	              }
			
			           
	        
			
			}
		
		private class MyWebViewClient extends WebViewClient {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String _url) {
				// TODO Auto-generated method stub
				//return super.shouldOverrideUrlLoading(view, url);
				 boolean shouldOverride = false;
				imageUrl="";
		            // We only want to handle requests for mp3 files, everything else the webview
		            // can handle normally
		            if (_url.endsWith(".mp3")||_url.endsWith(".png")||_url.endsWith(".jpg")||_url.endsWith(".pdf")) {
		                shouldOverride = true;
		                Uri source = Uri.parse(_url);

		                // Make a new request pointing to the mp3 url
		                DownloadManager.Request request = new DownloadManager.Request(source);
		                // Use the same file name for the destination
		                File destinationFile = new File (Environment.getExternalStorageDirectory(), source.getLastPathSegment());
		                request.setDestinationUri(Uri.fromFile(destinationFile));
		                // Add it to the manager
		               // manager.enqueue(request);
		            }
		            
		            else{
						view.setWebChromeClient(new MyWebChromeClient());
						view.setWebViewClient(new MyWebViewClient());
						view.getSettings().setJavaScriptEnabled(true);
						progressBar1.setVisibility(View.VISIBLE);
					String ur=	_url.replace("newsDetails","newsWebview");

						view.loadUrl(ur);

						url=ur;

						/*pref.set("newurl",url).commit();
						((DrawerActivity) getActivity()).displayView(33);*/
		            }
		            return shouldOverride;

		        //  return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d("111111111111111111111","111111111111111111111");
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				Log.d("2222222222222222", "2222222222222222222222");
			}

			@Override
			    public void onPageFinished(WebView view, String url) {
			        // TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Log.d("33333333333333333", "3333333333333333333333");
			        Constants.isWebLoading=false;
			    //    progressBar1.setVisibility(View.GONE);
				sharebutton.setVisibility(View.VISIBLE);
			    }
		}
		
	}
