package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class MapSettingsSelectActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private int markerCounter = 0;
    private ArrayList<LatLng> arrLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_settings_select);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng Zhor = new LatLng(49.44120, 15.76772);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Zhor));

        Button btn_ok = findViewById(R.id.btn_calibrate);
        Button btn_clear = findViewById(R.id.btn_clear);

        btn_ok.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerCounter < 3) {

                    arrLocation.add(new LatLng(latLng.latitude, latLng.longitude));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("onMapClick()"));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    markerCounter++;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_calibrate:
                Area area = new Area(
                        arrLocation.get(0).latitude, arrLocation.get(1).latitude, arrLocation.get(2).latitude,
                        arrLocation.get(0).longitude, arrLocation.get(1).longitude, arrLocation.get(2).longitude
                );

                area.computeArea();

                mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(area.getXa(), area.getYa()),
                                new LatLng(area.getXb(), area.getYb()),
                                new LatLng(area.getXaa(), area.getYaa()),
                                new LatLng(area.getXbb(), area.getXbb()))
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("area", area);
                setResult(1, resultIntent);
                finish();
                break;

            case R.id.btn_clear:
                mMap.clear();
                markerCounter = 0;
                arrLocation.clear();
                break;
        }
    }
}
