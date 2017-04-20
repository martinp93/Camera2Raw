package com.example.android.camera2raw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;

/**
 * Created by martin on 16.01.17.
 */

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCameraView(View view){
        setContentView(R.layout.activity_camera);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, Camera2RawFragment.newInstance())
                .commit();

    }
    /** Called when the user clicks the Send button */
    public void toImageView(View view) {
        setContentView(R.layout.image_view);
    }
    public void back(View view) {
        setContentView(R.layout.activity_main);
    }

    public void readImage(View view){
        ImageView imageView= (ImageView) findViewById(R.id.imageViewBitmap);
        String photoPath = Environment.getExternalStorageDirectory()+"/basics/output.png";
        Bitmap bm = BitmapFactory.decodeFile(photoPath);
        imageView.setImageBitmap(bm);
    }

}

