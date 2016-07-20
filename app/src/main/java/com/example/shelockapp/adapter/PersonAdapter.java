package com.example.shelockapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shelockapp.R;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Person;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/13/2016.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{

    private ArrayList<Person> lstInfo;

    Context context;
    DatabaseHelper helper;
    OnClickListener callback;


    public PersonAdapter(ArrayList<Person> list, Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        lstInfo = list;
    }

    public void setCallback(OnClickListener callback) {
        this.callback = callback;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_person, null);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(v, holder.getLayoutPosition());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (callback != null) {
                    callback.onLongClick(v, holder.getLayoutPosition());
                }
                return true;
            }
        });
        holder.txtName.setText(lstInfo.get(position).getName());
        Glide.with(context).load(Uri.parse(lstInfo.get(position).getCover())).override(200,200).centerCrop().into(holder.imgCover);
        holder.txtMovement.setText("Movements: " + String.valueOf(helper.countMovement(lstInfo.get(position).getId())));
    }


    @Override
    public int getItemCount() {
        return lstInfo.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgcover)
        ImageView imgCover;
        @Bind(R.id.txtName) TextView txtName;
        @Bind(R.id.txtMovement) TextView txtMovement;

        View itemView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }



    }



}
