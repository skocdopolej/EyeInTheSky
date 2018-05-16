package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class FlightSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Camera camera;
    Area area;

    EditText txb_gsd;
    EditText txb_altitude;
    EditText txb_overlap_longitudal;
    EditText txb_overlap_lateral;

    Location a = new Location("GoogleMaps");
    Location b = new Location("GoogleMaps");
    Location aa = new Location("GoogleMaps");
    Location bb = new Location("GoogleMaps");

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

        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        txb_gsd = findViewById(R.id.txb_gsd);
        txb_altitude = findViewById(R.id.txb_altitude);
        txb_overlap_longitudal = findViewById(R.id.txb_overlap_longitudal);
        txb_overlap_lateral = findViewById(R.id.txb_overlap_lateral);

        // Get camera and area
        Intent intent = getIntent();

        if (intent.hasExtra("camera") && intent.hasExtra("area")) {

            camera = (Camera) intent.getSerializableExtra("camera");
            area = (Area) intent.getSerializableExtra("area");
        }

        a.setLatitude(area.getLatA());
        a.setLongitude(area.getLngA());
        b.setLatitude(area.getLatB());
        b.setLongitude(area.getLngB());
        aa.setLatitude(area.getLatAA());
        aa.setLongitude(area.getLngAA());
        bb.setLatitude(area.getLatBB());
        bb.setLongitude(area.getLngBB());

        RadioButton rb_v1 = findViewById(R.id.rb_v1);
        RadioButton rb_v2 = findViewById(R.id.rb_v2);
        RadioButton rb_v3 = findViewById(R.id.rb_v3);
        RadioButton rb_v4 = findViewById(R.id.rb_v4);

        rb_v1.setText(toDegrees360(a.bearingTo(b)) + "°");
        rb_v2.setText(toDegrees360(b.bearingTo(a)) + "°");
        rb_v3.setText(toDegrees360(a.bearingTo(aa)) + "°");
        rb_v4.setText(toDegrees360(aa.bearingTo(a)) + "°");
    }

    @Override
    public void onClick(View v) {

        //double gsd = Double.parseDouble(txb_gsd.getText().toString());
        double altitude = Double.parseDouble(txb_altitude.getText().toString());

        double fovX = camera.getFovX((float) altitude);
        double fovY = camera.getFovY((float) altitude);

        Toast.makeText(this, ("FoV X: " + fovX + " m, FoV Y: " + fovY + " m"), Toast.LENGTH_SHORT).show();

        float sideX = a.distanceTo(b);
        float sideY = a.distanceTo(aa);

        Toast.makeText(this, ("Side X: " + sideX + "m, Side Y: " + sideY + " m"), Toast.LENGTH_SHORT).show();

        int tileX = (int) (sideX / fovX + 1);
        int tileY = (int) (sideY / fovY + 1);

        Toast.makeText(this, ("Tiles X: " + tileX + ", Tiles Y: " + tileY), Toast.LENGTH_SHORT).show();

        double deltaX = area.getDeltaX();
        double deltaY = area.getDeltaY();

        Toast.makeText(this, ("Delta X: " + deltaX + "°, Delta Y: " + deltaY + "°"), Toast.LENGTH_SHORT).show();

        area.getWayPoints(tileX, tileY, sideX, sideY, deltaX, deltaY, a.bearingTo(b));
    }

    public static float toDegrees360(float a) {

        if (a < 0) {

            a += 360;
        }

        return a;
    }
}
