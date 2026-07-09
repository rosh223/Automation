package com.automationanywhere.ui;

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

        System.out.println("=== ATTACHING NETWORK INTERCEPTOR - CAPTURING ALL POST/PUT TO AA DOMAIN ===");
        page.onRequest(request -> {
            String method = request.method();
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                String url = request.url();
                // Capture ALL POST/PUT to the automationanywhere domain, skip pendo/analytics
                if (url.contains("automationanywhere") && !url.contains("pendo") && !url.contains("registration")) {
                    System.out.println("\n>>> CAPTURED " + method + " REQUEST <<<");
                    System.out.println("URL: " + url);
                    String postData = request.postData();
                    if (postData != null) {
                        System.out.println("Payload: " + postData);
                    } else {
                        System.out.println("Payload: (null/empty)");
                    }
                    System.out.println(">>> END <<<\n");
                }
            }
        });

        String baseUrl = ConfigReader.get("AA_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://community.cloud.automationanywhere.digital";
        }
        page.navigate(baseUrl + "/#/modules/cognitive/iqbot/pages/learning-instances");
        page.waitForLoadState();
        page.waitForTimeout(8000);

        System.out.println("=== Clicking 'Create new' ===");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create new")).click();
        page.waitForTimeout(3000);

        System.out.println("=== Filling Name: " + instanceName + " ===");
        page.locator("input[type='text']").first().fill(instanceName);
        page.waitForTimeout(1000);

        System.out.println("=== Clicking 'Next' ===");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForTimeout(8000);

        System.out.println("=== Attempting 'Create' button ===");
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create")).click();
            page.waitForTimeout(5000);
        } catch (Exception e) {
            System.out.println("Create button not found or not clickable: " + e.getMessage());
        }
        System.out.println("=== FINISHED ===");
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
