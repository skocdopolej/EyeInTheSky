package cz.sk_net.eyeinthesky;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class WayPoint {

    private Location location;
    private boolean photoTaken;
    private Telemetry telemetry;

    public WayPoint(double lat, double lng) {
        this.location = location;
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

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setPhotoTaken(boolean photoTaken) {
        this.photoTaken = photoTaken;
    }
}
