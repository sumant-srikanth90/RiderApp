package in.droom.riderapp.main;


import android.app.Application;
import android.content.Context;

public class TestApplication extends Application {

    private static TestApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

}