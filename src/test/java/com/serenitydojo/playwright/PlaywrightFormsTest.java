package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PlaywrightFormsTest {

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

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");}

        @DisplayName("Complete the form")
        @Test
        void completeForm() throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Sushmita");
            lastNameField.fill("Kumari");
            emailNameField.fill("jhasushmita.com");
            messageField.fill("Hello-world");
            subjectField.selectOption("Warranty");

            Path FileToUpload = Paths.get(ClassLoader.getSystemResource("data/Data-Sample.txt").toURI());
            page.setInputFiles("#attachment", FileToUpload);

            PlaywrightAssertions.assertThat(firstNameField).hasValue("Sushmita");
            PlaywrightAssertions.assertThat(lastNameField).hasValue("Kumari");
            PlaywrightAssertions.assertThat(emailNameField).hasValue("jhasushmita.com");
            PlaywrightAssertions.assertThat(messageField).hasValue("Hello-world");
            PlaywrightAssertions.assertThat(subjectField).hasValue("warranty");

            String UploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(UploadedFile).endsWith("SampleTest.txt");


        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings={"First name","Last name","Email","Message"})
        void mandatoryfields(String fieldName){
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendbutton = page.getByText("Send");

            //fill in the field values
            firstNameField.fill("Sushmita");
            lastNameField.fill("Kumari");
            emailNameField.fill("jhasushmita.com");
            messageField.fill("Hello-world");
            subjectField.selectOption("Warranty");

            //clear one of the fields
            page.getByLabel(fieldName).clear();
            sendbutton.click();
            //check the error message for that field



            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
            PlaywrightAssertions.assertThat(errorMessage).isVisible();

        }



    }


}
