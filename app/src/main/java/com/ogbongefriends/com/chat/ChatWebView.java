package com.ogbongefriends.com.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.androidrecording.audio.AudioRecordingHandler;
import com.ogbongefriends.com.androidrecording.audio.AudioRecordingThread;
import com.ogbongefriends.com.androidrecording.visualizer.VisualizerView;
import com.ogbongefriends.com.androidrecording.visualizer.renderer.BarGraphRenderer;


public class ChatWebView extends android.app.Fragment{
	private WebView webView;

	DownloadManager manager;

	private ProgressBar progressBar1;
private int audiorecording=0,videoRecording=0;
	private ValueCallback<Uri> mUploadMessage;  
	private Context _ctx;
	private View rootView;
	Preferences pref;
	 private Dialog dialog;
	 private static String fileName = null;
	    private int cnt=0;
	    private boolean show_attachment=false;
		private Button recordBtn, playBtn;
		private VisualizerView visualizerView;
		
		private AudioRecordingThread recordingThread;
		private boolean startRecording = true;
		private Uri mCapturedImageURI;
		public final int SELECT_FILE=100;
		public final int REQUEST_CAMERA=101;
	String str="";
	CountDownTimer t;
	private TextView timer;
	private Uri fileUri;
	private final static int FILECHOOSER_RESULTCODE=1;
	private final static int AUDIORECORDER=109;
	private static final String IMAGE_DIRECTORY_NAME = "Ogbonge";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}
		
		public ChatWebView()
		{
		}
		
	public ChatWebView(Context ctx)
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
			if(rootView==null){
				Constants.isWebLoading=true;
				rootView = inflater.inflate(R.layout.webview, container,
						false);
				
				
				pref=new Preferences(getActivity());

				if(pref.get("chat_other").length()>2){
				str="http://www.ogbongefriends.com/chat/chatting/1/"+ pref.get(Constants.KeyUUID)+"/"+pref.get("chat_other");
				DrawerActivity.chat_attachment.setVisibility(View.VISIBLE);
            	DrawerActivity.popBtn.setVisibility(View.GONE);
            	show_attachment=true;
					}

				else{
					DrawerActivity.chat_attachment.setVisibility(View.GONE);
	            	DrawerActivity.popBtn.setVisibility(View.VISIBLE);
				str="http://www.ogbongefriends.com/chat/chatting/0/"+ pref.get(Constants.KeyUUID);

				}




				//String str="http://ogbongefriends.com/chat/chatting/"+ bundle.getString("uuid");

				progressBar1=(ProgressBar)rootView.findViewById(R.id.progressBar1);

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
				
				t = new CountDownTimer( Long.MAX_VALUE , 1000) {

			        @Override
			        public void onTick(long millisUntilFinished) {

			            cnt++;
			            String time = new Integer(cnt).toString();

			            long millis = cnt;
			               int seconds = (int) (millis / 60);
			               int minutes = seconds / 60;
			               seconds     = seconds % 60;

			               timer.setText(String.format("%d:%02d:%02d", minutes, seconds,millis%60));

			        }

			        @Override
			        public void onFinish() {            
			        	
			        }
			    };
				


				webView.getSettings().setJavaScriptEnabled(true);

				//webView.getSettings().setPluginState(WebSettings.PluginState.ON);

				progressBar1.setVisibility(View.VISIBLE);

				webView.loadUrl(str);

				//hideSoftKeyboard(webView);
				
				DrawerActivity.audio_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						// TODO Auto-generated method stub
						dialog = new Dialog(_ctx);
						DrawerActivity.attachment_menu.setVisibility(View.GONE);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.audio_rec);
						 timer=(TextView)dialog.findViewById(R.id.timer);
						if (!StorageUtils.checkExternalStorageAvailable()) {
							NotificationUtils.showInfoDialog(_ctx, getString(R.string.noExtStorageAvailable));
							return;
						}
						fileName = StorageUtils.getFileName(true);
						
						recordBtn = (Button) dialog.findViewById(R.id.recordBtn);
						recordBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								record();
							}
						});
						
						playBtn = (Button) dialog.findViewById(R.id.playBtn);
						playBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								play();
							}
						});
						
						visualizerView = (VisualizerView) dialog.findViewById(R.id.visualizerView);
						setupVisualizer();
						
						
						DrawerActivity.attachment_menu.setVisibility(View.GONE);
						
						dialog.show();
					}
				});
			
				
				
				DrawerActivity.audio_rec.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						DrawerActivity.attachment_menu.setVisibility(View.VISIBLE);
						//DrawerActivity.attachment_menu.setVisibility(View.GONE);

					}
				});
				
				
				DrawerActivity.photo_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						DrawerActivity.attachment_menu.setVisibility(View.GONE);
//						startCamera();
						
						DrawerActivity.attachment_menu.setVisibility(View.GONE);
						
						Intent i = new Intent(_ctx, newClass.class);
						startActivity(i);
						
					}
				});
				
	DrawerActivity.gallery_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						DrawerActivity.attachment_menu.setVisibility(View.GONE);
						
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						intent.setType("image/*");
						startActivityForResult(
								Intent.createChooser(intent, "Select File"),
								SELECT_FILE);
						
						
						
					}
				});
				
				
				
				
				webView.setOnKeyListener(new OnKeyListener()
				{
				    @Override
				    public boolean onKey(View v, int keyCode, KeyEvent event)
				    {
				        if(event.getAction() == KeyEvent.ACTION_DOWN)
				        {
				            WebView webView = (WebView) v;

				            switch(keyCode)
				            {
				                case KeyEvent.KEYCODE_BACK:
				                    if(webView.canGoBack())
				                    {
				                        webView.goBack();
				                        return true;
				                    }
				                    break;
				            }
				        }

				        return false;
				    }
				});
				
				
				
				DrawerActivity.video_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DrawerActivity.attachment_menu.setVisibility(View.GONE);
						
						Intent i = new Intent(_ctx, VideoRecordingActivity.class);
						startActivity(i);
					//recordVideo();
					}
				});
				
			}
			
			return rootView;
		}
		
		
		private void recordVideo() {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

			fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

			// set video quality
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
																// name

			// start the video capture Intent
			startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
		}
		
		
		public Uri getOutputMediaFileUri(int type) {
			return Uri.fromFile(getOutputMediaFile(type));
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			if(show_attachment){
			//DrawerActivity.chat_attachment.setVisibility(View.GONE);
        	//DrawerActivity.popBtn.setVisibility(View.VISIBLE);
			}
			super.onResume();
		}

		public void startCamera() {

			
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			mCapturedImageURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			ChatWebView.this.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			
			
			
//			ContentValues values = new ContentValues();
//			values.put(MediaStore.Images.Media.TITLE, "temp.png");
//			 mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//			 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				startActivityForResult(intent, REQUEST_CAMERA);
				
				
				
		
		}
		
		public void startSpeechRecognition() {
			   // Fire an intent to start the speech recognition activity.
			   Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			   // secret parameters that when added provide audio url in the result
			   intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
			   intent.putExtra("android.speech.extra.GET_AUDIO", true);
			   startActivityForResult(intent, AUDIORECORDER);
			   
//			   startActivity(intent, "<some code you choose>");
			}
		private static File getOutputMediaFile(int type) {

			// External sdcard location
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					IMAGE_DIRECTORY_NAME);

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
							+ IMAGE_DIRECTORY_NAME + " directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.getDefault()).format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "IMG_" + timeStamp + ".jpg");
			} else if (type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "VID_" + timeStamp + ".mp4");
			} else {
				return null;
			}

			return mediaFile;
		}

		private void setupVisualizer() {
			Paint paint = new Paint();
	        paint.setStrokeWidth(5f);
	        paint.setAntiAlias(true);
	        paint.setColor(Color.argb(200, 227, 69, 53));
	        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(2, paint, false);
	        visualizerView.addRenderer(barGraphRendererBottom);
		}
		
		private void releaseVisualizer() {
			visualizerView.release();
			visualizerView = null;
		}
		
		private void record() {
	        if (startRecording) {
	        	recordStart();
	        }
	        else {
	        	recordStop();
	        }
		}
		
		private void recordStart() {
			t.start();
			cnt=0;
			startRecording();
	    	startRecording = false;
	    	recordBtn.setText(R.string.stopRecordBtn);
	    	playBtn.setEnabled(false);
		}
		
		private void recordStop() {
			t.cancel();
			stopRecording();
			startRecording = true;
	    	recordBtn.setText(R.string.recordBtn);
	    	playBtn.setEnabled(true);
		}
		
		private void startRecording() {
		    recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() {
				@Override
				public void onFftDataCapture(final byte[] bytes) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							if (visualizerView != null) {
								visualizerView.updateVisualizerFFT(bytes);
							}
						}
					});
				}

				@Override
				public void onRecordSuccess() {}

				@Override
				public void onRecordingError() {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							recordStop();
							NotificationUtils.showInfoDialog(_ctx, getString(R.string.recordingError));
						}
					});
				}

				@Override
				public void onRecordSaveError() {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							recordStop();
							NotificationUtils.showInfoDialog(_ctx, getString(R.string.saveRecordError));
						}
					});
				}
			});
		    recordingThread.start();
	    }
	    
	    private void stopRecording() {
	    	if (recordingThread != null) {
	    		recordingThread.stopRecording();
	    		recordingThread = null;
		    }
	    }

	    private void play() {
	    	
	    	manageVideo(fileName);
	    	
//			Intent i = new Intent(_ctx, AudioPlaybackActivity.class);
//			i.putExtra("arg_filename", fileName);
//			startActivityForResult(i, 0);
		}
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			if(audiorecording==1){
			recordStop();
			releaseVisualizer();
			pref.set("chat_other", "").commit();
			}
		}

		@Override
		public void onPause() {
			super.onPause();
			// TODO Auto-generated method stub
//			if(audiorecording==1){
//			super.onPause();
//			recordStop();
//			}
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,  
                Intent intent) {  

			super.onActivityResult(requestCode, resultCode, intent);
			
			if(requestCode==FILECHOOSER_RESULTCODE)  

{  

if (null == mUploadMessage) return;  

Uri result = intent == null || resultCode != Activity.RESULT_OK ? null  

: intent.getData();  

mUploadMessage.onReceiveValue(result);  

mUploadMessage = null;  

}


if (requestCode == SELECT_FILE) {
	try{
	Uri selectedImageUri = intent.getData();
//	p.show();
 	//tempPath = CelUtils.getpath(selectedImageUri, _ctx);
 	 manageImage(CelUtils.getpath(selectedImageUri, _ctx));
 	
//	Log.d("Image Path Select", ""+tempPath);
//	selectedPhoto.setText(tempPath);
//	String[] Img_char=tempPath.split("/");
//	//imageName=Img_char[Img_char.length-1];
//	 String[] Img_size = { MediaStore.Images.Media.SIZE};
//	 fileSelected=1;
//	Bitmap bm;
//	BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//	bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//	prof.setImageBitmap(bm);
	}
	catch(Exception e){
		e.printStackTrace();
	}
}


if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
	if (resultCode == Activity.RESULT_OK) {
		// video successfully recorded
		// preview the recorded video
		manageVideo(fileUri.getPath());
	} else if (resultCode == Activity.RESULT_CANCELED) {
		// user cancelled recording
		Toast.makeText(getActivity(),
				"User cancelled video recording", Toast.LENGTH_SHORT)
				.show();
	} else {
		// failed to record video
		Toast.makeText(getActivity(),
				"Sorry! Failed to record video", Toast.LENGTH_SHORT)
				.show();
	}
}


if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

	
	
	BitmapFactory.Options options = new BitmapFactory.Options();

	options.inSampleSize = 8;

	final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
			options);

//	imgPreview.setImageBitmap(bitmap);
//
//
//Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
//ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//File destination = new File(Environment.getExternalStorageDirectory(),
//		System.currentTimeMillis() + ".jpg");
//
//FileOutputStream fo;
//try {
//	destination.createNewFile();
//	fo = new FileOutputStream(destination);
//	fo.write(bytes.toByteArray());
//	fo.close();
//} catch (FileNotFoundException e) {
//	e.printStackTrace();
//} catch (IOException e) {
//	e.printStackTrace();
//}
//
//
//Log.d("URLLLL", ""+mCapturedImageURI);
//String selectedImageUri = CelUtils.getpath(mCapturedImageURI, getActivity());
//Log.d("URLLLL", ""+selectedImageUri);
// String[] Img_size = { MediaStore.Images.Media.SIZE };
// PostCurrentImage(destination.getAbsolutePath());
 //manageImage(destination.getAbsolutePath());

	manageImage(fileUri.getPath());
	
	
//BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//bm = BitmapFactory.decodeFile(selectedImageUri, btmapOptions);
//tempImage.setImageBitmap(bm);
			}

if(requestCode==AUDIORECORDER){
	
	 Bundle bundle = intent.getExtras();
	    ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
	    // the recording url is in getData:
	    Uri audioUri = intent.getData();
	    
	
}

}  
	private void manageVideo(String path) {

			Intent intent = new Intent(_ctx, VideoUploadService.class);

			Bundle b=new Bundle();
			b.putString("msg", path);
			b.putString("friend_id", pref.get("chat_other"));
			b.putString("type", "10");
			b.putString("user_id", pref.get(Constants.KeyUUID));
			intent.putExtras(b);
			_ctx.startService(intent);

	}
	private void manageImage(String path) {

			Intent intent = new Intent(_ctx, VideoUploadService.class);

			Bundle b=new Bundle();
			b.putString("msg", path);
			b.putString("friend_id", pref.get("chat_other"));
			b.putString("type", "2");
			b.putString("user_id", pref.get(Constants.KeyUUID));
			intent.putExtras(b);
			_ctx.startService(intent);
	}

	public void hideSoftKeyboard(View v) {
	        Activity activity = (Activity) v.getContext();
	        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    }
		
		
		private class MyWebChromeClient extends WebChromeClient {

			@Override

			public void onProgressChanged(WebView view, int newProgress) {

			super.onProgressChanged(view, newProgress);

			getActivity().setProgress(newProgress * 100); //Make the bar disappear after URL is loaded

			              }
			// For Android 3.0+

			        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  



			            mUploadMessage = uploadMsg;  

			            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  

			            i.addCategory(Intent.CATEGORY_OPENABLE);  

			            i.setType("image/*");  

			            ChatWebView.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  
			           }

			        // For Android 3.0+

			           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {

			           mUploadMessage = uploadMsg;

			           Intent i = new Intent(Intent.ACTION_GET_CONTENT);

			           i.addCategory(Intent.CATEGORY_OPENABLE);

			           i.setType("*/*");

			           ChatWebView.this.startActivityForResult(

			           Intent.createChooser(i, "File Browser"),

			           FILECHOOSER_RESULTCODE);

			           }



			        //For Android 4.1

			           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){

			               mUploadMessage = uploadMsg;  

			               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  

			               i.addCategory(Intent.CATEGORY_OPENABLE);  

			               i.setType("image/*");  

			               ChatWebView.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), ChatWebView.FILECHOOSER_RESULTCODE );



			           }

			       }



			private class MyWebViewClient extends WebViewClient{



			@Override

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

					boolean shouldOverride = false;

					            if (url.endsWith(".mp3")||url.endsWith(".png")||url.endsWith(".jpg")||url.endsWith(".pdf")) {

			                shouldOverride = true;

			                Uri source = Uri.parse(url);

			                DownloadManager.Request request = new DownloadManager.Request(source);
			                File destinationFile = new File (Environment.getExternalStorageDirectory(), source.getLastPathSegment());
			                request.setDestinationUri(Uri.fromFile(destinationFile));
			                manager.enqueue(request);
			            }

					           

			            else{

			            	
			           String []test=url.split("/");
			           if(test.length==8){
			        	   if(test.length==8){
			        		   pref.set("chat_other", test[7]).commit();
					        	 DrawerActivity.chat_attachment.setVisibility(view.VISIBLE);
					            	DrawerActivity.popBtn.setVisibility(View.GONE);
					         }
					         else{
					        	  DrawerActivity.chat_attachment.setVisibility(view.GONE);
					            	DrawerActivity.popBtn.setVisibility(View.VISIBLE);
					         }
			        	   
			        	 
			           }
			           
			           else{
			        	   
			        	   DrawerActivity.chat_attachment.setVisibility(view.GONE);
			            	DrawerActivity.popBtn.setVisibility(View.VISIBLE);
			            	pref.set("chat_other", "").commit();
			        	  
			           }
			         
			            }
			            	
			            view.loadUrl(url);
					return shouldOverride;
			}

				@Override

			public void onPageFinished(WebView view, String url) {

			        // TODO Auto-generated method stub
					super.onPageFinished(view, url);
			        progressBar1.setVisibility(View.GONE);
			        Constants.isWebLoading=false;
			    }
			}
}