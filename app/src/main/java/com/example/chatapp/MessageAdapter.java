package com.example.chatapp;

import static com.example.chatapp.ChatWindow.receiverImg;
import static com.example.chatapp.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MsgModel> messageAdapterArrayList;
    int ITEM_SEND = 1, ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<MsgModel> messageAdapterArrayList) {
        this.context = context;
        this.messageAdapterArrayList = messageAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgModel message = messageAdapterArrayList.get(position);
        
        if(holder.getClass().equals(senderViewHolder.class))
        {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgTextView.setText(message.getMessage());
            viewHolder.timeStampTextView.setText(getTimeDate(message.timeStamp));
            Picasso.get().load(senderImg).into(viewHolder.circleImageView); //for setting sender image
        }
        else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgTextView.setText(message.getMessage());
            viewHolder.timeStampTextView.setText(getTimeDate(message.timeStamp));
            Picasso.get().load(receiverImg).into(viewHolder.circleImageView); //for setting receiver image
        }
    }

    @Override
    public int getItemCount() {
        return messageAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgModel message = messageAdapterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId()))
        {
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView msgTextView, timeStampTextView;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profilePicSender);
            msgTextView = itemView.findViewById(R.id.senderText);
            timeStampTextView = itemView.findViewById(R.id.senderTimeStamp);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgTextView, timeStampTextView;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profilePicReceiver);
            msgTextView = itemView.findViewById(R.id.receiverText);
            timeStampTextView = itemView.findViewById(R.id.receiverTimeStamp);
        }
    }

    public static String getTimeDate(long timestamp){
        try{
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}
