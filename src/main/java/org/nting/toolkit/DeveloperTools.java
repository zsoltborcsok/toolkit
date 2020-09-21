package org.nting.toolkit;

import org.nting.data.Property;
import org.nting.toolkit.component.AbstractComponent;

public interface DeveloperTools extends PaintableComponent {

    /**
     * Returns a boolean property indicating whether the whole DeveloperTools and this tool is active or not.
     */
    Property<Boolean> addTool(String name, AbstractComponent tool);
}
