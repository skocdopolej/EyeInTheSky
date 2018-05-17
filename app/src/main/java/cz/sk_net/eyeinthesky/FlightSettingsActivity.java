package cz.sk_net.eyeinthesky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class FlightSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Camera camera;
    Area area;
    Flight flight;
    private int vector;

    EditText txb_gsd;
    EditText txb_altitude;
    EditText txb_overlap_longitudal;
    EditText txb_overlap_lateral;
    EditText txb_tolerance_angle;
    EditText txb_tolerance_distance;

    Location a = new Location("GoogleMaps");
    Location b = new Location("GoogleMaps");
    Location aa = new Location("GoogleMaps");
    Location bb = new Location("GoogleMaps");

    Boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_flight_settings);

        txb_gsd = findViewById(R.id.txb_gsd);
        txb_altitude = findViewById(R.id.txb_altitude);
        txb_overlap_longitudal = findViewById(R.id.txb_overlap_longitudal);
        txb_overlap_lateral = findViewById(R.id.txb_overlap_lateral);
        txb_tolerance_angle = findViewById(R.id.txb_tolerance_angle);
        txb_tolerance_distance = findViewById(R.id.txb_tolerance_distance);

        // Get camera and area
        Intent intent = getIntent();

        if (intent.hasExtra("camera") && intent.hasExtra("area")) {

            camera = (Camera) intent.getSerializableExtra("camera");
            area = (Area) intent.getSerializableExtra("area");
        }

        Button btn_ok = findViewById(R.id.btn_ok);
        Button btn_save = findViewById(R.id.btn_save);
        Button btn_load = findViewById(R.id.btn_load);
        RadioGroup rb_group = findViewById(R.id.rb_group);
        RadioButton rb_v1 = findViewById(R.id.rb_v1);
        RadioButton rb_v2 = findViewById(R.id.rb_v2);
        RadioButton rb_v3 = findViewById(R.id.rb_v3);
        RadioButton rb_v4 = findViewById(R.id.rb_v4);

        btn_ok.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_load.setOnClickListener(this);
        rb_group.setOnClickListener(this);

        rb_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isChecked = true;
                vector = findViewById(checkedId).getId();
            }
        });

        if (area != null) {

            a.setLatitude(area.getLatA());
            a.setLongitude(area.getLngA());
            b.setLatitude(area.getLatB());
            b.setLongitude(area.getLngB());
            aa.setLatitude(area.getLatAA());
            aa.setLongitude(area.getLngAA());
            bb.setLatitude(area.getLatBB());
            bb.setLongitude(area.getLngBB());

            rb_v1.setText(toDegrees360(a.bearingTo(b)) + "°");
            rb_v2.setText(toDegrees360(b.bearingTo(a)) + "°");
            rb_v3.setText(toDegrees360(a.bearingTo(aa)) + "°");
            rb_v4.setText(toDegrees360(aa.bearingTo(a)) + "°");
        } else {

            Toast.makeText(this, "Load only.", Toast.LENGTH_SHORT).show();
            txb_gsd.setEnabled(false);
            txb_altitude.setEnabled(false);
            txb_overlap_longitudal.setEnabled(false);
            txb_overlap_lateral.setEnabled(false);
            txb_overlap_lateral.setEnabled(false);
            rb_group.setEnabled(false);
            rb_v1.setEnabled(false);
            rb_v2.setEnabled(false);
            rb_v3.setEnabled(false);
            rb_v4.setEnabled(false);
            btn_ok.setEnabled(false);
            btn_save.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {

            case R.id.btn_ok:

                if (txb_tolerance_distance.getText().toString().equals("")) {
                    Toast.makeText(this, "Distance tolerace required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txb_tolerance_angle.getText().toString().equals("")) {
                    Toast.makeText(this, "Angle tolerace required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!txb_overlap_lateral.getText().toString().equals("")) {
                    camera.shrinkChipSizeY(Float.parseFloat(txb_overlap_lateral.getText().toString()));
                }
                if (!txb_overlap_longitudal.getText().toString().equals("")) {
                    camera.shrinkChipSizeX(Float.parseFloat(txb_overlap_longitudal.getText().toString()));
                }

                flight = new Flight(Float.parseFloat(txb_tolerance_angle.getText().toString()), Float.parseFloat(txb_tolerance_distance.getText().toString()), vector);

                if (!compute(false)) {
                    return;
                }
                intent.putExtra("area", area);
                intent.putExtra("flight", flight);
                setResult(1, intent);
                finish();
                break;

            case R.id.btn_save:
                compute(true);
                saveArea();
                break;

            case R.id.btn_load:
                Intent intentLoad = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentLoad, "path"), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent chooser) {

        super.onActivityResult(requestCode, resultCode, chooser);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (txb_tolerance_distance.getText().toString().equals("")) {
                Toast.makeText(this, "Distance tolerace required!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txb_tolerance_angle.getText().toString().equals("")) {
                Toast.makeText(this, "Angle tolerace required!", Toast.LENGTH_SHORT).show();
                return;
            }

            flight = new Flight(Float.parseFloat(txb_tolerance_angle.getText().toString()), Float.parseFloat(txb_tolerance_distance.getText().toString()), vector);

            String filePath = chooser.getData().getPath();
            area = new Area(0,0,0,0,0,0);
            area.setPath(filePath.replace("/document/", ""));
            Intent resultIntent = new Intent();
            resultIntent.putExtra("area", area);
            resultIntent.putExtra("flight", flight);
            setResult(1, resultIntent);
            finish();
        }
    }

    public static float toDegrees360(float a) {

        if (a < 0) {

            a += 360;
        }

        return a;
    }

    private boolean compute(boolean save) {

        double gsd;
        double altitude;
        double fovX;
        double fovY;

        if (!txb_gsd.getText().toString().equals("") && txb_altitude.getText().toString().equals("")) {

            gsd = Double.parseDouble(txb_gsd.getText().toString());
            fovX = gsd * camera.getResolutionX();
            fovY = gsd * camera.getResolutionY();
            altitude = fovX * camera.getFocalLength() / camera.getChipSizeX();
        } else if (txb_gsd.getText().toString().equals("") && !txb_altitude.getText().toString().equals("")) {

            altitude = Double.parseDouble(txb_altitude.getText().toString());
            fovX = camera.getFovX((float) altitude);
            fovY = camera.getFovY((float) altitude);
            gsd = fovX / camera.getResolutionX();
        } else {

            Toast.makeText(this, "Set GSD or altitude settings!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(this, ("Altitude: " + altitude + " m over terrain, GSD: " + gsd + " m/px"), Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, ("FoV X: " + fovX + " m, FoV Y: " + fovY + " m"), Toast.LENGTH_SHORT).show();

        float sideX = a.distanceTo(b);
        float sideY = a.distanceTo(aa);

        //Toast.makeText(this, ("Side X: " + sideX + "m, Side Y: " + sideY + " m"), Toast.LENGTH_SHORT).show();

        int tileX = (int) (sideX / fovX + 1);
        int tileY = (int) (sideY / fovY + 1);

        //Toast.makeText(this, ("Tiles X: " + tileX + ", Tiles Y: " + tileY), Toast.LENGTH_SHORT).show();

        double deltaX = area.getDeltaX();
        double deltaY = area.getDeltaY();

        //Toast.makeText(this, ("Delta X: " + deltaX + "°, Delta Y: " + deltaY + "°"), Toast.LENGTH_SHORT).show();

        area.setTileCountX(tileX);
        area.setTileCountY(tileY);

        if (save) {

            area.getWayPoints(sideX, sideY, deltaX, deltaY, a.bearingTo(b));
        }

        return true;
    }

    private void saveArea() {

        int permission = ActivityCompat.checkSelfPermission(FlightSettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FlightSettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        String areaString = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<kml xmlns='http://www.opengis.net/kml/2.2'>\n" + "\t<Document>\n";

        for (int i = 0; i < area.getArrLocation().size(); i++) {

            areaString += "\t\t<Placemark>\n" +
                    "\t\t\t<name>WayPoint_" + i + "</name>\n" +
                    "\t\t\t<description>WayPoint_" + i + "</description>\n" +
                    "\t\t\t<Point>\n" +
                    "\t\t\t\t<coordinates>" + area.getArrLocation().get(i).getLocation().getLongitude() + "," + area.getArrLocation().get(i).getLocation().getLatitude() + "</coordinates>\n" +
                    "\t\t\t</Point>" +
                    "\t\t</Placemark>\n";
        }

        areaString += "\t</Document>\n</kml>\n";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/area.kml");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream (new File(file.getAbsolutePath()), true);
            outputStream.write(areaString.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
