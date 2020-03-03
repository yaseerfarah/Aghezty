package com.example.aghezty.View;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;
    private Observer currentUserInfoObserver;

    @BindView(R.id.person_name)
    TextView userName;
    @BindView(R.id.person_email)
    TextView userEmail;
    @BindView(R.id.an_first)
    TextView userFirstName;
    @BindView(R.id.first)
    TextView firstName;
    @BindView(R.id.an_last)
    TextView userLastName;
    @BindView(R.id.last)
    TextView lastName;
    @BindView(R.id.an_email)
    TextView user_Email;
    @BindView(R.id.an_phone)
    TextView userPhoneNumber;



    @BindView(R.id.user_image)
    ImageView imageView;

    @BindView(R.id.setting)
    ImageButton editProfile;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    @BindView(R.id.progress)
    ProgressBar progressBar;


    public Profile() {
        // Required empty public constructor

        currentUserInfoObserver=new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo currentUserInfo) {
                if (currentUserInfo!=null) {
                    userInfo = currentUserInfo;
                    assignView();
                    statefulLayout.showContent();
                }
            }
        };


    }



    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getLoginUserInfo();
        userViewModel.getCurrentUserInfoLiveData().observe(this,currentUserInfoObserver);

    }


    @Override
    public void onStop() {
        super.onStop();
        userViewModel.getCurrentUserInfoLiveData().removeObservers(this);
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
       // userInfo=userViewModel.getCurrentUserInfo();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);
        statefulLayout.showLoading("");

        editProfile.setOnClickListener(v -> {
            popUp_Menu();
        });


    }



    private void assignView(){


        if (userInfo.getImgUrl()!=null){
            imageView.setBackground(null);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(userInfo.getImgUrl())
                    .apply(RequestOptions.timeoutOf(60*1000))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e(getClass().getName(),e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(imageView);

        }


        userName.setText(userInfo.getName());
        String[]name=userInfo.getName().split(" ");
        userFirstName.setText(name[0]);
        if (name.length>1&&name[1] != null) {
            userLastName.setText(name[1]);
        } else {
            firstName.setText("Name");
            lastName.setVisibility(View.GONE);
            userLastName.setVisibility(View.GONE);
        }

        userEmail.setText(userInfo.getEmail());
        user_Email.setText(userInfo.getEmail());
        userPhoneNumber.setText(userInfo.getPhoneNumber());

    }




    private void popUp_Menu(){

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getContext(), editProfile);

        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.edit_profile_popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.edit_profile:
                        navController.navigate(R.id.action_profile_to_editProfile);
                        break;

                    case R.id.my_addresses:
                        navController.navigate(R.id.action_profile_to_myAddresses);
                        break;

                    case R.id.new_password:
                        navController.navigate(R.id.action_profile_to_changePassword);
                        break;

                    case R.id.log_out:
                        userViewModel.logOut();
                        navController.navigateUp();
                        break;

                }

                return true;
            }
        });

        popup.show(); //showing popup menu

    }



}
