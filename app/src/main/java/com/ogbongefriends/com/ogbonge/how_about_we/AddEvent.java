package com.ogbongefriends.com.ogbonge.how_about_we;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;


public class AddEvent extends Activity{
	
	private static final int CAMERA_REQUEST = 1888;
	private Uri imageUri;
	private static final int SELECT_PHOTO = 100;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 2;
	private TextView dateDisplay;
	private Button pickDate;
	private int year, month, day;
	private TextView timeDisplay;
	private Button pickTime;
	private int hours, min;
	private EditText ename;
	private Spinner spinner;
	private Button save;
	private Button upload_image;
	private TextView txt;
	private String event_type;
	private Uri selectedImage;
	private SharedPreferences pref;
	private int placeId;
	private long str;
	private DB db;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.event);
	     
	        
	        pref = PreferenceManager.getDefaultSharedPreferences(this);
			placeId = pref.getInt("clicked_place_id", -1);
			str = pref.getLong("id", -1);
						
			db = new DB(this);
			
			
	        ename=(EditText)findViewById(R.id.rgisemail);
	        spinner=(Spinner)findViewById(R.id.spinner1);
	        save=(Button)findViewById(R.id.button1);
	        upload_image=(Button)findViewById(R.id.button2);
	        txt=(TextView)findViewById(R.id.textView1);
	        txt.setText("");
	        
	        addItemsOnSpinner();
	        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				

				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					Object item = parent.getItemAtPosition(pos);
					event_type = item.toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
	        
	        
	        upload_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					chose_image();	
				}
			});
	        
	        save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ename.getText().length()>0){
						try {
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("name", ename.getText().toString());
							map.put("category_id", event_type);
							map.put("place_id", String.valueOf(placeId));
							db.insert(DB.Table.Name.event_master, map);
							Toast.makeText(getBaseContext(),
									"Record Saved.-->" + map, 100).show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
					Toast.makeText(getApplicationContext(), "Please Enter Event name.", 100).show();	
					}
				}
			});
	        
	        dateDisplay = (TextView)findViewById(R.id.textView2);
	        pickDate = (Button)findViewById(R.id.button3);
	        
	        pickDate.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
				}
	        	
	        });
	        
	        timeDisplay = (TextView)findViewById(R.id.textView3);
	        pickTime = (Button)findViewById(R.id.button4);
	        
	        pickTime.setOnClickListener( new OnClickListener () {

				@Override
				public void onClick(View v) {
					showDialog(TIME_DIALOG_ID);
					
				}
	        	
	        });
	        
	        final Calendar cal = Calendar.getInstance();
	        year = cal.get(Calendar.YEAR);
	        month = cal.get(Calendar.MONTH);
	        day = cal.get(Calendar.DAY_OF_MONTH);
	        
	        updateDate();
	        hours = cal.get(Calendar.HOUR);
	        min = cal.get(Calendar.MINUTE);
	        
	        updateTime();
	        
	  
	 }


		public void chose_image() {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					AddEvent.this);

			// set title
			alertDialogBuilder.setTitle("Choose Image from..");

			// set dialog message
			alertDialogBuilder
					
					.setCancelable(true)
					.setPositiveButton("CAMERA",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									startCamera();
//									Intent cameraIntent = new Intent(
//											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//									startActivityForResult(cameraIntent, CAMERA_REQUEST);
								}
							})
					.setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							
							 Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
							 photoPickerIntent.setType("image/*");
							 startActivityForResult(photoPickerIntent, SELECT_PHOTO);
						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent imageReturnedIntent) {
			super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

			switch (requestCode) {
			case SELECT_PHOTO:
				if (resultCode == RESULT_OK) {
					selectedImage = imageReturnedIntent.getData();
					InputStream imageStream = null;
					try {
						imageStream = getContentResolver().openInputStream(
								selectedImage);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Bitmap yourSelectedImage = BitmapFactory
							.decodeStream(imageStream);

					txt.setText(String.valueOf(selectedImage));
					// img.setImageBitmap(yourSelectedImage);
				}
				break;
			case CAMERA_REQUEST:
				if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
					//selectedImage = imageReturnedIntent.getData();
//					Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get(
//							"data");
					// img.setImageBitmap(photo);

					txt.setText(String.valueOf(imageUri));
				}
				break;

			}
		}

		public void startCamera() {
	        Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
	        String fileName = "testphoto.jpg";
	        ContentValues values = new ContentValues();
	        values.put(MediaStore.Images.Media.TITLE, fileName);
	        values.put(MediaStore.Images.Media.DESCRIPTION,
	                "Image capture by camera");
	        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
	        imageUri = getContentResolver().insert(
	                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        startActivityForResult(intent, CAMERA_REQUEST);
	    }


	private void updateTime() {
		timeDisplay.setText(new StringBuilder().append(hours).append(':')
				.append(min));
		
	}

	private void updateDate() {
		dateDisplay.setText(new StringBuilder().append(day).append('-')
				.append(month + 1).append('-').append(year));
		
	}
	
	private DatePickerDialog.OnDateSetListener dateListener = 
		new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int yr, int monthOfYear,
					int dayOfMonth) {
				year = yr;
				month = monthOfYear;
				day = dayOfMonth;
				updateDate();
			}
	};
	
	private TimePickerDialog.OnTimeSetListener timeListener = 
		new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				hours = hourOfDay;
				min = minute;
				updateTime();
			}
		
	};
	protected Dialog onCreateDialog(int id){
		switch(id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dateListener, year, month, day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timeListener, hours, min, false);
		}
		return null;
		
	}
	// add items into spinner
		public void addItemsOnSpinner() {

			List<String> list = new ArrayList<String>();
			list.add("Party");
			list.add("Concert");
			list.add("Exhibition");
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
		}
}
