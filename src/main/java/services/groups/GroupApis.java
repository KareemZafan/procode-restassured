package services.groups;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.Group;

import static io.restassured.RestAssured.given;

public class GroupApis {
    public static Response creatNewParentGroup(Group newGroup) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(newGroup)
                .when()
                .post(Config.GROUP_ENDPOINT)
                .then()
                .log()
                .body()
                .extract().response();
    }

    public static Response creatNewChildGroup(Group newGroup, String parentGroupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(newGroup)
                .when()
                .post(Config.GROUP_ENDPOINT + "/" + parentGroupId + "/children");
    }

    public static Response moveGroupToAnotherParent(String childGroupId, String newParentGroupId) {

        String requestBody = String.format("""
                {
                  "id": "%s"
                }
                """, childGroupId);

        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .post(Config.GROUP_ENDPOINT + "/" + newParentGroupId + "/move")
                .then()
                .log()
                .body()
                .extract().response();
    }

    public static Response getGroups() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT);
    }

    public static Response getAllGroups() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT + "/all");
    }

    public static Response getSpecificGroupByName(String groupName) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT + "?search=" + groupName)
                .then()
                .log()
                .body().extract().response();
    }

    public static Response getSpecificGroup(String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT + "/" + groupId);
    }

    public static Response updateAnExistingGroup(String updatedGroup, String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .body(updatedGroup)
                .put(Config.GROUP_ENDPOINT + "/" + groupId);
    }

    public static Response deleteAnExistingGroup(String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .delete(Config.GROUP_ENDPOINT + "/" + groupId);
    }

    public static Response getGroupMembers(String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT + "/" + groupId + "/members");
    }

    public static Response assignMultipleUsersToGroup(String groupId, String requestBody) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .post(Config.GROUP_ENDPOINT + "/" + groupId + "/members");
    }

    public static Response assignRoleToGroup(String groupId, String requestBody) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .post(Config.GROUP_ENDPOINT + "/" + groupId + Config.ROLE_ENDPOINT);
    }

    public static Response getGroupRoles(String groupId) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.GROUP_ENDPOINT + "/" + groupId + Config.ROLE_ENDPOINT);
    }

    public static Response deleteGroupRole(String groupId, String requestBody) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(requestBody)
                .when()
                .delete(Config.GROUP_ENDPOINT + "/" + groupId + Config.ROLE_ENDPOINT);
    }


}
