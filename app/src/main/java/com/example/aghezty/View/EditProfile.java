package com.example.aghezty.View;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.CityInfo;
import com.example.aghezty.POJO.GovernorateInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {

    private static final int REQUEST_GALLERY=200;


    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;


    private Uri userImageUri=null;


    @BindView(R.id.first_edit)
    EditText firstName;
    @BindView(R.id.last_edit)
    EditText lastName;
    @BindView(R.id.email_edit)
    EditText email;
    @BindView(R.id.phone_edit)
    EditText phoneNumber;
    @BindView(R.id.save)
    Button save;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;


    @BindView(R.id.user_image)
    ImageView imageView;

    public EditProfile() {
        // Required empty public constructor


    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);

        userInfo=userViewModel.getCurrentUserInfo();
        if (userInfo!=null){
            assignFields();
        }else {
            userInfo=new UserInfo();
        }

        imageView.setOnClickListener(v -> {

            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, REQUEST_GALLERY);

        });


        save.setOnClickListener(v -> {


            validationFields();

        });




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK&&requestCode==REQUEST_GALLERY){

            try {
                userImageUri=data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), userImageUri);
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                Log.e(getClass().getName(),e.getMessage());

            }


        }
    }



    private void validationFields(){

        if (!firstName.getText().toString().isEmpty()&&!lastName.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!phoneNumber.getText().toString().isEmpty()){
            statefulLayout.showLoading(" ");

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                email.setError("Email Format Not Correct");
            }else {


                userInfo.setName(firstName.getText().toString()+" "+lastName.getText().toString());
                userInfo.setEmail(email.getText().toString());
                userInfo.setPhoneNumber(phoneNumber.getText().toString());

                //userViewModel.setCurrentUserInfo(userInfo);

                userViewModel.updateProfile(userInfo, userImageUri, new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        navController.navigateUp();
                    }

                    @Override
                    public void onFailure(String e) {
                        statefulLayout.showContent();
                    }
                });

            }


        }else {
            Toasty.error(getContext(),"Complete All Fields Please", Toast.LENGTH_SHORT).show();
        }


    }





    private void assignFields(){

        if (userInfo.getImgUrl()!=null){
            Glide.with(getContext()).load(userInfo.getImgUrl()).apply(RequestOptions.timeoutOf(60*1000)).into(imageView);
        }

        String[]name=userInfo.getName().split("\\s+");

        firstName.setText(name[0]);
        if (name.length>1&&name[1]!=null){
            lastName.setText(name[1]);
        }
        email.setText(userInfo.getEmail());
        phoneNumber.setText(userInfo.getPhoneNumber());


    }






}
