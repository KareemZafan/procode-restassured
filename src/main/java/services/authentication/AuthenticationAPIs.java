package services.authentication;

import base_specs.BaseSpecs;
import config.Config;
import mappers.authentication.Authentication;

import static io.restassured.RestAssured.given;

public class AuthenticationAPIs {

    public static String getAccessToken() {
        Authentication authentication =
                Authentication
                        .builder()
                        .setUsername(Config.USERNAME)
                        .setPassword(Config.PASSWORD)
                        .build();

        return given()
                .spec(BaseSpecs.get().build())
                .body(authentication)
                .when()
                .post(Config.AUTHENTICATION_ENDPOINT)
                .then()
                .extract()
                .response()
                .jsonPath()
                .get("token");
    }
}
