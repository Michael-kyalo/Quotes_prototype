package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Adapters.CommentsAdapter;
import com.mikonski.quotes.Models.Com;
import com.mikonski.quotes.Models.Comment;
import com.mikonski.quotes.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SinglePostViewFragment extends Fragment {
    ImageView back, add;
    TextInputEditText comment;
    RecyclerView recyclerView;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    ProgressBar progressBar;
    CommentsAdapter commentsAdapter;
    TextView textView;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String comment_text;
    private String uid,post_id;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public SinglePostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_single_post_view, container, false);
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();
        comment = view.findViewById(R.id.comment_et);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textFailed);
        recyclerView = view.findViewById(R.id.rec);

        commentsAdapter = new CommentsAdapter(commentArrayList,getContext());
        recyclerView.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            post_id = getArguments().getString("post_id");
//            db.collection("Comments").orderBy("date", Query.Direction.ASCENDING).whereEqualTo("post_id",post_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//                        progressBar.setVisibility(View.GONE);
//                        for (QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
//                            Comment comment = documentSnapshot.toObject(Comment.class);
//                            commentArrayList.add(comment);
//                        }
//                        commentsAdapter.notifyDataSetChanged();
//                        textView.setVisibility(View.GONE);
//
//                    }
//                    else {
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText(getText(R.string.loading_failed));
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }
//            });
            db.collection("Comments").orderBy("date", Query.Direction.ASCENDING).whereEqualTo("post_id", post_id)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e!=null){
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(getText(R.string.loading_failed));
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        commentArrayList.clear();
                        progressBar.setVisibility(View.GONE);
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            commentArrayList.add(comment);
                        }
                        commentsAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(commentArrayList.size()-1);
                        textView.setVisibility(View.GONE);
                    }

                }
            });
        }
        add = view.findViewById(R.id.comment);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                comment_text = Objects.requireNonNull(comment.getText()).toString().trim();


                if(!TextUtils.isEmpty(comment_text)&&!TextUtils.isEmpty(post_id)){
                    final String ref = db.collection("Comments").document().getId();
                    Com com = new Com(ref,comment_text,uid, post_id);
                    progressBar.setVisibility(View.VISIBLE);

                    db.collection("Comments").document(ref).set(com).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String, Object> update = new HashMap<>();
                                update.put("date", FieldValue.serverTimestamp());
                                db.collection("Comments").document(ref).update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            comment.setText("");
                                            progressBar.setVisibility(View.GONE);

                                        }
                                        else{
                                            textView.setVisibility(View.VISIBLE);
                                            textView.setText(getText(R.string.commenting_failed));
                                            progressBar.setVisibility(View.GONE);

                                        }
                                    }
                                });
                            }
                            else {

                                textView.setVisibility(View.VISIBLE);
                                textView.setText(getText(R.string.commenting_failed));
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });


                }
            }
        });



        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
            }
        });
        return  view;
    }
}
