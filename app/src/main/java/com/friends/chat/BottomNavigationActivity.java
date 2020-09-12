package com.friends.chat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.friends.chat.behaviours.BottomNavigationBehavior;

public class BottomNavigationActivity extends AppCompatActivity {

    public FrameLayout frameContainer;
    private ActionBar toolbar;
    public FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bottom_navigation);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    launchLoginActivity();
                }
            }
        };

        toolBar();
        animateBackground();
        bottomNavigation();
        initViews();

        loadFragment(new FragmentUserList());
    }

    private void toolBar() {
        toolbar = getSupportActionBar();
        toolbar.setTitle("Chat User List");
    }

    private void initViews() {
        frameContainer = findViewById(R.id.frame_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_logout:
                Toast.makeText(getBaseContext(),"Logout..",Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            case R.id.item_notification:
                Toast.makeText(getBaseContext(),"click to Notifications..",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_setting:
                Toast.makeText(getBaseContext(),"click to Setting..",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),SettingActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
    private void launchLoginActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void bottomNavigation() {
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getBaseContext(),"click to Home..",Toast.LENGTH_SHORT).show();
                    Intent intentMain = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intentMain);
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(getBaseContext(),"click to UserList..",Toast.LENGTH_SHORT).show();
                    FragmentUserList fragmentUserList = new FragmentUserList();
                    loadFragment(fragmentUserList);
                    return true;
                case R.id.navigation_notifications:
                    //Toast.makeText(getBaseContext(),"click to Notification..",Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.navigation_setting_app:
                    //Toast.makeText(getBaseContext(),"app setting..",Toast.LENGTH_SHORT).show();
                    Intent intentSetting = new Intent(getApplicationContext(),SettingActivity.class);
                    startActivity(intentSetting);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
                notificationVisible = true;
            }
        }, 1000);
    }*/

    public void animateBackground() {
        LinearLayout anim_Layout = findViewById(R.id.ll_anim_chat_list);
        anim_Layout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) anim_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }


}
