package services.roles;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.Role;

import static io.restassured.RestAssured.given;

public class RoleApis {
    public static Response createRole(Role role) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(role)
                .when()
                .post(Config.ROLE_ENDPOINT)
                .then()
                .log()
                .body()
                .extract()
                .response();
    }

    public static Response updateRole(String name, Role newRole) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(newRole)
                .when()
                .put(Config.ROLE_ENDPOINT + "/" + name)
                .then()
                .log()
                .body()
                .extract()
                .response();
    }

    public static Response deleteRole(String name) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .delete(Config.ROLE_ENDPOINT + "/" + name);
    }

    public static Response getRoleByName(String name) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.ROLE_ENDPOINT + "/" + name)
                .then()
                .log()
                .body()
                .extract()
                .response();
    }

    public static Response getRoles() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.ROLE_ENDPOINT)
                .then()
                .log()
                .body()
                .extract()
                .response();
    }

    public static Response getAllRoles() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.ROLE_ENDPOINT + "/all")
                .then()
                .log()
                .body()
                .extract()
                .response();

    }

    public static Response assignMultipleUsersToRole(String roleName, String userIds) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(userIds)
                .when()
                .post(Config.ROLE_ENDPOINT + "/" + roleName + "/" + Config.USER_ENDPOINT)
                .then()
                .log()
                .body()
                .extract()
                .response();
    }


}
