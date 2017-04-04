package in.droom.riderapp.model;

import java.util.ArrayList;

public class TripRiderUpdateResponse extends BaseResponse {

    ArrayList<TripRiderUpdateEntity> data;

    public ArrayList<TripRiderUpdateEntity> getData() {
        return data;
    }

    public void setData(ArrayList<TripRiderUpdateEntity> data) {
        this.data = data;
    }
}
