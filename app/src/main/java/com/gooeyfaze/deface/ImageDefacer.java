package com.gooeyfaze.deface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class ImageDefacer {
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
            circleLandmarksForFace(face, canvas, 1);
        }
    }

    private void circleLandmarksForFace(Face face, Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        for (Landmark landmark : face.getLandmarks()) {
            int cx = (int) (landmark.getPosition().x * scale);
            int cy = (int) (landmark.getPosition().y * scale);
            canvas.drawCircle(cx, cy, 10, paint);
        }
    }
}
