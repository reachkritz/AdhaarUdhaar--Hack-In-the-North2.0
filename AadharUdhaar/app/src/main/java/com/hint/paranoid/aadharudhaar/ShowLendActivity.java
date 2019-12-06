package com.hint.paranoid.aadharudhaar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ShowLendActivity extends AppCompatActivity {
    private int position;
    SQLiteDatabase mydatabase;
    Cursor resultSet;
    TextView nametv,amttv,phonetv,interesttv,uidtv,addrtv,statetv,commenttv,pintv,datetv;
    Button call,sms,paid;
    String phoneNo;
    int id,month,year;
    String sms_text="Reminder: You owe me ";
    private int MY_PERMISSIONS_REQUEST_SMS=123;
    private int MY_PERMISSIONS_REQUEST_CALL=124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Buttons starts
        call=(Button)findViewById(R.id.call_lend);
        sms=(Button)findViewById(R.id.sms_lend);
        paid=(Button)findViewById(R.id.paid_lend);
        position = Integer.parseInt(getIntent().getExtras().getString("position"))+1;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowLendActivity.this,EditLendActivity.class);
                intent.putExtra("position",Integer.toString(position));
                startActivity(intent);
            }
        });
        loadTextViews();
        DBConnect();
        displayData();
        sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                phoneNo = phonetv.getText().toString();
                sms_text = sms_text + amttv.getText().toString() + " (with interest if applicable).Your due date is " + datetv.getText().toString() + ".";
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms_text.toString(), null, null);

                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        paid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int princi=(Integer.parseInt(amttv.getText().toString()));
                final Calendar cal = Calendar.getInstance();
                int year_x = cal.get(Calendar.YEAR);
                int month_x = cal.get(Calendar.MONTH);
                int day_x=cal.get(Calendar.DAY_OF_MONTH);
                //String paydate=day_x+"/"+month_x+"/"+year_x;
                int rate=resultSet.getInt(4);
                int totalmon;
                if(year==year_x)
                    totalmon=month_x-month;
                else
                {
                    totalmon=(12-month)+month_x+(year_x-year-1)*12;
                }
                double interest=(princi*rate*totalmon)*1.0/100;
                try{
                    mydatabase.execSQL("UPDATE lend SET payday="+day_x+",paymonth="+month_x+",payyear="+year_x+",finalinterest="+interest+" where id="+id+ " ;");
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "database query failed", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ShowLendActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });

        if (ContextCompat.checkSelfPermission(ShowLendActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowLendActivity.this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(ShowLendActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SMS);
            }
        }

        if (ContextCompat.checkSelfPermission(ShowLendActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowLendActivity.this,
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(ShowLendActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            }
        }



    }
    private void loadTextViews()
    {
        nametv = (TextView) findViewById(R.id.lend_name);
        phonetv = (TextView) findViewById(R.id.lend_phone);
        amttv = (TextView) findViewById(R.id.amt_lend);
        interesttv = (TextView) findViewById(R.id.interest_lend);
        commenttv = (TextView) findViewById(R.id.comments_lend);
        uidtv = (TextView) findViewById(R.id.uid);
        addrtv = (TextView) findViewById(R.id.address);
        statetv = (TextView) findViewById(R.id.state);
        pintv = (TextView) findViewById(R.id.postal);
        datetv = (TextView) findViewById(R.id.date_lend);
    }

    private void displayData() {
        try{
            resultSet = mydatabase.rawQuery("SELECT * FROM lend WHERE id = "+position+ ";", null);
            resultSet.moveToFirst();
            id=resultSet.getInt(0);
            //Toast.makeText(this, Integer.toString(row_num), Toast.LENGTH_SHORT).show();
            String name = resultSet.getString(1);
            String phone = resultSet.getString(2);
            String  amount = Integer.toString(resultSet.getInt(3));
            String interest = Integer.toString(resultSet.getInt(4));
            String date = resultSet.getString(5);

            month=resultSet.getInt(7);
            year=resultSet.getInt(8);

            String comments = resultSet.getString(9);
            String uid = resultSet.getString(10);
            String address = resultSet.getString(11);
            String state = resultSet.getString(12);
            String pin = Integer.toString(resultSet.getInt(13));
            nametv.setText(name);
            phonetv.setText(phone);
            amttv.setText(amount);
            interesttv.setText(interest+" %");
            commenttv.setText(comments);
            datetv.setText(date);
            uidtv.setText(uid);
            addrtv.setText(address);
            statetv.setText(state);
            pintv.setText(pin);
        } catch(SQLException e)
        {
            Toast.makeText(this, "failed to display.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void DBConnect() {
        mydatabase = openOrCreateDatabase("MoneyDB",MODE_PRIVATE,null);
    }
    public void Call(View v){
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + phonetv.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(call);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode==MY_PERMISSIONS_REQUEST_SMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getBaseContext(),"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getBaseContext(),"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show();
            }
            return;
        }

    }

}
