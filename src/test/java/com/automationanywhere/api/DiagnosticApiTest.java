package com.automationanywhere.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DiagnosticApiTest {

    private static String token;

    @BeforeAll
    public static void setup() {
        token = AuthClient.getAuthToken();
    }

    @Test
    public void testFetchDomains() {
        System.out.println("=== FETCHING DOMAINS ===");
        
        // Try the domains endpoint
        Response response = RestAssured
            .given()
                .spec(ApiConfig.getRequestSpec(token))
            .when()
                .get("/cognitive/v3/domains")
            .then()
                .extract().response();
                
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asPrettyString());
        System.out.println("========================\n");
    }
}
