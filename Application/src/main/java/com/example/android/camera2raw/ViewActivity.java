package com.example.android.camera2raw;

import android.app.Activity;
import android.content.ContentProvider;
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
        setContentView(R.layout.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public ViewActivity(){
        System.out.println("ViewActivity STARTET****");
    }

    public void back(View view) {
        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(myIntent);
    }
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
    public void editImage(View view){
        System.out.println("EDITIMAGE*******");
        int i = spinner.getSelectedItemPosition();
        new ImageEditor().execute(i);
    }

    private class ImageEditor extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            Looper.prepare();
            int i=params[0];
            String photoPath = Environment.getExternalStorageDirectory()+"/basics/output.png";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bm = BitmapFactory.decodeFile(photoPath,options);
            Helper helper = new Helper(i);
            helper.setBitmap(bm);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
