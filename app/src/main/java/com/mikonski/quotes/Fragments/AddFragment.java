package com.mikonski.quotes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikonski.quotes.Models.Post;
import com.mikonski.quotes.R;
import com.mikonski.quotes.utils.CustomAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private TextInputEditText postInput;
    private LinearLayout body, loading;
    private TextView failed;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String post_text;
    private String uid;
    private String type;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Post post;
    private Button send;
    private String[] types={"Prayer","Devotion"};

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();


        View view =inflater.inflate(R.layout.fragment_add, container, false);
        spinner = view.findViewById(R.id.spinner);
        postInput = view.findViewById(R.id.post_et);
        send = view.findViewById(R.id.send_button);
        body = view.findViewById(R.id.linearLayout);
        loading = view.findViewById(R.id.linearLayout_loading);
        failed = view.findViewById(R.id.textFailed);



        CustomAdapter spinnerAdapter = new CustomAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(),types);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_text = Objects.requireNonNull(postInput.getText()).toString();
                Log.d(TAG, "onClick: " + type);
                Log.d(TAG, "onClick: " + post_text);
                if(!TextUtils.isEmpty(post_text)&&!TextUtils.isEmpty(type)){
                    body.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);

                    final String ref = db.collection("Posts").document().getId();
                    post = new Post(ref,type,post_text,uid);

                    db.collection("Posts").document(ref).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){

                             Map<String, Object> updates = new HashMap<>();
                             updates.put("date", FieldValue.serverTimestamp());
                             db.collection("Posts").document(ref).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      body.setVisibility(View.VISIBLE);
                                      loading.setVisibility(View.GONE);
                                      postInput.setText("");
                                      failed.setText("Sending was Successful");
                                      failed.setTextColor(getContext().getResources().getColor(R.color.colorGreen));

                                  }
                                  else {
                                      body.setVisibility(View.VISIBLE);
                                      loading.setVisibility(View.GONE);
                                      failed.setText("Error Sending");
                                      failed.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                                  }
                                 }
                             });

                         }
                         else {
                             body.setVisibility(View.VISIBLE);
                             loading.setVisibility(View.GONE);
                             failed.setText("Error Sending");
                             failed.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                         }
                        }
                    });
                }
                else {
                    failed.setText("fill all fields");
                    failed.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " +position);
        type = types[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
