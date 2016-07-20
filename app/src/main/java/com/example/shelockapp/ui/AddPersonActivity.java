package com.example.shelockapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.shelockapp.R;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Person;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class AddPersonActivity extends AppCompatActivity {

    @Bind(R.id.tb_add)
    Toolbar tbAdd;
    @Bind(R.id.btn_cover_info)
    ImageButton btnAddCover;
    @Bind(R.id.rg_gender)
    RadioGroup rgGender;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.et_name_info)
    EditText etName;
    @Bind(R.id.et_age)
    EditText etAge;
    @Bind(R.id.et_height)
    EditText etHeight;
    @Bind(R.id.et_hair_color)
    EditText etHairColor;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.rb_male)
    RadioButton rbMale;
    @Bind(R.id.rb_female)
    RadioButton rbFemale;
    @Bind(R.id.btn_return)
    Button btnReturn;
    Uri imageUri;

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        ButterKnife.bind(this);

        setSupportActionBar(tbAdd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.sherlock);
        getSupportActionBar().setTitle("Add Person");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        btnAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = dialog();
                dialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erName;
                boolean erAge;
                boolean erHeight;
                if ((erName = etName.getText().toString().equals("")) | (erAge = etAge.getText().toString().equals("")) | (erHeight = etHeight.getText().toString().equals(""))) {
                    etName.setError(erName ? "Required value" : null);
                    etAge.setError(erAge ? "Required value" : null);
                    etHeight.setError(erHeight ? "Required value" : null);
                    (erName ? etName : (erAge ? etAge : etHeight)).requestFocus();
                } else {
                    if (imageUri == null) {
                        String imageMale = Uri.parse("android.resource://com.example.shelockapp/drawable/male").toString();
                        String imageFemale = Uri.parse("android.resource://com.example.shelockapp/drawable/female").toString();
                        Person person = new Person(rbMale.isChecked() ? imageMale : imageFemale, etName.getText().toString(), Integer.parseInt(etAge.getText().toString())
                                , Integer.parseInt(etHeight.getText().toString()), rbMale.getText().toString(), etHairColor.getText().toString()
                                , etAddress.getText().toString(), etComment.getText().toString());
                        sendInfo(person);
                    } else {
                        Person person = new Person(imageUri.toString(), etName.getText().toString(), Integer.parseInt(etAge.getText().toString())
                                , Integer.parseInt(etHeight.getText().toString()), rbMale.getText().toString(), etHairColor.getText().toString()
                                , etAddress.getText().toString(), etComment.getText().toString());
                        sendInfo(person);
                    }
                }
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public AlertDialog dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        CharSequence[] items = {"Pick from Gallery", "Take a Photo"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickPhoto();
                        break;
                    case 1:
                        takePhoto();
                        break;
                }

            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public void takePhoto() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhoto, Constant.REQUEST_TAKE_A_PHOTO);
    }

    public void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, Constant.REQUEST_PICK_FROM_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Constant.REQUEST_TAKE_A_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Glide.with(this).load(selectedImage).override(200, 200).centerCrop().into(btnAddCover);
                    imageUri = selectedImage;
                    break;
                }


            case Constant.REQUEST_PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Glide.with(this).load(selectedImage).override(200, 200).centerCrop().into(btnAddCover);
                    imageUri = selectedImage;
                    break;
                }
        }

    }


    public void sendInfo(Person person) {
        Intent intent = getIntent();
        helper.insertInfo(person);
        intent.putExtra(Constant.PERSON, person);
        setResult(RESULT_OK, intent);
        finish();
    }

}

