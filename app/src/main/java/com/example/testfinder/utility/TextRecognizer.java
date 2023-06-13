package com.example.testfinder.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//original source: https://github.com/ashomokdev/Tess-two_example
public class TextRecognizer {
    private static final String TESSDATA = "tessdata";

    private TessBaseAPI tessBaseApi;

    public TextRecognizer() {
    }

    public void copyTessDataFiles(String path, String DATA_PATH, Context context) {
        try {
            String fileList[] = context.getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = context.getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d("DEB", "Copied " + fileName + " to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e("DEB", e.toString());
        }
    }


    public void prepareTesseract(String DATA_PATH, Context context) {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyTessDataFiles(TESSDATA, DATA_PATH, context);
    }


    private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e("DEB", "Creation of directory " + path + " failed");
            }
        } else {
            Log.d("DEB", "Created directory " + path);
        }
    }


    public String extractText(Bitmap bitmap, String DATA_PATH, String lang) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e("DEB", e.getMessage());
            if (tessBaseApi == null) {
                Log.e("DEB", "TessBaseAPI is null");
            }
        }

        tessBaseApi.init(DATA_PATH, lang);
        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, ("\\\";'_/#$%@><-«»-„°“|~`*[]{}&‛‘’〝＂,.!?›‹’ \\\""));

        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e("DEB", "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }
}
