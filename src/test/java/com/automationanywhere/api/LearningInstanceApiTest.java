package com.automationanywhere.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;

public class LearningInstanceApiTest {

    private static String token;

    @BeforeAll
    public static void setup() {
        // Authenticate via API and capture token before running tests
        token = AuthClient.getAuthToken();
    }

    @Test
    public void testCreateLearningInstance() {
        System.out.println("=== TEST: CREATE LEARNING INSTANCE ===");

        try {
            // Read the exact payload we captured from the browser
            Path path = Paths.get("src", "test", "resources", "learning_instance_payload.json");
            String payloadStr = new String(Files.readAllBytes(path));

            // Replace the static name with a dynamic one to avoid duplicates
            String uniqueName = "API_Invoice_" + System.currentTimeMillis();
            payloadStr = payloadStr.replace("\"TestInstance6\"", "\"" + uniqueName + "\"");

            // Execute API Request
            Response response = RestAssured
                .given()
                    .spec(ApiConfig.getRequestSpec(token))
                    .body(payloadStr)
                .when()
                    .post("/cognitive/v3/learninginstances")
                .then()
                    .spec(ApiConfig.getResponseSpec())
                    .extract().response();

            // Validate Response
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody().asPrettyString());
            
            // Allow 200 or 201
            Assertions.assertTrue(
                response.getStatusCode() == 200 || response.getStatusCode() == 201, 
                "Expected 200 or 201 but got " + response.getStatusCode()
            );

            System.out.println("Successfully created Learning Instance: " + uniqueName);
            System.out.println("======================================\n");
            
        } catch (Exception e) {
            Assertions.fail("Test failed: " + e.getMessage());
        }
    }
}
