package com.example.shelockapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.shelockapp.R;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.model.Movement;
import com.example.shelockapp.model.Person;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/19/2016.
 */
public class MovementActivity extends AppCompatActivity {
    Person person;
    Movement movement;
    @Bind(R.id.txt_name_movement)
    TextView txtNameMovement;
    @Bind(R.id.txt_time_movement)
    TextView txtTimeMovement;
    @Bind(R.id.txt_note_movement)
    TextView txtNoteMovement;
    @Bind(R.id.txt_location_movement)
    TextView txtLocationMovement;
    @Bind(R.id.tbmovement)
    Toolbar tbMovement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);
        ButterKnife.bind(this);

        setSupportActionBar(tbMovement);
        getSupportActionBar().setTitle("Sherlock");
        getSupportActionBar().setLogo(R.drawable.sherlock);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        movement = (Movement) intent.getSerializableExtra(Constant.MOVEMENT);
        loadData(movement);
    }

    private void loadData(Movement movement) {
        txtNameMovement.setText(movement.getName());
        txtTimeMovement.setText(movement.getDate());
        txtNoteMovement.setText(movement.getNote());
        txtLocationMovement.setText(movement.getLocation());
    }
}
