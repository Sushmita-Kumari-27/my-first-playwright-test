package com.serenitydojo.playwright;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright
    public class AnAnnotatedPlaywrightTest {
        public static class MyOptions implements OptionsFactory{
            @Override
            public Options getOptions() {
                return new Options()
                        .setHeadless(false)
                        .setLaunchOptions(
                                new BrowserType.LaunchOptions()
                                        .setArgs(Arrays.asList("--no-sandbox","--disable-gpu"))
                        );
            }
        }

        @Test
        void shouldShowThePageTitle(Page page) {

            page.navigate("https://practicesoftwaretesting.com/");
            String title = page.title();
            org.junit.jupiter.api.Assertions.assertTrue(title.contains("Practice software Testing"));

        }
        @Test
        void shouldSearchByKeyword(Page page) {
            page.navigate("https://practicesoftwaretesting.com/");
            page.locator("[placeholder=Search]").fill("Pliers");
            page.locator("button:has-text('Search')").click();

            int matchingSearchresults = page.locator(".card").count();

            org.junit.jupiter.api.Assertions.assertTrue(matchingSearchresults > 0);


        }
    }
