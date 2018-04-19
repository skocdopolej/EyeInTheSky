package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalibrationActivity extends AppCompatActivity implements SensorEventListener{

    // Axis text
    private TextView xAxis;
    private TextView yAxis;
    private TextView zAxis;

    // Sensors (ACCELEROMETER + GEOMAG)
    private SensorManager SM;
    private Sensor myAcc;
    private Sensor myGeo;


    float[] m_lastAccel = new float[3];
    float[] m_lastMagFields = new float[3];
    private float[] m_rotationMatrix = new float[16];
    private float[] m_orientation = new float[4];

    float calX = 0f;
    float calY = 0f;
    float calZ = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final EditText txb_yaw = findViewById(R.id.txb_yaw);
        final EditText txb_roll = findViewById(R.id.txb_roll);
        final EditText txb_pitch = findViewById(R.id.txb_pitch);

        txb_yaw.setText("0");
        txb_roll.setText("0");
        txb_pitch.setText("0");

        Button bt_calibrate = findViewById(R.id.btn_calibrate);
        bt_calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calX = (float) (m_orientation[0] - Math.toRadians(Double.parseDouble(txb_yaw.getText().toString())));
                calY = (float) (m_orientation[1] - Math.toRadians(Double.parseDouble(txb_roll.getText().toString())));
                calZ = (float) (m_orientation[2] - Math.toRadians(Double.parseDouble(txb_pitch.getText().toString())));
            }
        });

        // Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Sensor
        assert SM != null;
        myAcc = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myGeo = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register
        SM.registerListener(this, myAcc, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this, myGeo, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign Text Views
        xAxis = findViewById(R.id.txv_x_axis);
        yAxis = findViewById(R.id.txv_y_axis);
        zAxis = findViewById(R.id.txv_z_axis);

        Button btn_ok = findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", 0);

                setResult(1, resultIntent);
                finish();
            }
        });
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

        if (SensorManager.getRotationMatrix(m_rotationMatrix, null, m_lastAccel, m_lastMagFields)) {
            SensorManager.getOrientation(m_rotationMatrix, m_orientation);
            // Show values
            xAxis.setText("Y: " + (int)Math.toDegrees(m_orientation[0] - calX) + "°");
            yAxis.setText("R: " + (int)Math.toDegrees(m_orientation[1] - calY) + "°");
            zAxis.setText("P: " + (int)Math.toDegrees(m_orientation[2] - calZ) + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use.
    }
}
