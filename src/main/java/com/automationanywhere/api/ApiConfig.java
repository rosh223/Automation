package com.automationanywhere.api;

import com.automationanywhere.utils.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiConfig {
    
    public static RequestSpecification getRequestSpec(String authToken) {
        String baseUrl = ConfigReader.get("AA_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "https://community.cloud.automationanywhere.digital";
        }

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (authToken != null && !authToken.isEmpty()) {
            // AA v2 APIs use standard Bearer token authorization
            builder.addHeader("Authorization", "Bearer " + authToken);
        }

        return builder.build();
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
