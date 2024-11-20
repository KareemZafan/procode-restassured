package api_testing;

import io.restassured.response.Response;
import mappers.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.users.UserApis;

import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class UserManagementTests {
    private String id;
    private Response response;
    private User requestBody;

    @BeforeClass
    void setUp() {
        requestBody = User.builder().setUsername("api-test-user").setFirstName("Api").setLastName("Testing").setEmail("test@test.com").setOtp(false).setEnabled(true).setPassword("12345").setRequiredActions(List.of("CONFIGURE_TOTP")).build();
        response = UserApis.createNewUser(requestBody).then().extract().response();

        id = response.body().jsonPath().getString("id");
        assertEquals(response.statusCode(), SC_OK);
        assertFalse(id.isEmpty());
        assertEquals(response.as(User.class), requestBody);
    }

    @Test(testName = "Users-update a user")
    void testUpdatingAnExistingUser() {
        requestBody.setFirstName("UpdatedFirstName");
        requestBody.setLastName("UpdatedLastName");
        requestBody.setEnabled(false);

        response = UserApis.updateUser(id, requestBody);

        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.as(User.class), requestBody);

    }

    @Test(testName = "Users-delete a user", priority = 2)
    void testDeletingAnExistingUser() {
        response = UserApis.deleteUser(id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(UserApis.getUserById(requestBody.getId()).getStatusCode(), SC_NOT_FOUND);
    }

    @Test(testName = "Users-get user by id")
    void testGettingSpecificUser() {
        response = UserApis.getUserById(id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.as(User.class), requestBody);
    }

    @Test(testName = "Users-get all users")
    void testGettingAllUsers() {
        response = UserApis.getUsers();
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.getBody().jsonPath().getList("items").toArray().length > 0);
        assertNotNull(response.getBody().jsonPath().get("totalCount"));
    }

    @Test(testName = "Users-assign role to user")
    void testAssignRoleToUser() {
        response = UserApis.assignRoleToUser(id, "esu-power-user");
        assertEquals(response.statusCode(), SC_OK);
    }

    @Test(testName = "Users-assign multiple roles to user")
    void testAssignMultipleRolesToUser() {
        String requestBody = """
                [
                    {"name": "dh-user"},
                    {"name": "admin"},
                    {"name": "esu-sma"}
                ]
                """;
        response = UserApis.assignMultipleRolesToUser(id, requestBody);
        assertEquals(response.statusCode(), SC_OK);
    }

    @Test(testName = "Users-get user roles")
    void testGettingUserRoles() {
        response = UserApis.getUserRoles(id);
        assertEquals(response.statusCode(), SC_OK);
        List<String> expectedRoles = List.of("admin", "dh-user", "default-roles-master", "esu-power-user", "esu-sma");

        List<String> actualRoles = response.body().jsonPath().getList("name");
        assertEquals(actualRoles, expectedRoles);
    }

    @Test(testName = "Users-delete user roles", priority = 1)
    void testDeletingUserRoles() {
        response = UserApis.getUserRoles(id);
        List<String> userRoles = List.of("admin", "dh-user", "default-roles-master", "esu-power-user", "esu-sma");
        List<String> userRolesIds = response.body().jsonPath().get("id");

        String requestBody = String.format("""
                 {
                     "id": "%s",
                     "name": "%s"
                 }
                """, userRolesIds.get(userRolesIds.size() - 1), userRoles.get(userRoles.size() - 1));
        response = UserApis.deleteUserRole(id, requestBody);
        assertEquals(response.statusCode(), SC_OK);
        response = UserApis.getUserRoles(id);
        userRoles = response.body().jsonPath().getList("name");
        assertEquals(userRoles, List.of("admin", "dh-user", "default-roles-master", "esu-power-user"));
    }

    @Test(testName = "Users-get user groups")
    void testGettingUserGroups() {
        response = UserApis.getUserGroups(id);
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.body().jsonPath().getList("name").containsAll(List.of("Admin", "ESU", "ÖGD")));

    }

    @Test(testName = "Users-remove user from a group", priority = 1)
    void testUnassigningGroupToUser() {
        response = UserApis.unassignUserToAGroup(id, "d2ae6ef2-b28d-4064-8223-a11bd93960ce");
        assertEquals(response.statusCode(), SC_OK);
        response = UserApis.getUserGroups(id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.body().jsonPath().getList("name").size(), 2);
        assertFalse(response.body().jsonPath().getList("name").contains("ESU"));
    }

    @Test(testName = "Users- assign user to groups")
    void testAddingUserToGroups() {
        Map<String, String> groupNameIds = Map.of("6f64c8a2-6eff-43cd-b702-6d0e6d5a9cf7", "Admin",
                "0a20fc49-90b6-4b69-8270-23c9b2a08488", "ÖGD",
                "d2ae6ef2-b28d-4064-8223-a11bd93960ce", "ESU");
        groupNameIds.keySet().forEach(el -> UserApis.addUserToGroup(id, el));
        response = UserApis.getUserGroups(id);
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.body().jsonPath().getList("name").containsAll(groupNameIds.values().stream().toList()));
    }

}

