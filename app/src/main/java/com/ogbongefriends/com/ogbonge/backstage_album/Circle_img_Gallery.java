package com.ogbongefriends.com.ogbonge.backstage_album;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.GetAllComment_of_Photo;
import com.ogbongefriends.com.api.GetPhotoRating;
import com.ogbongefriends.com.api.PhotoRating;
import com.ogbongefriends.com.api.getImageComments;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;


@SuppressLint("NewApi") @SuppressWarnings("deprecation")
public class Circle_img_Gallery extends Fragment implements OnClickListener{
	
	Circle_Gallery_Adapter circle_gallery_adapter;	
	
// ******* DECLARING VARIABLES *******	
	TextView tv_loading;
    String dest_file_path;
    int downloadedSize = 0, totalsize;
    String download_file_url ;
    private ArrayList<HashMap<String, String>> allCommentData,allRatingData;
    private HashMap<String, String>commentData,rating_data;
    float per = 0;
    private DB db;
    private TextView image_count,user_name,location,last_seen,commentCount,you_had_rated_it;
    private int commentCounter=0;
    private EditText comment_box,rate_text;
    private Button post_btn,rating_done;
    private Context _ctx;
    private ImageView rate1,rate2,rate3,rate4,rate5,userImage;
    private ListView commentList,rated_user_list;
    private Gallery circle_gallery;
    private CustomLoader p;
    private LinearLayout comment_parent,rateImageParent,rating_parent;
    private ImageView hide_dialog,r1,r2,r3,r4,r5,userRate1,userRate2,userRate3,userRate4,userRate5;
    private int current_image=0,youLiked=0;
    private Dialog dialog,ratingDialog,userLisrDialog;
    
    private getImageComments getImageComment;
    private HashMap<String, String>data_to_send;
    private GetAllComment_of_Photo getAllCommentFromPhoto;
    private GetPhotoRating getPhotorating;
    public int sel_pos=0;
    private Cursor data;
    private RatingBar ratingbar;
    private float selected_rating=0;
    private PhotoCommentAdapter photoCommentAdapter;
    private PhotoRating photo_rating_api;
    private float user_rating=0;
    private ProgressBar pb;
    private RatingUserAdapter ratingUserAdapter;
// ******* DECLARING CLASS OBJECTS *******	
	
	Preferences pref;

	public Circle_img_Gallery(){

	}
	public Circle_img_Gallery (Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
				
		View rootView = inflater.inflate(R.layout.circle_img_gallery, container, false);		
		pref=new Preferences(_ctx);
		p= DrawerActivity.p;
		db=new DB(_ctx);
		data_to_send=new HashMap<String, String>();
		allCommentData=new ArrayList<HashMap<String,String>>();
		allRatingData=new ArrayList<HashMap<String,String>>();
		
		image_count=(TextView)rootView.findViewById(R.id.img_count);
		rate1=(ImageView)rootView.findViewById(R.id.rate1_img);
		rate2=(ImageView)rootView.findViewById(R.id.rate2_img);
		rate3=(ImageView)rootView.findViewById(R.id.rate3_img);
		rate4=(ImageView)rootView.findViewById(R.id.rate4_img);
		rate5=(ImageView)rootView.findViewById(R.id.rate5_img);
		current_image=pref.getInt(Constants.SelectedImage);
		comment_parent=(LinearLayout)rootView.findViewById(R.id.comment_parent);
		rating_parent=(LinearLayout)rootView.findViewById(R.id.rating_parent);
		commentCount=(TextView)rootView.findViewById(R.id.comment_count);
		sel_pos = pref.getInt(Constants.SelectedImage);
		
		
		getAllCommentFromPhoto=new GetAllComment_of_Photo(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				
				//arvi
				if(getAllCommentFromPhoto.type==2){
					
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							 getPhgotoRating(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
						}
					});
			
				}
				else{
					
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pushCommentInList(getAllCommentFromPhoto.commentId);
						}
					});
					
				}
			}
			
		};
		
		
		getPhotorating=new GetPhotoRating(_ctx, db, p){

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
			
				
			
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				//arvi
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						p.cancel();
						callOnUpdaUI(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
					}
				})	;
				
				
			}
			
		};
		

		if(MyBackstageAlbum.myPhotoVo.size()>0){
			image_count.setText("1/"+MyBackstageAlbum.myPhotoVo.size());
		}
		else{
			image_count.setText("0/0");
		}
		
		 dialog = new Dialog(_ctx);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.comment_list);
		
			userImage=(ImageView)dialog.findViewById(R.id.userImage);
			user_name=(TextView)dialog.findViewById(R.id.userName);
			location=(TextView)dialog.findViewById(R.id.loc_name);
			last_seen=(TextView)dialog.findViewById(R.id.last_seen);
			commentList=(ListView)dialog.findViewById(R.id.listView1);
			comment_box=(EditText)dialog.findViewById(R.id.comment_box);
			post_btn=(Button)dialog.findViewById(R.id.post_btn);
			you_had_rated_it=(TextView)dialog.findViewById(R.id.you_had_rated_it);
			
			
			r1=(ImageView)dialog.findViewById(R.id.r1);
			r2=(ImageView)dialog.findViewById(R.id.r2);
			r3=(ImageView)dialog.findViewById(R.id.r3);
			r4=(ImageView)dialog.findViewById(R.id.r4);
			r5=(ImageView)dialog.findViewById(R.id.r5);
			
			
			userRate1=(ImageView)dialog.findViewById(R.id.rate1);
			userRate2=(ImageView)dialog.findViewById(R.id.rate2);
			userRate3=(ImageView)dialog.findViewById(R.id.rate3);
			userRate4=(ImageView)dialog.findViewById(R.id.rate4);
			userRate5=(ImageView)dialog.findViewById(R.id.rate5);
			
			pb=(ProgressBar)dialog.findViewById(R.id.userImageProgress);
			rateImageParent=(LinearLayout)dialog.findViewById(R.id.rateImageParent);
			
			setUserInfo();
			photo_rating_api=new PhotoRating(_ctx, db, p){

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							p.cancel();
							ratingDialog.dismiss();
							
							setStars(selected_rating);
							Utils.same_id("Message", "you rated successfuly", _ctx);
							callOnUpdaUI(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
								}
					});
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
				}
				
				  
			};
			rateImageParent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ratingDialog=new Dialog(_ctx);
					ratingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					ratingDialog.setContentView(R.layout.rating_layout);
					ratingbar=(RatingBar)ratingDialog.findViewById(R.id.ratingBar1);
					rating_done=(Button)ratingDialog.findViewById(R.id.rating_done);
					ratingDialog.show();
					
					rating_done.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							ratePhoto(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
						
						}
					});
					ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
						
						@Override
						public void onRatingChanged(RatingBar ratingBar, float rating,
								boolean fromUser) {
							// TODO Auto-generated method stub
							selected_rating=rating;
							Log.d("Value of rating ", ""+rating);
						}
					});
					
//					ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
//				            public void onRatingChanged(RatingBar ratingBar, float rating,
//				             boolean fromUser) {
//
//				             Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
//				           
//				            }
//				           });
					
				}
			});
			
			
			post_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(comment_box.getText().length()>0){
						p.show();
					
					HashMap<String, String>keydata=new HashMap<String, String>();
					keydata.put("uuid", pref.get(Constants.KeyUUID));
					keydata.put("auth_token", pref.get(Constants.kAuthT));
					keydata.put("photo_gallery_id",String.valueOf(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId()));
					keydata.put("time_stamp", "");
					keydata.put("type", "1");
					keydata.put("page_index", "1");
					keydata.put("comment",comment_box.getText().toString());
					
					getAllCommentFromPhoto.setPostData(keydata);
					callApi(getAllCommentFromPhoto);
					}
					else{
						Utils.same_id("Message", "Comment Can't be blank", _ctx);
					}
				}
			});
			
			ratingUserAdapter=new RatingUserAdapter(getActivity(),allRatingData) {
				
				@Override
				protected void onItemClick(View v, int event_id, int comment_id) {
					// TODO Auto-generated method stub
					
				}
			};
			
			
		
			photoCommentAdapter = new PhotoCommentAdapter(getActivity(), allCommentData) {
				@Override
				protected void onItemClick(View v, int event_id, int comment_id) {
					// TODO Auto-generated method stub
				}
			};
			commentList.setAdapter(photoCommentAdapter);
		//arvi
				getComments(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
			
			
			
		
			
			
			hide_dialog=(ImageView)dialog.findViewById(R.id.hide_dialog);
		 circle_gallery = (Gallery)rootView.findViewById(R.id.circle_gallery);
		//ImageView selectedImage = (ImageView)rootView.findViewById(R.id.SingleView);
		circle_gallery.setSpacing(1);
		
		
		circle_gallery_adapter = new Circle_Gallery_Adapter(getActivity(), MyBackstageAlbum.myPhotoVo) {
			
			@Override
			protected void onItemClick(View v, String position) {				
				
			}

			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
			Log.d("arvi ", "arvi position in getItem"+position);
				Log.d("arvi", "arvi getin getItem"+position);
				return super.getItem(position);
			}


			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				Log.d("arvi", "arvi getin itemId"+position);
				if(sel_pos!=position){
					sel_pos=position;
					//arvi
					Log.d("arvi ", "arvi getin getView"+position);
					getComments(MyBackstageAlbum.myPhotoVo.get(position).getId());
					}
				return super.getItemId(position);
			}


			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
		//		image_count.setText((position+1)+"/"+MyBackstageAlbum.myPhoto.size());
				
				return super.getView(position, convertView, parent);
			}
			
		};
		
		comment_parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showComments(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId());
				
				showUserRating(user_rating);
				dialog.show();
				data_to_send.clear();
			
			//	data_to_send.put("uuid", pref.get(Constants.KeyUUID));
			//	data_to_send.put("auth_token", pref.get(Constants.kAuthT));
		//		data_to_send.put("time_stamp", "");
				
				
			}
		});
		
		hide_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				p.cancel();
			}
		});
		
		circle_gallery.setAdapter(circle_gallery_adapter);
		circle_gallery.setSelection(sel_pos, true);
		circle_gallery_adapter.notifyDataSetChanged();
		
		
		return rootView;
	}
	
	public void showUserRating(float rating){
		
		if(rating>=0.0 && rating <0.4){
			userRate1.setImageResource(R.drawable.no_rating);
			userRate2.setImageResource(R.drawable.no_rating);
			userRate3.setImageResource(R.drawable.no_rating);
			userRate4.setImageResource(R.drawable.no_rating);
			userRate5.setImageResource(R.drawable.no_rating);

			}
		
if(rating>0.0 && rating<=0.5){
userRate1.setImageResource(R.drawable.small_half_star);
userRate2.setImageResource(R.drawable.no_rating);
userRate3.setImageResource(R.drawable.no_rating);
userRate4.setImageResource(R.drawable.no_rating);
userRate5.setImageResource(R.drawable.no_rating);

}

if(rating>0.5 && rating<=1.0){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.no_rating);
	userRate3.setImageResource(R.drawable.no_rating);
	userRate4.setImageResource(R.drawable.no_rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>1.0 && rating<=1.5){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.small_half_star);
	userRate3.setImageResource(R.drawable.no_rating);
	userRate4.setImageResource(R.drawable.no_rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>1.5 && rating<=2.0){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.no_rating);
	userRate4.setImageResource(R.drawable.no_rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>2.0 && rating<=2.5){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.small_half_star);
	userRate4.setImageResource(R.drawable.no_rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>2.5 && rating<=3.0){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.rating);
	userRate4.setImageResource(R.drawable.no_rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>3.0 && rating<=3.5){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.rating);
	userRate4.setImageResource(R.drawable.small_half_star);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>3.5 && rating<=4.0){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.rating);
	userRate4.setImageResource(R.drawable.rating);
	userRate5.setImageResource(R.drawable.no_rating);
}
if(rating>4.0 && rating<=4.5){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.rating);
	userRate4.setImageResource(R.drawable.rating);
	userRate5.setImageResource(R.drawable.small_half_star);
}
if(rating>4.5){
	userRate1.setImageResource(R.drawable.rating);
	userRate2.setImageResource(R.drawable.rating);
	userRate3.setImageResource(R.drawable.rating);
	userRate4.setImageResource(R.drawable.rating);
	userRate5.setImageResource(R.drawable.rating);
}
}
	
	public void ratePhoto(int photo_id){
		
		HashMap<String, String>data_to_send=new HashMap<String, String>();
		
		p.show();
		data_to_send.put("uuid", pref.get(Constants.KeyUUID));
		data_to_send.put("auth_token", pref.get(Constants.kAuthT));
		data_to_send.put("photo_gallery_id",String.valueOf(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId()) );
		data_to_send.put("time_stamp", "");
		data_to_send.put("rating",String.valueOf(selected_rating));
		data_to_send.put("type", "1");
		data_to_send.put("page_index", "");
		photo_rating_api.setPostData(data_to_send);
		callApi(photo_rating_api);
		
	}
	
	
	public void setStars(float rating){

		if(rating>=0.0 && rating <0.4){
			r1.setImageResource(R.drawable.no_rating);
			r2.setImageResource(R.drawable.no_rating);
			r3.setImageResource(R.drawable.no_rating);
			r4.setImageResource(R.drawable.no_rating);
			r5.setImageResource(R.drawable.no_rating);

			}
		
if(rating>0.0 && rating<=0.5){
r1.setImageResource(R.drawable.small_half_star);
r2.setImageResource(R.drawable.no_rating);
r3.setImageResource(R.drawable.no_rating);
r4.setImageResource(R.drawable.no_rating);
r5.setImageResource(R.drawable.no_rating);

}

if(rating>0.5 && rating<=1.0){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.no_rating);
	r3.setImageResource(R.drawable.no_rating);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>1.0 && rating<=1.5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.small_half_star);
	r3.setImageResource(R.drawable.no_rating);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>1.5 && rating<=2.0){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.no_rating);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>2.0 && rating<=2.5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.small_half_star);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>2.5 && rating<=3.0){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.no_rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>3.0 && rating<=3.5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.small_half_star);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>3.5 && rating<=4.0){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.rating);
	r5.setImageResource(R.drawable.no_rating);
}
if(rating>4.0 && rating<=4.5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.rating);
	r5.setImageResource(R.drawable.small_half_star);
}
if(rating>4.5){
	r1.setImageResource(R.drawable.rating);
	r2.setImageResource(R.drawable.rating);
	r3.setImageResource(R.drawable.rating);
	r4.setImageResource(R.drawable.rating);
	r5.setImageResource(R.drawable.rating);
}
}


	public void setratingStars(float rating){
		
		
		if(rating>=0.0 && rating <0.4){
			rate1.setImageResource(R.drawable.no_rating);
			rate2.setImageResource(R.drawable.no_rating);
			rate3.setImageResource(R.drawable.no_rating);
			rate4.setImageResource(R.drawable.no_rating);
			rate5.setImageResource(R.drawable.no_rating);

			}
		
		
if(rating>0.0 && rating<=0.5){
rate1.setImageResource(R.drawable.small_half_star);
rate2.setImageResource(R.drawable.no_rating);
rate3.setImageResource(R.drawable.no_rating);
rate4.setImageResource(R.drawable.no_rating);
rate5.setImageResource(R.drawable.no_rating);

}

if(rating>0.5 && rating<=1.0){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.no_rating);
	rate3.setImageResource(R.drawable.no_rating);
	rate4.setImageResource(R.drawable.no_rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>1.0 && rating<=1.5){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.small_half_star);
	rate3.setImageResource(R.drawable.no_rating);
	rate4.setImageResource(R.drawable.no_rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>1.5 && rating<=2.0){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.no_rating);
	rate4.setImageResource(R.drawable.no_rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>2.0 && rating<=2.5){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.small_half_star);
	rate4.setImageResource(R.drawable.no_rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>2.5 && rating<=3.0){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.rating);
	rate4.setImageResource(R.drawable.no_rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>3.0 && rating<=3.5){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.rating);
	rate4.setImageResource(R.drawable.small_half_star);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>3.5 && rating<=4.0){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.rating);
	rate4.setImageResource(R.drawable.rating);
	rate5.setImageResource(R.drawable.no_rating);
}
if(rating>4.0 && rating<=4.5){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.rating);
	rate4.setImageResource(R.drawable.rating);
	rate5.setImageResource(R.drawable.small_half_star);
}
if(rating>4.5){
	rate1.setImageResource(R.drawable.rating);
	rate2.setImageResource(R.drawable.rating);
	rate3.setImageResource(R.drawable.rating);
	rate4.setImageResource(R.drawable.rating);
rate5.setImageResource(R.drawable.rating);
}
}
	
	
	public void callOnUpdaUI(final int photo_gallery_id){
		
		db.open();
		
		data=db.findCursor(DB.Table.Name.photo_comment, DB.Table.photo_comment.photo_gallery_id+" = "+photo_gallery_id, null, null);
		Log.d("Comnet data", ""+data.getCount());
		commentCounter=data.getCount();
		commentCount.setText(data.getCount()+" Comments");
		
		data=db.findCursor(DB.Table.Name.photo_rating, DB.Table.photo_rating.photo_gallery_id.toString()+" = "+photo_gallery_id+" AND "+ DB.Table.photo_rating.user_master_id.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		if(data.getCount()>0){
		data.moveToFirst();
		setratingStars(Float.parseFloat(data.getString(data.getColumnIndex(DB.Table.photo_rating.rating.toString()))));
		setStars(Float.parseFloat(data.getString(data.getColumnIndex(DB.Table.photo_rating.rating.toString()))));
		rateImageParent.setEnabled(false);
		youLiked=1;
		you_had_rated_it.setText("You Rated It");
		data=db.findCursor(DB.Table.Name.photo_rating, DB.Table.photo_rating.photo_gallery_id.toString()+" = "+photo_gallery_id, null, null);
		if(data.getCount()>1){
			you_had_rated_it.setText("You and "+(data.getCount()-1)+" other user rated it");

		}
		
	}
		
		else{
			data=db.findCursor(DB.Table.Name.photo_rating, DB.Table.photo_rating.photo_gallery_id.toString()+" = "+photo_gallery_id, null, null);
			if(data.getCount()>1){
				you_had_rated_it.setText((data.getCount()-1)+"other user rated it");
			}
		}
		you_had_rated_it.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userLisrDialog=new Dialog(_ctx);
				userLisrDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				userLisrDialog.setContentView(R.layout.list);
				
				rated_user_list=(ListView)userLisrDialog.findViewById(R.id.listView1);
				data=db.findCursor(DB.Table.Name.photo_rating, DB.Table.photo_rating.photo_gallery_id.toString()+" = "+photo_gallery_id, null, null);
				data.moveToFirst();
				for(int i=0;i<data.getCount();i++){
					
					rating_data=new HashMap<String, String>();
					rating_data.put(DB.Table.photo_rating.rating.toString(), data.getString(data.getColumnIndex(DB.Table.photo_rating.rating.toString())));
					rating_data.put(DB.Table.photo_rating.add_date.toString(), CelUtils.dateDifrence(Long.parseLong(data.getString(data.getColumnIndex(DB.Table.photo_rating.add_date.toString())))));
					
				Cursor temp=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+data.getString(data.getColumnIndex(DB.Table.photo_comment.user_master_id.toString()))+"'", null, null);
					if(temp.moveToFirst()){
						rating_data.put(DB.Table.user_master.profile_pic.toString(), getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.profile_pic.toString())));
						rating_data.put(DB.Table.user_master.first_name.toString(), temp.getString(temp.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+temp.getString(temp.getColumnIndex(DB.Table.user_master.last_name.toString()))+","+temp.getString(temp.getColumnIndex(DB.Table.user_master.last_name.toString())));
				
					}
				allRatingData.add(rating_data);
				data.moveToNext();
				}
				ratingUserAdapter.notifyDataSetChanged();
				
				rated_user_list.setAdapter(ratingUserAdapter);
				ratingUserAdapter.notifyDataSetChanged();
				userLisrDialog.show();
			}
		});
		
		
		}


	
	
	public void getComments(final int photo_gallery_id){
		p.show();
//		db.open();
//		
//		data=db.findCursor(Table.Name.photo_comment, Table.photo_comment.photo_gallery_id+" = "+photo_gallery_id, null, null);
//		Log.d("Comnet data", ""+data.getCount());
		
		
		
		HashMap<String, String>keydata=new HashMap<String, String>();
		keydata.put("uuid", pref.get(Constants.KeyUUID));
		keydata.put("auth_token", pref.get(Constants.kAuthT));
		keydata.put("photo_gallery_id",String.valueOf(photo_gallery_id) );
		keydata.put("time_stamp", "");
		keydata.put("type", "2");
		keydata.put("page_index", "1");
		keydata.put("comment", "");
		
		getAllCommentFromPhoto.setPostData(keydata);
		callApi(getAllCommentFromPhoto);
		
	}
	
	
	public void getPhgotoRating(int photoId){
		
		HashMap<String, String>keydata=new HashMap<String, String>();
		keydata.put("uuid", pref.get(Constants.KeyUUID));
		keydata.put("auth_token", pref.get(Constants.kAuthT));
		keydata.put("photo_gallery_id",String.valueOf(photoId) );
		keydata.put("time_stamp", "");
		keydata.put("type", "2");
		keydata.put("page_index", "1");
		keydata.put("rating", "");
		getPhotorating.setPostData(keydata);
		callApi(getPhotorating);
		
	}
	
	public void pushCommentInList(int comment_id){
		db.open();
		HashMap<String, String>putData=new HashMap<String, String>();
		HashMap<String, String>dataToInsert=new HashMap<String, String>();
		putData.put(DB.Table.photo_comment.comment.toString(), comment_box.getText().toString());
		dataToInsert.put(DB.Table.photo_comment.comment.toString(), comment_box.getText().toString());
		dataToInsert.put(DB.Table.photo_comment.id.toString(), String.valueOf(comment_id));
		dataToInsert.put(DB.Table.photo_comment.user_master_id.toString(), pref.get(Constants.KeyUUID));
		dataToInsert.put(DB.Table.photo_comment.photo_gallery_id.toString(), String.valueOf(MyBackstageAlbum.myPhotoVo.get(sel_pos).getId()));
		dataToInsert.put(DB.Table.photo_comment.add_date.toString(), String.valueOf(CelUtils.getmiliTimeStamp()));
		dataToInsert.put(DB.Table.photo_comment.modify_date.toString(), String.valueOf(CelUtils.getmiliTimeStamp()));
		db.insert(DB.Table.Name.photo_comment, dataToInsert);
		putData.put(DB.Table.user_master.last_seen.toString(), CelUtils.dateDifrence(CelUtils.getmiliTimeStamp()));
		Cursor	temp=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		temp.moveToFirst();
		putData.put(DB.Table.user_master.profile_pic.toString(), getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.profile_pic.toString())));
		putData.put(DB.Table.user_master.first_name.toString(), temp.getString(temp.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+temp.getString(temp.getColumnIndex(DB.Table.user_master.last_name.toString())));
		allCommentData.add(0, putData);
		photoCommentAdapter.notifyDataSetChanged();
		comment_box.setText("");
		commentCounter++;
		commentCount.setText(commentCounter+" Comments");
		p.cancel();
		db.close();
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
	
	
	
	public void showComments(int pId){
		db.open();
		Cursor temp;
		allCommentData.clear();
		data=db.findCursor(DB.Table.Name.photo_comment, DB.Table.photo_comment.photo_gallery_id+" = "+pId, null, "ORDER BY add_date");
		data.moveToLast();
			for(int i=data.getCount()-1;i>=0;i--){
			commentData=new HashMap<String, String>();
			commentData.put(DB.Table.photo_comment.comment.toString(), data.getString(data.getColumnIndex(DB.Table.photo_comment.comment.toString())));
			commentData.put(DB.Table.user_master.last_seen.toString(), CelUtils.dateDifrence(Long.parseLong(data.getString(data.getColumnIndex(DB.Table.photo_comment.modify_date.toString())))));
			
			temp=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+data.getString(data.getColumnIndex(DB.Table.photo_comment.user_master_id.toString()))+"'", null, null);
			if(temp.moveToFirst()){
			commentData.put(DB.Table.user_master.profile_pic.toString(), getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+temp.getString(temp.getColumnIndex(DB.Table.user_master.profile_pic.toString())));
			commentData.put(DB.Table.user_master.first_name.toString(), temp.getString(temp.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+temp.getString(temp.getColumnIndex(DB.Table.user_master.last_name.toString())));
		}
		allCommentData.add(commentData);
		data.moveToPrevious();
		}
		photoCommentAdapter.notifyDataSetChanged();
	}
	
	
	public void setUserInfo(){
		db.open();
		
		final Cursor	temp_user=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.KeyUUID)+"'", null, null);
		
		Log.d("arv", "arv"+temp_user.getCount()+"   "+pref.get(Constants.KeyUUID));
		
		temp_user.moveToFirst();
		Log.d("arv", "arv First" + temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.first_name.toString())));
		Log.d("arv", "arv Rirst "+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_name.toString())));
		
		user_name.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.first_name.toString())) + " "
				+ temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_name.toString())) + ", "
				+ CelUtils.getAgeStrFromDOB(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.date_of_birth.toString()))));
		location.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.location.toString())));
		last_seen.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_seen.toString())));
		user_rating=temp_user.getFloat(temp_user.getColumnIndex(DB.Table.user_master.rating.toString()));


		Glide.with(_ctx).load(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.profile_pic.toString()))).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).thumbnail(0.01f).into(userImage);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}	
	
		
}