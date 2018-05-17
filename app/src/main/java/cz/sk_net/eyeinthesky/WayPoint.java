package cz.sk_net.eyeinthesky;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class WayPoint {

    private Location location;
    private boolean photoTaken;

    WayPoint(double lat, double lng) {
        photoTaken = false;
        location = new Location("GoogleMaps");
        location.setLatitude(lat);
        location.setLongitude(lng);
    }

    public Location getLocation() {
        return location;
    }

    public boolean isPhotoTaken() {
        return photoTaken;
    }

    public void setPhotoTaken(boolean photoTaken) {
        this.photoTaken = photoTaken;
    }
}
