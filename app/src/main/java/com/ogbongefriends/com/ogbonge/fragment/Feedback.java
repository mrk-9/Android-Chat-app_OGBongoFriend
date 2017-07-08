package com.ogbongefriends.com.ogbonge.fragment;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.FeedbackApi;
import com.ogbongefriends.com.api.settingAccountApi;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.GeocodeJSONParser;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

public class Feedback extends Fragment{

	private View rootView;
	private Context _ctx;
	MapView mMapView;
	private GoogleMap googleMap;
	double latitude,longitude;
	private TextView address,e_mail,pincode,phone;
	private EditText name,email,subject,message;
	private Button submit;
	private Spinner category;
	private Preferences pref;
	private String[] cate={"Select Category","Complaint","Feedback","Suggestion"};
	private int cat=0;
	private FeedbackApi feedBackApi;
	private DB db;
	private CustomLoader p;
	private Cursor user_data;
	String url = "https://maps.googleapis.com/maps/api/geocode/json?";	
	
	private settingAccountApi setting_account_api;

	public Feedback(){

	}
	public Feedback(Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.feedback, container, false);
		db= DrawerActivity.db;
		p=DrawerActivity.p;
		name=(EditText)rootView.findViewById(R.id.name);
		email=(EditText)rootView.findViewById(R.id.email);
		subject=(EditText)rootView.findViewById(R.id.subject);
		message=(EditText)rootView.findViewById(R.id.message);
		
		address=(TextView)rootView.findViewById(R.id.address);
		pincode=(TextView)rootView.findViewById(R.id.pincode);
		e_mail=(TextView)rootView.findViewById(R.id.e_mail);
		phone=(TextView)rootView.findViewById(R.id.phone);
		
		submit=(Button)rootView.findViewById(R.id.submit);
		category=(Spinner)rootView.findViewById(R.id.category_list);
		pref=new Preferences(_ctx);
	db.open();
		user_data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		if(user_data.getCount()>0){
			user_data.moveToFirst();
			name.setText(user_data.getString(user_data.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+user_data.getString(user_data.getColumnIndex(DB.Table.user_master.last_name.toString())));
			email.setText(user_data.getString(user_data.getColumnIndex(DB.Table.user_master.email.toString())));
			name.setEnabled(false);
			email.setEnabled(false);
		}
		
		ArrayAdapter<String> a =new ArrayAdapter<String>(_ctx,android.R.layout.simple_spinner_item, cate);
		a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category.setAdapter(a);
		
		mMapView = (MapView) rootView.findViewById(R.id.mapView);
	    mMapView.onCreate(savedInstanceState);

	    mMapView.onResume();// needed to get the map to display immediately

	    category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				cat=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		
	    
	    
	    });
	    
	    try {
	        MapsInitializer.initialize(getActivity().getApplicationContext());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    
	    
	    googleMap = mMapView.getMap();
	    googleMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );   
	    feedBackApi=new FeedbackApi(_ctx, db, p) {

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				p.cancel();
				super.onResponseReceived(is);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				p.cancel();
				super.onDone();
				
				if(feedBackApi.resCode==1){
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Utils.same_id("Sucess...", feedBackApi.resMsg, _ctx);
						}
					});
					
					
				}
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
	    };
	    
	    submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				HitApi();
				
			}
		});
	    
	    setting_account_api=new settingAccountApi(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setdata();
					}
				});
				
				}
			

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
			}
			 
		 };
		 p.show();
	callApi(setting_account_api);

		return rootView;
	}
	
	private void setdata(){
		p.cancel();
		db.open();
		String location="",loc_address="";
	Cursor	data=db.findCursor(DB.Table.Name.setting_master, DB.Table.setting_master.id.toString()+" = 1", null, null);
		 if(data.getCount()>0){
			data.moveToFirst();
			
		//points=	data.getString(data.getColumnIndex(Table.setting_master.advertise_me_value.toString()));
		address.setText("Address: "+data.getString(data.getColumnIndex(DB.Table.setting_master.contact_us_address.toString())));
		location=data.getString(data.getColumnIndex(DB.Table.setting_master.contact_us_address.toString()));
		e_mail.setText("E-mail: "+data.getString(data.getColumnIndex(DB.Table.setting_master.contact_us_email.toString())));
		pincode.setText("Pincode: "+data.getString(data.getColumnIndex(DB.Table.setting_master.contact_us_pincode.toString())));
		phone.setText("Phone: "+data.getString(data.getColumnIndex(DB.Table.setting_master.contact_us_phone.toString())));
		
		 }
		 db.close();
		 location="139 Nnebisi Rd, Asaba";
		 try {
				// encoding special characters like space in the user input place
				location = URLEncoder.encode(location, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String address;
			try {
				loc_address = "address=" + URLEncoder.encode(location, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String sensor = "sensor=false";
			
			
			// url , from where the geocoding data is fetched
			url = url + loc_address + "&" + sensor;
			url.replace(" ", "%20");
			// Instantiating DownloadTask to get places from Google Geocoding service
			// in a non-ui thread
			DownloadTask downloadTask = new DownloadTask();
			
			// Start downloading the geocoding places
			downloadTask.execute(url);
		 
	}
	
	private boolean validate(){
		
		boolean flag=false;
		
		
		if(name.getText().toString().length()>0){
			if(email.getText().toString().length()>0){
				if(cat>=1){
					if(subject.getText().toString().length()>0)	{
						if(message.getText().toString().length()>0){
							flag=true;
							
							
						}
						else{
							Utils.same_id("Message", "Message can't be blanck", _ctx);
						}
					}
					
					else{
						Utils.same_id("Message", "Subject can't be blanck", _ctx);
					}
				}
				else{
					Utils.same_id("Message", "Please select a category", _ctx);
				}
				
			}
			
			else{
				Utils.same_id("Message", "Email can't be blanck", _ctx);
			}
		}
		else{
			Utils.same_id("Message", "Name can't be blanck", _ctx);
		}
		return flag;
	}
	
	private void HitApi(){
		
		
	if(validate()){
		p.show();
		HashMap<String, String>data=new HashMap<String, String>();
		
		data.put("name", name.getText().toString());
		data.put("email", email.getText().toString());
		data.put("category", String.valueOf(cat));
		data.put("subject", subject.getText().toString());
		data.put("message", message.getText().toString());
	
		feedBackApi.setPostData(data);
		callApi(feedBackApi);
		
		
	}
		
		
	}
	
	
	
	private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);


                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
        
	}

	
	 /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String>{

            String data = null;

            // Invoked by execute() method of this object
            @Override
            protected String doInBackground(String... url) {
                    try{                    		
                            data = downloadUrl(url[0]);
                    }catch(Exception e){
                             Log.d("Background Task",e.toString());
                    }
                    return data;
            }

            // Executed after the complete execution of doInBackground() method
            @Override
            protected void onPostExecute(String result){
            		
            		// Instantiating ParserTask which parses the json data from Geocoding webservice
            		// in a non-ui thread
            		ParserTask parserTask = new ParserTask();

                    // Start parsing the places in JSON format
                    // Invokes the "doInBackground()" method of the class ParseTask
                    parserTask.execute(result);
            }

    }

    /** A class to parse the Geocoding Places in non-ui thread */
	class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;
		
		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {
		
			List<HashMap<String, String>> places = null;			
			GeocodeJSONParser parser = new GeocodeJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            /** Getting the parsed data as a an ArrayList */
	            places = parser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return places;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){			
			
			// Clears all the existing markers			
			googleMap.clear();
			
			for(int i=0;i<list.size();i++){
			
				// Creating a marker
	            MarkerOptions markerOptions = new MarkerOptions();
	            
	            // Getting a place from the places list
	            HashMap<String, String> hmPlace = list.get(i);
	
	            // Getting latitude of the place
	            double lat = Double.parseDouble(hmPlace.get("lat"));	            
	            
	            // Getting longitude of the place
	            double lng = Double.parseDouble(hmPlace.get("lng"));
	            
	            // Getting name
	            String name = hmPlace.get("formatted_address");
	            
	            LatLng latLng = new LatLng(lat, lng);
	            
	            // Setting the position for the marker
	            markerOptions.position(latLng);
	            
	            // Setting the title for the marker
	            markerOptions.title(name);
	
	            // Placing a marker on the touched position
	            googleMap.addMarker(markerOptions);    
	            
	            // Locate the first location
                if(i==0)
                	googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }            
		}
	}
	
	
	
	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(getActivity())) {
			Log.v("Internet Not Conneted", "");
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Thread.currentThread().setPriority(1);
					p.cancel();
					Utils.same_id("Error", getString(R.string.no_internet),
							getActivity());
				}
			});
			return;
		} else {
			Log.v("Internet Conneted", "");
		}

		Thread t = new Thread(r);
		t.setName(r.getClass().getName());
		t.start();

	}
		
}
