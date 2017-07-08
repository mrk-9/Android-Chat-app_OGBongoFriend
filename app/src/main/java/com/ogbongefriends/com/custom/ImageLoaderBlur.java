package com.ogbongefriends.com.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;


public class ImageLoaderBlur {

	static MemoryCache memoryCache = new MemoryCache();
	static FileCache fileCache;
	Activity activity;
	private static final float BLUR_RADIUS = 25f;
	Context ctx;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// private Map<ImageView, String> imageViews=Collections.synchronizedMap(new
	// WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoaderBlur(Context context) {
		activity = (Activity) context;
		this.ctx = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	public void DisplayImage(String url, ImageView imageView/*, Boolean chk*/) {

		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		
		if (bitmap != null) {

			imageView.setImageBitmap(blur(bitmap));
			
			//if(chk == true){
				//imageView.setImageBitmap(getCroppedBitmap(bitmap));	
			/*}
			else{
				imageView.setImageBitmap(bitmap);
			}*/
			
			
			
		} else {
			queuePhoto(url, imageView);
			imageView.setImageResource(R.drawable.profile);
		}
	}

	public Bitmap blur(Bitmap image) {
		if (null == image) return null;

		Bitmap outputBitmap = Bitmap.createBitmap(image);
		final RenderScript renderScript = RenderScript.create(this.ctx);
		Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
		Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

		//Intrinsic Gausian blur filter
		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
		theIntrinsic.setRadius(BLUR_RADIUS);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);
		return outputBitmap;
	}



	public Bitmap getCroppedBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    
	    
	   // final RectF rectF = new RectF(rect);
	   	    
	    paint.setAntiAlias(true);
	    paint.setDither(true);
        paint.setFilterBitmap(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color); 
	   // canvas.drawOval(rectF, paint);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    return output;
	}   
	
	
	
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {

		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			@SuppressWarnings("unused")
			final int REQUIRED_SIZE = 70;
			@SuppressWarnings("unused")
			int width_tmp = o.outWidth, height_tmp = o.outHeight;

			int scale = 30;

			// while(true){
			// if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
			// break;
			// width_tmp/=2;
			// height_tmp/=2;
			// scale*=2;
			// }

			final int IMAGE_MAX_SIZE = Utils.getDevicewidth(activity);

			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// decode with inSampleSize

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))

				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(blur(bitmap));
			// else
			// photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	// ======= clear cache==============

	public static void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	// ======== to laod image on slow network========

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}