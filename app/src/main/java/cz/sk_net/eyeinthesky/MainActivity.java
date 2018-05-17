package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Intent request codes
    public static final int REQ_CODE_CAMERA = 10;
    public static final int REQ_CODE_AREA = 20;
    public static final int REQ_CODE_FLIGHT = 30;
    public static final int REQ_CODE_CALIBRATION = 40;
    public static final int REQ_CODE_START = 50;

    // Camera settings
    Camera camera;

    // Area settings
    Area area;

    // Tablet sensor calibration
    PositionSensor positionSensor;

    // Flight params
    Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Assign buttons
        Button btn_camera_new = findViewById(R.id.btn_camera);
        Button btn_area = findViewById(R.id.btn_area);
        Button btn_calibrate = findViewById(R.id.btn_calibrate);
        Button btn_start = findViewById(R.id.btn_start);
        Button btn_flight = findViewById(R.id.btn_flight);

        // Set buttons listener
        btn_camera_new.setOnClickListener(this);
        btn_area.setOnClickListener(this);
        btn_calibrate.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_flight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.btn_camera:
                intent = new Intent(MainActivity.this, CameraSettingsActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA);
                break;

            case R.id.btn_area:
                intent = new Intent(MainActivity.this, AreaSettingsActivity.class);
                startActivityForResult(intent, REQ_CODE_AREA);
                break;

            case R.id.btn_flight:
                if (camera == null) {
                    Toast.makeText(this, "Camera settings required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(MainActivity.this, FlightSettingsActivity.class);
                intent.putExtra("camera", camera);
                intent.putExtra("area", area);
                startActivityForResult(intent, REQ_CODE_FLIGHT);
                break;

            case R.id.btn_calibrate:
                intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivityForResult(intent, REQ_CODE_CALIBRATION);
                break;

            case R.id.btn_start:
                if (positionSensor == null) {
                    Toast.makeText(this, "Calibration required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (camera == null) {
                    Toast.makeText(this, "Camera settings required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (area == null) {
                    Toast.makeText(this, "Area selection required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((area.getPath() == null) && (area.getArrLocation() == null)) {
                    Toast.makeText(this, "Flight settings required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent = new Intent(MainActivity.this, FlightActivity.class);
                intent.putExtra("correction", positionSensor);
                intent.putExtra("camera", camera);
                intent.putExtra("area", area);
                intent.putExtra("flight", flight);
                startActivityForResult(intent, REQ_CODE_START);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_CAMERA:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    camera = (Camera) data.getSerializableExtra("camera");

                    Toast.makeText(this, camera.getParams(), Toast.LENGTH_LONG).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Camera settings canceled.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQ_CODE_AREA:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    area = (Area) data.getSerializableExtra("area");

                    Toast.makeText(this, "Area selected.", Toast.LENGTH_SHORT).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Area selection canceled.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQ_CODE_FLIGHT:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    Toast.makeText(this, "Flight settings successful.", Toast.LENGTH_SHORT).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Flight settings canceled.", Toast.LENGTH_SHORT).show();
                    return;
                }

                area = (Area) data.getSerializableExtra("area");
                flight = (Flight) data.getSerializableExtra("flight");
                break;

            case REQ_CODE_CALIBRATION:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    positionSensor = (PositionSensor) data.getSerializableExtra("correction");

                    Toast.makeText(this, "Calibration successful.", Toast.LENGTH_SHORT).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Calibration canceled.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQ_CODE_START:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    Toast.makeText(this, "Telemetry saved.", Toast.LENGTH_SHORT).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    Toast.makeText(this, "Flight canceled.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
