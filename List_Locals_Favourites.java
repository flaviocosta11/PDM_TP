package pt.ipp.estg.pdm_tp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Tables.Category;
import pt.ipp.estg.pdm_tp.Tables.PointInterest;

import static android.support.constraint.Constraints.TAG;

public class List_Locals_Favourites extends ListFragment {


    private ArrayList<Category> mListCategories;
    private ArrayList<MultiSelectModel> ListCategorias;
    private ArrayList<Integer> listIdsCategoriasSelec;
    private MyDb dbHelper;
    private ArrayList<PointInterest> mListPoints = new ArrayList<>();
    private MyListAdapter adapter;
    private Fragment mFragment;
    BoomMenuButton bmb;


    public List_Locals_Favourites() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDb(getContext());

        mListCategories = new ArrayList<>();
        mListPoints = getPoints();

        adapter = new MyListAdapter(getContext(), mListPoints, getFragmentManager());

        //adapter = new ListAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mListPessoas);

        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        listIdsCategoriasSelec = new ArrayList<>();
        ListCategorias = new ArrayList<>();
        mListCategories = getCategorias();
        ListCategorias = preencherlistacategorias();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)

    {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_list__locals__favourites, parent, false);

        bmb = v.findViewById(R.id.bmb);

        String[] filtros = {"Categorias", "Distancia", "Rating"};
        int[] myImageList = new int[]{R.drawable.categories, R.drawable.distance, R.drawable.rating};
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder();
            builder.normalImageRes(myImageList[i]);
            builder.normalText(filtros[i]);
            builder.listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    switch (index) {

                        case 0: {
                            OpenListCat();
                            break;
                        }
                        case 1: {
                            String[] rating = {"<= 5km", "<= 10km", "<= 20km ", "<= 30km", "> 30km"};
                            new LovelyChoiceDialog(getContext(), R.style.CheckBoxTintTheme)
                                    .setTopColorRes(R.color.darkRed)
                                    .setTitle("Distância:")
                                    .setIcon(R.drawable.baseline_add_location_black_18dp)
                                    .setItemsMultiChoice(rating, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                                        @SuppressLint("StringFormatInvalid")
                                        @Override
                                        public void onItemsSelected(List<Integer> positions, List<String> items) {
                                            Toast.makeText(getContext(),
                                                    getString(R.string.you_choice, TextUtils.join("\n", items)),
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    })
                                    .setConfirmButtonText(R.string.confirm)
                                    .show();
                            break;
                        }
                        case 2: {
                            String[] rating = {"1", "2", "3", "4", "5"};
                            new LovelyChoiceDialog(getContext(), R.style.CheckBoxTintTheme)
                                    .setTopColorRes(R.color.darkRed)
                                    .setTitle("Rating:")
                                    .setIcon(R.drawable.baseline_add_location_black_18dp)
                                    .setItemsMultiChoice(rating, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                                        @SuppressLint("StringFormatInvalid")
                                        @Override
                                        public void onItemsSelected(List<Integer> positions, List<String> items) {
                                            Toast.makeText(getContext(),
                                                    getString(R.string.you_choice, TextUtils.join("\n", items)),
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    })
                                    .setConfirmButtonText(R.string.confirm)
                                    .show();
                            break;

                        }

                    }
                    // When the boom-button corresponding this builder is clicked.
                    Toast.makeText(getContext(), "Clicked " + index, Toast.LENGTH_SHORT).show();
                }
            });

            bmb.addBuilder(builder);
        }

        return v;

    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Log.d("idres", String.valueOf(mListPoints.get(position).getId()));

        Bundle args = new Bundle();
        args.putInt("id", mListPoints.get(position).getId());

        Fragment newFragment = new DetailPlace();
        newFragment.setArguments(args);//passar id para o fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();


    }


    private ArrayList<PointInterest> getPoints() {
        // Limpar o ArrayList
        mListPoints.clear();
        mListPoints = dbHelper.getPointsFavourites();

        if (adapter != null) adapter.notifyDataSetChanged();

        return mListPoints;
    }


    private ArrayList<Category> getCategorias() {
        // Limpar o ArrayList
        mListCategories.clear();

        mListCategories = dbHelper.getCategorias();

        if (adapter != null) adapter.notifyDataSetChanged();

        return mListCategories;
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

        Log.d("pontos", String.valueOf(mListCategories.size()));
        for (int i = 0; i < mListCategories.size(); i++) {
            ListCategorias.add(new MultiSelectModel(mListCategories.get(i).getId_categoria(), mListCategories.get(i).getNomecategoria()));
        }
        return ListCategorias;
    }
}