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
    public static final int REQ_CODE_CALIBRATION = 30;
    public static final int REQ_CODE_FLIGHT = 40;
    public static final int REQ_CODE_START = 50;

    // Camera
    Camera camera;

    // Tablet
    PositionSensor positionSensor;

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

        // Set buttons listener
        btn_camera_new.setOnClickListener(this);
        btn_area.setOnClickListener(this);
        btn_calibrate.setOnClickListener(this);
        btn_start.setOnClickListener(this);
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

            case R.id.btn_calibrate:
                intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivityForResult(intent, REQ_CODE_CALIBRATION);
                break;

            case R.id.btn_start:
                if (positionSensor == null) {
                    return;
                }
                intent = new Intent(MainActivity.this, FlightActivity.class);
                intent.putExtra("correction", positionSensor);
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

                    Toast.makeText(this, camera.print(), Toast.LENGTH_LONG).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    // TODO
                }
                break;

            case REQ_CODE_AREA:
                // TODO
                break;

            case REQ_CODE_FLIGHT:
                // TODO
                break;

            case REQ_CODE_CALIBRATION:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    positionSensor = (PositionSensor) data.getSerializableExtra("correction");

                    Toast.makeText(this, positionSensor.print(), Toast.LENGTH_LONG).show();
                }
                break;

            case REQ_CODE_START:
                if (resultCode == 1) {

                    // TODO
                } else if (resultCode == 2) {

                    // TODO
                }

                if (resultCode == RESULT_CANCELED) {

                    // TODO
                }
                break;
        }
    }
}
