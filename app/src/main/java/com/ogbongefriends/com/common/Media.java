package com.ogbongefriends.com.common;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.ogbongefriends.com.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;


//import com.community.dcp.commonscreens.AudioRecorder;
//import com.community.dcp.commonscreens.VideoRecorder;

public class Media {
	
	public static final int ACTION_TAKE_PHOTO  = 1000;
	public static final int ACTION_TAKE_VIDEO  = 2000;
	public static final int ACTION_TAKE_AUDIO  = 3000;

	public static final String IMAGE_FILE_PREFIX = "IMG_";
	public static final String VIDEO_FILE_PREFIX = "VID_";
	public static final String AUDIO_FILE_PREFIX = "AUD_";
	public static final String UNKNOWN_FILE_PREFIX = "UNKNOWN_";
	
	public static final String IMAGE_FILE_SUFFIX = ".jpg";
	public static final String AUDIO_FILE_SUFFIX = ".3gpp";
	public static final String VIDEO_FILE_SUFFIX = ".mp4";
	public static final String UNKNOWN_FILE_SUFFIX = ".tmp";
	
	public static final String QUE_MEDIA_DIR = "Question_Media";
	public static final String PROFILE_MEDIA_DIR = "Profile_Media";
	public static final String BACK_MEDIA_DIR = "Background_Media";
	public static final String DB_DIR = "Database_Backup";
	public static final String LOG_DIR = "Log_Data";
	
	public static final String AUDIO_DIR = "Audio";
	public static final String VIDEO_DIR = "Video";
	public static final String IMAGE_DIR = "Image";
	
	public static final int AUDIO_FILE = 1;
	public static final int VIDEO_FILE = 2;
	public static final int IMAGE_FILE = 3;
	
	
	//======
	
	public static File getAlbumDir(Context ctx, String currPath) {
		
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = getAlbumStorageDir(ctx, currPath);

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(ctx.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		
		return storageDir;
	}
	
	//======
	public static String getAlbumName(Context ctx) {
		return ctx.getString(R.string.album_name);
	}

	//======
	private static File getAlbumStorageDir(Context ctx, String currPath) {
		// TODO Auto-generated method stub
		return new File(
		  Environment.getExternalStorageDirectory()+"/"+getAlbumName(ctx)+"/"+currPath
		);
	}
//	
	//======
	public static String getFileName(int fileType){
		
		long timeStamp = Calendar.getInstance().getTimeInMillis();//new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		
//		String filePrefix = UNKNOWN_FILE_PREFIX;
		String fileSuffix = UNKNOWN_FILE_SUFFIX;
		
		switch (fileType) {
		
			case AUDIO_FILE:
//				filePrefix = AUDIO_FILE_PREFIX;
				fileSuffix = AUDIO_FILE_SUFFIX;
				break;
				
			case VIDEO_FILE:
//				filePrefix = VIDEO_FILE_PREFIX;
				fileSuffix = VIDEO_FILE_SUFFIX;
				break;
							
			case IMAGE_FILE:
//				filePrefix = IMAGE_FILE_PREFIX;		
				fileSuffix = IMAGE_FILE_SUFFIX;
				break;	

			}
		
		return /*filePrefix+*/timeStamp+"_V1"+fileSuffix;
		
	}
	
	//======
	public static String getTempImgName(int fileType){
		
		String filePrefix = UNKNOWN_FILE_PREFIX;
		String fileSuffix = UNKNOWN_FILE_SUFFIX;
		
		switch (fileType) {
		
			case AUDIO_FILE:
				filePrefix = AUDIO_FILE_PREFIX;
				fileSuffix = AUDIO_FILE_SUFFIX;
				break;
				
			case VIDEO_FILE:
				filePrefix = VIDEO_FILE_PREFIX;
				fileSuffix = VIDEO_FILE_SUFFIX;
				break;
							
			case IMAGE_FILE:
				filePrefix = IMAGE_FILE_PREFIX;		
				fileSuffix = IMAGE_FILE_SUFFIX;
				break;	

			}
		
		return filePrefix +"TMP"+fileSuffix;
		
	}
	
	//=====
	public static void dispatchTakeIntent(Context ctx, String url, int actionCode) {
		
		switch(actionCode) {
		
			case ACTION_TAKE_PHOTO:
				
				ImageCaptureAction(ctx, url, actionCode);
				break;
				
//			case ACTION_TAKE_VIDEO:
//				VideoCaptureAction(ctx, url, actionCode);
//				break;
//				
//			case ACTION_TAKE_AUDIO:
//				AudioCaptureAction(ctx, url, actionCode);
//				break;
			
		} // switch

		
		
	}
	
	//=====
	private static void ImageCaptureAction(Context ctx, String url, int actionCode){
		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File f;
		
		try {
			
			f = createFile(url);
			
			Log.d("Image File Path == "+f.getAbsolutePath());
			
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
			f = null;
			//mCurrentPhotoPath = null;
		}
		
		if(f!=null)
			((Activity)ctx).startActivityForResult(takePictureIntent, actionCode);
		
	}
	
	//=====
//	public static void VideoCaptureAction(Context ctx, String url, int actionCode){
//		
//		Intent takeVideoIntent = new Intent(ctx, VideoRecorder.class);
//		File f;
//		
//		try {
//			
//			f = createFile(url);
//			Log.d("Video File Path == "+f.getAbsolutePath());
//			
//			takeVideoIntent.putExtra(VideoRecorder.FILE_PATH, f.getAbsolutePath());
//			
//			
//		}
//		
//		catch (IOException e) {
//			e.printStackTrace();
//			f = null;
//			//mCurrentPhotoPath = null;
//		}
//		
//		if(f!=null)
//			((Activity)ctx).startActivityForResult(takeVideoIntent, actionCode);
//		
//	}
	
	//=====
//	private static void AudioCaptureAction(Context ctx, String url, int actionCode){
//		
//		Intent takeAudioIntent = new Intent(ctx, AudioRecorder.class);
//		File f;
//		
//		try {
//			
//			f = createFile(url);
//			
//			Log.d("Audio File Path == "+f.getAbsolutePath());
//			
//			takeAudioIntent.putExtra(AudioRecorder.FILE_PATH, f.getAbsolutePath());
//			
//		}
//		
//		catch (IOException e) {
//			e.printStackTrace();
//			f = null;
//			//mCurrentPhotoPath = null;
//		}
//		
//		if(f!=null)
//			((Activity)ctx).startActivityForResult(takeAudioIntent, actionCode);
//		
//	}
	
	//=====
	public static File createFile(String url) throws IOException {
		
		File file = new File(url);
		
		if(file.exists()){
			//======Do Nothing
		}
		else{
			file.createNewFile();
		}	
		
		return file;
	}
	
	//=====
	public static void setPicture(String imgUrl, ImageView view, int defaultRes){
		try {			
			//Bitmap mImageBitmap = Utils.getPortraitViewBitmap(imgUrl);
			//view.setImageBitmap(mImageBitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			view.setImageResource(defaultRes);
		}
	}
}
