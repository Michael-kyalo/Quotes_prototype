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
import com.mikonski.quotes.Models.Comment;
import com.mikonski.quotes.Models.User;
import com.mikonski.quotes.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.holder> {
    List<Comment> commentList;
    Context context;

    public CommentsAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int position) {

        Comment comment = commentList.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(comment.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                holder.username.setText(user.getUsername());
            }
        });

        Date date = comment.getDate();

        if(date !=null){
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String add_date = dateFormat.format(date);
            holder.date.setText(add_date);
        }

        holder.comment.setText(comment.getComment_body());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView username, date, comment;
        public holder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.text_username);
            date = itemView.findViewById(R.id.text_date);
            comment = itemView.findViewById(R.id.text_comment);
        }
    }
}
