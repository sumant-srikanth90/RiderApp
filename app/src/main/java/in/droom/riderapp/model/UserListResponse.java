package in.droom.riderapp.model;

import java.util.ArrayList;

public class UserListResponse extends BaseResponse {

    ArrayList<UserEntity> data;

    public ArrayList<UserEntity> getData() {
        return data;
    }

    public void setData(ArrayList<UserEntity> data) {
        this.data = data;
    }
}
