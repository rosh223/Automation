package com.automationanywhere.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void login(String username, String password) {
        // Uses generic locator strategies - adjust as necessary for AA CE real DOM
        page.getByPlaceholder("Username", new Page.GetByPlaceholderOptions().setExact(false)).fill(username);
        page.getByPlaceholder("Password", new Page.GetByPlaceholderOptions().setExact(false)).fill(password);
        page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Login")).click();
        page.waitForLoadState();
    }
}
