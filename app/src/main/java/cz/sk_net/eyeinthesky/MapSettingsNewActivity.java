package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MapSettingsNewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQ_CODE_SELECT_AREA = 2120;

    Area area;

    private EditText x_a;
    private EditText x_b;
    private EditText x_c;
    private EditText y_a;
    private EditText y_b;
    private EditText y_c;
    private TextView txb_xaa;
    private TextView txb_yaa;
    private TextView txb_xbb;
    private TextView txb_ybb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_settings_new);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button btn_save = findViewById(R.id.btn_save);
        Button btn_select_area = findViewById(R.id.btn_select_area);
        Button btn_compute = findViewById(R.id.btn_compute);

        btn_save.setOnClickListener(this);
        btn_select_area.setOnClickListener(this);
        btn_compute.setOnClickListener(this);

        x_a = findViewById(R.id.num_point_a_lat);
        x_b = findViewById(R.id.num_point_b_lat);
        x_c = findViewById(R.id.num_point_c_lat);

        y_a = findViewById(R.id.num_point_a_lng);
        y_b = findViewById(R.id.num_point_b_lng);
        y_c = findViewById(R.id.num_point_c_lng);

        txb_xaa = findViewById(R.id.txb_point_aa_lat);
        txb_yaa = findViewById(R.id.txb_point_aa_lng);

        txb_xbb = findViewById(R.id.txb_point_bb_lat);
        txb_ybb = findViewById(R.id.txb_point_bb_lng);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_save:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", 0);
                setResult(1, resultIntent);
                finish();
                break;

            case R.id.btn_select_area:
                Intent intent = new Intent(MapSettingsNewActivity.this, MapSettingsSelectActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_AREA);
                break;

            case R.id.btn_compute:
                // TESTING

                double xa = Double.parseDouble(x_a.getText().toString());
                double xb = Double.parseDouble(x_b.getText().toString());
                double xc = Double.parseDouble(x_c.getText().toString());
                double ya = Double.parseDouble(y_a.getText().toString());
                double yb = Double.parseDouble(y_b.getText().toString());
                double yc = Double.parseDouble(y_c.getText().toString());
                double u1 = xb - xa;
                double u2 = yb - ya;
                double v1 = u2;
                double v2 = - u1;

                double yaa = (xc - xa - (u1 / u2) * yc + (v1 / v2) * ya) / ((v1 / v2) - (u1 / u2));
                double ybb = (xc - xb - (u1 / u2) * yc + (v1 / v2) * yb) / ((v1 / v2) - (u1 / u2));

                double xaa = (v1 / v2) * yaa + xc - (v1 / v2) * yc;
                double xbb = (v1 / v2) * ybb + xc - (v1 / v2) * yc;

                txb_xaa.setText(Double.toString(xaa));
                txb_yaa.setText(Double.toString(yaa));

                txb_xbb.setText(Double.toString(xbb));
                txb_ybb.setText(Double.toString(ybb));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}