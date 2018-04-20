package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FlightActivity extends AppCompatActivity implements LocationListener {

    ArrayList<Location> arrLocation = new ArrayList<>();
    int wpCtr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Button btn_end = findViewById(R.id.btn_end);

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", 0);

                setResult(1, resultIntent);
                finish();
            }
        });

        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FlightActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }

        if (locManager == null) {
            return;
        }

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.441776);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.768025);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.443405);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.762090);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.445326);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.770455);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.446250);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.775212);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.447984);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.777248);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.446521);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.778494);

        arrLocation.add(new Location("GoogleMaps"));
        arrLocation.get(arrLocation.size() - 1).setLatitude(49.446385);
        arrLocation.get(arrLocation.size() - 1).setLongitude(15.778215);

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        //Location locPPV = new Location("hardcoded");
        //locPPV.setLatitude(49.2324722);
        //locPPV.setLongitude(16.5701389);

        TextView tx_gps = findViewById(R.id.tx_gps);
        TextView tx_gps_acc = findViewById(R.id.tx_gps_acc);
        TextView tx_alt = findViewById(R.id.tx_alt);
        TextView tx_vel = findViewById(R.id.tx_vel);
        TextView tx_vel_kph = findViewById(R.id.tx_vel_kph);
        TextView tx_bear = findViewById(R.id.tx_bear);
        TextView tx_wp = findViewById(R.id.tx_wp);
        TextView tx_dist = findViewById(R.id.tx_dist);
        TextView tx_bear_to = findViewById(R.id.tx_bear_to);

        float bearing = location.getBearing();

        if (bearing < 0) {

            bearing += 360;
        }

        tx_gps.setText(location.getLatitude() + "° N\n" + location.getLongitude() + "° E");
        tx_gps_acc.setText("(" + location.getAccuracy() + " m)");
        tx_alt.setText(location.getAltitude() + " m");
        tx_vel.setText(location.getSpeed() + " m/s");
        tx_vel_kph.setText((location.getSpeed() * 3.6) + " km/h");
        tx_bear.setText(bearing + "°");
        tx_wp.setText("WP " + wpCtr + ":");
        tx_dist.setText(location.distanceTo(arrLocation.get(wpCtr)) + " m");
        tx_bear_to.setText(location.bearingTo(arrLocation.get(wpCtr)) + "°");

        if ((wpCtr < arrLocation.size()) && (location.distanceTo(arrLocation.get(wpCtr)) < 10)) {

            Toast.makeText(this, "You've reached position: " + wpCtr, Toast.LENGTH_SHORT).show();
            wpCtr++;
        }
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
}
