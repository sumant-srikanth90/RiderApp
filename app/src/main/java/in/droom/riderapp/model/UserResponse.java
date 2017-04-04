package in.droom.riderapp.model;

public class UserResponse extends BaseResponse {

    UserEntity data;

    public UserEntity getData() {
        return data;
    }

    public void setData(UserEntity data) {
        this.data = data;
    }
}
