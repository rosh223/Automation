package com.automationanywhere.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class RulesBuilderPage {
    private final Page page;
    private final FrameLocator frame;

    public RulesBuilderPage(Page page) {
        this.page = page;
        this.frame = page.frameLocator("iframe").first();
    }

    public void createRule(String ruleName) {
        firstVisible(
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Rule")),
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add rule")),
            frame.getByText("Add Rule").first()
        ).click();
        page.waitForTimeout(1000);
        
        // Fill the rule name if a name field appears
        Locator ruleNameField = firstExisting(
            frame.getByPlaceholder("Rule Name"),
            frame.getByPlaceholder("Rule name"),
            frame.getByLabel("Rule Name"),
            frame.getByLabel("Rule name"),
            frame.locator("input[name*='rule' i], input[aria-label*='rule' i]").first()
        );
        if (ruleNameField != null && ruleNameField.isVisible()) {
            ruleNameField.fill(ruleName);
        }
    }

    public void addCondition(String element, String conditionType, boolean isAndMode) {
        firstVisible(
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Condition")),
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add condition")),
            frame.getByText("Add Condition").first()
        ).click();
        page.waitForTimeout(500);
        
        if (isAndMode) {
            Locator andOption = firstExisting(
                frame.getByLabel("AND"),
                frame.getByText("AND").first()
            );
            if (andOption != null && andOption.isVisible()) {
                andOption.check();
            }
        }
        
        // Select element and condition type from dropdowns
        Locator elementSelect = frame.locator("select[name*='element' i], select[aria-label*='element' i]").first();
        if (elementSelect.count() > 0) {
            elementSelect.selectOption(element);
        }
        
        Locator conditionSelect = frame.locator("select[name*='condition' i], select[aria-label*='condition' i]").first();
        if (conditionSelect.count() > 0) {
            conditionSelect.selectOption(conditionType);
        }
    }

    public void addAction(String actionType, String targetElement) {
        firstVisible(
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Action")),
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add action")),
            frame.getByText("Add Action").first()
        ).click();
        page.waitForTimeout(500);
        
        Locator actionSelect = frame.locator("select[name*='action' i], select[aria-label*='action' i]").last();
        if (actionSelect.count() > 0) {
            actionSelect.selectOption(actionType);
        }
        
        Locator targetSelect = frame.locator("select[name*='target' i], select[aria-label*='target' i]").last();
        if (targetSelect.count() > 0) {
            targetSelect.selectOption(targetElement);
        }
    }

    public void addRuleBelow(String ruleName) {
        // Right-click or use context menu on the last rule card
        Locator contextMenu = frame.locator("[class*='context'], [class*='menu'], [aria-label*='more' i], [title*='more' i]").last();
        if (contextMenu.count() > 0) {
            contextMenu.click();
            page.waitForTimeout(500);
        }
        
        Locator addBelow = firstExisting(
            frame.getByText("Add Rule Below"),
            frame.getByText("Add rule below")
        );
        if (addBelow != null && addBelow.isVisible()) {
            addBelow.click();
            page.waitForTimeout(500);
        }
        
        Locator ruleNameField = firstExisting(
            frame.getByPlaceholder("Rule Name"),
            frame.getByPlaceholder("Rule name"),
            frame.getByLabel("Rule Name"),
            frame.getByLabel("Rule name"),
            frame.locator("input[name*='rule' i], input[aria-label*='rule' i]").last()
        );
        if (ruleNameField != null && ruleNameField.isVisible()) {
            ruleNameField.fill(ruleName);
        }
    }
    
    public Locator getRuleCard(String ruleName) {
        return frame.getByText(ruleName);
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

    private Locator firstExisting(Locator... locators) {
        for (int i = 0; i < 20; i++) {
            for (Locator locator : locators) {
                if (locator.count() > 0) {
                    return locator.first();
                }
            }
            page.waitForTimeout(500);
        }
        return null;
    }
}
