package in.droom.riderapp.api;

import in.droom.riderapp.model.GenericResponse;
import in.droom.riderapp.model.TripListResponse;
import in.droom.riderapp.model.TripResponse;
import in.droom.riderapp.model.TripRiderUpdateResponse;
import in.droom.riderapp.model.UserResponse;
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
    Call<UserResponse> registerRider(
            @Field("username") String username, @Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("trips/update_rider")
    Call<UserResponse> updateRider(
            @Field("token") String token, @Field("username") String username, @Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("trips/login_rider")
    Call<UserResponse> loginRider(
            @Field("username") String username, @Field("password") String password);

    @DELETE("trips/delete_rider/{id}")
    Call<UserListResponse> deleteRider(@Path("token") String token, @Path("id") String id);

    @GET("trips/get_trips")
    Call<TripListResponse> getTrips(@Query("token") String token);

    @FormUrlEncoded
    @POST("trips/register_trip")
    Call<TripResponse> registerTrip(
            @Field("token") String token, @Field("name") String name);

    @FormUrlEncoded
    @POST("trips/join_trip")
    Call<TripResponse> joinTrip(
            @Field("token") String token, @Field("id") String id);

    @FormUrlEncoded
    @POST("trips/update_location")
    Call<TripRiderUpdateResponse> updateRiderLocation(
            @Field("token") String token, @Field("trip_id") String trip_id, @Field("latitude") String latitude, @Field("longitude") String longitude);

}
