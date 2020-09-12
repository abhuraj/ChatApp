package com.friends.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.friends.chat.ChatActivity;
import com.friends.chat.CurrentUserDetail;
import com.friends.chat.Entities.User;
import com.friends.chat.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    /*private Context context1;*/
    private List<User> userList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
        /*context1 = context;*/
    }

    /*public  int  getA(int a, int b){}
    public  int  getA(String a, int b){}*/

    @Override
    public void onClick(View v) {
        MyViewHolder holder= (MyViewHolder)v.getTag();
        int pos =  holder.getAdapterPosition();

        int id = v.getId();
        switch (id)
        {
            case R.id.tv_user_name:{
                String keyValue = userList.get(pos).getUserName().trim();
                String key = keyValue.substring((keyValue.indexOf(":")+1));
                String name = keyValue.substring(0,keyValue.indexOf(":"));
                Log.d("KeyvalueUser",""+key);
                CurrentUserDetail.chatWith = key.trim();
                callChat(name);
            }
            break;
        }
    }

    private void callChat(String name) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username",name);
        context.startActivity(intent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        //public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_user_name);
            //thumbnail = view.findViewById(R.id.thumbnail);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_user_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final User user = userList.get(position);
        holder.name.setText(user.getUserName());
        holder.name.setTag(holder);
        holder.name.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}