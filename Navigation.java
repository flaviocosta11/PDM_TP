package pt.ipp.estg.pdm_tp;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Tables.Route;
import pt.ipp.estg.pdm_tp.directionhelpers.FetchURL;
import pt.ipp.estg.pdm_tp.directionhelpers.TaskLoadedCallback;

import static android.support.constraint.Constraints.TAG;


public class Navigation extends Fragment implements OnMapReadyCallback, View.OnClickListener, TaskLoadedCallback {


    private Fragment mFragment;
    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;
    Context context;
    private MyDb dbHelper;
    private ArrayList<PointInterest> mListPoints = new ArrayList<>();
    private ArrayList<Route> mListRoutes = new ArrayList<>();

    private ArrayList<Integer> listaIdsPontosSelec;
    private ArrayList<MultiSelectModel> list;

    Button getdirection;
    private Polyline currentPolyline;

    int first=0;

    public Navigation() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDb(getContext());

        listaIdsPontosSelec = new ArrayList<>();
        list = new ArrayList<>();
        mListPoints = getPoints();
        list = preencherlistadepontos();
        mListRoutes = getRoutes();

        new FetchURL(context).execute(getUrl());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_navigation, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        iniciarrotasMapa();
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        IniciaLocalizacaoAtual();



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab: {
                /*PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(getContext())
                        .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                            @Override
                            public void locationName(String locationName) {
                                //set textview or edittext
                            }
                        })
                        .build();
                placeSearchDialog.show();*/

                mFragment = new ListRoutes();
                // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, mFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            }
            case R.id.fab2: {
                dialogInserirRota();
                break;
            }
        }
    }


    public void dialogInserirRota() {
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title(getResources().getString(R.string.titloGerarRota)) //setting title for dialog
                .titleSize(25)
                .positiveText("Gerar Rota")
                .negativeText("Cancelar")
                .setMinSelectionLimit(2) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(list.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(listaIdsPontosSelec) //List of ids that you need to be selected
                .multiSelectList(list) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(getContext(), "Id Selecionado : " + selectedIds.get(i) + "\n" +
                                    "Ponto : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }
                        inserirTituloRota();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Rota Cancelada");
                    }


                });

        multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");
    }


    public void inserirTituloRota(){
        new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkDeepOrange)
                .setTitle("Titulo")
                .setMessage("Introduza o titulo da Rota:")
                .setIcon(R.drawable.baseline_directions_black_18dp)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        inserirDescRota(text);
                    }
                })
                .show();
    }
    public void inserirDescRota(String titulo){
        new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkDeepOrange)
                .setTitle("Descrição")
                .setMessage("Introduza a descrição da Rota:")
                .setIcon(R.drawable.baseline_directions_black_18dp)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }



    private ArrayList<MultiSelectModel> preencherlistadepontos() {

        Log.d("pontos", String.valueOf(mListPoints.size()));
        for (int i = 0; i < mListPoints.size(); i++) {
            list.add(new MultiSelectModel(mListPoints.get(i).getId(), mListPoints.get(i).getTitulo()));
        }
        return list;
    }

    private ArrayList<PointInterest> getPoints() {
        // Limpar o ArrayList
        mListPoints.clear();
        mListPoints = dbHelper.getPoints();


        return mListPoints;
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

    private ArrayList<PointInterest> getPontosRota(int id) {
        // Limpar o ArrayList
        //categorias.clear();
        ArrayList<PointInterest> pontos= new ArrayList<>();
        pontos = dbHelper.getPontosRota(id);

        return pontos;
    }
    private ArrayList<Route> getRoutes() {
        // Limpar o ArrayList
        mListRoutes.clear();
        mListRoutes = dbHelper.getRotas();

        //if (adapter != null) adapter.notifyDataSetChanged();

        return mListRoutes;
    }

    public void iniciarrotasMapa(){
        for(int i=0;i<mListRoutes.size();i++){
            int idrota = mListRoutes.get(i).getId();
            ArrayList<PointInterest> listapontos = getPontosRota(idrota);
            List<LatLng> listaLocalizacoes =  new ArrayList();

            MarkerOptions place1 = null;
            MarkerOptions place2 = null;
            LatLng pointone;
            LatLng pointtwo;
            Boolean countpointsmed = false;
            for(int e=0;e<listapontos.size();e++){
                if(e == 0){
                    pointone = new LatLng(Double.parseDouble(listapontos.get(e).getLatitude()),  Double.parseDouble(listapontos.get(e).getLongitude()));
                    place1 = new MarkerOptions().position(pointone).title(listapontos.get(e).getTitulo());
                }else if(e == listapontos.size() - 1){
                    pointtwo = new LatLng(Double.parseDouble(listapontos.get(e).getLatitude()),  Double.parseDouble(listapontos.get(e).getLongitude()));
                    place2 = new MarkerOptions().position(pointtwo).title(listapontos.get(e).getTitulo());
                }else{
                    countpointsmed = true;
                    listaLocalizacoes.add(new LatLng(Double.parseDouble(listapontos.get(e).getLatitude()),  Double.parseDouble(listapontos.get(e).getLongitude())));
                }
            }
            //drawRoutes(listaLocalizacoes);

        }
    }

    public LatLng IniciaLocalizacaoAtual() {
        final LatLng[] zona = new LatLng[1];
        final int[] first = {0};
        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

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
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,8));
            first=1;
        }

    }


    private String getUrl(/*LatLng origin, LatLng dest, String directionMode , Boolean mediumpoints,List<LatLng> locations*/) {
     /*   // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service

        String output = "json";
        String url;

        if (mediumpoints == true){
            StringBuilder waypoints =  new StringBuilder("&waypoints=");
            String points;
            for ( LatLng latLng : locations ){
                String point = latLng.latitude + "," + latLng.longitude;
                waypoints.append("via:"+point);
            }

            String parameters = str_origin + "&" + str_dest + waypoints + "&" + mode;
            url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);

        }else{
            String parameters = str_origin + "&" + str_dest + "&" + mode;
            url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        }

*/
        //https://maps.googleapis.com/maps/api/directions/json?origin=sydney,au&destination=perth,au&waypoints=via:-37.81223%2C144.96254%7Cvia:-34.92788%2C138.60008&key=
        // Output format

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=41.367131,-8.194706&destination=41.581111,-8.441614&mode=driving&key=AIzaSyA0famDnPeHEqVCMcw9b69-o6zgI1m0Ctw";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }


}
