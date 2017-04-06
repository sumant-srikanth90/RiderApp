package in.droom.riderapp.adapter;


import android.app.Activity;
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
import in.droom.riderapp.api.APIRequestHandler;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.model.TripEntity;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class TripListAdapter extends BaseAdapter {

    ArrayList<TripEntity> list;
    MapActivity act;

    String userId;

    public TripListAdapter(MapActivity act, ArrayList<TripEntity> list) {
        this.list = list;
        this.act = act;

        this.userId = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USER_ID, GlobalMethods.STRING);
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
        final ViewHolder holder;

        if (v == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_trip, viewGroup, false);

            holder.btn_join = (Button) v.findViewById(R.id.btn_join);
            holder.btn_leave = (Button) v.findViewById(R.id.btn_leave);

            holder.id = (TextView) v.findViewById(R.id.tv_id);
            holder.name = (TextView) v.findViewById(R.id.tv_name);
            holder.date = (TextView) v.findViewById(R.id.tv_date);
            holder.riders = (TextView) v.findViewById(R.id.tv_riders);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final TripEntity trip = list.get(i);

        if (!isUserInTrip(trip.getRiders())) {
            holder.btn_leave.setVisibility(View.GONE);
        } else {
            holder.btn_leave.setVisibility(View.VISIBLE);
        }

        holder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showYesNoDialog(act, act.getString(R.string.confirm_join_trip), "join_trip", null, trip.getId());
            }
        });

        holder.btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showYesNoDialog(act, act.getString(R.string.confirm_leave_trip), "leave_trip", null, trip.getId());
            }
        });

        holder.id.setText("#" + trip.getId());
        holder.name.setText(trip.getName());
        holder.date.setText(trip.getCreated_at());
        holder.riders.setText(trip.getRiders().size() + " Riders");

        GlobalMethods.underlineText(holder.riders, 0, -1);

        holder.riders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showRiderList(act, trip.getRiders(), trip.getId());
            }
        });

        return v;
    }

    private class ViewHolder {
        TextView id, name, date, riders;
        Button btn_join, btn_leave;
    }

    // If user is in trip, show different text on button
    private boolean isUserInTrip(ArrayList<UserEntity> userList) {
        if (userList != null) {
            for (UserEntity user : userList) {
                if (user.getId() != null && user.getId().equalsIgnoreCase(userId))
                    return true;
            }
        }
        return false;
    }
}
