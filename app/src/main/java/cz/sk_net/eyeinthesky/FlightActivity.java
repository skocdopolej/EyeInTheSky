package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FlightActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    ArrayList<WayPoint> arrLocation = new ArrayList<>();
    int txvId[] = new int[9];
    int wpCtr = 0;
    String telemetry = "----TELEMETRY RECORD----\n";

    private TextView txv_wp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_flight);

        Button btn_end = findViewById(R.id.btn_end);
        Button btn_prev = findViewById(R.id.btn_prev);
        Button btn_next = findViewById(R.id.btn_next);

        btn_end.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        txv_wp = findViewById(R.id.txv_wp);

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

        TableLayout tbl_grid = findViewById(R.id.tbl_grid);

        for (int i = 0; i < 3; i++) {

            // Row
            TableRow tbl_row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tbl_row.setLayoutParams(lp);

            for (int j = 0; j < 3; j++) {

                // Cell
                TextView txv_wp = new TextView(this);
                txv_wp.setText((i * 3 + j) + "");
                txv_wp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 512/6);
                txv_wp.setWidth(512/3);
                txv_wp.setHeight(512/3);
                txv_wp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                txv_wp.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                txv_wp.setId((txvId[i * 3 + j] = View.generateViewId()));

                tbl_row.addView(txv_wp);
            }

            tbl_grid.addView(tbl_row, i);
        }

        arrLocation.add(new WayPoint(49.441051, 15.767174));
        arrLocation.add(new WayPoint(49.44084, 15.76696));
        arrLocation.add(new WayPoint(49.44012, 15.7664));
        arrLocation.add(new WayPoint(49.43932, 15.76708));
        arrLocation.add(new WayPoint(49.43785, 15.76986));
        arrLocation.add(new WayPoint(49.43708, 15.76939));
        arrLocation.add(new WayPoint(49.43823, 15.76725));
        arrLocation.add(new WayPoint(49.43924, 15.76592));
        arrLocation.add(new WayPoint(49.4384, 15.76806));

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView tx_gps = findViewById(R.id.tx_gps);
        TextView tx_alt = findViewById(R.id.tx_alt);
        TextView tx_vel = findViewById(R.id.tx_vel);
        TextView tx_bear = findViewById(R.id.tx_bear);
        TextView tx_wp = findViewById(R.id.tx_wp);
        TextView tx_dist = findViewById(R.id.tx_dist);
        TextView tx_bear_to = findViewById(R.id.tx_bear_to);

        float bearing = location.getBearing();
        if (bearing < 0) {

            bearing += 360;
        }

        float bearingTo = location.bearingTo(arrLocation.get(wpCtr).getLocation());
        if (bearingTo < 0) {

            bearingTo += 360;
        }

        tx_gps.setText(String.format("%.6f", location.getLatitude()) + "° N\n" + String.format("%.6f", location.getLongitude()) + "° E");
        tx_alt.setText(location.getAltitude() + " m");
        tx_vel.setText(location.getSpeed() + " m/s");
        tx_bear.setText(bearing + "°");
        tx_wp.setText("WP " + wpCtr + ":");
        tx_dist.setText(location.distanceTo(arrLocation.get(wpCtr).getLocation()) + " m");
        tx_bear_to.setText(bearingTo + "°");

        for (int i = 0; i < arrLocation.size(); i++) {

            if (!arrLocation.get(i).isPhotoTaken()) {

                if (location.distanceTo(arrLocation.get(i).getLocation()) < 10) {

                    Toast.makeText(this, "You've reached position", Toast.LENGTH_SHORT).show();
                    arrLocation.get(i).setPhotoTaken(true);
                    findViewById(txvId[i]).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

                    telemetry += "Waypoint:\t" + wpCtr + "\n"
                            + "WP Loc:\t\t" + arrLocation.get(wpCtr).getLocation().getLatitude() + "° N," + arrLocation.get(wpCtr).getLocation().getLongitude() + "° E\n"
                            + "GPS:\t\t\t" + location.getLatitude() + "° N," + location.getLatitude() + "° E\n"
                            + "Bearing To:\t" + bearingTo + "\n"
                            + "Distance:\t\t" + location.distanceTo(arrLocation.get(wpCtr).getLocation()) + "\n"
                            + "Time:\t\t\t" + location.getTime() + "\n"
                            + "Accuracy:\t" + location.getAccuracy() + " m\n"
                            + "Altitude:\t\t" + location.getAltitude() + " m\n"
                            + "Bearing:\t\t" + bearing + "°\n"
                            + "Speed:\t\t\t" + location.getSpeed() + " m/s\n\n";

                    wpCtr++;

                    if (wpCtr > arrLocation.size() - 1) {
                        wpCtr = 0;
                    }
                    txv_wp.setText("" + wpCtr);
                }
            }
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_end:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", 0);

                telemetry += "\n----END OF TELEMETRY RECORD----\n";
                writeToFile(telemetry);

                setResult(1, resultIntent);
                finish();
                break;

            case R.id.btn_prev:
                wpCtr--;
                if (wpCtr < 0) {
                    wpCtr = arrLocation.size() - 1;
                }
                txv_wp.setText("" + wpCtr);
                break;

            case R.id.btn_next:
                wpCtr++;
                if (wpCtr > arrLocation.size() - 1) {
                    wpCtr = 0;
                }
                txv_wp.setText("" + wpCtr);
                break;
        }
    }

    protected void writeToFile(String string) {

        int permission = ActivityCompat.checkSelfPermission(FlightActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FlightActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/telemetry.txt");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (new File(file.getAbsolutePath()), true);
            outputStream.write(string.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
