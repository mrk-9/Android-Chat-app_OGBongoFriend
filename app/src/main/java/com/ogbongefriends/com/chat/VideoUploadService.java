package com.ogbongefriends.com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import com.ogbongefriends.com.R;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class VideoUploadService extends Service {
	private static final String TAG = "VideoUploadService";

	private Handler handler = new Handler();
	private Queue<VideoUploadThread> myQueue;
	private VideoUploadThread thread;
	private String type;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		MessageModel model = new MessageModel();
		Bundle bb=intent.getExtras();
		type=intent.getExtras().getString("type");
		model.setMessage(bb.getString("msg"));
		model.setFriendId(bb.getString("friend_id"));
		model.setUserID(bb.getString("user_id"));
	
		

		if (myQueue == null) {
			myQueue = new LinkedList<VideoUploadThread>();
		}
		
		VideoUploadThread tempThread = new VideoUploadThread(model);
		myQueue.add(tempThread);
		if (thread == null) {
			thread = myQueue.poll();
			thread.start();

		}

		return START_STICKY;
	}

	private void manageQueue() {
		thread = myQueue.poll();
		if (thread != null) {
			thread.start();
		} else {
			stopSelf();
		}
	}

	private void doFileUpload(MessageModel model) {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;// 1 MB
		String responseFromServer = "";

		String imageName = null;
		try {
			// ------------------ CLIENT REQUEST
			File file = new File(model.getMessage());
			FileInputStream fileInputStream = new FileInputStream(file);
			Log.d(TAG, "File Name :: " + file.getName());
			String[] temp = file.getName().split("\\.");
			Log.d(TAG, "temp array ::" + temp);
			String extension = temp[temp.length - 1];
			imageName = model.getUserID() + "_" + System.currentTimeMillis()
					+ "." + extension;

			// open a URL connection to the Servlet
			String ur=VideoUploadService.this.getString(R.string.urlString)+"api/uploadChat/index/"+model.getUserID()+"/"+model.getFriendId()+"/"+type;
			URL url = new URL(ur);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			
			
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ imageName + "\"" + lineEnd);
			Log.i(TAG, "Uploading starts");
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				// Log.i(TAG, "Uploading");
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				Log.d(TAG, "Uploading Vedio :: " + imageName);
			}
			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			Log.e("Debug", "File is written");
			Log.i(TAG, "Uploading ends");
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Log.e("Debug", "error: " + ioe.getMessage(), ioe);
		}
		// ------------------ read the SERVER RESPONSE ----------------
		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;

			while ((str = inStream.readLine()) != null) {
				Log.e("Debug", "Server Response " + str);
				try {
					final JSONObject jsonObject = new JSONObject(str);
					
					
//					if (jsonObject.getBoolean("success")) {
//						handler.post(new Runnable() {
//							public void run() {
//								try {
//									Toast.makeText(getApplicationContext(),
//											jsonObject.getString("message"),
//											Toast.LENGTH_SHORT).show();
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//						});
//					} else {
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								try {
//									Toast.makeText(getApplicationContext(),
//											jsonObject.getString("message"),
//											Toast.LENGTH_SHORT).show();
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//						});
//					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			model.setMessage(imageName);
			onUploadComplete(model);
			inStream.close();

		} catch (IOException ioex) {
			ioex.printStackTrace();
			Log.e("Debug", "error: " + ioex.getMessage(), ioex);
		}
		manageQueue();
	}

	private void onUploadComplete(MessageModel model) {

//		Log.d(TAG, "Vedio Uploading Complete:: " + model);
//
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("id", model.getUserID() + ""));
//		list.add(new BasicNameValuePair("friend_id", model.getFriendId() + ""));
//		list.add(new BasicNameValuePair("message", model.getMessage()));
//		list.add(new BasicNameValuePair("type", MessageModel.MESSAGE_TYPE_VEDIO
//				+ ""));
//		list.add(new BasicNameValuePair("temp_msg_id", model.getId() + ""));
//
//		try {
//			String responseString = new HttpRequest().postData(
//					Urls.SEND_MESSAGE, list);
//			Log.d(TAG, "Response form server :: " + responseString);
//			AHttpResponse response = new AHttpResponse(responseString, true);
//			final MessageModel message = response.getSendMessage();
//			message.setMessageStatus(MessageModel.STATUS_SEND);
//			if (response.isSuccess) {
//				mDbAdapter.insertOrUpdateMessage(model);
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						Intent intent = new Intent(MyActions.SEND_MESSAGE);
//						intent.putExtra("data", message);
//						LocalBroadcastManager.getInstance(
//								getApplicationContext()).sendBroadcast(intent);
//					}
//				});
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	class VideoUploadThread extends Thread {

		private MessageModel model;

		public VideoUploadThread(MessageModel model) {
			this.model = model;

		}

		@Override
		public void run() {
			doFileUpload(model);
		}
	}

}
