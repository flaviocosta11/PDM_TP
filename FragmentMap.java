package pt.ipp.estg.pdm_tp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.PointInterest;

public class FragmentMap extends Fragment implements OnMapReadyCallback, LocationListener, View.OnClickListener {


    protected GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    private GoogleMap mMap;
    MapView mMapView;
    private Context context;
    private View mView;
    private Button addPlace;
    private Place placeSearch;
    private View button;
    private LocationManager locationManager;
    private View rootView;
    private MyDb dbHelper;
    private ArrayList<PointInterest> mListPoints = new ArrayList<>();
    int first=0;
    FloatingActionButton fab2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = getActivity();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);


        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        dbHelper = new MyDb(getContext());

        mListPoints = getPoints();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_maps, container, false);

        button = mView.findViewById(R.id.btnaddplace);
        button.setVisibility(View.GONE);

        addPlace = mView.findViewById(R.id.btnaddplace);

        addPlace.setOnClickListener(this);

        fab2 = mView.findViewById(R.id.fab2);

        fab2.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        IniciaLocalizacaoAtual();

        iniciaPontosMapa();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {


                /*mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("ola")
                        .snippet("dsfdsfsfdfds")
                        .anchor(0.5f, 1));*/

                Bundle args = new Bundle();
                args.putInt("tipo", 1);
                args.putDouble("latitude", point.latitude);
                args.putDouble("longitude", point.longitude);

                Fragment newFragment = new AddLocals();
                newFragment.setArguments(args);//passar id para o fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();


            }

        });


    }



    public LatLng IniciaLocalizacaoAtual() {
        final LatLng[] zona = new LatLng[1];
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                LatLng location = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                zona[0] = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                irParalocaliza(location);
            }
        });

        return zona[0];
    }
    public void irParalocaliza(LatLng location){
        if(first==0){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,8));
            first=1;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void iniciaPontosMapa(){
        for (int i=0;i<mListPoints.size();i++){
            LatLng latlon = new LatLng(Double.parseDouble(mListPoints.get(i).getLatitude()), Double.parseDouble(mListPoints.get(i).getLongitude()));
            addMarkertomap(latlon,mListPoints.get(i).getTitulo(),mListPoints.get(i).getDescricao());
        }
    }
    public void addMarkertomap(LatLng point,String title,String desc){

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(point.latitude, point.longitude)).title(title).snippet(desc);

        mMap.addMarker(marker);

    }

    private ArrayList<PointInterest> getPoints() {
        // Limpar o ArrayList
        mListPoints.clear();
        mListPoints = dbHelper.getPoints();

        return mListPoints;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab2: {

                SimplePlacesSearchDialog searchDialog = new SimplePlacesSearchDialogBuilder(getContext())
                        .setLocationListener(new SimplePlacesSearchDialog.PlaceSelectedCallback() {
                            @Override
                            public void onPlaceSelected(@NotNull Place place) {

                                placeSearch = place;
                                /*textView.setText(place.getName());

                                latitude = place.getLatLng().latitude;
*/
                                // Creating a marker
                                MarkerOptions markerOptions = new MarkerOptions();

                                // Setting the position for the marker
                                markerOptions.position(place.getLatLng());

                                // Setting the title for the marker.
                                // This will be displayed on taping the marker
                                markerOptions.title(place.getLatLng().latitude + " : " + place.getLatLng().longitude);

                                // Clears the previously touched position
                                mMap.clear();

                                // Animating to the touched position
                                // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                                // Placing a marker on the touched position
                                mMap.addMarker(markerOptions);

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),15));
                                // Zoom in, animating the camera.
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                            }
                        }).build();

                searchDialog.show();
                button.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.btnaddplace: {
                Bundle args = new Bundle();
                args.putInt("tipo", 2);
                args.putDouble("latitude", placeSearch.getLatLng().latitude);
                args.putDouble("longitude", placeSearch.getLatLng().longitude);
                args.putString("pais",placeSearch.getLocale().getCountry());
                args.putString("morada",placeSearch.getAddress().toString());
                args.putString("titulo",placeSearch.getName().toString());
                args.putInt("rating", (int) placeSearch.getRating());

                Fragment newFragment = new AddLocals();
                newFragment.setArguments(args);//passar id para o fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

                break;
            }
        }

    }
}
