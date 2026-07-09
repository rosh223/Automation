package com.automationanywhere.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FormBuilderPage {
    private final Page page;
    private final FrameLocator frame;

    public FormBuilderPage(Page page) {
        this.page = page;
        // The form builder content lives inside an iframe
        this.frame = page.frameLocator("iframe").first();
    }

    public void dragAndDropTextbox() {
        // The palette button is "Text Box" (with a space), inside the iframe
        Locator textboxTool = frame.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new FrameLocator.GetByRoleOptions().setName("Text Box").setExact(true));
        
        // Click the Text Box button to add it to the canvas (many form builders use click-to-add)
        textboxTool.click();
        page.waitForTimeout(1000);
    }

    public void setPropertiesForElement(String label, String hint, String minLength, String maxLength) {
        // Switch to the Properties tab inside the iframe
        frame.getByRole(com.microsoft.playwright.options.AriaRole.TAB,
            new FrameLocator.GetByRoleOptions().setName("Properties")).click();
        page.waitForTimeout(500);

        // Fill in properties - these selectors may need adjustment based on the actual property panel
        Locator labelField = frame.getByLabel("Label");
        if (labelField.count() > 0) {
            labelField.first().fill(label);
        }

        Locator hintField = frame.getByLabel("Hint text");
        if (hintField.count() > 0) {
            hintField.first().fill(hint);
        }

        Locator minField = frame.getByLabel("Minimum length");
        if (minField.count() > 0) {
            minField.first().fill(minLength);
        }

        Locator maxField = frame.getByLabel("Maximum length");
        if (maxField.count() > 0) {
            maxField.first().fill(maxLength);
        }
    }

    public void saveForm() {
        // Save button might be inside the iframe or on the main page
        Locator saveInFrame = frame.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new FrameLocator.GetByRoleOptions().setName("Save"));
        if (saveInFrame.count() > 0) {
            saveInFrame.click();
        } else {
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save")).click();
        }
        page.waitForTimeout(2000);
    }

    public void navigateToRulesTab() {
        // Tab is "Form rules (0)" or "Form rules (N)" inside the iframe
        frame.getByRole(com.microsoft.playwright.options.AriaRole.TAB,
            new FrameLocator.GetByRoleOptions().setName("Form rules")).click();
        page.waitForTimeout(1000);
    }
}
