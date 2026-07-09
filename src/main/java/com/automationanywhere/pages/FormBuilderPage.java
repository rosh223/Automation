package com.automationanywhere.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class FormBuilderPage {
    private final Page page;
    private final FrameLocator frame;

    public FormBuilderPage(Page page) {
        this.page = page;
        this.frame = page.frameLocator("iframe").first();
    }

    public void dragAndDropTextbox() {
        System.out.println("Attempting to add Text Box...");
        Locator textboxTool = frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Text Box"));
        
        try {
            // Some builders add on click, some require drag
            textboxTool.click();
            page.waitForTimeout(1000);
            
            // Drag to center of the frame body just in case
            Locator dropZone = frame.locator("body");
            textboxTool.dragTo(dropZone, new Locator.DragToOptions().setTargetPosition(300, 300));
            page.waitForTimeout(2000);
            System.out.println("Successfully interacted with Text Box tool.");
        } catch (Exception e) {
            System.out.println("Error adding text box: " + e.getMessage());
        }
    }

    public void setPropertiesForElement(String label, String hint, String minLength, String maxLength) {
        clickIfVisible(frame.getByRole(AriaRole.TAB, new FrameLocator.GetByRoleOptions().setName("Properties")));
        clickIfVisible(frame.getByText("Properties", new FrameLocator.GetByTextOptions().setExact(true)));

        fillFirstVisible(label,
            frame.getByLabel("Label", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.getByLabel("Name", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.locator("input[name*='label' i], input[aria-label*='label' i]").first()
        );

        fillFirstVisible(hint,
            frame.getByLabel("Hint text", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.getByLabel("Hint", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.locator("input[name*='hint' i], textarea[name*='hint' i], input[aria-label*='hint' i]").first()
        );

        fillFirstVisible(minLength,
            frame.getByLabel("Minimum length", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.getByLabel("Min length", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.locator("input[name*='min' i], input[aria-label*='min' i]").first()
        );

        fillFirstVisible(maxLength,
            frame.getByLabel("Maximum length", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.getByLabel("Max length", new FrameLocator.GetByLabelOptions().setExact(true)),
            frame.locator("input[name*='max' i], input[aria-label*='max' i]").first()
        );
    }

    public void saveForm() {
        System.out.println("Attempting to save the form...");
        Locator saveBtn = frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Save")).first();
        try {
            saveBtn.click(new Locator.ClickOptions().setForce(true));
            page.waitForTimeout(2000);
            System.out.println("Successfully clicked Save.");
        } catch (Exception e) {
            System.out.println("Error saving form: " + e.getMessage());
        }
    }

    public void navigateToRulesTab() {
        firstVisible(
            frame.getByRole(AriaRole.TAB, new FrameLocator.GetByRoleOptions().setName("Form rules")),
            frame.getByText("Form rules").first(),
            frame.getByText("Rules").first()
        ).click();
        page.waitForTimeout(1000);
    }

    private void clickIfVisible(Locator locator) {
        if (locator.count() > 0 && locator.first().isVisible()) {
            locator.first().click();
            page.waitForTimeout(500);
        }
    }

    private void fillFirstVisible(String value, Locator... locators) {
        for (Locator locator : locators) {
            if (locator.count() > 0 && locator.first().isVisible()) {
                locator.first().fill(value);
                return;
            }
        }
    }

    private Locator firstVisible(Locator... locators) {
        for (int i = 0; i < 20; i++) {
            for (Locator locator : locators) {
                if (locator.count() > 0 && locator.first().isVisible()) {
                    return locator.first();
                }
            }
            page.waitForTimeout(500);
        }
        return locators[0].first();
    }
}
