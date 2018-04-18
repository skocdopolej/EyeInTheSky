package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_CAMERA_LOAD = 120;
    public static final int REQ_CODE_CAMERA_NEW = 140;
    public static final int REQ_CODE_MAP_LOAD = 220;
    public static final int REQ_CODE_MAP_NEW = 240;
    public static final int REQ_CODE_CALIBRATE = 320;
    public static final int REQ_CODE_START = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Button btn_camera_load = findViewById(R.id.btn_camera_load);
        Button btn_camera_new = findViewById(R.id.btn_camera_new);
        Button btn_map_load = findViewById(R.id.btn_map_load);
        Button btn_map_new = findViewById(R.id.btn_map_new);
        Button btn_calibrate = findViewById(R.id.btn_calibrate);
        Button btn_start = findViewById(R.id.btn_start);

        btn_camera_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraSettingsLoadActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_NEW);
            }
        });

        btn_camera_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraSettingsNewActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
            }
        });

        btn_map_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapSettingsLoadActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
            }
        });

        btn_map_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapSettingsNewActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
            }
        });

        btn_calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlyActivity.class);
                startActivityForResult(intent, REQ_CODE_CAMERA_LOAD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_CAMERA_LOAD:
                if (resultCode == 1) {

                    // TODO
                } else if (resultCode == 2) {

                    // TODO
                }

                if (resultCode == RESULT_CANCELED) {

                    // TODO
                }
                break;

            case REQ_CODE_CAMERA_NEW:
                // TODO
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
                // TODO
                break;
        }
    }
}
