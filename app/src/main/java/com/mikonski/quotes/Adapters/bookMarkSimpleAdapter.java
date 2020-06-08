package com.mikonski.quotes.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Activities.MainActivity;
import com.mikonski.quotes.Fragments.BookmarkListFragment;
import com.mikonski.quotes.Fragments.SinglePostViewFragment;
import com.mikonski.quotes.Models.BookMark;
import com.mikonski.quotes.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class bookMarkSimpleAdapter extends RecyclerView.Adapter<bookMarkSimpleAdapter.bookmarkholder>{
    private List<BookMark> bookMarkList;
    private Context context;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String uid;


    public bookMarkSimpleAdapter(List<BookMark> bookMarkList, Context context) {
        this.bookMarkList = bookMarkList;
        this.context = context;
    }

    @NonNull
    @Override
    public bookmarkholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_single_item,parent,false);
        return new bookmarkholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final bookmarkholder holder, int position) {

        BookMark bookMark = bookMarkList.get(position);
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();  firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(position == 0){
            holder.title.setText(bookMark.getTitle());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)v.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    BookmarkListFragment bookmarkListFragment = new BookmarkListFragment();
                    fragmentTransaction.replace(R.id.framelayout, bookmarkListFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            db.collection("Bookmarks").whereEqualTo("uid", uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e== null){
                        int counter = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            counter ++;
                        }
                        if(counter>0){
                            holder.number.setText(formatValue(counter) + " prayers and devotions");}
                        else { holder.number.setText(counter + " prayers and devotions");}
                    }
                }
            });

            holder.title.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }else if(position == 1){
            holder.title.setText(bookMark.getTitle());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)v.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    BookmarkListFragment bookmarkListFragment = new BookmarkListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("post_type", "Prayer");
                    bookmarkListFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.framelayout, bookmarkListFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            db.collection("Bookmarks").whereEqualTo("uid", uid).whereEqualTo("post_type", "Prayer").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e== null){
                        int counter = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            counter ++;
                        }
                        if(counter>0){
                            holder.number.setText(formatValue(counter) + " prayers");}
                        else { holder.number.setText(counter + " prayers");}
                    }
                }
            });



            holder.title.setTextColor(context.getResources().getColor(R.color.colorBlue));

        }
        else{
            holder.title.setText(bookMark.getTitle());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)v.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                   BookmarkListFragment bookmarkListFragment = new BookmarkListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("post_type", "Devotion");
                    bookmarkListFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.framelayout, bookmarkListFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

            db.collection("Bookmarks").whereEqualTo("uid", uid).whereEqualTo("post_type", "Devotion").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e== null){
                        int counter = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            counter ++;
                        }
                        if(counter>0){
                        holder.number.setText(formatValue(counter) + " devotions");}
                        else { holder.number.setText(counter + " devotions");}
                    }
                }
            });

            holder.title.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }


    }

    @Override
    public int getItemCount() {
        return bookMarkList.size();
    }

    public class bookmarkholder extends RecyclerView.ViewHolder{
        TextView title, number;
        CardView cardView;

        public bookmarkholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_title);
            number = itemView.findViewById(R.id.text_number);
            cardView = itemView.findViewById(R.id.card);


        }
    }
    public static String formatValue(double value) {
        int power;
        String suffix = " kmbt";
        String formattedNumber = "";

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int)StrictMath.log10(value);
        value = value/(Math.pow(10,(power/3)*3));
        formattedNumber=formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power/3);
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }
}
