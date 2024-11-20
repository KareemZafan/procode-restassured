package api_testing;

import com.google.gson.Gson;
import io.restassured.response.Response;
import mappers.Role;
import mappers.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import services.roles.RoleApis;
import services.users.UserApis;

import java.util.List;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class RoleManagementTests {
    String user1Id;
    String user2Id;
    private String roleName;
    private Response response;
    private Role requestBody;


    @BeforeClass
    void setUp() {
        User user1 = User.builder().
                setUsername("test-role-assignment1")
                .setFirstName("User1")
                .setLastName("Test1")
                .setEmail("test1@test1.com")
                .setPassword("12345678")
                .build();

        User user2 = User.builder().
                setUsername("test-role2")
                .setFirstName("User2")
                .setLastName("Test2")
                .setEmail("test2@test2.com")
                .setPassword("123456789")
                .build();

        user1Id = UserApis.createNewUser(user1).body().jsonPath().getString("id");
        user2Id = UserApis.createNewUser(user2).body().jsonPath().getString("id");


        roleName = "api-test-role";
        requestBody = Role.builder().setName(roleName).setDescription("For Testing Role").build();
        response = RoleApis.createRole(requestBody).then().extract().response();
    }

    @Test(testName = "Roles- create a new role")
    void testCreatingNewRole() {
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.body().jsonPath().get("description"), requestBody.getDescription());
        assertEquals(response.body().jsonPath().get("name"), requestBody.getName());

    }

    @Test(dependsOnMethods = "testCreatingNewRole", testName = "Roles-update a role")
    void testUpdatingAnExistingRole() {
        requestBody.setName("Updated-Testing-Role");
        requestBody.setDescription("Updated-Role-Description");
        response = RoleApis.updateRole(roleName, requestBody);

        assertEquals(response.statusCode(), SC_OK);
        assertFalse(response.body().jsonPath().getString("id").isEmpty());
        assertEquals(response.body().jsonPath().get("description"), requestBody.getDescription());
        assertEquals(response.body().jsonPath().get("name"), requestBody.getName());

    }

    @Test(dependsOnMethods = "testCreatingNewRole", testName = "Roles-delete a role", priority = 2)
    void testDeletingAnExistingRole() {
        response = RoleApis.deleteRole(requestBody.getName());
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(RoleApis.getRoleByName(requestBody.getName()).getStatusCode(), SC_NOT_FOUND);

        response = UserApis.deleteUser(user1Id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(UserApis.getUserById(user1Id).statusCode(), SC_NOT_FOUND);

        response = UserApis.deleteUser(user2Id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(UserApis.getUserById(user2Id).statusCode(), SC_NOT_FOUND);
    }

    @Test(dependsOnMethods = "testCreatingNewRole", testName = "Roles-get role by name")
    void testGettingSpecificRole() {
        response = RoleApis.getRoleByName(requestBody.getName());
        assertEquals(response.statusCode(), SC_OK);
        assertFalse(response.body().jsonPath().getString("id").isEmpty());
        assertEquals(response.body().jsonPath().get("description"), requestBody.getDescription());
        assertEquals(response.body().jsonPath().get("name"), requestBody.getName());
    }

    @Test(testName = "Roles-get all roles")
    void testGettingAllRoles() {
        response = RoleApis.getRoles();
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.getBody().jsonPath().getList("items").toArray().length > 0);
        assertNotNull(response.getBody().jsonPath().get("totalCount"));
        response = RoleApis.getAllRoles();
        assertEquals(response.statusCode(), SC_OK);
        List<String> ids = response.body().jsonPath().getList("id");
        List<String> names = response.body().jsonPath().getList("name");
        assertTrue(ids.stream().noneMatch(String::isEmpty));
        assertTrue(names.stream().noneMatch(String::isEmpty));

    }

    @Test(dependsOnMethods = "testCreatingNewRole", testName = "Roles-assign multiple users to a role")
    @Ignore
    void testAssigningMultipleUsersToRole() {

        response = RoleApis.assignMultipleUsersToRole(requestBody.getName(), new Gson().toJson(List.of(user1Id, user2Id)));
        assertEquals(response.statusCode(), SC_OK);

        response = UserApis.getUserRoles(user1Id);
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.body().jsonPath().getList("name").contains(requestBody.getName()));

        response = UserApis.getUserRoles(user2Id);
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.body().jsonPath().getList("name").contains(requestBody.getName()));

    }



}
