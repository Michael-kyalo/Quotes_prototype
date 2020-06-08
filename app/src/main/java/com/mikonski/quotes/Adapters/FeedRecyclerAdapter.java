package com.mikonski.quotes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikonski.quotes.Activities.MainActivity;
import com.mikonski.quotes.Fragments.SinglePostViewFragment;
import com.mikonski.quotes.Models.BookMark;
import com.mikonski.quotes.Models.FullPost;
import com.mikonski.quotes.Models.User;
import com.mikonski.quotes.Models.userBookmark;
import com.mikonski.quotes.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.feedHolder> {
    private Context context;
    private List<FullPost> fullPostList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String uid;


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
        final FullPost fullPost = fullPostList.get(position);
        holder.username.setText("");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(fullPost.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                assert user != null;
                holder.username.setText(user.getUsername());
            }
        });
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        uid = firebaseUser.getUid();
        holder.bookmark.setImageResource(R.drawable.ic_action_bookmarks_outline);
        db.collection("Bookmarks").whereEqualTo("post_id", fullPost.getPost_id()).whereEqualTo("uid", uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e== null){
                    int counter = 0;

                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        counter++;

                    }
                    if(counter>0){
                        // add delete bookmark function in future updates
                        holder.bookmark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast toast = Toast.makeText(context,"already in bookmark",Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        });
                        holder.bookmark.setImageResource(R.drawable.ic_action_bookmark_black);
                    }
                    else {
                        holder.bookmark.setImageResource(R.drawable.ic_action_bookmarks_outline);
                        holder.bookmark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                userBookmark bookMark = new userBookmark();
                                bookMark.setPost_id(fullPost.getPost_id());
                                bookMark.setPost_type(fullPost.getPost_type());
                                bookMark.setUid(uid);
                                String ref = db.collection("Bookmarks").document().getId();

                                db.collection("Bookmarks").document(ref).set(bookMark).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                           Toast toast = Toast.makeText(context,"added to bookmark",Toast.LENGTH_SHORT);
                                           toast.setGravity(Gravity.CENTER,0,0);
                                           toast.show();

                                            holder.bookmark.setImageResource(R.drawable.ic_action_bookmark_black);
                                        }
                                    }
                                });
                            }
                        });
                    }

                }
            }
        });



        holder.body.setText(fullPost.getPost_body());
        holder.type.setText(fullPost.getPost_type());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)v.getContext();
                Uri baseUrl = Uri.parse("https://mikonski.page.link/naxz");
                String domain = "https://mikonski.page.link";

                DynamicLink link = FirebaseDynamicLinks.getInstance()
                        .createDynamicLink()
                        .setLink(baseUrl)
                        .setDomainUriPrefix(domain)
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.mikonski.quotes").build())
                        .buildDynamicLink();
                /*Create an ACTION_SEND Intent*/
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = fullPost.getPost_body();
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, activity.getString(R.string.share_subject));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody +"download to share prayers "+ link.getUri().toString());
                /*Fire!*/
                activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_using)));
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)v.getContext();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                SinglePostViewFragment singlePostViewFragment = new SinglePostViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("post_id", fullPost.getPost_id());
                singlePostViewFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.framelayout, singlePostViewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        Date date =fullPost.getDate();

        if(date !=null){
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String add_date = dateFormat.format(date);

            holder.date.setText(add_date);
        }
        db.collection("Comments").whereEqualTo("post_id", fullPost.getPost_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    double count = 0;
                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                        count++;
                    }

                    if(count> 0){
                        holder.comments.setText("( "+formatValue(count)+")");
                    }
                    else{
                        holder.comments.setText("( 0 )");
                    }


                }
            }
        });



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

    @Override
    public int getItemCount() {
        return fullPostList.size();
    }

    public class feedHolder extends RecyclerView.ViewHolder{
        TextView type, date, body, username,comments;
        ImageView comment, bookmark, share;



        public feedHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.text_type);
            date = itemView.findViewById(R.id.text_date);
            body = itemView.findViewById(R.id.text_body);
            comment = itemView.findViewById(R.id.comment);
            comments = itemView.findViewById(R.id.comment_tv);
            bookmark = itemView.findViewById(R.id.bookmark);
            share = itemView.findViewById(R.id.tweet);
            username = itemView.findViewById(R.id.text_username);
        }
    }


}
