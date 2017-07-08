package com.ogbongefriends.com.common;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

//import org.apache.commons.codec.binary.Base64;

public class HttpFileUploader implements Runnable {

	URL connectURL;
	String mediaType;
	String responseString;
	String userId;
	String Type;
	
	//long FeedId;
	// InterfaceHttpUtil ifPostBack;
	String fileName;
	byte[] dataToServer;

	public HttpFileUploader(String urlString, String mediaType, String fileName, String userid/*, long Feedid*/,String type, String upload_type,String uploadType) {

		try {
//			 http://192.168.0.188/skillgrok_pro/Cpanels/fileUpload/Image/1/0
//			connectURL = new URL(urlString + "?media=" + mediaType+"&type="+type+"&id="+Feedid);
			
			
			connectURL = new URL(urlString + "/" + mediaType+"/"+upload_type+"/"+uploadType);//+"/"); //+Feedid+"/"+1);
/*			String u = "http://mc2mediastudios.com/crossover/urban/api/fileUpload.php";//?media=Image&type=1&id=33";
			connectURL = new URL(u + "?media=Image&type"+type+"&id=" + 33);*/
		//	http://mc2mediastudios.com/crossover/urban/api/fileUpload.php?media=Image&type=1&id=33
			
			Log.v("final url","final url"+ connectURL.toString());
		} catch (Exception ex) {
			Log.i("URL FORMATION", "MALFORMATED URL");
		}

		this.mediaType = mediaType;
		this.fileName = fileName;
		this.userId = userid;
		this.Type = type;
		//this.FeedId = Feedid;

	}

	public void doStart(FileInputStream stream) {
		fileInputStream = stream;
		thirdTry();
	}

	FileInputStream fileInputStream = null;
	private static String s;

	void thirdTry() {
		// String exsistingFileName = "asdf.png";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String Tag = "ash";
		try {
			// ------------------ CLIENT REQUEST

			Log.e(Tag, "Starting to bad things");
			// Open a HTTP connection to the URL

			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",	"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			/*
			dos.writeBytes("Content-Disposition: form-data; name=\"file_upload\"; " +
					"media=\""+ mediaType+ "\"; uuid=\""+ userId+ "\";type=\""+Type+ "\";" +
					" filename=\"" + fileName + "\"" + lineEnd);
			*/
			/*dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; " +
					"media=\""+ mediaType + "\"; uuid=\"" + userId + "\";type=\""+ Type+ "\";filename=\"" + fileName + "\"" + lineEnd);
			*/

			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; " +
					"media=\""+ mediaType+ "\"; uuid=\""+ userId+ "\";type=\""+ Type+ "\";" +
					"filename=\"" + fileName + "\"" + lineEnd);
			
			
			/*
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; " +
					"media=\""+ mediaType+ "\"; uuid=\""+ userId+ "\";type=\""+ Type+ "\";" +
					" feed_id=\""+ FeedId + "\"; filename=\"" + fileName + "\"" + lineEnd);*/
			
			
			
			
			dos.writeBytes(lineEnd);
			
			
			//dos.writeBytes(lineEnd);

			Log.e(Tag, "Headers are written");

			// create a buffer of maximum size

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// read file and write it into form...

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e(Tag, "File is written");
			fileInputStream.close();
			dos.flush();

			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			s = b.toString();		
			
			Log.i("ash sparsh", s);

			dos.close();

		} catch (MalformedURLException ex) {
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}
	}

	@Override
	public void run() {
		
	}

	public static String updateCall() {
		String st = s;
		s="";
		return st;
		
	}
}