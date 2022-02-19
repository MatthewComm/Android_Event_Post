package com.example.affiliate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHold> {

    Context context;
    ArrayList<PostModel> postModelList;

    public PostAdapter(Context context, ArrayList<PostModel> postModelList){
        this.context = context;
        this.postModelList = postModelList;
    }


    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);

        return new ViewHold(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        PostModel user = postModelList.get(position);

        holder.AdminName.setText(user.getAdminName());
        holder.DatePosted.setText(user.getDatePosted());
        holder.Post.setText(user.getPost());
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public static class ViewHold extends RecyclerView.ViewHolder {
        TextView AdminName, DatePosted, Post;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            AdminName = itemView.findViewById(R.id.txtAdminName);
            DatePosted = itemView.findViewById(R.id.txtDatePosted);
            Post = itemView.findViewById(R.id.txtPost);
        }
    }
}
