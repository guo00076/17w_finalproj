package com.teamwork.final_project;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.teamwork.final_project.kitchenDatabaseHelper.KEY_ID;
import static com.teamwork.final_project.kitchenDatabaseHelper.KEY_SETTING;
import static com.teamwork.final_project.kitchenDatabaseHelper.TableName;

/**
 * Fridge fragment
 * Jiawei Guo
 */
public class kitchenFridge extends Fragment {

    protected TextView fridge_current;
    protected TextView freezer_current;
    protected Spinner spinner_fridge;
    protected Spinner spinner_freezer;
    Button save;
    String set;
    int tm;
    TextView n_f;
    String temper;
    kitchenDatabaseHelper kdb;
    SQLiteDatabase db;
    ContentValues contentValues;

    /**
     * To create the view of the fridge fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.kitchen_activity_fridge, container, false);
        spinner_fridge = (Spinner) view.findViewById(R.id.fridge_change);
        spinner_freezer = (Spinner) view.findViewById(R.id.freezer_change);
        fridge_current = (TextView) view.findViewById(R.id.fridge);
        freezer_current = (TextView) view.findViewById(R.id.freezer);
        save = (Button)view.findViewById(R.id.save);
        n_f = (TextView)view.findViewById(R.id.f_name);

        kdb = new kitchenDatabaseHelper(getActivity().getBaseContext());
        db = kdb.getWritableDatabase();

        //options of the fridge settings
        ArrayList<String> temperature_fridge = new ArrayList<>();
        temperature_fridge.add("0");
        temperature_fridge.add("1");
        temperature_fridge.add("2");
        temperature_fridge.add("3");
        temperature_fridge.add("4");
        temperature_fridge.add("5");
        temperature_fridge.add("6");

        ArrayAdapter<String> adapter_fridge = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, temperature_fridge);
        adapter_fridge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fridge.setAdapter(adapter_fridge);

        //options of the freezer settings
        ArrayList<String> temperature_freezer = new ArrayList<>();
        temperature_freezer.add("-24");
        temperature_freezer.add("-23");
        temperature_freezer.add("-22");
        temperature_freezer.add("-21");
        temperature_freezer.add("-20");
        temperature_freezer.add("-19");
        temperature_freezer.add("-18");
        temperature_freezer.add("-17");
        temperature_freezer.add("-16");
        temperature_freezer.add("-15");
        temperature_freezer.add("-14");
        temperature_freezer.add("-13");
        temperature_freezer.add("-12");
        temperature_freezer.add("-11");
        temperature_freezer.add("-10");

        ArrayAdapter<String> adapter_freezer = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, temperature_freezer);
        adapter_freezer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_freezer.setAdapter(adapter_freezer);

        //get the temperature from the intent of transferring from main activity
        Bundle b_get = getArguments();
        if(b_get != null){
            n_f.setText(b_get.getString("namedata"));
            temper = b_get.getString("setdata");
        }else {
            temper = this.getActivity().getIntent().getStringExtra("temper");
            n_f.setText(getActivity().getIntent().getStringExtra("n"));
        }
        tm = Integer.valueOf(temper);
        if(tm >= 0){
            spinner_freezer.setEnabled(false);
            int spinnerPosition = adapter_fridge.getPosition(temper);
            spinner_fridge.setSelection(spinnerPosition);
        }else{
            spinner_fridge.setEnabled(false);
            int spinnerPosition = adapter_freezer.getPosition(temper);
            spinner_freezer.setSelection(spinnerPosition);
        }
        //to show selected options to notify users their settings of fridge
        spinner_fridge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "kitchenFridge temperature set to " + set, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //to show the selected settings for the freezer
        spinner_freezer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Freezer temperature set to " + set, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //the text to show current temperature of fridge and freezer in textview
        fridge_current.setText("kitchenFridge current temperature: 2");
        freezer_current.setText("Freezer current temperature: -20");

        //save button function
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i_f = getActivity().getIntent().getStringExtra("di");
                Updateset(i_f, set);
                Toast.makeText(v.getContext(), "The set has been updated ", Toast.LENGTH_LONG).show();
            }
        });
        //return view of fridge
        return view;

    }

    /**
     * The method used to update the settings of the device database.
     * @param id is the device id.
     * @param set_num is the setting values needed to save.
     */
    private void Updateset(String id, String set_num){
        contentValues = new ContentValues();
        contentValues.put(KEY_SETTING, set_num);
        contentValues.put(KEY_ID, id);

        db.update(TableName, contentValues, KEY_ID + "=" + id, null);
    }

}
