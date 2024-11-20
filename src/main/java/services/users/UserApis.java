package services.users;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.User;

import static io.restassured.RestAssured.given;

public class UserApis {
    public static Response createNewUser(User user) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(user)
                .when()
                .post(Config.USER_ENDPOINT);
    }

    public static Response updateUser(String id, User newUser) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(newUser)
                .when()
                .put(Config.USER_ENDPOINT + "/" + id);
    }

    public static Response deleteUser(String id) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .delete(Config.USER_ENDPOINT + "/" + id);
    }

    public static Response getUsers() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.USER_ENDPOINT)
                .then()
                .log()
                .all()
                .extract().response();
    }

    public static Response getUserById(String id) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.USER_ENDPOINT + "/" + id);
    }

    public static Response getUserRoles(String userId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.USER_ENDPOINT + "/" + userId + "/roles");
    }

    public static Response assignRoleToUser(String userId, String role) {
        String requestBody = "{\n" +
                "    \"name\": \"%s\"\n" +
                "}";
        return given()
                .spec(BaseSpecs.get().build())
                .body(String.format(requestBody, role))
                .when()
                .post(Config.USER_ENDPOINT + "/" + userId + "/role");
    }

    public static Response assignMultipleRolesToUser(String userId, String requestBody) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .post(Config.USER_ENDPOINT + "/" + userId + "/roles");
    }

    public static Response deleteUserRole(String userId, String requestBody) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .delete(Config.USER_ENDPOINT + "/" + userId + "/roles");
    }

    public static Response addUserToGroup(String userId, String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .post(Config.USER_ENDPOINT + "/" + userId + Config.GROUP_ENDPOINT + "/" + groupId);
    }

    public static Response getUserGroups(String userId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.USER_ENDPOINT + "/" + userId + Config.GROUP_ENDPOINT);
    }

    public static Response unassignUserToAGroup(String userId, String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .delete(Config.USER_ENDPOINT + "/" + userId + Config.GROUP_ENDPOINT + "/" + groupId);
    }
}
