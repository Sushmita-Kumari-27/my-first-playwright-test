package com.serenitydojo.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PlaywrightLocatorsTests {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
        page = browser.newPage();
    }

    @AfterEach
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementsUsingCSS{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID(){
            page.locator("#first_name").fill("Sushmita");
            PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Sushmita");
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass(){
            page.locator("#first_name").fill("Sushmita");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            Assertions.assertTrue(!alertMessages.isEmpty());
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByAttribute(){
            page.locator("[placeholder= 'Your last name *']").fill("Kumari");
            PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Kumari");
        }
    }

}



