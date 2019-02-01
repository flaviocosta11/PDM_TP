package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Tables.Route;
import pt.ipp.estg.pdm_tp.directionhelpers.FetchURL;
import pt.ipp.estg.pdm_tp.directionhelpers.TaskLoadedCallback;

import static java.lang.String.valueOf;

public class DetailRout extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener {

    private GoogleMap mMap;
    private int idRoute;
    private MyDb dbHelper;
    Context context;
    private ArrayList<PointInterest> pontos;
    private Route route;
    private View view;
    LinearLayout linearLayout;
    TextView Titulo;
    TextView Descricao;
    private Polyline currentPolyline;
    MapView mapView;
    ArrayList markerPoints= new ArrayList();
    private Button verDetalhes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        idRoute = Integer.parseInt(intent.getStringExtra("id"));

        verDetalhes = findViewById(R.id.btndetalhsrt);
        verDetalhes.setOnClickListener(this);

        pontos = new ArrayList<>();
        context = DetailRout.this;
        route = new Route();

        Log.d("id_rota", valueOf(idRoute));

        dbHelper = new MyDb(context);

        pontos = getPontosRota(idRoute);


        linearLayout = new LinearLayout(context);


        iniciarLayout();

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
        iniciaPontosMapa();

        List<LatLng> listaLocalizacoes =  updateView();
        zoomRoute(mMap,listaLocalizacoes);
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

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

    private List<LatLng> updateView() {
        List<LatLng> listaLocalizacoes =  new ArrayList();
        for(int i=0;i<pontos.size();i++){
            listaLocalizacoes.add(new LatLng(Double.parseDouble(pontos.get(i).getLatitude()),  Double.parseDouble(pontos.get(i).getLongitude())));
        }

        new FetchURL(DetailRout.this).execute(getUrl(listaLocalizacoes, "driving"), "driving");
        //drawRoutes(listaLocalizacoes);
        return listaLocalizacoes;
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






    private String getUrl(List<LatLng> locations, String directionMode) {

        String str_origin = null;
        String str_dest = null;

        int i =0;
        StringBuilder waypoints =  new StringBuilder("&waypoints=");
        for ( LatLng latLng : locations ){
            if(i==0){
                str_origin = "origin=" + latLng.latitude + "," + latLng.longitude;
            }else if(i == locations.size() -1){
                str_dest = "destination=" + latLng.latitude + "," + latLng.longitude;
            }else{
                waypoints.append("via:" + latLng.latitude + "," + latLng.longitude);
                Log.d("waypoints",waypoints.toString());
                //String str_medium = "&waypoints=via:" + med.latitude + "," + med.longitude;
            }
            i++;
        }

        String mode = "mode=" + directionMode;
        String output = "json";
        String parameters = str_origin + "&" + str_dest + waypoints +  "&" + mode;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btndetalhsrt: {
                Intent i= new Intent(context, MainActivity.class);
                i.putExtra("id", idRoute+"");
                context.startActivity(i);
                break;
            }
        }
    }


    public void iniciaPontosMapa(){
        for (int i=0;i<pontos.size();i++){
            LatLng latlon = new LatLng(Double.parseDouble(pontos.get(i).getLatitude()), Double.parseDouble(pontos.get(i).getLongitude()));
            addMarkertomap(latlon,pontos.get(i).getTitulo(),pontos.get(i).getDescricao());
        }
    }
    public void addMarkertomap(LatLng point,String title,String desc){

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(point.latitude, point.longitude)).title(title).snippet(desc);

        mMap.addMarker(marker);

    }
}
