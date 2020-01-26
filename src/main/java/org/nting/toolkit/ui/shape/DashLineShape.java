package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import playn.core.Canvas;

public class DashLineShape extends LineShape {

    float[] intervals = new float[] { 3, 3 }; // array of ON and OFF distances, even number of entries (>=2);
                                              // startOffset?

    public DashLineShape(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    public DashLineShape intervals(float[] intervals) {
        Preconditions.checkArgument(intervals == null || (2 <= intervals.length && intervals.length % 2 == 0));
        this.intervals = intervals;
        return this;
    }

    @Override
    public void paint(Canvas canvas) {
        if (intervals != null) {
            canvas.setLineDash(intervals);
            super.paint(canvas);
            canvas.setLineDash(null);
        } else {
            super.paint(canvas);
        }
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("intervals", intervals);
    }
}
