package com.ogbongefriends.com.common;

import java.io.File;

import com.ogbongefriends.com.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;



public class MultipleImageSelecterActivity extends Activity {
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	private int type;
	private String id;
	private CustomLoader p;
	private AsyncTaskLoadFiles myAsyncTaskLoadFiles;
	public static Activity fa;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.promote_yourself);
		p = new CustomLoader(MultipleImageSelecterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		fa = this;

		Intent intent = getIntent();
		try {
			type = intent.getIntExtra("feed_type", 0);
			id = intent.getStringExtra("id");
		} catch (Exception e) {
		}

		
		imageAdapter = new ImageAdapter();
		myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(imageAdapter);
		myAsyncTaskLoadFiles.execute();

		GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);

		imagegrid.setAdapter(imageAdapter);
		// imagecursor.close();

		final Button selectBtn = (Button) findViewById(R.id.select_prof_pic);
		selectBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int len = thumbnailsselection.length;
				int cnt = 0;
				String selectImages = "";
				for (int i = 0; i < len; i++) {
					if (thumbnailsselection[i]) {
						cnt++;
						selectImages = selectImages + arrPath[i] + "***";

					}
				}
				if (cnt == 0) {
					Toast.makeText(getApplicationContext(),
							"Please select at least one image",
							Toast.LENGTH_LONG).show();
				} else {

					
//					Intent returnIntent = new Intent(
//							MultipleImageSelecterActivity.this,
//							AndroidCustomGalleryActivity.class);
//					returnIntent.putExtra("result", selectImages);
//					returnIntent.putExtra("result_count", cnt);
//					returnIntent.putExtra("feed_type", type);
//					returnIntent.putExtra("id", id);
//					startActivity(returnIntent);
					
				}
			}
		});
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.gellary_item, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.itemCheckBox);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (thumbnailsselection[id]) {
						cb.setChecked(false);
						thumbnailsselection[id] = false;
					} else {
						cb.setChecked(true);
						thumbnailsselection[id] = true;
					}
				}
			});
			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = v.getId();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse("file://" + arrPath[id]),
							"image/*");
					Log.v("img", arrPath[id].toString());
					startActivity(intent);
				}
			});

			holder.imageview.setImageBitmap(thumbnails[position]);
			holder.checkbox.setChecked(thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
		int id;
	}

	// =======================

	public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

		File targetDirector;
		ImageAdapter myTaskAdapter;

		public AsyncTaskLoadFiles(ImageAdapter adapter) {
			myTaskAdapter = adapter;
		}

		@Override
		protected void onPreExecute() {
			String ExternalStorageDirectoryPath = Environment
					.getExternalStorageDirectory().getAbsolutePath();

			String targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera/";
			targetDirector = new File(targetPath);
p.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			int image_column_index = imagecursor
					.getColumnIndex(MediaStore.Images.Media._ID);
			count = imagecursor.getCount();
			thumbnails = new Bitmap[count];
			arrPath = new String[count];
			thumbnailsselection = new boolean[count];
			for (int i = 0; i < count-10; i++) {
				try {
					imagecursor.moveToPosition(i);
					int id = imagecursor.getInt(image_column_index);
					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
							getApplicationContext().getContentResolver(), id,
							MediaStore.Images.Thumbnails.MICRO_KIND, null);

					arrPath[i] = imagecursor.getString(dataColumnIndex);

					onProgressUpdate(String.valueOf(id));

				} catch (Exception e) {

				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {

		//	myTaskAdapter.notifyDataSetChanged();
			super.onProgressUpdate(values);

		}

		@Override
		protected void onPostExecute(Void result) {
			//myTaskAdapter.notifyDataSetChanged();
			p.cancel();
			super.onPostExecute(result);
		}

	}
}