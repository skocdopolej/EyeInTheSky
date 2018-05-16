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

public class FlightActivity extends AppCompatActivity implements LocationListener, SensorEventListener, View.OnClickListener {

    PositionSensor positionSensor;
    ArrayList<WayPoint> arrLocation = new ArrayList<>();
    int txvId[] = new int[64];
    int wpCtr = 0;
    String telemetry = "----TELEMETRY RECORD----\n";

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

        TableLayout tbl_grid = findViewById(R.id.tbl_grid);

        for (int i = 0; i < 8; i++) {

            // Row
            TableRow tbl_row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tbl_row.setLayoutParams(lp);

            for (int j = 0; j < 8; j++) {

                // Cell
                TextView txv_wp = new TextView(this);
                txv_wp.setText((i * 8 + j) + "");
                txv_wp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 512/16);
                txv_wp.setWidth(512/8);
                txv_wp.setHeight(512/8);
                txv_wp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                txv_wp.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                txv_wp.setId((txvId[i * 8 + j] = View.generateViewId()));

                tbl_row.addView(txv_wp);
            }

            tbl_grid.addView(tbl_row, i);
        }

        // Testing
        /*
        arrLocation.add(new WayPoint(49.4410372, 15.7671958));
        arrLocation.add(new WayPoint(49.4408517, 15.7672992));
        arrLocation.add(new WayPoint(49.4400764, 15.7664717));
        arrLocation.add(new WayPoint(49.4390500, 15.7668511));
        arrLocation.add(new WayPoint(49.4384028, 15.7682311));
        arrLocation.add(new WayPoint(49.4379111, 15.7698942));
        arrLocation.add(new WayPoint(49.4370547, 15.7694114));
        arrLocation.add(new WayPoint(49.4381150, 15.7674747));
        arrLocation.add(new WayPoint(49.4391719, 15.7660344));
        */

        arrLocation.add(new WayPoint(49.3929244, 15.8967881));
        arrLocation.add(new WayPoint(49.3921197, 15.8992814));
        arrLocation.add(new WayPoint(49.3913856, 15.9018092));
        arrLocation.add(new WayPoint(49.3907564, 15.9044069));
        arrLocation.add(new WayPoint(49.3902142, 15.9070464));
        arrLocation.add(new WayPoint(49.3897139, 15.9097003));
        arrLocation.add(new WayPoint(49.3891886, 15.9123478));
        arrLocation.add(new WayPoint(49.3885792, 15.9149428));
        arrLocation.add(new WayPoint(49.3878511, 15.9174733));
        arrLocation.add(new WayPoint(49.3870636, 15.9199544));
        arrLocation.add(new WayPoint(49.3862667, 15.9224261));
        arrLocation.add(new WayPoint(49.3854617, 15.9249083));
        arrLocation.add(new WayPoint(49.3846594, 15.9273761));
        arrLocation.add(new WayPoint(49.3838650, 15.9298464));
        arrLocation.add(new WayPoint(49.3830686, 15.9323261));
        arrLocation.add(new WayPoint(49.3822647, 15.9347925));
        arrLocation.add(new WayPoint(49.3814561, 15.9372694));
        arrLocation.add(new WayPoint(49.3806519, 15.9397303));
        arrLocation.add(new WayPoint(49.3798514, 15.9422086));
        arrLocation.add(new WayPoint(49.3790358, 15.9446736));
        arrLocation.add(new WayPoint(49.3782361, 15.9471439));
        arrLocation.add(new WayPoint(49.3774478, 15.9496303));
        arrLocation.add(new WayPoint(49.3766453, 15.9521019));
        arrLocation.add(new WayPoint(49.3758428, 15.9545764));
        arrLocation.add(new WayPoint(49.3750378, 15.9570494));
        arrLocation.add(new WayPoint(49.3742353, 15.9595117));
        arrLocation.add(new WayPoint(49.3734311, 15.9619939));
        arrLocation.add(new WayPoint(49.3726311, 15.9644575));
        arrLocation.add(new WayPoint(49.3718103, 15.9669239));
        arrLocation.add(new WayPoint(49.3709814, 15.9693619));
        arrLocation.add(new WayPoint(49.3701361, 15.9718108));
        arrLocation.add(new WayPoint(49.3684508, 15.9766897));
        arrLocation.add(new WayPoint(49.3685547, 15.9767503));
        arrLocation.add(new WayPoint(49.3693992, 15.9743200));
        arrLocation.add(new WayPoint(49.3702350, 15.9718872));
        arrLocation.add(new WayPoint(49.3710775, 15.9694492));
        arrLocation.add(new WayPoint(49.3719125, 15.9670044));
        arrLocation.add(new WayPoint(49.3727358, 15.9645353));
        arrLocation.add(new WayPoint(49.3735392, 15.9620556));
        arrLocation.add(new WayPoint(49.3743400, 15.9595867));
        arrLocation.add(new WayPoint(49.3751400, 15.9571125));
        arrLocation.add(new WayPoint(49.3759442, 15.9546433));
        arrLocation.add(new WayPoint(49.3767500, 15.9521772));
        arrLocation.add(new WayPoint(49.3775456, 15.9496975));
        arrLocation.add(new WayPoint(49.3783436, 15.9472111));
        arrLocation.add(new WayPoint(49.3791478, 15.9447419));
        arrLocation.add(new WayPoint(49.3799561, 15.9422717));
        arrLocation.add(new WayPoint(49.3807586, 15.9397961));
        arrLocation.add(new WayPoint(49.3815592, 15.9373311));
        arrLocation.add(new WayPoint(49.3823658, 15.9348553));
        arrLocation.add(new WayPoint(49.3831569, 15.9323878));
        arrLocation.add(new WayPoint(49.3839611, 15.9299067));
        arrLocation.add(new WayPoint(49.3847606, 15.9274417));
        arrLocation.add(new WayPoint(49.3855631, 15.9249728));
        arrLocation.add(new WayPoint(49.3863644, 15.9224917));
        arrLocation.add(new WayPoint(49.3871633, 15.9200147));
        arrLocation.add(new WayPoint(49.3879481, 15.9175483));
        arrLocation.add(new WayPoint(49.3886769, 15.9149978));
        arrLocation.add(new WayPoint(49.3892942, 15.9123986));
        arrLocation.add(new WayPoint(49.3898214, 15.9097392));
        arrLocation.add(new WayPoint(49.3903217, 15.9070919));
        arrLocation.add(new WayPoint(49.3914914, 15.9018669));
        arrLocation.add(new WayPoint(49.3922203, 15.8993403));
        arrLocation.add(new WayPoint(49.3930336, 15.8968806));


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

        float bearingTo = location.bearingTo(arrLocation.get(wpCtr).getLocation());
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
        tx_dist.setText(String.format("%.2f", location.distanceTo(arrLocation.get(wpCtr).getLocation())) + " m");
        tx_bear_to.setText(String.format("%.2f", bearingTo) + "°");

        if (!arrLocation.get(wpCtr).isPhotoTaken()) {

            if (location.distanceTo(arrLocation.get(wpCtr).getLocation()) < 25) {

                if (Math.abs(Math.toDegrees(m_orientation[1] - calY)) > 50) {

                    Toast.makeText(this, "Roll over limit", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Math.abs(Math.toDegrees(m_orientation[2] - calZ)) > 50) {

                    Toast.makeText(this, "Pitch over limit!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Pohoto taken.", Toast.LENGTH_SHORT).show();
                arrLocation.get(wpCtr).setPhotoTaken(true);
                findViewById(txvId[wpCtr]).setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

                telemetry += "Waypoint:\t" + wpCtr + "\n"
                        + "WP Loc:\t\t" + arrLocation.get(wpCtr).getLocation().getLatitude() + "° N," + arrLocation.get(wpCtr).getLocation().getLongitude() + "° E\n"
                        + "GPS:\t\t" + location.getLatitude() + "° N," + location.getLongitude() + "° E\n"
                        + "Bearing To:\t" + bearingTo + "\n"
                        + "Distance:\t" + location.distanceTo(arrLocation.get(wpCtr).getLocation()) + "\n"
                        + "Time:\t\t" + location.getTime() + "\n"
                        + "Accuracy:\t" + location.getAccuracy() + " m\n"
                        + "Altitude:\t" + location.getAltitude() + " m\n"
                        + "Bearing:\t" + bearing + "°\n"
                        + "Speed:\t\t" + location.getSpeed() + " m/s\n"
                        + "Yaw:\t\t" + yaw + "°\n"
                        + "Roll:\t\t" + (Math.toDegrees(m_orientation[1] - calY)) + "°\n"
                        + "Pitch:\t\t" + (Math.toDegrees(m_orientation[2] - calZ)) + "°\n\n";

                wpCtr++;

                if (wpCtr > arrLocation.size() - 1) {
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
