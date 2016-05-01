package com.example.grace.bilingualspellcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] languages = {"English", "Spanish", "French", "Polish", "German"};


        ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        languages);
        Spinner langSetter1 =
                (Spinner)  findViewById(R.id.lang1);
        langSetter1.setAdapter(stringArrayAdapter);
        //langSetter1.setOnItemSelectedListener(onSpinner1);

        Spinner langSetter2 =
                (Spinner)  findViewById(R.id.lang2);
        langSetter2.setAdapter(stringArrayAdapter);
        //langSetter1.setOnItemSelectedListener(onSpinner2);


    }


    private EditText favcolor;
    private TextView textout;

    public void buttonOnClick (View v) {
 // do something when button is clicked
        Button Check=(Button) v;
        // startActivity(nev Intent (getApplicationContext(), Activity2.class));
        favcolor = (EditText) findViewById(R.id.editText);
        Spinner langSetter1 =
                (Spinner)  findViewById(R.id.lang1);
        Spinner langSetter2 =
                (Spinner)  findViewById(R.id.lang2);


        textout = (TextView) findViewById (R.id.txtOutput);
        textout.setText (Algorithm.check(langSetter1.getSelectedItem().toString(),
                langSetter2.getSelectedItem().toString(), favcolor.toString()));
        ((Button) v).setText("CHECKED");

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
}
