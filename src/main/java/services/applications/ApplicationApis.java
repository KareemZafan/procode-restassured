package services.applications;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.Application;

import static io.restassured.RestAssured.given;

public class ApplicationApis {
    public static Response creatNewApplication(Application newApplication) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .body(newApplication)
                .log()
                .body()
                .when()
                .post(Config.APPLICATION_ENDPOINT)
                .then()
                .log()
                .body()
                .extract().response();
    }

    public static Response getApplications() {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .get(Config.APPLICATION_ENDPOINT);
    }

    public static Response getFilteredApplications(String userGroups, String assignedUser) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .get(Config.APPLICATION_ENDPOINT + "/filter?userGroups=" + userGroups + "&assignedUser=" + assignedUser);
    }

    public static Response getSpecificApplication(long applicationId) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .get(Config.APPLICATION_ENDPOINT + "/" + applicationId);
    }

    public static Response updateAnExistingApplication(Application newApplication, long applicationId) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .body(newApplication)
                .patch(Config.APPLICATION_ENDPOINT + "/" + applicationId);
    }

    public static Response deleteAnExistingApplication(long applicationId) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .delete(Config.APPLICATION_ENDPOINT + "/" + applicationId);
    }

}
