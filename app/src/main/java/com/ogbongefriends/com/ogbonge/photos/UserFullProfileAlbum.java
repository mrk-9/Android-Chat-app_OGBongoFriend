package com.ogbongefriends.com.ogbonge.photos;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.backstage_album.Circle_Gallery_Adapter;
import com.ogbongefriends.com.ogbonge.backstage_album.PhotoCommentAdapter;
import com.ogbongefriends.com.ogbonge.profile.full_users_profile;
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
import com.ogbongefriends.com.emoj.EmojiconGridFragment;
import com.ogbongefriends.com.emoj.element.Emojicon;

public class UserFullProfileAlbum extends Fragment implements OnClickListener,EmojiconGridFragment.OnEmojiconClickedListener,
EmojiconsFragment.OnEmojiconBackspaceClickedListener{
	
	Circle_Gallery_Adapter circle_gallery_adapter;
	
// ******* DECLARING VARIABLES *******	
	TextView tv_loading;
    String dest_file_path;
    int downloadedSize = 0, totalsize;
    String download_file_url ;
    private ArrayList<HashMap<String, String>> allCommentData;
    private HashMap<String, String>commentData;
    float per = 0;
    private DB db;
    private ImageLoader imageLoader;
    private TextView image_count,user_name,location,last_seen,commentCount,you_had_rated_it;
    private int commentCounter=0;
    private float ratingCounter=0;
    private EditText comment_box;
    private Button post_btn,rating_done;
    private Context _ctx;
    private ImageView rate1,rate2,rate3,rate4,rate5,userImage,drate1,drate2,drate3,drate4,drate5;
    private ListView commentList;
    private Gallery circle_gallery;
    private CustomLoader p;
    private LinearLayout comment_parent,rateImageParent,rating_parent;
    private ImageView hide_dialog,r1,r2,r3,r4,r5;
    private int current_image=0;
    private Dialog dialog,ratingDialog;
    private ProgressBar pb;
    private getImageComments getImageComment;
    private HashMap<String, String>data_to_send;
    private DisplayImageOptions options;
    private GetAllComment_of_Photo getAllCommentFromPhoto;
    private GetPhotoRating getPhotorating;
    public int sel_pos=0;
    private PhotoRating photo_rating_api;
    private Cursor data;
    private RatingBar ratingbar;
    private float selected_rating=0;
    private PhotoCommentAdapter photoCommentAdapter;
// ******* DECLARING CLASS OBJECTS *******	
	
	Preferences pref;

	
	public  UserFullProfileAlbum (Context ctx){
		_ctx=ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
				
		View rootView = inflater.inflate(R.layout.circle_img_gallery, container, false);		
		pref = new Preferences(getActivity());
		p= DrawerActivity.p;
		db=new DB(_ctx);
		data_to_send=new HashMap<String, String>();
		allCommentData=new ArrayList<HashMap<String,String>>();
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
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(getAllCommentFromPhoto.type==2){
							 getPhgotoRating(full_users_profile.myPhotoVo.get(sel_pos).getId());
								}
								else{
									pushCommentInList(getAllCommentFromPhoto.commentId);
								}
					}
				});
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
				
				db.open();
				
				data=db.findCursor(DB.Table.Name.photo_rating, DB.Table.photo_rating.photo_gallery_id+" = "+full_users_profile.myPhotoVo.get(sel_pos).getId(), null, null);
		
			if(data.getCount()>0){
				data.moveToFirst();
				ratingCounter=data.getFloat(data.getColumnIndex(DB.Table.photo_rating.rating.toString()));
			}
			
			data=db.findCursor(DB.Table.Name.photo_comment, DB.Table.photo_comment.photo_gallery_id.toString()+" = "+full_users_profile.myPhotoVo.get(sel_pos).getId(), null, null);
			if(data.getCount()>0){
				data.moveToFirst();
				commentCounter=data.getCount();
			}
				//commentCounter=data.getCount();
				//db.close();
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						p.cancel();
						commentCount.setText(commentCounter+" Comments");
					setOuterStars(ratingCounter);
					setStars(ratingCounter);
					}
				});
			
				
			}
			
		};
		
		
		 File cacheDir = StorageUtils.getCacheDirectory(_ctx);
	     options = new DisplayImageOptions.Builder()
	        .resetViewBeforeLoading(false)  // default
	        .delayBeforeLoading(1000)
	        .cacheInMemory(true) // default
	        .cacheOnDisk(true) // default
	        .considerExifParams(false) // default
	        .build();
	  
	  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(_ctx)
			.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(26) // default
		        .diskCacheSize(1000 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .imageDownloader(new BaseImageDownloader(_ctx)) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(options) // default
		        .writeDebugLogs()
		        .build();
				
			ImageLoader.getInstance().init(config);
				 imageLoader = ImageLoader.getInstance();
		
		
		if(full_users_profile.myPhotoVo.size()>0){
			image_count.setText("1/"+full_users_profile.myPhotoVo.size());
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
			
			drate1=(ImageView)dialog.findViewById(R.id.rate1);
			drate2=(ImageView)dialog.findViewById(R.id.rate2);
			drate3=(ImageView)dialog.findViewById(R.id.rate3);
			drate4=(ImageView)dialog.findViewById(R.id.rate4);
			drate5=(ImageView)dialog.findViewById(R.id.rate5);
			
			r1=(ImageView)dialog.findViewById(R.id.r1);
			r2=(ImageView)dialog.findViewById(R.id.r2);
			r3=(ImageView)dialog.findViewById(R.id.r3);
			r4=(ImageView)dialog.findViewById(R.id.r4);
			r5=(ImageView)dialog.findViewById(R.id.r5);
			
			photo_rating_api=new PhotoRating(_ctx, db, p){

				@Override
				protected void onDone() {
					// TODO Auto-generated method stub
					super.onDone();
					
					
					data=db.findCursor(DB.Table.Name.photo_comment, DB.Table.photo_comment.photo_gallery_id+" = "+full_users_profile.myPhotoVo.get(sel_pos).getId(), null, null);
					Log.d("Comnet data", ""+data.getCount());
					commentCounter=data.getCount();
					db.close();
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							p.cancel();
							ratingDialog.dismiss();
							
							setStars(selected_rating);
							Utils.same_id("Message", "you rated successfuly", _ctx);
						
							commentCount.setText(data.getCount()+" Comments");
								}
					});
				}

				@Override
				protected void updateUI() {
					// TODO Auto-generated method stub
					super.updateUI();
				}
				
				  
			};
			
			pb=(ProgressBar)dialog.findViewById(R.id.userImageProgress);
			rateImageParent=(LinearLayout)dialog.findViewById(R.id.rateImageParent);
			
			
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
//							ratingDialog.dismiss();
//							setStars(selected_rating);
							ratePhoto(full_users_profile.myPhotoVo.get(sel_pos).getId());
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
					if(full_users_profile.backstage_photo_comment_status.get(sel_pos)==false){
					if(comment_box.getText().length()>0){
						p.show();
					
					HashMap<String, String>keydata=new HashMap<String, String>();
					keydata.put("uuid", pref.get(Constants.KeyUUID));
					keydata.put("auth_token", pref.get(Constants.kAuthT));
					keydata.put("photo_gallery_id",String.valueOf(full_users_profile.myPhotoVo.get(sel_pos).getId()) );
					keydata.put("time_stamp", "");
					keydata.put("type", "1");
					keydata.put("page_index", "1");
					keydata.put("comment",comment_box.getText().toString());
					
					getAllCommentFromPhoto.setPostData(keydata);
					full_users_profile.backstage_comment_status=1;
					comment_box.setEnabled(false);
					
					post_btn.setEnabled(false);
					full_users_profile.backstage_photo_comment_status.set(sel_pos, true);
					callApi(getAllCommentFromPhoto);
					
					}
					else{
						Utils.same_id("Message", "Comment Can't be blank", _ctx);
					}
					}
				}
			});
		
			photoCommentAdapter = new PhotoCommentAdapter(getActivity(), allCommentData) {
				@Override
				protected void onItemClick(View v, int event_id, int comment_id) {
					// TODO Auto-generated method stub
				}
			};
			commentList.setAdapter(photoCommentAdapter);
			getComments(full_users_profile.myPhotoVo.get(sel_pos).getId());
			setUserInfo();
			
			
		
			
			
			hide_dialog=(ImageView)dialog.findViewById(R.id.hide_dialog);
		 circle_gallery = (Gallery)rootView.findViewById(R.id.circle_gallery);
		//ImageView selectedImage = (ImageView)rootView.findViewById(R.id.SingleView);
		circle_gallery.setSpacing(1);
		
		
		circle_gallery_adapter = new Circle_Gallery_Adapter(getActivity(), full_users_profile.myPhotoVo) {
			
			@Override
			protected void onItemClick(View v, String position) {				
				
			}

			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return super.getCount();
				if(full_users_profile.backstage_purchase_status==1){
					return super.getCount();
				}
				else{
				return full_users_profile.myPhoto_count;
			}
			}


			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				Log.d("test", "test in getItem "+position);
				
				
				return super.getItem(position);
			}


			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				Log.d("test", "test in getItemId "+position);
				if(full_users_profile.myPhoto_type.get(position)==1){
					comment_parent.setVisibility(View.GONE);
					rating_parent.setVisibility(View.GONE);
					
				}
				else{
					comment_parent.setVisibility(View.VISIBLE);
					rating_parent.setVisibility(View.VISIBLE);
				}
				
				image_count.setText((position+1)+"/"+full_users_profile.myPhotoVo.size());
				if(sel_pos!=position){
				sel_pos=position;
				if(full_users_profile.backstage_photo_comment_status.get(position)==true){
					
					comment_box.setEnabled(false);
					post_btn.setEnabled(false);
				}
				else{
					comment_box.setEnabled(true);
					post_btn.setEnabled(true);
				}
				
				getComments(full_users_profile.myPhotoVo.get(position).getId());
				}
				return super.getItemId(position);
				
			}


			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				Log.d("test", "test in getView "+position);
				
				return super.getView(position, convertView, parent);
			}
			
		};
		
		comment_parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showComments(full_users_profile.myPhotoVo.get(sel_pos).getId());
				
				
				dialog.show();
				data_to_send.clear();
			
				data_to_send.put("uuid", pref.get(Constants.KeyUUID));
				data_to_send.put("auth_token", pref.get(Constants.kAuthT));
				data_to_send.put("time_stamp", "");
				
				
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
		circle_gallery.setSelection(sel_pos);
		
		return rootView;
	}
	
	
public void ratePhoto(int photo_id){
		
		HashMap<String, String>data_to_send=new HashMap<String, String>();
		
		p.show();
		data_to_send.put("uuid", pref.get(Constants.KeyUUID));
		data_to_send.put("auth_token", pref.get(Constants.kAuthT));
		data_to_send.put("photo_gallery_id",String.valueOf(full_users_profile.myPhotoVo.get(sel_pos).getId()) );
		data_to_send.put("time_stamp", "");
		data_to_send.put("rating",String.valueOf(selected_rating));
		data_to_send.put("type", "1");
		data_to_send.put("page_index", "");
		photo_rating_api.setPostData(data_to_send);
		callApi(photo_rating_api);
		
	}
	
	public void setStars(float rating){
		
		if(rating>0){
			rateImageParent.setEnabled(false);
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

	
public void setUserStars(float rating){
		
		if(rating>0.0 && rating<=0.5){
		drate1.setImageResource(R.drawable.small_half_star);
		drate2.setImageResource(R.drawable.no_rating);
		drate3.setImageResource(R.drawable.no_rating);
		drate4.setImageResource(R.drawable.no_rating);
		drate5.setImageResource(R.drawable.no_rating);

		}

		if(rating>0.5 && rating<=1.0){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.no_rating);
			drate3.setImageResource(R.drawable.no_rating);
			drate4.setImageResource(R.drawable.no_rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>1.0 && rating<=1.5){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.small_half_star);
			drate3.setImageResource(R.drawable.no_rating);
			drate4.setImageResource(R.drawable.no_rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>1.5 && rating<=2.0){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.no_rating);
			drate4.setImageResource(R.drawable.no_rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>2.0 && rating<=2.5){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.small_half_star);
			drate4.setImageResource(R.drawable.no_rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>2.5 && rating<=3.0){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.rating);
			drate4.setImageResource(R.drawable.no_rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>3.0 && rating<=3.5){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.rating);
			drate4.setImageResource(R.drawable.small_half_star);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>3.5 && rating<=4.0){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.rating);
			drate4.setImageResource(R.drawable.rating);
			drate5.setImageResource(R.drawable.no_rating);
		}
		if(rating>4.0 && rating<=4.5){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.rating);
			drate4.setImageResource(R.drawable.rating);
			drate5.setImageResource(R.drawable.small_half_star);
		}
		if(rating>4.5){
			drate1.setImageResource(R.drawable.rating);
			drate2.setImageResource(R.drawable.rating);
			drate3.setImageResource(R.drawable.rating);
			drate4.setImageResource(R.drawable.rating);
			drate5.setImageResource(R.drawable.rating);
		}
		}
	

	public void setOuterStars(float rating){
		
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
		dataToInsert.put(DB.Table.photo_comment.photo_gallery_id.toString(), String.valueOf(full_users_profile.myPhotoVo.get(sel_pos).getId()));
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
	comment_box.setText("");
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
			
			//last_seen.setText(CelUtils.dateDifrence(Long.parseLong(row.get(Table.event_comment.add_date.toString()))));
			
			
			commentData.put(DB.Table.user_master.first_name.toString(), temp.getString(temp.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "+temp.getString(temp.getColumnIndex(DB.Table.user_master.last_name.toString())));
		}
			
		allCommentData.add(commentData);
		
		data.moveToPrevious();
		}
		photoCommentAdapter.notifyDataSetChanged();
	}
	
	
	public void setUserInfo(){
		
		final Cursor	temp_user=db.findCursor(DB.Table.Name.user_master, DB.Table.user_master.uuid.toString()+" = '"+pref.get(Constants.OtherUser)+"'", null, null);
		Log.d("arv", "arv"+temp_user.getCount());
		
		temp_user.moveToFirst();
		Log.d("arv", "arv First"+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.first_name.toString())));
		Log.d("arv", "arv Rirst "+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_name.toString())));
		
		user_name.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.first_name.toString()))+" "
		+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_name.toString()))+", "
				+CelUtils.getAgeStrFromDOB(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.date_of_birth.toString())) ));
		location.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.location.toString())));
		last_seen.setText(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.last_seen.toString())));
		
		setUserStars(Float.parseFloat(temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.rating.toString()))));
			
		
		
		imageLoader.displayImage(getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.server_id.toString()))+"/photos_of_you/"+temp_user.getString(temp_user.getColumnIndex(DB.Table.user_master.profile_pic.toString())), userImage, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.GONE);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		
	}	
	
		
}