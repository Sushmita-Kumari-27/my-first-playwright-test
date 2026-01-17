package com.serenitydojo.playwright.login;

import com.microsoft.playwright.Page;
import com.serenitydojo.playwright.Cucumber.domain.ProductSummary;
import io.qameta.allure.Step;

import java.util.List;

import static java.util.Arrays.stream;

public class ProductList {

        public final Page page;

        public ProductList(Page page){
            this.page = page;
        }

        public List<String> getProductNames() {
            return page.getByTestId("product-name").allInnerTexts();
        }

        public List<ProductSummary> getProductSummaries(){
            return page.locator(".card").all()
                        .stream()
                            .map(productCard -> {
                                String productName = productCard.getByTestId("product-name").textContent().strip();
                                String productPrice = productCard.getByTestId("product-price").textContent();
                                return new ProductSummary(productName, productPrice);
                        }).toList();
        }

         @Step("View product details")
         public void viewProductDetails(String productName) {page.locator(".card").getByText(productName).click();}

         public String getSearchCompletedMessage() { return  page.getByTestId("search_completed").textContent();}

}



