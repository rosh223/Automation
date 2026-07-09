package com.automationanywhere.api;

import com.automationanywhere.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class AuthClient {

    private static String authToken;

    public static String getAuthToken() {
        if (authToken == null) {
            Map<String, String> loginPayload = new HashMap<>();
            loginPayload.put("username", ConfigReader.get("AA_USERNAME"));
            loginPayload.put("password", ConfigReader.get("AA_PASSWORD"));

            Response response = RestAssured
                    .given()
                        .spec(ApiConfig.getRequestSpec(null)) // Pass null since we don't have a token yet
                        .body(loginPayload)
                    .when()
                        .post("/v1/authentication") // NOTE: Adjust to the actual AA login endpoint
                    .then()
                        .statusCode(200)
                        .extract().response();

            // Extract the token (e.g. JWT) from the response JSON. Adjust the jsonPath based on real schema.
            authToken = response.jsonPath().getString("token");
        }
        return authToken;
    }
}
