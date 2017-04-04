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
import in.droom.riderapp.util.GlobalMethods;

public class TripListAdapter extends BaseAdapter {

    ArrayList<TripEntity> list;
    BaseActivity act;

    public TripListAdapter(BaseActivity act, ArrayList<TripEntity> list) {
        this.list = list;
        this.act = act;
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
            v = inflater.inflate(R.layout.adapter_trip, viewGroup, false);

            holder.btn_join = (Button) v.findViewById(R.id.btn_join);

            holder.id = (TextView) v.findViewById(R.id.tv_id);
            holder.name = (TextView) v.findViewById(R.id.tv_name);
            holder.date = (TextView) v.findViewById(R.id.tv_date);
            holder.riders = (TextView) v.findViewById(R.id.tv_riders);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().joinTrip((MapActivity) act, list.get(i).getId());
            }
        });

        holder.id.setText("#" + list.get(i).getId());
        holder.name.setText("Name: " + list.get(i).getName());
        holder.date.setText("Date: " + list.get(i).getCreated_at());
        holder.riders.setText("Riders: " + list.get(i).getRiders().size());

        GlobalMethods.underlineText(holder.riders, 0, -1);

        holder.riders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalMethods.showRiderList(act, list.get(i).getRiders());
            }
        });

        return v;
    }

    private class ViewHolder {
        TextView id, name, date, riders;
        Button btn_join;
    }
}
