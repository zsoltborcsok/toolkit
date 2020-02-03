package org.nting.toolkit.component.builder;

import org.nting.toolkit.layout.FormLayout;

public class FormLayoutMiddleBuilder<T extends ContainerBuilder<?, ?>> {

    private final T containerBuilder;
    private final FormLayout layoutManager;

    public FormLayoutMiddleBuilder(T containerBuilder, FormLayout layoutManager) {
        this.containerBuilder = containerBuilder;
        this.layoutManager = layoutManager;
    }

    public FormLayoutMiddleBuilder<T> addColumn(String column) {
        layoutManager.addColumn(column);
        return this;
    }

    public FormLayoutMiddleBuilder<T> addColumns(String columns) {
        layoutManager.addColumns(columns);
        return this;
    }

    public FormLayoutMiddleBuilder<T> addRow(String row) {
        layoutManager.addRow(row);
        return this;
    }

    public FormLayoutMiddleBuilder<T> addRows(String rows) {
        layoutManager.addRows(rows);
        return this;
    }

    public FormLayoutMiddleBuilder<T> clearColumns() {
        layoutManager.clearColumns();
        return this;
    }

    public FormLayoutMiddleBuilder<T> clearRows() {
        layoutManager.clearRows();
        return this;
    }

    public int lastColumn() {
        return layoutManager.lastColumn();
    }

    public int lastColumn(int index) {
        return layoutManager.lastColumn(index);
    }

    public int lastRow() {
        return layoutManager.lastRow();
    }

    public int lastRow(int index) {
        return layoutManager.lastRow(index);
    }

    public T done() {
        return containerBuilder;
    }
}
