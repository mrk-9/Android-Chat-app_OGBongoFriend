package com.ogbongefriends.com.ogbonge.fragment;

import java.io.InputStream;
import java.util.HashMap;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.EarnPointAPI;
import com.ogbongefriends.com.api.settingAccountApi;
import com.ogbongefriends.com.api.user_profile_api;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

public class CashForPayment extends Fragment{
private View rootView;
private Context _ctx;
private TextView about_points,basic_steps;
private Button proceed;
private user_profile_api user_profile_info;
private DB db;
private int convertPoints=0,remainingPoints=0,min_balance=1000;
private float amountPaybable=0;
private String points,points_to_money;
private CustomLoader p;
private Preferences pref;
private settingAccountApi setting_account_api;
private EarnPointAPI earn_point_api;
private EditText reference_id,current_points,convert_points,remaining_points,amount_paybable,bank_name,account_holder,account_number;
	public CashForPayment(){

	}
	public CashForPayment(Context ctx) {
	_ctx=ctx;
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.cash_for_payment, container, false);
		about_points=(TextView)rootView.findViewById(R.id.about_points);
			basic_steps=(TextView)rootView.findViewById(R.id.basic_steps);
			reference_id=(EditText)rootView.findViewById(R.id.reference_id);
			current_points=(EditText)rootView.findViewById(R.id.current_points);
			convert_points=(EditText)rootView.findViewById(R.id.convert_points);
			remaining_points=(EditText)rootView.findViewById(R.id.remaining_points);
			amount_paybable=(EditText)rootView.findViewById(R.id.amount_paybable);
			bank_name=(EditText)rootView.findViewById(R.id.bank_name);
			account_holder=(EditText)rootView.findViewById(R.id.account_holder);
			account_number=(EditText)rootView.findViewById(R.id.account_number);
			proceed=(Button)rootView.findViewById(R.id.proceed);
			remaining_points.setEnabled(false);
			amount_paybable.setEnabled(false);
			current_points.setEnabled(false);
			 p= DrawerActivity.p;
			 db=new DB(_ctx);
			 pref=new Preferences(_ctx);
			 
			 
			 convert_points.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					if(convert_points.getText().length()>0){
				calculateAmount(); 
					}
					else{
						remaining_points.setText("");
						amount_paybable.setText("");
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
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
						getReferenceId();
						super.onDone();
					
						
					}

					@Override
					protected void updateUI() {
						// TODO Auto-generated method stub
						showUserData();
						super.updateUI();
					}
					 
				 };
			
			 
			 
			 earn_point_api=new EarnPointAPI(_ctx, db, p){

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					p.cancel();
					
					if(EarnPointAPI.resCode==1 && EarnPointAPI.reference_id.length()>1){
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								if(EarnPointAPI.type.equalsIgnoreCase("1")){
							CashForPayment.this.reference_id.setText(EarnPointAPI.reference_id);
							current_points.setText(points);
							CashForPayment.this.reference_id.setEnabled(false);
								}
								
						if(EarnPointAPI.type.equalsIgnoreCase("2")){
							
							same_idBACK("Ogbonge", EarnPointAPI.resMsg, getActivity());
							
							//getActivity().onBackPressed();						
							}
						
							}
						});
					}
					super.onDone();
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
				}
				 
				 
			 };
			 



			 proceed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				if(convert_points.getText().length()>0){
					if(bank_name.getText().length()>0) {
						if(account_holder.getText().length()>0) {
							if(account_number.getText().length()>0) {
								HashMap<String, String> dataToProcess = new HashMap<String, String>();
								dataToProcess.put("uuid", pref.get(Constants.KeyUUID));
								dataToProcess.put("auth_token", pref.get(Constants.kAuthT));
								dataToProcess.put("type", "2");

								dataToProcess.put("convert_points", String.valueOf(convertPoints));
								dataToProcess.put("reference_id", reference_id.getText().toString());
								dataToProcess.put("gain_money", String.valueOf(amountPaybable));
								dataToProcess.put("bank_name", bank_name.getText().toString());
								dataToProcess.put("account_holder", account_holder.getText().toString());
								dataToProcess.put("account_number", account_number.getText().toString());
								p.show();
								earn_point_api.setPostData(dataToProcess);
								callApi(earn_point_api);
							}
							else{
								Utils.alert(_ctx, getString(R.string.account_number));
							}
						}
						else{
							Utils.alert(_ctx, getString(R.string.account_name));
						}
					}
					else {
						Utils.alert(_ctx, getString(R.string.bank_name));
					}
				}
				else{
					Utils.alert(_ctx, getString(R.string.convert_points));
				}

				}
			});
			 
			 
			 
			 
//			Spannable WordtoSpan = new SpannableString("Currently you have 8493 points in your Ogbonge's Account. Ogbonge system would be able to convert your Ogbonge's Points into Cash Money.");        
//			WordtoSpan.setSpan(new ForegroundColorSpan(R.color.green_btn), 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			about_points.setText(WordtoSpan);
			 getUserProfileFromApi();
			
			return rootView;
	}

	
	private void calculateAmount(){
		if(Integer.parseInt(points)>min_balance){
	convertPoints=Integer.parseInt(convert_points.getText().toString());
	remainingPoints=Integer.parseInt(points)-convertPoints;
	if(remainingPoints>=min_balance){
	amountPaybable=convertPoints*(Float.parseFloat(points_to_money));
	remaining_points.setText(""+remainingPoints);
	amount_paybable.setText(""+amountPaybable);
	}
	else{
		Utils.same_id("Ogbonge", "Ogbonge Will keep minimum of your "+ min_balance +" ogbonge points in your balance", _ctx);
		convert_points.setText("");
		remaining_points.setText("");
		amount_paybable.setText("");
	}
		}
		else{
			return;
		}
	}
	
	private void getUserProfileFromApi(){
		 p.show();
	     user_profile_info=new user_profile_api(_ctx, db, p){
			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				callApi(setting_account_api);
				
				
				super.onDone();
//			getActivity().runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					p.cancel();
//					init();
//					
//				}
//			});
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				
			}
	     };
	     
	     
	     HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("other_user_uuid","");
	     map.put("time_stamp", "");  
	     user_profile_info.setPostData(map);
	     callApi(user_profile_info);
	}

	
	private void getReferenceId(){
		
		
		
		HashMap<String, String>data=new HashMap<String, String>();
		data.put("uuid", pref.get(Constants.KeyUUID));
		data.put("auth_token", pref.get(Constants.kAuthT));
		data.put("convert_points", "");
		data.put("reference_id", "");
		data.put("gain_money", "");
		data.put("bank_name", "");
		data.put("account_holder", "");
		data.put("account_number", "");
		data.put("type", "1");
		
		p.show();
		earn_point_api.setPostData(data);
	     callApi(earn_point_api);
		
	}
	
	
	private void showUserData(){
		db.open();
	Cursor	data=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		Log.d("arv", "arv "+data.getCount());
		data.moveToFirst();
	points=data.getString(data.getColumnIndex(DB.Table.user_master.points.toString()));
	Spannable WordtoSpan=null;
	if(Integer.parseInt(points)>min_balance){
	 WordtoSpan = new SpannableString("Currently you have "+ points +" points in your Ogbonge's Account. Ogbonge system would be able to convert maximum of your "+ (Integer.parseInt(points)-min_balance) +" Ogbonge's Points into Cash Money Keeping minimum balance of "+ min_balance +" Ogbonge Points.");

	}
	else{
		WordtoSpan = new SpannableString("Currently you have "+ points +" points in your Ogbonge's Account. There is a need to keep minimum balance of "+ min_balance +" Ogbonge Points in your account to get Cash Money.");
	}

	WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 19, (19+points.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	about_points.setText(WordtoSpan);
	data=db.findCursor(DB.Table.Name.setting_master, DB.Table.setting_master.id.toString()+" = 1", null, null);
	 if(data.getCount()>0){
		data.moveToFirst();
		
		points_to_money=	data.getString(data.getColumnIndex(DB.Table.setting_master.point_to_money.toString()));
	 //amount_paybable.setText(points_to_money);
	 
	 }
	 
	 if(Integer.parseInt(points)>min_balance){
		 
		 convert_points.setEnabled(true);
		 bank_name.setEnabled(true);
		 account_holder.setEnabled(true);
		 account_number.setEnabled(true);
		 proceed.setEnabled(true);
	 }
	 else{
		
		 convert_points.setEnabled(false);
		 bank_name.setEnabled(false);
		 account_holder.setEnabled(false);
		 account_number.setEnabled(false);
		 proceed.setEnabled(false);
	 }
	 
		db.close();
		p.cancel();
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
	
public void same_idBACK(String title, String message, Context ctx) {
		
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        dialog.setContentView(R.layout.alert_dialog);
       TextView title_msg=(TextView)dialog.findViewById(R.id.title);
       TextView msg=(TextView)dialog.findViewById(R.id.message);
		Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
		
		
		ok_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				getActivity().onBackPressed();	
			}
		});
		
		title_msg.setText(title);
		msg.setText(message);
		//title_msg.setText("Confirmation..");
		//String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
		//msg.setText(str);
        dialog.show();

	}

}
