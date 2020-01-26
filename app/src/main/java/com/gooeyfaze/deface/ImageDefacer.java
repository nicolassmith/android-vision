package com.gooeyfaze.deface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

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
            circleLandmarksForFace(face, canvas, 1);
        }
        InputStream stream = mContext.getResources().openRawResource(R.raw.smiley_640);

        // turned into a bitmap image
        Bitmap smiley_bitmap = BitmapFactory.decodeStream(stream);

        canvas.drawBitmap(smiley_bitmap, 0, 0, null);
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
