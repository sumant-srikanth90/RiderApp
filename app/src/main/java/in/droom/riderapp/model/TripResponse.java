package in.droom.riderapp.model;

public class TripResponse extends BaseResponse {

    TripEntity data;

    public TripEntity getData() {
        return data;
    }

    public void setData(TripEntity data) {
        this.data = data;
    }
}
