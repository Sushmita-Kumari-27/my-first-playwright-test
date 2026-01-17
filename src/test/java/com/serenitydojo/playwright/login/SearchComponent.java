package com.serenitydojo.playwright.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SearchComponent {

    public final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    public void searchBy(String text) {

        page.waitForResponse("**/products/search**", () -> {
            page.getByPlaceholder("Search").fill(text); // ✅ use variable
            page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("Search")
            ).click();
        });
    }

    public void filterBy(String filterName) {
        page.waitForResponse("**/products**by_category=**", () -> {

            page.getByLabel(filterName).click();
        });
    }

    public void sortBy(String sortFilter) {

        String before =
                page.getByTestId("product-name").first().innerText();

        page.getByTestId("sort").selectOption(sortFilter);

        page.waitForCondition(() ->
                !page.getByTestId("product-name").first()
                        .innerText().equals("before"));
    }


}
