package in.droom.riderapp.api;

import in.droom.riderapp.model.GenericResponse;
import in.droom.riderapp.model.RegisterResponse;
import in.droom.riderapp.model.UserListResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICommonInterface {

    @GET("/trips/get_riders")
    Call<UserListResponse> getAllRiders(
            @Query("token") String token);

    @FormUrlEncoded
    @POST("trips/register_rider")
    Call<RegisterResponse> registerRider(
            @Field("username") String username, @Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("trips/update_rider")
    Call<RegisterResponse> updateRider(
            @Field("token") String token, @Field("username") String username, @Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("trips/login_rider")
    Call<RegisterResponse> loginRider(
            @Field("username") String username, @Field("password") String password);

    @DELETE("trips/delete_rider/{id}")
    Call<GenericResponse> deleteRider(@Path("id") int id);

    @GET("trips/get_trips")
    Call<RegisterResponse> getTrips(@Field("token") String token);

    @FormUrlEncoded
    @POST("trips/register_trip")
    Call<RegisterResponse> registerTrip(
            @Field("token") String token, @Field("name") String name);

}
