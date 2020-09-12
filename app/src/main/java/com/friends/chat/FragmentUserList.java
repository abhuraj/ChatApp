package com.friends.chat;

import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.friends.chat.Entities.User;
import com.friends.chat.adapters.UserAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FragmentUserList extends Fragment {

    public RecyclerView listView;
    public ArrayList<User> userList;
    public int totalUsers = 0;
    public ProgressDialog progressDialog;
    public View view;
    public UserAdapter userAdapter;

    public FragmentUserList() {
        // Required empty public constructor
    }

    public static FragmentUserList newInstance(String param1, String param2) {
        FragmentUserList fragment = new FragmentUserList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        initViews();
        animateBackground(view);
        getUserList();
        actionListener();
        return view;
    }

    private void initViews() {
        listView = view.findViewById(R.id.rv_user_listview);

    }

    private void actionListener() {
      /*  listView.addOnItemTouchListener(new AdapterView.() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });*/
    }

    private void userList (){
        listView = view.findViewById(R.id.rv_user_listview);

        userAdapter = new UserAdapter(getContext(),userList);
        // Set your adapter
        UserAdapter adapter = new UserAdapter(getContext(), userList);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(adapter);
    }

    private void animateBackground(View view) {
        LinearLayout anim_Layout = view.findViewById(R.id.ll_anim_chat_list);
        anim_Layout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) anim_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }

    private void getUserList() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://freindzchat.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                showList(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.hide();
                Log.d("getUserList","progressDialog:- "+volleyError);
                Toast.makeText(getContext(),"click to Notifications..",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    private void showList(String s) {
        try {
            userList = new ArrayList<>();
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key;

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals("")) {
                    JSONObject jsonObject = obj.getJSONObject(key);
                    String userName = jsonObject.getString("userName");
                    String password = jsonObject.getString("password");
                    if (userName.equalsIgnoreCase(CurrentUserDetail.username)
                            && password.equalsIgnoreCase(CurrentUserDetail.password)) {
                        CurrentUserDetail.username = key;
                    } else {
                        User user = new User();
                        user.setUserName(userName + " : " + key);
                        userList.add(user);
                    }
                }
                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.hide();
        userList();
    }
}
