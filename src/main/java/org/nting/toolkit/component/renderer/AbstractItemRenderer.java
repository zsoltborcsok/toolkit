package org.nting.toolkit.component.renderer;

import static org.nting.toolkit.util.GwtCompatibleUtils.abbreviate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.GwtCompatibleUtils;
import org.nting.toolkit.util.SimpleMap;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public abstract class AbstractItemRenderer<T> implements ItemRenderer<T> {

    private static final Joiner VALUE_JOINER = Joiner.on(", ");

    private final Function<T, String>[] searchPropertyGetters;

    private String searchText;
    private final SimpleMap<T, Integer> foundItemMatches = new SimpleMap<>();
    private int highlightMatchIndex = -1;

    /** How to convert a getter: stringGetter(ValueChangeListener::priority, StringConverters.intConverter()); */
    @SuppressWarnings("unchecked")
    public AbstractItemRenderer(Function<T, String>... searchPropertyGetters) {
        this.searchPropertyGetters = searchPropertyGetters;
    }

    @Override
    public int search(String searchText, List<T> items) {
        foundItemMatches.clear();
        if (Strings.isNullOrEmpty(searchText)) {
            this.searchText = null;
            return 0;
        }
        int count = 0;
        for (T item : items) {
            for (Function<T, String> searchPropertyGetter : searchPropertyGetters) {
                String propertyValue = searchPropertyGetter.apply(item);
                if (!Strings.isNullOrEmpty(propertyValue)) {
                    int foundMatches = GwtCompatibleUtils.countMatches(propertyValue.toLowerCase(), searchText);
                    foundItemMatches.put(item, foundMatches);
                    count += foundMatches;
                }
            }
        }

        this.searchText = 0 < count ? searchText : null;
        return count;
    }

    @Override
    public T highlightMatch(int index) {
        highlightMatchIndex = index;
        if (0 <= index) {
            return getItemOfMatch(index);
        } else {
            return null;
        }
    }

    protected void paintSearch(T item, TextContent... textContents) {
        int matchIndex = highlightMatchIndex - getHighlightMatchStartIndex(item);
        if (matchIndex < 0) {
            for (TextContent textContent : textContents) {
                textContent.search(searchText);
                textContent.highlightMatch(-1);
            }
        } else {
            for (TextContent textContent : textContents) {
                int matches = textContent.search(searchText);
                if (matches <= matchIndex) {
                    matchIndex -= matches;
                    textContent.highlightMatch(-1);
                } else {
                    textContent.highlightMatch(matchIndex);
                    matchIndex = -1;
                }
            }
        }
    }

    private int getHighlightMatchStartIndex(T item) {
        int highlightMatchStartIndex = 0;
        List<Map.Entry<T, Integer>> foundMatches = Lists.reverse(foundItemMatches.entries());
        for (Map.Entry<T, Integer> match : foundMatches) {
            if (item == match.getKey()) {
                return highlightMatchStartIndex;
            } else {
                highlightMatchStartIndex += match.getValue();
            }
        }

        return highlightMatchIndex + 1;
    }

    private T getItemOfMatch(int index) {
        int matches = 0;
        List<Map.Entry<T, Integer>> foundMatches = Lists.reverse(foundItemMatches.entries());
        for (Map.Entry<T, Integer> match : foundMatches) {
            if (index < matches + match.getValue()) {
                return match.getKey();
            } else {
                matches += match.getValue();
            }
        }

        return null;
    }

    protected String getValueOrDefault(String value, String defaultValue) {
        return !Strings.isNullOrEmpty(value) ? getAbbreviatedValue(value) : defaultValue;
    }

    protected String getConcatenatedValue(T item, Function<T, String>... propertyGetters) {
        List<String> values = Lists.newLinkedList();
        for (Function<T, String> propertyGetter : propertyGetters) {
            String value = getAbbreviatedValue(propertyGetter.apply(item));
            if (!Strings.isNullOrEmpty(value)) {
                values.add(value);
            }
        }
        return VALUE_JOINER.join(values);
    }

    protected String getAbbreviatedValue(String value) {
        return abbreviate(value, 160);
    }

    public static <T, R> Function<T, String> stringGetter(Function<T, R> getter, Function<R, String> toString) {
        return getter.andThen(toString);
    }
}
