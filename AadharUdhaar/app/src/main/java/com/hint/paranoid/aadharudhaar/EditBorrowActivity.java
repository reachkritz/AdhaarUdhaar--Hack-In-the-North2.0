package com.hint.paranoid.aadharudhaar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditBorrowActivity extends AppCompatActivity {
    EditText nametv,amttv,phonetv,interesttv,commenttv;
    private int position;
    TextView datetv;
    int year_x,month_x,day_x,week_x,curr_year,curr_month,curr_day;
    private TextView result_tv;
    //private EditText name,phone,amt,interest,comment,date;
    private TextView datex;
    private int amtInt, interestInt;
    private String nameString,phoneString,commentString,dateString,amtString,interestString;
    SQLiteDatabase mydatabase;
    Cursor resultSet;
    Button updateButton;
    int flag=0;
    Button btn;
    static final int DIALOG_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_borrow);
        loadTextViews();

        final Calendar cal = Calendar.getInstance();
        curr_year=year_x=cal.get(Calendar.YEAR);
        curr_month=month_x=cal.get(Calendar.MONTH) ;
        curr_day=day_x=cal.get(Calendar.DAY_OF_MONTH);
        week_x=cal.get(Calendar.WEEK_OF_YEAR);
        result_tv=(TextView)findViewById(R.id.date_borrow_edit);
        showDialogOnButtonClick();

        position = Integer.parseInt(getIntent().getExtras().getString("position"));
        DBConnect();
        displayData();
        update();
    }
    public void showDialogOnButtonClick(){
        btn = (Button)findViewById(R.id.cal_borrow_edit);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }

        );
    }
    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x=year;
            month_x=monthOfYear ;
            day_x=dayOfMonth;
            Calendar c =new GregorianCalendar();
            c.set(Calendar.YEAR,year_x);
            c.set(Calendar.MONTH,monthOfYear);
            c.set(Calendar.DAY_OF_MONTH,day_x);
            week_x = c.get(Calendar.WEEK_OF_YEAR);
            Toast.makeText(EditBorrowActivity.this, day_x + "/" + month_x + "/" + year_x + "/" + week_x, Toast.LENGTH_LONG).show();
            result_tv.setText(day_x + "/" + (month_x+1) + "/" + year_x);

            //duedate=(String)result_tv.getText();
        }
    };
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
            nametv.setText(name);
            phonetv.setText(phone);
            amttv.setText(amount);
            interesttv.setText(interest);
            commenttv.setText(comments);
            result_tv.setText(date);
           // datetv.setText(date);
        } catch(SQLException e)
        {
            Toast.makeText(this, "failed to display.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void loadTextViews()
    {
        nametv = (EditText) findViewById(R.id.borrow_name_edit);
        phonetv = (EditText) findViewById(R.id.borrow_phone_edit);
        amttv = (EditText) findViewById(R.id.amt_borrow_edit);
        interesttv = (EditText) findViewById(R.id.interest_borrow_edit);
        commenttv = (EditText) findViewById(R.id.comments_borrow_edit);
        //datetv = (TEx) findViewById(R.id.date_borrow_edit);
    }
    private void DBConnect() {
        mydatabase = openOrCreateDatabase("MoneyDB",MODE_PRIVATE,null);
    }

    private void update()
    {
        updateButton = (Button) findViewById(R.id.update_borrow_edit);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                updateInput();
                if(flag == 0) {
                    Intent intent = new Intent(EditBorrowActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void updateInput()
    {
        if(validate())
        {
            try {
                final Calendar cal = Calendar.getInstance();
                int year_x=cal.get(Calendar.YEAR);
                int month_x=cal.get(Calendar.MONTH);
                int day_x=cal.get(Calendar.DAY_OF_MONTH);
                int week_x=cal.get(Calendar.WEEK_OF_YEAR);
                String query = "UPDATE borrow SET name='"+nameString+"',phone='"+phoneString+"',amount="+amtInt+",interest="+interestInt+",date='"+dateString+"',day="+day_x+","+"month="+month_x+",year="+year_x+",comments='"+commentString+"' WHERE id="+position+";" ;
                mydatabase.execSQL(query);
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            }catch (SQLException e)
            {
                e.printStackTrace();
                Toast.makeText(this, "database update failed", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            nametv.setText("");
            phonetv.setText("");
            amttv.setText("");
            interesttv.setText("");
            commenttv.setText("");
            datetv.setText("");
            Toast.makeText(this, "Invalid entry", Toast.LENGTH_SHORT).show();
            flag = 1;
        }
    }
    private boolean validate() {

        if(nameString.equals(""))
        {
            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            return false;

        }
        if(phoneString.length()!=10)
        {
            //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(amtString.equals(""))
        {
            //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isDateValid(dateString))
        {
            Toast.makeText(this, "due date has passed!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    public boolean isDateValid(String date)
    {

        if(year_x==curr_year){
            if(month_x==curr_month){
                if(day_x<curr_day){
                    return false;
                }
            }else if(month_x<curr_month)
                return false;
        }
        else if(year_x<curr_year)
            return false;
        return true;
    }
    private void getInput()
    {
        nameString = nametv.getText().toString();
        phoneString = phonetv.getText().toString();
        amtString = amttv.getText().toString();
        if(!amtString.equals(""))
        {
            amtInt = Integer.parseInt(amtString);
        }
        interestString = interesttv.getText().toString();
        if(!interestString.equals(""))
            interestInt = Integer.parseInt(interestString);
        commentString = commenttv.getText().toString();
       // dateString = datetv.getText().toString();
    }
}
