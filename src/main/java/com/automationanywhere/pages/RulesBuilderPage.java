package com.automationanywhere.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RulesBuilderPage {
    private final Page page;
    private final FrameLocator frame;

    public RulesBuilderPage(Page page) {
        this.page = page;
        this.frame = page.frameLocator("iframe").first();
    }

    public void createRule(String ruleName) {
        frame.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new FrameLocator.GetByRoleOptions().setName("Add Rule")).click();
        page.waitForTimeout(1000);
        
        // Fill the rule name if a name field appears
        Locator ruleNameField = frame.getByPlaceholder("Rule Name");
        if (ruleNameField.count() > 0) {
            ruleNameField.fill(ruleName);
        }
    }

    public void addCondition(String element, String conditionType, boolean isAndMode) {
        frame.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new FrameLocator.GetByRoleOptions().setName("Add Condition")).click();
        page.waitForTimeout(500);
        
        if (isAndMode) {
            Locator andOption = frame.getByLabel("AND");
            if (andOption.count() > 0) {
                andOption.check();
            }
        }
        
        // Select element and condition type from dropdowns
        Locator elementSelect = frame.locator("select[name='element']");
        if (elementSelect.count() > 0) {
            elementSelect.selectOption(element);
        }
        
        Locator conditionSelect = frame.locator("select[name='condition']");
        if (conditionSelect.count() > 0) {
            conditionSelect.selectOption(conditionType);
        }
    }

    public void addAction(String actionType, String targetElement) {
        frame.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new FrameLocator.GetByRoleOptions().setName("Add Action")).click();
        page.waitForTimeout(500);
        
        Locator actionSelect = frame.locator("select[name='action']");
        if (actionSelect.count() > 0) {
            actionSelect.selectOption(actionType);
        }
        
        Locator targetSelect = frame.locator("select[name='target']");
        if (targetSelect.count() > 0) {
            targetSelect.selectOption(targetElement);
        }
    }

    public void addRuleBelow(String ruleName) {
        // Right-click or use context menu on the last rule card
        Locator contextMenu = frame.locator("[class*='context'], [class*='menu'], [aria-label='more']").last();
        if (contextMenu.count() > 0) {
            contextMenu.click();
            page.waitForTimeout(500);
        }
        
        Locator addBelow = frame.getByText("Add Rule Below");
        if (addBelow.count() > 0) {
            addBelow.click();
            page.waitForTimeout(500);
        }
        
        Locator ruleNameField = frame.getByPlaceholder("Rule Name");
        if (ruleNameField.count() > 0) {
            ruleNameField.last().fill(ruleName);
        }
    }
    
    public Locator getRuleCard(String ruleName) {
        return frame.getByText(ruleName);
    }
}
