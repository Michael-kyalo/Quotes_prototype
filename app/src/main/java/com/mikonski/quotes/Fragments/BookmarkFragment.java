package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikonski.quotes.Adapters.bookMarkSimpleAdapter;
import com.mikonski.quotes.Models.BookMark;
import com.mikonski.quotes.R;

import java.util.ArrayList;


public class BookmarkFragment extends Fragment {

    ArrayList <BookMark> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    bookMarkSimpleAdapter bookMarkSimpleAdapter;


    public BookmarkFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        arrayList.add(new BookMark("all"));
        arrayList.add(new BookMark("prayers"));
        arrayList.add(new BookMark("devotions"));

        recyclerView = view.findViewById(R.id.rec);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        bookMarkSimpleAdapter = new bookMarkSimpleAdapter(arrayList, getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bookMarkSimpleAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}
