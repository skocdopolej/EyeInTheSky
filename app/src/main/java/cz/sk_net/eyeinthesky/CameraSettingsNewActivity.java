package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class CameraSettingsNewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_save;
    private EditText txb_camera_name;
    private EditText txb_resolution_x;
    private EditText txb_resolution_y;
    private EditText txb_chip_x;
    private EditText txb_chip_y;
    private EditText txb_focal_length;
    private EditText txb_camera_shutter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_new_settings);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Assign text fields and buttons
        btn_save = findViewById(R.id.btn_save);
        txb_camera_name = findViewById(R.id.txb_camera_name);
        txb_resolution_x = findViewById(R.id.txb_camera_resolution_horizontal);
        txb_resolution_y = findViewById(R.id.txb_camera_resolution_vertical);
        txb_chip_x = findViewById(R.id.txb_chip_size_horizontal);
        txb_chip_y = findViewById(R.id.txb_chip_size_vertical);
        txb_focal_length = findViewById(R.id.txb_camera_focal_length);
        txb_camera_shutter = findViewById(R.id.txb_camera_shutter);

        // Set button listener
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String cameraName = txb_camera_name.getText().toString();
        int resolutionX = Integer.parseInt(txb_resolution_x.getText().toString());
        int resolutionY = Integer.parseInt(txb_resolution_y.getText().toString());
        float chipX = (float) Double.parseDouble(txb_chip_x.getText().toString());
        float chipY = (float) Double.parseDouble(txb_chip_y.getText().toString());
        float focalLength = (float) Double.parseDouble(txb_focal_length.getText().toString());
        String shutter = txb_camera_shutter.getText().toString();

        Camera camera = new Camera(cameraName, resolutionX, resolutionY, chipX, chipY, focalLength, shutter);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("camera", camera);

        setResult(1, resultIntent);
        finish();
    }
}
