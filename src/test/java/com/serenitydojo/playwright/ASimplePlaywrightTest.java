package com.serenitydojo.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.assertj.core.api.Assertions;
import org.assertj.core.error.AssertionErrorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ASimplePlaywrightTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterEach
    public void tearDown() {
        browser.close();
        playwright.close();
    }
    @Test
    void shouldshowThePageTitle() {

        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertThat(title.contains("Practice software Testing"));

    }
    @Test
    void shouldsearchByKeyword() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchresults = page.locator(".card").count();

        Assertions.assertThat(matchingSearchresults > 0);


    }
}
