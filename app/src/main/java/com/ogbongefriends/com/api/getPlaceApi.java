package com.ogbongefriends.com.api;

import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.Utils;
import com.ogbongefriends.com.custom.BasicApi;


public class getPlaceApi extends BasicApi implements Runnable {

	private DB db;
	private String url;
	private Context ctx;
	private String PForUserId;
	public static int resCode;
	public static String resMsg;
	CustomLoader p;

	public getPlaceApi(Context ctx, DB db, String PlaceForUserId, CustomLoader p) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;
		PForUserId = PlaceForUserId;
		String endurl = assembleUrl(PlaceForUserId);

		String startUrl = Utils.getCompleteApiUrl(ctx, R.string.getPlace_api);
		url = startUrl + endurl;

	}

	public getPlaceApi(Context ctx, DB db, CustomLoader p, String PlaceId) {
		super(ctx, db);
		this.db = db;
		this.ctx = ctx;
		this.p = p;

		String endurl = assembleUrlOnePlace(PlaceId);

		String startUrl = Utils.getCompleteApiUrl(ctx, R.string.getPlace_api);
		url = startUrl + endurl;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (url == null) {
			Utils.log(Constants.kApiTag, "Url Not Found in Login Api");
		} else {

			if (Utils.CNet()) {

				callUrl(url);
				// postData(url, postData);
			} else {
				p.cancel();
				Utils.NoInternet(ctx);
			}
		}
	}

	@Override
	protected void onResponseReceived(InputStream is) {
		// TODO Auto-generated method stub
		try {

			String res = getString(is);
			Log.d("Responce>>>>>>>>>>>>"+url ,res);
			JsonParser p = new JsonParser();
			JsonElement jele = p.parse(res);
			JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject()
					: null;

			if (obj == null) {
				onError(new ApiException(Constants.kApiError));
			} else {

				JsonObject objJson = obj.get("tmwtg").getAsJsonObject();

				resCode = objJson.get("res_code").getAsInt();
				resMsg = objJson.get("res_msg").getAsString();

				Log.d("place Returned json", "" + objJson.toString());
				JsonArray innerJson = objJson.get("Places").getAsJsonArray();

				HashMap<String, String> rowplacedata;
				HashMap<String, String> PlacesPics;

				// int k = 0, kp = 0;
				for (int i = 0; i < innerJson.size(); i++) {
					// k = 0;

					rowplacedata = new HashMap<String, String>();

					rowplacedata.put("id", innerJson.get(i).getAsJsonObject()
							.get("place_id").getAsString());
					rowplacedata.put("name", innerJson.get(i).getAsJsonObject()
							.get("place_name").getAsString());
					rowplacedata.put("street_address", innerJson.get(i)
							.getAsJsonObject().get("street_address")
							.getAsString());
					rowplacedata.put("city", innerJson.get(i).getAsJsonObject()
							.get("city").getAsString());
					try {
						rowplacedata.put("zip", innerJson.get(i)
								.getAsJsonObject().get("zip").getAsString());
					} catch (Exception e) {

					}

					rowplacedata.put("owner_id", innerJson.get(i)
							.getAsJsonObject().get("owner_id").getAsString());
					rowplacedata
							.put("description", innerJson.get(i)
									.getAsJsonObject().get("description")
									.getAsString());
					rowplacedata.put("category_id", innerJson.get(i)
							.getAsJsonObject().get("place_category")
							.getAsString());

					JsonArray placePics = innerJson.get(i).getAsJsonObject()
							.get("place_photo").getAsJsonArray();
//
//					db.autoInsertUpdate(
//							Table.Name.place_master,
//							rowplacedata,
//							Table.place_master.id
//									+ " = "
//									+ innerJson.get(i).getAsJsonObject()
//											.get("place_id").getAsString(),
//							null);

					for (int j = 0; j < placePics.size(); j++) {
						// kp = 0;

						PlacesPics = new HashMap<String, String>();

						PlacesPics.put("place_id", innerJson.get(i)
								.getAsJsonObject().get("place_id")
								.getAsString());
						PlacesPics.put("image_name", placePics.get(j)
								.getAsJsonObject().get("img_name")
								.getAsString());
						PlacesPics.put("status", placePics.get(j)
								.getAsJsonObject().get("status").getAsString());
						PlacesPics.put("image_id", placePics.get(j)
								.getAsJsonObject().get("img_id").getAsString());

						String pid = innerJson.get(i).getAsJsonObject()
								.get("place_id").getAsString();

						String imid = placePics.get(j).getAsJsonObject()
								.get("img_id").getAsString();

//						db.autoInsertUpdate(Table.Name.place_image_master,
//								PlacesPics, Table.place_image_master.place_id
//										+ " = " + pid + " AND "
//										+ Table.place_image_master.image_id
//										+ " = " + imid, null);

					}

				}

			}
		} catch (Exception e) {
			onError(e);

		}

	}

	@Override
	protected void onError(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDone() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateUI() {
		// TODO Auto-generated method stub

	}

	private String assembleUrl(String PlaceForUserId) {
		// TODO Auto-generated method stub
		String endurl = "?auth_token=" + Utils.getCurrentAuthToken(ctx)
				+ "&uuid=" + Utils.getCurrentUserID(ctx) + "&place_user_id="
				+ PlaceForUserId + "&client_idd="
				+ Utils.getCurrentClientId(ctx) + "&sync_date= ";

		return endurl;
	}

	// ===============

	private String assembleUrlOnePlace(String PlaceId) {
		// TODO Auto-generated method stub
		String endurl = "?auth_token=" + Utils.getCurrentAuthToken(ctx)
				+ "&uuid=" + Utils.getCurrentUserID(ctx) + "&place_user_id="
				+ "" + "&place_id=" + PlaceId + "&client_idd="
				+ Utils.getCurrentClientId(ctx) + "&sync_date= ";

		return endurl;
	}

}
