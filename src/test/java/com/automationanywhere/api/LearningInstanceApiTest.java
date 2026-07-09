package com.automationanywhere.api;

import com.automationanywhere.models.LearningInstanceRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class LearningInstanceApiTest {

    private static String token;

    @BeforeAll
    public static void setup() {
        // Authenticate via API and capture token before running tests
        token = AuthClient.getAuthToken();
    }

    @Test
    public void testCreateLearningInstance() {
        // Prepare the payload (Document type: Invoice)
        LearningInstanceRequest requestPayload = new LearningInstanceRequest(
                "TestInvoiceInstance_Automated",
                "Invoice",
                "Automated API Test for Invoice Document Type"
        );

        // Execute API Request
        Response response = RestAssured
            .given()
                .spec(ApiConfig.getRequestSpec(token))
                .body(requestPayload)
            .when()
                .post("/v1/learning-instances") // NOTE: Adjust to the actual endpoint from Network tab
            .then()
                .spec(ApiConfig.getResponseSpec())
                // Assertions
                .statusCode(anyOf(is(200), is(201))) // Verify HTTP status codes
                .time(lessThan(3000L))               // Verify response time < 3000ms
                .body("id", notNullValue())          // Assert 'id' is generated in response schema
                .body("status", equalTo("ACTIVE"))   // Assert instance status is active
                .body("documentType", equalTo("Invoice")) // Assert functional accuracy
                .extract().response();
        
        System.out.println("Successfully Created Learning Instance with ID: " + response.jsonPath().getString("id"));
    }
}
