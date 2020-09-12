package com.friends.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.friends.chat.Entities.User;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private TextView tvLoginHeader, tvRegistaerHeader, tvClickLogin, tvGetRegister, tvClickRegister, tvForgotPassword;
    private EditText etUserLogin, etUserPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;
    private String user, pass;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String key;

     /*private int count = 0;
    private Object u;
    private int a[]  = {2,3};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_page_view);
        //animateBackground(); // to animate background
        Intent intent = getIntent();

        /*count++;
        count++;
        count ++;
        Log.d("",""+count);
        if(u instanceof  String){
        }
        else  if( u instanceof  Integer){
        }
        else  if(u instanceof BottomNavigationActivity){
        }
        else  if(u instanceof JSONArray){
        }*/

        if (intent != null && intent.getExtras() != null) {
            key = intent.getStringExtra("register");
        }
        initViews();
    }

    /*private void animateBackground() {
        LinearLayout anim_Layout = findViewById(R.id.main_container);
        AnimationDrawable animationDrawable = (AnimationDrawable) anim_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }*/

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
                Toast.makeText(getBaseContext(), "Logout..", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            case R.id.item_notification:
                Toast.makeText(getBaseContext(), "click to Notifications..", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_setting:
                Toast.makeText(getBaseContext(), "click to Setting..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        auth.signOut();
        etUserLogin.setText("");
        etUserPassword.setText("");

        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, BottomNavigationActivity.class));
                    finish();
                }
            }
        };
    }

    /*private void changePassword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }*/

    /*private void changeEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Email address is updated.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/

    /*private void deleteAccount(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }*/

    private void initViews() {
        initDb();

        tvLoginHeader = findViewById(R.id.tv_login_header);
        tvRegistaerHeader = findViewById(R.id.tv_register_header);
        etUserLogin = findViewById(R.id.et_user_login);
        etUserPassword = findViewById(R.id.et_user_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvClickLogin = findViewById(R.id.tv_click_to_login);
        tvClickRegister = findViewById(R.id.tv_click_to_register);
        tvGetRegister = findViewById(R.id.tv_get_register);
        tvForgotPassword = findViewById(R.id.tv_click_to_forgot_password);
        etUserLogin.setText("abhishek@test.com");
        etUserPassword.setText("pass12345");

        if(key != null && key.equalsIgnoreCase("register")) {
            tvGetRegister.setVisibility(View.GONE);
            tvClickRegister.setVisibility(View.GONE);
            tvClickLogin.setVisibility(View.VISIBLE);
            tvRegistaerHeader.setVisibility(View.VISIBLE);
            tvLoginHeader.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            tvForgotPassword.setVisibility(View.GONE);
            etUserLogin.setText("");
            etUserPassword.setText("");
        } else {
            tvClickLogin.setVisibility(View.GONE);
            tvGetRegister.setVisibility(View.VISIBLE);
            tvClickRegister.setVisibility(View.VISIBLE);
            tvLoginHeader.setVisibility(View.VISIBLE);
            tvRegistaerHeader.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
            tvForgotPassword.setVisibility(View.VISIBLE);
            etUserLogin.setText("");
            etUserPassword.setText("");
        }
        Firebase.setAndroidContext(this);
        setClickListeners();
    }

    private void initDb() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    private void setClickListeners() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvClickLogin.setOnClickListener(this);
        tvClickRegister.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId ();
        switch (i){
            case R.id.btn_login:
                userLogin();
                break;

            case R.id.btn_register:
                userRegister();
                break;

            case R.id.tv_click_to_login:
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.tv_click_to_register:
                Intent intent1 = new Intent(getBaseContext(),MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("register","register");
                startActivity(intent1);
                break;

            case R.id.tv_click_to_forgot_password:
                Toast.makeText(getBaseContext(),"Forgot Password Coming Soon..",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void userLogin() {
        user = etUserLogin.getText().toString();
        pass = etUserPassword.getText().toString();

        final User users = new User();
        users.setUserName(user);
        users.setPassword(pass);

        if(user.equals("")){
            etUserLogin.setError("can't be blank");
        }
        else if(pass.equals("")){
            etUserPassword.setError("can't be blank");
        }
        else {
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            final String user = etUserLogin.getText().toString();
            final String password = etUserPassword.getText().toString();
            auth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            pd.hide();
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Incorrect Login/Password", Toast.LENGTH_LONG).show();
                            } else {
                                CurrentUserDetail.username = user;
                                CurrentUserDetail.password = password;
                                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_LONG).show();
                                callUserList();
                            }
                        }
                    });
        }
    }

    private void callUserList() {
        //Intent intent = new Intent(this, FragmentUserList.class);
        Intent intent = new Intent(this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    private void userRegister() {
        user = etUserLogin.getText().toString();
        pass = etUserPassword.getText().toString();

        final User users = new User();
        users.setUserName(user);
        users.setPassword(pass);

        if(user.equals("")){
            etUserLogin.setError("can't be blank");
        }
        else if(pass.equals("")){
            etUserPassword.setError("can't be blank");
        }
        else if(user.length()<5){
            etUserLogin.setError("at least 5 characters long");
        }
        else if(pass.length()<5){
            etUserPassword.setError("at least 5 characters long");
        }
        else {
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            auth.createUserWithEmailAndPassword(user,pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:"
                                    + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                pd.hide();
                                Toast.makeText(MainActivity.this, ""+task.getException().getMessage()
                                        + task.getException(),Toast.LENGTH_SHORT).show();
                            } else {
                                pd.hide();
                                CurrentUserDetail.username = user;
                                CurrentUserDetail.password = pass;
                                String key = databaseReference.child("users").push().getKey();
                                databaseReference.child("users").child(key).setValue(users);
                                callUserList();
                            }
                        }
                    });
        }
    }
}
