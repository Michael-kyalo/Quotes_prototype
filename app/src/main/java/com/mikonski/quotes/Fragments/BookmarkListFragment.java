package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Adapters.FeedRecyclerAdapter;
import com.mikonski.quotes.Models.BookMark;
import com.mikonski.quotes.Models.FullPost;
import com.mikonski.quotes.Models.userBookmark;
import com.mikonski.quotes.R;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkListFragment extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String uid, post_type;
    ImageView back;
    RecyclerView recyclerView;
    FeedRecyclerAdapter feedRecyclerAdapter;
    ProgressBar progressBar;
    ArrayList<FullPost> fullPostArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BookmarkListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();
        recyclerView = view.findViewById(R.id.rec);
        progressBar = view.findViewById(R.id.progressBar);
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(),fullPostArrayList);
        recyclerView.setAdapter(feedRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();
            }
        });

        Bundle bundle = this.getArguments();
        if(bundle == null){
            db.collection("Bookmarks").whereEqualTo("uid", uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e == null){

                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                            userBookmark bookMark = queryDocumentSnapshot.toObject(userBookmark.class);

                            db.collection("Posts").whereEqualTo("post_id",bookMark.getPost_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        for (QueryDocumentSnapshot queryDocumentSnapshots1: Objects.requireNonNull(task.getResult())){
                                            FullPost fullPost = queryDocumentSnapshots1.toObject(FullPost.class);
                                            fullPostArrayList.add(fullPost);
                                        }
                                    }
                                }
                            });
                        }
                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                    else {
                        Log.d(TAG, "onEvent: " + e.getMessage());
                    }

                }
            });
        }
        else {
            post_type = getArguments().getString("post_type");
            db.collection("Bookmarks").whereEqualTo("uid", uid).whereEqualTo("post_type", post_type).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e == null){

                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                            userBookmark bookMark = queryDocumentSnapshot.toObject(userBookmark.class);

                            db.collection("Posts").whereEqualTo("post_id",bookMark.getPost_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        for (QueryDocumentSnapshot queryDocumentSnapshots1: Objects.requireNonNull(task.getResult())){
                                            FullPost fullPost = queryDocumentSnapshots1.toObject(FullPost.class);
                                            fullPostArrayList.add(fullPost);
                                        }
                                    }
                                }
                            });
                        }
                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                    else {
                        Log.d(TAG, "onEvent: " + e.getMessage());
                    }

                }
            });
        }

        return view;
    }
}
