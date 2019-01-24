package pt.ipp.estg.pdm_tp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Tables.Route;
import pt.ipp.estg.pdm_tp.Utils.ConvertUtils;


import static java.lang.String.valueOf;


public class DetailRoute extends Fragment implements OnMapReadyCallback, View.OnClickListener  {

    private int idRoute;
    private MyDb dbHelper;
    Context context;
    private ArrayList<PointInterest> pontos;
    private Route route;
    private View view;
    LinearLayout linearLayout;
    TextView Titulo;
    TextView Descricao;

    GoogleMap mGoogleMap;
    MapView mapView;
    ArrayList markerPoints= new ArrayList();

    public DetailRoute() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idRoute = getArguments().getInt("id");
        pontos = new ArrayList<>();
        context = getContext();
        route = new Route();

        Log.d("id_rota", valueOf(idRoute));

        dbHelper = new MyDb(context);

        pontos = getPontosRota(idRoute);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_route, container, false);

        linearLayout = new LinearLayout(context);
        linearLayout = view.findViewById(R.id.linearlayout);
        Titulo = view.findViewById(R.id.titulo);
        Descricao = view.findViewById(R.id.descricao);

        Titulo.setText(route.getTitulo());
        Descricao.setText(route.getDescricao());

        iniciarLayout();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private ArrayList<PointInterest> getPontosRota(int id) {
        // Limpar o ArrayList
        //categorias.clear();
        Log.d("iddddarota", String.valueOf(id));
        pontos = dbHelper.getPontosRota(id);
        route = dbHelper.getDescRota(id);

        return pontos;
    }

    public void iniciarLayout(){

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for( int i = 0; i < pontos.size(); i++ )
        {
            TextView textView = new TextView(context);
            textView.setHeight(100);
            textView.setTextSize(15);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(pontos.get(i).getTitulo() + ", " + pontos.get(i).getCidade());
            linearLayout.addView(textView);
            if(i == pontos.size()-1){

            }else{
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                params.gravity=Gravity.CENTER;
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.arrow_bellow);
                imageView.setLayoutParams(params);
                linearLayout.addView(imageView);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        List<LatLng> listaLocalizacoes =  updateView();
        zoomRoute(mGoogleMap,listaLocalizacoes);
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mGoogleMap.addMarker(options);

                }
        });


    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> listaLocalizacoes) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (googleMap == null || listaLocalizacoes == null || listaLocalizacoes.isEmpty()) return;

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLngPoint : listaLocalizacoes)
                boundsBuilder.include(latLngPoint);

            int routePadding = 100;
            LatLngBounds latLngBounds = boundsBuilder.build();

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));

        } else {}


    }


    private void drawRoutes( List<LatLng> locations )
    {
        if ( mGoogleMap == null ){
            Toast.makeText(getContext(), "erro", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if ( locations.size() < 2 ){
            return;
        }*/
        PolylineOptions options = new PolylineOptions();

        options.color(getResources().getColor(R.color.darkRed));
        options.width( 5 );
        options.visible( true );

        for ( LatLng latLng : locations ){
            options.add( new LatLng( latLng.latitude,
                    latLng.longitude ) );

        }

        mGoogleMap.addPolyline( options );

    }


    private List<LatLng> updateView() {
        List<LatLng> listaLocalizacoes =  new ArrayList();
        for(int i=0;i<pontos.size();i++){
            listaLocalizacoes.add(new LatLng(Double.parseDouble(pontos.get(i).getLatitude()),  Double.parseDouble(pontos.get(i).getLongitude())));
        }

        drawRoutes(listaLocalizacoes);
        return listaLocalizacoes;
    }



}
