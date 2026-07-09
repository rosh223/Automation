package com.automationanywhere.ui;

import com.automationanywhere.pages.DashboardPage;
import com.automationanywhere.pages.LoginPage;
import com.automationanywhere.utils.ConfigReader;
import com.automationanywhere.utils.PlaywrightFactory;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

public class DiagnosticSaveButtonTest {
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
    public void discoverSaveButton() {
        String formName = "DiagFormSave_" + System.currentTimeMillis();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.get("AA_USERNAME"), ConfigReader.get("AA_PASSWORD"));

        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.navigateToAutomation();
        dashboardPage.createNewForm(formName);

        // Wait a bit for the form builder to load fully
        page.waitForTimeout(10000);

        System.out.println("\n\n=== DUMPING ALL TEXTS CONTAINING 'Save' ===");
        Locator saveLocators = page.locator("text=Save");
        for (int i = 0; i < saveLocators.count(); i++) {
            System.out.println("Save Element [" + i + "]: " + saveLocators.nth(i).evaluate("el => el.outerHTML"));
        }
        
        System.out.println("\n\n=== DUMPING ALL BUTTONS ON PAGE ===");
        Locator buttons = page.locator("button, [role='button']");
        for (int i = 0; i < buttons.count(); i++) {
            System.out.println("Button [" + i + "]: " + buttons.nth(i).textContent() + " | Class: " + buttons.nth(i).getAttribute("class"));
        }

        // Take a full screenshot
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("form_builder_full.png")).setFullPage(true));
        System.out.println("Screenshot saved to form_builder_full.png");
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
