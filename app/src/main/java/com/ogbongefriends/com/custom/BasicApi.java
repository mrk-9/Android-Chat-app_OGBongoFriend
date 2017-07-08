
package com.ogbongefriends.com.custom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Utils;


//=====
public abstract class BasicApi{
    
    //===
    protected boolean interrupted = false;
    protected int timeout = 1000*30;
    protected DB dbb;
    protected Context ctx;
    protected boolean save = false;
    
    //===
    public BasicApi(Context ctx, DB db){
    	this.dbb = db;
    	this.ctx = ctx;
    }
    
    //===
    public void toBeSaved(){ save = true; }
    public void toBeSaved(boolean bsave){ save = bsave; }
    
    //===
    public void interrupt(){ interrupted = true; }
    
    //===
    public void callUrl(String rurl){
        
    	Log.d("HIT URL IS", ""+rurl);
        try{
            
            interrupted = false;
            HttpURLConnection conn = (HttpURLConnection)new URL(rurl).openConnection();
            conn.setConnectTimeout(timeout);
                   
            if(onConnection(conn)){
                InputStream stream= new BufferedInputStream(conn.getInputStream());
                if(!interrupted) onResponseReceived(stream);
            }
            else{
            	
            }
            conn.disconnect();
            onDone();
            updateUICall();
            
            
        }catch(Exception e){ 
            
            if(save && dbb != null) saveInDB(rurl, null); 
            Utils.exception(Constants.kApiExpTag, e, "CallUrl in BasicApi");
            onError(e);
            updateUICall();
           
        }
    }
    
    //===
    /** Invoked when connection is established */
    protected boolean onConnection(HttpURLConnection conn){
        return true;
    }
    
    //===
    public void postData(String purl, String data){
        
    	HttpURLConnection conn = null;
        try{
            
            interrupted = false;
            String postData = data;
            
      //      Utils.log(Constants.kApiTag, "request url is "+purl);
        //    Utils.log(Constants.kApiTag, "request xml is "+postData);
            
            URL url = new URL(purl);
            
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setRequestProperty("Content-type", "application/json; charset=" + "UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            
            if(onConnection(conn)){
                
            //	Log.syncMsg("Connection established ====>>> ");
                OutputStreamWriter writer= new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.close();
                
              //  Log.syncMsg("Stream Writed =====>>> ");
                
                InputStream stream=new BufferedInputStream(conn.getInputStream());
                Log.v("Stream taken as an input =====>>> ", "");
                if(!interrupted) onResponseReceived(stream);
                
            }
            else{
            	//Log.syncMsg("Error In Connection =====>>> ");
            }    
            
            conn.disconnect();
            
            onDone();
            updateUICall();
            
            
        }catch(Exception e){
        //	Log.syncMsg(e.getMessage()+" Occurs At Post Data Module");
        //	Log.syncMsg("Connection Error Stream ==== >>> "+conn.getErrorStream());
        //Log.v("ash", e.getMessage());
            onError(e);
            updateUICall();
            if(save && dbb != null) saveInDB(purl, data);
        }
    }
    
    //===
    protected void saveInDB(String url, String post){
        
//        ContentValues cv = new ContentValues();
//        cv.put(PendingApi.Table.Column.Url.toString(), url);
//        if(post!=null) cv.put(PendingApi.Table.Column.Post.toString(), post);
//        dbb.getSqliteDB().insert(PendingApi.Table.Name.toString(), null, cv);
    }
    
    //===
    protected String getString(InputStream is) throws IOException{
        
    	/*Log.d("CONVERSION STARTED");
    	Log.d("Input Stream Size == "+is.available());*/
    	
        String s = new StringBuilder(is.available()).toString();
        int l = -1;
        byte[] b = new byte[1024];
        
        while((l=is.read(b)) != -1){
        	
        	/*Log.d("String Length == "+s.length());*/
            s += new String(b, 0 ,l);
            
        }    
        is.close();
        
        /*Log.d("CONVERSION ENDED");*/
        
        return Html.fromHtml(s).toString();
    }
    
    //=
    
    protected String convertStreamToString(InputStream is) throws IOException{
    	
        ByteArrayOutputStream oas = new ByteArrayOutputStream(10 * 1024 * 1024);
        
        /*Log.d("CONVERSION STARTED");*/
        copyStream(is, oas);
        /*Log.d("CONVERSION ENDED");*/
        
        String t = oas.toString();
        oas.close();
        oas = null;

        return t;
    }
    
    //=
    
    private void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
  //=
    
    private void updateUICall()
    {
    	if (ctx instanceof Activity) {
    		((Activity)ctx).runOnUiThread(new Runnable() {
    			
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				updateUI();
    			}
    		});
    	}
		
    }
    
    //=
    
    protected abstract void onResponseReceived(final InputStream is);
    protected abstract void onError(final Exception e);
    /** Invoked when all things are done and nothig is left now */
    protected abstract void onDone();
    /** Invoked when all things are done to update The UI Components*/
    protected abstract void updateUI();
    
}