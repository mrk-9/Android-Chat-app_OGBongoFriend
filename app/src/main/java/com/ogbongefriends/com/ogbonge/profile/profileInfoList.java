package com.ogbongefriends.com.ogbonge.profile;

import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.CustomLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

public class profileInfoList extends Fragment{
private View rootView;
private Context _ctx;
private ListView list;
private CustomLoader p;
private profileInfoAdapter profileInfoAdapter;
String[] dataToShow={"Basic Information","Contact Details","About Me","Interested In","Personal Information"};
public profileInfoList(Context ctx) {
	_ctx=ctx;
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	rootView = inflater.inflate(R.layout.update_profile_info, container, false);
	list=(ListView)rootView.findViewById(R.id.listView1);
	 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
	
	 profileInfoAdapter=new profileInfoAdapter(getActivity(),dataToShow) {
		
		@Override
		protected void onItemClick(View v, Integer string) {
			// TODO Auto-generated method stub
			Log.d("arv Position ", "arv"+string);
			if(string==1){
			}
			if(string==0){
			}
		}
	};
	list.setAdapter(profileInfoAdapter);
	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	
return 	rootView;
	

}


	

}
