package com.ogbongefriends.com.ogbonge.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper implements LocationListener {
	// private static final String TAG = "LocationHelper";

	public interface MyLocationListener {
		public void onLocationUpdate(Location location);
	}

	private LocationManager lManager;
	private Location myLocation;
	private MyLocationListener locationListener;

	public LocationHelper(Context context) {
		lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		myLocation = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (myLocation == null) {
			myLocation = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}

	}

	/**
	 * this method is responsible for the dispatch location requests for the gps
	 * and network both
	 * 
	 * @param locationListener
	 * 
	 *            Problem arrise at 13-09-2013 Problem description : location
	 *            will find by only gps not aslo network Solution: start two
	 *            request for update his location with both gps and network
	 *            information
	 * 
	 * 
	 *            20-09-2013 : Problem arise now it is not finding a location of
	 *            the after start two services for the finding the location
	 * 
	 * 
	 */

	public void startLocationUpdates(MyLocationListener locationListener) {
		this.locationListener = locationListener;

		String updateProvider = LocationManager.NETWORK_PROVIDER;
		String gps_provider = LocationManager.GPS_PROVIDER;
if(lManager!=null) {
	lManager.requestLocationUpdates(gps_provider, 30000, 0, this);
	lManager.requestLocationUpdates(updateProvider, 30000, 0, this);
}

	}

	public Location getMyLocation() {
		return myLocation;
	}

	public void destroy() {
		lManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			myLocation = location;

			if (locationListener != null) {
				locationListener.onLocationUpdate(location);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// Log.i(TAG, "onProviderDisabled " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// Log.i(TAG, "onProviderEnabled ++" + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Log.i(TAG, "onStatusChanged");
	}
}
