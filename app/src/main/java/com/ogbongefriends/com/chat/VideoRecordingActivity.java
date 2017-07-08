package com.ogbongefriends.com.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.androidrecording.video.CameraHelper;
import com.ogbongefriends.com.androidrecording.video.VideoRecordingHandler;
import com.ogbongefriends.com.androidrecording.video.VideoRecordingManager;

public class VideoRecordingActivity extends Activity {
	private static String fileName = null;
    
	private Button recordBtn, playBtn;
	private ImageButton switchBtn;
	private Spinner videoSizeSpinner;
	private Preferences pref;
	private Uri fileUri;
	static final int REQUEST_VIDEO_CAPTURE = 1;
	private Size videoSize = null;
	private static final String IMAGE_DIRECTORY_NAME = "Ogbonge";
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private VideoRecordingManager recordingManager;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	private VideoRecordingHandler recordingHandler = new VideoRecordingHandler() {
		@Override
		public boolean onPrepareRecording() {
			if (videoSizeSpinner == null) {
	    		initVideoSizeSpinner();
	    		return true;
			}
			return false;
		}
		
		@Override
		public Size getVideoSize() {
			return videoSize;
		}
		
		@Override
		public int getDisplayRotation() {
			return VideoRecordingActivity.this.getWindowManager().getDefaultDisplay().getRotation();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.video_rec);
		pref=new Preferences(VideoRecordingActivity.this);
//		if (!StorageUtils.checkExternalStorageAvailable()) {
//			NotificationUtils.showInfoDialog(this, getString(R.string.noExtStorageAvailable));
//			return;
//		}
//		fileName = StorageUtils.getFileName(false);
//		
//		AdaptiveSurfaceView videoView = (AdaptiveSurfaceView) findViewById(R.id.videoView);
//		recordingManager = new VideoRecordingManager(videoView, recordingHandler);
//		
//		recordBtn = (Button) findViewById(R.id.recordBtn);
//		recordBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				record();
//			}
//		});
//		
//		switchBtn = (ImageButton) findViewById(R.id.switchBtn);
//		if (recordingManager.getCameraManager().hasMultipleCameras()) {
//			switchBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					switchCamera();
//				}
//			});
//		}
//		else {
//			switchBtn.setVisibility(View.GONE);
//		}
//		
//		playBtn = (Button) findViewById(R.id.playBtn);
//		playBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				play();
//			}
//		});
		
		
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		// set video quality
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the video capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
		
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
	    }
		
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	        Uri videoUri = data.getData();
	        String tempPath = CelUtils.getpath(videoUri, VideoRecordingActivity.this);
	        manageVideo(fileUri.getPath());
	        //mVideoView.setVideoURI(videoUri);
	        onBackPressed();
	    }
	}



	@Override
	protected void onDestroy() {
//		recordingManager.dispose();
//		recordingHandler = null;
		
		super.onDestroy();
	}
	
	@SuppressLint("NewApi")
	private void initVideoSizeSpinner() {
		videoSizeSpinner = (Spinner) findViewById(R.id.videoSizeSpinner);
		if (Build.VERSION.SDK_INT >= 11) {
			List<Size> sizes = CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera());
			videoSizeSpinner.setAdapter(new SizeAdapter(sizes));
			videoSizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					videoSize = (Size) arg0.getItemAtPosition(arg2);
					recordingManager.setPreviewSize(videoSize);
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			videoSize = (Size) videoSizeSpinner.getItemAtPosition(0);
		}
		else {
			videoSizeSpinner.setVisibility(View.GONE);
		}
	}
	
	@SuppressLint("NewApi")
	private void updateVideoSizes() {
		if (Build.VERSION.SDK_INT >= 11) {
			((SizeAdapter) videoSizeSpinner.getAdapter()).set(CameraHelper.getCameraSupportedVideoSizes(recordingManager.getCameraManager().getCamera()));
			videoSizeSpinner.setSelection(0);
			videoSize = (Size) videoSizeSpinner.getItemAtPosition(0);
			recordingManager.setPreviewSize(videoSize);
		}
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
	private void switchCamera() {
		recordingManager.getCameraManager().switchCamera();
		updateVideoSizes();
	}
	
	private void record() {
		if (recordingManager.stopRecording()) {
			recordBtn.setText(R.string.recordBtn);
			switchBtn.setEnabled(true);
			playBtn.setEnabled(true);
			videoSizeSpinner.setEnabled(true);
		}
		else {
			startRecording();
		}
	}
	
	private void startRecording() {
		if (recordingManager.startRecording(fileName, videoSize)) {
			recordBtn.setText(R.string.stopRecordBtn);
			switchBtn.setEnabled(false);
			playBtn.setEnabled(false);
			videoSizeSpinner.setEnabled(false);
			return;
		}
		Toast.makeText(this, getString(R.string.videoRecordingError), Toast.LENGTH_LONG).show();
	}
	
	private void play() {
//		Intent i = new Intent(VideoRecordingActivity.this, VideoPlaybackActivity.class);
//		i.putExtra(VideoPlaybackActivity.FileNameArg, fileName);
//		startActivityForResult(i, 0);
		
		
		manageVideo(fileName);
	}
	
	private void manageVideo(String path) {

		
		Intent intent = new Intent(VideoRecordingActivity.this, VideoUploadService.class);
		Bundle b=new Bundle();
		b.putString("msg", path);
		b.putString("friend_id", pref.get("chat_other"));
		b.putString("user_id", pref.get(Constants.KeyUUID));
		b.putString("type","4");
		intent.putExtras(b);
		VideoRecordingActivity.this.startService(intent);

	}
}