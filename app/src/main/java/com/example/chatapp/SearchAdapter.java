package com.example.chatapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    Search searchActivity;
    ArrayList<Users> userArrayList;
    public SearchAdapter(Search searchActivity, ArrayList<Users> userArrayList) {
        this.searchActivity = searchActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public SearchAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(searchActivity).inflate(R.layout.user_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.viewHolder holder, int position) {
        Users user = userArrayList.get(position);
        holder.userName.setText(user.userName);
        holder.email.setText(user.email);
        Picasso.get().load(user.profilePic).into(holder.profilePic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchActivity, ChatWindow.class);
                intent.putExtra("userName", user.getUserName());
                intent.putExtra("email", user.getEmail());
                intent.putExtra("receiverImg", user.getProfilePic());
                intent.putExtra("uid", user.getUserId());
                intent.putExtra("status", user.getStatus());
                searchActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView userName, email;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic);
            userName = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.email);

        }
    }
}
