package in.droom.riderapp.model;

import java.util.ArrayList;

public class TripRiderResponse extends BaseResponse {

    ArrayList<TripRiderEntity> data;

    public ArrayList<TripRiderEntity> getData() {
        return data;
    }

    public void setData(ArrayList<TripRiderEntity> data) {
        this.data = data;
    }
}
