package com.mikonski.quotes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikonski.quotes.Models.FullPost;
import com.mikonski.quotes.Models.User;
import com.mikonski.quotes.R;

import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.feedHolder> {
    private Context context;
    private List<FullPost> fullPostList;

    public FeedRecyclerAdapter(Context context, List<FullPost> fullPostList) {
        this.context = context;
        this.fullPostList = fullPostList;
    }

    @NonNull
    @Override
    public feedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_view,parent,false);
        return new feedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final feedHolder holder, int position) {
        FullPost fullPost = fullPostList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(fullPost.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                holder.username.setText(user.getUsername());
            }
        });

        holder.body.setText(fullPost.getPost_body());
        holder.type.setText(fullPost.getPost_type());

    }

    @Override
    public int getItemCount() {
        return fullPostList.size();
    }

    public class feedHolder extends RecyclerView.ViewHolder{
        TextView type, date, body, username;

        public feedHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.text_type);
            date = itemView.findViewById(R.id.text_date);
            body = itemView.findViewById(R.id.text_body);
            username = itemView.findViewById(R.id.text_username);
        }
    }

}
