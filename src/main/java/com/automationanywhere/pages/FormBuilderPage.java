package com.automationanywhere.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class FormBuilderPage {
    private final Page page;

    public FormBuilderPage(Page page) {
        this.page = page;
    }

    public void dragAndDropTextbox() {
        Locator textboxTool = firstVisible(
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Text Box").setExact(true)),
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("TextBox").setExact(true)),
            page.getByText("Text Box", new Page.GetByTextOptions().setExact(true)),
            page.getByText("Textbox", new Page.GetByTextOptions().setExact(true)),
            page.locator("[aria-label*='Text Box' i], [title*='Text Box' i], [data-name*='text' i], [data-testid*='text' i]").first()
        );

        textboxTool.click();
        page.waitForTimeout(1000);
    }

    public void setPropertiesForElement(String label, String hint, String minLength, String maxLength) {
        clickIfVisible(page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Properties")));
        clickIfVisible(page.getByText("Properties", new Page.GetByTextOptions().setExact(true)));

        fillFirstVisible(label,
            page.getByLabel("Label", new Page.GetByLabelOptions().setExact(true)),
            page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)),
            page.locator("input[name*='label' i], input[aria-label*='label' i]").first()
        );

        fillFirstVisible(hint,
            page.getByLabel("Hint text", new Page.GetByLabelOptions().setExact(true)),
            page.getByLabel("Hint", new Page.GetByLabelOptions().setExact(true)),
            page.locator("input[name*='hint' i], textarea[name*='hint' i], input[aria-label*='hint' i]").first()
        );

        fillFirstVisible(minLength,
            page.getByLabel("Minimum length", new Page.GetByLabelOptions().setExact(true)),
            page.getByLabel("Min length", new Page.GetByLabelOptions().setExact(true)),
            page.locator("input[name*='min' i], input[aria-label*='min' i]").first()
        );

        fillFirstVisible(maxLength,
            page.getByLabel("Maximum length", new Page.GetByLabelOptions().setExact(true)),
            page.getByLabel("Max length", new Page.GetByLabelOptions().setExact(true)),
            page.locator("input[name*='max' i], input[aria-label*='max' i]").first()
        );
    }

    public void saveForm() {
        firstVisible(
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save").setExact(true)),
            page.locator("[aria-label='Save'], [title='Save']").first()
        ).click();
        page.waitForTimeout(2000);
    }

    public void navigateToRulesTab() {
        firstVisible(
            page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Form rules")),
            page.getByText("Form rules").first(),
            page.getByText("Rules").first()
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
        for (Locator locator : locators) {
            if (locator.count() > 0 && locator.first().isVisible()) {
                return locator.first();
            }
        }

        return locators[0].first();
    }
}
