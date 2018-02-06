package com.awesome.layouts.parallaxstyle;

import android.graphics.Canvas;

import com.awesome.layouts.AwesomeImageView;


public class VerticalAlphaStyle implements AwesomeImageView.ParallaxStyle {
    private float finalAlpha = 0.3f;

    public VerticalAlphaStyle() {
    }

    public VerticalAlphaStyle(float finalAlpha) {
        if (finalAlpha < 0 || finalAlpha > 1.0f) {
            throw new IllegalArgumentException("the alpha must between 0 and 1.");
        }
        this.finalAlpha = finalAlpha;
    }

    public void setFinalAlpha(float alpha) {
        finalAlpha = alpha;
    }

    @Override
    public void transform(AwesomeImageView view, Canvas canvas, int x, int y) {
        // view's height
        int vHeight = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
        // device's height
        int dHeight = view.getResources().getDisplayMetrics().heightPixels;

        if (vHeight >= dHeight) {
            // Do nothing if imageView's height is bigger than device's height.
            return;
        }

        float alpha;
        int pivot = (dHeight - vHeight) / 2;
        if (y <= pivot) {
            alpha = 2 * (1 - finalAlpha) * (y + vHeight) / (dHeight + vHeight) + finalAlpha;
        } else {
            alpha = 2 * (1 - finalAlpha) * (dHeight - y) / (dHeight + vHeight) + finalAlpha;
        }
        view.setAlpha(alpha);
    }

    @Override
    public void onAttachedToImageView(AwesomeImageView view) {

    }

    @Override
    public void onDetachedFromImageView(AwesomeImageView view) {

    }
}
