package com.ogbongefriends.com.ogbonge.backstage_album;

import java.io.InputStream;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ogbongefriends.com.ogbonge.photos.albums;
import com.ogbongefriends.com.ogbonge.profile.updateProfileApi;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	public class AlbumGallery extends Fragment implements OnClickListener{
		
		Circle_Gallery_Adapter circle_gallery_adapter;	
		
	// ******* DECLARING VARIABLES *******	
		TextView tv_loading;
	    String dest_file_path;
	    int downloadedSize = 0, totalsize;
	    String download_file_url ;
	    float per = 0;
	    private DB db;
		private updateProfileApi update_profile;
	    private TextView image_count;
	    private int commentCounter=0;
	    private Context _ctx;
	    private ImageView rate1,rate2,rate3,rate4,rate5,userImage;
	    private Gallery circle_gallery;
	    private CustomLoader p;
	    private int current_image=0;
	    private ProgressBar pb;
	    public int sel_pos=0;
	    private Cursor data;
	   
		Preferences pref;
		public AlbumGallery(){

		}
		
		public AlbumGallery (Context ctx){
			_ctx=ctx;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
					
			View rootView = inflater.inflate(R.layout.photo_of_you_gallery, container, false);		
			pref = new Preferences(getActivity());
			p= DrawerActivity.p;
			db=new DB(_ctx);
			image_count=(TextView)rootView.findViewById(R.id.img_count);
			rate1=(ImageView)rootView.findViewById(R.id.rate1_img);
			rate2=(ImageView)rootView.findViewById(R.id.rate2_img);
			rate3=(ImageView)rootView.findViewById(R.id.rate3_img);
			rate4=(ImageView)rootView.findViewById(R.id.rate4_img);
			rate5=(ImageView)rootView.findViewById(R.id.rate5_img);
			current_image=pref.getInt(Constants.SelectedImage);
			sel_pos = pref.getInt(Constants.SelectedImage);

			if(albums.myPhotoVos.size()>0){
				image_count.setText("1/"+albums.myPhotoVos.size());
			}
			else{
				image_count.setText("0/0");
			}

			((LinearLayout)rootView.findViewById(R.id.make_profile)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					setData();
				}
			});
			
			 circle_gallery = (Gallery)rootView.findViewById(R.id.circle_gallery);
			//ImageView selectedImage = (ImageView)rootView.findViewById(R.id.SingleView);
			circle_gallery.setSpacing(1);
			
			
			circle_gallery_adapter = new Circle_Gallery_Adapter(getActivity(), albums.myPhotoVos) {
				
				@Override
				protected void onItemClick(View v, String position) {



				}

				
				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
				
					return super.getItem(position);
				}


				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					image_count.setText((position+1)+"/"+albums.myPhotoVos.size());
					if(sel_pos!=position){
						sel_pos=position;

					}
					return super.getItemId(position);
				}


				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					
					
					return super.getView(position, convertView, parent);
				}
				
			};
			
			circle_gallery.setAdapter(circle_gallery_adapter);
			circle_gallery.setSelection(sel_pos);

			update_profile=new updateProfileApi(getActivity(), db, p){

				@Override
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
				}

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					p.cancel();
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
					p.cancel();
					//	Toast.makeText(_ctx, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
					//Utils.same_id("Message", "Profile Updated Successfully", getActivity());
					Utils.same_id("Message", "Profile Picture Updated Successfully", getActivity());

							((DrawerActivity)getActivity()).setprofile(albums.myPhotoVos.get(sel_pos).getPhoto_pic());


					//getActivity().onBackPressed();
				}
			};


			return rootView;
		}
		
		
			@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

		private void setData(){

			HashMap<String, String>data=new HashMap<String, String>();
			data.put("profile_pic", albums.myPhotoVos.get(sel_pos).getPhoto_pic());
			data.put("type", "1");
			hitAPI(data);
		}
		public void hitAPI(final HashMap<String, String> map) {

			p.show();
			update_profile.setPostData(map);
			callApi(update_profile);

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