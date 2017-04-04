package in.droom.riderapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import in.droom.riderapp.R;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class MainActivity extends BaseActivity {

    Button btn_chat, btn_users, btn_map, btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_users = (Button) findViewById(R.id.btn_users);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_map = (Button) findViewById(R.id.btn_map);
        btn_settings = (Button) findViewById(R.id.btn_settings);

        btn_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        setData();
    }

    void setData() {

        String saved_ip = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_SERVER_IP, GlobalMethods.STRING);
        if (saved_ip != null) {
            AppConstants.BASE_DOMAIN = saved_ip;
            System.out.println("SERVER IP (saved) >>>>>>>>>>> " + AppConstants.BASE_DOMAIN);
        }
        else {
            System.out.println("SERVER IP (const) >>>>>>>>>>> " + AppConstants.BASE_DOMAIN);
            GlobalMethods.saveToPrefs(AppConstants.PREFS_SERVER_IP, AppConstants.SERVER_IP, GlobalMethods.STRING);
        }
    }

}
