package com.ogbongefriends.com.ogbonge.payment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.api.EarnPointAPI;
import com.ogbongefriends.com.common.Preferences;

public class transactionDetails extends Fragment implements Runnable,MyLocationListener {

	// private ListView listView;
	private Button back;
	Preferences pref;
	View rootView;
	int transaction_counter;
	private Context _ctx;
	private TextView amount,point_gain,tran_status,transaction_cur,transaction_ref,payment_ref,tran_date,tran_mode;
	int count = 0;
  @SuppressLint("NewApi") public HashMap<String, String> urls;
  
	public transactionDetails(Context ctx) {
		_ctx=ctx;
	}

	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.transaction_details, container, false);
		amount=(TextView)rootView.findViewById(R.id.amount_d);
		point_gain=(TextView)rootView.findViewById(R.id.point_gain);
		tran_status=(TextView)rootView.findViewById(R.id.tran_status);
		transaction_cur=(TextView)rootView.findViewById(R.id.transaction_cur);
		transaction_ref=(TextView)rootView.findViewById(R.id.transaction_ref);
		payment_ref=(TextView)rootView.findViewById(R.id.payment_ref);
		tran_date=(TextView)rootView.findViewById(R.id.tran_date);
		tran_mode=(TextView)rootView.findViewById(R.id.tran_mode);
		back=(Button)rootView.findViewById(R.id.back);

		
		pref=new Preferences(_ctx);
		transaction_counter=pref.getInt("transaction_idex");
		
		showTransactionDetails(EarnPointAPI.transaction_array,transaction_counter);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			getActivity().onBackPressed();	
			}
		});
		
		
	return 	rootView;
		

	}
	
	
	private void showTransactionDetails(JsonArray transactionData, int count)
	{
		if(transactionData.size()>0){
		JsonObject data=transactionData.get(count).getAsJsonObject();
		
		 	amount.setText(data.get("payment_amount").getAsString());
		 	point_gain.setText(data.get("total_points").getAsString());
		 	tran_status.setText(data.get("payment_status").getAsString());
		 	transaction_cur.setText("₦");
		 	transaction_ref.setText(data.get("txn_ref").getAsString());
		 	payment_ref.setText(data.get("pay_ref").getAsString());
		 	tran_date.setText(getDate(Long.parseLong(data.get("transaction_date").getAsString())*1000, "dd/MM/yyyy hh:mm:ss.SSS"));
		 	tran_mode.setText(data.get("payment_details").getAsString());
		 	
		
		}
	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}	
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}

	
}
