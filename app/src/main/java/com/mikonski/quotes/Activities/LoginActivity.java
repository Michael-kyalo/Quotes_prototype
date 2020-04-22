package com.mikonski.quotes.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikonski.quotes.Models.User;
import com.mikonski.quotes.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static int DEFAULT_SIGNIN = 1;
    private static int RC_SIGN_IN =2;
    private TextView textView_login;
    private Button signInButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LottieAnimationView lottieAnimationView , Failed_anim, loading_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check first run to redirect to onboarding
        Boolean isfirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        if(isfirstRun){
            Log.d(TAG, "onCreate: " + isfirstRun);
            Intent onBoard = new Intent(LoginActivity.this, onBoarding.class);
            onBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(onBoard);
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
        Log.d(TAG, "onCreate: " + isfirstRun);

        /*
        GOOGLE SIGNIN
         */
        mAuth = FirebaseAuth.getInstance();



        GoogleSignInOptions google = new GoogleSignInOptions. Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.DEFAULT_CLIENT_ID))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,google);

        lottieAnimationView = findViewById(R.id.login_anim);
        Failed_anim = findViewById(R.id.login_failed);
        loading_anim = findViewById(R.id.login_anim_loading);
        textView_login = findViewById(R.id.textlogin);

        signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });


    }

    private void signin() {
        signInButton.setVisibility(View.INVISIBLE);
        textView_login.setText(getString(R.string.signin));
        lottieAnimationView.setVisibility(View.GONE);
        loading_anim.setVisibility(View.VISIBLE);
        Failed_anim.setVisibility(View.GONE);

        Intent intent  = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                assert account != null;
                firebaseauth(account);

            }
            catch (ApiException e) {
                signInButton.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(View.GONE);
                loading_anim.setVisibility(View.GONE);
                Failed_anim.setVisibility(View.VISIBLE);
                textView_login.setText(getString(R.string.signinfailed));
                Log.d(TAG, "onActivityResult: Signin failed" + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void firebaseauth(final GoogleSignInAccount account) {
        Log.d(TAG, "firebaseauth: "+ account.getId());

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    /**
                     * task is successfull send to main activity after checking if profile is set
                     *
                     *
                     */
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    User user = new User();

                    user.setImage(Objects.requireNonNull(account.getPhotoUrl()).toString());
                    user.setUsername(account.getDisplayName());
                    assert firebaseUser != null;
                    user.setUid(firebaseUser.getUid());

                    db.collection("Users").document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                            else {
                                signInButton.setVisibility(View.VISIBLE);
                                lottieAnimationView.setVisibility(View.GONE);
                                loading_anim.setVisibility(View.GONE);
                                Failed_anim.setVisibility(View.VISIBLE);
                                textView_login.setText(getString(R.string.signinfailed));

                                Log.d(TAG, "onComplete: " + task.getException());
                            }
                        }
                    });





                }
                else {
                    signInButton.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                    loading_anim.setVisibility(View.GONE);
                    Failed_anim.setVisibility(View.VISIBLE);
                    textView_login.setText(getString(R.string.signinfailed));

                    Log.d(TAG, "onComplete: " + task.getException());
                }

            }
        });
    }

}
