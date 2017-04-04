package in.droom.riderapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TripEntity implements Serializable {

    String id;
    String name;
    String created_at;
    ArrayList<UserEntity> riders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<UserEntity> getRiders() {
        return riders;
    }

    public void setRiders(ArrayList<UserEntity> riders) {
        this.riders = riders;
    }
}
