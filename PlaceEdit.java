package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.Category;
import pt.ipp.estg.pdm_tp.Tables.Photo;
import pt.ipp.estg.pdm_tp.Tables.PointInterest;

import static android.support.constraint.Constraints.TAG;
import static java.lang.String.valueOf;

public class PlaceEdit extends Fragment implements View.OnClickListener {

    private int idPoint;
    private MyDb dbHelper;
    Context context;
    private PointInterest point;
    private ArrayList<Photo> fotos;
    private ArrayList<Category> categorias;
    private ArrayList<String> listfotos;

    EditText Textlatitude;
    EditText Textlongitude;
    EditText Titulo;
    EditText Descricao;
    EditText Rating;
    EditText Cidade;
    private ArrayList<Category> mListCategories;
    private Button btnSelectCategorys;
    private Button btnInserft;
    private String linksfotos;
    private String stringCat;
    private ArrayList<MultiSelectModel> ListCategorias;
    private ArrayList<Integer> listIdsCategoriasSelec;

    public PlaceEdit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listfotos= new ArrayList<>();
        context = getContext();
        idPoint = getArguments().getInt("id");

        fotos = new ArrayList<>();
        categorias = new ArrayList<>();
        ListCategorias = new ArrayList<>();
        mListCategories = new ArrayList<>();
        listIdsCategoriasSelec = new ArrayList<>();


        Log.d("id_ponto", valueOf(idPoint));

        dbHelper = new MyDb(getContext());
        point = new PointInterest();
        point = getPoint(idPoint);
        fotos = getFotos(idPoint);
        mListCategories = getAllCategorias();
        ListCategorias = preencherlistacategorias();
        categorias = getCategorias(idPoint);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_edit, container, false);

        Textlatitude = v.findViewById(R.id.textlatitude);
        Textlongitude = v.findViewById(R.id.textlongitude);
        Titulo = v.findViewById(R.id.titulo);
        Descricao = v.findViewById(R.id.descricao);
        Rating = v.findViewById(R.id.rating);
        Cidade = v.findViewById(R.id.cidade);
        btnSelectCategorys = v.findViewById(R.id.btnSelectCategorys);
        btnSelectCategorys.setOnClickListener(this);
        btnInserft = v.findViewById(R.id.btninserft);
        btnInserft.setOnClickListener(this);

        Textlatitude.setText(point.getLatitude());
        Textlongitude.setText(point.getLongitude());
        Titulo.setText(point.getTitulo());
        Descricao.setText(point.getDescricao());
        Rating.setText(valueOf(point.getRating()));
        Cidade.setText(point.getCidade());

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
    private ArrayList<Category> getAllCategorias() {
        // Limpar o ArrayList
        mListCategories.clear();

        mListCategories = dbHelper.getCategorias();

        return mListCategories;
    }

    private ArrayList<Photo> getFotos(int id) {
        // Limpar o ArrayList
        fotos.clear();
        fotos = dbHelper.getFotosPonto(id);
        return fotos;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSelectCategorys: {
                new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.indigo)
                        .setButtonsColorRes(R.color.darkDeepOrange)
                        .setIcon(R.drawable.baseline_add_photo_alternate_black_18dp)
                        .setTitle(R.string.info_title_categ)
                        .setMessage(getStringCategorias())
                        .setPositiveButton(R.string.inserirnvct, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "Inserir Novas Categorias", Toast.LENGTH_SHORT).show();
                                OpenListCat();
                            }
                        })
                        .setNegativeButton(R.string.deixar, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "Deixar as mesmas categorias", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                break;

            }
            case R.id.btninserft: {
                new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.indigo)
                        .setButtonsColorRes(R.color.darkDeepOrange)
                        .setIcon(R.drawable.baseline_add_photo_alternate_black_18dp)
                        .setTitle(R.string.info_title_edit_place)
                        .setMessage(getLinksFotos())
                        .setPositiveButton(R.string.editar, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "editar clicked", Toast.LENGTH_SHORT).show();
                                inpEdtFotos();
                            }
                        })
                        .setNegativeButton(R.string.insernvsfts, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "inserir novas ftos", Toast.LENGTH_SHORT).show();
                                inserirnovasfotos();
                            }
                        })
                        .show();

                break;
            }

        }

    }

    public void inpEdtFotos(){
        for(int i=0;i<fotos.size();i++){
            new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                    .setTopColorRes(R.color.darkDeepOrange)
                    .setTitle(R.string.edtitarlink)
                    .setMessage("link:")
                    .setIcon(R.drawable.ic_assignment_white_36dp)
                    .setInitialInput(fotos.get(i).getPhoto())
                    .setInputFilter(R.string.text_input_error_message_ft, new LovelyTextInputDialog.TextFilter() {
                        @Override
                        public boolean check(String text) {
                            //verificar se é um link
                            return text != null &&  URLUtil.isValidUrl(text);
                            //return text.matches("\\w+");
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

    }

    public String getLinksFotos(){
        linksfotos="";
        for(int i=0;i<fotos.size();i++){
            linksfotos += fotos.get(i).getPhoto();
            if(i == fotos.size()-1){

            }else{
                linksfotos += ", \n";
            }
        }
        return linksfotos;
    }
    public String getStringCategorias(){
        stringCat="";
        for(int i=0;i<categorias.size();i++){
            stringCat += categorias.get(i).getNomecategoria();
            if(i == categorias.size()-1){

            }else{
                stringCat += ", \n";
            }
        }
        return stringCat;
    }

    public void inserirnovasfotos(){
        new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.darkDeepOrange)
                .setTitle(R.string.text_input_title)
                .setMessage(R.string.text_input_message)
                .setIcon(R.drawable.baseline_add_photo_alternate_black_18dp)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        //verificar se é numero
                        return text != null && text.matches("[-+]?\\d*\\.?\\d+");
                        //return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        int in2 = new Integer(text);
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        insertfts(in2);
                    }
                })
                .show();
    }
    public void insertfts(int num){
        for (int i=0;i<num;i++){
            final int n = i;
            new LovelyTextInputDialog(context, R.style.EditTextTintTheme)
                    .setTopColorRes(R.color.darkDeepOrange)
                    .setTitle(R.string.text_input_title_ft)
                    .setMessage(R.string.text_input_message_ft)
                    .setIcon(R.drawable.baseline_add_photo_alternate_black_18dp)
                    .setInputFilter(R.string.text_input_error_message_ft, new LovelyTextInputDialog.TextFilter() {
                        @Override
                        public boolean check(String text) {
                            //verificar se é um link
                            return text != null &&  URLUtil.isValidUrl(text);
                            //return text.matches("\\w+");
                        }
                    })
                    .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                        @Override
                        public void onTextInputConfirmed(String text) {

                            listfotos.add(text);
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

        }
    }

    public void OpenListCat() {
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title(getResources().getString(R.string.tituloSLCatg)) //setting title for dialog
                .titleSize(25)
                .positiveText("Concluir")
                .negativeText("Cancelar")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(ListCategorias.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(listIdsCategoriasSelec) //List of ids that you need to be selected
                .multiSelectList(ListCategorias) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(getContext(), "Id Selecionado : " + selectedIds.get(i) + "\n" +
                                    "Categoria Selecionada : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Nenhuma Categoria Adicionada");
                    }


                });

        multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");
    }
    private ArrayList<MultiSelectModel> preencherlistacategorias() {

        for (int i = 0; i < mListCategories.size(); i++) {
            ListCategorias.add(new com.abdeveloper.library.MultiSelectModel(mListCategories.get(i).getId_categoria(), mListCategories.get(i).getNomecategoria()));
        }
        return ListCategorias;
    }
}
