package advancenji.teetech.com.dankkyrider2.driver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import advancenji.teetech.com.dankkyrider2.MapsActivity;
import advancenji.teetech.com.dankkyrider2.R;
import advancenji.teetech.com.dankkyrider2.helper.HttpResponse;
import advancenji.teetech.com.dankkyrider2.helper.PlayerConfig;
import advancenji.teetech.com.dankkyrider2.helper.Utils;
import advancenji.teetech.com.dankkyrider2.model.Coordinate;
import advancenji.teetech.com.dankkyrider2.model.SharedValues;

public class DriverMapsActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Intent intent;

    GeoDataClient mGeoDataClient;

    PlaceDetectionClient mPlaceDetectionClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;

    FusedLocationProviderClient mFusedLocationProviderClient;
    DatabaseReference mDatabase;
    private Task locationResult;
    private LatLng latLngFrom;
    private LatLng latLngTO;
    Location mLastKnownLocation;
    public static final float DEFAULT_ZOOM = 15.5f;
    FirebaseUser firebaseUser;

    String userid;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        userid = SharedValues.getValue(this,"userid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_home));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setElevation(0);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Glide.with(this).load(firebaseUser.getPhotoUrl()).into((ImageView) findViewById(R.id.driverAvatar));

        mGeoDataClient = Places.getGeoDataClient(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Snackbar snackbar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_INDEFINITE).setAction("Action", null);
                snackbar.show();

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key="+PlayerConfig.API_KEY+"&location="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&radius=500&type=restaurant";

                Utils.httpGetConnection(url, new HttpResponse() {
                    @Override
                    public void success(String response) {

                        Location location = null;
                        JSONObject resultObject = null;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject != null && jsonObject.has("results"))
                            {
                                JSONArray resultsArray = jsonObject.getJSONArray("results");
                                resultObject = resultsArray.getJSONObject(0);

                                Coordinate driverCooord = new Coordinate();

                                if(resultObject != null && resultObject.has("geometry")) {
                                    JSONObject locationObject = resultObject.getJSONObject("geometry").getJSONObject("location");
                                    location = new Location("");
                                    location.setLatitude(locationObject.getDouble("lat"));
                                    location.setLongitude(locationObject.getDouble("lng"));

                                    driverCooord.setLandMark(resultObject.getString("place_id"));
                                }


                                driverCooord.setLatitude(latLngFrom.latitude);
                                driverCooord.setLongitude(latLngFrom.longitude);


                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference dinkky = mDatabase.child("dinkky");
                                String id = mDatabase.push().getKey();


                                Driver driver = new Driver(firebaseUser.getUid(),firebaseUser.getDisplayName(),mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                                driver.setCoordinate(driverCooord);
                                dinkky.child("onlineDrivers").child(firebaseUser.getUid()).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        snackbar.dismiss();
                                    }
                                });
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(String error) {

                    }
                });

            }
        });
//
//
//        try {
//
//            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                    .setCountry("NG")
//                    .build();
//
//            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                    .setFilter(typeFilter)
//                    .setBoundsBias(new LatLngBounds(new LatLng(6.45407, 3.39467), new LatLng(6.45907, 3.39967)))
//                    .build(DriverMapsActivity.this);
//
//
//        } catch (GooglePlayServicesRepairableException e) {
//            // TODO: Handle the error.
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night));

        getCurrentLocation();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void getCurrentLocation()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            mMap.setBuildingsEnabled(true);

            locationResult = mFusedLocationProviderClient.getLastLocation();

            locationResult.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                    Log.e("mylocation","myloction");
                    if(o != null)
                    {
                        mLastKnownLocation = (Location)o;
                        latLngFrom = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM));
                        CameraPosition cameraPosition = mMap.getCameraPosition();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));

                    }

                }
            });
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode)
        {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                }
            }

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{

            }
        }
        //updateLocationUI();
    }
}
