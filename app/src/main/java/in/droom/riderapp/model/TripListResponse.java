package in.droom.riderapp.model;

import java.util.ArrayList;

public class TripListResponse extends BaseResponse {

    ArrayList<TripEntity> data;

    public ArrayList<TripEntity> getData() {
        return data;
    }

    public void setData(ArrayList<TripEntity> data) {
        this.data = data;
    }
}
