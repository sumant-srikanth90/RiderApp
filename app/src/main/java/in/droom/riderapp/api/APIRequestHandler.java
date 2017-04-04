package in.droom.riderapp.api;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import in.droom.riderapp.activity.UserActivity;
import in.droom.riderapp.R;
import in.droom.riderapp.model.GenericResponse;
import in.droom.riderapp.model.RegisterEntity;
import in.droom.riderapp.model.RegisterResponse;
import in.droom.riderapp.model.UserListResponse;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    static APICommonInterface apiCommonInterface;

    public static APIRequestHandler getInstance() {
        AppConstants.BASE_URL = "http://" + AppConstants.BASE_DOMAIN + ":8080/";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiCommonInterface = retrofit.create(APICommonInterface.class);

        return new APIRequestHandler();
    }


    // =============== API CALLS ============

    public void getAllRiders(final Activity act) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.getAllRiders(token).enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        ((UserActivity) act).onRequestSuccess(response.body());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

    public void registerRider(final Activity act, String username, String name, final String pwd) {

        GlobalMethods.showLoadingDialog(act);

        apiCommonInterface.registerRider(username, name, pwd).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                        RegisterEntity data = response.body().getData();

                        GlobalMethods.saveToPrefs(AppConstants.PREFS_NAME, data.getName(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USERNAME, data.getUsername(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_PASSWORD, pwd, GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, data.getToken(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, AppConstants.USER_STATUS_LOGGED_IN, GlobalMethods.STRING);

                        ((UserActivity) act).onRequestSuccess(response.body());
                    } else {
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                    }
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

    public void loginRider(final Activity act, String username, final String pwd) {

        GlobalMethods.showLoadingDialog(act);

        apiCommonInterface.loginRider(username, pwd).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                        RegisterEntity data = response.body().getData();

                        GlobalMethods.saveToPrefs(AppConstants.PREFS_NAME, data.getName(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USERNAME, data.getUsername(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_PASSWORD, pwd, GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, data.getToken(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, AppConstants.USER_STATUS_LOGGED_IN, GlobalMethods.STRING);

                        ((UserActivity) act).onRequestSuccess(response.body());
                    } else {
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                    }
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

    public void updateRider(final Activity act, String username, String name, String pwd) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.updateRider(token, username, name, pwd).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        ((UserActivity) act).onRequestSuccess(response.body());
                    else
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

    public void deleteRider(final AppCompatActivity act, int id) {

        GlobalMethods.showLoadingDialog(act);

        apiCommonInterface.deleteRider(id).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        ((UserActivity) act).onRequestSuccess(response.body());
                    else
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

    public void registerTrip(final Activity act, String trip_name) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.registerTrip(trip_name, token).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    ((UserActivity) act).onRequestSuccess(response.body());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
            }
        });
    }

}
