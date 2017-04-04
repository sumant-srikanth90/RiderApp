package in.droom.riderapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.droom.riderapp.R;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class SettingsActivity extends BaseActivity {

    EditText et_ip;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_ip = (EditText) findViewById(R.id.et_ip);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_ip.getText().toString().trim().length() < 7 && !et_ip.getText().toString().contains(".")) {
                    GlobalMethods.showSnackbar(SettingsActivity.this, "Invalid IP Address!");
                    return;
                }

                GlobalMethods.saveToPrefs(AppConstants.PREFS_SERVER_IP, et_ip.getText().toString(), GlobalMethods.STRING);
                AppConstants.BASE_DOMAIN = et_ip.getText().toString();

                finish();
            }
        });

        setData();
    }

    void setData() {
        String saved_ip = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_SERVER_IP, GlobalMethods.STRING);
        et_ip.setText(saved_ip);
    }
}