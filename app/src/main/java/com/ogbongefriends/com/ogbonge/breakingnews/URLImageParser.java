package com.ogbongefriends.com.ogbonge.breakingnews;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Base64;
import android.view.View;

public class URLImageParser  implements ImageGetter {
    Context context;
    View container;

    public URLImageParser(View container, Context context) {
        this.context = context;
        this.container = container;
    }

    public Drawable getDrawable(String source) {   
        if(source.matches("data:image.*base64.*")) {
            String base_64_source = source.replaceAll("data:image.*base64", "");
            byte[] data = Base64.decode(base_64_source, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);                
            Drawable image = new BitmapDrawable(context.getResources(), bitmap);
            image.setBounds(0, 0, 0 + image.getIntrinsicWidth(), 0 + image.getIntrinsicHeight()); 
            return image;
        } else {
            URLDrawable urlDrawable = new URLDrawable();
            ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
            asyncTask.execute(source);          
            return urlDrawable; //return reference to URLDrawable where We will change with actual image from the src tag
        }        
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {         
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight()); //set the correct bound according to the result from HTTP call            
            urlDrawable.drawable = result; //change the reference of the current drawable to the result from the HTTP call          
            URLImageParser.this.container.invalidate(); //redraw the image by invalidating the container
        }

        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = (InputStream) new URL(urlString).getContent();
                Drawable drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight()); 
                return drawable;
            } catch (Exception e) {
                return null;
            } 
        }
    }
}