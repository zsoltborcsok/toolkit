package org.nting.toolkit.component.builder;

import org.nting.toolkit.component.Panel;
import org.nting.toolkit.layout.FormLayout;

public class FormBuilder<PARENT_BUILDER extends ContainerBuilder<?, ?>>
        extends ContainerBuilder<Panel, PARENT_BUILDER> {

    public FormBuilder(FormLayout layoutManager) {
        super(new Panel(layoutManager));
    }

    public FormBuilder(String columns, String rows) {
        super(new Panel(new FormLayout(columns, rows)));
    }

    /** Sets the button with the given layout constraint as default. */
    public FormBuilder<PARENT_BUILDER> defaultButton(Object constraints) {
        getComponent().setDefaultButton(getComponent().componentAt(constraints));
        return this;
    }

    public FormBuilder<PARENT_BUILDER> addColumn(String column) {
        layoutManager().addColumn(column);
        return this;
    }

    public FormBuilder<PARENT_BUILDER> addColumns(String columns) {
        layoutManager().addColumns(columns);
        return this;
    }

    public FormBuilder<PARENT_BUILDER> addRow(String row) {
        layoutManager().addRow(row);
        return this;
    }

    public FormBuilder<PARENT_BUILDER> addRows(String rows) {
        layoutManager().addRows(rows);
        return this;
    }

    public int lastColumn() {
        return layoutManager().lastColumn();
    }

    public int lastColumn(int index) {
        return layoutManager().lastColumn(index);
    }

    public int lastRow() {
        return layoutManager().lastRow();
    }

    public int lastRow(int index) {
        return layoutManager().lastRow(index);
    }

    public FormBuilder<PARENT_BUILDER> clearColumns() {
        layoutManager().clearColumns();
        return this;
    }

    public FormBuilder<PARENT_BUILDER> clearRows() {
        layoutManager().clearRows();
        return this;
    }

    private FormLayout layoutManager() {
        return (FormLayout) getComponent().getLayoutManager();
    }
}
