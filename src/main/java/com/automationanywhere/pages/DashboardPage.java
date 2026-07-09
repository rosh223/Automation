package com.automationanywhere.pages;

import com.microsoft.playwright.Page;

public class DashboardPage {
    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public void navigateToAutomation() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, 
            new Page.GetByRoleOptions().setName("Automation")).click();
        page.waitForLoadState();
    }

    public void createNewForm(String formName) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Create New")).click();
        page.getByText("Form").click();
        page.getByPlaceholder("Name", new Page.GetByPlaceholderOptions().setExact(false)).fill(formName);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Create & Edit")).click();
        page.waitForLoadState();
    }
}
