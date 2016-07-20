package com.example.shelockapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shelockapp.R;
import com.example.shelockapp.adapter.MovementAdapter;
import com.example.shelockapp.adapter.ViewPagerAdapter;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Movement;
import com.example.shelockapp.model.Person;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/14/2016.
 */
public class InformationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_info)
    Toolbar toolbarInfo;
    @Bind(R.id.tabs)
    TabLayout layoutTab;
    @Bind(R.id.viewpager)
    ViewPager vpInfo;
    Person person;
    DatabaseHelper helper = new DatabaseHelper(this);
    ArrayList<Movement> lstMovement = new ArrayList<>();
    MovementAdapter movementAdapter;
    InformationFragment informationFragment;
    MovementFragment movementFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra(Constant.PERSON);

        helper.loadMovementData(lstMovement, person.getId());
        movementAdapter = new MovementAdapter(lstMovement, this);

        informationFragment = new InformationFragment(person);
        movementFragment = new MovementFragment(person, movementAdapter, lstMovement);

        setupViewPager(vpInfo);
        layoutTab.setupWithViewPager(vpInfo);

        informationFragment.setCallback(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
//                intent.putExtra(Constant.PERSON, person);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        movementFragment.setCallback(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                getSupportFragmentManager().beginTransaction().detach(movementFragment).attach(movementFragment).commitAllowingStateLoss();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    private void setupViewPager(ViewPager vpInfo) {
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpAdapter.addFragment(movementFragment, Constant.MOVEMENT_LABEL);
        vpAdapter.addFragment(informationFragment, Constant.INFOMATION_LABEL);
        vpInfo.setAdapter(vpAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), AddMovementActivity.class);
            intent.putExtra(Constant.PERSON, person);
            startActivityForResult(intent, Constant.ADD_MOVEMENT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Constant.ADD_MOVEMENT:
                if (resultCode == RESULT_OK) {
                    helper.loadMovementData(lstMovement, person.getId());
                    movementAdapter.notifyDataSetChanged();
                    getSupportFragmentManager().beginTransaction().detach(movementFragment).attach(movementFragment).commitAllowingStateLoss();

                    break;
                }


        }

    }
}
