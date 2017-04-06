package in.droom.riderapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import in.droom.riderapp.R;
import in.droom.riderapp.activity.MapActivity;
import in.droom.riderapp.activity.UserActivity;
import in.droom.riderapp.api.APIRequestHandler;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class UserListAdapter extends BaseAdapter {

    ArrayList<UserEntity> list;
    BaseActivity act;

    String userStatus;

    public UserListAdapter(BaseActivity act, ArrayList<UserEntity> list) {
        this.list = list;
        this.act = act;

        userStatus = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USER_STATUS, GlobalMethods.STRING);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder holder;

        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_user, viewGroup, false);

            holder.btn_delete = (Button) v.findViewById(R.id.btn_delete);

            holder.ll_token = v.findViewById(R.id.ll_token);

            holder.id = (TextView) v.findViewById(R.id.tv_id);
            holder.username = (TextView) v.findViewById(R.id.tv_username);
            holder.name = (TextView) v.findViewById(R.id.tv_name);
            holder.token = (TextView) v.findViewById(R.id.tv_token);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final UserEntity rider = list.get(i);

        if (userStatus != null && rider.getId().equalsIgnoreCase(AppConstants.USER_STATUS_SUPER_ADMIN)) {
            holder.btn_delete.setVisibility(View.VISIBLE);
            holder.token.setVisibility(View.VISIBLE);
        } else {
            holder.btn_delete.setVisibility(View.GONE);
            holder.token.setVisibility(View.GONE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showYesNoDialog(act, act.getString(R.string.confirm_del_user), "del_user", String.valueOf(list.get(i).getId()));
            }
        });

        holder.id.setText("#" + list.get(i).getId());
        holder.username.setText(rider.getUsername());
        holder.name.setText(rider.getName());
        holder.token.setText(rider.getToken());

        return v;
    }

    private class ViewHolder {
        View ll_token;
        TextView id, username, name, token;
        Button btn_delete;
    }
}
