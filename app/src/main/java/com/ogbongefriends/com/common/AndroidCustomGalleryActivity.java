package com.ogbongefriends.com.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ogbongefriends.com.ogbonge.photos.addPhotoOption;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.custom.GalleryImagesVO;

public class AndroidCustomGalleryActivity extends Activity {
	private int count;
	private CustomLoader p;
	private ImageAdapter imageAdapter;
	private ArrayList<GalleryImagesVO> galleryImageList;
	Preferences pref;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_pic_selection);
		p = new CustomLoader(AndroidCustomGalleryActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		pref=new Preferences(AndroidCustomGalleryActivity.this);
		galleryImageList= new ArrayList<>();
		new LoadPhotos().execute();
		GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
		imageAdapter = new ImageAdapter();
		imagegrid.setAdapter(imageAdapter);
		final Button selectBtn = (Button) findViewById(R.id.selectBtn);
		selectBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int cnt = 0;
				String selectImages = "";
				for (int i =0; i<galleryImageList.size(); i++)
				{
					if (galleryImageList.get(i).getSelected()){
						
						selectImages = selectImages + galleryImageList.get(i).getPath() + "|";
						/*finalImages[cnt]=galleryImageList.get(i).getPath();
								cnt++;*/
						cnt++;
						getBitmap(galleryImageList.get(i).getPath(),cnt-1,cnt);
						//PostCurrentImage(galleryImageList.get(i).getPath(),cnt-1,cnt);



					}
				}
				final int len = cnt;
				
			/*	for(int i=0;i<cnt;i++){
					 PostCurrentImage(finalImages[i],i,cnt);
				}*/
				
				if (cnt == 0){
					Toast.makeText(getApplicationContext(),
							"Please select at least one image",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"You've selected Total " + cnt + " image(s).",
							Toast.LENGTH_LONG).show();
					Log.d("SelectedImages", selectImages);
				}
			}
		});
	}

	
	public static void same_id(String title, String message, Context ctx) {
		

			

		}

	public void getBitmap(String path, int pointer,int index){

		Bitmap bitmap = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
		Log.d("File Size", "" + bytes.size());
		File destination = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");

		FileOutputStream fo;
		try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.show();
		PostCurrentImage(destination.getAbsolutePath(),pointer,index);
	}

	protected void PostCurrentImage(String imagePost,final int i,final int len) {
p.show();
		String upload_type = String.valueOf(addPhotoOption.uploadType);
		String type = "image/jpg";
		File file 	= new File(imagePost);
		String url 	=AndroidCustomGalleryActivity.this.getString(R.string.urlString)+"api/uploadPhoto/index";				
		PostImage_AfterLogin callbackservice = new PostImage_AfterLogin(file, url, imagePost, pref.get(Constants.KeyUUID)/*, Feedid*/, type, upload_type, AndroidCustomGalleryActivity.this) {
			
			@Override
			public void receiveData() {
				
				if(PostImage_AfterLogin.resCode == 1){		
					if(i==len-1){
						Log.d("uploadedddddd*********", "successfully");

						p.cancel();
						final Dialog dialog = new Dialog(AndroidCustomGalleryActivity.this);
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
								onBackPressed();
							}
						});

						title_msg.setText("Ogbonge");
						msg.setText(PostImage_AfterLogin.resMsg);
						//title_msg.setText("Confirmation..");
						//String str="Are you sure want to send gift to "+pref.get(Constants.OtherUserName)+" ?";
						//msg.setText(str);
						dialog.show();
						Log.e("", "postImage " + imageName);

					}
				}
				else{
					p.cancel();
					Utils.alert(AndroidCustomGalleryActivity.this, PostImage_AfterLogin.resMsg);					
				}						
			}						
							
			@Override
			public void receiveError() {
				p.cancel();
				AndroidCustomGalleryActivity.this.runOnUiThread(new Runnable() {
									
					@Override
					public void run() {
							
						Utils.alert(AndroidCustomGalleryActivity.this, "Error in uploading Image");
					}
				});
			}
		};
		callbackservice.execute(url, null, null);
	}	
	
	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return galleryImageList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		ViewHolder holder;
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.galleryitem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
				holder.id = position;
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setChecked(galleryImageList.get(position).getSelected());
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (holder.checkbox.isSelected()) {
						holder.checkbox.setSelected(false);
						galleryImageList.get(position).setisSelected(false);
					} else {
						holder.checkbox.setSelected(true);
						galleryImageList.get(position).setisSelected(true);
					}
				}
			});

			GalleryImagesVO infoVO= galleryImageList.get(position);
			Glide.with(AndroidCustomGalleryActivity.this).load(infoVO.getPath()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).into(holder.imageview);

			return convertView;
		}
	}
	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
		int id;
	}

	class LoadPhotos extends AsyncTask<Object, GalleryImagesVO, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			String[] projection = {MediaStore.Images.ImageColumns.DATA};
			Cursor c = null;
			SortedSet<String> dirList = new TreeSet<>();
			ArrayList<GalleryImagesVO> resultIAV = new ArrayList<>();
			String[] directories = null;
			if (u != null) {
				c = managedQuery(u, projection, null, null, null);
			}

			if ((c != null) && (c.moveToFirst())) {
				do {
					String tempDir = c.getString(0);
					tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
					try {
						dirList.add(tempDir);
					} catch (Exception ignored) {

					}
				}
				while (c.moveToNext());
				directories = new String[dirList.size()];
				dirList.toArray(directories);

			}
			for (int i = 0; i < dirList.size(); i++) {
				if(directories[i] != null) {
					File imageDir = new File(directories[i]);
					File[] imageList = imageDir.listFiles();
					if (imageList == null)
						continue;
					for (File imagePath : imageList) {
						try {
							if (imagePath.isDirectory()) {
								imageList = imagePath.listFiles();

							}
							if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
									|| imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
									|| imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
									|| imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
									|| imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
									) {
								String path = imagePath.getAbsolutePath();
								File file = new File(path);
								String name = null;
								String[] nameSplit = file.getName().split("\\.");
								if (nameSplit.length == 0)
									name = file.getName();
								else if (nameSplit.length > 0)
									name = nameSplit[0];
								galleryImageList.add(new GalleryImagesVO(name, String.valueOf(file.length()), path, false, file.getName().substring(file.getName().lastIndexOf('.'))));
								if (i % 20 == 0) {
									publishProgress();
								}
								//new GalleryImagesVO(file.getName(),String.valueOf(file.length()),path,false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}


			return null;
		}


		@Override
		protected void onProgressUpdate(GalleryImagesVO... values) {
			//   resultIAV.add(values[0]);
			imageAdapter.notifyDataSetChanged();
		}



		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if(galleryImageList!=null && galleryImageList.size()>0) {

				imageAdapter.notifyDataSetChanged();
				//hideAnimationLoader();
			}
		}
	}

}