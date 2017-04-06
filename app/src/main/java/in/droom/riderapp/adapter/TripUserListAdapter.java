package in.droom.riderapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import in.droom.riderapp.R;
import in.droom.riderapp.activity.MapActivity;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class TripUserListAdapter extends BaseAdapter {

    private ArrayList<UserEntity> list;
    private MapActivity act;

    private String userId, tripId, userStatus;

    public TripUserListAdapter(MapActivity act, ArrayList<UserEntity> list, String tripId) {
        this.list = list;
        this.act = act;
        this.tripId = tripId;

        userId = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USER_ID, GlobalMethods.STRING);
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
            v = inflater.inflate(R.layout.adapter_trip_user, viewGroup, false);

            holder.btn_remove = (Button) v.findViewById(R.id.btn_remove);

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

        if (isUserAdminInTrip()) {
            holder.btn_remove.setVisibility(View.VISIBLE);
        } else {
            holder.btn_remove.setVisibility(View.GONE);
        }

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showYesNoDialog(act, act.getString(R.string.confirm_del_user_trip), "remove_user", String.valueOf(rider.getId()), tripId);
            }
        });

        holder.id.setText("#" + rider.getId());
        holder.username.setText(rider.getUsername());
        holder.name.setText(rider.getName());
        holder.token.setText(rider.getToken());

        if (rider.getToken() != null) {
            holder.ll_token.setVisibility(View.VISIBLE);
        } else {
            holder.ll_token.setVisibility(View.GONE);
        }

        return v;
    }

    // If user is trip admin or super admin, he can remove other riders
    private boolean isUserAdminInTrip() {
        if (list != null) {
            for (UserEntity user : list) {
                if ((userStatus != null && userStatus.equalsIgnoreCase(AppConstants.USER_STATUS_SUPER_ADMIN)) ||
                        (user.getId() != null && user.getId().equalsIgnoreCase(userId) && user.getTrip_info().getIs_admin().equalsIgnoreCase("1")))
                    return true;
            }
        }
        return false;
    }

    private class ViewHolder {
        View ll_token;
        TextView id, username, name, token;
        Button btn_remove;
    }
}
