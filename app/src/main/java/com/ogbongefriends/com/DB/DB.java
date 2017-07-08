package com.ogbongefriends.com.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.Media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;


//==
public class DB {

    public static String _DB_NAME = "ogbonge.sqlite";
    public static String _DB_BAK_NAME = "ogbonge_backup.sqlite";

    public static String _DB_PATH;

    private static int _DB_VERSION = 1;

    private Context _context;


    public static final String KEY_ID = "_id";

    public static final String KEY_NAME = "display_name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LAST_MESSAGE = "last_message";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_PHONE_CODE = "phone_code";
    public static final String KEY_EMAIL = "key_email";


    private SQLiteDatabase sqdb;

    private DBHelper helper;

    private static boolean hasBackUp = false;

    @SuppressWarnings("unused")
    private DBListener dbListener;

    // CONSTRUCTOR
    public DB(Context context) {

        initialize(context, _DB_NAME, true);
    }

    // CONSTRUCTOR
    public DB(Context context, boolean initialize) {

        initialize(context, _DB_NAME, initialize);
    }

    // CONSTRUCTOR
    public DB(Context context, String dbName, boolean initialize) {

        initialize(context, dbName, initialize);
    }

    // INITIALIZE METHOD
    @SuppressLint("SdCardPath")
    private void initialize(Context context, String dbName, boolean initialize) {

        this._context = context;
        /* handler = new Handler(); */
        _DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        helper = new DBHelper(this._context, dbName, null, _DB_VERSION);

        if (initialize)
            createorUpgradeDatabse();
    }

    // CLEAR METHOD
    public void clear() {

        _context = null;
        sqdb = null;
        helper = null;
        dbListener = null;
    }

    // DB LISTENER
    public void setDBListener(DBListener listener) {
        dbListener = listener;
    }

    // OPEN DB IN WRITABLE MODE
    public void open() {
        sqdb = helper.getWritableDatabase();
    }

    // CLOSE DB
    public void close() {

        if (sqdb != null && sqdb.isOpen())
            sqdb.close();
    }

    public SQLiteDatabase getSqliteDB() {
        return sqdb;
    }


    public boolean isImagePresent(String id) {

//		// Select All Query
//		String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME
//				+ " WHERE " + KEY_ID + " = " + id + "";
//		openForRead();
//		Cursor cursor = null;
//		if (db.rawQuery(selectQuery, null) != null) {
//			cursor = db.rawQuery(selectQuery, null);
//		}
//		if (cursor.getCount() > 0) {
//			cursor.close();
//			db.close();
//			return true;
//		}
//		db.close();
        return false;
    }


    // CREATE DATABASE BACKUP
    public static boolean backUp(Context c) {

        Log.d("Back Up Called", "");
        boolean status = false;

        File sf = new File(_DB_PATH, _DB_NAME);
        File df = new File(_DB_PATH, _DB_BAK_NAME);

        if (sf.exists()) {

            try {

                c.deleteDatabase(_DB_BAK_NAME);
                df.createNewFile();
                copyDataBase(sf, df);
                hasBackUp = true;
                status = true;
            } catch (IOException e) {

            }
        }
        return status;
    }

    // EXTRACT DATABASE
    public static boolean extract(Context c) {

        Log.d("Back Up Called", "***********");
        boolean status = false;

        File sf = new File(_DB_PATH, _DB_NAME);
        File sf1 = new File(_DB_PATH, _DB_BAK_NAME);
        File df = new File(Media.getAlbumDir(c, Media.DB_DIR), _DB_NAME);
        File df1 = new File(Media.getAlbumDir(c, Media.DB_DIR), _DB_BAK_NAME);

        if (sf.exists()) {

            try {
				/*c.deleteDatabase(_DB_BAK_NAME);*/

                if (sf.exists()) {

                    if (df.exists())
                        df.delete();

                    df.createNewFile();
                    copyDataBase(sf, df);
                }

                if (sf1.exists()) {

                    if (df1.exists())
                        df1.delete();

                    df1.createNewFile();
                    copyDataBase(sf1, df1);
                }

                hasBackUp = true;
                status = true;
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return status;
    }

    // RESTORE DATABASE
    public synchronized static boolean restore(Context c) {

        //	Log.d("Restore Called");

        boolean status = false;

        File sf = new File(_DB_PATH, _DB_BAK_NAME);
        File df = new File(_DB_PATH, _DB_NAME);

        if (sf.exists() && hasBackUp) {

            c.deleteDatabase(_DB_NAME);
            try {
                df.createNewFile();
                copyDataBase(sf, df);
            } catch (IOException e) {

                e.printStackTrace();
            }

            hasBackUp = true;
            status = true;
			
			/*try {*/
            //sf.renameTo(df);
            //df.renameTo(sf);

            //df.delete();
            //c.deleteDatabase(_DB_NAME);
            //df.createNewFile();
            //copyDataBase(sf, df);
            //c.deleteDatabase(_DB_BAK_NAME);
            //copyDatabase(c, sf, df);
            hasBackUp = false;
            Log.d("FIle name ", "src " + sf.getName() + "  BC" + df.getName());
            Log.d("CheckDB", "com.ogbonge" + sf.exists() + "    BC" + df.exists());

            status = true;
				
				/*} 
			catch (IOException e) {
				
			}*/
        }

        Log.d("Restore Completed", "***********");

        return status;
    }

    // CREATE OR UPGRADE DATABASE
    public void createorUpgradeDatabse() {

        boolean dbExist = checkDataBase();

        Log.d("ash_db", "Database Existence === " + dbExist);

        if (!dbExist) {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.

            try {
                sqdb = helper.getWritableDatabase();
            } catch (SQLException e) {
                Log.d("ash_exp", e.getMessage() + " at open(database not open for writing)");
            }

            try {
                copyDataBase();
                Log.d("ash_db", "Database Copied");

            } catch (IOException e) {

                Log.d("ash_exp", e.getMessage() + " at open (Database not Copied)");
            }
        } else {
            sqdb = helper.getWritableDatabase();
        }
    }

    // CHECK DATABASE
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = _DB_PATH + _DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException e) {

            // Log.d("ash","Database not exist yet");
        }

        if (checkDB != null) {

            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    // COPY DATABASE
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = _context.getAssets().open(_DB_NAME);

        // Path to the just created empty db
        String outFileName = _DB_PATH + _DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();

        // Close the streams
        myOutput.close();
        myInput.close();
    }

    // COPY DATABASE ONE FILE TO ANOTHER
    private static void copyDataBase(File from, File to) throws IOException {

        // Open your local db as the input stream
        InputStream myInput = new FileInputStream(from);

        // Path to the just created empty db

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(to);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    /**
     * Clears rows as per where clause, else complete table.
     */
    public void clear(String tableName, String where) {

        getSqliteDB().execSQL("delete from " + tableName + (where == null ? "" : " where " + where));
    }

    // AUTO INSERT AND UPDATE
    public void autoInsertUpdate(String tableName, HashMap<String, String> values, String where, String[] args) {

        Log.d("autoInsertUpdate", " where " + where);
        Log.d("autoInsertUpdate", " values " + values);

        if (where != null && isRecordExist(tableName, where, args) != Constants.kZero) {
            if (tableName.equalsIgnoreCase("sg_users")) {

                Log.d("", " Updated to database.................................");
            }
            update(tableName, values, where, args);
        } else {

            if (tableName.equalsIgnoreCase("sg_users")) {

                Log.d("", " added to database.................................");
            }
            insert(tableName, values);
        }
    }

    // CHECK RECORD IS EXIST
    public int isRecordExist(String tableName, String where, String[] args) {

        int status = Constants.kZero;

        Log.e("isRecordExist", "tableName " + tableName + " where " + where);

        Cursor c = getSqliteDB().query(tableName, null, where, args, null, null, null);

        if (c.getCount() > 0) {

            c.moveToNext();
            status = c.getInt(0);
        }

        c.close();
        Log.e("isRecordExist", "status " + status);
        return status;
    }

    // CHECK RECORD IS EXIST
    public int isRecordExist(String query, String[] args) {

        int status = Constants.kZero;

        Cursor c = getSqliteDB().rawQuery(query, args);

        if (c.getCount() > 0) {

            c.moveToNext();
            status = c.getInt(0);
        }

        c.close();

        return status;
    }

    // INSERT
    public long insert(String tableName, HashMap<String, String> values) {

        ContentValues vals = createContentValues(values);
        Log.d("Inserting data ", vals.toString());
        return getSqliteDB().insert(tableName, null, vals);

    }

    // UPDATE
    public int update(String tableName, HashMap<String, String> values, String where, String[] args) {

        ContentValues vals = createContentValues(values);
        return getSqliteDB().update(tableName, vals, where, args);
    }

    // DELETE
    public int delete(String tableName, String where, String[] args) {

        return getSqliteDB().delete(tableName, where, args);
    }

    // TRUNCATE
    public void truncate(String tableName) {

        getSqliteDB().execSQL("delete from " + tableName);
    }


    public ArrayList<HashMap<String, String>> find(String sql, String[] args) {

        ArrayList<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
        Cursor c = getSqliteDB().rawQuery(sql, args);

        Log.d("HashMap Data Count ====> " + c.getCount(), "");

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                HashMap<String, String> info = new HashMap<String, String>();
                String[] cols = c.getColumnNames();

                for (String col : cols)
                    info.put(col, c.getString(c.getColumnIndex(col)));

                c.moveToNext();
                infos.add(info);
            }
        }
        c.close();
        return infos;
    }


    public ArrayList<HashMap<String, String>> find(String tableName, String where, String[] args, String other) {

        String sql = "select * from " + tableName
                + (where == null ? "" : " where " + where) + (other == null ? "" : " " + other);

        Log.d("SQLLL", "" + sql);

        ArrayList<HashMap<String, String>> infos = find(sql, args);

        Log.d("Return", infos.toString());
        return infos;
    }


    public Cursor findCursor(String sql, String[] args) {

        Log.d("SQL STRING IS", "" + sql);

        Cursor cursor = getSqliteDB().rawQuery(sql, args);
        return cursor;
    }

    // GET MAX VALUE
    public int getMaxValue(String tableName, String fieldName, String whereCaluse) {

        int maxVal = 0;

        String query = "SELECT MAX(" + fieldName + ") FROM " + tableName;

        if (whereCaluse != null) {
            query = query + " WHERE " + whereCaluse;
        }

        Cursor c = findCursor(query, null);

        if (c.moveToNext())
            maxVal = c.getInt(0);

        return maxVal;

    }

    // GET MAX VALUE
    public String getStringMaxValue(String tableName, String fieldName, String whereCaluse) {

        String maxVal = "";

        String query = "SELECT MAX(" + fieldName + ") FROM " + tableName;

        if (whereCaluse != null) {
            query = query + " WHERE " + whereCaluse;
        }

        Log.d("String Max Query === " + query, "");

        Cursor c = findCursor(query, null);

        if (c.moveToNext())
            maxVal = c.getString(0);

        return maxVal;
    }

    //
	/*
	 * public Cursor findCursorForGivenColumn(String tableName, String []
	 * columnNames, String where, String[] args,String other){
	 * 
	 * String columnNameString = StringUtils.join(columnNames, ", "); String sql
	 * =
	 * "select "+columnNameString+" from "+tableName+(where==null?"":" WHERE "+
	 * where)+(other==null?"":" "+other); Cursor c= findCursor(sql, args);
	 * 
	 * return c;
	 * 
	 * }
	 */

    // GET MIN VALUE
    public int getMinValue(String tableName, String fieldName, String whereCaluse) {

        int minVal = 0;

        String query = "SELECT MIN(" + fieldName + ") FROM " + tableName;

        if (whereCaluse != null) {
            query = query + " WHERE " + whereCaluse;
        }

        Cursor c = findCursor(query, null);

        if (c.moveToNext())
            minVal = c.getInt(0);

        return minVal;
    }

    // GET MIN VALUE
    public String getStringMinValue(String tableName, String fieldName, String whereCaluse) {

        String minVal = "";

        String query = "SELECT MIN(" + fieldName + ") FROM " + tableName;

        if (whereCaluse != null) {
            query = query + " WHERE " + whereCaluse;
        }

        Log.d("String Min Query === " + query, "");

        Cursor c = findCursor(query, null);

        if (c.moveToNext())
            minVal = c.getString(0);

        return minVal;
    }

    // GET COUNT
    public int getCount(String tableName, String whereCaluse) {

        int minVal = 0;

        String query = "SELECT COUNT(*) FROM " + tableName;

        if (whereCaluse != null) {
            query = query + " WHERE " + whereCaluse;
        }

        Cursor c = findCursor(query, null);

        if (c.moveToNext())
            minVal = c.getInt(0);

        return minVal;
    }


    public Cursor findCursor(String tableName, String where, String[] args, String other) {

        String sql = "select * from " + tableName + (where == null ? "" : " where " + where) + (other == null ? "" : " " + other);

        Log.d("Find Cursor Query == ", "" + sql);

        Cursor c = findCursor(sql, args);

        return c;
    }

    public Cursor findCursorNoWhere(String tableName, String where, String[] args, String other) {

        String sql = "select * from " + tableName + (where == null ? "" : " " + where) + (other == null ? "" : " " + other);

        Log.d("Find Cursor Query == ", "" + sql);
        Cursor c = findCursor(sql, args);
        return c;
    }

    // CONTENT VALUES
    private ContentValues createContentValues(HashMap<String, String> values) {

        ContentValues vals = new ContentValues();
        String[] keys = values.keySet().toArray(new String[]{});
        for (String key : keys)
            vals.put(key, values.get(key));

        return vals;
    }

    // CLASS TABLE
    public static class Table {

        // CLASS NAME
        public static class Name {


            public final static String sg_user_transactions = "sg_user_transactions";
            public final static String sg_user_skill_masters = "sg_user_skill_masters";
            public final static String sg_user_roles = "sg_user_roles";
            public final static String sg_user_favourite_list = "sg_user_favourite_list";

            public final static String sg_users_experiences = "sg_user_experiences";
            public final static String sg_user_descriptions = "sg_user_descriptions";
            public final static String sg_user_certificates = "sg_user_certificates";
            public final static String sg_skills = "sg_skills";
            public final static String sg_security_questions = "sg_security_questions";

            public final static String sg_courses = "sg_courses";
            public final static String sg_language_data_masters = "sg_language_data_masters";
            public final static String sg_language_masters = "sg_language_masters";
            public final static String sg_skill_categories = "sg_skill_categories";
            public final static String sg_country = "sg_country";

            public final static String sg_subscription_plans = "sg_subscription_plans";
            public final static String sg_subscription_features = "sg_subscription_features";

            public final static String sg_circles = "sg_circles";
            public final static String sg_circles_search = "sg_circles_search";
            public final static String sg_circle_members = "sg_circle_members";

            public final static String sg_circle_events = "sg_circle_events";


            public final static String sg_likes = "sg_likes";
            public final static String sg_comments = "sg_comments";
            public final static String sg_feeds = "sg_feeds";
            public final static String sg_feed_media = "sg_feed_media";
            public final static String sg_shares = "sg_shares";
            public final static String sg_report_abuses = "sg_report_abuses";

            public final static String sg_user_account_details = "sg_user_account_details";

            public final static String sg_user_education_details = "sg_user_education_details";

            public final static String sg_user_privacy_settings = "sg_user_privacy_settings";

            public final static String user_master = "user_master";
            public final static String bodytype_master = "bodytype_master";
            public final static String relationship_master = "relationship_master";
            public final static String eye_color_master = "eye_color_master";
            public final static String hair_color_master = "hair_color_master";
            public final static String interestedin_master = "interestedin_master";
            public final static String sexuality_master = "sexuality_master";
            public final static String weight_master = "weight_master";
            public final static String height_master = "height_master";
            public final static String gift_master = "gift_master";
            public final static String user_notification_master = "user_notification_master";
            public final static String country_master = "country_master";
            public final static String nigiria_state_master = "nigiria_state_master";
            public final static String nigiria_city_master = "nigiria_city_master";
            public final static String starsandclubs_master = "starsandclubs_master";
            public final static String education_master = "education_master";
            public final static String job_master = "job_master";
            public final static String user_starsandclubs = "user_starsandclubs";

            public final static String event_master = "event_master";
            public final static String interestedin_purpose_master = "interestedin_purpose_master";
            public final static String event_attendee_master = "event_attendee_master";
            public final static String event_image_master = "event_image_master";
            public final static String event_categories = "event_categories";
            public final static String event_comment = "event_comment";
            public final static String photo_comment = "photo_comment";
            public final static String photo_rating = "photo_rating";
            public final static String photo_likes = "photo_likes";
            public final static String photo_album_master = "photo_album_master";

            public final static String news_media = "news_media";
            public final static String news_master = "news_master";
            public final static String user_block_master = "user_block_master";
            public final static String gift_sent_to_user = "gift_sent_to_user";
            public final static String setting_master = "setting_master";
            public final static String user_contact = "user_contact";
            public final static String banner_master = "banner_master";
            public final static String notification_master = "notification_master";
            public final static String red_bubble_master = "red_bubble_master";

            public final static String starsandclubs_category_master = "starsandclubs_category_master";
        }


        public enum starsandclubs_category_master {
            id, starsandclubs_category_id, category_name, status, add_date, modify_date
        }


        public enum red_bubble_master {
            uuid, favouriteCount, whoFavdMeCount, friendCount, whoRateMeCount, giftCount, newNewsCount
        }


        public enum notification_master {
            id, uuid, other_user_uuid, type, msg, userId, profile_pic
        }


        public enum user_contact {
            id, number, name, email
        }

        public enum banner_master {
            id, image, type, status, add_date
        }


        public enum setting_master {
            id, max_number_of_images, max_file_size, pass_7days_value, advertise_me_value,
            point_to_money, admin_one_name, admin_one_photo, admin_two_name, admin_two_photo,
            admin_three_name, admin_three_photo, contact_us_address, contact_us_pincode, contact_us_email,
            contact_us_phone, hot_member_likes, hot_member_points, redeem_points, advertise_me_time,
            block_keywords, block_10_digit_no, block_11_digit_no, backstage_points, advertise_me_content, match_notification, event_time_validate
        }


        public enum gift_sent_to_user {
            id, gitf_id, sender_master_id, receiver_master_id, description, status, gift_master_id, add_date, modify_date, seen_status, gift_cost
        }

        public enum user_block_master {
            id, user_master_id, other_user_master_id, message, status, add_date, modify_date
        }


        public enum news_media {
            id, news_master_id, media_type, news_image, news_video, video_thumb, status, add_date, modify_date, news_short_description
        }


        public enum news_master {
            id, news_title, news_short_description, status, add_date, modify_date, posted_by
        }


        public enum photo_album_master {
            id, user_master_id, album_category_id, album_title, album_description, privacy_settings, status, add_date, modify_date, point_value
        }

        public enum photo_likes {
            id, user_master_id, photo_gallery_id, status, add_date, modify_date
        }


        public enum photo_rating {
            id, user_master_id, photo_gallery_id, rating, add_date, modify_date
        }


        public enum photo_comment {
            id, user_master_id, photo_gallery_id, comment, add_date, modify_date
        }

        public enum event_comment {
            id, user_master_id, event_master_id, event_comment, status, add_date, modify_date
        }


        public enum event_master {
            id, event_category_id, user_master_id, event_description, place, event_datetime, status, add_date, modify_date, comments, city, event_subcategory_id
        }

        public enum event_categories {
            id, category_name, status, add_date, modity_date, type, category_id
        }


        public enum event_image_master {
            event_id, image_name, status, image_id, add_date
        }

        public enum event_attendee_master {
            user_id, event_id, status, follow_status, add_date, modify_date
        }


        public enum interestedin_purpose_master {
            id, purpose, add_date;
        }


        public enum user_starsandclubs {
            id, user_master_id, starsandclubs_master_id, status, add_date, modify_date, type, followers;
        }

        public enum job_master {
            id, name, status, add_date, modify_date;
        }


        public enum education_master {
            id, name, status, add_date, modify_date;
        }

        public enum starsandclubs_master {
            id, sac_type, sac_title, sac_description, sac_image, status, add_date, modify_date;
        }

        public enum nigiria_city_master {
            id, city_name, latitude, longitude, state_id, modify_date, add_date, status;
        }

        public enum nigiria_state_master {
            id, state_code, state_name, modify_date, add_date, status;
        }

        public enum country_master {
            id, country_code, country_name, country_isd_code, modify_date, add_date, status;
        }

        public enum user_notification_master {
            id, uuid, user_master_id, push_commentonpost, push_newmatchfriend, push_newchatmessage, status, push_rateonphoto, push_someonelikeyou,
            push_someonefavyou, push_someonesendgift, email_commentonpost, email_newmatchfriend, email_newchatmessage, email_rateonphoto,
            email_someonelikeyou, email_someonefavyou, email_someonesendgift, hide_account;
        }


        public enum gift_master {
            id, gift_title, gift_description, gift_image, cost, status, add_date, modify_date
        }


        public enum height_master {
            id, length, status, date_date, modify_date
        }

        public enum weight_master {
            id, weight, status, date_date, modify_date
        }

        public enum user_master {
            id, fb_user_id, first_name, rating, pass_status, last_name, email, user_name, password, gender, phone_number, profile_pic, handle_description,
            address, state, city, country_master_id, pin_code, date_of_birth, profile_type, latitude, longitude, hide_account,
            interestedin_master_id, bodytype_master_id, social_network_id, social_network_type, points, auth_token, uuid, about_myself,
            status, server_id, add_date, modify_date, relationship_master_id, sexuality_master_id, height_master_id, weight_master_id, hair_color_master_id, eye_color_master_id, education_master_id, job_master_id, meet_min_age, meet_max_age, interestedin_purpose_master_id, location, last_seen, pass_7days_expiry_time, friend_status, favourite_status, backstage_album_cost, gift_count, photo_count, rate_count, advertise_me_expiry_time, verification_level, login_status
        }


        public enum bodytype_master {
            id, name, bodytype_content
        }

        public enum relationship_master {
            id, name, status, date_date, modify_date
        }


        public enum eye_color_master {
            id, color, status, date_date, modify_date
        }

        public enum hair_color_master {
            id, color, status, date_date, modify_date
        }


        public enum interestedin_master {
            id, interestedin_content
        }

        public enum sexuality_master {
            id, name, status, date_date, modify_date
        }


        // ------------------2------------------------------
        public enum sg_user_transactions {
            id, item, item_id, add_date, modify_date
        }

        // ------------------3------------------------------
        public enum sg_user_skill_masters {
            id, uuid, skill_id, status, add_date, modify_date
        }

        // -----------------4-------------------------------
        public enum sg_user_roles {
            id, uuid, cercel_id, name, status, add_date, modify_date
        }

        // -------------------5-----------------------------
        public enum sg_user_favourite_list {
            id, uuid, fav_user_id, add_date, modify_date
        }

        // ---------------------6---------------------------
        public enum sg_users_experiences {
            id, account_id, company_name, joining_date, worked_till, designation, job_description, add_date, modify_date
        }

        // --------------------7----------------------------
        public enum sg_user_descriptions {
            uuid, account_id, response_rate, about_me, badges, introductory_media, type, responce_avg_time, profile_pic, certificates_status, how_good, what_user_do, highest_education, street_address, address2, city, state, country, latitude, longitude, zip_code, contact_number
        }

        // --------------------8----------------------------
        public enum sg_user_certificates {
            id, account_id, type, certificate_name, description, add_date, modify_date, certificate_image, certification_date
        }

        // ---------------------9--------------------------
        public enum sg_skills {
            id, skill_name_eng, status, skill_category_id, add_date, modify_date
        }

        // -------------------10---------------------------
        public enum sg_security_questions {
            id, question, created_on, updated_on, status
        }

        // -------------------11---------------------------
        public enum sg_courses {
            id, course_name, created_by, level, creation_on, status, updated_on, course_fees, duration, course_parent_id, fee_code, exam_associated_code, languages, related_skills, course_logo, course_video, course_syllabus, maximum_grokee, prerequistes, location, published_on
        }

        // --------------------12----------------------------
        public enum sg_language_data_masters {
            label_id, language_id, table_name, created_on, updated_on, label_data
        }

        // --------------------13----------------------------
        public enum sg_language_masters {
            id, language_name, language_code
        }

        // -------------------14-----------------------------
        public enum sg_skill_categories {
            id, category_name, status, created_on, updated_on
        }

        // --------------------15----------------------------
        public enum sg_country {
            id, country_code, country_name, country_name_ar
        }

        // ---------------------16---------------------------
        public enum sg_subscription_plans {
            id, plan_name, plan_type, plan_price, plan_duration, plan_description, created_on, updated_on, status
        }

        // ------------------------17------------------------
        public enum sg_subscription_features {
            id, subscription_plan_id, features, status, add_date, modify_date
        }


        // ------------------------18------------------------
        public enum sg_circles {
            id, user_id, circle_name, introductory_text, privacy_type_code, status, keywords, circle_profile_pic, circle_theme, city, country, created_on, updated_on
        }

        // ------------------------18 B------------------------
        public enum sg_circles_search {
            id, user_id, circle_name, introductory_text, privacy_type_code, status, keywords, circle_profile_pic, circle_theme, city, country, created_on, updated_on
        }

        // ------------------------19------------------------
        public enum sg_circle_members {
            id, user_id, circle_id, joined_on, updated_on, status
        }

        // ------------------------20------------------------
        public enum sg_circle_events {
            id, circle_id, event_name, event_start, event_end, event_info, created_on, udpated_on, status
        }

        // ------------------------21------------------------
        public enum sg_likes {
            id, feed_id, liked_by, add_date, status, type;
        }

        // ------------------------22------------------------
        public enum sg_comments {
            id, feed_id, created_by, text, created_on, updated_on, status
        }

        // ------------------------23------------------------
        public enum sg_feeds {
            id, uid, text, media_exist_status, status, created_on, updated_on, action, circle_id, type
        }

        // ------------------------24------------------------
        public enum sg_feed_media {
            id, feed_id, media_name, media_type, status, add_date
        }

        // ------------------------25------------------------
        public enum sg_shares {
            id, feed_id, shared_by, created_on, updated_on, status;
        }

        // ------------------------26------------------------
        public enum sg_report_abuses {
            id, user_id, entity_id, type, created_on, updated_on, status;
        }

        // ------------------------27------------------------

        public enum sg_user_account_details {
            id, uuid, paymode, paypal_email, account_name, account_number, country, bank_name, swift_code, bank_address1, bank_address2, remark, status, is_default, sync_time;
        }

        // ------------------------28------------------------

        public enum sg_user_education_details {
            id, uuid, college_name, degree_name, start_date, end_date, is_current, grade, description, add_date, modify_date, status;
        }

        // ------------------------29------------------------

        public enum sg_user_privacy_settings {
            id, uuid, online_status, exact_location, contact_me, company_name, my_favourite_circles;
        }
		
		
		
		/*// ------------------------29------------------------
		public enum sg_feed_types {
			id, type_name, add_date, modify_date
		}*/


        // // ------------------------------------------------
        // public enum place_image_master {
        // place_id, image_name, image_id, status, add_date
        // }
        //
        // // ------------------------------------------------
        // public enum place_feed_master {
        // id, user_id, place_id, post_comment, action, feed_liked, like_count,
        // image_exist_status, status, add_date, modify_date
        // }
        //
        // // ------------------------------------------------
        // public enum place_feed_images {
        // place_feed_id, image_name, status, image_id, add_date
        // }
        //
        // // ------------------------------------------------
        // public enum hash_master {
        // id, hash_data, hash_type_code, relational_id, status, add_date
        // }
        //
        // // ------------------------------------------------
        // public enum hash_type {
        // id, type_name, type_code, status, add_date
        // }
        //
        // // ------------------------------------------------
        // public enum android_metadata {
        // local
        // }
        //
        // // ------------------------------------------------
        // public enum country {
        // id, country_code, country_name
        // }
        //
        // // ------------------------------------------------
        // public enum message_master {
        // message_id, sender_id, recipient_id, message, status, image,
        // send_time
        // }
        //
        // // ------------------------------------------------
        // public enum comment_master {
        // comment_id, comment_text, user_id, comment_type, status, feed_id,
        // add_date
        // }
    }

    // =================================
    public interface DBListener {

        /**
         * Executed when data is fetched via rawQuery
         */
        public void onDataFetchedSucessfully(int queryId, Cursor c);
    }

    // ============ DB HELPER CLASS =====================

    class DBHelper extends SQLiteOpenHelper {

        //
        public DBHelper(Context context, String name, CursorFactory factory, int version) {

            super(context, name, factory, version);
        }

        //
        @Override
        public void onCreate(SQLiteDatabase db) {

            // createTables(db);
        }

        //
        // private void createTables(SQLiteDatabase db) {
        //
        // try {
        //
        // db.execSQL("PRAGMA foreign_keys=ON;");
        //
        // String sql = null;
        //
        // // PendingApi.Table.create(db);
        //
        // sql = " create table if not exists `" + Table.Name.user_master
        // + "` (`" + Table.user_master.id
        // + "` integer primary key, `" + Table.user_master.fb_user_id
        // + "` text, `" + Table.user_master.first_name
        // + "` varchar(255), `" + Table.user_master.sur_name
        // + "` varchar(255), `" + Table.user_master.email
        // + "` varchar(255), `" + Table.user_master.gender
        // + "` integer, `" + Table.user_master.image + "` text, `"
        // + Table.user_master.password + "` text, `"
        // + Table.user_master.street_address + "` text, `"
        // + Table.user_master.city + "` text, `"
        // + Table.user_master.zip + "` text, `"
        // + Table.user_master.date_of_birth + "` datetime, `"
        // + Table.user_master.profile_privacy_id + "` integer, `"
        // + Table.user_master.status + "` integer, `"
        // + Table.user_master.add_date + "` datetime, `"
        // + Table.user_master.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `"
        // + Table.Name.ProfilePrivacyInfo + "` (`"
        // + Table.ProfilePrivacyInfo.id
        // + "` integer primary key, `"
        // + Table.ProfilePrivacyInfo.P_type + "` varchar(255), `"
        // + Table.ProfilePrivacyInfo.status + "` integer, `"
        // + Table.ProfilePrivacyInfo.add_date + "` datetime, `"
        // + Table.ProfilePrivacyInfo.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.CategoryInfo
        // + "` (`" + Table.CategoryInfo.id
        // + "` integer primary key, `"
        // + Table.CategoryInfo.c_type + "` varchar(255), `"
        // + Table.CategoryInfo.status + "` integer, `"
        // + Table.CategoryInfo.add_date + "` datetime, `"
        // + Table.CategoryInfo.modify_date + "` datetime)";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.PlaceInfo
        // + "` (`" + Table.PlaceInfo.id
        // + "` integer primary key, `" + Table.PlaceInfo.name
        // + "` varchar(255), `" + Table.PlaceInfo.street_address
        // + "` text, `" + Table.PlaceInfo.city + "` text, `"
        // + Table.PlaceInfo.zip + "` integer, `"
        // + Table.PlaceInfo.description + "` text, `"
        // + Table.PlaceInfo.category_id + "` text, `"
        // + Table.PlaceInfo.user_id + "` integer, `"
        // + Table.PlaceInfo.image_id + "` integer, `"
        // + Table.PlaceInfo.status + "` integer, `"
        // + Table.PlaceInfo.add_date + "` datetime, `"
        // + Table.PlaceInfo.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `"
        // + Table.Name.PlaceImageInfo + "` (`"
        // + Table.PlaceImageInfo.id + "` integer primary key, `"
        // + Table.PlaceImageInfo.place_id + "` integer, `"
        // + Table.PlaceImageInfo.photo + "` text, `"
        // + Table.PlaceImageInfo.status + "` integer, `"
        // + Table.PlaceImageInfo.add_date + "` datetime, `"
        // + Table.PlaceImageInfo.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.LikeInfo
        // + "` (`" + Table.LikeInfo.id
        // + "` integer primary key, `" + "` integer, `"
        // + Table.LikeInfo.feed_id + "` integer, `"
        // + Table.LikeInfo.user_id + "` integer, `"
        // + Table.LikeInfo.add_date + "` datetime, `"
        // + Table.LikeInfo.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.EventInfo
        // + "` (`" + Table.EventInfo.id
        // + "` integer primary key, `" + Table.EventInfo.name
        // + "` varchar(255), `" + Table.EventInfo.event_Time
        // + "` datetime, `" + Table.EventInfo.place_id
        // + "` integer, `" + Table.EventInfo.post_id
        // + "` integer, `" + Table.EventInfo.user_id
        // + "` integer, `" + Table.EventInfo.image_id
        // + "` integer, `" + Table.EventInfo.status
        // + "` integer, `" + Table.EventInfo.add_date
        // + "` datetime, `" + Table.EventInfo.modify_date
        // + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.FeedInfo
        // + "` (`" + Table.FeedInfo.id
        // + "` integer primary key, `" + Table.FeedInfo.feed_text
        // + "` text, `" + Table.FeedInfo.image_id
        // + "` integer, `" + Table.FeedInfo.user_id
        // + "` integer, `" + Table.FeedInfo.follower_id
        // + "` integer, `" + Table.FeedInfo.follower_type
        // + "` integer, `" + Table.FeedInfo.tag_id
        // + "` integer, `" + Table.FeedInfo.place_id
        // + "` integer, `" + Table.FeedInfo.event_id
        // + "` integer, `" + Table.FeedInfo.status
        // + "` integer, `" + Table.FeedInfo.add_date
        // + "` datetime, `" + Table.FeedInfo.modify_date
        // + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `"
        // + Table.Name.FollowersInfo + "` (`"
        // + Table.FollowersInfo.id + "` integer primary key, `"
        // + Table.FollowersInfo.user_id + "` integer, `"
        // + Table.FollowersInfo.follower_id + "` integer, `"
        // + Table.FollowersInfo.place_id + "` integer, `"
        // + Table.FollowersInfo.privacy_id + "` integer, `"
        // + Table.FollowersInfo.status + "` integer, `"
        // + Table.FollowersInfo.add_date + "` datetime, `"
        // + Table.FollowersInfo.modify_date + "` datetime )";
        // db.execSQL(sql);
        //
        // sql = " create table if not exists `" + Table.Name.ShareInfo
        // + "` (`" + Table.ShareInfo.id
        // + "` integer primary key, `" + Table.ShareInfo.user_id
        // + "` integer, `" + Table.ShareInfo.Receiver_id
        // + "` integer, `" + Table.ShareInfo.feed_id
        // + "` integer, `" + Table.ShareInfo.status
        // + "` integer, `" + Table.ShareInfo.add_date
        // + "` datetime, `" + Table.ShareInfo.modify_date
        // + "` datetime )";
        // db.execSQL(sql);
        //
        // insertAnyInitialRecords(db);
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }

        //
        @SuppressWarnings("unused")
        private void insertAnyInitialRecords(SQLiteDatabase db) {

        }

        //
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {

                // String str =
                // "create table comment_master(comment_id INTEGER NOT NULL, comment_text TEXT, comment_type INTEGER, status INTEGER, feed_id INTEGER, add_date TEXT, user_id TEXT);";
                // db.execSQL(str);

            }
        }
    }

    public static void copyDBFromAssets(Context _context, File df) throws IOException {

        // Open your local db as the input stream
        InputStream myInput = _context.getAssets().open(_DB_NAME);

        // Path to the just created empty db
        // String outFileName = _DB_PATH + _DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(df);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
}