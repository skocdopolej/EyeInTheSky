package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class SensorLocation implements LocationListener, Runnable {

    private Context context;
    private Location loc;
    private Handler handlerLocation;
    private LocationManager locManager;


    SensorLocation(Context context, Handler handlerLocation) {
        this.handlerLocation = handlerLocation;
        this.context = context;
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(context, "Location service is not permitted", Toast.LENGTH_LONG).show();
            return;
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //loc = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not in use.
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Not in use.
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Not in use.
    }

    @Override
    public void run() {

        for (int i = 0; i < 64; i++) {

            Message msgLocation = handlerLocation.obtainMessage();
            msgLocation.arg1 = i;
            handlerLocation.sendMessage(msgLocation);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
