package in.droom.riderapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Dialog;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.droom.riderapp.R;
import in.droom.riderapp.activity.MapActivity;
import in.droom.riderapp.adapter.TripUserListAdapter;
import in.droom.riderapp.adapter.UserListAdapter;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.main.RiderApplication;
import in.droom.riderapp.model.UserEntity;

public class GlobalMethods {

    static Dialog mDialog;
    static int curOrientation;

    public static final int STRING = 0, BOOLEAN = 1, INTEGER = 2;

    public static void showLoadingDialog(final Activity ctx) {

        mDialog = new Dialog(ctx);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.setContentView(R.layout.dialog_loading);

        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        // Lock orientation
        curOrientation = ctx.getResources().getConfiguration().orientation;
        if (curOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ctx.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            ctx.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        // Prevent back press/cancel
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        ctx.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mDialog.show();
    }

    public static void hideLoadingDialog(Activity ctx) {
        mDialog.hide();
        ctx.setRequestedOrientation(curOrientation);
        ctx.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // =================
    // ======= DIALOGS

    public static void showYesNoDialog(final BaseActivity ctx, String msg, final String type, final String... data) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_yes_no);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        TextView tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(msg);

        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.onSubmitClick(type, data);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showRiderList(MapActivity ctx, ArrayList<UserEntity> riderList, String tripId) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_rider_list);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        ListView lv_riders = (ListView) dialog.findViewById(R.id.lv_riders);
        TripUserListAdapter adapter = new TripUserListAdapter(ctx, riderList, tripId);
        lv_riders.setAdapter(adapter);

        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    // ==================

    // Takes start and end pos
    public static void underlineText(View view, int start, int end) {
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        if (view instanceof TextView) {
            strBuilder.append(((TextView) view).getText());
            if (end == -1) {
                end = strBuilder.length();
            }
            strBuilder.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ((TextView) view).setText(strBuilder);
        } else if (view instanceof Button) {
            strBuilder.append(((Button) view).getText());
            if (end == -1) {
                end = strBuilder.length();
                ;
            }
            strBuilder.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ((Button) view).setText(strBuilder);
        }
    }

    public static void saveToPrefs(String key, Object value, int type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RiderApplication.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (type == STRING)
            editor.putString(key, (String) value);

        else if (type == BOOLEAN)
            editor.putBoolean(key, (Boolean) value);

        editor.commit();
    }

    public static Object getFromPrefs(String key, int type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RiderApplication.getContext());
        if (type == STRING)
            return sharedPreferences.getString(key, null);

        else if (type == BOOLEAN)
            return sharedPreferences.getBoolean(key, false);

        return null;
    }

    public static void showSnackbar(Activity act, String msg) {
        Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(act, R.color.white));
        snackbar.show();
    }
}
