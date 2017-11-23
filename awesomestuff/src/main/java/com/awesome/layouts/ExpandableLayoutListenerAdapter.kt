package com.awesome.layouts


abstract class ExpandableLayoutListenerAdapter : ExpandableLayoutListener {
    /**
     * {@inheritDoc}
     */
    override fun onAnimationStart() {}

    /**
     * {@inheritDoc}
     */
    override fun onAnimationEnd() {}

    /**
     * {@inheritDoc}
     */
    override fun onPreOpen() {}

    /**
     * {@inheritDoc}
     */
    override fun onPreClose() {}

    /**
     * {@inheritDoc}
     */
    override fun onOpened() {}

    /**
     * {@inheritDoc}
     */
    override fun onClosed() {}
}