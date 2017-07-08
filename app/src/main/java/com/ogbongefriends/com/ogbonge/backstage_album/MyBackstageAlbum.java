package com.ogbongefriends.com.ogbonge.backstage_album;

	import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

	import android.annotation.SuppressLint;
	import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
	import android.database.Cursor;
import android.location.Location;
	import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
	import android.widget.SeekBar;
import android.widget.TextView;
	import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

	import com.ogbongefriends.com.ogbonge.Vos.PhotoOfYouVO;
	import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
	import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DB.DB.Table;
import com.ogbongefriends.com.api.BackStageAlbumUpdate;
import com.ogbongefriends.com.api.DelPhotoAlbumApi;
import com.ogbongefriends.com.api.getAlbumApi;
	import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
	import com.ogbongefriends.com.common.Preferences;
	import com.ogbongefriends.com.common.Utils;

@SuppressLint("NewApi") public class MyBackstageAlbum extends Fragment implements Runnable,MyLocationListener,OnClickListener,OnCheckedChangeListener {

		// private ListView listView;
		@SuppressWarnings("unused")
		private EditText posttetx,album_name;
		@SuppressWarnings("unused")
		private Button post;
		Cursor data;
		Cursor eventdatacorsor;
		Cursor followerdatacorsor;
		Cursor followingdatacorsor;
		Cursor secfollowingdatacorsor;
		Preferences pref;
	private Context _ctx;
	private CustomLoader p;
	private DB db;
	 private Dialog dialog;
	private View rootView;
	private GridView my_photos;
	private BackstageAdapter backstageAdapter;
		static ArrayList<PhotoOfYouVO>myPhotoVo=new ArrayList<PhotoOfYouVO>();
	/*static ArrayList<String>myPhoto=new ArrayList<String>();
	public static boolean[] checkStatus;
	static ArrayList<Integer>myPhoto_id=new ArrayList<Integer>();*/
	
	private TextView photo_of_user,private_photos_text,album_rate;
	private String server_id,userName,del_item;
	private getAlbumApi getAlbumApi;
	private DelPhotoAlbumApi del_photo_album_api;
	private String del_photo_id="";
	private SeekBar points;
	private Button add_new_photo,del_album,rate_album,save,cancel;
	private BackStageAlbumUpdate backStageAlbumUpdate;
	private String album_points="",album_title="",album_points_temp="";
		
		public MyBackstageAlbum(Context ctx) {
			_ctx=ctx;
		}
		public MyBackstageAlbum() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
			p=DrawerActivity.p; 
			pref=new Preferences(_ctx);
				db=new DB(_ctx);
			p.show();
			myPhotoVo.clear();
				Cursor c=db.findCursor(Table.Name.user_master, Table.user_master.uuid+" = "+"'"+ pref.get(Constants.KeyUUID) +"'", null, null);
				c.moveToFirst();
				 server_id=c.getString(c.getColumnIndex(Table.user_master.server_id.toString()));
				 userName=c.getString(c.getColumnIndex(Table.user_master.first_name.toString()))+" "+c.getString(c.getColumnIndex(Table.user_master.first_name.toString()));
			rootView = inflater.inflate(R.layout.my_backstage_album, container, false);
			my_photos=(GridView)rootView.findViewById(R.id.my_photos);
		   add_new_photo=(Button)rootView.findViewById(R.id.add_new_photo);
			del_album=(Button)rootView.findViewById(R.id.del_album);
			rate_album=(Button)rootView.findViewById(R.id.rate_album);
			
			add_new_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pref.set("upload_type", 2);
					pref.commit();
					((DrawerActivity) getActivity()).displayView(56);
				}
			});
			
			backstageAdapter=new BackstageAdapter(_ctx,myPhotoVo,server_id) {

				@Override
				protected void onItemClick(View v, int string, String path) {
					// TODO Auto-generated method stub
					pref.set(Constants.SelectedImage, string);
					
					pref.commit();	
					((DrawerActivity) getActivity()).displayView(35);
				}

				@Override
				protected void onLongItemClick(View v, int string, String path) {
					// TODO Auto-generated method stub
				Log.d("Arv", "clicked on "+path);
				del_photo_id=path;
				}
			};
			
		
				rate_album.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					 dialog = new Dialog(_ctx);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.update_backstage_album);
						points=(SeekBar)dialog.findViewById(R.id.points_bar);
						album_rate=(TextView)dialog.findViewById(R.id.album_rate);
						album_name=(EditText)dialog.findViewById(R.id.album_name);
						save=(Button)dialog.findViewById(R.id.save);
						cancel=(Button)dialog.findViewById(R.id.cancel);
						album_name.setText(""+album_title);
						points.setProgress(Integer.parseInt(album_points));
						album_points_temp=album_points;
						album_rate.setText("Points:"+album_points);
						cancel.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						
						save.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							
								backStageAlbumUpdate=new BackStageAlbumUpdate(_ctx, db, p){

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
						p.cancel();
							album_points=album_points_temp;
							album_title=album_name.getText().toString();
							dialog.dismiss();
						}
					});
										
									}
									
									
								};
								
							
								 HashMap<String, String>map=new HashMap<String, String>();
							     map.put("uuid", pref.get(Constants.KeyUUID));
							     map.put("auth_token", pref.get(Constants.kAuthT));
							     map.put("point_value",album_points_temp); 
							     map.put("album_title",album_name.getText().toString()); 
							     map.put("type","2"); 
							   
							   
							    p.show();
							     backStageAlbumUpdate.setPostData(map);
							     callApi(backStageAlbumUpdate);
								
							}
						});
						
						
						points.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
							int progressChanged = 0;

							public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
								progressChanged = progress;
								album_points_temp=String.valueOf(progress);
								album_rate.setText("Points:"+progress);
							}

							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
							}

							public void onStopTrackingTouch(SeekBar seekBar) {
//								Toast.makeText(SeekbarActivity.this, "seek bar progress: " + progressChanged, Toast.LENGTH_SHORT)
//										.show();
							}
						});
						
//						userImage=(ImageView)dialog.findViewById(R.id.userImage);
//						user_name=(TextView)dialog.findViewById(R.id.userName);
//						location=(TextView)dialog.findViewById(R.id.loc_name);
//						last_seen=(TextView)dialog.findViewById(R.id.last_seen);
//						commentList=(ListView)dialog.findViewById(R.id.listView1);
//						comment_box=(EditText)dialog.findViewById(R.id.comment_box);
//						post_btn=(Button)dialog.findViewById(R.id.post_btn);
					
						dialog.show();
				}
			});
			
			
			
//			del_photos.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {}
//			});
			
			
			del_photo_album_api=new DelPhotoAlbumApi(_ctx, db, p){

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
							p.cancel();
							
							Utils.same_id("Message", del_photo_album_api.resMsg, _ctx);
							//sff
							((DrawerActivity) getActivity()).displayView(13);
							
						}
					});
				}
				
				
			};
			
			
		
			
			
			del_album.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					delPhotoes();
//						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);
//
//						// set title
//						alertDialogBuilder.setTitle(getString(R.string.app_name));
//
//						// set dialog message
//						
//						String str="Are you sure want to delete your Backstage Album ?";
//						alertDialogBuilder.setMessage(str).setCancelable(false)
//						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//							
//
////								HashMap<String, String>map=new HashMap<String, String>();
////							     map.put("uuid", pref.get(Constants.KeyUUID));
////							     map.put("auth_token", pref.get(Constants.kAuthT));
////							     map.put("photo_id",""); 
////							     map.put("album_type","2"); 
////							     
////							     
////							   
////							     p.show();
////							     del_photo_album_api.setPostData(map);
////							     callApi(del_photo_album_api);
//								
//								
//								
//								
//							}
//						})
//						.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//							
//							public void onClick(DialogInterface dialog, int id) {
//								// if this button is clicked, close
//								dialog.dismiss();
//								// UpdateProfile.this.finish();
//							}
//						});
//						
//
//						// create alert dialog
//						AlertDialog alertDialog = alertDialogBuilder.create();
//						
//						// show it
//						alertDialog.show();
						
					
				}
			});
			
			my_photos.setAdapter(backstageAdapter);
			
			ViewGroup.LayoutParams layoutParams = my_photos.getLayoutParams();
			//layoutParams.height = 50*urls.length; //this is in pixels
			
			
			my_photos.setLayoutParams(layoutParams);
			getAlbums();
			
		return 	rootView;
			}

		
	public void	getBackStageAlbumInfo(){
			
			
			backStageAlbumUpdate=new BackStageAlbumUpdate(_ctx, db, p){

				@Override
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
				}

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					getAlbumData();
				}
				
				
			};
			
		
			 HashMap<String, String>map=new HashMap<String, String>();
		     map.put("uuid", pref.get(Constants.KeyUUID));
		     map.put("auth_token", pref.get(Constants.kAuthT));
		     map.put("point_value",""); 
		     map.put("album_title",""); 
		     map.put("type","1"); 
		   
		   
		   //  p.show();
		     backStageAlbumUpdate.setPostData(map);
		     callApi(backStageAlbumUpdate);
			
		}
		
	
	public void getAlbumData(){
		db.open();
		Cursor data=db.findCursor(Table.Name.photo_album_master,Table.photo_album_master.user_master_id.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null,null);
		data.moveToFirst();
		album_points=data.getString(data.getColumnIndex(Table.photo_album_master.point_value.toString()));
		album_points_temp=album_points;
		album_title=data.getString(data.getColumnIndex(Table.photo_album_master.album_title.toString()));
		p.cancel();
	}
		
	
	private void delPhotoes(){
		

		// TODO Auto-generated method stub
		boolean test=false;
		del_item="";
		for(int i=0; i<myPhotoVo.size();i++){
			
			if(myPhotoVo.get(i).isChecked()==true){
				if(test==false){
					test=true;
				del_item=del_item.concat(""+myPhotoVo.get(i).getId());
				
				
				}
				else{
					del_item=del_item.concat(","+myPhotoVo.get(i).getId());
				}
			}
			
		}
		
		if(del_item.length()>1){
			callPropmtForDelete(del_item.length());
			
		}
		else{
			Utils.same_id("Message", "Please select at least 1 photo to delete.", _ctx);
		}
		Log.d("arvinddd", "arvinn"+del_item);
	
	}
	
		private void callPropmtForDelete(int len){
			
			String str="";
		//	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);

		   final Dialog dialog = new Dialog(_ctx);
		   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
           dialog.setContentView(R.layout.confirmation_dialog);
           TextView title=(TextView)dialog.findViewById(R.id.title);
           TextView msg=(TextView)dialog.findViewById(R.id.message);
			Button ok_btn=(Button)dialog.findViewById(R.id.ok_btn);
			Button cancel_btn=(Button)dialog.findViewById(R.id.cancel_btn);
			title.setText("Confirmation..");
			if(len>1){
				 str="Are you sure want to delete selected photos from your backstage?";
			}
			else{
				str="Are you sure want to delete selected photo from your backstage?";
			}
			msg.setText(str);
            dialog.show();
            cancel_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
            
            ok_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					 HashMap<String, String>map=new HashMap<String, String>();
				     map.put("uuid", pref.get(Constants.KeyUUID));
				     map.put("auth_token", pref.get(Constants.kAuthT));
				     map.put("photo_id",del_item); 
				     map.put("album_type",""); 
				     
				     
				   
				     p.show();
				     del_photo_album_api.setPostData(map);
				     callApi(del_photo_album_api);
				}
			});
		
			// show it
            dialog.show();
			
			
			
		}
		
		public void getAlbums(){
			
			getAlbumApi=new getAlbumApi(_ctx, db, p){

				@Override
				protected void onResponseReceived(InputStream is) {
					// TODO Auto-generated method stub
					super.onResponseReceived(is);
					p.cancel();
					myPhotoVo.clear();

					if(getAlbumApi.backstage_photos.size()>0){
						for(int i=0;i<getAlbumApi.backstage_photos.size();i++){
						//	JsonObject json=getAlbumApi.backstage_photos.get(i).getAsJsonObject();
						PhotoOfYouVO photoOfYouVO=new PhotoOfYouVO(getAlbumApi.backstage_photos.get(i).getAsJsonObject());
						photoOfYouVO.setfullPhotoUrl(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+server_id+"/backstage_photos/"+photoOfYouVO.getPhoto_pic());
						myPhotoVo.add(photoOfYouVO);
						}
					}
				}

				@Override
				protected void onError(Exception e) {
					// TODO Auto-generated method stub
					super.onError(e);
				}

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					getBackStageAlbumInfo();
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					
					super.updateUI();
					p.cancel();
					backstageAdapter.notifyDataSetChanged();
				}
				
			};
			HashMap<String, String>keydata=new HashMap<String, String>();
			keydata.put("uuid", pref.get(Constants.KeyUUID));
			keydata.put("auth_token", pref.get(Constants.kAuthT));
			keydata.put("other_uuid", "");
			keydata.put("time_stamp", "");
			
			getAlbumApi.setPostData(keydata);
			callApi(getAlbumApi);
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
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLocationUpdate(Location location) {
			// TODO Auto-generated method stub
			
		}	
		
		
		
		
	}


