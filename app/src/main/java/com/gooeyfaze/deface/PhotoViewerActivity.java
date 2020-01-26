/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gooeyfaze.deface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.gooeyfaze.deface.R;
import com.google.android.gms.vision.face.Face;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Demonstrates basic usage of the GMS vision face detector by running face landmark detection on a
 * photo and displaying the photo with associated landmarks in the UI.
 */
public class PhotoViewerActivity extends Activity {
    private static final String TAG = "PhotoViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        // if this is a share, something happens?
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Bitmap bitmap = null;

        if(action.equals(Intent.ACTION_SEND)) {

            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            // Refactor: this needs to all be a function, input image in TBD format but output is faces.
            // get faces for image
            InputStream stream = getResources().openRawResource(R.raw.face);

            // turned into a bitmap image
            bitmap = BitmapFactory.decodeStream(stream);
        }

        SparseArray<Face> faces = Bitmap2FaceProcessor.getFaceSparseArray(
                getApplicationContext(), bitmap);

        // This is where the results are displayed, image along with annotations.

        // refactor to have canvas stuff here

        // we will create a canvas object
        // we may make a new bitmap or reuse existing
        // draw annotations.
        ImageDefacer defacer = new ImageDefacer(getApplicationContext());
        Bitmap defaced_bitmap = defacer.defaceBitmap(bitmap, faces);

        // pass to view for display

        FaceView overlay = (FaceView) findViewById(R.id.faceView);
        overlay.setContent(defaced_bitmap);

    }

}
