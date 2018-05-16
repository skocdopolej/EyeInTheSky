package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AreaSelectActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private int markerCounter = 0;
    private ArrayList<LatLng> arrLocation = new ArrayList<>();
    Area area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_area_select);

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
        Button btn_show = findViewById(R.id.btn_show);
        Button btn_clear = findViewById(R.id.btn_clear);

        btn_ok.setOnClickListener(this);
        btn_show.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerCounter < 3) {

                    arrLocation.add(new LatLng(latLng.latitude, latLng.longitude));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Point: " + markerCounter));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    markerCounter++;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ok:

                if (markerCounter == 3) {

                    if (area == null) {

                        area = new Area(
                                arrLocation.get(0).latitude, arrLocation.get(1).latitude, arrLocation.get(2).latitude,
                                arrLocation.get(0).longitude, arrLocation.get(1).longitude, arrLocation.get(2).longitude
                        );
                        area.computeArea();
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("area", area);
                    setResult(1, resultIntent);
                    finish();
                } else {
                    Toast.makeText(this, "3 points needed!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_show:
                if (markerCounter == 3) {

                    area = new Area(
                            arrLocation.get(0).latitude, arrLocation.get(1).latitude, arrLocation.get(2).latitude,
                            arrLocation.get(0).longitude, arrLocation.get(1).longitude, arrLocation.get(2).longitude
                    );
                    area.computeArea();

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(new LatLng(area.getLatA(), area.getLngA())));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(area.getLatB(), area.getLngB())));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(area.getLatAA(), area.getLngAA())));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(area.getLatBB(), area.getLngBB())));
                } else {
                    Toast.makeText(this, "3 points needed!", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btn_clear:
                mMap.clear();
                markerCounter = 0;
                arrLocation.clear();
                break;
        }
    }
}
