package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.fuzzproductions.ratingbar.RatingBar;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.Category;
import pt.ipp.estg.pdm_tp.Tables.Photo;
import pt.ipp.estg.pdm_tp.Tables.PointInterest;

import static java.lang.String.valueOf;


public class DetailPlace extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {


    private SliderLayout mDemoSlider;
    RequestOptions requestOptions = new RequestOptions();
    private int idPoint;
    private MyDb dbHelper;
    Context context;
    private PointInterest point;
    private ArrayList<Photo> fotos;
    private ArrayList<Category> categorias;
    View v;

    ImageView foto;
    TextView titulo;
    TextView descricao;
    TextView cidade;
    TextView categoria;
    TextView latitude;
    TextView longitude;

    public DetailPlace() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idPoint = getArguments().getInt("id");
        fotos = new ArrayList<>();
        categorias = new ArrayList<>();

        Log.d("id_ponto", valueOf(idPoint));

        dbHelper = new MyDb(getContext());
        point = new PointInterest();
        point = getPoint(idPoint);
        fotos = getFotos(idPoint);
        categorias = getCategorias(idPoint);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail_place, container, false);

        mDemoSlider = v.findViewById(R.id.slider);
        requestOptions.centerCrop();
        slider();

        titulo = v.findViewById(R.id.titulo);
        descricao = v.findViewById(R.id.descricao);
        cidade = v.findViewById(R.id.cidade);
        //rating = v.findViewById(R.id.rating);
        RatingBar rating = v.findViewById(R.id.rating_bar);
        categoria = v.findViewById(R.id.categorias);
        latitude = v.findViewById(R.id.latitude);
        longitude = v.findViewById(R.id.longitude);


        rating.setRating(point.getRating());
        titulo.setText(point.getTitulo());
        descricao.setText(point.getDescricao());
        cidade.setText(point.getCidade());

        String Categorias = "";
        for (int i = 0; i < categorias.size(); i++) {
            Categorias += categorias.get(i).getNomecategoria();
            if (i == categorias.size() - 1) {

            } else {
                Categorias += ", ";
            }
        }

        categoria.setText(Categorias);
        Log.d("categoria", valueOf(Categorias));
        latitude.setText(point.getLatitude());
        longitude.setText(point.getLongitude());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private PointInterest getPoint(int id) {

        point = dbHelper.getPointInterest(id);

        return point;
    }

    private ArrayList<Category> getCategorias(int id) {
        // Limpar o ArrayList
        //categorias.clear();
        categorias = dbHelper.getCategoriasPonto(id);
        Log.d("categorias", valueOf(categorias));
        return categorias;
    }

    private ArrayList<Photo> getFotos(int id) {
        // Limpar o ArrayList
        fotos.clear();
        fotos = dbHelper.getFotosPonto(id);
        return fotos;
    }


    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        //Toast.makeText(context, baseSliderView.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        String link = (String) baseSliderView.getBundle().get("extra");
        ImagePopup imagePopup = new ImagePopup(context);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional
        imagePopup.initiatePopupWithPicasso(link);
        imagePopup.viewPopup();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void slider() {
        for (int i = 0; i < fotos.size(); i++) {
            TextSliderView sliderView = new TextSliderView(context);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            BaseSliderView baseSliderView = sliderView
                    .image(fotos.get(i).getPhoto())
                    // .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);


            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", fotos.get(i).getPhoto());
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(com.glide.slider.library.SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(com.glide.slider.library.SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

}
