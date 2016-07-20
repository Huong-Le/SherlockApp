package com.example.shelockapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelockapp.R;
import com.example.shelockapp.adapter.MovementAdapter;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Movement;
import com.example.shelockapp.model.Person;
import com.example.shelockapp.model.SpaceItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class MovementFragment extends Fragment{
    @Bind(R.id.rv_movement)
    RecyclerView rvMovement;
    @Bind(R.id.txt_notification)
    TextView txtNotification;
    MovementAdapter movementAdapter;
    ArrayList<Movement> lstMovement;
    Person person;
    DatabaseHelper helper;
    OnClickListener callback;

    public MovementFragment(Person person, MovementAdapter movementAdapter, ArrayList<Movement> lstMovement) {
        this.person = person;
        this.movementAdapter = movementAdapter;
        this.lstMovement = lstMovement;
    }
    public void setCallback(OnClickListener callback){
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movement, container, false);
        ButterKnife.bind(this, view);
        helper = new DatabaseHelper(getActivity());
        if(lstMovement.size()!=0)
            txtNotification.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        SpaceItem spaceItem = new SpaceItem(Constant.SPACE);
        rvMovement.addItemDecoration(spaceItem);
        rvMovement.setLayoutManager(layoutManager);
        rvMovement.setAdapter(movementAdapter);
        movementAdapter.setCallback(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), MovementActivity.class);
                intent.putExtra(Constant.MOVEMENT, lstMovement.get(position));
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view,int position) {
                AlertDialog dialog = dialog(position);
                dialog.show();
            }
        });



        return view;
    }
    public AlertDialog dialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("WARNING!!!");
        builder.setMessage("Are you sure want to delete " + lstMovement.get(position).getNote() + "?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.deleteMovement(lstMovement.get(position).getNumber());
                lstMovement.remove(position);
                movementAdapter.notifyItemRemoved(position);
                Toast.makeText(getContext(), "Successfully Delete", Toast.LENGTH_SHORT).show();
//                callback.onClick(view,position);
                getActivity().getSupportFragmentManager().beginTransaction().detach(MovementFragment.this).attach(MovementFragment.this).commitAllowingStateLoss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
