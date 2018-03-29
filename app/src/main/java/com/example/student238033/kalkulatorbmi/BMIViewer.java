package com.example.student238033.kalkulatorbmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class BMIViewer extends AppCompatActivity {

    final static double underweightBorden = 18.5;
    final static double correctWeightBorden = 25.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String s = getIntent().getStringExtra("result");
        chooseLayoutInCaseOfBMI(s);

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(myToolbar);

        TextView result = (TextView) findViewById(R.id.bmi);
        result.setText(s);
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_viewer, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_return) {
            Intent i = new Intent(BMIViewer.this, BMICounter.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseLayoutInCaseOfBMI(String result) throws IllegalArgumentException
    {
        try
        {
            double bmi = Double.valueOf(result.replaceAll(",", "."));
            if (bmi < underweightBorden) {
                setContentView(R.layout.underweight);
            }
            if (bmi > underweightBorden && bmi < correctWeightBorden) {
                setContentView(R.layout.correct);
            }
            if (bmi > correctWeightBorden) {
                setContentView(R.layout.overweight);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException();
        }
    }
}
