package com.automationanywhere.ui;

import com.automationanywhere.utils.ConfigReader;
import com.automationanywhere.utils.PlaywrightFactory;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Selector discovery utility. Run manually only when the AA UI changes.")
public class DiscoverSelectorsTest {

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
    public void discoverFormBuilderIframe() {
        // Login
        page.getByLabel("Username", new Page.GetByLabelOptions().setExact(true)).fill(ConfigReader.get("AA_USERNAME"));
        page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(ConfigReader.get("AA_PASSWORD"));
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Log in")).click();
        page.waitForLoadState();
        page.waitForTimeout(5000);

        // Navigate to Automation > Create > Form
        page.getByLabel("Automation", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForLoadState();
        page.waitForTimeout(3000);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Create").setExact(true)).first().click();
        page.waitForTimeout(1000);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Form")).click();
        page.waitForTimeout(2000);
        page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true))
            .fill("DiagFormBuilder_" + System.currentTimeMillis());
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Create & edit")).click();
        page.waitForLoadState();
        page.waitForTimeout(8000); // Extra wait for form builder

        System.out.println("=== FORM BUILDER URL: " + page.url() + " ===");

        // Check for iframes
        System.out.println("\n=== IFRAMES ON PAGE ===");
        Locator iframes = page.locator("iframe");
        int iframeCount = iframes.count();
        System.out.println("  Total iframes found: " + iframeCount);
        for (int i = 0; i < iframeCount; i++) {
            String src = iframes.nth(i).getAttribute("src");
            String id = iframes.nth(i).getAttribute("id");
            String name = iframes.nth(i).getAttribute("name");
            boolean visible = iframes.nth(i).isVisible();
            System.out.println("  iframe[" + i + "]: src='" + src + "' id='" + id + "' name='" + name + "' visible=" + visible);
        }

        // If there are iframes, look inside the first visible one
        if (iframeCount > 0) {
            for (int fi = 0; fi < iframeCount; fi++) {
                if (!iframes.nth(fi).isVisible()) continue;
                
                System.out.println("\n=== INSIDE IFRAME[" + fi + "] ===");
                FrameLocator frame = page.frameLocator("iframe").nth(fi);
                
                // Buttons inside iframe
                Locator frameBtns = frame.locator("button:visible");
                int fbCount = Math.min(frameBtns.count(), 30);
                System.out.println("  Buttons: " + fbCount);
                for (int i = 0; i < fbCount; i++) {
                    String text = frameBtns.nth(i).textContent().trim();
                    if (!text.isEmpty() && text.length() < 60) {
                        System.out.println("    Button[" + i + "]: '" + text + "'");
                    }
                }

                // Text elements with "text" inside iframe
                Locator frameText = frame.locator("*:visible").filter(new Locator.FilterOptions().setHasText("Text"));
                int ftCount = Math.min(frameText.count(), 20);
                for (int i = 0; i < ftCount; i++) {
                    String text = frameText.nth(i).innerText().trim();
                    if (text.length() < 40 && !text.contains("\n")) {
                        System.out.println("    TextEl[" + i + "]: '" + text + "'");
                    }
                }

                // Tabs inside iframe
                Locator frameTabs = frame.locator("[role='tab']:visible");
                int ftabCount = frameTabs.count();
                System.out.println("  Tabs: " + ftabCount);
                for (int i = 0; i < ftabCount; i++) {
                    System.out.println("    Tab[" + i + "]: '" + frameTabs.nth(i).textContent().trim() + "'");
                }
            }
        }

        // Also dump the full page HTML structure (top-level only) 
        System.out.println("\n=== MAIN CONTENT AREA ===");
        String bodyText = page.locator("body").innerText();
        // Just print first 500 chars to understand structure
        System.out.println(bodyText.substring(0, Math.min(bodyText.length(), 800)));

        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("target/form-builder-iframe.png"))
            .setFullPage(true));
        System.out.println("\n=== Screenshot saved ===");
    }

    @AfterAll
    public static void tearDown() {
        PlaywrightFactory.quitBrowser();
    }
}
