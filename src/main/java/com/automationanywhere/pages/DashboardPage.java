package com.automationanywhere.pages;

import com.microsoft.playwright.Page;

public class DashboardPage {
    private final Page page;

    public DashboardPage(Page page) {
        this.page = page;
    }

    public void navigateToAutomation() {
        page.getByLabel("Automation", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForLoadState();
        page.waitForTimeout(3000);
    }

    public void createNewForm(String formName) {
        // Click "Create" button (use first() since there are two Create buttons)
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Create").setExact(true)).first().click();
        page.waitForTimeout(1000);
        
        // Select "Form…" from the dropdown
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Form")).click();
        page.waitForTimeout(2000);

        // Fill form name using the label "Name" (placeholder is "Required")
        page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)).fill(formName);
        
        // Click "Create & edit" (lowercase 'e')
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Create & edit")).click();
        page.waitForLoadState();
        page.waitForTimeout(5000); // Wait for form builder to fully load
    }
}
