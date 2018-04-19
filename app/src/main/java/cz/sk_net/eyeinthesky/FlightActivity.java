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

public class FlightActivity extends AppCompatActivity implements LocationListener {

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

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        Location locPPV = new Location("hardcoded");
        locPPV.setLatitude(49.2324722);
        locPPV.setLongitude(16.5701389);

        TextView tx_gps = findViewById(R.id.tx_gps);
        TextView tx_gps_acc = findViewById(R.id.tx_gps_acc);
        TextView tx_alt = findViewById(R.id.tx_alt);
        TextView tx_vel = findViewById(R.id.tx_vel);
        TextView tx_bear = findViewById(R.id.tx_bear);
        TextView tx_dist = findViewById(R.id.tx_dist);
        TextView tx_bear_to = findViewById(R.id.tx_bear_to);

        tx_gps.setText(location.getLatitude() + "° N\n" + location.getLongitude() + "° E");
        tx_gps_acc.setText("(" + location.getAccuracy() + " m)");
        tx_alt.setText(location.getAltitude() + " m");
        tx_vel.setText(location.getSpeed() + " m/s");
        tx_bear.setText(location.getBearing() + "°");
        tx_dist.setText(location.distanceTo(locPPV) + " m");
        tx_bear_to.setText(location.bearingTo(locPPV) + "°");
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
