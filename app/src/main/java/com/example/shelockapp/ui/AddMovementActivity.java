package com.example.shelockapp.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shelockapp.R;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.gps.AlertDialogManager;
import com.example.shelockapp.gps.ConnectionDetector;
import com.example.shelockapp.gps.GPSTracker;
import com.example.shelockapp.gps.GooglePlaces;
import com.example.shelockapp.gps.Place;
import com.example.shelockapp.gps.PlacesList;
import com.example.shelockapp.model.Movement;
import com.example.shelockapp.model.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/18/2016.
 */
public class AddMovementActivity extends AppCompatActivity{

    @Bind(R.id.tb_add_movement)
    Toolbar tbAddMovement;
    @Bind(R.id.btn_location)
    ImageButton btnLocation;
    @Bind(R.id.et_location)
    EditText etLocation;
    @Bind(R.id.et_note) EditText etNote;
    @Bind(R.id.btn_add_movement) Button btnAddMovement;
    @Bind(R.id.btn_return_movement) Button btnReturnMovement;
    Person person;
    DatabaseHelper helper = new DatabaseHelper(this);
    Boolean isInternetPresent = false;

    ConnectionDetector cd;

    AlertDialogManager alert = new AlertDialogManager();

    GooglePlaces googlePlaces;

    PlacesList nearPlaces;

    GPSTracker gps;

    ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();


    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movement);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        person = (Person)intent.getSerializableExtra(Constant.PERSON);
        setSupportActionBar(tbAddMovement);
        getSupportActionBar().setLogo(R.drawable.sherlock);
        getSupportActionBar().setTitle("Add Movement");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        btnAddMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erNote, erLocation;
                if( (erNote = etNote.getText().toString().equals("") ) | (erLocation = etLocation.getText().toString().equals("")) ){
                    etNote.setError(erNote?"Required value":null);
                    etLocation.setError(erLocation?"Required value":null);
                    (erLocation ? etLocation :etNote).requestFocus();

                }
                else {
                    String currentTime = new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date());
                    helper.insertMovement(new Movement(person.getId(), person.getName(), etNote.getText().toString(), etLocation.getText().toString(), currentTime), person);
                    Toast.makeText(AddMovementActivity.this, "Successfully Add", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnReturnMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = getIntent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cd = new ConnectionDetector(getApplicationContext());

                isInternetPresent = cd.isConnectingToInternet();
                if (!isInternetPresent) {
                    // Internet Connection is not present
                    alert.showAlertDialog(AddMovementActivity.this, "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }

                // creating GPS Class object
                gps = new GPSTracker(AddMovementActivity.this);

                if (gps.canGetLocation()) {
                    Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
                } else {
                    AlertDialog dialogGPS = dialogGPS();
                    dialogGPS.show();

                    return;
                }

                new LoadPlaces().execute();

            }

            class LoadPlaces extends AsyncTask<String, String, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(AddMovementActivity.this);
                    pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                protected String doInBackground(String... args) {
                    // creating Places class object
                    googlePlaces = new GooglePlaces();

                    try {

                        String types = "cafe|restaurant"; // Listing places only cafes, restaurants

                        double radius = 1000; // 1000 meters

                        nearPlaces = googlePlaces.search(gps.getLatitude(),
                                gps.getLongitude(), radius, types);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                protected void onPostExecute(String file_url) {
                    // dismiss the dialog after getting all products
                    pDialog.dismiss();
                    // updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Get json response status
                            String status = nearPlaces.status;

                            if (status.equals("OK")) {
                                if (nearPlaces.results != null) {
                                    for (Place p : nearPlaces.results) {
                                        HashMap<String, String> map = new HashMap<String, String>();

                                        map.put(KEY_REFERENCE, p.reference);

                                        map.put(KEY_NAME, p.name);

                                        map.put(KEY_VICINITY, p.vicinity);


                                        placesListItems.add(map);
                                    }
                                    ListAdapter listAdapter = new SimpleAdapter(AddMovementActivity.this, placesListItems,
                                            R.layout.item_place,
                                            new String[]{KEY_REFERENCE, KEY_NAME}, new int[]{
                                            R.id.reference, R.id.name});
                                    dialog(listAdapter);


                                }
                            } else if (status.equals("ZERO_RESULTS")) {
                                // Zero results found
                                alert.showAlertDialog(AddMovementActivity.this, "Near Places",
                                        "Sorry no places found. Try to change the types of places",
                                        false);
                            } else if (status.equals("UNKNOWN_ERROR")) {
                                alert.showAlertDialog(AddMovementActivity.this, "Places Error",
                                        "Sorry unknown error occured.",
                                        false);
                            } else if (status.equals("OVER_QUERY_LIMIT")) {
                                alert.showAlertDialog(AddMovementActivity.this, "Places Error",
                                        "Sorry query limit to google places is reached",
                                        false);
                            } else if (status.equals("REQUEST_DENIED")) {
                                alert.showAlertDialog(AddMovementActivity.this, "Places Error",
                                        "Sorry error occured. Request is denied",
                                        false);
                            } else if (status.equals("INVALID_REQUEST")) {
                                alert.showAlertDialog(AddMovementActivity.this, "Places Error",
                                        "Sorry error occured. Invalid Request",
                                        false);
                            } else {
                                alert.showAlertDialog(AddMovementActivity.this, "Places Error",
                                        "Sorry error occured.",
                                        false);
                            }
                        }
                    });

                }

            }

        });
    }





    public void dialog(final ListAdapter listAdapter){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Selecting your place: ");


        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                listAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        HashMap<String, String> item = (HashMap<String, String>) listAdapter.getItem(which);
                        etLocation.setText(item.get(KEY_NAME) + " - " + item.get(KEY_VICINITY));
                    }
                });
        builderSingle.create();
        builderSingle.show();
    }

    public AlertDialog dialogGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your GPS is disable, Please turn on GPS!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();

    }

}
