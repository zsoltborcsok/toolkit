package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;

import java.util.List;
import java.util.UUID;

import org.nting.data.bean.BeanDescriptor;
import org.nting.data.query.ListDataProvider;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.DropDownList;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FormLayout;

import com.google.common.collect.Lists;

public class DropDownListTestPage implements ITestPage {

    private static String[] languageNames = new String[] { "Afrikaans", "Albanian", "Arabic", "Armenian", "Azerbaijani",
            "Basque", "Belarusian", "Bengali", "Bulgarian", "Catalan", "Chinese", "Croatian", "Czech", "Danish",
            "Dutch", "English", "Estonian", "Finnish", "French", "Gallegan", "Georgian", "German", "Greek", "Gujarati",
            "Haitian", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese",
            "Kannada", "Korean", "Latin", "Latvian", "Lithuanian", "Macedonian", "Malay", "Maltese", "Norwegian",
            "Persian", "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish",
            "Swahili", "Swedish", "Tagalog", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese",
            "Welsh", "Yiddish" };
    private static String[] languageCodes = new String[] { "af", "sq", "ar", "hy", "az", "eu", "be", "bn", "bg", "ca",
            "zh-CN", "hr", "cs", "da", "nl", "en", "et", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "iw", "hi",
            "hu", "is", "id", "ga", "it", "ja", "kn", "ko", "la", "lv", "lt", "mk", "ms", "mt", "no", "fa", "pl", "pt",
            "ro", "ru", "sr", "sk", "sl", "es", "sw", "sv", "tl", "ta", "te", "th", "tr", "uk", "ur", "vi", "cy",
            "yi" };

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        FormLayout formLayout = new FormLayout("pref, 7dlu, 0px:grow(2), 7dlu, 0px:grow(1)",
                "pref, 4dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 4dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref, 7dlu, center:pref");
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(formLayout);

        panelBuilder.addLabel(xyw(0, 14, 5)).text("DropDownLists").pass().font(LARGE_FONT, BOLD).end() //
                .addLabel(xy(0, 16)).text("LanguageSelection").end() //
                .addComponent(createLanguageDropDownList(), xy(2, 16)).end() //
                .addLabel(xy(0, 18)).text("With random data").end() //
                .addComponent(createDropDownList(20, true), xy(2, 18)).end() //
                .addLabel(xy(0, 20)).text("No scroll").end() //
                .addComponent(createDropDownList(5, true), xy(2, 20)).end() //
                .addLabel(xy(0, 22)).text("Disabled").end() //
                .addComponent(createDropDownList(20, false), xy(2, 22));

        return wrap(panelBuilder.build());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private DropDownList<Pair<String, String>> createLanguageDropDownList() {
        List<Pair<String, String>> languages = Lists.newLinkedList();
        for (int i = 0; i < languageNames.length; i++) {
            languages.add(new Pair<>(languageNames[i], languageCodes[i]));
        }

        DropDownList<Pair<String, String>> dropDownList = new DropDownList<>();
        dropDownList.setDataProvider(new ListDataProvider<Pair<String, String>>(languages,
                new BeanDescriptor(Pair.class), pair -> pair.second));
        dropDownList.setItemCaptionGenerator(pair -> pair.first);
        return dropDownList;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private DropDownList<String> createDropDownList(int containerSize, boolean enabled) {
        List<String> data = Lists.newLinkedList();
        for (int i = 0; i < containerSize; i++) {
            data.add(UUID.randomUUID().toString());
        }

        DropDownList<String> dropDownList = new DropDownList<>();
        dropDownList.setDataProvider(new ListDataProvider<String>(data, new BeanDescriptor(String.class), t -> t));
        if (!enabled) {
            dropDownList.enabled.setValue(false);
            dropDownList.emptySelectionAllowed.setValue(false);
        } else {
            dropDownList.enabled.setValue(true);
            dropDownList.emptySelectionAllowed.setValue(true);
            dropDownList.emptySelectionCaption.setValue("[NOTHING SELECTED] :>");
        }
        return dropDownList;
    }
}
