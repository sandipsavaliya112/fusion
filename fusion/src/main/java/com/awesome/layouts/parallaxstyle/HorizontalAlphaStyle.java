package com.awesome.layouts.parallaxstyle;

import android.graphics.Canvas;

import com.awesome.layouts.AwesomeImageView;

public class HorizontalAlphaStyle implements AwesomeImageView.ParallaxStyle {
    private float finalAlpha = 0.3f;

    public HorizontalAlphaStyle() {
    }

    public HorizontalAlphaStyle(float finalAlpha) {
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
        // view's width
        int vWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        // device's width
        int dWidth = view.getResources().getDisplayMetrics().widthPixels;

        if (vWidth >= dWidth) {
            // Do nothing if imageView's width is bigger than device's width.
            return;
        }

        float alpha;
        int pivot = (dWidth - vWidth) / 2;
        if (x <= pivot) {
            alpha = 2 * (1 - finalAlpha) * (x + vWidth) / (dWidth + vWidth) + finalAlpha;
        } else {
            alpha = 2 * (1 - finalAlpha) * (dWidth - x) / (dWidth + vWidth) + finalAlpha;
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
