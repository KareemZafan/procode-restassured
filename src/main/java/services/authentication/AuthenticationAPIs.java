package services.authentication;

import static io.restassured.RestAssured.given;

public class AuthenticationAPIs {

    public static String getAccessToken() {
        String requestBody = "{\n" +
                "  \"username\": \"admin\",\n" +
                "  \"password\": \"password123\"\n" +
                "}";
       return given()
                .header("Content-Type", "application/json")
                .baseUri("https://restful-booker.herokuapp.com")
                .body(requestBody)
                .when()
                .post("/auth")
                .then()
                .extract()
                .response()
                .jsonPath().get("token");
    }
}
