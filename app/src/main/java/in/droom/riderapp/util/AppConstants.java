package in.droom.riderapp.util;

public class AppConstants {

    public static String SERVER_IP = "192.168.3.66";
    public static String SERVER_PORT = "443";

    public static String BASE_DOMAIN = SERVER_IP;
    public static String BASE_URL = "http://" + BASE_DOMAIN;

    public static String ACTIVE_TRIP_ID = null;

    public static final String SUCCESS_CODE = "1";
    public static final String TOKEN_INVALID_CODE = "2";

    public static final String USER_STATUS_BASIC = "0", USER_STATUS_SUPER_ADMIN = "100";

    // PREFS CONSTANTS
    public static String PREFS_SERVER_IP = "server_ip";
    public static String PREFS_SERVER_PORT = "server_port";
    public static String PREFS_TOKEN = "token";
    public static String PREFS_NAME = "name";
    public static String PREFS_USERNAME = "username";
    public static String PREFS_PASSWORD = "password";
    public static String PREFS_USER_STATUS = "user_status";
    public static String PREFS_USER_ID = "user_id";
}
