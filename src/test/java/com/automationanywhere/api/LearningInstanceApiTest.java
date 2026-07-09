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
                "TestInvoiceInstance_Automated_" + System.currentTimeMillis(),
                "Automated API Test for Invoice Document Type"
        );

        // API Endpoint with domain ID in path, language and provider as query params
        String domainId = "33DED827-3DC4-4201-B478-7C15B94AF522";
        String languageId = "B62EFA19-3592-4D2B-910A-E9C1C7DAE1A9";
        String providerId = "B4DBACBA-5C86-4E32-A522-F668D48CC74B";
        String endpoint = String.format("/cognitive/v3/learninginstances/%s?language=%s&provider=%s", 
                                        domainId, languageId, providerId);

        // Execute API Request
        Response response = RestAssured
            .given()
                .spec(ApiConfig.getRequestSpec(token))
                .body(requestPayload)
            .when()
                .post(endpoint) // Updated endpoint
            .then()
                .spec(ApiConfig.getResponseSpec())
                .extract().response();
                
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
        
        // Assertions
        response.then().statusCode(anyOf(is(200), is(201)));
        
        // Try to extract an ID if it exists (might be "id", "instanceId", etc.)
        try {
            String id = response.jsonPath().getString("id");
            if (id != null) {
                System.out.println("Successfully Created Learning Instance with ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Could not parse ID from response.");
        }
    }
}
