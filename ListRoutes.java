package pt.ipp.estg.pdm_tp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pdm_tp.Route_Files.DividerItemDecoration;
import pt.ipp.estg.pdm_tp.Route_Files.ItemTouchHelperCallback;
import pt.ipp.estg.pdm_tp.Route_Files.MainRecyclerAdapter;
import pt.ipp.estg.pdm_tp.Tables.Route;


public class ListRoutes extends Fragment {

    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mAdapter;
    private View v;

    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    private MyDb dbHelper;
    private ArrayList<Route> mListRoutes;


    public ListRoutes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDb(getContext());

        mListRoutes = new ArrayList<>();
        mListRoutes = getRoutes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_list_routes, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MainRecyclerAdapter(getContext(),mListRoutes,getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setItemTouchHelperExtension(mItemTouchHelper);



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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Route> getRoutes() {
        // Limpar o ArrayList
        mListRoutes.clear();
        mListRoutes = dbHelper.getRotas();

        //if (adapter != null) adapter.notifyDataSetChanged();

        return mListRoutes;
    }


}
