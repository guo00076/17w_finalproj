package com.teamwork.final_project;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.teamwork.final_project.kitchenDatabaseHelper.KEY_ID;
import static com.teamwork.final_project.kitchenDatabaseHelper.KEY_SETTING;
import static com.teamwork.final_project.kitchenDatabaseHelper.TableName;

/**
 * The class for light device
 * Jiawei Guo
 */
public class kitchen_main_light extends Activity {

    EditText ed_light;
    Button b_dim;
    String light_set;
    Switch sh;
    ProgressBar bar;
    kitchenDatabaseHelper kdb;
    SQLiteDatabase db;
    ContentValues cv;
    Button b_disave;
    TextView n_l;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_activity_main_light);


        ed_light = (EditText) findViewById(R.id.light_dim);
        b_dim = (Button) findViewById(R.id.light_adjust);
        sh = (Switch)findViewById(R.id.light_switch);
        bar = (ProgressBar) findViewById(R.id.pros_bar);
        n_l = (TextView)findViewById(R.id.l_name);
        b_disave=(Button)findViewById(R.id.light_change);
        bar.setVisibility(View.VISIBLE);

        kdb = new kitchenDatabaseHelper(this.getBaseContext());
        db = kdb.getWritableDatabase();
        //The switch object is set to show different information when it is checked or not.
        sh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LayoutInflater li = getLayoutInflater();
                    View v = li.inflate(R.layout.kitchen_light_toast, (ViewGroup)findViewById(R.id.toast_layout_on));
                    Toast toast = new Toast(kitchen_main_light.this);
                    toast.setView(v);
                    toast.show();
                }else{
                    LayoutInflater l = getLayoutInflater();
                    View v_off = l.inflate(R.layout.kitchen_light_toast_off, (ViewGroup)findViewById(R.id.toast_layout_off));
                    Toast toast = new Toast(kitchen_main_light.this);
                    toast.setView(v_off);
                    toast.show();
                }
            }
        });

        //To show values of the initial user entry when add the devices
        light_set = this.getIntent().getStringExtra("dim");
        ed_light.setText(light_set);
        //To adjust the light and check if the value entered is valid
        b_dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.INVISIBLE);
                try {
                    Double.valueOf(light_set);
                } catch (NumberFormatException e) {
                    Toast.makeText(kitchen_main_light.this, "Please enter setting value", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(kitchen_main_light.this, "The light set to " + light_set + " %", Toast.LENGTH_SHORT).show();
            }
        });
        //execute the asyncTask of load the setting value from the previous activity's intent
        new LoadQuery().execute();
        //Let users save let settings when there is any change
        b_disave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_set = ed_light.getText().toString();
                String d = kitchen_main_light.this.getIntent().getStringExtra("di");
                Updateset(d, light_set);
                Toast.makeText(v.getContext(), "The set has been updated ", Toast.LENGTH_LONG).show();
            }
        });

        n_l.setText(this.getIntent().getStringExtra("n"));

    }


    class LoadQuery extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... args) {
            light_set = getIntent().getStringExtra("dim");
            publishProgress(100);
            return light_set;
        }

        protected void onProgressUpdate(Integer... values) {
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(values[0]);
        }

        protected void onPostExecute(String result) {
            bar.setVisibility(View.VISIBLE);
            ed_light.setText(light_set);

        }


    }
    //The method used to upate the database when there is any change in light settings
    //in this class
    private void Updateset(String id, String set_num){
        cv = new ContentValues();
        cv.put(KEY_SETTING, set_num);
        cv.put(KEY_ID, id);

        db.update(TableName, cv, KEY_ID + "=" + id, null);
    }
}
