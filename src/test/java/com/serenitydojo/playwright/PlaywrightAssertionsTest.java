package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.microsoft.playwright.options.LoadState;


@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightAssertionsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;


    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();

    }

    @BeforeEach
    public void setUp() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Making assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues() {
            var firstNameField = page.getByLabel("First name");

            firstNameField.waitFor();
            firstNameField.fill("Sushmita");
//            firstNameField.fill("Sushmita", new Locator.FillOptions().setTimeout(60000));

            assertThat(firstNameField).hasValue("Sushmita");

            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }

    }


    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsDataValues {


        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
            page.waitForLoadState(LoadState.NETWORKIDLE);
        }

        //        @Test
//        void allProductPricesShouldBeCorrectValues() {
//            Locator priceElements = page.locator("span.price");
//
//            // Debug: see how many are found
//            System.out.println("Prices found: " + priceElements.count());
//
//            List<Double> prices = priceElements.allInnerTexts()
//                    .stream()
//                    .map(price -> Double.parseDouble(price.replace("$", "").trim()))
//                    .toList();
//
//            Assertions.assertThat(prices)
//                    .isNotEmpty()
//                    .allMatch(price -> price > 0)
//                    .allMatch(price -> price < 1000);
//        }
//
//
        @Test
        void shouldSortInAlphabeticalOrder() {
            page.getByLabel("Sort").selectOption("Name (A - Z)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.naturalOrder());
            Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);

        }
    }
}
