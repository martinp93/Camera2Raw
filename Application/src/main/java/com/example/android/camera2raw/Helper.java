package com.example.android.camera2raw;

        import android.app.Activity;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Base64;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ProgressBar;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;

/**
 * Created by martin on 16.01.17.
 */

public class Helper extends Activity {
    private int value;

    public Helper(int value) {
        this.value = value;
    }

    public void setBitmap(Bitmap bitmap, String info){
        switch (value) {
            case 0:  bitmap=setGreen(bitmap,info);
                break;
            case 1:  bitmap=setBlue(bitmap,info);
                break;
            case 2:  bitmap=setRed(bitmap,info);
                break;
            case 3:  bitmap=luminance(bitmap);
                break;
            case 4:  bitmap=setRed(bitmap,info);
                break;
            case 5:  bitmap=setRed(bitmap,info);
                break;
            case 6:  bitmap=setRed(bitmap,info);
                break;
            case 7:  bitmap=setRed(bitmap,info);
                break;
            default: System.out.println("*************DEFAULT VALUE*****");
                break;
        }
        SetImage(bitmap);
    }

    public void SetImage(Bitmap bitmap) {
            File file = new File(Environment.getExternalStorageDirectory(), "/basics/output.png");
            file.getParentFile().mkdirs();
            try {
                    if (file.exists()) {
                            file.delete();
                            System.out.println("FILE DELETED");
                    }
                    if (file.createNewFile()) {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.close();
                            System.out.println("File created!");
                    } else {
                            System.out.println("File not created!");
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to create file");
            }
    }

    public Bitmap setGreen (Bitmap bitmap, String hex){
        try {
            for (int i = 0; i < bitmap.getHeight()/10; i++) {
                for (int j = 0; j < bitmap.getWidth()/10; j++) {
                    int pixel = bitmap.getPixel(j, i);
                    String inn="";
                    inn = "0000"+hex+"00";
                    int inter=0xFFFF00FF;
                    int inter2=Integer.parseInt(inn,16);
                    pixel= inter&pixel;
                    pixel= inter2+pixel;
                    bitmap.setPixel(j,i,pixel);
                }
            }
        } catch (Exception e) {
            System.out.println("FEIL setGreen*****************" + e);
        }
        return bitmap;
    }

    public Bitmap setRed (Bitmap bitmap, String hex){
        try {
            for (int i = 0; i < bitmap.getHeight()/10; i++) {
                for (int j = 0; j < bitmap.getWidth()/10; j++) {
                    int pixel = bitmap.getPixel(j, i);
                    String inn="";
                    inn = "00"+hex+"0000";
                    int inter=0xFF00FFFF;
                    int inter2=Integer.parseInt(inn,16);
                    pixel= inter&pixel;
                    pixel= inter2+pixel;
                    bitmap.setPixel(j,i,pixel);
                }
            }
        } catch (Exception e) {
            System.out.println("FEIL setRed*****************" + e);
        }
        return bitmap;
    }

    public Bitmap setBlue (Bitmap bitmap, String hex){
        try {
            for (int i = 0; i < bitmap.getHeight()/10; i++) {
                for (int j = 0; j < bitmap.getWidth()/10; j++) {
                    int pixel = bitmap.getPixel(j, i);
                    String inn="";
                    inn = "000000"+hex+"";
                    int inter=0xFFFFFF00;
                    int inter2=Integer.parseInt(inn,16);
                    pixel= inter&pixel;
                    pixel= inter2+pixel;
                    bitmap.setPixel(j,i,pixel);
                }
            }
        } catch (Exception e) {
            System.out.println("FEIL setBlue*****************" + e);

        }
        return bitmap;
    }
    public static Bitmap luminance(Bitmap bitmap) {
        int grey =0;
        int color =0;
        for (int i=0;i<bitmap.getHeight()/10;i++){
            for (int j=0;j<bitmap.getWidth()/10;j++){
                int pixel = bitmap.getPixel(j,i);
                double red = (double) (pixel >> 16 & 0xFF) / 255.0;
                red = red < 0.03928 ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
                double green = (double) (pixel >> 8 & 0xFF) / 255.0;
                green = green < 0.03928 ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
                double blue = (double) (pixel & 0xFF) / 255.0;
                blue = blue < 0.03928 ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);
                grey = (int)(((0.2126 * red) + (0.7152 * green) + (0.0722 * blue)) * 255);
                color = (255 << 24) + (grey << 16) + (grey << 8) + grey;
                bitmap.setPixel(j, i, color);
            }
            System.out.println(color+" "+grey);
        }
        return bitmap;
    }
}

