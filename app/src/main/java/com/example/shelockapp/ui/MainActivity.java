package com.example.shelockapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shelockapp.R;
import com.example.shelockapp.adapter.PersonAdapter;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Person;
import com.example.shelockapp.model.SpaceItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {



    @Bind(R.id.toolbar_main)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    SearchView searchView;
    PersonAdapter personAdapter;
    ArrayList<Person> listPerson = new ArrayList<Person>();
    DatabaseHelper helper = new DatabaseHelper(this);
//    int positionClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        helper.loadInfoData(listPerson);

        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.sherlock);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        personAdapter = new PersonAdapter(listPerson, this);
        SpaceItem spaceItem = new SpaceItem(Constant.SPACE);
        recyclerView.addItemDecoration(spaceItem);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(personAdapter);

        personAdapter.setCallback(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
//                positionClick = position;
                sendIntentInfo(listPerson.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                helper.deleteInfo(listPerson.get(position).getId());
                helper.loadInfoData(listPerson);
//                listPerson.remove(position);
                personAdapter.notifyDataSetChanged();
//                personAdapter.notifyItemRemoved(position);
            }
        });


    }


    private void sendIntentInfo(Person person) {
        Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
        intent.putExtra(Constant.PERSON, person);
        startActivityForResult(intent, Constant.REQUEST_INFO);
    }

    private void sendIntentAdd() {
        Intent intent = new Intent(getApplicationContext(), AddPersonActivity.class);
        intent.putExtra(Constant.LISTDATA, listPerson);
        startActivityForResult(intent, Constant.REQUEST_ADD);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Constant.REQUEST_INFO:
                if (resultCode == RESULT_OK) {
//                    Person person = (Person)data.getSerializableExtra(Constant.PERSON);
//                    listPerson.set(positionClick, person);
                    helper.loadInfoData(listPerson);
                    personAdapter.notifyDataSetChanged();

                    break;
                }


            case Constant.REQUEST_ADD:

                    helper.loadInfoData(listPerson);
//                if (resultCode == RESULT_OK) {
//                    listPerson.add((Person) data.getSerializableExtra(Constant.PERSON));
                    personAdapter.notifyDataSetChanged();
                    break;
//                }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(listener);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_upload:
                Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add:
                sendIntentAdd();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            newText = newText.toLowerCase();

            final ArrayList<Person> filteredList = new ArrayList<Person>();

            for (int i = 0; i < listPerson.size(); i++) {

                final String text = listPerson.get(i).getName().toLowerCase();
                if (text.contains(newText)) {

                    filteredList.add(listPerson.get(i));
                }
            }

            personAdapter = new PersonAdapter(filteredList, MainActivity.this);
            personAdapter.setCallback(new OnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    sendIntentInfo(filteredList.get(position));
                }

                @Override
                public void onLongClick(View view, int position) {
                    listPerson.remove(filteredList.get(position));
                    filteredList.remove(position);
//                        personAdapter.notifyDataSetChanged();
                }

            });
            recyclerView.setAdapter(personAdapter);
            personAdapter.notifyDataSetChanged();

            return true;
        }
    };
}
