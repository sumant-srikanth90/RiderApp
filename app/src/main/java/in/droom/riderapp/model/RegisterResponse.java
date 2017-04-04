package in.droom.riderapp.model;

public class RegisterResponse extends BaseResponse {

    RegisterEntity data;

    public RegisterEntity getData() {
        return data;
    }

    public void setData(RegisterEntity data) {
        this.data = data;
    }
}
