package com.example.android.camera2raw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

/**
 * Created by martin on 09.03.17.
 */

public class ViewActivity extends Activity{
    public Spinner spinner;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set new view
        setContentView(R.layout.image_view);
        //Lode items used in this view: progressbar and spinner
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public ViewActivity(){
        System.out.println("ViewActivity STARTET****");
    }

    //Go back to mainactivity
    public void back(View view) {
        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(myIntent);
    }

    //Read image into the imageview and display it for the user
    //make the image smaller and rotate it to fit the view.
    public void readImage(View view){
        ImageView imageView= (ImageView) findViewById(R.id.imageViewBitmap);
        String photoPath = Environment.getExternalStorageDirectory()+"/basics/output.png";
        Bitmap bm = BitmapFactory.decodeFile(photoPath);
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate((float) 90);
        Bitmap rotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        rotated = Bitmap.createScaledBitmap(rotated,imageView.getWidth(),imageView.getHeight(),true);
        imageView.setImageBitmap(rotated);

    }
    //If user wants to edit image this is where it sets the value and starts ImageEditor
    public void editImage(View view){
        int i = spinner.getSelectedItemPosition();
        new ImageEditor().execute(i);
    }

    //Create a AsyncTask to run the imageEditor on a seperate thread.
    // Takes the givven value and sends it to the helper.
    private class ImageEditor extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            Looper.prepare();
            //Takes the inn value and set it as a int
            int i=params[0];
            //Lode the image and create a bitmap for the Helper class.
            String photoPath = Environment.getExternalStorageDirectory()+"/basics/output.png";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bm = BitmapFactory.decodeFile(photoPath,options);
            //Send value and bitmap into Helper class
            Helper helper = new Helper(i);
            helper.setBitmap(bm);
            return "Executed";
        }
        //Show the user that the work is done
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        //Show the user that work is in progress
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
