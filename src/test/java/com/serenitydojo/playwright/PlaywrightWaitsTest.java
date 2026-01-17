package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PlaywrightWaitsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
        playwright.selectors().setTestIdAttribute("data-test");

    }

    @BeforeEach
    void setUp(){
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext(){
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Nested
    class WaitingForState{
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForSelector("[data-test=product-name]");
            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProductNames() {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
        }

        @Test
        void shouldShowProductImages() {
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");

        }
    }


    @Nested
    class AutomaticWaits{
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

//        AutomaticTest
        @Test
        @DisplayName("Should wait for the filter checkbox to appear before clicking")
        void shouldWaitForTheFilterCheckBox(){
            var screwdriverFilter = page.getByLabel("Screwdriver");

            screwdriverFilter.click();

            PlaywrightAssertions.assertThat(screwdriverFilter).isChecked();

        }

        @Test
        @DisplayName("Should filter products by category")
        void shouldFilterProductByCategory(){
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

            page.waitForSelector(".card",
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000)
            );

            var filterProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filterProducts).contains("Sheet Sander", "Belt Sander", "Random Orbit Sander");



        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear{

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayToasterMessage(){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            //wait for the toaster message
            PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        @DisplayName("Should update the cart item count")
        void shouldUpdateCartItemCount(){
            page.getByText("Bolt Cutters").click();
            page.getByText("add to cart").click();

            //page.waitForCondition(() -> page.getByTestId("card-quantity").textContent().equals("1"));
            page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
        }

    }

}
