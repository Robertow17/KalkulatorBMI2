package com.example.student238033.kalkulatorbmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class BMICounter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicounter);

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_main_menu);
        setSupportActionBar(myToolbar);

        getSavedValues();

        Switch s = (Switch) findViewById(R.id.change_unit);
        setOnCheckedChangeListenerForSwitch(s);

        final Button button = (Button) findViewById(R.id.count);
        setOnClickListenerForButton(button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_save)
        {
            saveValues();
            Toast.makeText(BMICounter.this, "Dane zostały zapisane!", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.action_about_me)
        {
            Intent i = new Intent(BMICounter.this, AboutMe.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnCheckedChangeListenerForSwitch(Switch s) {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent i = new Intent(BMICounter.this, BMICounterUS.class);
                    startActivity(i);

                    Toast.makeText(BMICounter.this, "Zmieniono jednostki!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnClickListenerForButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText weight = (EditText) findViewById(R.id.weight);
                EditText height = (EditText) findViewById(R.id.growth);

                double mass = getValueFromEditText(weight);
                double tall = getValueFromEditText(height);
                viewAnswer(mass, tall);
            }
        });
    }

    private void viewAnswer(double mass, double tall) {
        if (mass < 0 || mass > 350 || tall < 0 || tall > 3) {
            Intent i = new Intent(BMICounter.this, WrongValues.class);
            startActivity(i);
        }
        else
        {
            double result = countBMI(mass, tall);
            Intent i = new Intent(BMICounter.this, BMIViewer.class).putExtra("result", String.format("%.3f", result));
            startActivity(i);
        }
    }

    private double getValueFromEditText(EditText e)
    {
        double result;
        try
        {
           result=Double.parseDouble(e.getText().toString());
        }
        catch(NumberFormatException ex)
        {
            result=-0.1;
        }
        return result;
    }

    private void saveValues() {
        EditText weight = (EditText) findViewById(R.id.weight);
        EditText height = (EditText) findViewById(R.id.growth);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("saved_weight", weight.getText().toString());
        editor.putString("saved_height", height.getText().toString());
        editor.apply();
    }

    private void getSavedValues() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("PREFS_NAME", 0);
        String mass = settings.getString("saved_weight", "");
        String growth = settings .getString("saved_height", "");
        EditText weight = (EditText) findViewById(R.id.weight);
        EditText height = (EditText) findViewById(R.id.growth);
        if(!mass.isEmpty())
        {
            weight.setText(mass);
        }
        if(!growth.isEmpty())
        {
            height.setText(growth);
        }
    }

    public double squared(double height)
    {
        return height*height;
    }

    public double countBMI(double mass, double height) throws IllegalArgumentException
    {
        if(mass<=0 || height<=0) throw new IllegalArgumentException();
        else {
            double result = mass / squared(height);
            return result;
        }
    }
}