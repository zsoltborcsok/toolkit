package org.nting.toolkit.app.pages;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;

public interface ITestPage {

    PageSize getPageSize();

    Component createContent();
}
