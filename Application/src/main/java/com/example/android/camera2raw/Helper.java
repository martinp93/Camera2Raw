package com.example.android.camera2raw;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Environment;
        import imageapi.Blur;
        import imageapi.CompositeImage;
        import imageapi.Gradient;
        import imageapi.Luminance;

        import java.io.File;
        import java.io.FileOutputStream;

/**
 * Created by martin on 16.01.17.
 */

public class Helper extends Activity {
    private int value;

    public Helper(int value) {
        this.value = value;
    }


    public void setBitmap(Bitmap bitmap){
        System.out.println("START at value"+value );
        CompositeImage composit = bitmapToComposit(bitmap);
        switch (value) {
            case 0:  //do noting
                break;
            case 1:
                composit=Blur.linearBlur(composit,50);
                bitmap = compositToBitmap(composit);
                break;
            case 2:
                composit=Luminance.getLuminance(composit);
                composit=Gradient.getGradient(composit);
                bitmap=compositToBitmap(composit);
                break;
            case 3:
                composit=Luminance.getLuminance(composit);
                bitmap=compositToBitmap(composit);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:  //do noting
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


    public Bitmap compositToBitmap(CompositeImage compositeImage){
        compositeImage.getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bm = Bitmap.createBitmap(compositeImage.getWidth(),compositeImage.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i =0; i<compositeImage.getHeight();i++){
            for (int j=0; j<compositeImage.getWidth();j++){
                int pixel=compositeImage.getPixel(j,i);
                bm.setPixel(j,i,pixel);
            }
        }
        return bm;
    }
    public CompositeImage bitmapToComposit(Bitmap bm){
        CompositeImage compositeImage=new CompositeImage(bm);
        return compositeImage;
    }


    public Bitmap setGreen (Bitmap bitmap, String hex){
        try {
            for (int i = 0; i < bitmap.getHeight(); i++) {
                for (int j = 0; j < bitmap.getWidth(); j++) {
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
            for (int i = 0; i < bitmap.getHeight(); i++) {
                for (int j = 0; j < bitmap.getWidth(); j++) {
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
            for (int i = 0; i < bitmap.getHeight(); i++) {
                for (int j = 0; j < bitmap.getWidth(); j++) {
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

}

