package base_specs;

import config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;

public class BaseSpecs {

    public static RequestSpecBuilder get() {
        return new RequestSpecBuilder()
                .addFilter((new AllureRestAssured()))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setBaseUri(Config.BASE_URI);
    }

    public static RequestSpecBuilder get(String token) {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addCookie("token", token)
                .setBaseUri(Config.BASE_URI);
    }

}
