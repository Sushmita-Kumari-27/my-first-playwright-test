package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class AddingItemsToTheCartTest {
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

    @BeforeEach
    void setupTrace(){
        browserContext.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }


    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Stop tracing BEFORE closing context
        browserContext.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get(
                                "trace_" +
                                        "" + testInfo.getDisplayName().replace(" ", "_") + ".zip"
                        ))
        );

        browserContext.close();
    }


    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }



    @DisplayName("Search for pliers")
    @Test
    void SearchForPilers(){
        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

         assertThat(page.locator(".card")).hasCount(4);

         List<String> productNames = page.getByTestId("product-name").allTextContents();
         Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

         Locator outOfStockItem = page.locator(".card")
                 .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                 .getByTestId("product-name");

         assertThat(outOfStockItem).hasCount(1);
         assertThat(outOfStockItem).hasText("Long Nose Pliers");

    }


}
