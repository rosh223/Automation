package com.automationanywhere.ui;

import com.automationanywhere.pages.DashboardPage;
import com.automationanywhere.pages.LoginPage;
import com.automationanywhere.utils.ConfigReader;
import com.automationanywhere.utils.PlaywrightFactory;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DiagnosticIframeButtonsTest {
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
    public void discoverIframeButtons() {
        String formName = "DiagIframeBtn_" + System.currentTimeMillis();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.get("AA_USERNAME"), ConfigReader.get("AA_PASSWORD"));

        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.navigateToAutomation();
        dashboardPage.createNewForm(formName);

        page.waitForTimeout(10000);

        FrameLocator frame = page.frameLocator("iframe").first();
        Locator buttons = frame.locator("button, [role='button'], a");
        
        System.out.println("\n\n=== DUMPING ALL BUTTONS IN IFRAME ===");
        for (int i = 0; i < buttons.count(); i++) {
            Locator btn = buttons.nth(i);
            try {
                System.out.println("Btn [" + i + "]: Text='" + btn.textContent() + 
                                   "', AriaLabel='" + btn.getAttribute("aria-label") + 
                                   "', Title='" + btn.getAttribute("title") + 
                                   "', Class='" + btn.getAttribute("class") + "'");
            } catch (Exception e) {}
        }
        
        System.out.println("=== FINISHED DUMP ===");
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
