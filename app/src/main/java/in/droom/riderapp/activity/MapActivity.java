package in.droom.riderapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import in.droom.riderapp.R;
import in.droom.riderapp.adapter.TripListAdapter;
import in.droom.riderapp.api.APIRequestHandler;
import in.droom.riderapp.base.BaseActivity;
import in.droom.riderapp.model.BaseResponse;
import in.droom.riderapp.model.TripEntity;
import in.droom.riderapp.model.TripListResponse;
import in.droom.riderapp.model.TripResponse;
import in.droom.riderapp.model.TripRiderUpdateEntity;
import in.droom.riderapp.model.TripRiderUpdateResponse;
import in.droom.riderapp.model.UserEntity;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Start UI stuff
    Button btn_join, btn_new, btn_create_trip;
    View ll_begin, ll_create, ll_select;
    EditText et_name;

    ListView lv_trips;
    ArrayList<TripEntity> tripList;
    TripListAdapter tripsAdapter;

    // Map stuff
    MapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    ArrayList<Marker> riderMarkers;

    int updateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        riderMarkers = new ArrayList<>();

        lv_trips = (ListView) findViewById(R.id.lv_trips);
        tripList = new ArrayList<TripEntity>();
        tripsAdapter = new TripListAdapter(this, tripList);
        lv_trips.setAdapter(tripsAdapter);

        ll_begin = findViewById(R.id.ll_begin);
        ll_create = findViewById(R.id.ll_create_trip);
        ll_select = findViewById(R.id.ll_select_trip);
        ll_create.setVisibility(View.GONE);
        ll_select.setVisibility(View.GONE);

        et_name = (EditText) findViewById(R.id.et_trip_name);

        btn_create_trip = (Button) findViewById(R.id.btn_create_trip);

        btn_join = (Button) findViewById(R.id.btn_join_trip);
        btn_new = (Button) findViewById(R.id.btn_new_trip);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_select.setVisibility(View.VISIBLE);
                ll_create.setVisibility(View.GONE);

                getTrips();
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_select.setVisibility(View.GONE);
                ll_create.setVisibility(View.VISIBLE);
            }
        });

        btn_create_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().registerTrip(MapActivity.this, et_name.getText().toString().trim());
            }
        });
    }

    void getTrips() {
        APIRequestHandler.getInstance().getAllTrips(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        if (mCurrLocationMarker != null)
            mCurrLocationMarker.remove();
        drawMarker(location.getLatitude(), location.getLongitude(), "Me", -1);

        //move map camera
        if (updateCount == 0) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        }

        //stop location updates
        /*
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapActivity.this);
        }
        */

        if (updateCount % 2 == 0)
            APIRequestHandler.getInstance().updateRiderLocation(this, AppConstants.ACTIVE_TRIP_ID, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        updateCount++;
    }

    float[] colors = new float[]{BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_BLUE};

    void drawMarker(double latitude, double longitude, String name, int id) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(name);

        if (id == -1) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(colors[id % colors.length]));
            riderMarkers.add(mMap.addMarker(markerOptions));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) MapActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestSuccess(String type, BaseResponse response) {

        if (type.equalsIgnoreCase("get_trips")) {
            tripList.clear();
            tripList.addAll(((TripListResponse) response).getData());
            tripsAdapter.notifyDataSetChanged();
        } else if (type.equalsIgnoreCase("reg_trip")) {

            TripResponse obj = (TripResponse) response;
            GlobalMethods.showSnackbar(this, obj.getMessage());

            AppConstants.ACTIVE_TRIP_ID = obj.getData().getId();

            ll_begin.setVisibility(View.GONE);
            mapFragment.getMapAsync(MapActivity.this);

        } else if (type.equalsIgnoreCase("join_trip")) {

            TripResponse obj = (TripResponse) response;
            GlobalMethods.showSnackbar(this, obj.getMessage());

            AppConstants.ACTIVE_TRIP_ID = obj.getData().getId();

            ll_begin.setVisibility(View.GONE);
            mapFragment.getMapAsync(MapActivity.this);

        } else if (type.equalsIgnoreCase("update_loc")) {

            String user_id = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_USER_ID, GlobalMethods.STRING);

            for (Marker marker : riderMarkers) {
                marker.remove();
            }
            riderMarkers.clear();

            TripRiderUpdateResponse obj = (TripRiderUpdateResponse) response;
            for (int i = 0; i < obj.getData().size(); i++) {

                TripRiderUpdateEntity rider = obj.getData().get(i);

                if (!rider.getId().equalsIgnoreCase(user_id)) {

                    double lat = 0, lng = 0;

                    try {
                        lat = Double.parseDouble(rider.getLatitude());
                        lng = Double.parseDouble(rider.getLongitude());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                    drawMarker(lat, lng, rider.getUsername(), i);
                }
            }
        }
    }

    @Override
    public void onRequestFailure(String msg) {
        GlobalMethods.showSnackbar(this, msg);
    }

    @Override
    public void onSubmitClick(String type, String... data) {
        APIRequestHandler.getInstance().joinTrip(this, data[0], data[1]);
    }
}
