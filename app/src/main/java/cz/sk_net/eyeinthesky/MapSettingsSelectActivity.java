package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class MapSettingsSelectActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private int markerCounter = 0;
    private ArrayList<Location> arrLocation = new ArrayList<>();

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

        Button btn_ok = findViewById(R.id.btn_ok);
        Button btn_clear = findViewById(R.id.btn_clear);

        btn_ok.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerCounter < 3) {


                    arrLocation.add(new Location("GoogleMaps"));
                    arrLocation.get(markerCounter).setLatitude(latLng.latitude);
                    arrLocation.get(markerCounter).setLongitude(latLng.longitude);

                    mMap.addMarker(new MarkerOptions().position(latLng).title("onMapClick()"));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    markerCounter++;
                } else {
                    Log.d("LOC: ", arrLocation.get(0).getLatitude() + "; " + arrLocation.get(1).getLongitude());
                    Log.d("LOC: ", arrLocation.get(1).getLatitude() + "; " + arrLocation.get(1).getLongitude());
                    Log.d("LOC: ", arrLocation.get(2).getLatitude() + "; " + arrLocation.get(2).getLongitude());
                    mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(arrLocation.get(0).getLatitude(), arrLocation.get(0).getLongitude()),
                                    new LatLng(arrLocation.get(1).getLatitude(), arrLocation.get(1).getLongitude()),
                                    new LatLng(arrLocation.get(2).getLatitude(), arrLocation.get(2).getLongitude()))
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", 0);
                setResult(1, resultIntent);
                finish();
                break;

            case R.id.btn_clear:
                mMap.clear();
                markerCounter = 0;
                arrLocation.clone();
                break;
        }
    }
}
