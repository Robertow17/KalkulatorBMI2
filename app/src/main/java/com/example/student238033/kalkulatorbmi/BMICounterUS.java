package com.example.student238033.kalkulatorbmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class BMICounterUS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicounter_us);

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
            Toast.makeText(BMICounterUS.this, "Dane zostały zapisane!", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.action_about_me)
        {
            Intent i = new Intent(BMICounterUS.this, AboutMe.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnClickListenerForButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ibs = (EditText) findViewById(R.id.weight);
                EditText ft = (EditText) findViewById(R.id.feets);
                EditText ins = (EditText) findViewById(R.id.inches);

                double mass = getDoubleValueFromEditText(ibs);
                mass=convertToKg(mass);
                int feets=getIntegerValueFromEditText(ft);
                int inches=getIntegerValueFromEditText(ins);
                double growth = getGrowth(feets, inches);
                viewAnswer(mass, feets, inches, growth);
            }
        });
    }

    private void setOnCheckedChangeListenerForSwitch(Switch s) {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    Intent i = new Intent(BMICounterUS.this, BMICounter.class);
                    startActivity(i);

                    Toast.makeText(BMICounterUS.this, "Zmieniono jednostki!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewAnswer(double mass, int feets, int inches, double tall) {
        if(mass<0 || mass>350 || tall<0 || tall>3 || inches<0 || inches>=12 || feets<0 || feets>10 )
        {
            Intent i = new Intent(BMICounterUS.this, WrongValues.class);
            startActivity(i);
        }
        else
        {
            double result = countBMI(mass,tall);
            Intent i=new Intent(BMICounterUS.this, BMIViewer.class).putExtra("result", String.format("%.3f", result));
            startActivity(i);
        }
    }

    private double getGrowth(int feets, int inches) {
        double growth;
        try
        {
            growth=convertToM(feets,inches);
        }
        catch(IllegalArgumentException e)
        {
            growth=-0.1;
        }
        return growth;
    }

    public double getDoubleValueFromEditText(EditText e)
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

    public int getIntegerValueFromEditText(EditText e)
    {
        int result;
        try
        {
            result=Integer.parseInt(e.getText().toString());
        }
        catch(NumberFormatException ex)
        {
            result=-1;
        }
        return result;
    }

    private void getSavedValues() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("PREFS_NAME", 0);
        String ibs = settings.getString("saved_weight_ibs", "");
        String feet = settings .getString("saved_height_feet", "");
        String inches = settings .getString("saved_height_inches", "");
        EditText weight = (EditText) findViewById(R.id.weight);
        EditText heightf = (EditText) findViewById(R.id.feets);
        EditText heighti = (EditText) findViewById(R.id.inches);
        if(!ibs.isEmpty())
        {
            weight.setText(ibs);
        }
        if(!feet.isEmpty())
        {
            heightf.setText(feet);
        }
        if(!inches.isEmpty())
        {
            heighti.setText(inches);
        }
    }

    private void saveValues() {
        EditText weight = (EditText) findViewById(R.id.weight);
        EditText heightf = (EditText) findViewById(R.id.feets);
        EditText heighti = (EditText) findViewById(R.id.inches);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("saved_weight_ibs", weight.getText().toString());
        editor.putString("saved_height_feet", heightf.getText().toString());
        editor.putString("saved_height_inches", heighti.getText().toString());
        editor.apply();
    }

    public double squared(double height)
    {
        double result = height*height;
        return result;
    }
    public double countBMI(double mass, double height) throws IllegalArgumentException
    {
        if(mass<=0 || height<=0) throw new IllegalArgumentException();
        else {
            double result = mass / squared(height);
            return result;
        }
    }

    public double convertToKg(double mass) throws IllegalArgumentException
    {
        if(mass<=0) throw new IllegalArgumentException();
        else {
            double result = mass * 0.45359;
            return result;
        }
    }
    public double convertToM(int feets, int inches) throws IllegalArgumentException
    {
        if(feets<=0 || inches<0) throw new IllegalArgumentException();
        else
        {
            double result = feets * 0.3048 + inches * 0.0254;
            return result;
        }
    }
}

