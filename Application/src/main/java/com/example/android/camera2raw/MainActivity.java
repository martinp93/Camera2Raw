package com.example.android.camera2raw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by martin on 16.01.17.
 */

public class MainActivity extends Activity {
    ImageView imageView=null;

    //Create mainactivity and set view for first page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lode logo into the view
        imageView=(ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.frontpage);
    }

    //Start a cameraActivity to take pictures and save them ass
    public void toCameraView(View view){
        setContentView(R.layout.activity_camera);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, Camera2RawFragment.newInstance())
                .commit();

    }
    /** Called when the user clicks the Send button */
    public void toImageView(View view) {
        Intent myIntent = new Intent(view.getContext(), ViewActivity.class);
        startActivity(myIntent);
    }

}

