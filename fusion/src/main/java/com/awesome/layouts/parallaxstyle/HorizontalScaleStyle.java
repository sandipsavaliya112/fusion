package com.awesome.layouts.parallaxstyle;

import android.graphics.Canvas;

import com.awesome.layouts.AwesomeImageView;


public class HorizontalScaleStyle implements AwesomeImageView.ParallaxStyle {
    private float finalScaleRatio = 0.7f;

    public HorizontalScaleStyle() {
    }

    public HorizontalScaleStyle(float finalScaleRatio) {
        this.finalScaleRatio = finalScaleRatio;
    }

    public void setFinalScaleRatio(float scale) {
        finalScaleRatio = scale;
    }

    @Override
    public void transform(AwesomeImageView view, Canvas canvas, int x, int y) {
        // view's width and height
        int vWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        int vHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
        // device's width
        int dWidth = view.getResources().getDisplayMetrics().widthPixels;

        if (vWidth >= dWidth) {
            // Do nothing if imageView's width is bigger than device's width.
            return;
        }

        float scale;
        int pivot = (dWidth - vWidth) / 2;
        if (x <= pivot) {
            scale = 2 * (1 - finalScaleRatio) * (x + vWidth) / (dWidth + vWidth) + finalScaleRatio;
        } else {
            scale = 2 * (1 - finalScaleRatio) * (dWidth - x) / (dWidth + vWidth) + finalScaleRatio;
        }

        canvas.scale(scale, scale, vWidth / 2, vHeight / 2);
    }

    @Override
    public void onAttachedToImageView(AwesomeImageView view) {

    }

    @Override
    public void onDetachedFromImageView(AwesomeImageView view) {

    }
}
