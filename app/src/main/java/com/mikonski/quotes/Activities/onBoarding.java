package com.mikonski.quotes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikonski.quotes.Adapters.SliderAdapter;
import com.mikonski.quotes.R;

public class onBoarding extends AppCompatActivity {
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout linearLayout;
    private TextView[] textViews;
    private ImageView next, back;
    private int currentPage;
    private Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        linearLayout = findViewById(R.id.dots);
        skip = findViewById(R.id.skip);

        sliderAdapter = new SliderAdapter(this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(sliderAdapter);

        next = findViewById(R.id.next);
        back = findViewById(R.id.back);

        addDots(0);

        viewPager.addOnPageChangeListener(listener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage+1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage-1);
            }
        });
    }

    private void addDots(int position) {
        textViews = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < textViews.length;i++){
            textViews[i] = new TextView(this);
            textViews[i].setText(Html.fromHtml("&#8226;"));
            textViews[i].setTextColor(getResources().getColor(R.color.colorAccent));
            textViews[i].setTextSize(35);

            linearLayout.addView(textViews[i]);
        }
        if(textViews.length > 0){

            textViews[position].setTextSize(45);

        }
    }
    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

            currentPage = position;
            if(position ==0){

                back.setVisibility(View.GONE);
            }


          else if (position == textViews.length-1){
                skip.setEnabled(true);
                skip.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent login = new Intent(onBoarding.this,LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(login);
                    }
                });

            }
            else {
                next.setVisibility(View.VISIBLE);
               back.setVisibility(View.VISIBLE);
               skip.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
