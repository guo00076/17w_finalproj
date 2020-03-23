package com.teamwork.final_project;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The class creates the fragment run on the phone
 */
public class kitchenMicrowave_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_microwave_detail);

        kitchenMicrowave km = new kitchenMicrowave();
        //insert into a FrameLayout
        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        ft2.add(R.id.activity_kitchen_microwave_detail, km);
        ft2.commit();

    }
}
