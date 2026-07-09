package com.automationanywhere.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class RulesBuilderPage {
    private final Page page;

    public RulesBuilderPage(Page page) {
        this.page = page;
    }

    public void createRule(String ruleName) {
        firstVisible(
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Rule")),
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add rule")),
            page.getByText("Add Rule").first()
        ).click();
        page.waitForTimeout(1000);
        
        // Fill the rule name if a name field appears
        Locator ruleNameField = firstExisting(
            page.getByPlaceholder("Rule Name"),
            page.getByPlaceholder("Rule name"),
            page.getByLabel("Rule Name"),
            page.getByLabel("Rule name"),
            page.locator("input[name*='rule' i], input[aria-label*='rule' i]").first()
        );
        if (ruleNameField != null && ruleNameField.isVisible()) {
            ruleNameField.fill(ruleName);
        }
    }

    public void addCondition(String element, String conditionType, boolean isAndMode) {
        firstVisible(
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Condition")),
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add condition")),
            page.getByText("Add Condition").first()
        ).click();
        page.waitForTimeout(500);
        
        if (isAndMode) {
            Locator andOption = page.getByLabel("AND");
            if (andOption != null && andOption.isVisible()) {
                andOption.check();
            } else {
                Locator andText = page.getByText("AND", new Page.GetByTextOptions().setExact(true)).first();
                if (andText.count() > 0 && andText.isVisible()) {
                    andText.click();
                }
            }
        }
        
        // Select element and condition type from dropdowns
        Locator elementSelect = page.locator("select[name*='element' i], select[aria-label*='element' i]").last();
        if (elementSelect.count() > 0) {
            elementSelect.selectOption(element);
        }
        
        Locator conditionSelect = page.locator("select[name*='condition' i], select[aria-label*='condition' i]").last();
        if (conditionSelect.count() > 0) {
            conditionSelect.selectOption(conditionType);
        }
    }

    public void addAction(String actionType, String targetElement) {
        firstVisible(
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Action")),
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add action")),
            page.getByText("Add Action").first()
        ).click();
        page.waitForTimeout(500);
        
        Locator actionSelect = page.locator("select[name*='action' i], select[aria-label*='action' i]").last();
        if (actionSelect.count() > 0) {
            actionSelect.selectOption(actionType);
        }
        
        Locator targetSelect = page.locator("select[name*='target' i], select[aria-label*='target' i]").last();
        if (targetSelect.count() > 0) {
            targetSelect.selectOption(targetElement);
        }
    }

    public void addRuleBelow(String ruleName) {
        // Right-click or use context menu on the last rule card
        Locator contextMenu = page.locator("[class*='context'], [class*='menu'], [aria-label*='more' i], [title*='more' i]").last();
        if (contextMenu.count() > 0) {
            contextMenu.click();
            page.waitForTimeout(500);
        }
        
        Locator addBelow = firstExisting(
            page.getByText("Add Rule Below"),
            page.getByText("Add rule below")
        );
        if (addBelow != null && addBelow.isVisible()) {
            addBelow.click();
            page.waitForTimeout(500);
        }
        
        Locator ruleNameField = firstExisting(
            page.getByPlaceholder("Rule Name"),
            page.getByPlaceholder("Rule name"),
            page.getByLabel("Rule Name"),
            page.getByLabel("Rule name"),
            page.locator("input[name*='rule' i], input[aria-label*='rule' i]").last()
        );
        if (ruleNameField != null && ruleNameField.isVisible()) {
            ruleNameField.fill(ruleName);
        }
    }
    
    public Locator getRuleCard(String ruleName) {
        return page.getByText(ruleName);
    }

    private Locator firstVisible(Locator... locators) {
        for (Locator locator : locators) {
            if (locator.count() > 0 && locator.first().isVisible()) {
                return locator.first();
            }
        }

        return locators[0].first();
    }

    private Locator firstExisting(Locator... locators) {
        for (Locator locator : locators) {
            if (locator.count() > 0) {
                return locator.first();
            }
        }

        return null;
    }
}
