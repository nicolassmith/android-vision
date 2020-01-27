package com.gooeyfaze.deface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;

import java.io.InputStream;

public class ImageDefacer {
    private Context mContext;

    public ImageDefacer(Context context) {
        mContext = context;
    }

    Bitmap defaceBitmap(Bitmap bitmap, SparseArray<Face> faces) {
        // Copy to mutable bitmap
        Bitmap mutableBitmap = bitmap.copy(bitmap.getConfig(), true);
        // Create a canvas starting from the source bitmap.
        Canvas canvas = new Canvas(mutableBitmap);
        // draw the face annotations onto the canvas
        drawFaceAnnotations(canvas, faces);

        // return the bitmap
        return mutableBitmap;
    }

    private void drawFaceAnnotations(Canvas canvas, SparseArray<Face> faces) {
        for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.valueAt(i);
            drawSmileyOnFace(face, canvas);
        }
    }

    private void drawSmileyOnFace(Face face, Canvas canvas) {
        InputStream stream = mContext.getResources().openRawResource(R.raw.smiley_640);

        // turned into a bitmap image
        Bitmap smiley_bitmap = BitmapFactory.decodeStream(stream);

        // get the face position
        PointF facePosition = face.getPosition();

        // get the face scale
        double faceScale = Math.max(face.getHeight(), face.getWidth());

        double margin = faceScale * 0.2;

        Rect rectDest = new Rect(
                (int) Math.round(facePosition.x-margin),
                (int) Math.round(facePosition.y-margin),
                (int) Math.round(facePosition.x+faceScale+margin),
                (int) Math.round(facePosition.y+faceScale+margin)
        );

        canvas.drawBitmap(smiley_bitmap, null, rectDest, null);
    }
}
