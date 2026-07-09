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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("UI")
@Tag("UseCase1")
@DisplayName("Use Case 1: Form with Rules Builder (UI Automation)")
public class FormRulesBuilderTest {

    private static Page page;

    @BeforeAll
    public static void setUp() {
        page = PlaywrightFactory.initBrowser("chromium", false);
        String baseUrl = ConfigReader.get("AA_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://community.cloud.automationanywhere.digital";
        }
        page.navigate(baseUrl);
        page.waitForLoadState();
    }

    @Test
    @DisplayName("Automate the full Rules Builder flow using POM")
    public void testFormAndRulesBuilder() {
        String formName = "AutomationTestForm_" + System.currentTimeMillis();

        // Step 1: Login
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.get("AA_USERNAME"), ConfigReader.get("AA_PASSWORD"));

        // Step 2: Navigate and create form
        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.navigateToAutomation();
        dashboardPage.createNewForm(formName);

        // Step 3: Drag and drop textboxes
        FormBuilderPage formBuilderPage = new FormBuilderPage(page);
        formBuilderPage.dragAndDropTextbox(); // First textbox
        formBuilderPage.setPropertiesForElement("TextBox1", "Hint1", "1", "50");
        
        formBuilderPage.dragAndDropTextbox(); // Second textbox
        formBuilderPage.setPropertiesForElement("TextBox2", "Hint2", "1", "50");

        // Step 4: Save form
        formBuilderPage.saveForm();

        // Step 5: Navigate to Rules tab
        formBuilderPage.navigateToRulesTab();

        // Assert: Add Rule button is visible and functional
        assertThat(page.frameLocator("iframe").first().getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new com.microsoft.playwright.FrameLocator.GetByRoleOptions().setName("Add rule"))).isVisible();

        // Step 6: Build Rules
        RulesBuilderPage rulesBuilderPage = new RulesBuilderPage(page);
        rulesBuilderPage.createRule("Rule1");
        rulesBuilderPage.addCondition("TextBox1", "Is Not Empty", false);
        rulesBuilderPage.addCondition("TextBox1", "Contains", true); // AND mode
        rulesBuilderPage.addAction("Set Value", "TextBox2");

        // Step 7: Add Rule2 and Rule3 via context menu
        rulesBuilderPage.addRuleBelow("Rule2");
        rulesBuilderPage.addRuleBelow("Rule3");

        // Step 8: Save and verify
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
