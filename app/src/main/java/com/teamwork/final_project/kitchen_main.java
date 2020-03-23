package com.teamwork.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static com.teamwork.final_project.kitchenDatabaseHelper.KEY_ID;
import static com.teamwork.final_project.kitchenDatabaseHelper.TableName;

/**
 * Main listview of the kitchen devices
 * Jiawei Guo
 */
public class kitchen_main extends AppCompatActivity {

    protected ListView list_kitchen;
    protected Button b_add;
    kitchenDatabaseHelper kdb;
    SQLiteDatabase db;
    Cursor cursor;
    Bundle bundle;
    kitchenFridge kf;
    kitchenMicrowave km;
    AlertDialog d1;

    public static String KEY_NAME="name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_fragment_listviewkitchen);

        list_kitchen = (ListView)findViewById(R.id.list_kitchen);
        b_add = (Button)findViewById(R.id.add_device);

        kdb = new kitchenDatabaseHelper(this);
        db = kdb.getWritableDatabase();


        //the dialog to let users choose which type of device he/she would like to add
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(kitchen_main.this);
                builder.setMessage("What type of device would you like to add?");

                builder.setPositiveButton("Light", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String type_light = "Light";
                        Intent in = new Intent(kitchen_main.this, kitchen_add_new.class);
                        in.putExtra("device_type", type_light);
                        startActivity(in);
                    }
                });

                builder.setNegativeButton("Fridge", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String type_fridge = "Fridge";
                        Intent in = new Intent(kitchen_main.this, kitchen_add_new.class);
                        in.putExtra("device_type", type_fridge);
                        startActivity(in);
                    }
                });

                builder.setNeutralButton("Micwave", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String type_micro = "Microwave";
                        Intent in = new Intent(kitchen_main.this, kitchen_add_new.class);
                        in.putExtra("device_type", type_micro);
                        startActivity(in);
                    }
                });
                builder.show();

            }
        });

        cursor = db.rawQuery("SELECT * FROM kitchen_device", null);
        String[] names = new String[] { KEY_NAME };
        int[] to = new int[] { R.id.list_name};

        final CursorAdapter adapter = new SimpleCursorAdapter(kitchen_main.this,
                R.layout.kitchen_main_list, cursor, names, to, 0);
        list_kitchen.setAdapter(adapter);

        list_kitchen.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int index_type = cursor.getColumnIndex( "type" );
                int index_id = cursor.getColumnIndex("_id");
                //cursor.moveToPosition(position);
                cursor.moveToFirst();
                String type = cursor.getString(index_type);
                int i = cursor.getInt(index_id);
                db.delete(TableName, KEY_ID + "=" + i, null);
                cursor=db.rawQuery("SELECT * FROM kitchen_device", null);
                adapter.swapCursor(cursor);
                adapter.notifyDataSetChanged();
                Toast.makeText(kitchen_main.this, "Delete "+ type, Toast.LENGTH_LONG).show();
                if(findViewById(R.id.frameHolder) != null){
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frameHolder)).commit();
                }

                return false;
            }
        });

        //add clicklistener to the listview of kitchen devices
        list_kitchen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int index = position+1;
                long index = adapter.getItemId(position);
                cursor = db.rawQuery("SELECT * FROM kitchen_device where _id = ? ", new String[]{String.valueOf(index)});
                int index_type = cursor.getColumnIndex( "type" );
                int index_setting = cursor.getColumnIndex("settings");
                int index_id = cursor.getColumnIndex("_id");
                int index_n = cursor.getColumnIndex("name");
                cursor.moveToFirst();
                String type = cursor.getString(index_type);
                String set = cursor.getString(index_setting);
                String data_i = cursor.getString(index_id);
                String name = cursor.getString(index_n);
                //store values of database to a bundle
                bundle = new Bundle();
                bundle.putString("dataid", data_i);
                bundle.putString("setdata", set);
                bundle.putString("namedata", name);

                //to check which type the item is and transit to corresponding activity
                    if(type.equals("Microwave")) {
                        if (findViewById(R.id.frameHolder) != null) {
                            km = new kitchenMicrowave();
                            km.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.frameHolder, km).commit();
                        } else {
                            Intent intent1 = new Intent(kitchen_main.this, kitchenMicrowave_detail.class);
                            intent1.putExtra("time", set);
                            intent1.putExtra("di", data_i);
                            intent1.putExtra("n", name);
                            startActivity(intent1);
                        }
                    }
                    if(type.equals("Fridge")) {
                        if (findViewById(R.id.frameHolder) != null) {
                            kf = new kitchenFridge();
                            kf.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.frameHolder, kf).commit();
                        } else {
                            Intent intent2 = new Intent(kitchen_main.this, kitchenFridge_detail.class);
                            intent2.putExtra("temper", set);
                            intent2.putExtra("di", data_i);
                            intent2.putExtra("n", name);
                            startActivity(intent2);
                        }
                    }

                if(type.equals("Light")){
                    Intent intent3 = new Intent(kitchen_main.this, kitchen_main_light.class);
                    intent3.putExtra("dim", set);
                    intent3.putExtra("di", data_i);
                    intent3.putExtra("n", name);
                    startActivity(intent3);
                }

            }
        });
        //The usage instructions dialog
        AlertDialog.Builder help_m = new AlertDialog.Builder(kitchen_main.this);
        help_m.setTitle("Instructions of kitchen-control");
        help_m.setMessage("This interface is used to control 3 types of kitchen devices: kitchen_microwave, light, and kitchen_fridge." + "\n"
        + "Simple steps to use: 1) using add device button to add any device you want and save entered name and setting. " +
                "2) select the name of the device from current interface. 3) control your devices or change previous settings." + "\n" +
        "Version 1.0" + "\n" + "Author: Jiawei Guo");
        help_m.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        d1 = help_m.create();
    }
    //inflate the help_menu layout
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.help_menu, m);
        return true;
    }
    //to show the help dialog when the menu item is selected
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        if(id == R.id.action_help){
            d1.show();
        }
        return true;
    }


}
