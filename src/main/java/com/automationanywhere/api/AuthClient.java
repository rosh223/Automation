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
                        .spec(ApiConfig.getRequestSpec(null))
                        .body(loginPayload)
                    .when()
                        .post("/v2/authentication")
                    .then()
                        .statusCode(200)
                        .extract().response();

            // AA returns the token directly in the response body
            authToken = response.jsonPath().getString("token");
        }
        return authToken;
    }
}
