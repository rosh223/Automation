package com.automationanywhere.ui;

import com.automationanywhere.pages.DashboardPage;
import com.automationanywhere.pages.FormBuilderPage;
import com.automationanywhere.pages.LoginPage;
import com.automationanywhere.pages.RulesBuilderPage;
import com.automationanywhere.utils.ConfigReader;
import com.automationanywhere.utils.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FormRulesBuilderTest {

    private static Page page;

    @BeforeAll
    public static void setUp() {
        // Set headless=false for local visual debugging; Docker will run headless
        page = PlaywrightFactory.initBrowser("chromium", false);
        page.navigate(ConfigReader.get("AA_BASE_URL"));
    }

    @Test
    public void testFormAndRulesBuilder() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.get("AA_USERNAME"), ConfigReader.get("AA_PASSWORD"));

        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.navigateToAutomation();
        dashboardPage.createNewForm("AutomationAssignmentForm");

        FormBuilderPage formBuilderPage = new FormBuilderPage(page);
        formBuilderPage.dragAndDropTextbox(1);
        formBuilderPage.setPropertiesForElement("TextBox1", "Hint1", "1", "50");
        
        formBuilderPage.dragAndDropTextbox(2);
        formBuilderPage.setPropertiesForElement("TextBox2", "Hint2", "1", "50");

        formBuilderPage.saveForm();
        formBuilderPage.navigateToRulesTab();

        RulesBuilderPage rulesBuilderPage = new RulesBuilderPage(page);
        
        // Assert: Add Rule button is visible
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new com.microsoft.playwright.Page.GetByRoleOptions().setName("Add Rule"))).isVisible();
            
        // Build Rule 1
        rulesBuilderPage.createRule("Rule1");
        rulesBuilderPage.addCondition("TextBox1", "Is Not Empty", false);
        rulesBuilderPage.addCondition("TextBox1", "Contains", true); // AND mode
        rulesBuilderPage.addAction("Set Value", "TextBox2");

        // Build Rule 2 and 3 via Context Menu
        rulesBuilderPage.addRuleBelow("Rule2");
        rulesBuilderPage.addRuleBelow("Rule3");

        formBuilderPage.saveForm();

        // Assert: Rules exist and persist
        assertThat(rulesBuilderPage.getRuleCard("Rule1")).isVisible();
        assertThat(rulesBuilderPage.getRuleCard("Rule2")).isVisible();
        assertThat(rulesBuilderPage.getRuleCard("Rule3")).isVisible();
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
