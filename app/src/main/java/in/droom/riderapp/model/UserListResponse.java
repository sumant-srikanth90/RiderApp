package in.droom.riderapp.model;

import java.util.ArrayList;

public class UserListResponse extends BaseResponse {

    ArrayList<RegisterEntity> data;

    public ArrayList<RegisterEntity> getData() {
        return data;
    }

    public void setData(ArrayList<RegisterEntity> data) {
        this.data = data;
    }
}
