package com.ogbongefriends.com.ogbonge.photos;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.JsonArray;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.common.AndroidCustomGalleryActivity;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.PostImage_AfterLogin;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;


@SuppressLint("NewApi")
public class addPhotoOption extends Fragment implements Runnable, MyLocationListener, OnClickListener, OnCheckedChangeListener {

    // private ListView listView;
    @SuppressWarnings("unused")
    private EditText posttetx;
    @SuppressWarnings("unused")
    private Button post;
    private Button attchbtn;
    private JsonArray searchedata;
    private Uri imageUri;
    private TextView photoOfYou, backStagePhoto;
    private Uri selectedImage;
    private Preferences pref;
    private Context _ctx;
    private static String APP_ID = "1509359755975991";
    private CustomLoader p;
    public final int SELECT_FILE = 100;
    public final int REQUEST_CAMERA = 101;
    private DB db;
    private View rootView;
    private ImageView tempImage;
    private LinearLayout addFromFblayout, addFromExisting, addFromCapture;
    private Button backToList;
    public static boolean[] thumbnailsselection;
    public static int uploadType = 0;
    private Uri mCapturedImageURI;
    private int[] urls = {R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here,
            R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here,
            R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here, R.drawable.add_me_here};

    private Facebook facebook = new Facebook(APP_ID);
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

    public addPhotoOption(Context ctx) {
        _ctx = ctx;
    }

    public addPhotoOption() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        p = new CustomLoader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        db = new DB(getActivity());
        //p.show();
        pref = new Preferences(getActivity());
        uploadType = pref.getInt("upload_type");

        rootView = inflater.inflate(R.layout.add_photo_options, container, false);
        addFromFblayout = (LinearLayout) rootView.findViewById(R.id.addFromFblayout);
        addFromExisting = (LinearLayout) rootView.findViewById(R.id.addExistLayout);
        addFromCapture = (LinearLayout) rootView.findViewById(R.id.addfromCapture);
        photoOfYou = (TextView) rootView.findViewById(R.id.photoOfYou);
        backToList = (Button) rootView.findViewById(R.id.backtolist);
        backStagePhoto = (TextView) rootView.findViewById(R.id.backStagePhoto);
        tempImage = (ImageView) rootView.findViewById(R.id.tempImage);
        addFromFblayout.setOnClickListener(this);
        addFromExisting.setOnClickListener(this);
        addFromCapture.setOnClickListener(this);
        backToList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().onBackPressed();
            }
        });

        if (uploadType == 1) {
            backStagePhoto.setVisibility(View.GONE);
            photoOfYou.setVisibility(View.VISIBLE);
        } else {
            backStagePhoto.setVisibility(View.VISIBLE);
            photoOfYou.setVisibility(View.GONE);
        }

        return rootView;


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

        switch (v.getId()) {
            case R.id.addFromFblayout:

                ((DrawerActivity) getActivity()).displayView(60);

                break;


            case R.id.addExistLayout:

                Intent intent = new Intent(_ctx, AndroidCustomGalleryActivity.class);
                startActivity(intent);
                break;


            case R.id.addfromCapture:


                startCamera();
                break;
            default:
                break;
        }

    }


    public void GetAlbumdata() {
        mAsyncRunner.request("me/albums", new RequestListener() {
            @Override
            public void onComplete(String response, Object state) {
                Log.d("Profile", response);
                String json = response;
                try {
                    JSONObject profile = new JSONObject(json);
                    Log.d("Arvind Profile Data", "" + profile.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException(IOException e, Object state) {
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                                                Object state) {
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                                                Object state) {
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void loginToFacebook() {

        //mPrefs = getPreferences(Context.MODE_PRIVATE);
        mPrefs = getActivity().getSharedPreferences(Constants.SOCIAL_MEDIA, _ctx.MODE_PRIVATE);
        //	mPrefs=getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);

            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(getActivity(),
                    new String[]{"user_photos", "user_about_me", "email", "publish_stream"},
                    new DialogListener() {

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                            Log.d("Arv", "Calling getProfileInformation");
                            GetAlbumdata();
                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors

                        }

                    });
        } else {
            GetAlbumdata();
        }
    }


    public boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
                + android.os.Build.DEVICE);

    }

    public void startCamera() {

//		Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
//		String fileName = "testphoto.jpg";
//		ContentValues values = new ContentValues();
//		values.put(MediaStore.Images.Media.TITLE, fileName);
//		values.put(MediaStore.Images.Media.DESCRIPTION,	"Image capture by camera");
//		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//		imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//		startActivityForResult(intent, REQUEST_CAMERA);


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "temp.png");
        mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//		startActivityForResult(intentPicture,REQUEST_CAMERA);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);


//		 final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//         intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//         startActivityForResult(intent, REQUEST_CAMERA);

//		 Intent intent = new Intent(
//                 MediaStore.ACTION_IMAGE_CAPTURE);
//         File file = new File(Environment
//                 .getExternalStorageDirectory(),
//                 "test.jpg");
//
//         mCapturedImageURI = Uri.fromFile(file);
//         Log.d("TAG", "outputFileUri intent"
//                 + mCapturedImageURI);
//         intent.putExtra(MediaStore.EXTRA_OUTPUT,
//        		 mCapturedImageURI);
//         startActivityForResult(intent, REQUEST_CAMERA);
//		

    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        mCapturedImageURI = Uri.parse(file.getAbsolutePath());
        return imgUri;
    }


    public String getpath(Uri imageUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(imageUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private File getTempFile(Context context) {
        //it will return /sdcard/image.tmp
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path + "/OgbongeDir/" + Constants.MyOgbongePhotos + "/", "image.tmp");
    }


    @Override
    public void onLocationUpdate(Location location) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == DrawerActivity.RESULT_OK) {


            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
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
                PostCurrentImage(destination.getAbsolutePath());

            } else if (requestCode == SELECT_FILE) {
                try {
                    Uri selectedImageUri = data.getData();
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
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
                    String tempPath = CelUtils.getpath(selectedImageUri, getActivity());
                    Log.d("Image Path Select", "" + tempPath);
                    String[] Img_size = {MediaStore.Images.Media.SIZE};
                    PostCurrentImage(destination.getAbsolutePath());
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                    tempImage.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected void PostCurrentImage(String imagePost) {

        String upload_type = String.valueOf(uploadType);
        String type = "image/jpg";
        File file = new File(imagePost);
        String url = getActivity().getString(R.string.urlString) + "api/uploadPhoto/index";
        PostImage_AfterLogin callbackservice = new PostImage_AfterLogin(file, url, imagePost, pref.get(Constants.KeyUUID)/*, Feedid*/, type, upload_type, getActivity()) {

            @Override
            public void receiveData() {

                if (PostImage_AfterLogin.resCode == 1) {
                    Log.d("uploadedddddd*********", "successfully");

                    p.cancel();
                    Utils.same_id("Message", PostImage_AfterLogin.resMsg, getActivity());
                    Log.e("", "postImage " + imageName);
                } else {
                    p.cancel();
                    Utils.alert(getActivity(), PostImage_AfterLogin.resMsg);
                }
            }

            @Override
            public void receiveError() {
                p.cancel();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Utils.alert(getActivity(), "Error in uploading Image");
                    }
                });
            }
        };
        callbackservice.execute(url, null, null);
    }


}


