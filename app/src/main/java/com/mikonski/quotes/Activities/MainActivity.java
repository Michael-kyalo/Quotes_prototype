package com.mikonski.quotes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikonski.quotes.Fragments.AddFragment;
import com.mikonski.quotes.Fragments.BookmarkFragment;
import com.mikonski.quotes.Fragments.HomeFragment;
import com.mikonski.quotes.Fragments.MagazineFragment;
import com.mikonski.quotes.Fragments.RequestsFragment;
import com.mikonski.quotes.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.performClick();


        if(user == null){
            Intent login = new Intent(MainActivity.this,LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
        }



        position = bottomNavigationView.getVerticalScrollbarPosition();
        Log.d(TAG, "onCreate: "+ position);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //replace and commit
            fragmentTransaction.replace(R.id.framelayout, new HomeFragment());
            fragmentTransaction.commit();




    }

    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: loading fragments");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add animation
        fragmentTransaction.setCustomAnimations(R.animator.fragment_fade_in,R.animator.fragment_fade_out);

        //replace and commit
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            switch (id){
                case R.id.menu_add:
                    loadFragment(new AddFragment());
                    return true;
                case  R.id.menu_bookmarks:
                    loadFragment(new BookmarkFragment());
                    return true;
                case R.id.menu_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.menu_magazine:
                    loadFragment(new MagazineFragment());
                    return true;
                case R.id.menu_requests:
                    loadFragment(new RequestsFragment());
                    return true;
            }
            return true;
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
    }
}
