package com.automationanywhere.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class RulesBuilderPage {
    private final Page page;

    public RulesBuilderPage(Page page) {
        this.page = page;
    }

    public void createRule(String ruleName) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Add Rule")).click();
        page.getByPlaceholder("Rule Name", new Page.GetByPlaceholderOptions().setExact(false)).fill(ruleName);
    }

    public void addCondition(String element, String conditionType, boolean isAndMode) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Add Condition")).click();
        
        if (isAndMode) {
            page.getByLabel("AND").check();
        }
        
        page.locator("select[name='element']").selectOption(element);
        page.locator("select[name='condition']").selectOption(conditionType);
    }

    public void addAction(String actionType, String targetElement) {
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Add Action")).click();
        
        page.locator("select[name='action']").selectOption(actionType);
        page.locator("select[name='target']").selectOption(targetElement);
    }

    public void addRuleBelow(String ruleName) {
        page.locator(".rule-card .context-menu").last().click();
        page.getByText("Add Rule Below").click();
        page.getByPlaceholder("Rule Name", new Page.GetByPlaceholderOptions().setExact(false)).last().fill(ruleName);
    }
    
    public Locator getRuleCard(String ruleName) {
        return page.locator(".rule-card:has-text('" + ruleName + "')");
    }
}
