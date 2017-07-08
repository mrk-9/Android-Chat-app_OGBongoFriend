package com.ogbongefriends.com.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.util.Log;

public class GetBackFragment {

	static int ProfilePage         	= 0;
	static int NewsFeed 		  	= 1;
	static int ShowPlace 		   	= 4;
	static int ShowEvents 			= 12;
	static int UpdateCategory 		= 14;
	static int UpdatePersonalInfo 	= 50;
	static int UpdateAddressInfo 	= 51;
	static int UpdatePassword 		= 52;
	static int UpdateAccountInfo	= 53;
	static int PlaceDetailPage 		= 60;
	static int EventDetailPage 		= 61;

	static Fragment fragment;

	static List<Integer> fpostion_list = new ArrayList<Integer>();
	static List<Long> profilelist = new ArrayList<Long>();

	public static String Framentname(int name) {

		if (name == 1) {
			return "Profile";
		} else if (name == 3) {
			return "NewsFeed";
		} else if (name == 4) {
			return "Show Place";
		} else if (name == 5) {
			return "show Message";
		} else if (name == 7) {
			return "SomeOne Free Tonight";
		} else if (name == 8) {
			return "SomeOne Secretly follow";
		} else if (name == 102) {
			return "SomeOne you like";
		} else if (name == 11) {
			return "Show Events";
		} else if (name == 13) {
			return "Update Profile";
		} else if (name == 14) {
			return "Logout";
		}else if (name == 80) {
			return "Search page";
		}
		return null;

	}

	// // ===============================
	//
	// public static String AddtoStack(String name) {
	//
	// list.add(list.size(), name);
	//
	// Log.v("added to statck", list.toString() + "==" + list.size());
	//
	// return name;
	//
	// }
	//
	// // ===============================

	public static long AddUUID(long id) {

		profilelist.add(profilelist.size(), id);

		return id;

	}

	// ===============================

	public static long LastUUID() {
		Log.v("last profile", profilelist + "");
		if (profilelist.size() > 0) {
			return profilelist.get(profilelist.size() - 1);
		} else {
			return 0;
		}

	}

	// ===============================

	public static int Removelast() {

		if (profilelist.size() > 0) {

			profilelist.remove(profilelist.size() - 1);

			return profilelist.size();

		} else {

			return 0;

		}

	}

	// // ===============================
	//
	// public static String LastAtStack() {
	//
	// if (list.size() > 1) {
	// return list.get(list.size() - 2);
	// } else {
	// return null;
	// }
	//
	// }
	//
	// // ===============================
	//
	// public static int RemovetoStack() {
	//
	// if (list.size() > 0) {
	//
	// list.remove(list.size() - 1);
	//
	// return list.size();
	//
	// } else {
	//
	// return 0;
	//
	// }
	//
	// }
	//
	// // ===============================
	//
	public static void ClearStack() {

		fpostion_list.clear();

	}

	// ===============================

	public static long Addpos(int pos) {

		fpostion_list.add(fpostion_list.size(), pos);

		return pos;

	}

	// ===============================

	public static int Removepos() {

		if (fpostion_list.size() > 0) {

			fpostion_list.remove(fpostion_list.size() - 1);

			return fpostion_list.size();

		} else {

			return -1;

		}

	}

	// ===============================

	public static int Lastpos() {

		if (fpostion_list.size() > 1) {
			return fpostion_list.get(fpostion_list.size() - 2);
		} else {
			return -1;
		}

	}

}
