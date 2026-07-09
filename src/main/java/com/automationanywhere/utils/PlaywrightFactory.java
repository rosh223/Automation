package com.automationanywhere.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightFactory {
    private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static ThreadLocal<Page> page = new ThreadLocal<>();

    public static Page initBrowser(String browserName, boolean headless) {
        playwright.set(Playwright.create());

        boolean runHeadless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(headless)));
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(runHeadless);
        
        switch (browserName.toLowerCase()) {
            case "firefox":
                browser.set(playwright.get().firefox().launch(launchOptions));
                break;
            case "webkit":
                browser.set(playwright.get().webkit().launch(launchOptions));
                break;
            case "chromium":
            default:
                browser.set(playwright.get().chromium().launch(launchOptions));
                break;
        }

        page.set(browser.get().newPage());
        page.get().setDefaultTimeout(20_000);
        return getPage();
    }

    public static Page getPage() {
        return page.get();
    }

    public static void quitBrowser() {
        if (page.get() != null) {
            page.get().close();
            page.remove();
        }
        if (browser.get() != null) {
            browser.get().close();
            browser.remove();
        }
        if (playwright.get() != null) {
            playwright.get().close();
            playwright.remove();
        }
    }
}
