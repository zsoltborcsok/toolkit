package org.nting.toolkit.app.pages;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.builder.CheckBoxMiddleBuilder;
import org.nting.toolkit.component.builder.TextAreaMiddleBuilder;
import org.nting.toolkit.form.ReadOnlyFormBuilder;

public class ReadOnlyFormBuilderTestPage implements IPageFactory {

    private static final String NOTES = "----- Mi, 22.01.2014 14:06 (Philipp Sauder) -----\n"
            + "bisher keine Rückmeldung. WV\n" + "\n" + "----- Do, 01.08.2013 16:45 (Philipp Sauder) -----\n"
            + "Normann Unternehmensgruppe, Herr Klein(?), sie setzen gerade MS CRM ein, sind aber unzufrieden und haben über CAS nur gutes gehört.\n"
            + "Anfangen wollen sie \"mal mit 200Usern\" und dann \"ausbauen auf so ca. 1000\". \n"
            + "Schwierigkeit ist hier die Entscheider zu überzeugen. Habe ihm Webinare vorgeschlagen, die er sich evtl. anschauen wird.\n"
            + "Für ihn ist neben Kauf auch Miete/Leasing interessant.\n"
            + "Da er erstmal in der Infosammlungsphase ist, um die Entscheider zu überzeugen, wollte er keine Daten hinterlegen und meldet sich bei Gelegenheit. Explizit darum gebeten, nicht angerufen zu werden.";

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        ReadOnlyFormBuilder readOnlyFormBuilder = new ReadOnlyFormBuilder();
        readOnlyFormBuilder.newZLayout();

        readOnlyFormBuilder.addFieldGroupCaption("General");
        readOnlyFormBuilder.addField("Customer", new ObjectProperty<>("Klein"));
        readOnlyFormBuilder.addField("Person Responsible", new ObjectProperty<>("CAS\\Philipp Sauder"));
        readOnlyFormBuilder.addMultiLineField("Multi line field",
                new ObjectProperty<>("MultiLineLabel is capable to wrap a long text into multiple lines..."));
        readOnlyFormBuilder.addFieldWithEllipsis("Label with ellipsis",
                new ObjectProperty<>("LabelWithEllipsis is a helper component to show the long texts"));

        readOnlyFormBuilder.addFieldGroupCaption("Volume");
        readOnlyFormBuilder.addField("Probability", new ObjectProperty<>("0 %"));
        readOnlyFormBuilder.addField("Total", new ObjectProperty<>("0.00 EUR"));
        readOnlyFormBuilder.addField("Total (weighted)", new ObjectProperty<>("0.00 EUR"));

        readOnlyFormBuilder.addFieldGroupCaption("Acquisition");
        readOnlyFormBuilder.addField("Status", new ObjectProperty<>("processed"));
        readOnlyFormBuilder.addField("Phase old", new ObjectProperty<>("Lead"));
        readOnlyFormBuilder.addField("Lead source", new ObjectProperty<>("Kunde ruft an"));

        readOnlyFormBuilder.addFieldGroupCaption("Time");
        readOnlyFormBuilder.addField("Start", new ObjectProperty<>("Thursday, August 1, 2013"));
        readOnlyFormBuilder.addComponent("Component", new CheckBoxMiddleBuilder<>().selected(true)/* .enabled(false) */
                .padding(0).pass().build());

        readOnlyFormBuilder.addFieldGroupCaption("Additional information");
        readOnlyFormBuilder.addField("Created by", new ObjectProperty<>("Philipp Sauder"));
        readOnlyFormBuilder.addField("Created on", new ObjectProperty<>("Thursday, August 1, 2013, 4:52:58 PM"));
        readOnlyFormBuilder.addField("Modified by", new ObjectProperty<>("Zsolt Borcsok"));
        readOnlyFormBuilder.addField("Modified on", new ObjectProperty<>("Wednesday, January 14, 2015, 8:54:49 AM"));

        readOnlyFormBuilder.addFieldGroupCaption("Authorized persons");
        readOnlyFormBuilder.addFieldWithoutCaption(new ObjectProperty<>("All (public), Philipp Sauder"));

        readOnlyFormBuilder.addSeparateField("Subject", new ObjectProperty<>("Anrufer Normann Unternehmensgruppe"));

        TextAreaMiddleBuilder<?> textAreaBuilder = new TextAreaMiddleBuilder<>().rows(9);
        textAreaBuilder.text(NOTES).enabled(false);
        readOnlyFormBuilder.addSeparateComponent("Notes", textAreaBuilder.build());

        readOnlyFormBuilder.newZLayout();
        readOnlyFormBuilder.addFieldGroupCaption("General");
        readOnlyFormBuilder.addField("Customer", new ObjectProperty<>("Klein"));
        readOnlyFormBuilder.addField("Person Responsible", new ObjectProperty<>("CAS\\Philipp Sauder"));

        readOnlyFormBuilder.addFieldGroupCaption("Volume");
        readOnlyFormBuilder.addField("Probability", new ObjectProperty<>("0 %"));
        readOnlyFormBuilder.addField("Total", new ObjectProperty<>("0.00 EUR"));
        readOnlyFormBuilder.addField("Total (weighted)", new ObjectProperty<>("0.00 EUR"));

        readOnlyFormBuilder.addFieldGroupCaption("Acquisition");
        readOnlyFormBuilder.addField("Status", new ObjectProperty<>("processed"));
        readOnlyFormBuilder.addField("Phase old", new ObjectProperty<>("Lead"));
        readOnlyFormBuilder.addField("Lead source", new ObjectProperty<>("Kunde ruft an"));

        readOnlyFormBuilder.addFieldGroupCaption("Time");
        readOnlyFormBuilder.addField("Start", new ObjectProperty<>("Thursday, August 1, 2013"));

        readOnlyFormBuilder.addFieldGroupCaption("Additional information");
        readOnlyFormBuilder.addField("Created by", new ObjectProperty<>("Philipp Sauder"));
        readOnlyFormBuilder.addField("Created on", new ObjectProperty<>("Thursday, August 1, 2013, 4:52:58 PM"));
        readOnlyFormBuilder.addField("Modified by", new ObjectProperty<>("Zsolt Borcsok"));
        readOnlyFormBuilder.addField("Modified on", new ObjectProperty<>("Wednesday, January 14, 2015, 8:54:49 AM"));

        return readOnlyFormBuilder.componentOnScrollPane();
    }
}
