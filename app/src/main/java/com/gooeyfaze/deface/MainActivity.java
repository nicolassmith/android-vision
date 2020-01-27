package com.gooeyfaze.deface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deface main activity.
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        // Handle inputs.
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Bitmap bitmap = null;

        if(action.equals(Intent.ACTION_SEND)) {
            Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Default face image.
            InputStream stream = getResources().openRawResource(R.raw.face);

            // turned into a bitmap image
            bitmap = BitmapFactory.decodeStream(stream);
        }

        // Face detection.
        SparseArray<Face> faces = Bitmap2FaceProcessor.getFaceSparseArray(
                getApplicationContext(), bitmap);

        // Image processing.
        ImageDefacer defacer = new ImageDefacer(getApplicationContext());
        Bitmap defaced_bitmap = defacer.defaceBitmap(bitmap, faces);

        // Pass to view for display.
        MainView overlay = findViewById(R.id.faceView);
        overlay.setContent(defaced_bitmap);

    }

}
