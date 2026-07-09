package com.automationanywhere.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void login(String username, String password) {
        // Use setExact(true) to avoid matching "Remember my username" checkbox
        page.getByLabel("Username", new Page.GetByLabelOptions().setExact(true)).fill(username);
        page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill(password);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Log in")).click();
        page.waitForLoadState();
    }
}
