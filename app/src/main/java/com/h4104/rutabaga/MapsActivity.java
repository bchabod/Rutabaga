package com.h4104.rutabaga;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private LocationManager service;
    private MyLocationListener locationListener;
    private ArrayList<Marker> restaurantMarkers;
    private Marker lastSelected;

    private Button information;
    private Button filters_button;
    private FloatingActionButton localize;
    private Button settings;
    private FloatingActionButton facebook;
    private Toolbar toolbar;
    private EditText searchField;
    private LinearLayout filters;

    private boolean hidden = false;
    private boolean select = false;
    private boolean focus = false;

    private CallbackManager callbackmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(MapsActivity.this);
        callbackmanager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraChangeListener(this);
        mMap.clear();

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        information = (Button) findViewById(R.id.information);
        settings = (Button) findViewById(R.id.settings);
        filters_button = (Button) findViewById(R.id.filters);
        localize = (FloatingActionButton) findViewById(R.id.localize);
        facebook = (FloatingActionButton) findViewById(R.id.facebook);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchField = (EditText) findViewById(R.id.search_field);
        filters = (LinearLayout) findViewById(R.id.layout_filters);

        restaurantMarkers = new ArrayList<>();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.7814008, 4.8735197), 17.0f));
        information.setVisibility(View.GONE);

        try {
            service = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new MyLocationListener();
            service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
        catch (SecurityException e) {
        }

        Marker newMarker;
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.781032, 4.873573)).title("Castor et Pollux").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.781053, 4.873225)).title("Prévert").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.780796, 4.872254)).title("Supr'M café").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.784173, 4.874862)).title("Olivier").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.783952, 4.875026)).title("Grillon").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.783884, 4.874865)).title("Sandwicherie").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.780960, 4.876180)).title("Restaurant Universitaire").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.778789, 4.871979)).title("Resto'U").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.780565, 4.875876)).title("Grignote").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.777137, 4.874586)).title("Snack du Campus").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.776967, 4.876093)).title("Panorama").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.778702, 4.873661)).title("Mosaic").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);
        newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(45.781919, 4.878656)).title("Roccasecca").icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        restaurantMarkers.add(newMarker);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        localize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } else {
                    WaitForLocalisationParams params = new WaitForLocalisationParams(mMap, service, locationListener);
                    WaitForLocalisation asyncLocalisation = new WaitForLocalisation();
                    asyncLocalisation.execute(params);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, RestaurantActivity.class);
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {

        filters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hidden) {
                    filters.setVisibility(View.VISIBLE);
                    hidden = false;
                } else {
                    filters.setVisibility(View.GONE);
                    hidden = true;
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFacebook();
            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(new LatLng(45.776677, 4.858178), new LatLng(45.776677, 4.890499), new LatLng(45.787019, 4.890499), new LatLng(45.787019, 4.858178), new LatLng(45.776677, 4.858178));
        polylineOptions.width(2.5f);
        polylineOptions.color(Color.RED);
        mMap.addPolyline(polylineOptions);
    }

    @Override
    public boolean onMarkerClick(Marker m) {
        if (m.getTitle() != "Vous êtes ici :)") {
            if (lastSelected != null)
                lastSelected.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
            if (focus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                focus = false;
            }
            lastSelected = m;
            lastSelected.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.bluemarker));
            information.setVisibility(View.VISIBLE);
            information.setText(lastSelected.getTitle());
            select = true;
            return true;
        }
        else {
            m.showInfoWindow();
        }
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
        LatLng a = arg0.target;
        double lat = a.latitude;
        double lng = a.longitude;
        double zoom = arg0.zoom;

        if(lng < 4.858178) mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, 4.858180)));
        else if(lng > 4.890499) mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, 4.890497)));

        if(lat < 45.776677) mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.776679, lng)));
        else if(lat > 45.787019) mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.787017, lng)));

        if(zoom < 13) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 13.1f));
    }

    @Override
    public void onMapClick(LatLng point) {
        if (!focus && !select){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            if (hidden) {
                /*
                toolbar.setVisibility(View.VISIBLE);
                facebook.setVisibility(View.VISIBLE);
                localize.setVisibility(View.VISIBLE);
                hidden = false;
                */
            } else {
                /*
                facebook.setVisibility(View.GONE);
                localize.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                */
                filters.setVisibility(View.GONE);
                hidden = true;
            }
        }
        if(select) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            if(lastSelected!=null) lastSelected.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
            information.setVisibility(View.GONE);
            select = false;
        }
        if(focus){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            focus = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void onFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends"));
        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                //new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //JSONObject jsonResponse = response.getJSONObject();
                        String stringResponse = response.getRawResponse();
                        information.setText(stringResponse);
                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }
}

class MyLocationListener implements LocationListener {

    LatLng position;

    @Override
    public void onLocationChanged(Location loc) {
        this.position = new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public LatLng getPosition(){
        return this.position;
    }
}

class WaitForLocalisation extends AsyncTask<WaitForLocalisationParams, Void, Void> {
    GoogleMap mMap;
    MyLocationListener locationListener;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    @Override
    protected Void doInBackground(WaitForLocalisationParams... params) {
        mMap = params[0].mMap;
        LocationManager service = params[0].service;
        locationListener = params[0].locationListener;
        try {
            service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            while(locationListener.position == null){}
        } catch (SecurityException e) { }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationListener.position, 17.0f));
        mMap.addMarker(new MarkerOptions().position(locationListener.position).title("Vous êtes ici :)"));
    }
}

class WaitForLocalisationParams {
    GoogleMap mMap;
    LocationManager service;
    MyLocationListener locationListener;
    MapsActivity mapsActivity;

    public WaitForLocalisationParams(GoogleMap mMap, LocationManager service, MyLocationListener locationListener){
        this.mMap = mMap;
        this.locationListener = locationListener;
        this.service = service;
    }
}
