package pt.ipp.estg.pdm_tp;

import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.google.android.gms.maps.model.LatLng;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.Category;

import static android.support.constraint.Constraints.TAG;


public class AddLocals extends Fragment implements View.OnClickListener {
    Spinner spinner;
    View child;
    private MyDb dbHelper;
    private MyListAdapter adapter;
    private Context context;
    private ArrayList<Category> mListCategories;
    private ArrayList<MultiSelectModel> ListCategorias;
    private Dialog dialog;
    private Button btnSelectCategorys;
    private Button btnInserft;
    LovelyTextInputDialog lovelyDialog;
    private ArrayList<String> listfotos;
    private ArrayList<Integer> listIdsCategoriasSelec;
    Double latitude = null;
    Double longitude = null;
    String titulooo = "";
    String desccc = "";
    int ratingggg = 0;
    int tippppp = 0;
    EditText LAT;
    EditText LON;
    EditText TITULO;
    EditText DESC;
    EditText RAT;
    EditText CIDADE;
    public AddLocals() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            tippppp = getArguments().getInt("tipo");
            if(tippppp == 1){
                latitude = getArguments().getDouble("latitude");
                longitude = getArguments().getDouble("longitude");
            }else if(tippppp == 2){
                desccc =  getArguments().getString("morada");
                titulooo = getArguments().getString("titulo");
                ratingggg = getArguments().getInt("rating");
                latitude = getArguments().getDouble("latitude");
                longitude = getArguments().getDouble("longitude");
            }

        }

        dbHelper = new MyDb(getContext());
        mListCategories = new ArrayList<>();
        listfotos= new ArrayList<>();

        listIdsCategoriasSelec = new ArrayList<>();
        ListCategorias = new ArrayList<>();
        mListCategories = getCategorias();
        ListCategorias  = preencherlistacategorias();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)

    {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_add_locals, parent, false);
        // Spinner element
        //spinner = v.findViewById(R.id.spinner);
        btnSelectCategorys = v.findViewById(R.id.btnSelectCategorys);
        btnInserft = v.findViewById(R.id.btninserft);
        btnSelectCategorys.setOnClickListener(this);
        btnInserft.setOnClickListener(this);

        LAT = v.findViewById(R.id.textlatitude);
        LON = v.findViewById(R.id.textlongitude);
        TITULO = v.findViewById(R.id.titulo);
        DESC = v.findViewById(R.id.descricao);
        RAT = v.findViewById(R.id.rating);
        CIDADE = v.findViewById(R.id.cidade);

        if(tippppp == 2){
            LAT.setText(String.valueOf(latitude));
            LON.setText(String.valueOf(longitude));
            TITULO.setText(titulooo);
            if (ratingggg >0 ){
                RAT.setText(String.valueOf(ratingggg));
            }
            DESC.setText(desccc);
        }else if(tippppp == 1){
            LAT.setText(String.valueOf(latitude));
            LON.setText(String.valueOf(longitude));
         }

        // Creating adapter for spinner
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.context, android.R.layout.simple_spinner_dropdown_item, ListCategorias);

        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //spinner.setAdapter(dataAdapter);

        return v;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private ArrayList<Category> getCategorias() {
        // Limpar o ArrayList
        mListCategories.clear();

        mListCategories = dbHelper.getCategorias();

        if(adapter!= null) adapter.notifyDataSetChanged();

        return mListCategories;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSelectCategorys: {
                OpenListCat();
                break;
            }
            case R.id.btninserft: {
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
                                InserirFotosArray(in2);
                            }
                        })
                        .show();
                break;
            }

        }

    }

    public void OpenListCat(){
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
                        Log.d(TAG,"Nenhuma Categoria Adicionada");
                    }


                });

        multiSelectDialog.show(getFragmentManager(), "multiSelectDialog");
    }

    private ArrayList<MultiSelectModel> preencherlistacategorias(){

        Log.d("pontos", String.valueOf(mListCategories.size()));
        for(int i=0;i<mListCategories.size();i++){
            ListCategorias.add(new MultiSelectModel(mListCategories.get(i).getId_categoria(),mListCategories.get(i).getNomecategoria()));
        }
        return ListCategorias;
    }

    public void InserirFotosArray(int num){

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


}
