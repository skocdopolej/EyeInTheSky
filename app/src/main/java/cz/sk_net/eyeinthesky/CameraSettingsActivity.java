package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CameraSettingsActivity extends AppCompatActivity implements View.OnClickListener {

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

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_camera_settings);

        // Assign text fields and buttons
        Button btn_ok = findViewById(R.id.btn_ok);
        Button btn_save = findViewById(R.id.btn_save);
        Button btn_load = findViewById(R.id.btn_load);
        txb_camera_name = findViewById(R.id.txb_camera_name);
        txb_resolution_x = findViewById(R.id.txb_camera_resolution_horizontal);
        txb_resolution_y = findViewById(R.id.txb_camera_resolution_vertical);
        txb_chip_x = findViewById(R.id.txb_chip_size_horizontal);
        txb_chip_y = findViewById(R.id.txb_chip_size_vertical);
        txb_focal_length = findViewById(R.id.txb_camera_focal_length);
        txb_camera_shutter = findViewById(R.id.txb_camera_shutter);

        // Set button listener
        btn_ok.setOnClickListener(this);
        btn_load.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ok:
                if (!txb_camera_name.getText().toString().equals("")
                        && !txb_resolution_x.getText().toString().equals("")
                        && !txb_resolution_y.getText().toString().equals("")
                        && !txb_chip_x.getText().toString().equals("")
                        && !txb_chip_y.getText().toString().equals("")
                        && !txb_focal_length.getText().toString().equals("")
                        && !txb_camera_shutter.getText().toString().equals("")) {

                    String cameraName = txb_camera_name.getText().toString();
                    int resolutionX = Integer.parseInt(txb_resolution_x.getText().toString());
                    int resolutionY = Integer.parseInt(txb_resolution_y.getText().toString());
                    float chipX = (float) Double.parseDouble(txb_chip_x.getText().toString());
                    float chipY = (float) Double.parseDouble(txb_chip_y.getText().toString());
                    float focalLength = (float) Double.parseDouble(txb_focal_length.getText().toString());
                    String shutter = txb_camera_shutter.getText().toString();

                    if (resolutionX < resolutionY) {
                        resolutionY = resolutionX;
                        chipY = chipX;
                    } else {
                        resolutionX = resolutionY;
                        chipX = chipY;
                    }

                    Camera camera = new Camera(cameraName, resolutionX, resolutionY, chipX, chipY, focalLength, shutter);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("camera", camera);
                    setResult(1, resultIntent);
                    finish();
                } else {

                    Toast.makeText(this, "Incomplete settings!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_load:
                Intent intentLoad = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentLoad, "path"), 1);
                break;

            case R.id.btn_save:
                saveCamera();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent chooser) {

        super.onActivityResult(requestCode, resultCode, chooser);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            String filePath = chooser.getData().getPath();
            loadCamera(filePath.replace("/document/", ""));
        }
    }

    private void loadCamera(String filePath) {

        int permission = ActivityCompat.checkSelfPermission(CameraSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CameraSettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        String line;

        try {

            FileInputStream is = new FileInputStream (new File(filePath));
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bfReader = new BufferedReader(isReader);
            StringBuilder stringBuilder = new StringBuilder();

            line = bfReader.readLine();
            stringBuilder.append(line + System.getProperty("line.separator"));

            line = stringBuilder.toString();

            is.close();
            bfReader.close();

            List<String> cameraArray = Arrays.asList(line.split(";"));

            Camera camera = new Camera(cameraArray.get(0),
                    Integer.parseInt(cameraArray.get(1)), Integer.parseInt(cameraArray.get(2)),
                    Float.parseFloat(cameraArray.get(3)), Float.parseFloat(cameraArray.get(4)),
                    Float.parseFloat(cameraArray.get(5)), cameraArray.get(6));

            Intent resultIntent = new Intent();
            resultIntent.putExtra("camera", camera);
            setResult(1, resultIntent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCamera() {

        int permission = ActivityCompat.checkSelfPermission(CameraSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CameraSettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        String cameraString = txb_camera_name.getText().toString() + ";"
                + txb_resolution_x.getText().toString() + ";" + txb_resolution_y.getText().toString() + ";"
                + txb_chip_x.getText().toString() + ";" + txb_chip_y.getText().toString() + ";"
                + txb_focal_length.getText().toString() + ";" + txb_camera_shutter.getText().toString() + ";";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/" + txb_camera_name.getText().toString() + ".csv");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (new File(file.getAbsolutePath()), true);
            outputStream.write(cameraString.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
