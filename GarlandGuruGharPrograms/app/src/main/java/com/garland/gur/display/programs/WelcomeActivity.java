package com.garland.gur.display.programs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[]dotstv;
    private int[]layouts;
    private Button btnNext;
    private MyPagerAdapter pagerAdapter;

    private List<TVShow> movies=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("prgms");
       // DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        //TVShow artist = new TVShow("www", "12/12/2000");
        //myRef.setValue(artist);


        setStatusBarTransparent();

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem()+1;
                if(currentPage < layouts.length) {
                    //move to next page
                    viewPager.setCurrentItem(currentPage);
                } else {
                    startMainActivity();
                }
            }
        });



        layouts = new int[]{R.layout.slider_1,R.layout.slider_2, R.layout.slider_3, R.layout.slider_4};
        pagerAdapter = new MyPagerAdapter(layouts, getMovies(), getApplicationContext());

        viewPager.setAdapter(pagerAdapter);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int currentPage = viewPager.getCurrentItem()+1;
                if(currentPage < layouts.length) {
                    //move to next page
                    viewPager.setCurrentItem(currentPage);
                } else {
                    viewPager.setCurrentItem(0);

                    //startMainActivity();
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        //End




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length-1){
                   //LAST PAGE
                    btnNext.setText("START");

                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private boolean isFirstTimeStartApp() {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("GarlandGuruGhar_Prgms_Retreved", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag", true);
    }

    private void setFirstTimeStartStatus(boolean stt) {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("GarlandGuruGhar_Prgms_Retreved", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv =new TextView[layouts.length];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        //Set current dot active
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private void startMainActivity(){
        setFirstTimeStartStatus(false);
       // startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
       // finish();
    }
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }


    private List<TVShow> getHardCodedMovies() {
        //SINGLE MOVIE
        TVShow tvShow = new TVShow("Fri","Manjeet","2014-09-10");

        //ADD ITR TO COLLECTION
        movies.add(tvShow);

        return movies;
    }

    private List<TVShow> getMovies() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int year1;

        int month = c.get(Calendar.MONTH)+1;
        int month1;
        if(month==12){
            month1 = 1;
            year1 = year+1;
        }else {
            month1 = c.get(Calendar.MONTH)+2;
            year1 = year;
        }

        String strMonth;
        String strMonth1;
        if (month < 10) {
            NumberFormat f = new DecimalFormat("00");
            strMonth = String.valueOf(f.format(month));
            strMonth1 = String.valueOf(f.format(month1));
        }else{
            strMonth = String.valueOf(month);
            strMonth1 = String.valueOf(month1);
        }


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.d("WelcomeActivity", "$$$$$$$$$$ MONTH YEAR - "+strMonth +"- "+year);
        //ref.child("user_id").orderByKey().startAt("id_001125")
        //database.child("prgms/201809").addValueEventListener(new ValueEventListener() {
        database.child("prgms").orderByKey().startAt(year+""+strMonth).endAt(year1+""+strMonth1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    TVShow note = noteDataSnapshot.getValue(TVShow.class);
                    movies.add(note);
                }
                */
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {

                    DataSnapshot next = (DataSnapshot) iterator.next();
                    Log.i("WELECOME ", "Key = " + next.getKey());
                    Iterator<DataSnapshot> iterator1=  next.getChildren().iterator();
                    while (iterator1.hasNext()) {
                        DataSnapshot next1 = (DataSnapshot) iterator1.next();
                        Log.i("WELECOME ", "Value = " + next1.child("title").getValue());
                        TVShow note = next1.getValue(TVShow.class);
                        movies.add(note);

                    }
                }
               // pagerAdapter.updateList(notes);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               // hideProgressDialog();
            }

        });
        return movies;
    }

    @Override
    public String toString() {
        String title="Crime";
        return title;
    }

}
