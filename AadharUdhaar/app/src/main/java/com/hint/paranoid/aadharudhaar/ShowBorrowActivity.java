package com.hint.paranoid.aadharudhaar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ShowBorrowActivity extends AppCompatActivity {
    TextView nametv,amttv,phonetv,interesttv,commenttv,datetv;
    private int position;
    SQLiteDatabase mydatabase;
    Cursor resultSet;
    Button paid;
    int month,year;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        position = Integer.parseInt(getIntent().getExtras().getString("position"))+1;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowBorrowActivity.this,EditBorrowActivity.class);
                intent.putExtra("position",Integer.toString(position));
                startActivity(intent);
            }
        });
        paid=(Button)findViewById(R.id.paid_borrow);
        loadTextViews();

        DBConnect();
        displayData();
        paid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int princi = (Integer.parseInt(amttv.getText().toString()));
                final Calendar cal = Calendar.getInstance();
                int year_x = cal.get(Calendar.YEAR);
                int month_x = cal.get(Calendar.MONTH);
                int day_x = cal.get(Calendar.DAY_OF_MONTH);
                //String paydate=day_x+"/"+month_x+"/"+year_x;
                int rate = resultSet.getInt(4);
                int totalmon;
                if (year == year_x)
                    totalmon = month_x - month;
                else {
                    totalmon = (12 - month) + month_x + (year_x - year - 1) * 12;
                }
                double interest = (princi * rate * totalmon) * 1.0 / 100;
                try {
                    mydatabase.execSQL("UPDATE borrow SET payday=" + day_x + ",paymonth=" + month_x + ",payyear=" + year_x + ",finalinterest=" + interest + " where id=" + id + " ;");
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "database query failed", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ShowBorrowActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void displayData() {
        try{
            resultSet = mydatabase.rawQuery("SELECT * FROM borrow WHERE id = "+position+ ";", null);
            resultSet.moveToFirst();
            //Toast.makeText(this, Integer.toString(row_num), Toast.LENGTH_SHORT).show();
            String name = resultSet.getString(1);
            String phone = resultSet.getString(2);
            String  amount = Integer.toString(resultSet.getInt(3));
            String interest = Integer.toString(resultSet.getInt(4));
            String date = resultSet.getString(5);
            String comments = resultSet.getString(9);
            month=resultSet.getInt(7);
            year=resultSet.getInt(8);
            id=resultSet.getInt(0);
            nametv.setText(name);
            phonetv.setText(phone);
            amttv.setText(amount);
            interesttv.setText(interest+" %");
            commenttv.setText(comments);
            datetv.setText(date);
        } catch(SQLException e)
        {
            Toast.makeText(this, "failed to display.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void loadTextViews()
    {
        nametv = (TextView) findViewById(R.id.borrow_name);
        phonetv = (TextView) findViewById(R.id.borrow_phone);
        amttv = (TextView) findViewById(R.id.amt_borrow);
        interesttv = (TextView) findViewById(R.id.interest_borrow);
        commenttv = (TextView) findViewById(R.id.comments_borrow);
        datetv = (TextView) findViewById(R.id.date_borrow);
    }
    private void DBConnect() {
        mydatabase = openOrCreateDatabase("MoneyDB",MODE_PRIVATE,null);
    }

}
