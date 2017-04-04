package in.droom.riderapp.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.app.Dialog;

import in.droom.riderapp.R;
import in.droom.riderapp.main.TestApplication;

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

    public static void saveToPrefs(String key, Object value, int type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TestApplication.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (type == STRING)
            editor.putString(key, (String) value);

        else if (type == BOOLEAN)
            editor.putBoolean(key, (Boolean) value);

        editor.commit();
    }

    public static Object getFromPrefs(String key, int type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TestApplication.getContext());
        if (type == STRING)
            return sharedPreferences.getString(key, null);

        else if (type == BOOLEAN)
            return sharedPreferences.getBoolean(key, false);

        return null;
    }

}
