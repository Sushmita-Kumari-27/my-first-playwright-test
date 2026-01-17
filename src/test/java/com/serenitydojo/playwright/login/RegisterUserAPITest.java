package com.serenitydojo.playwright.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();
    @BeforeEach
    void setup (Playwright playwright){
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @AfterEach
    void tearDown(){
        if (request != null){
            request.dispose();
        }
    }

    @Test
    void should_register_user(){
        User validUser = User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        Assertions.assertThat(response.status()).isEqualTo(201);

        String responseBody = response.text();


        User createdUser = gson.fromJson(responseBody, User.class);
        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);
//        Assertions.assertThat(createdUser).isEqualTo(validUser.withPassword(null));

        assertSoftly(softly ->{
                softly.assertThat(response.status())
                        .as("Registration should return 201 created status code")
                        .isEqualTo(201);
                softly.assertThat(createdUser)
                        .as("User should be created")
                        .isEqualTo(validUser.withPassword(null));

                Assertions.assertThat(responseObject.has("password"))
                                .as("No password should be returned")
                                        .isFalse();

                softly.assertThat(responseObject.get("id").getAsString())
                        .as("Registered user should have an id")
                        .isNotEmpty();

                softly.assertThat(
                    response.headers().get("content-type")
                ).contains("application/json");


        });

    }


    @Test
    void first_name_is_mandatory(){
        User userWithNoName = new User(
                null,
                "address",
                null,
                "1234567899",
                "1979-01-01",
                "ABCD123!@#$",
                "Sample@email.com"
        );

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        assertSoftly(softly ->{
            softly.assertThat(response.status()).isEqualTo(422);


            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);
//
            softly.assertThat(responseObject.has("first_name")).isTrue();

            String errorMessage = responseObject.get("first_name").getAsString();

            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");
        });
    }

}
