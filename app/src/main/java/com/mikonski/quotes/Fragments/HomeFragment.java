package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Adapters.FeedRecyclerAdapter;
import com.mikonski.quotes.Models.FullPost;
import com.mikonski.quotes.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView all, prayers, devotions,requests;
    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<FullPost> fullPostArrayList = new ArrayList<>();
    FeedRecyclerAdapter feedRecyclerAdapter;
    ProgressBar progressBar;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(),fullPostArrayList);
        progressBar = view.findViewById(R.id.progressBar);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.feedRecycler);
        recyclerView.setAdapter(feedRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        all = view.findViewById(R.id.text_all);
        prayers = view.findViewById(R.id.text_prayers);
        devotions = view.findViewById(R.id.text_devotions);


        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        FullPost fullPost = document.toObject(FullPost.class);
                        fullPostArrayList.add(fullPost);
                    }
                    feedRecyclerAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.scrollToPosition(0);

            }
        });



        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_selected));
                all.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                prayers.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                devotions.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                prayers.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                devotions.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                progressBar.setVisibility(View.VISIBLE);

                fullPostArrayList.clear();



                db.collection("Posts").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                FullPost fullPost = document.toObject(FullPost.class);
                                fullPostArrayList.add(fullPost);
                            }
                            feedRecyclerAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                            recyclerView.scrollToPosition(0);
                        }

                    }
                });


            }
        });
        prayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prayers.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_selected));
                prayers.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                all.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                devotions.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                all.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                devotions.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                progressBar.setVisibility(View.VISIBLE);

                fullPostArrayList.clear();

                db.collection("Posts").orderBy("date", Query.Direction.DESCENDING).whereEqualTo("post_type", "Prayer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                FullPost fullPost = document.toObject(FullPost.class);
                                fullPostArrayList.add(fullPost);
                            }
                            feedRecyclerAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.scrollToPosition(0);
                        }

                    }
                });

            }
        });
        devotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                devotions.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_selected));
                devotions.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                prayers.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                all.setBackground(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.chip_unselected));
                all.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                prayers.setTextColor(getContext().getResources().getColor(R.color.colorGrey));
                progressBar.setVisibility(View.VISIBLE);

                fullPostArrayList.clear();

                db.collection("Posts").whereEqualTo("post_type", "Devotion").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                                FullPost fullPost = document.toObject(FullPost.class);
                                fullPostArrayList.add(fullPost);
                            }
                            feedRecyclerAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.scrollToPosition(0);
                        }

                    }
                });
            }
        });


        return view;
    }
}
