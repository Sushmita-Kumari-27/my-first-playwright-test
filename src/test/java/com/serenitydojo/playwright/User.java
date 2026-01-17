package com.serenitydojo.playwright;

import net.datafaker.Faker;

public record User(
        String first_name,
        String last_name,
        Address address,
        String phone,
        String dob,
        String password,
        String email
) {

    public static User randomUser() {
        Faker fake = new Faker();

        Address address = new Address(
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().postcode()
        );

        return new User(
                fake.name().firstName(),
                fake.name().lastName(),
                address,
                fake.phoneNumber().phoneNumber(),
                "1990-01-01",
                "Az123!&xyz",
                fake.internet().emailAddress()
        );
    }

    public Object withPassword(String password) {
        return new User(
                first_name,
                last_name,
                address,
                phone,
                dob,
                password,
                email
        );
    }
}
