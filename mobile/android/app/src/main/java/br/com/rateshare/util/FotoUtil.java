package br.com.rateshare.util;

import android.content.Context;
import android.os.Environment;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FotoUtil {
    public static int COMPRESS_QUALIDADE_RUIM = 5;

    public static int COMPRESS_QUALIDADE_MEDIA = 13;

    public static int COMPRESS_QUALIDADE_ALTA = 100;

    public static String caminhoFoto = "";

    public static final int CODIGO_CAMERA = 567;

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        caminhoFoto = image.getAbsolutePath();
        return image;
    }






}
