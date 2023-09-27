package com.intabux.gobussines.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDataBase;
    private GeoFire mGeofire;

    public GeofireProvider(String reference){
        mDataBase = FirebaseDatabase.getInstance().getReference().child(reference);
        mGeofire = new GeoFire(mDataBase);
    }
    public void saveLocation(String idDriver, LatLng latLng){
        mGeofire.setLocation(idDriver,new GeoLocation(latLng.latitude,latLng.longitude));
    }
    public void removeLocation(String idDriver){
        mGeofire.removeLocation(idDriver);
    }

    public GeoQuery getActiveDrivers(LatLng latLng, double radius){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
    public DatabaseReference getDriverLocation(String idClient){
        return mDataBase.child(idClient).child("l");
    }
}