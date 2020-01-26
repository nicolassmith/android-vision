package com.gooeyfaze.deface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
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
            //circleLandmarksForFace(face, canvas, 1);
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

        Rect rectDest = new Rect(
                (int) Math.round(facePosition.x),
                (int) Math.round(facePosition.y),
                (int) Math.round(facePosition.x+faceScale),
                (int) Math.round(facePosition.y+faceScale)
        );

        canvas.drawBitmap(smiley_bitmap, null, rectDest, null);
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
