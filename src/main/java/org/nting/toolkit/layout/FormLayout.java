package org.nting.toolkit.layout;

import static java.lang.Float.parseFloat;
import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.UnitConverter;
import org.nting.toolkit.util.SimpleMap;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;

/**
 * For using DLU-s, see http://msdn.microsoft.com/en-us/library/ms997619.
 * <p/>
 * By using 'grow' we prepare for filling (it shouldn't be used in dialogs; see calculateColumnWidths(...) method)!
 */
@SuppressWarnings("unused")
public class FormLayout implements LayoutManager {

    private final List<ColumnSpec> columnSpecs;
    private final List<RowSpec> rowSpecs;

    /**
     * Use: new FormLayout( “10dlu, pref, center:50px, max(pref;50px), min(pref;60%), right:50px, 0px:grow”, “5px, pref,
     * 5px, pref, 5px, pref”);
     */
    public FormLayout(String columns, String rows) {
        columnSpecs = Lists.newArrayList();
        rowSpecs = Lists.newArrayList();

        String[] tokenizer = columns.split(",");
        for (String value : tokenizer) {
            addColumn(value.trim());
        }

        tokenizer = rows.split(",");
        for (String s : tokenizer) {
            addRow(s.trim());
        }
    }

    public FormLayout(ColumnSpec[] columnSpecs, RowSpec[] rowSpecs) {
        this.columnSpecs = Lists.newArrayList(columnSpecs);
        this.rowSpecs = Lists.newArrayList(rowSpecs);
    }

    public FormLayout addColumns(String columns) {
        String[] tokenizer = columns.split(",");
        for (String s : tokenizer) {
            addColumn(s.trim());
        }

        return this;
    }

    public FormLayout addColumn(String column) {
        if (Strings.isNullOrEmpty(column)) {
            return this;
        }

        ColumnAlignment alignment = ColumnAlignment.FILL;
        Size size = Size.PREF;
        float grow = 0;

        String[] tokenizer = column.split(":");
        List<String> tokens = Lists.newArrayList();
        for (String item : tokenizer) {
            tokens.add(item.trim());
        }

        int index = 0;
        if (tokens.size() > index) {
            try {
                alignment = ColumnAlignment.valueOf(tokens.get(index).toUpperCase());
                index++;
            } catch (RuntimeException e) {
                // NOTHING To Do
            }
        }

        if (tokens.size() > index) {
            if ("pref".equalsIgnoreCase(tokens.get(index))) {
                size = Size.PREF;
            } else {
                String s = tokens.get(index).toLowerCase();
                if (s.endsWith("px")) {
                    float value = parseFloat(s.substring(0, s.length() - 2));
                    size = Size.createConstant(value);
                } else if (s.endsWith("dlu")) {
                    float value = parseFloat(s.substring(0, s.length() - 3));
                    size = Size.createDlu(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("px)")) {
                    float value = parseFloat(s.substring(9, s.length() - 3));
                    size = Size.createMax(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("dlu)")) {
                    float value = parseFloat(s.substring(9, s.length() - 4));
                    size = Size.createMaxDlu(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("%)")) {
                    float value = parseFloat(s.substring(9, s.length() - 2));
                    size = Size.createMaxPercent(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("px)")) {
                    float value = parseFloat(s.substring(9, s.length() - 3));
                    size = Size.createMin(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("dlu)")) {
                    float value = parseFloat(s.substring(9, s.length() - 4));
                    size = Size.createMinDlu(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("%)")) {
                    float value = parseFloat(s.substring(9, s.length() - 2));
                    size = Size.createMinPercent(value);
                }
            }

            index++;
        }

        if (tokens.size() > index) {
            if ("none".equalsIgnoreCase(tokens.get(index))) {
                grow = 0;
            } else if ("grow".equalsIgnoreCase(tokens.get(index))) {
                grow = 1;
            } else {
                String s = tokens.get(index).toLowerCase();
                if (s.startsWith("grow(") && s.endsWith(")")) {
                    grow = parseFloat(s.substring(5, s.length() - 1));
                }
            }
        }

        columnSpecs.add(new ColumnSpec(alignment, size, grow));
        return this;
    }

    public FormLayout addRows(String rows) {
        String[] tokenizer = rows.split(",");
        for (String s : tokenizer) {
            addRow(s.trim());
        }
        return this;
    }

    public FormLayout addRow(String row) {
        if (Strings.isNullOrEmpty(row)) {
            return this;
        }

        RowAlignment alignment = RowAlignment.FILL;
        Size size = Size.PREF;
        float grow = 0;

        String[] tokenizer = row.split(":");
        List<String> tokens = Lists.newArrayList();
        for (String item : tokenizer) {
            tokens.add(item.trim());
        }

        int index = 0;
        if (tokens.size() > index) {
            try {
                alignment = RowAlignment.valueOf(tokens.get(index).toUpperCase());
                index++;
            } catch (RuntimeException e) {
                // NOTHING To Do
            }
        }

        if (tokens.size() > index) {
            if ("pref".equalsIgnoreCase(tokens.get(index))) {
                size = Size.PREF;
            } else {
                String s = tokens.get(index).toLowerCase();
                if (s.endsWith("px")) {
                    float value = parseFloat(s.substring(0, s.length() - 2));
                    size = Size.createConstant(value);
                } else if (s.endsWith("dlu")) {
                    float value = parseFloat(s.substring(0, s.length() - 3));
                    size = Size.createDlu(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("px)")) {
                    float value = parseFloat(s.substring(9, s.length() - 3));
                    size = Size.createMax(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("dlu)")) {
                    float value = parseFloat(s.substring(9, s.length() - 4));
                    size = Size.createMaxDlu(value);
                } else if (s.startsWith("max(pref;") && s.endsWith("%)")) {
                    float value = parseFloat(s.substring(9, s.length() - 2));
                    size = Size.createMaxPercent(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("px)")) {
                    float value = parseFloat(s.substring(9, s.length() - 3));
                    size = Size.createMin(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("dlu)")) {
                    float value = parseFloat(s.substring(9, s.length() - 4));
                    size = Size.createMinDlu(value);
                } else if (s.startsWith("min(pref;") && s.endsWith("%)")) {
                    float value = parseFloat(s.substring(9, s.length() - 2));
                    size = Size.createMinPercent(value);
                }
            }

            index++;
        }

        if (tokens.size() > index) {
            if ("none".equalsIgnoreCase(tokens.get(index))) {
                grow = 0;
            } else if ("grow".equalsIgnoreCase(tokens.get(index))) {
                grow = 1;
            } else {
                String s = tokens.get(index).toLowerCase();
                if (s.startsWith("grow(") && s.endsWith(")")) {
                    grow = parseFloat(s.substring(5, s.length() - 1));
                }
            }
        }

        rowSpecs.add(new RowSpec(alignment, size, grow));
        return this;
    }

    public int lastColumn() {
        return columnSpecs.size() - 1;
    }

    public int lastColumn(int index) {
        return columnSpecs.size() - 1 - index;
    }

    public int lastRow() {
        return rowSpecs.size() - 1;
    }

    public int lastRow(int index) {
        return rowSpecs.size() - 1 - index;
    }

    public FormLayout clearColumns() {
        columnSpecs.clear();
        return this;
    }

    public FormLayout clearRows() {
        rowSpecs.clear();
        return this;
    }

    @Override
    public void layout(Component component) {
        List<Component> children = component.getComponents();

        SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes = new SimpleMap<>();
        for (Component child : children) {
            if (!child.isVisible()) {
                continue;
            }
            Object childConstraints = component.getLayoutConstraints(child);
            if (childConstraints instanceof CellConstraints) {
                cellConstraintsAndPreferredSizes.put(child,
                        Pair.of((CellConstraints) childConstraints, child.getPreferredSize()));
            } else if (childConstraints instanceof PositionConstraints) {
                PositionConstraints constraints = (PositionConstraints) childConstraints;
                setComponentPosition(child, constraints.x, constraints.y);
                setComponentSize(child, constraints.width, constraints.height);
            }
        }

        List<Float> columnWidths = calculateColumnPositions(component, cellConstraintsAndPreferredSizes);
        List<Float> rowHeights = calculateRowPositions(component, cellConstraintsAndPreferredSizes);

        Multimap<Integer, Component> componentsPerBaseline = ArrayListMultimap.create();
        Map<Integer, Float> positionOfBaselines = Maps.newHashMap();

        // Handle alignments.
        for (Map.Entry<Component, Pair<CellConstraints, Dimension>> entry : cellConstraintsAndPreferredSizes
                .entries()) {
            Component child = entry.getKey();
            CellConstraints constraints = entry.getValue().first;
            Dimension prefSize = entry.getValue().second;

            float x1 = constraints.x == 0 ? 0 : columnWidths.get(constraints.x - 1);
            float y1 = constraints.y == 0 ? 0 : rowHeights.get(constraints.y - 1);
            float x2 = columnWidths.get(constraints.x + constraints.width - 1);
            float y2 = rowHeights.get(constraints.y + constraints.height - 1);
            float width = x2 - x1;
            float height = y2 - y1;

            if (constraints.width == 1) {
                ColumnSpec columnSpec = columnSpecs.get(constraints.x);
                switch (columnSpec.alignment) {
                case CENTER:
                    if (prefSize.width < width) {
                        float d = MathUtil.round((width - prefSize.width) / 2.0f);
                        x1 = x1 + d;
                        width = prefSize.width;
                    }
                    break;
                case LEFT:
                    if (prefSize.width < width) {
                        width = prefSize.width;
                    }
                    break;
                case RIGHT:
                    if (prefSize.width < width) {
                        x1 = x2 - prefSize.width;
                        width = prefSize.width;
                    }
                    break;
                case FILL:
                    break;
                }
            }
            if (constraints.height == 1) {
                RowSpec rowSpec = rowSpecs.get(constraints.y);
                switch (rowSpec.alignment) {
                case CENTER:
                    if (prefSize.height < height) {
                        float d = MathUtil.round((height - prefSize.height) / 2.0f);
                        y1 = y1 + d;
                        height = prefSize.height;
                    }
                    break;
                case TOP:
                    if (prefSize.height < height) {
                        height = prefSize.height;
                    }
                    break;
                case BOTTOM:
                    if (prefSize.height < height) {
                        y1 = y2 - prefSize.height;
                        height = prefSize.height;
                    }
                    break;
                case BASELINE:
                    if (prefSize.height < height) {
                        y1 = y2 - prefSize.height;
                        height = prefSize.height;
                    }

                    float baselinePosition = child.getBaselinePosition();
                    if (0 <= baselinePosition) {
                        if (!positionOfBaselines.containsKey(constraints.y)) {
                            positionOfBaselines.put(constraints.y, baselinePosition);
                        } else {
                            float currentBaselinePosition = positionOfBaselines.get(constraints.y);
                            if (baselinePosition <= currentBaselinePosition) {
                                y1 -= currentBaselinePosition - baselinePosition;
                            } else {
                                for (Component c : componentsPerBaseline.get(constraints.y)) {
                                    Point position = c.getPosition();
                                    c.setPosition(position.x,
                                            position.y - (baselinePosition - currentBaselinePosition));
                                }
                                positionOfBaselines.put(constraints.y, baselinePosition);
                            }
                        }
                        componentsPerBaseline.put(constraints.y, child);
                    }
                    break;
                case FILL:
                    break;
                }
            }

            setComponentPosition(child, x1, y1);
            setComponentSize(child, width, height);
        }
    }

    private List<Float> calculateRowPositions(Component component,
            SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes) {
        List<Float> rowHeights = calculateRowHeights(component, cellConstraintsAndPreferredSizes);
        float heightSum = 0;
        for (Float rowHeight : rowHeights) {
            heightSum += rowHeight;
        }
        if (heightSum < component.getSize().height) {// Grow
            float heightToGrow = component.getSize().height - heightSum;

            float growSumma = 0;
            for (RowSpec rowSpec : rowSpecs) {
                growSumma += rowSpec.grow;
            }

            int remainingToGrow = MathUtil.round(heightToGrow);
            for (int i = 0; i < rowSpecs.size(); i++) {
                RowSpec rowSpec = rowSpecs.get(i);
                if (rowSpec.grow > 0) {
                    int d = MathUtil.round(heightToGrow * rowSpec.grow / growSumma);
                    if (remainingToGrow <= d) {
                        rowHeights.set(i, rowHeights.get(i) + remainingToGrow);
                        break;
                    } else {
                        rowHeights.set(i, rowHeights.get(i) + d);
                        remainingToGrow -= d;
                    }
                }
            }
        }
        for (int i = 1; i < rowHeights.size(); i++) {
            rowHeights.set(i, rowHeights.get(i - 1) + rowHeights.get(i));
        }
        return rowHeights;
    }

    private List<Float> calculateRowHeights(Component component,
            SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes) {
        UnitConverter unitConverter = unitConverter();

        List<Float> rowHeights = new ArrayList<>(rowSpecs.size());
        for (int i = 0; i < rowSpecs.size(); i++) {
            RowSpec rowSpec = rowSpecs.get(i);
            if (rowSpec.size.type == SizeType.CONSTANT) {
                rowHeights.add(rowSpec.size.unit);
            } else if (rowSpec.size.type == SizeType.DLU) {
                rowHeights.add((float) unitConverter.dialogUnitYAsPixel(rowSpec.size.unit, component));
            } else {
                float rowHeight = 0;
                for (Map.Entry<Component, Pair<CellConstraints, Dimension>> entry : cellConstraintsAndPreferredSizes
                        .entries()) {
                    CellConstraints constraints = entry.getValue().first;
                    if (constraints.y == i && constraints.height == 1) {
                        float preferredHeight = entry.getValue().second.height;
                        if (rowHeight < preferredHeight) {
                            rowHeight = preferredHeight;
                        }
                    }
                }

                if (rowSpec.size.type == SizeType.MAX) {
                    rowHeight = Math.max(rowHeight, rowSpec.size.unit);
                } else if (rowSpec.size.type == SizeType.MAX_DLU) {
                    rowHeight = Math.max(rowHeight,
                            (float) unitConverter.dialogUnitYAsPixel(rowSpec.size.unit, component));
                } else if (rowSpec.size.type == SizeType.MAX_PERCENT) {
                    rowHeight = Math.max(rowHeight, component.getSize().height * rowSpec.size.unit * 0.01f);
                } else if (rowSpec.size.type == SizeType.MIN) {
                    rowHeight = Math.min(rowHeight, rowSpec.size.unit);
                } else if (rowSpec.size.type == SizeType.MIN_DLU) {
                    rowHeight = Math.min(rowHeight,
                            (float) unitConverter.dialogUnitYAsPixel(rowSpec.size.unit, component));
                } else if (rowSpec.size.type == SizeType.MIN_PERCENT) {
                    rowHeight = Math.min(rowHeight, component.getSize().height * rowSpec.size.unit * 0.01f);
                }

                rowHeights.add(rowHeight);
            }

            for (Map.Entry<Component, Pair<CellConstraints, Dimension>> entry : cellConstraintsAndPreferredSizes
                    .entries()) {
                CellConstraints constraints = entry.getValue().first;
                if (constraints.y + constraints.height - 1 == i && constraints.height > 1) {
                    float preferredHeight = entry.getValue().second.height;
                    int lastRow = -1;
                    float sumRows = 0.0f;
                    for (int k = 0; k < constraints.height; k++) {
                        sumRows += rowHeights.get(constraints.y + k);
                        if (rowSpecs.get(constraints.y + k).grow > 0) {
                            lastRow = -1;
                            break;
                        } else {
                            SizeType type = rowSpecs.get(constraints.y + k).size.type;
                            if (type == SizeType.PREF) {
                                lastRow = constraints.y + k;
                            } else if (type == SizeType.MAX || type == SizeType.MAX_DLU || type == SizeType.MAX_PERCENT
                                    || type == SizeType.MIN || type == SizeType.MIN_DLU
                                    || type == SizeType.MIN_PERCENT) {
                                if (lastRow == -1) { // The lastRow is preferred NOT to be a MAX/MIN row!
                                    lastRow = constraints.x + k;
                                }
                            }
                        }
                    }

                    if (0 <= lastRow && sumRows < preferredHeight) {// Greedy algorithm, for the least height
                        Size size = rowSpecs.get(lastRow).size;
                        float newRowHeight = rowHeights.get(lastRow) + preferredHeight - sumRows;
                        if (size.type == SizeType.MIN) { // MIN sizes means upper bounds. So, consider those limits!
                            newRowHeight = Math.min(newRowHeight, size.unit);
                        } else if (size.type == SizeType.MIN_DLU) {
                            newRowHeight = Math.min(newRowHeight,
                                    (float) unitConverter.dialogUnitYAsPixel(size.unit, component));
                        } else if (size.type == SizeType.MIN_PERCENT) {
                            newRowHeight = Math.min(newRowHeight, component.getSize().height * size.unit * 0.01f);
                        }
                        rowHeights.set(lastRow, newRowHeight);
                    }
                }
            }
        }
        return rowHeights;
    }

    private List<Float> calculateColumnPositions(Component component,
            SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes) {
        List<Float> columnWidths = calculateColumnWidths(component, cellConstraintsAndPreferredSizes);
        float widthSum = 0;
        for (Float columnWidth : columnWidths) {
            widthSum += columnWidth;
        }
        if (widthSum < component.getSize().width) {// Grow
            float widthToGrow = component.getSize().width - widthSum;

            float growSumma = 0;
            for (ColumnSpec columnSpec : columnSpecs) {
                growSumma += columnSpec.grow;
            }

            int remainingToGrow = MathUtil.round(widthToGrow);
            for (int i = 0; i < columnSpecs.size(); i++) {
                ColumnSpec columnSpec = columnSpecs.get(i);
                if (columnSpec.grow > 0) {
                    int d = MathUtil.round(widthToGrow * columnSpec.grow / growSumma);
                    if (remainingToGrow <= d) {
                        columnWidths.set(i, columnWidths.get(i) + remainingToGrow);
                        break;
                    } else {
                        columnWidths.set(i, columnWidths.get(i) + d);
                        remainingToGrow -= d;
                    }
                }
            }
        }
        for (int i = 1; i < columnWidths.size(); i++) {
            columnWidths.set(i, columnWidths.get(i - 1) + columnWidths.get(i));
        }
        return columnWidths;
    }

    private List<Float> calculateColumnWidths(Component component,
            SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes) {
        UnitConverter unitConverter = unitConverter();

        List<Float> columnWidths = new ArrayList<>(columnSpecs.size());
        for (int i = 0; i < columnSpecs.size(); i++) {
            ColumnSpec columnSpec = columnSpecs.get(i);
            if (columnSpec.size.type == SizeType.CONSTANT) {
                columnWidths.add(columnSpec.size.unit);
            } else if (columnSpec.size.type == SizeType.DLU) {
                columnWidths.add((float) unitConverter.dialogUnitXAsPixel(columnSpec.size.unit, component));
            } else {
                float columnWidth = 0;
                for (Map.Entry<Component, Pair<CellConstraints, Dimension>> entry : cellConstraintsAndPreferredSizes
                        .entries()) {
                    CellConstraints constraints = entry.getValue().first;
                    if (constraints.x == i && constraints.width == 1) {
                        float preferredWidth = entry.getValue().second.width;
                        if (columnWidth < preferredWidth) {
                            columnWidth = preferredWidth;
                        }
                    }
                }

                if (columnSpec.size.type == SizeType.MAX) {
                    columnWidth = Math.max(columnWidth, columnSpec.size.unit);
                } else if (columnSpec.size.type == SizeType.MAX_DLU) {
                    columnWidth = Math.max(columnWidth,
                            (float) unitConverter.dialogUnitXAsPixel(columnSpec.size.unit, component));
                } else if (columnSpec.size.type == SizeType.MAX_PERCENT) {
                    columnWidth = Math.max(columnWidth, component.getSize().width * columnSpec.size.unit * 0.01f);
                } else if (columnSpec.size.type == SizeType.MIN) {
                    columnWidth = Math.min(columnWidth, columnSpec.size.unit);
                } else if (columnSpec.size.type == SizeType.MIN_DLU) {
                    columnWidth = Math.min(columnWidth,
                            (float) unitConverter.dialogUnitXAsPixel(columnSpec.size.unit, component));
                } else if (columnSpec.size.type == SizeType.MIN_PERCENT) {
                    columnWidth = Math.min(columnWidth, component.getSize().width * columnSpec.size.unit * 0.01f);
                }

                columnWidths.add(columnWidth);
            }

            for (Map.Entry<Component, Pair<CellConstraints, Dimension>> entry : cellConstraintsAndPreferredSizes
                    .entries()) {
                CellConstraints constraints = entry.getValue().first;
                if (constraints.x + constraints.width - 1 == i && constraints.width > 1) {
                    float preferredWidth = entry.getValue().second.width;
                    int lastColumn = -1;
                    float sumColumns = 0.0f;
                    for (int k = 0; k < constraints.width; k++) {
                        sumColumns += columnWidths.get(constraints.x + k);
                        if (columnSpecs.get(constraints.x + k).grow > 0) {
                            lastColumn = -1;
                            break;
                        } else {
                            SizeType type = columnSpecs.get(constraints.x + k).size.type;
                            if (type == SizeType.PREF) {
                                lastColumn = constraints.x + k;
                            } else if (type == SizeType.MAX || type == SizeType.MAX_DLU || type == SizeType.MAX_PERCENT
                                    || type == SizeType.MIN || type == SizeType.MIN_DLU
                                    || type == SizeType.MIN_PERCENT) {
                                if (lastColumn == -1) { // The lastColumn is preferred NOT to be a MAX/MIN column!
                                    lastColumn = constraints.x + k;
                                }
                            }
                        }
                    }

                    if (0 <= lastColumn && sumColumns < preferredWidth) {// Greedy algorithm, for the least width
                        Size size = columnSpecs.get(lastColumn).size;
                        float newColumnWidth = columnWidths.get(lastColumn) + preferredWidth - sumColumns;
                        if (size.type == SizeType.MIN) { // MIN sizes means upper bounds. So, consider those limits!
                            newColumnWidth = Math.min(newColumnWidth, size.unit);
                        } else if (size.type == SizeType.MIN_DLU) {
                            newColumnWidth = Math.min(newColumnWidth,
                                    (float) unitConverter.dialogUnitXAsPixel(size.unit, component));
                        } else if (size.type == SizeType.MIN_PERCENT) {
                            newColumnWidth = Math.min(newColumnWidth, component.getSize().width * size.unit * 0.01f);
                        }
                        columnWidths.set(lastColumn, newColumnWidth);
                    }
                }
            }
        }
        return columnWidths;
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        List<Component> children = component.getComponents();

        SimpleMap<Component, Pair<CellConstraints, Dimension>> cellConstraintsAndPreferredSizes = new SimpleMap<>();
        for (Component child : children) {
            if (!child.isVisible()) {
                continue;
            }
            Object childConstraints = component.getLayoutConstraints(child);
            if (childConstraints instanceof CellConstraints) {
                cellConstraintsAndPreferredSizes.put(child,
                        Pair.of((CellConstraints) childConstraints, child.getPreferredSize()));
            }
        }

        List<Float> columnWidths = calculateColumnWidths(component, cellConstraintsAndPreferredSizes);
        float width = 0;
        for (Float columnWidth : columnWidths) {
            width += columnWidth;
        }
        List<Float> rowHeights = calculateRowHeights(component, cellConstraintsAndPreferredSizes);
        float height = 0;
        for (Float rowHeight : rowHeights) {
            height += rowHeight;
        }

        // Dimension parentSize = component.getParent().getSize();
        // if (component.getParent() != null) { CAUSED A BUG!!!
        // if (parentSize.width > width) {
        // width = parentSize.width;
        // }
        // if (parentSize.height > height) {
        // height = parentSize.height;
        // }
        // }
        return new Dimension(width, height);
    }

    public enum ColumnAlignment {
        FILL, LEFT, CENTER, RIGHT
    }

    public enum RowAlignment {
        FILL, TOP, CENTER, BOTTOM, BASELINE
    }

    public enum SizeType {
        CONSTANT, PREF, DLU, MAX, MAX_DLU, MAX_PERCENT, MIN, MIN_DLU, MIN_PERCENT
    }

    // Size: CONSTANT(float), PREF, MAX(PREF; CONSTANT), MIN(PREF; CONSTANT)
    public static class Size {
        public static Size PREF = new Size(SizeType.PREF, 0);

        public final SizeType type;
        public final float unit;

        private Size(SizeType type, float unit) {
            this.type = type;
            this.unit = unit;
        }

        public static Size createConstant(float unit) {
            return new Size(SizeType.CONSTANT, unit);
        }

        public static Size createDlu(float unit) {
            return new Size(SizeType.DLU, unit);
        }

        public static Size createMax(float unit) {
            return new Size(SizeType.MAX, unit);
        }

        public static Size createMaxDlu(float unit) {
            return new Size(SizeType.MAX_DLU, unit);
        }

        public static Size createMaxPercent(float unit) {
            return new Size(SizeType.MAX_PERCENT, unit);
        }

        public static Size createMin(float unit) {
            return new Size(SizeType.MIN, unit);
        }

        public static Size createMinDlu(float unit) {
            return new Size(SizeType.MIN_DLU, unit);
        }

        public static Size createMinPercent(float unit) {
            return new Size(SizeType.MIN_PERCENT, unit);
        }
    }

    // ResizeBehaviour: NONE(0.0), UNIT_GROW(1.0), GROW(float)
    public static class ColumnSpec {
        public final ColumnAlignment alignment;
        public final Size size;
        public final float grow;

        public ColumnSpec(Size size) {
            this(ColumnAlignment.FILL, size, size.type == SizeType.CONSTANT ? 0 : 1);
        }

        public ColumnSpec(Size size, float grow) {
            this(ColumnAlignment.FILL, size, grow);
        }

        public ColumnSpec(ColumnAlignment alignment, Size size) {
            this(alignment, size, 1.0f);
        }

        public ColumnSpec(ColumnAlignment alignment, Size size, float grow) {
            this.alignment = alignment;
            this.size = size;
            this.grow = grow;
        }
    }

    public static class RowSpec {
        public final RowAlignment alignment;
        public final Size size;
        public final float grow;

        public RowSpec(Size size) {
            this(RowAlignment.FILL, size, 1.0f);
        }

        public RowSpec(Size size, float grow) {
            this(RowAlignment.FILL, size, grow);
        }

        public RowSpec(RowAlignment alignment, Size size) {
            this(alignment, size, 1.0f);
        }

        public RowSpec(RowAlignment alignment, Size size, float grow) {
            this.alignment = alignment;
            this.size = size;
            this.grow = grow;
        }
    }

    public static CellConstraints xy(int x, int y) {
        return new CellConstraints(x, y, 1, 1);
    }

    public static CellConstraints xyw(int x, int y, int width) {
        return new CellConstraints(x, y, width, 1);
    }

    public static CellConstraints xyh(int x, int y, int height) {
        return new CellConstraints(x, y, 1, height);
    }

    public static CellConstraints xywh(int x, int y, int width, int height) {
        return new CellConstraints(x, y, width, height);
    }

    public static class CellConstraints {
        private final int x, y, width, height;

        public CellConstraints(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean containsColumn(int i) {
            return x <= i && i < x + width;
        }

        public boolean containsRow(int i) {
            return y <= i && i < y + height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, width, height);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CellConstraints that = (CellConstraints) o;
            return height == that.height && width == that.width && x == that.x && y == that.y;
        }
    }

    public static PositionConstraints fixPositionAndSize(float x, float y, float width, float height) {
        return new PositionConstraints(x, y, width, height);
    }

    public static class PositionConstraints {
        private final float x, y, width, height;

        public PositionConstraints(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, width, height);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PositionConstraints that = (PositionConstraints) o;
            return height == that.height && width == that.width && x == that.x && y == that.y;
        }
    }
}
