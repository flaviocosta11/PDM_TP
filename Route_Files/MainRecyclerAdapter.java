package pt.ipp.estg.pdm_tp.Route_Files;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;

import pt.ipp.estg.pdm_tp.DetailPlace;
import pt.ipp.estg.pdm_tp.DetailRout;
import pt.ipp.estg.pdm_tp.DetailRoute;
import pt.ipp.estg.pdm_tp.MyDb;
import pt.ipp.estg.pdm_tp.R;
import pt.ipp.estg.pdm_tp.Tables.Route;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Route> mListRoutes;
    FragmentManager fragmentmanager;
    private Context mContext;
    private ItemTouchHelperExtension mItemTouchHelperExtension;
    private MyDb dbHelper;
    View view;


    public MainRecyclerAdapter(Context context, ArrayList<Route> list, FragmentManager fragmentmanager) {

        this.mContext = context;
        this.mListRoutes = list;
        this.fragmentmanager = fragmentmanager;

        dbHelper = new MyDb(context);
    }


    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = getLayoutInflater().inflate(R.layout.list_route_item, parent, false);


        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(mListRoutes.get(position));

        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Posição: #" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "ID: " + mListRoutes.get(position).getId(), Toast.LENGTH_SHORT).show();

                /*Bundle args = new Bundle();
                args.putInt("id", mListRoutes.get(position).getId());
                Fragment newFragment = new DetailRoute();
                newFragment.setArguments(args);//passar id para o fragment
                FragmentTransaction transaction = fragmentmanager.beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();*/

                Intent i=new Intent(mContext, DetailRout.class);
                i.putExtra("id", mListRoutes.get(position).getId()+"");
                mContext.startActivity(i);
            }
        });

        ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;

        viewHolder.mActionViewDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //passo o id da rota e a posicao
                        doDelete(mListRoutes.get(position).getId(), holder.getAdapterPosition());

                    }
                }

        );

    }

    private void doDelete(int id, int adapterPosition) {
        Log.d("idddd", String.valueOf(id));
        //mListRoutes.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }


    @Override
    public int getItemCount() {
        return mListRoutes.size();
    }

    class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        TextView mTextTitle;
        TextView mTextDesc;
        TextView mTextIndex;
        View mViewContent;
        View mActionContainer;

        public ItemBaseViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.titulo);
            mTextDesc = (TextView) itemView.findViewById(R.id.descricao);
            mTextIndex = (TextView) itemView.findViewById(R.id.id);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(Route route) {
            mTextTitle.setText(route.getTitulo());
            mTextDesc.setText(route.getDescricao());

        }
    }


    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View mActionViewDelete;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            //mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }


    public void move(int from, int to) {
        //Route prev = mListRoutes.remove(from);
        /*mListRoutes.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);*/
    }


}