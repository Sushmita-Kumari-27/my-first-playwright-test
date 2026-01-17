package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.serenitydojo.playwright.login.NavBar;
import com.serenitydojo.playwright.login.ProductList;

public class PlaywrightPageObjectTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        playwright.selectors().setTestIdAttribute("data-test");

    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Nested
    class WhenSearchingProductsByKeyword {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");

        }

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {

            page.waitForResponse("**/products/search**", () -> {
                page.getByPlaceholder("Search").fill("tape");
                page.getByRole(AriaRole.BUTTON,
                                new Page.GetByRoleOptions().setName("Search"))
                        .click();
            });

            List<String> productNames = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(productNames)
                    .contains(
                            "Tape Measure 7.5m",
                            "Measuring Tape",
                            "Tape Measure 5m"
                    );
        }

        @DisplayName("With Page Objects")
        @Test
        void withPageObjects() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("tape");

            var matchingProduct = productList.getProductNames();

            Assertions.assertThat(matchingProduct)
                    .contains(
                            "Tape Measure 7.5m",
                            "Measuring Tape",
                            "Tape Measure 5m"
                    );
        }

        class SearchComponent {
            private final Page page;

            SearchComponent(Page page) {
                this.page = page;
            }

            public void searchBy(String tape) {
                page.waitForResponse("**/products/search**", () -> {
                    page.getByPlaceholder("Search").fill("tape");
                    page.getByRole(AriaRole.BUTTON,
                                    new Page.GetByRoleOptions().setName("Search"))
                            .click();
                });
            }
        }


        @Nested
        class WhenAddingItemsToTheCart {


            @BeforeEach
            void openHomePage() {
                page.navigate("https://practicesoftwaretesting.com");

            }

            @DisplayName("Without Page Objects")
            @Test
            void withoutPageObjects() {
                //Search for pliers
                page.waitForResponse("**/products/search?q=pliers", () -> {
                    page.getByPlaceholder("Search").fill("pliers");
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
                });

                //show page details
                page.locator(".card").getByText("Combination Pliers").click();

                // Increase cart quantity
                page.getByTestId("increase-quantity").click();
                page.getByTestId("increase-quantity").click();

                //Add to Cart
                page.getByText("Add to cart").click();
                page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));

                //open the cart
                page.getByTestId("nav-cart").click();

                //check cart contents
                PlaywrightAssertions.assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();

            }

            @Test
            void withPageObjects() {

                SearchComponent searchComponent = new SearchComponent(page);
                ProductList productList = new ProductList(page);
                ProductDetails productDetails = new ProductDetails(page);
                NavBar navBar = new NavBar(page);
                CheckoutCart checkoutCart = new CheckoutCart(page);
                searchComponent.searchBy("pliers");

                productList.viewProductDetails("Combination Pliers");

                productDetails.increaseQuantityBy(2);

                productDetails.addTOCart();

                navBar.openCart();

                List<CartLineItem> lineItems = checkoutCart.getLineItems();


                Assertions.assertThat(lineItems)
                        .hasSize(1)
                        .first()
                        .satisfies(item -> {
                            Assertions.assertThat(item.title()).contains("Combination Pliers");
                            Assertions.assertThat(item.quantity()).isEqualTo(3);
                            Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                        });


            }

        }

        class SearchComponents {
            private final Page page;

            SearchComponents(Page page) {
                this.page = page;
            }

            public void searchBy(String Pliers) {
                page.waitForResponse("**/products/search**", () -> {
                    page.getByPlaceholder("Search").fill("Pliers");
                    page.getByRole(AriaRole.BUTTON,
                                    new Page.GetByRoleOptions().setName("Search"))
                            .click();
                });
            }
        }

        class ProductList {
            private final Page page;

            ProductList(Page page) {
                this.page = page;
            }

            public List<String> getProductNames() {
                return page.getByTestId("product-name").allInnerTexts();
            }

            public void viewProductDetails(String productName) {
                page.locator(".card").getByText(productName).click();

            }
        }

        class ProductDetails {
            private final Page page;

            ProductDetails(Page page) {
                this.page = page;
            }


            public void increaseQuantityBy(int increment) {
                for (int i = 1; i <= increment; i++) {
                    page.getByTestId("increase-quantity").click();
                }

            }

            public void addTOCart() {

                page.getByText("Add to cart").click();
                page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));
            }
        }


        record CartLineItem(String title, int quantity, double price, double total) {
        }

        class CheckoutCart {
            private final Page page;

            CheckoutCart(Page page) {
                this.page = page;
            }

            public List<CartLineItem> getLineItems() {

                page.locator(".app-cart tbody tr").first().waitFor();

                return page.locator(".app-cart tbody tr")
                        .all()
                        .stream()
                        .map(
                                row -> {
                                    String title = row.getByTestId("product-title").innerText();
                                    int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                                    double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                                    double linePrice = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                                    return new CartLineItem(title, quantity, price, linePrice);
                                }
                        ).toList();
            }
        }

        private String price(String value) {
            return value.replace("$", "");
        }

    }
}