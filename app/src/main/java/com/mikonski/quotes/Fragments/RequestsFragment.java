package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Adapters.MyRecyclerAdapter;
import com.mikonski.quotes.Models.FullPost;
import com.mikonski.quotes.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {
    private RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<FullPost> fullPostArrayList = new ArrayList<>();
    ProgressBar progressBar;

    MyRecyclerAdapter myRecyclerAdapter ;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String uid;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();


        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        myRecyclerAdapter = new MyRecyclerAdapter(getContext(),fullPostArrayList);
        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setAdapter(myRecyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        db.collection("Posts").orderBy("date", Query.Direction.ASCENDING).whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        FullPost fullPost = document.toObject(FullPost.class);
                        fullPostArrayList.add(fullPost);

                    }
                    myRecyclerAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


        return view;
    }
}
