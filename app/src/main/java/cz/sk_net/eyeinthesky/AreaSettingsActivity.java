package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AreaSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQ_CODE_SELECT_AREA = 2120;

    Area area;

    private EditText num_a_lat;
    private EditText num_b_lat;
    private EditText num_c_lat;
    private EditText num_a_lng;
    private EditText num_b_lng;
    private EditText num_c_lng;
    private TextView txb_aa_lat;
    private TextView txb_aa_lng;
    private TextView txb_bb_lat;
    private TextView txb_bb_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hide system top tray.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_area_settings);

        Button btn_save = findViewById(R.id.btn_ok);
        Button btn_select_area = findViewById(R.id.btn_select_area);
        Button btn_compute = findViewById(R.id.btn_compute);

        btn_save.setOnClickListener(this);
        btn_select_area.setOnClickListener(this);
        btn_compute.setOnClickListener(this);

        num_a_lat = findViewById(R.id.num_a_lat);
        num_b_lat = findViewById(R.id.num_b_lat);
        num_c_lat = findViewById(R.id.num_c_lat);
        num_a_lng = findViewById(R.id.num_a_lng);
        num_b_lng = findViewById(R.id.num_b_lng);
        num_c_lng = findViewById(R.id.num_c_lng);

        txb_aa_lat = findViewById(R.id.txb_aa_lat);
        txb_bb_lat = findViewById(R.id.txb_bb_lat);
        txb_aa_lng = findViewById(R.id.txb_aa_lng);
        txb_bb_lng = findViewById(R.id.txb_bb_lng);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_ok:

                if (!num_a_lat.getText().toString().equals("")
                        && !num_a_lng.getText().toString().equals("")
                        && !num_b_lat.getText().toString().equals("")
                        && !num_b_lng.getText().toString().equals("")
                        && !num_c_lat.getText().toString().equals("")
                        && !num_c_lng.getText().toString().equals("")) {

                    Intent resultIntent = new Intent();

                    if (area == null) {

                        area = new Area(
                                Double.parseDouble(num_a_lat.getText().toString()),
                                Double.parseDouble(num_b_lat.getText().toString()),
                                Double.parseDouble(num_c_lat.getText().toString()),
                                Double.parseDouble(num_a_lng.getText().toString()),
                                Double.parseDouble(num_b_lng.getText().toString()),
                                Double.parseDouble(num_c_lng.getText().toString())
                        );

                        area.computeArea();
                    }

                    resultIntent.putExtra("area", area);
                    setResult(1, resultIntent);
                    finish();
                } else {
                    Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_select_area:
                Intent intent = new Intent(AreaSettingsActivity.this, AreaSelectActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_AREA);
                break;

            case R.id.btn_compute:

                if (!num_a_lat.getText().toString().equals("")
                        && !num_a_lng.getText().toString().equals("")
                        && !num_b_lat.getText().toString().equals("")
                        && !num_b_lng.getText().toString().equals("")
                        && !num_c_lat.getText().toString().equals("")
                        && !num_c_lng.getText().toString().equals("")) {

                    //Longitude == X
                    //Latitude  == Y
                    area = new Area(
                            Double.parseDouble(num_a_lat.getText().toString()),
                            Double.parseDouble(num_b_lat.getText().toString()),
                            Double.parseDouble(num_c_lat.getText().toString()),
                            Double.parseDouble(num_a_lng.getText().toString()),
                            Double.parseDouble(num_b_lng.getText().toString()),
                            Double.parseDouble(num_c_lng.getText().toString())
                    );

                    area.computeArea();

                    txb_aa_lat.setText(Double.toString(area.getLatAA()));
                    txb_aa_lng.setText(Double.toString(area.getLngAA()));
                    txb_bb_lat.setText(Double.toString(area.getLatBB()));
                    txb_bb_lng.setText(Double.toString(area.getLngBB()));
                } else {
                    Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            area = (Area) data.getSerializableExtra("area");

            num_a_lat.setText(Double.toString(area.getLatA()));
            num_a_lng.setText(Double.toString(area.getLngA()));
            num_b_lat.setText(Double.toString(area.getLatB()));
            num_b_lng.setText(Double.toString(area.getLngB()));
            num_c_lat.setText(Double.toString(area.getLatC()));
            num_c_lng.setText(Double.toString(area.getLngC()));

            txb_aa_lat.setText(Double.toString(area.getLatAA()));
            txb_aa_lng.setText(Double.toString(area.getLngAA()));
            txb_bb_lat.setText(Double.toString(area.getLatBB()));
            txb_bb_lng.setText(Double.toString(area.getLngBB()));
        }
    }
}