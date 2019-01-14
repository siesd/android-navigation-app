package com.saskpolytech.cst142;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * This class is the location helper that can be instantiated and used in multiple classes rather
 * than having the same code everywhere. It will allow us to get the current devices coordinates.
 * @author Dylan Sies (cst142)
 */
public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    public GoogleApiClient mGoogleApiClient;
    public double latestLong;
    public double latestLat;
    private Context currentActivity;


    //Constructor
    public LocationHelper(Context context)
    {
        //Build google client
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //Get the location every 5 seconds
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try
        {
            //Get location updates, throws exception if there is no location permissions
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        catch (SecurityException exp)
        {
            //No permissions
            Toast.makeText(currentActivity, "Unable to request location changes", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i)
    {
        //Let user know the connection was suspended
        Toast.makeText(currentActivity, "GPS connection suspended", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        //Let user know the connection has failed
        Toast.makeText(currentActivity, "GPS connection failed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        //Set the attributes that will be accesed by other classes to the current latitude and longitude
        latestLong = location.getLongitude();
        latestLat = location.getLatitude();
    }


}
