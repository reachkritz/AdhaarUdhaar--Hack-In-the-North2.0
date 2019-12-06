package com.hint.paranoid.aadharudhaar;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

//import com.github.mikephil.charting.utils.Highlight;


public class piechart extends AppCompatActivity implements OnChartValueSelectedListener {
    private GestureDetectorCompat gestureDetectorCompat;
    SQLiteDatabase mydatabase;
    Cursor resultset,resultset2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());
        mydatabase = openOrCreateDatabase("MoneyDB", MODE_PRIVATE, null);
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        final Calendar cal = Calendar.getInstance();
        int curr_year = cal.get(Calendar.YEAR);
        try {

            resultset= mydatabase.rawQuery("SELECT month,SUM(finalinterest) FROM lend WHERE year="+curr_year+" AND paymonth!=-1 GROUP BY paymonth ;",null);
            Log.d("check1", resultset.getCount() + "");

            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "database query failed", Toast.LENGTH_SHORT).show();
        }
        try {

            resultset2= mydatabase.rawQuery("SELECT paymonth,SUM(finalinterest) FROM borrow WHERE year="+curr_year+" AND paymonth!=-1 GROUP BY paymonth ;",null);
            Log.d("check2", resultset2.getCount() + "");

            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }catch (SQLException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "database query failed", Toast.LENGTH_SHORT).show();
        }
        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        int hash[]=new int[12];
        if(resultset.moveToFirst())
        {
            do{
                int mn=resultset.getInt(0);
                int cnt=resultset.getInt(1);
                Log.d("check","mon="+mn+"count="+cnt);
                hash[mn]=cnt;
                // entries.add(new Entry(cnt, mn));
            }while(resultset.moveToNext());
        }
        if(resultset2.moveToFirst())
        {
            do{
                int mn=resultset2.getInt(0);
                int cnt=resultset2.getInt(1);
                Log.d("check","mon="+mn+"count="+cnt);
                hash[mn]=-cnt;
                // entries.add(new Entry(cnt, mn));
            }while(resultset2.moveToNext());
        }
        ArrayList<Integer> colors = new ArrayList<Integer>();
        ArrayList<String> xVals = new ArrayList<String>();
        String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Jul","Aug","Sep","Oct","Nov","Dec"};
        for(int i=0;i<12;i++)
        {
            if(hash[i]<0) {
                xVals.add(months[i]+"(loss)");
                colors.add(Color.rgb(192, 0, 0));
                yvalues.add(new Entry(-hash[i], i));
            }
            else if(hash[i]>0){
                xVals.add(months[i]+"(profit)");
                colors.add(Color.rgb(0,255,0));
                yvalues.add(new Entry(hash[i], i));
            }

        }


        PieDataSet dataSet = new PieDataSet(yvalues, "Revenue");



        xVals.add("December");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDescription("Monthly profit/loss");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe left' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

         /*
         Toast.makeText(getBaseContext(),
          event1.toString() + "\n\n" +event2.toString(),
          Toast.LENGTH_SHORT).show();
         */

            if (event2.getX() < event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe left - startActivity()",
                        Toast.LENGTH_SHORT).show();

                //switch another activity
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }

            return true;
        }
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}