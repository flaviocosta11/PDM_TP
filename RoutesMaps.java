package pt.ipp.estg.pdm_tp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialogBuilder;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Tables.Route;

import static android.support.constraint.Constraints.TAG;


public class RoutesMaps extends Fragment implements OnMapReadyCallback, View.OnClickListener {


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
    FloatingActionButton fab;
    FloatingActionButton fab2;
    int first=0;

    public RoutesMaps() {
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_routes_maps, container, false);

        fab = mView.findViewById(R.id.fab);
        fab2 = mView.findViewById(R.id.fab2);
        fab.setOnClickListener(this);
        fab2.setOnClickListener(this);

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
            for(int e=0;e<listapontos.size();e++){
                listaLocalizacoes.add(new LatLng(Double.parseDouble(listapontos.get(e).getLatitude()),  Double.parseDouble(listapontos.get(e).getLongitude())));
            }
            drawRoutes(listaLocalizacoes);

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


}
