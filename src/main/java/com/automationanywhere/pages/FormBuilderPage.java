package com.automationanywhere.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class FormBuilderPage {
    private final Page page;

    public FormBuilderPage(Page page) {
        this.page = page;
    }

    public void dragAndDropTextbox(int index) {
        // Using generic text matcher for the palette tool
        Locator textboxTool = page.locator("text='TextBox'").first();
        Locator canvas = page.locator(".canvas-area"); // Adjust with actual canvas class/ID
        
        textboxTool.dragTo(canvas);
        page.waitForTimeout(500); // Allow UI to render dropped element
    }

    public void setPropertiesForElement(String label, String hint, String minLength, String maxLength) {
        page.getByLabel("Label").fill(label);
        page.getByLabel("Hint text").fill(hint);
        page.getByLabel("Minimum length").fill(minLength);
        page.getByLabel("Maximum length").fill(maxLength);
    }

    public void saveForm() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Save")).click();
    }

    public void navigateToRulesTab() {
        page.getByRole(com.microsoft.playwright.options.AriaRole.TAB, 
            new Page.GetByRoleOptions().setName("Rules")).click();
    }
}
