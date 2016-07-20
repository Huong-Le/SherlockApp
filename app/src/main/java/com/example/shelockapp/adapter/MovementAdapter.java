package com.example.shelockapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shelockapp.R;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.model.Movement;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/16/2016.
 */
public class MovementAdapter extends RecyclerView.Adapter<MovementAdapter.MovementHolder>{

    private ArrayList<Movement> lstMovement;

    Context context;
    OnClickListener callback;
    public MovementAdapter(ArrayList<Movement> lstMovement, Context context) {
        this.context = context;
        this.lstMovement = lstMovement;
    }

    public void setCallback(OnClickListener callback){
        this.callback = callback;
    }

    @Override
    public MovementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_movement, null);
        return new MovementHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovementHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, holder.getLayoutPosition());

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onLongClick(v, holder.getLayoutPosition());
                return true;
            }
        });
        holder.txtLocation.setText(lstMovement.get(position).getLocation());
        holder.txtNote.setText(lstMovement.get(position).getNote());
        holder.txtDate.setText(lstMovement.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return lstMovement.size();
    }


    public class MovementHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_note) TextView txtNote;
        @Bind(R.id.txt_location) TextView txtLocation;
        @Bind(R.id.txt_date) TextView txtDate;
        View itemView;

        public MovementHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            ButterKnife.bind(this, itemView);
        }



    }



}
