package in.droom.riderapp.main;


import android.app.Application;
import android.content.Context;

public class RiderApplication extends Application {

    private static RiderApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

}