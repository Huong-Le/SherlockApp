package com.example.shelockapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shelockapp.R;
import com.example.shelockapp.callback.OnClickListener;
import com.example.shelockapp.constant.Constant;
import com.example.shelockapp.database.DatabaseHelper;
import com.example.shelockapp.model.Person;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shini_000 on 7/15/2016.
 */
public class InformationFragment extends Fragment {

    @Bind(R.id.btn_cover_info)
    ImageView btnCoverInfo;
    @Bind(R.id.et_name_info)
    EditText etNameInfo;
    @Bind(R.id.et_age_info)
    EditText etAgeInfo;
    @Bind(R.id.et_height_info)
    EditText etHeightInfo;
    @Bind(R.id.rb_male_info)
    RadioButton rbMaleInfo;
    @Bind(R.id.rb_female_info)
    RadioButton rbFemaleInfo;
    @Bind(R.id.et_hair_color_info)
    EditText etHairColorInfo;
    @Bind(R.id.et_address_info)
    EditText etAddressInfo;
    @Bind(R.id.et_comment_info)
    EditText etCommentInfo;
    @Bind(R.id.btn_save)
    Button btnSave;
    Person person;
    DatabaseHelper helper;
    Uri imageUri;
    OnClickListener callback;
    int id;

    public InformationFragment(Person person) {
        this.person = person;
    }
    public void setCallback(OnClickListener callback){
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, view);
        loadData(person);
        id = person.getId();
        helper = new DatabaseHelper(getActivity());
        btnCoverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = dialog();
                dialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erName;
                boolean erAge;
                boolean erHeight;
                if ((erName = etNameInfo.getText().toString().equals("")) | (erAge = etAgeInfo.getText().toString().equals("")) | (erHeight = etHeightInfo.getText().toString().equals(""))) {
                    etNameInfo.setError(erName ? "Required value" : null);
                    etAgeInfo.setError(erAge ? "Required value" : null);
                    etHeightInfo.setError(erHeight ? "Required value" : null);
                    (erName ? etNameInfo : (erAge ? etAgeInfo : etHeightInfo)).requestFocus();
                } else {
                    if (imageUri == null) {
                        String image = person.getCover();
                        person = new Person(image, etNameInfo.getText().toString(), Integer.parseInt(etAgeInfo.getText().toString())
                                , Integer.parseInt(etHeightInfo.getText().toString()), (rbMaleInfo.isChecked()?rbMaleInfo:rbFemaleInfo).getText().toString(), etHairColorInfo.getText().toString()
                                , etAddressInfo.getText().toString(), etCommentInfo.getText().toString());
                        helper.updateInfo(person, id);
                    } else {
                        person = new Person(imageUri.toString(), etNameInfo.getText().toString(), Integer.parseInt(etAgeInfo.getText().toString())
                                , Integer.parseInt(etHeightInfo.getText().toString()), (rbMaleInfo.isChecked()?rbMaleInfo:rbFemaleInfo).getText().toString(), etHairColorInfo.getText().toString()
                                , etAddressInfo.getText().toString(), etCommentInfo.getText().toString());
                        helper.updateInfo(person, id);
                    }
                    Toast.makeText(getContext(), "Success Update", Toast.LENGTH_SHORT).show();
                    callback.onClick(v,0);
//                    Intent intent = getActivity().getIntent();
//                    intent.putExtra(Constant.PERSON, person);
//                    getActivity().setResult(Activity.RESULT_OK, intent);
//                    getActivity().finish();
                }
            }
        });
        return view;
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


    public AlertDialog dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_TAKE_A_PHOTO:
                if (resultCode == Constant.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Glide.with(this).load(selectedImage).override(200, 200).centerCrop().into(btnCoverInfo);
                    imageUri = selectedImage;
                    break;
                }
            case Constant.REQUEST_PICK_FROM_GALLERY:
                if (resultCode == Constant.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Glide.with(this).load(selectedImage).override(200, 200).centerCrop().into(btnCoverInfo);
                    imageUri = selectedImage;
                    break;
                }
        }
    }
    public void loadData(Person person){
        Glide.with(this).load(Uri.parse(person.getCover())).override(200,200).centerCrop().into(btnCoverInfo);
        etNameInfo.setText(person.getName());
        etAgeInfo.setText(String.valueOf(person.getAge()));
        etHeightInfo.setText(String.valueOf(person.getHeight()));
        (person.getGender().equals("Male")?rbMaleInfo:rbFemaleInfo).setChecked(true);
        etHairColorInfo.setText(person.getHairColor());
        etAddressInfo.setText(person.getAddress());
        etCommentInfo.setText(person.getComment());
    }
}
