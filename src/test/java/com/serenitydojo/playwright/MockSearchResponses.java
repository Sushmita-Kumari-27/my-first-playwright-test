package com.serenitydojo.playwright;

public class MockSearchResponses {
    public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
            "current_page": 1,
            "data":
                [
                     {
                       "id": "01KE1DY4JS70YEMY164KPWS4H9",
                       "name": "Super Pliers",
                       "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris viverra felis nec pellentesque feugiat. Donec faucibus arcu maximus, convallis nisl eu, placerat dolor. Morbi finibus neque nec tincidunt pharetra. Sed eget tortor malesuada, mollis enim id, condimentum nisi. In viverra quam at bibendum ultricies. Aliquam quis eros ex. Etiam at pretium massa, ut pharetra tortor. Sed vel metus sem. Suspendisse ac molestie turpis. Duis luctus justo massa, faucibus ornare eros elementum et. Vestibulum quis nisl vitae ante dapibus tempor auctor ut leo. Mauris consectetur et magna at ultricies. Proin a aliquet turpis.",
                       "price": 14.15,
                       "is_location_offer": false,
                       "is_rental": false,
                       "co2_rating": "D",
                       "in_stock": true,
                       "is_eco_friendly": false,
                       "product_image": {
                           "id": "01KE1DY4J6E993CX69CP1GC5CH",
                           "by_name": "Helinton Fantin",
                           "by_url": "https:\\/\\/unsplash.com\\/@fantin",
                           "source_name": "Unsplash",
                           "source_url": "https:\\/\\/unsplash.com\\/photos\\/W8BNwvOvW4M",
                           "file_name": "pliers01.avif",
                           "title": "Super pliers"
                       },
                       "category": {
                           "id": "01KE1DY4HN9J7J7X3MSEK95QKG",
                           "name": "Super Pliers"
                       },
                       "brand": {
                           "id": "01KE1DY47AFVN5RV2R8JS82YSK",
                           "name": "ForgeFlex Tools"
                       }
                     }
                 ],
            "from": 1,
            "last_page": 1,
            "per_page": 9,
            "to": 1,
            "total": 1

            }""";

    public static final String RESPONSE_WITH_NO_ENTRIES = """
            {
                "current_page": 1,
                "data": [],
                "from": null,
                "last_page": 1,
                "per_page": 9,
                "to": null,
                "total": 0
            }
            """;
}






