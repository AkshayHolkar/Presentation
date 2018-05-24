package com.fit.app.fitit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.util.Log.e;












public class MapActivity extends FragmentActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener   {

    private GoogleMap mMap;

    private static final String TAG = MapActivity.class.getSimpleName();

    private CameraPosition mCameraPosition;


    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    static final LatLng UTS = new LatLng(-33.883997, 151.199156);


    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);





    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
       // setSupportActionBar(toolbar);
        //find and set fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //find and set nav drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //find and set nav view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_steps) {
            Intent startIntent = new Intent(getApplicationContext(), StepsActivity.class);
            startActivity(startIntent);

        } else if (id == R.id.nav_map) {
            Intent startIntent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(startIntent);


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .position(UTS)
                .draggable(true)
                .title("University of Technology Sydney")
                .snippet("Building 11"));


        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        // see the methods below for descriptions of the method use.
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        this.init();

        mMap.setBuildingsEnabled(true);


        mMap.setOnPolylineClickListener(this);


//Polyline 2-4 are pre-set routes based on the current location of the emulator (googleplex).
//Polygons 1-2 are pre-set area based on the current location of the emulator (googleplex).
//Polyline 1 and 5-onwards are pre-set routes based on the actual current location in this case building 11 UTS.
// Polygon 3- onwards are pre-set routes based on the actual current location in this case building 11 UTS.

        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-33.884037, 151.199840),
                        new LatLng(-33.883839, 151.199829),
                        new LatLng(-33.883040, 151.199325),
                        new LatLng(-33.881453, 151.198443),

                        new LatLng(-33.881053, 151.198735),
                        new LatLng(-33.880620, 151.199088)));



        polyline1.setTag("Relaxed Run");
        stylePolyline(polyline1);




        Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(37.421973, -122.084107),
                        new LatLng(37.421988, -122.084330),
                        new LatLng(37.421904, -122.084343),
                        new LatLng(37.421793, -122.084950),

                        new LatLng(37.421734, -122.085267),
                        new LatLng(37.421827, -122.085302),
                        new LatLng(37.421869, -122.085332)


                        ));

        polyline2.setTag("Short Run");
        stylePolyline(polyline2);

        Polyline polyline3 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(37.421958, -122.083914),
                        new LatLng(37.421867, -122.083915),
                        new LatLng(37.421893, -122.083529),
                        new LatLng(37.421960, -122.083206),


                        new LatLng(37.421912, -122.083126),
                        new LatLng(37.421889, -122.083027),
                        new LatLng(37.422009, -122.083030),


                        new LatLng(37.422093, -122.082729),
                        new LatLng(37.422198, -122.082717),
                        new LatLng(37.422266, -122.082622),
                        new LatLng(37.422331, -122.082508),
                        new LatLng(37.422492, -122.082123),
                        new LatLng(37.422605, -122.081690)





                        //   new LatLng(37.422604, -122.081692)



                ));

        polyline3.setTag("Relaxed Run");
        stylePolyline(polyline3);



        Polyline polyline4 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(


                        new LatLng(37.422031, -122.083936),
                        new LatLng(37.422103, -122.083556),
                        new LatLng(37.422041, -122.083399),
                        new LatLng(37.422033, -122.083349),
                        new LatLng(37.422034, -122.083329),
                        new LatLng(37.422031, -122.083305),
                        new LatLng(37.422008, -122.083270),
                        new LatLng(37.421962, -122.083207),




                        new LatLng(37.422097, -122.082733),
                        new LatLng(37.422095, -122.082440),
                        new LatLng(37.422099, -122.082405),
                        new LatLng(37.422231, -122.082501),
                        new LatLng(37.422423, -122.082673),
                        new LatLng(37.422698, -122.082720),
                        new LatLng(37.422770, -122.082765),
                        new LatLng(37.422873, -122.082863),

                        new LatLng(37.422908, -122.082665),
                        new LatLng(37.422964, -122.082381),
                        new LatLng(37.422989, -122.081414)
                ));


        polyline4.setTag("Long Run");
        stylePolyline(polyline4);

        Polyline polyline5 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-33.884211, 151.199368),
                        new LatLng(-33.884187, 151.200146),
                        new LatLng(-33.884910, 151.200193),
                        new LatLng(-33.884913, 151.200199),
                        new LatLng(-33.884943, 151.200185),
                        new LatLng(-33.884964, 151.200157),
                        new LatLng(-33.884982, 151.200143),
                        new LatLng(-33.885041, 151.200130),
                        new LatLng(-33.885580, 151.200162),
                        new LatLng(-33.885976, 151.200190),
                        new LatLng(-33.885609, 151.199449),

                        new LatLng(-33.886004, 151.199462),
                        new LatLng(-33.886064, 151.198767),
                        new LatLng(-33.886463, 151.198792),
                        new LatLng(-33.886483, 151.200084),

                        new LatLng(-33.886352, 151.200106),
                        new LatLng(-33.886365, 151.200154),
                        new LatLng(-33.886122, 151.200181),
                        new LatLng(-33.886006, 151.200184)));

        polyline5.setTag("Short Run");
        stylePolyline(polyline5);


        Polyline polyline6 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-33.884178, 151.198623),
                        new LatLng(-33.883468, 151.198349),
                        new LatLng(-33.882021, 151.197502),
                        new LatLng(-33.880565, 151.196671),
                        new LatLng(-33.879073, 151.195839),

                        new LatLng(-33.877643, 151.194992),
                        new LatLng(-33.877839, 151.194584),
                        new LatLng(-33.879176, 151.194530),
                        new LatLng(-33.879438, 151.194541)
                ));

        polyline6.setTag("Long Run");
        stylePolyline(polyline6);


        Polyline polyline7 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-33.884207, 151.198241),
                        new LatLng(-33.884272, 151.196528),
                        new LatLng(-33.884299, 151.195100),

                        new LatLng(-33.879574, 151.193810),
                        new LatLng(-33.879241, 151.193321),
                        new LatLng(-33.878796, 151.192774),
                        new LatLng(-33.878706, 151.192913)
                ));

        polyline7.setTag("Relaxed Run");
        stylePolyline(polyline7);





        mMap.setOnPolygonClickListener(this);


        // Add polygons to indicate areas on the map.
        Polygon polygon1 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(37.424779, -122.090553),
                        new LatLng(37.424575, -122.086798),
                        new LatLng(37.423621, -122.086862),
                        new LatLng(37.423962, -122.090016)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.



        polygon1.setTag("Relaxed Area");
        // Style the polygon.
        stylePolygon(polygon1);

        Polygon polygon2 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(37.428203, -122.093104),
                        new LatLng(37.428340, -122.087023),
                        new LatLng(37.425103, -122.086916),
                        new LatLng(37.426599, -122.093267)));

        polygon2.setTag("Intense Area");
        stylePolygon(polygon2);



        Polygon polygon3 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(-33.889147, 151.202872),
                        new LatLng(-33.889422, 151.205315),
                        new LatLng(-33.888617, 151.205656),
                        new LatLng(-33.888670, 151.206251),

                        new LatLng(-33.886159, 151.206771),
                        new LatLng(-33.887957, 151.203492),
                        new LatLng(-33.888172, 151.203315),
                        new LatLng(-33.888110, 151.203176)));


        polygon3.setTag("Intense Area");
        stylePolygon(polygon3);


        Polygon polygon4 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(-33.882274, 151.207620),
                        new LatLng(-33.881805, 151.207942),
                        new LatLng(-33.880451, 151.208261),
                        new LatLng(-33.880275, 151.207257),

                        new LatLng(-33.880696, 151.207219),
                        new LatLng(-33.881066, 151.207021),
                        new LatLng(-33.881731, 151.206531)));



        polygon4.setTag("Relaxed Area");
        stylePolygon(polygon4);


        Polygon polygon5 = mMap.addPolygon(new com.google.android.gms.maps.model.PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(-33.885631, 151.199469),
                        new LatLng(-33.885585, 151.200640),
                        new LatLng(-33.885950, 151.200662),
                        new LatLng(-33.886002, 151.199466)));


        polygon5.setTag("Short Area");
        stylePolygon(polygon5);


    }




    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d (TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom( mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            e("Exception: %s", e.getMessage());
        }
    }








    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }

            }
        }
        updateLocationUI();
    }

    /*


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onPolygonClick(Polygon polygon) {

        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor(); //^ 0x00ffffff;
        polygon.setStrokeColor(color);
        //  color = polygon.getFillColor();
        //  polygon.setFillColor(color);

        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }




    private void stylePolyline(Polyline polyline) {

        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "Relaxed Run":


                polyline.setStartCap(new RoundCap());
                polyline.setEndCap(new SquareCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(COLOR_GREEN_ARGB);
                polyline.setJointType(JointType.ROUND);

                break;

            case "Long Run":
                polyline.setStartCap(new RoundCap());
                polyline.setEndCap(new SquareCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(Color.RED);
                polyline.setJointType(JointType.ROUND);


                break;

            case "Short Run":

                polyline.setStartCap(new RoundCap());
                polyline.setEndCap(new SquareCap());
                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                polyline.setColor(COLOR_ORANGE_ARGB );
                polyline.setJointType(JointType.ROUND);


                break;



        }

    }

    //adds marker with each short click and adds a polyline between each marker. This allows the user to create their own running routes.
    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        mMap.addMarker(marker);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(5);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);

    }


    //deletes routes if user has a long click
    @Override
    public void onMapLongClick(LatLng latLng) {
        // mMap.refresh();

        arrayPoints.clear();

    }

    // sets array points of coordinates of user clicks.
    private void init() {

        String coordinates[] = { "33.8523341", "151.2106085" };
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        LatLng position = new LatLng(lat, lng);
        //GooglePlayServicesUtil.isGooglePlayServicesAvailable(
        //      MapsActivity.this);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        arrayPoints = new ArrayList<>();
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "Relaxed Area":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_GREEN_ARGB;
                break;
            case "Intense Area":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = Color.RED;
                fillColor = Color.RED;
                break;


            case "Short Area":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_ORANGE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        // polygon.setFillColor(fillColor);
    }




}





