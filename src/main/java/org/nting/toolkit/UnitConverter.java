package org.nting.toolkit;

import playn.core.Font;

public interface UnitConverter {

    int dialogUnitXAsPixel(float dluX, Component component);

    float dialogUnitXAsPixel(Component component);

    float dialogUnitXAsPixel(Font font);

    int dialogUnitYAsPixel(float dluY, Component component);

    float dialogUnitYAsPixel(Component component);

    float dialogUnitYAsPixel(Font font);
}
