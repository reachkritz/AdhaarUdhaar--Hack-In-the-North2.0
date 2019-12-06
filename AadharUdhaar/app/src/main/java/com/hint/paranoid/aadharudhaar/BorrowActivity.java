package com.hint.paranoid.aadharudhaar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BorrowActivity extends AppCompatActivity {
    ListView lview;
    Activity context;
    ListViewAdapter lviewAdapter = null;
    ArrayList<RowData> arrayList = new ArrayList<RowData>();
    SQLiteDatabase mydatabase;
    Cursor resultSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowActivity.this, AddBorrowActivity.class);
                startActivity(intent);
            }
        });
        DBConnect();
        createList();
        if (lview != null){
            lview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(android.widget.AdapterView<?> parent,
                                        View view, int position, long id) {
                    Intent intent = new Intent(BorrowActivity.this, ShowBorrowActivity.class);
                    intent.putExtra("position", Integer.toString(position));
                    startActivity(intent);
                }
            });
    }


    }
    private void DBConnect() {
        mydatabase = openOrCreateDatabase("MoneyDB",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "borrow(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar NOT NULL,phone varchar NOT NULL,amount integer NOT NULL,interest integer,date varchar NOT NULL,day integer,month integer,year integer,comments varchar,payday integer,paymonth integer,payyear integer,finalinterest double);");

    }
    private void createList() {
        try {
            resultSet = mydatabase.rawQuery("SELECT * FROM borrow WHERE payday=-1;", null);
            if(resultSet.moveToFirst())
            {
                do{
                    int pid = resultSet.getInt(0);
                    String name = resultSet.getString(1);
                    String amt = resultSet.getString(3);
                    String date = resultSet.getString(5);
                    arrayList.add(new RowData(name,amt,date));
                }while(resultSet.moveToNext());
                lview = (ListView) findViewById(R.id.borrowList);
                ListViewAdapter adapter = new ListViewAdapter(this, arrayList,2);
                lview.setAdapter(adapter);
            }
        }
        catch(SQLException e)
        {
            Toast.makeText(context, "failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
