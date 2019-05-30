package it.univaq.mobileprogramming.uniweather.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationGoogleService {

    private FusedLocationProviderClient providerClient;

    private LocationListener listener;

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(listener != null) listener.onLocationChanged(locationResult.getLastLocation());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            System.out.println("Is location available? " + locationAvailability.isLocationAvailable());
        }
    };

    public void onCreate(Activity activity, LocationListener listener){
        providerClient = LocationServices.getFusedLocationProviderClient(activity);
        this.listener = listener;
    }

    public boolean areGoogleServicesAvailable(Context context){

        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    public boolean requestLocationUpdates(Context context){

        int permissionFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCoarseLocation == PackageManager.PERMISSION_GRANTED || permissionFineLocation == PackageManager.PERMISSION_GRANTED) {

            if(areGoogleServicesAvailable(context)) {
                LocationRequest request = new LocationRequest();
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                request.setInterval(0);
                request.setFastestInterval(0);

                providerClient.requestLocationUpdates(request, locationCallback, null);

                return true;
            }
        }
        return false;
    }

    public void stopLocationUpdates(Context context){
        if(areGoogleServicesAvailable(context)) {
            providerClient.removeLocationUpdates(locationCallback);
        }
    }

    public interface LocationListener {
        void onLocationChanged(Location location);
    }
}
