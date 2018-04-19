package cz.sk_net.eyeinthesky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MapSettingsNewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQ_CODE_SELECT_AREA = 2120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_settings_new);

        //Hide keyboard at the activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button btn_save = findViewById(R.id.btn_save);
        Button btn_select_area = findViewById(R.id.btn_select_area);

        btn_save.setOnClickListener(this);
        btn_select_area.setOnClickListener(this);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}