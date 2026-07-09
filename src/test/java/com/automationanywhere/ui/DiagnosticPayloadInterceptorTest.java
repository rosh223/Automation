package com.automationanywhere.ui;

import com.automationanywhere.pages.DashboardPage;
import com.automationanywhere.pages.LoginPage;
import com.automationanywhere.utils.ConfigReader;
import com.automationanywhere.utils.PlaywrightFactory;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DiagnosticPayloadInterceptorTest {
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
    public void captureLearningInstancePayload() {
        String instanceName = "DiagLI_" + System.currentTimeMillis();

        LoginPage loginPage = new LoginPage(page);
        loginPage.login(ConfigReader.get("AA_USERNAME"), ConfigReader.get("AA_PASSWORD"));

        System.out.println("=== ATTACHING NETWORK INTERCEPTOR ===");
        page.onRequest(request -> {
            if ("POST".equalsIgnoreCase(request.method()) || "PUT".equalsIgnoreCase(request.method())) {
                if (request.url().contains("learning-instance") || request.url().contains("document-automation") || request.url().contains("iqbot")) {
                    System.out.println("\n\n>>> CAPTURED API REQUEST <<<");
                    System.out.println("URL: " + request.url());
                    System.out.println("Method: " + request.method());
                    System.out.println("Payload: \n" + request.postData());
                    System.out.println(">>> ==================== <<<\n\n");
                }
            }
        });

        DashboardPage dashboardPage = new DashboardPage(page);
        // We don't have a direct method to go to Document Automation -> Learning Instances in DashboardPage yet.
        // Let's just navigate directly via URL since we are logged in.
        String baseUrl = ConfigReader.get("AA_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://community.cloud.automationanywhere.digital";
        }
        page.navigate(baseUrl + "/#/modules/cognitive/iqbot/pages/learning-instances");
        page.waitForLoadState();
        page.waitForTimeout(5000); // let UI load

        System.out.println("Clicking 'Create new'...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create new")).click();
        page.waitForTimeout(2000);
        
        System.out.println("Filling Name...");
        page.locator("input[type='text']").first().fill(instanceName);
        page.waitForTimeout(1000);

        System.out.println("Clicking 'Next'...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForTimeout(4000);

        System.out.println("Clicking 'Create' (if present)...");
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create")).click();
        } catch (Exception e) {
            System.out.println("Create button not found, maybe 'Next' already submitted it.");
        }
        page.waitForTimeout(4000);
        System.out.println("=== FINISHED CAPTURING ===");
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
