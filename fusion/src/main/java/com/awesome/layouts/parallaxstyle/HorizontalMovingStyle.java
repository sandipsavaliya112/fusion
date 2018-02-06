package com.awesome.layouts.parallaxstyle;

import android.graphics.Canvas;
import android.widget.ImageView;

import com.awesome.layouts.AwesomeImageView;

public class HorizontalMovingStyle implements AwesomeImageView.ParallaxStyle {
    @Override
    public void onAttachedToImageView(AwesomeImageView view) {
        // only supports CENTER_CROP
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onDetachedFromImageView(AwesomeImageView view) {

    }

    @Override
    public void transform(AwesomeImageView view, Canvas canvas, int x, int y) {
        if (view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            return;
        }

        // image's width and height
        int iWidth = view.getDrawable().getIntrinsicWidth();
        int iHeight = view.getDrawable().getIntrinsicHeight();
        if (iWidth <= 0 || iHeight <= 0) {
            return;
        }

        // view's width and height
        int vWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        int vHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();

        // device's width
        int dWidth = view.getResources().getDisplayMetrics().widthPixels;

        if (iWidth * vHeight > iHeight * vWidth) {
            // avoid over scroll
            if (x < -vWidth) {
                x = -vWidth;
            } else if (x > dWidth) {
                x = dWidth;
            }

            float imgScale = (float) vHeight / (float) iHeight;
            float max_dx = Math.abs((iWidth * imgScale - vWidth) * 0.5f);
            float translateX = -(2 * max_dx * x + max_dx * (vWidth - dWidth)) / (vWidth + dWidth);
            canvas.translate(translateX, 0);
        }
    }
}
