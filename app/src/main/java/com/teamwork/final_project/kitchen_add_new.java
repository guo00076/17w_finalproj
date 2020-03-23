package com.teamwork.final_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This class is used to achieve the function of adding devices
 * to kitchen's main listview.
 * Jiawei Guo
 */
public class kitchen_add_new extends AppCompatActivity {

    TextView type;
    EditText name;
    EditText set_num;
    Button set;
    ProgressBar bar;
    protected static String ActivityName = "kitchen_add_new";
    kitchenDatabaseHelper kdb;
    SQLiteDatabase db;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_add_new);

        kdb = new kitchenDatabaseHelper(getBaseContext());
        db = kdb.getWritableDatabase();
        contentValues = new ContentValues();

        type = (TextView) findViewById(R.id.device_type);
        name = (EditText) findViewById(R.id.device_name);
        set_num = (EditText) findViewById(R.id.device_setting);
        set = (Button) findViewById(R.id.device_submit);
        bar = (ProgressBar)findViewById(R.id.pros_bar);
        final int progress = 0;
        bar.setIndeterminate(false);

        final String type_d = getIntent().getStringExtra("device_type");
        type.setText(type_d);

        //The button clicked to check if the entered values are valid and add to the database
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name_d = name.getText().toString();
                if (name_d == "") {
                    Toast.makeText(getApplicationContext(), "Please enter device name.", Toast.LENGTH_SHORT).show();
                }

                final String setting = set_num.getText().toString();
                try {
                    Double.valueOf(setting);
                    //if(setting != "")
                    //AsyncTask used to insert device info(name and settings) into database
                    final AsyncTask<Void, Integer, Void> adddevice = new AsyncTask<Void, Integer, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            insert(name_d, type_d, setting);
                            db.insert(kdb.TableName, null, contentValues);
                            while (progress <= 2000) {
                                try {
                                    Thread.sleep(2000);
                                    publishProgress(progress);
                                } catch (Exception e) {
                                    Log.e(ActivityName, "Adding failed");
                                }
                                return null;
                            }

                        }

                        protected void onProgressUpdate(Integer... values) {
                            bar.setVisibility(View.VISIBLE);
                            bar.setProgress(values[0]);
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            // Close this activity
                            bar.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(kitchen_add_new.this, kitchen_main.class);
                            intent.putExtra("dname", name_d);
                            finish();
                            startActivity(intent);
                        }
                    };
                    adddevice.execute((Void) null);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please enter set numbers.", Toast.LENGTH_SHORT).show();
                }
            }

            //The method to insert new rows to the device database
            public void insert(String named, String typed, String settingd) {
                contentValues.put(kdb.KEY_NAME, named);
                contentValues.put(kdb.KEY_TYPE, typed);
                contentValues.put(kdb.KEY_SETTING, settingd);

            }




        });
    }
}
