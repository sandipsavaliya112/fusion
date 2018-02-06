package com.awesome.layouts.parallaxstyle;

import android.graphics.Canvas;

import com.awesome.layouts.AwesomeImageView;


public class VerticalScaleStyle implements AwesomeImageView.ParallaxStyle {
    private float finalScaleRatio = 0.7f;

    public VerticalScaleStyle() {
    }

    public VerticalScaleStyle(float finalScaleRatio) {
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
        // device's height
        int dHeight = view.getResources().getDisplayMetrics().heightPixels;

        if (vHeight >= dHeight) {
            // Do nothing if imageView's height is bigger than device's height.
            return;
        }

        float scale;
        int pivot = (dHeight - vHeight) / 2;
        if (y <= pivot) {
            scale = 2 * (1 - finalScaleRatio) * (y + vHeight) / (dHeight + vHeight) + finalScaleRatio;
        } else {
            scale = 2 * (1 - finalScaleRatio) * (dHeight - y) / (dHeight + vHeight) + finalScaleRatio;
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
