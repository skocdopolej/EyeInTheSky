package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightActivity extends AppCompatActivity implements LocationListener, SensorEventListener, View.OnClickListener {

    PositionSensor positionSensor;
    Camera camera;
    Area area;
    Flight flight;
    protected int tileCountX;
    protected int tileCountY;
    int txvId[] = new int[9];
    protected int wpCtr = 0;
    String telemetry = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<kml xmlns='http://www.opengis.net/kml/2.2'>\n";

    float[] m_lastAccel = new float[3];
    float[] m_lastMagFields = new float[3];
    private float[] m_rotationMatrix = new float[16];
    private float[] m_orientation = new float[4];

    private TextView txv_yaw;
    private TextView txv_roll;
    private TextView txv_pitch;
    private TextView tx_gps;
    private TextView tx_alt;
    private TextView tx_vel;
    private TextView tx_bear;
    private TextView tx_wp;
    private TextView tx_dist;
    private TextView tx_bear_to;
    private TextView txv_wp;

    private float calX = 0;
    private float calY = 0;
    private float calZ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_flight);

        // Get position corrections
        Intent intent = getIntent();

        if (intent.hasExtra("correction")) {

            positionSensor = (PositionSensor) intent.getSerializableExtra("correction");

            calX = positionSensor.getYawCorrection();
            calY = positionSensor.getRollCorrection();
            calZ = positionSensor.getPitchCorrection();
        }

        if (intent.hasExtra("camera")) {

            camera = (Camera) intent.getSerializableExtra("camera");
        }

        if (intent.hasExtra("area")) {

            area = (Area) intent.getSerializableExtra("area");
        }

        if (intent.hasExtra("flight")) {

            flight = (Flight) intent.getSerializableExtra("flight");
        }

        Button btn_end = findViewById(R.id.btn_end);
        Button btn_prev = findViewById(R.id.btn_prev);
        Button btn_next = findViewById(R.id.btn_next);

        txv_yaw = findViewById(R.id.txv_yaw);
        txv_roll = findViewById(R.id.txv_roll);
        txv_pitch = findViewById(R.id.txv_pitch);

        tx_gps = findViewById(R.id.tx_gps);
        tx_alt = findViewById(R.id.tx_alt);
        tx_vel = findViewById(R.id.tx_vel);
        tx_bear = findViewById(R.id.tx_bear);
        tx_wp = findViewById(R.id.tx_wp);
        tx_dist = findViewById(R.id.tx_dist);
        tx_bear_to = findViewById(R.id.tx_bear_to);
        txv_wp = findViewById(R.id.txv_wp);

        btn_end.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        // Location and position manager
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SensorManager SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Check permissions
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

        if (area.getPath() != null) {

            loadArea(area.getPath());
            tileCountX = (int) Math.sqrt(area.getArrLocation().size());
            tileCountY = area.getArrLocation().size() / tileCountX;
        } else {

            Location a = new Location("GoogleMaps");
            Location b = new Location("GoogleMaps");
            Location aa = new Location("GoogleMaps");
            Location bb = new Location("GoogleMaps");

            a.setLatitude(area.getLatA());
            a.setLongitude(area.getLngA());
            b.setLatitude(area.getLatB());
            b.setLongitude(area.getLngB());
            aa.setLatitude(area.getLatAA());
            aa.setLongitude(area.getLngAA());
            bb.setLatitude(area.getLatBB());
            bb.setLongitude(area.getLngBB());

            area.getWayPoints(a.distanceTo(b), a.distanceTo(aa), area.getDeltaX(), area.getDeltaY(), a.bearingTo(b));

            tileCountX = area.getTileCountX();
            tileCountY = area.getTileCountY();
        }

        TableLayout tbl_grid = findViewById(R.id.tbl_grid);

        for (int i = 0; i < tileCountX; i++) {

            // Row
            TableRow tbl_row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tbl_row.setLayoutParams(lp);

            for (int j = 0; j < tileCountY; j++) {

                // Cell
                TextView txv_wp = new TextView(this);
                txv_wp.setText((i * tileCountX + j) + "");
                txv_wp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 512/(tileCountX * 2));
                txv_wp.setWidth(512/tileCountX);
                txv_wp.setHeight(512/tileCountY);
                txv_wp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                txv_wp.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                txv_wp.setId((txvId[i * tileCountX + j] = View.generateViewId()));

                tbl_row.addView(txv_wp);
            }

            tbl_grid.addView(tbl_row, i);
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);

        // Position
        // Sensor
        assert SM != null;
        Sensor myAcc = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor myGeo = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register
        SM.registerListener(this, myAcc, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this, myGeo, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onLocationChanged(Location location) {

        float bearing = location.getBearing();
        if (bearing < 0) {

            bearing += 360;
        }

        float bearingTo = location.bearingTo(area.getArrLocation().get(wpCtr).getLocation());
        if (bearingTo < 0) {

            bearingTo += 360;
        }

        float yaw = (float)Math.toDegrees(m_orientation[0] - calX);
        if (yaw < 0) {

            yaw += 360;
        }

        tx_gps.setText(String.format("%.6f", location.getLatitude()) + "° N\n" + String.format("%.6f", location.getLongitude()) + "° E");
        tx_alt.setText(location.getAltitude() + " m");
        tx_vel.setText(location.getSpeed() + " m/s");
        tx_bear.setText(bearing + "°");
        tx_wp.setText("WP: " + wpCtr);
        tx_dist.setText(String.format("%.2f", location.distanceTo(area.getArrLocation().get(wpCtr).getLocation())) + " m");
        tx_bear_to.setText(String.format("%.2f", bearingTo) + "°");

        if (!area.getArrLocation().get(wpCtr).isPhotoTaken()) {

            if (location.distanceTo(area.getArrLocation().get(wpCtr).getLocation()) < flight.getDistance()) {

                if (Math.abs(Math.toDegrees(m_orientation[1] - calY)) > flight.getAngle()) {

                    Toast.makeText(this, "Roll over limit", Toast.LENGTH_SHORT).show();
                    findViewById(txvId[wpCtr]).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    return;
                }

                if (Math.abs(Math.toDegrees(m_orientation[2] - calZ)) > flight.getAngle()) {

                    Toast.makeText(this, "Pitch over limit!", Toast.LENGTH_SHORT).show();
                    findViewById(txvId[wpCtr]).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    return;
                }

                camera.takePhoto();
                Toast.makeText(this, "Pohoto taken.", Toast.LENGTH_SHORT).show();
                area.getArrLocation().get(wpCtr).setPhotoTaken(true);
                findViewById(txvId[wpCtr]).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

                telemetry += "<Document>\n\t<Placemark>\n"
                        + "\t\t<name>WayPoint_" + wpCtr + "</name>\n"
                        + "\t\t\t<description>" + area.getArrLocation().get(wpCtr).getLocation().getLongitude() + "," + area.getArrLocation().get(wpCtr).getLocation().getLatitude() + "</description>\n"
                        + "\t\t\t<Point>\n"
                        + "\t\t\t\t<coordinates>" + location.getLongitude() + "," + location.getLatitude() + "</coordinates>\n"
                        + "\t\t\t</Point>\n"
                        + "\t\t\t<TimeStamp>\t\n\t\t\t\t<when>" + location.getTime() + "</when>\n\t\t\t</TimeStamp>\n"
                        + "\t\t\t<altitude>" + location.getAltitude() + "</altitude>\n"
                        + "\t\t\t<Orientation>\n"
                        + "\t\t\t\t<heading>" + bearing + "</heading>\n"
                        + "\t\t\t\t<roll>" + (Math.toDegrees(m_orientation[1] - calY)) + "</roll>\n"
                        + "\t\t\t\t<tilt>" + (Math.toDegrees(m_orientation[2] - calZ)) + "</tilt>\n"
                        + "\t\t\t</Orientation>\n"
                        + "\t\t\t<ExtendedData>\n"
                        + "\t\t\t\t<Data name=\"bearingTo\">\n\t\t<value>" + bearingTo + "</value>\n\t</Data>\n"
                        + "\t\t\t\t<Data name=\"distanceTo\">\n\t\t<value>" + location.distanceTo(area.getArrLocation().get(wpCtr).getLocation()) + "</value>\n\t</Data>\n"
                        + "\t\t\t\t<Data name=\"accuracy\">\n\t\t<value>" + location.getAccuracy() + "</value>\n\t</Data>\n"
                        + "\t\t\t\t<Data name=\"headingGeoMag\">\n\t\t<value>" + yaw + "</value>\n\t</Data>\n"
                        + "\t\t\t\t<Data name=\"speed\">\n\t\t<value>" + location.getSpeed() + "</value>\n\t</Data>\n"
                        + "\t\t\t</ExtendedData>\n"
                        + "\t</Placemark>\n</Document>\n";

                wpCtr++;

                if (wpCtr > area.getArrLocation().size() - 1) {
                    wpCtr = 0;
                }
                txv_wp.setText("" + wpCtr);
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

                telemetry += "</kml>\n";
                writeToFile(telemetry);

                setResult(1, resultIntent);
                finish();
                break;

            case R.id.btn_prev:
                wpCtr--;
                if (wpCtr < 0) {
                    wpCtr = area.getArrLocation().size() - 1;
                }
                txv_wp.setText("" + wpCtr);
                break;

            case R.id.btn_next:
                wpCtr++;
                if (wpCtr > area.getArrLocation().size() - 1) {
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

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/telemetry.kml");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (new File(file.getAbsolutePath()), true);
            outputStream.write(string.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadArea(String filePath) {

        int permission = ActivityCompat.checkSelfPermission(FlightActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FlightActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        String line;

        try {

            area = new Area(0,0,0,0,0,0);

            FileInputStream is = new FileInputStream (new File(filePath));
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bfReader = new BufferedReader(isReader);
            StringBuilder stringBuilder = new StringBuilder();

            line = bfReader.readLine();
            stringBuilder.append(line + System.getProperty("line.separator"));

            while ((line = bfReader.readLine()) != null) {

                if (line.contains("<coordinates>")) {
                    line = line.replace("<coordinates>", "");
                    line = line.replace("</coordinates>", "");
                    List<String> coordsArray = Arrays.asList(line.split(","));
                    Log.i("COORDS", (coordsArray.get(1) + "," + coordsArray.get(0)));
                    area.addWP((Float.parseFloat(coordsArray.get(1))), Float.parseFloat(coordsArray.get(0)));
                }
            }

            is.close();
            bfReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, m_lastAccel, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, m_lastMagFields, 0, 3);
                break;
            default:
                return;
        }

        float yaw = (float)Math.toDegrees(m_orientation[0] - calX);
        if (yaw < 0) {

            yaw += 360;
        }

        if (SensorManager.getRotationMatrix(m_rotationMatrix, null, m_lastAccel, m_lastMagFields)) {
            SensorManager.getOrientation(m_rotationMatrix, m_orientation);
            // Show values
            txv_yaw.setText("Y: " + (int)yaw + "°");
            txv_roll.setText("R: " + (int)Math.toDegrees(m_orientation[1] - calY) + "°");
            txv_pitch.setText("P: " + (int)Math.toDegrees(m_orientation[2] - calZ) + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use.
    }
}
