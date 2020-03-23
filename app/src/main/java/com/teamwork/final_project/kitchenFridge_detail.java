package com.teamwork.final_project;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This class is to create the fragment to use on phones.
 * Jiawei Guo
 */
public class kitchenFridge_detail extends AppCompatActivity {
    //create the fragment and insert it into FrameLayout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_fridge_detail);

        kitchenFridge kf = new kitchenFridge();
        //insert
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.add(R.id.activity_kitchen_fridge_detail, kf);
        ft1.commit();

    }
}
