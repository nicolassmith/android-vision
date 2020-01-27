package com.gooeyfaze.deface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * View which displays the results of deface image.
 */
public class MainView extends View {
    private Bitmap mBitmap;
    private Context mContext;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * Sets the image and the click listener.
     */
    void setContent(Bitmap bitmap) {
        mBitmap = bitmap;
        this.setOnClickListener(mClickToShareListener);
        invalidate();
    }

    /**
     * Draws the bitmap.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            double scale = drawBitmap(canvas);
        }
    }

    /**
     * Draws the bitmap.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(
                0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Class that provides the method for sharing the image.
     */
    private View.OnClickListener mClickToShareListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                File cachePath = new File(mContext.getCacheDir(), "deface_cache");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File imagePath = new File(mContext.getCacheDir(), "deface_cache");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(
                    mContext, "com.gooeyfaze.fileprovider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(
                        contentUri, mContext.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                mContext.startActivity(Intent.createChooser(
                        shareIntent, mContext.getString(R.string.share_image_to)));
            }
        }
    };

}
