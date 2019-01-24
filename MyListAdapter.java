package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.Tables.Category;
import pt.ipp.estg.pdm_tp.Tables.Photo;
import pt.ipp.estg.pdm_tp.Tables.PointInterest;
import pt.ipp.estg.pdm_tp.Utils.DownloadImageFromInternet;

public class MyListAdapter extends ArrayAdapter<PointInterest> implements View.OnClickListener {
    private final ArrayList<PointInterest> list;
    private ArrayList<Photo> fotos;
    private final Context context;
    private MyDb dbHelper;
    private ArrayList<Category> categorias;
    private Fragment mFragment;
    private int idPonto = 0;
    private FragmentManager fragmentmanager;

    private TextView TitleTextView;

    public MyListAdapter(Context context, ArrayList<PointInterest> list, FragmentManager fragmentmanager) {
        super(context, R.layout.recycle_items, list);
        this.context = context;
        this.list = list;
        this.fragmentmanager = fragmentmanager;

        dbHelper = new MyDb(getContext());
        categorias = new ArrayList<>();
        fotos = new ArrayList<>();


    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.recycle_items, null);
        }
        ImageView editPlace = v.findViewById(R.id.editPlace);
        editPlace.setOnClickListener(this);
        ImageView removePlace = v.findViewById(R.id.removePlace);
        removePlace.setOnClickListener(this);

        ImageView Coverimg = v.findViewById(R.id.coverImageView);

        getFotos(list.get(position).getId());
        idPonto = list.get(position).getId();
        //ArrayList<String> categorias = dbHelper.getCategoriasPonto(list.get(position).getId());

        Log.d("foto", fotos.get(0).getPhoto());
        new DownloadImageFromInternet((ImageView) Coverimg.findViewById(R.id.coverImageView), context)
                .execute(fotos.get(0).getPhoto());

        Coverimg.setTag(list.get(position).getRating());

        TitleTextView = v.findViewById(R.id.titleTextView);
        TitleTextView.setText(list.get(position).getTitulo());

        final ImageView LikeImageView = v.findViewById(R.id.likeImageView);
        if (list.get(position).getId_like() == 0) {
            LikeImageView.setTag(R.drawable.ic_like);
            LikeImageView.setImageResource(R.drawable.ic_like);
        } else if (list.get(position).getId_like() == 1) {
            LikeImageView.setTag(R.drawable.ic_liked);
            LikeImageView.setImageResource(R.drawable.ic_liked);
        }

        final ImageView AddPointToMap = v.findViewById(R.id.addPointToMap);
        if (list.get(position).getId_marker() == 0) {
            AddPointToMap.setTag(R.drawable.removemarker);
            AddPointToMap.setImageResource(R.drawable.removemarker);
        } else if (list.get(position).getId_marker() == 1) {
            AddPointToMap.setTag(R.drawable.addmarker);
            AddPointToMap.setImageResource(R.drawable.addmarker);
        }

        LikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = (int) LikeImageView.getTag();
                if (id == R.drawable.ic_like) {

                    LikeImageView.setTag(R.drawable.ic_liked);
                    LikeImageView.setImageResource(R.drawable.ic_liked);

                    Toast.makeText(context, list.get(position).getTitulo() + " added to favourites", Toast.LENGTH_SHORT).show();

                } else {
                    LikeImageView.setTag(R.drawable.ic_like);
                    LikeImageView.setImageResource(R.drawable.ic_like);
                    Toast.makeText(context, list.get(position).getTitulo() + " removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AddPointToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = (int) AddPointToMap.getTag();
                if (id == R.drawable.removemarker) {

                    AddPointToMap.setTag(R.drawable.addmarker);
                    AddPointToMap.setImageResource(R.drawable.addmarker);

                    Toast.makeText(context, list.get(position).getTitulo() + " added to map", Toast.LENGTH_SHORT).show();

                } else {
                    AddPointToMap.setTag(R.drawable.removemarker);
                    AddPointToMap.setImageResource(R.drawable.removemarker);
                    Toast.makeText(context, list.get(position).getTitulo() + " removed from map", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }


    private ArrayList<Category> getCategorias(int id) {
        // Limpar o ArrayList
        //categorias.clear();

        categorias = dbHelper.getCategoriasPonto(id);
        return categorias;
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

            case R.id.removePlace: {
                new BottomDialog.Builder(context)
                        .setTitle("Remover " + TitleTextView.getText() + " ?")
                        .setContent("Tem certeza que pretende remover " + TitleTextView.getText() + " ?")
                        .setNegativeText("Cancelar")
                        .setNegativeTextColorResource(R.color.colorAccent)
                        //.setNegativeTextColor(ContextCompat.getColor(this, R.color.colorAccent)
                        .setPositiveText("Remover")
                        .setPositiveBackgroundColorResource(R.color.deeppink)
                        //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                        .setPositiveTextColorResource(android.R.color.white)
                        //.setPositiveTextColor(ContextCompat.getColor(this, android.R.color.colorPrimary)
                        .onPositive(new BottomDialog.ButtonCallback() {
                            @Override
                            public void onClick(BottomDialog dialog) {

                            }

                        }).onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {

                    }
                }).show();

                break;
            }
            case R.id.editPlace: {
                Bundle args = new Bundle();
                args.putInt("id", idPonto);

                Fragment newFragment = new PlaceEdit();
                newFragment.setArguments(args);//passar id para o fragment
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

                break;
            }

        }
    }
}
