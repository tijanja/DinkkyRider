package advancenji.teetech.com.dankkyrider2;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.maps.android.PolyUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.List;

import advancenji.teetech.com.dankkyrider2.helper.PlayerConfig;
import advancenji.teetech.com.dankkyrider2.helper.Utils;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    boolean mLocationPermissionGranted = false;

    private static final float DEFAULT_ZOOM = 15.5f;
    Location mLastKnownLocation;

    boolean moveCam = true;

    TextView to, from,disTime,priceRang;

    private GoogleApiClient mGoogleApiClient;

    GeoDataClient mGeoDataClient;

    PlaceDetectionClient mPlaceDetectionClient;

    FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationRequest mLocationRequest;
    private Intent intent;
    private String[] mLikelyPlaceNames;
    private LatLng latLngFrom;
    private LatLng latLngTO;

    LinearLayout price_details;

    CardView paymentPane;

    GridLayout addressBox;

    String myLocation,destination;
    private boolean isDestinationSet = false;
    private Task locationResult;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();


        paymentPane = findViewById(R.id.paymentPane);


        mGeoDataClient = Places.getGeoDataClient(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);

        disTime = findViewById(R.id.disTime);
        priceRang = findViewById(R.id.priceRang);
        addressBox = findViewById(R.id.addressBox);



        try {

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()

                    .setCountry("NG")
                    .build();

            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .setBoundsBias(new LatLngBounds(new LatLng(6.45407, 3.39467), new LatLng(6.45907, 3.39967)))
                    .build(MapsActivity.this);


        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, 1);

            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, 2);
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //mMap.setMyLocationEnabled(true);

        getPreviousLocation();

//
//        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
//        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//                from.setText(likelyPlaces.get(0).getPlace().getName());
//                from.invalidate();
//                //LatLng lagos = new LatLng(6.45407,3.39467);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(likelyPlaces.get(0).getPlace().getLatLng(), 16));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(likelyPlaces.get(0).getPlace().getLatLng(), 16 ),2000,null);
//                mMap.addMarker(new MarkerOptions().position(likelyPlaces.get(0).getPlace().getLatLng()).title(likelyPlaces.get(0).getPlace().getName().toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));
//                likelyPlaces.release();
//            }
//        });
        // Add a marker in Sydney and move the camera

        //mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape)).title("Marker in Sydney"));


        //mMap.setTrafficEnabled(true);

//        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
//            @Override
//            public void onCircleClick(Circle circle)
//            {
//                circle.setFillColor(Color.GREEN);
//            }
//        });



        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.mapstyle_night));

       // getMapsApiDirectionsUrl(new LatLng(6.4259634,3.4405344),new LatLng(6.5823080, 3.2742681));


    }


    private void  getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        new AsyncDownload(origin,dest).execute();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    private class AsyncDownload extends AsyncTask<Void,Void,DirectionsResult>
    {
        String url;
        LatLng mOrigin;
        LatLng mDest;
        List<LatLng> latLngs;
        private PolylineOptions polylines;
        DirectionsResult directionsResult;

        public AsyncDownload(LatLng origin,LatLng dest)
        {
            mDest = dest;
            mOrigin = origin;

        }

        @Override
        protected DirectionsResult doInBackground(Void... voids) {



            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

            GenericUrl url = new GenericUrl("https://maps.googleapis.com/maps/api/directions/json");
            url.put("key",PlayerConfig.API_KEY);
            url.put("origin",mOrigin.latitude+","+mOrigin.longitude);
            url.put("destination",mDest.latitude+","+mDest.longitude);
            url.put("departure_time","now");
            url.put("mode","driving");
            url.put("language","en");

            Log.i("url",url.toString());

            HttpRequest request = null;
            try {
                request = requestFactory.buildGetRequest(url);
                HttpResponse httpResponse = request.execute();



                directionsResult = httpResponse.parseAs(DirectionsResult.class);



            } catch (IOException e) {
                Log.e("error",e.getMessage());
            }


            return directionsResult;
        }

        protected void onPostExecute(DirectionsResult directionsResult) {

            moveCam=false;

            String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;

            Log.i("direction",directionsResult.routes.get(0).legs.get(0).distance.text);

            disTime.setText(directionsResult.routes.get(0).legs.get(0).duration.text+" / "+directionsResult.routes.get(0).legs.get(0).distance.text);

            priceRang.setText(Utils.formatPrice(getPriceRange(directionsResult.routes.get(0).legs.get(0).duration.value,directionsResult.routes.get(0).legs.get(0).distance.value)+"")+"/"+Utils.formatPrice(getPriceRangeForTraffic(directionsResult.routes.get(0).legs.get(0).durationInTraffic.value,directionsResult.routes.get(0).legs.get(0).distance.value)+""));

            latLngs = PolyUtil.decode(encodedPoints);
            polylines = new PolylineOptions()
                    .addAll(latLngs)
                    .color(Color.BLACK)
                    .width(8)
                    .endCap(new RoundCap());

            mMap.addPolyline(polylines);

            addressBox.setVisibility(View.GONE);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(int i=0;i<latLngs.size();i++)
            {
                builder.include(latLngs.get(i));
            }


            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngTO, 10.50f),2000,null);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200),2000,null);

            mMap.setInfoWindowAdapter(new MapInfoWindowAdapter(MapsActivity.this));

            MarkerOptions markerOptions = new MarkerOptions().title(destination).position(latLngTO).snippet("gggggggg").infoWindowAnchor(0.1f, 0.2f);

            MarkerOptions markerOptionsFromLocat = new MarkerOptions().title(destination).position(latLngFrom).snippet("gggggggg").infoWindowAnchor(-3.5f, 0.9f);




            Marker marker = mMap.addMarker(markerOptions);
            marker.showInfoWindow();

            Marker markerFromLocate = mMap.addMarker(markerOptionsFromLocat);
            //markerFromLocate.showInfoWindow();


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    mMap.clear();
                    startActivityForResult(intent, 2);
                }
            });
            paymentPane.setVisibility(View.VISIBLE);
            paymentPane.invalidate();

            isDestinationSet=true;


            //mMap.
        }

    }

    public void registerRequestUpdate(final LocationListener listener)
    {

        mLocationRequest = LocationRequest.create();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(1000); // every second

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                    Toast.makeText(MapsActivity.this, "Requesting Location Service", Toast.LENGTH_SHORT).show();
                }
                catch (SecurityException e)
                {
                    Log.e("security error",e.getMessage());
                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Log.e("Exception---",e.getMessage());
                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    if (!mGoogleApiClient.isConnected())
                    {

                        mGoogleApiClient.connect();

                    }

                    registerRequestUpdate(listener);

                }
            }
        }, 1000);
    }

    static class DirectionsResult {
        @Key("routes")
        public List<Route> routes;
    }

    public static class Route {

        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyLine;

        @Key("legs")
        public List<Legs> legs;
    }

    public static class OverviewPolyLine {
        @Key("points")
        public String points;
    }

    public static class Legs {
        @Key("distance")
        public Distance distance;

        @Key("duration")
        public Duration duration;

        @Key("duration_in_traffic")
        public DurationInTraffic durationInTraffic;
    }

    public static class Distance{
        @Key("text")
        public String text;

        @Key("value")
        public int value;
    }

    public static class Duration{
        @Key("text")
        public String text;

        @Key("value")
        public int value;
    }

    public static class DurationInTraffic
    {
        @Key("text")
        public String text;

        @Key("value")
        public int value;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMap.clear();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
            {

                Place place = PlaceAutocomplete.getPlace(this, data);

                        from.setText(place.getName());
                        from.invalidate();
                //Log.i("place", "Place: " + );
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("bbbb", status.getStatusMessage());
                to.setText(destination);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == 2)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(this, data);

                destination = place.getName().toString();
                to.setText(destination);
                to.invalidate();

                latLngTO = place.getLatLng();
                getMapsApiDirectionsUrl(latLngFrom,latLngTO);

                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latLngFrom.latitude+","+latLngFrom.longitude + "&destinations=" + latLngTO.latitude+","+latLngTO.longitude + "&mode=driving&language=en-FR&key=" + PlayerConfig.API_KEY;

                Log.i("matrix-url",url);
                Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
                    @Override
                    public void success(String response) {
                        Log.i("matrix-res",response);
                    }

                    @Override
                    public void fail(String error) {

                    }
                });


            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("bbbb", status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }
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

//    private void updateLocationUI() {
//        if (mMap == null) {
//            return;
//        }
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                mMap.setMyLocationEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//
//
//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (mLocationPermissionGranted) {
//                Task locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 16));
//                        }
//                        else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch(SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
//
//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace(null);
//            placeResult.addOnCompleteListener
//                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                    count = likelyPlaces.getCount();
//                                } else {
//                                    count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//
//                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                            .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                    i++;
//                                    if (i > (count - 1)) {
//                                        break;
//                                    }
//                                }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                                likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }

    public void updateUi(final String s)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                from.setText(s);
                from.invalidate();
            }
        });

    }


    public double getPriceRange(int t,int d)
    {
        int time = t/60;
        int distance = d/1000;

        double price = (distance*30)+(time*11)+300;
        return price;
    }

    public double getPriceRangeForTraffic(int t,int d)
    {
        int time = t/60;
        int distance = d/1000;

        double price = (distance*30)+(time*11)+300;
        return price;
    }

    class InfoWindowData {
        String mPlace;
        public InfoWindowData(String place)
        {
            mPlace = place;
        }

        public String getmPlace() {
            return mPlace;
        }
    }

    @Override
    public void onBackPressed()
    {
        if(isDestinationSet)
        {
            mMap.clear();
            //latLngFrom = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM),2000,null);
            //CameraPosition cameraPosition = mMap.getCameraPosition();
            mMap.addMarker(new MarkerOptions().position(latLngFrom).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));

            //getPreviousLocation();

            to.setText("Where to?");

            addressBox.setVisibility(View.VISIBLE);
            addressBox.invalidate();
            paymentPane.setVisibility(View.GONE);
            paymentPane.invalidate();
            isDestinationSet = false;

        }
        else
        {
            finish();
        }



    }

    public void getPreviousLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //mMap.setMyLocationEnabled(true);
            mMap.setBuildingsEnabled(true);

            locationResult = mFusedLocationProviderClient.getLastLocation();

//            PendingIntent pendingIntent = PendingIntent.getService(this,PendingIntent.FLAG_UPDATE_CURRENT,new Intent(),1);
//            mFusedLocationProviderClient.requestLocationUpdates(LocationRequest.create(),pendingIntent).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//
//                    Log.d("MainActivity", "Result: " + task.getResult());
//
////                    mLastKnownLocation = (Location) task.getResult();
////                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&key="+ PlayerConfig.API_KEY;
////                    Log.i("url",url);
////                    Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
////                        @Override
////                        public void success(String response) {
////                            Log.i("geoLocation-response",response);
////
////                            try {
////
////                                JSONObject jsonObject = new JSONObject(response);
////                                JSONArray jsonArray = jsonObject.getJSONArray("results");
////                                if(jsonArray.length()!=0)
////                                {
////                                    JSONObject resultObj = jsonArray.getJSONObject(0);
////                                    JSONArray  addressComp = resultObj.getJSONArray("address_components");
////                                    //updateUi(resultObj.getString("formatted_address"));
////                                    updateUi(addressComp.getJSONObject(0).getString("short_name")+" "+addressComp.getJSONObject(1).getString("short_name")/*+" "+addressComp.getJSONObject(2).getString("long_name")*/);
////                                }
////                                else
////                                {
////                                    Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
////                                }
////
////
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////
////                        }
////
////                        @Override
////                        public void fail(String error) {
////
////                        }
////                    });
////
////                    latLngFrom = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
////                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM));
////                    CameraPosition cameraPosition = mMap.getCameraPosition();
////                    mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));
//
//                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM),2000,null);
//                }
//            });

            locationResult.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                    mLastKnownLocation = (Location) (Location)o;
                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&key="+ PlayerConfig.API_KEY;

                    Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
                        @Override
                        public void success(String response) {
                            Log.i("geoLocation-response-2",response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                if(jsonArray.length()!=0)
                                {
                                    JSONObject resultObj = jsonArray.getJSONObject(0);
                                    JSONArray  addressComp = resultObj.getJSONArray("address_components");
                                    //updateUi(resultObj.getString("formatted_address"));
                                    updateUi(addressComp.getJSONObject(0).getString("short_name")+" "+addressComp.getJSONObject(1).getString("short_name")/*+" "+addressComp.getJSONObject(2).getString("long_name")*/);
                                }
                                else
                                {
                                    Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void fail(String error) {
                            Log.e("fail-error",error);
                        }
                    });

                    latLngFrom = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM));
                    CameraPosition cameraPosition = mMap.getCameraPosition();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));

                }
            });

//            locationResult.addOnCompleteListener(this,new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    mLastKnownLocation = (Location) task.getResult();
//                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mLastKnownLocation.getLatitude()+","+mLastKnownLocation.getLongitude()+"&key="+ PlayerConfig.API_KEY;
//                    Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
//                        @Override
//                        public void success(String response) {
//                            Log.i("geoLocation-response",response);
//
//                            try {
//
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("results");
//                                if(jsonArray.length()!=0)
//                                {
//                                    JSONObject resultObj = jsonArray.getJSONObject(0);
//                                    JSONArray  addressComp = resultObj.getJSONArray("address_components");
//                                    //updateUi(resultObj.getString("formatted_address"));
//                                    updateUi(addressComp.getJSONObject(0).getString("short_name")+" "+addressComp.getJSONObject(1).getString("short_name")/*+" "+addressComp.getJSONObject(2).getString("long_name")*/);
//                                }
//                                else
//                                {
//                                    Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void fail(String error) {
//                            Log.e("fail-error",error);
//                        }
//                    });
//
//                    latLngFrom = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM));
//                    CameraPosition cameraPosition = mMap.getCameraPosition();
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));
//
//                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngFrom, DEFAULT_ZOOM),2000,null);
//
//                }
//            });

//            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//                @Override
//                public void onCameraChange(CameraPosition cameraPosition) {
//                    if(moveCam)
//                    {
//                        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+cameraPosition.target.latitude+","+cameraPosition.target.longitude+"&key="+ PlayerConfig.API_KEY;
//                        Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
//                            @Override
//                            public void success(String response) {
//                                Log.i("geoLocation-response",response);
//
//                                try {
//
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//                                    if(jsonArray.length()!=0)
//                                    {
//                                        JSONObject resultObj = jsonArray.getJSONObject(0);
//                                        JSONArray  addressComp = resultObj.getJSONArray("address_components");
//                                        //updateUi(resultObj.getString("formatted_address"));
//                                        updateUi(addressComp.getJSONObject(0).getString("long_name")+" "+addressComp.getJSONObject(1).getString("long_name")+" "+addressComp.getJSONObject(2).getString("long_name"));
//
//                                    }
//                                    else
//                                    {
//                                        Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
//                                    }
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                            @Override
//                            public void fail(String error) {
//
//                            }
//                        });
//                    }
//
////                    mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));
////                    Log.i("zoom",cameraPosition.zoom+"");
//                }
//            });

            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                    if(moveCam)
                    {
                        mMap.clear();
                        CameraPosition cameraPosition = mMap.getCameraPosition();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)).draggable(true).snippet("Please move the marker if needed.").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_with_a_person_shape_png)));

                    }

                }
            });

            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {

                    CameraPosition cameraPosition = mMap.getCameraPosition();
                    url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+cameraPosition.target.latitude+","+cameraPosition.target.longitude+"&key="+ PlayerConfig.API_KEY;


                }
            });


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                    Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
                        @Override
                        public void success(String response) {
                            Log.i("geoLocation-response-1",response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                if(jsonArray.length()!=0)
                                {
                                    JSONObject resultObj = jsonArray.getJSONObject(0);
                                    JSONArray  addressComp = resultObj.getJSONArray("address_components");
                                    //updateUi(resultObj.getString("formatted_address"));
                                    updateUi(addressComp.getJSONObject(0).getString("long_name")+" "+addressComp.getJSONObject(1).getString("long_name")+" "+addressComp.getJSONObject(2).getString("long_name"));

                                }
                                else
                                {
                                    Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void fail(String error) {

                        }
                    });
                }
            });

//            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//                @Override
//                public void onCameraMove() {
//
//                    CameraPosition cameraPosition = mMap.getCameraPosition();
//                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+cameraPosition.target.latitude+","+cameraPosition.target.longitude+"&key="+ PlayerConfig.API_KEY;
//
//                    Utils.httpGetConnection(url, new advancenji.teetech.com.dankkyrider2.helper.HttpResponse() {
//                        @Override
//                        public void success(String response) {
//                            Log.i("geoLocation-response",response);
//
//                            try {
//
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("results");
//                                if(jsonArray.length()!=0)
//                                {
//                                    JSONObject resultObj = jsonArray.getJSONObject(0);
//                                    JSONArray  addressComp = resultObj.getJSONArray("address_components");
//                                    //updateUi(resultObj.getString("formatted_address"));
//                                    updateUi(addressComp.getJSONObject(0).getString("long_name")+" "+addressComp.getJSONObject(1).getString("long_name")+" "+addressComp.getJSONObject(2).getString("long_name"));
//
//                                }
//                                else
//                                {
//                                    Log.i("error","You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account");
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void fail(String error) {
//
//                        }
//                    });
//                }
//            });
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

}
