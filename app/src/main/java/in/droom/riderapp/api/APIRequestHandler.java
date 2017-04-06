package in.droom.riderapp.api;

import android.app.Activity;

import com.google.gson.JsonSyntaxException;

import in.droom.riderapp.activity.MapActivity;
import in.droom.riderapp.activity.UserActivity;
import in.droom.riderapp.R;
import in.droom.riderapp.model.TripListResponse;
import in.droom.riderapp.model.TripResponse;
import in.droom.riderapp.model.TripRiderResponse;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.model.UserResponse;
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
                        ((UserActivity) act).onRequestSuccess("get_riders", response.body());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                else
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void registerRider(final Activity act, String username, String name, final String pwd) {

        GlobalMethods.showLoadingDialog(act);

        apiCommonInterface.registerRider(username, name, pwd).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                        UserEntity data = response.body().getData();

                        GlobalMethods.saveToPrefs(AppConstants.PREFS_NAME, data.getName(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USERNAME, data.getUsername(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_PASSWORD, pwd, GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, data.getToken(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_ID, data.getId(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, data.getStatus(), GlobalMethods.STRING);

                        ((UserActivity) act).onRequestSuccess("reg_rider", response.body());
                    } else {
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                else
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void loginRider(final Activity act, String username, final String pwd) {

        GlobalMethods.showLoadingDialog(act);

        apiCommonInterface.loginRider(username, pwd).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                        UserEntity data = response.body().getData();

                        GlobalMethods.saveToPrefs(AppConstants.PREFS_NAME, data.getName(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USERNAME, data.getUsername(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_PASSWORD, pwd, GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, data.getToken(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_ID, data.getId(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, data.getStatus(), GlobalMethods.STRING);

                        ((UserActivity) act).onRequestSuccess("login_rider", response.body());
                    } else {
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                    }
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                else
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void updateRider(final Activity act, String username, String name, final String pwd) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.updateRider(token, username, name, pwd).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                        UserEntity data = response.body().getData();

                        GlobalMethods.saveToPrefs(AppConstants.PREFS_NAME, data.getName(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USERNAME, data.getUsername(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_PASSWORD, pwd, GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_TOKEN, data.getToken(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_ID, data.getId(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_USER_STATUS, data.getStatus(), GlobalMethods.STRING);

                        ((UserActivity) act).onRequestSuccess("update_rider", response.body());
                    } else
                        ((UserActivity) act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_server));
                else
                    ((UserActivity) act).onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void updateRiderLocation(final MapActivity act, String trip_id, String latitude, String longitude) {

        //GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.updateRiderLocation(token, trip_id, latitude, longitude).enqueue(new Callback<TripRiderResponse>() {
            @Override
            public void onResponse(Call<TripRiderResponse> call, Response<TripRiderResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        act.onRequestSuccess("update_loc", response.body());
                    else
                        act.onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    act.onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<TripRiderResponse> call, Throwable t) {
                //GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    System.out.println(act.getString(R.string.error_server));
                else
                    System.out.println(act.getString(R.string.error_conn));
            }
        });
    }

    public void deleteRider(final UserActivity act, String id) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.deleteRider(token, id).enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        act.onRequestSuccess("del_rider", response.body());
                    else
                        act.onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    act.onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    act.onRequestFailure(act.getString(R.string.error_server));
                else
                    act.onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void registerTrip(final MapActivity act, String trip_name) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.registerTrip(token, trip_name).enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        (act).onRequestSuccess("reg_trip", response.body());
                    else
                        (act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    (act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    act.onRequestFailure(act.getString(R.string.error_server));
                else
                    act.onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void leaveTrip(final MapActivity act, String rider_id, String trip_id) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.joinTrip(token, rider_id, trip_id, "1").enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        (act).onRequestSuccess("join_trip", response.body());
                    else
                        (act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    (act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    act.onRequestFailure(act.getString(R.string.error_server));
                else
                    act.onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

    public void joinTrip(final MapActivity act, String rider_id, String trip_id) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.joinTrip(token, rider_id, trip_id, "0").enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        (act).onRequestSuccess("join_trip", response.body());
                    else
                        (act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    (act).onRequestFailure(act.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    act.onRequestFailure(act.getString(R.string.error_server));
                else
                    act.onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }


    public void getAllTrips(final MapActivity act) {

        GlobalMethods.showLoadingDialog(act);

        String token = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_TOKEN, GlobalMethods.STRING);

        apiCommonInterface.getTrips(token).enqueue(new Callback<TripListResponse>() {
            @Override
            public void onResponse(Call<TripListResponse> call, Response<TripListResponse> response) {

                GlobalMethods.hideLoadingDialog(act);

                try {
                    if (response.body().getCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE))
                        (act).onRequestSuccess("get_trips", response.body());
                    else
                        (act).onRequestFailure(response.body().getMessage());
                } catch (Exception ex) {
                    (act).onRequestFailure(act.getString(R.string.error_server));
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TripListResponse> call, Throwable t) {
                GlobalMethods.hideLoadingDialog(act);
                if (t instanceof JsonSyntaxException)
                    act.onRequestFailure(act.getString(R.string.error_server));
                else
                    act.onRequestFailure(act.getString(R.string.error_conn));
            }
        });
    }

}
