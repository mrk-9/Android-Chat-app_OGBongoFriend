package com.ogbongefriends.com.custom;


import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Bitmap result1;
	Context context1;
	ProgressDialog dialog;

	public DownloadImageTask(ImageView bmImage, Context context) {
		this.bmImage = bmImage;
		context1 = context;
		dialog=new ProgressDialog(context);
	}

	public DownloadImageTask() {
		// TODO Auto-generated constructor stub
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Log.d("DEmo", "in background");
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		Log.d("DEmo", "in post " + result.getHeight());
		result1 = result;
		Drawable d = new BitmapDrawable(context1.getResources(), result);
		bmImage.setBackgroundDrawable(d);
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
	}
	
	@Override
	protected void onPreExecute() {
		dialog.setTitle("Downloading");
		dialog.show();
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	  
	//DownloadImageTask a= new DownloadImageTask();
	  
}