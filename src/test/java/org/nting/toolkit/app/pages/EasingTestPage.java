package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.AbsoluteLayout.customPosition;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static org.nting.toolkit.ui.style.material.ButtonPropertyIds.RAISED;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_500;

import java.util.Arrays;

import org.nting.data.Property;
import org.nting.data.bean.BeanDescriptor;
import org.nting.data.property.ObjectProperty;
import org.nting.data.query.ListDataProvider;
import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager;
import org.nting.toolkit.animation.Easing.EasingFunction;
import org.nting.toolkit.animation.Easing.EasingType;
import org.nting.toolkit.animation.Timeline;
import org.nting.toolkit.animation.TweenBuilder;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.ActionListener;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.util.EnumUtils;

public class EasingTestPage implements ITestPage {

    private final Property<EasingType> easingType = new ObjectProperty<>(EasingType.TYPE_IN);
    private final Property<EasingFunction> easingFunction = new ObjectProperty<>(EasingFunction.FUNCTION_LINEAR);

    private Timeline timeline;

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("7dlu, 100dlu, 7dlu, 100dlu, 7dlu, pref, 0px:grow, 7dlu",
                "7dlu, pref, 7dlu, 0px:grow, 7dlu");
        panelBuilder.<EasingType> addDropDownList(xy(1, 1))
                .dataProvider(new ListDataProvider<>(Arrays.asList(EasingType.values()),
                        new BeanDescriptor<>(EasingType.class), t -> t))
                .itemCaptionGenerator(EnumUtils::getHumanReadableName).selectedItem(easingType);
        panelBuilder.<EasingFunction> addDropDownList(xy(3, 1))
                .dataProvider(new ListDataProvider<>(Arrays.asList(EasingFunction.values()),
                        new BeanDescriptor<>(EasingFunction.class), t -> t))
                .itemCaptionGenerator(EnumUtils::getHumanReadableName).selectedItem(easingFunction);
        AbstractComponent component = new ContentBuilder()
                .content(FontIcon.VIEWPORT.getContent(FontManager.FontSize.EXTRA_EXTRA_LARGE_FONT, 0xffff5005))
                .asComponent();
        component.x.setValue(120f);
        component.y.setValue(150f);
        panelBuilder.addPanel(xyw(1, 3, 6)).layoutManager(new AbsoluteLayout()).addComponent(component,
                customPosition());

        panelBuilder.addButton(xy(5, 1)).text("Start").actionListener(new ActionHandler(component)).color(blue_500)
                .pass().set(RAISED.toString(), true);

        return panelBuilder.build();
    }

    private class ActionHandler implements ActionListener {

        private final AbstractComponent component;

        private ActionHandler(AbstractComponent component) {
            this.component = component;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            float fromValue = 120;
            float toValue = component.getParent().getSize().width - 120;

            if (timeline != null) {
                timeline.fastForward();
                component.x.setValue(fromValue);
            }

            timeline = new Timeline(500);
            timeline.add(new TweenBuilder<>(fromValue, toValue, 1000).property(component.x)
                    .easing(easingType.getValue(), easingFunction.getValue()).build());
            timeline.after(1000).add(new TweenBuilder<>(toValue, fromValue, 1000).property(component.x)
                    .easing(easingType.getValue(), easingFunction.getValue()).build());
            timeline.loopForever(1000);
            component.addBehavior(timeline);
        }
    }
}
