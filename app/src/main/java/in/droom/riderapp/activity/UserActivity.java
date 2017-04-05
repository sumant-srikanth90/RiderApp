package in.droom.riderapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.droom.riderapp.R;
import in.droom.riderapp.adapter.UserListAdapter;
import in.droom.riderapp.api.APIRequestHandler;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.model.GenericResponse;
import in.droom.riderapp.model.UserResponse;
import in.droom.riderapp.model.UserListResponse;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.model.BaseResponse;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.util.GlobalMethods;

public class UserActivity extends BaseActivity {

    EditText et_username, et_name, et_pwd;
    Button btn_register, btn_login, btn_reset, btn_logout, btn_update, btn_fetch;

    ListView lv_result;
    ArrayList<UserEntity> al_result;
    UserListAdapter adapter_result;

    TextView tv_status;

    View ll_input, ll_response;

    String username, name, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ll_input = findViewById(R.id.ll_input);

        et_username = (EditText) findViewById(R.id.et_username);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_fetch = (Button) findViewById(R.id.btn_fetch);

        ll_response = findViewById(R.id.ll_response);

        tv_status = (TextView) findViewById(R.id.tv_status);

        lv_result = (ListView) findViewById(R.id.lv_result);
        al_result = new ArrayList<UserEntity>();
        adapter_result = new UserListAdapter(UserActivity.this, al_result);
        lv_result.setAdapter(adapter_result);

        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().getAllRiders(UserActivity.this);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = et_username.getText().toString();
                name = et_name.getText().toString();
                pwd = et_pwd.getText().toString();

                tv_status.setText("");
                try {
                    APIRequestHandler.getInstance().registerRider(UserActivity.this, username, name, pwd);
                } catch (IllegalArgumentException e) {
                    tv_status.setText(e.getMessage());
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = et_username.getText().toString();
                name = et_name.getText().toString();
                pwd = et_pwd.getText().toString();

                tv_status.setText("");
                try {
                    APIRequestHandler.getInstance().loginRider(UserActivity.this, username, pwd);
                } catch (IllegalArgumentException e) {
                    tv_status.setText(e.getMessage());
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = et_username.getText().toString();
                name = et_name.getText().toString();
                pwd = et_pwd.getText().toString();

                tv_status.setText("");
                try {
                    APIRequestHandler.getInstance().updateRider(UserActivity.this, username, name, pwd);
                } catch (IllegalArgumentException e) {
                    tv_status.setText(e.getMessage());
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_status.setText("Logged out!");

                GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, null, GlobalMethods.STRING);
                GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, null, GlobalMethods.STRING);

                setData();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_username.setText("");
                et_name.setText("");
                et_pwd.setText("");

                al_result.clear();
                adapter_result.notifyDataSetChanged();

                tv_status.setText(getString(R.string.ready));
            }
        });

        setData();
    }

    void setData() {

        String user_status = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USER_STATUS, GlobalMethods.STRING);
        String saved_name = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_NAME, GlobalMethods.STRING);
        String saved_username = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USERNAME, GlobalMethods.STRING);
        String saved_pwd = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_PASSWORD, GlobalMethods.STRING);

        et_name.setText(saved_name);
        et_username.setText(saved_username);
        et_pwd.setText(saved_pwd);

        if (user_status != null) {
            btn_login.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.VISIBLE);

            if (user_status.equalsIgnoreCase(AppConstants.USER_STATUS_SUPER_ADMIN)) {
                btn_fetch.setVisibility(View.VISIBLE);
            } else {
                btn_fetch.setVisibility(View.GONE);
            }

        } else {
            btn_login.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
            btn_logout.setVisibility(View.GONE);

            btn_fetch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestSuccess(String type, BaseResponse response) {

        if (type.equalsIgnoreCase("login_rider") || type.equalsIgnoreCase("reg_rider")) {

            UserResponse obj = (UserResponse) response;

            al_result.clear();
            al_result.add(obj.getData());
            adapter_result.notifyDataSetChanged();

        } else if (type.equalsIgnoreCase("update_rider")) {

            UserResponse obj = (UserResponse) response;

            al_result.clear();
            al_result.add(obj.getData());
            adapter_result.notifyDataSetChanged();

        } else if (type.equalsIgnoreCase("get_riders")) {

            al_result.clear();
            al_result.addAll(((UserListResponse) response).getData());
            adapter_result.notifyDataSetChanged();

        } else if (type.equalsIgnoreCase("del_rider")) {

            al_result.clear();
            al_result.addAll(((UserListResponse) response).getData());
            adapter_result.notifyDataSetChanged();
        }
        tv_status.setText(response.getMessage());

        setData();
    }

    @Override
    public void onRequestFailure(String msg) {

        tv_status.setText(msg);
    }

    @Override
    public void onSubmitClick(String type, String... data) {

        if (type.equalsIgnoreCase("del_user")) {
            APIRequestHandler.getInstance().deleteRider(this, data[0]);
        }
    }

}