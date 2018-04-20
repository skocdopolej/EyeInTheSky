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
    public static final int REQ_CODE_CAMERA_LOAD = 1120;
    public static final int REQ_CODE_CAMERA_NEW = 1140;
    public static final int REQ_CODE_MAP_LOAD = 1220;
    public static final int REQ_CODE_MAP_NEW = 1240;
    public static final int REQ_CODE_CALIBRATE = 1320;
    public static final int REQ_CODE_START = 1420;

    // Camera
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Assign buttons
        Button btn_camera_load = findViewById(R.id.btn_camera_load);
        Button btn_camera_new = findViewById(R.id.btn_camera_new);
        Button btn_map_load = findViewById(R.id.btn_map_load);
        Button btn_map_new = findViewById(R.id.btn_map_new);
        Button btn_calibrate = findViewById(R.id.btn_ok);
        Button btn_start = findViewById(R.id.btn_start);

        // Set buttons listener
        btn_camera_load.setOnClickListener(this);
        btn_camera_new.setOnClickListener(this);
        btn_map_load.setOnClickListener(this);
        btn_map_new.setOnClickListener(this);
        btn_calibrate.setOnClickListener(this);
        btn_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.btn_camera_load:
                intent = new Intent(MainActivity.this, CameraSettingsLoadActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
                break;

            case R.id.btn_camera_new:
                intent = new Intent(MainActivity.this, CameraSettingsNewActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_NEW);
                break;

            case R.id.btn_map_load:
                intent = new Intent(MainActivity.this, MapSettingsLoadActivity.class);
                startActivityForResult(intent, REQ_CODE_MAP_LOAD);
                break;

            case R.id.btn_map_new:
                intent = new Intent(MainActivity.this, MapSettingsNewActivity.class);
                startActivityForResult(intent, REQ_CODE_MAP_NEW);
                break;

            case R.id.btn_ok:
                intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivityForResult(intent, REQ_CODE_CALIBRATE);
                break;

            case R.id.btn_start:
                intent = new Intent(MainActivity.this, FlightActivity.class);
                startActivityForResult(intent, REQ_CODE_START);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_CAMERA_LOAD:
                // TODO
                break;

            case REQ_CODE_CAMERA_NEW:
                if ((resultCode != RESULT_CANCELED) && (resultCode == 1)) {

                    camera = (Camera) data.getSerializableExtra("camera");

                    Toast.makeText(this, camera.print(), Toast.LENGTH_LONG).show();
                }

                if (resultCode == RESULT_CANCELED) {

                    // TODO
                }
                break;

            case REQ_CODE_MAP_LOAD:
                // TODO
                break;

            case REQ_CODE_MAP_NEW:
                // TODO
                break;

            case REQ_CODE_CALIBRATE:
                // TODO
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
