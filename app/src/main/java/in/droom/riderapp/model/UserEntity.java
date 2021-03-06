package in.droom.riderapp.model;


import java.io.Serializable;

public class UserEntity implements Serializable {

    String id;
    String username;
    String name;
    String status;
    String token;
    TripRiderEntity trip_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TripRiderEntity getTrip_info() {
        return trip_info;
    }

    public void setTrip_info(TripRiderEntity trip_info) {
        this.trip_info = trip_info;
    }
}
