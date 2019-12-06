package com.hint.paranoid.aadharudhaar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FancyButton borrowButton, lendButton;
    private ClingManager mClingManager;
    String START_TUTORIAL_KEY="showcase";
    String SHOW_CASE="showCase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadButtons();
        listenToButtons();
    }

    private void listenToButtons() {
        borrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BorrowActivity.class);
                startActivity(intent);
            }
        });
        lendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LendActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadButtons() {
        borrowButton = (FancyButton) findViewById(R.id.borrow);
        lendButton = (FancyButton) findViewById(R.id.lend);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_stats) {
            Intent intent = new Intent(MainActivity.this, barchart.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {
            StartTutorial();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_save_aadhaar) {


        } else if (id == R.id.nav_send) {
            Intent i=new Intent(MainActivity.this,SaveAadhaarActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void StartTutorial()
    {
        mClingManager = new ClingManager(this);

        // When no Target is set, Target.NONE is used
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Welcome to Aadhaar Udhaar!")
                .setContent("A modern day borrow / lend record maintenance app that uses Aadhaar for verification. ")
                .build());

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Your Borrow Records")
                .setContent("Maintain records of people from whom you borrowed.")
                .setMessageBackground(Color.rgb(255,152,0))
                .setTarget(new ViewTarget(this, R.id.borrow))
                .build());

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Your Lend Records")
                .setContent("Maintain records of people who borrowed from you.")
                .setMessageBackground(Color.rgb(3,155,229))
                .setTarget(new ViewTarget(this, R.id.lend))
                .build());

        mClingManager.setCallbacks(new ClingManager.Callbacks() {
            @Override
            public boolean onClingClick(int position) {

                return false;
            }

            @Override
            public void onClingShow(int position) {
                //Toast.makeText(MainActivity.this, "Cling #" + position + " is shown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClingHide(int position) {
                // Toast.makeText(MainActivity.this, "Cling #" + position + " is hidden", Toast.LENGTH_SHORT).show();

                // Last Cling has been shown, tutorial is ended.
            }
        });

        mClingManager.start();
    }
}
