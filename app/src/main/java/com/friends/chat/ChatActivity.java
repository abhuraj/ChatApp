package com.friends.chat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    Firebase reference1, reference2;
    Button btnSend;
    LinearLayout layout;
    ScrollView scrollView;
    RelativeLayout layout_2;
    String name;
    EditText etMessage;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_view);
        animateBackground();
        Intent intent = getIntent();
        if(intent != null && intent.getExtras()!= null) {
            name = intent.getStringExtra("username");
        }
        initViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle(name);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://freindzchat.firebaseio.com/messages/" + CurrentUserDetail.username + "_" + CurrentUserDetail.chatWith);
        reference2 = new Firebase("https://freindzchat.firebaseio.com/messages/" + CurrentUserDetail.chatWith + "_" + CurrentUserDetail.username);

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageText = etMessage.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user",CurrentUserDetail.username);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    etMessage.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(CurrentUserDetail.username)){
                    addMessageBox(message, 1);
                } else {
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void initViews() {
        btnSend = findViewById(R.id.btn_send_msg);
        etMessage = findViewById(R.id.et_msg);
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        scrollView = findViewById(R.id.scrollView);
    }

    private void animateBackground() {
        LinearLayout anim_Layout = findViewById(R.id.ll_anim_chat_box);
        anim_Layout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) anim_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        LinearLayout linearLayout = new LinearLayout(ChatActivity.this);
        textView.setText(message);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(14);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setPadding(20,10,20,10);

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bg_round_border_green);
            //linearLayout.setBackgroundResource(R.drawable.chat_box_left11);
            lp2.setMargins(200,10,20,10);
        } else {
            lp2.gravity = Gravity.LEFT;
            linearLayout.setBackgroundResource(R.drawable.bg_round_border_blue);
            //linearLayout.setBackgroundResource(R.drawable.chat_box_right11);
            lp2.setMargins(20,10,200,10);
        }
        linearLayout.setLayoutParams(lp2);
        linearLayout.addView(textView);
        layout.addView(linearLayout);
        scrollView.fullScroll(View.FOCUS_UP);
    }
}