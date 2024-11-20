package api_testing;

import com.google.gson.Gson;
import io.restassured.response.Response;
import mappers.Group;
import mappers.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.groups.GroupApis;
import services.users.UserApis;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class GroupManagementTests {
    private String parentGroup1Id;
    private String parentGroup2Id;
    private String childId;
    private Response response;
    private Group requestBody1;
    private String user1Id;
    private String user2Id;
    private User user1;
    private User user2;


    @BeforeClass
    void setUp() {

        requestBody1 = Group.builder()
                .setName("API_Test_PrentGroup1")
                .setSubGroups(new ArrayList<>()).build();

        response = GroupApis.creatNewParentGroup(requestBody1);

        parentGroup1Id = GroupApis.getSpecificGroupByName(requestBody1.getName())
                .body()
                .jsonPath()
                .getString("items[0].id");

        assertEquals(response.statusCode(), SC_OK);
        assertNotNull(parentGroup1Id);
        assertFalse(parentGroup1Id.isEmpty());
        assertEquals(response.as(Group.class), requestBody1);


        Group requestBody2 = Group.builder()
                .setName("API_Test_PrentGroup2")
                .setSubGroups(new ArrayList<>()).build();

        response = GroupApis.creatNewParentGroup(requestBody2);

        parentGroup2Id = GroupApis.getSpecificGroupByName(requestBody2.getName())
                .body()
                .jsonPath()
                .getString("items[0].id");

        assertEquals(response.statusCode(), SC_OK);
        assertNotNull(parentGroup2Id);
        assertFalse(parentGroup2Id.isEmpty());
        assertEquals(response.as(Group.class), requestBody2);

        user1 = User.builder().setEmail("api@test.com")
                .setPassword("123456788")
                .setFirstName("Test-User")
                .setLastName("Group-Tests1")
                .setUsername("test1-user1")
                .build();

        user2 = User.builder().setEmail("api2@test2.com")
                .setPassword("123456788")
                .setFirstName("Api-Test2-User2")
                .setLastName("Group-Tests2")
                .setUsername("test-groups-user2")
                .build();
    }

    @Test(testName = "Groups-create a new group")
    void testCreatingNewChildGroup() {
        Group childRequest = Group.builder().setName("API_Child_Group").setSubGroups(new ArrayList<>()).build();
        response = GroupApis.creatNewChildGroup(childRequest, parentGroup1Id);
        childId = GroupApis.getSpecificGroupByName(requestBody1.getName()).body().jsonPath().getString("items[0].id");

        assertEquals(response.statusCode(), SC_OK);
        assertNotNull(childId);
        assertFalse(childId.isEmpty());
        assertEquals(response.as(Group.class), childRequest);
    }

    @Test(testName = "Groups-get specific group by id")
    void testGetSpecificGroup() {
        response = GroupApis.getSpecificGroup(parentGroup1Id);
        String name = response.as(Group.class).getName();
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(name, requestBody1.getName());
        assertFalse(parentGroup1Id.isEmpty());
        assertNotNull(parentGroup1Id);

    }

    @Test(testName = "Groups-move group to another parent group")
    void testMoveGroupToAnotherParentGroup() {
        response = GroupApis.moveGroupToAnotherParent(childId, parentGroup2Id);
        assertEquals(response.statusCode(), SC_OK);
    }

    @Test(testName = "Groups-get all groups")
    void testGettingAllGroups() {
        response = GroupApis.getAllGroups();
        assertEquals(response.statusCode(), SC_OK);
        List<String> ids = response.body().jsonPath().getList("id");
        List<String> names = response.body().jsonPath().getList("name");
        List<String> paths = response.body().jsonPath().getList("path");

        assertTrue(ids.stream().noneMatch(String::isEmpty));
        assertTrue(names.stream().noneMatch(String::isEmpty));
        assertTrue(paths.stream().noneMatch(String::isEmpty));
    }

    @Test(testName = "Groups-update an existing group")
    void updateAnExistingGroup() {
        String updatedGroupName = """
                {
                 "name":"Updated_API_Test_Group"
                }
                """;

        response = GroupApis.updateAnExistingGroup(updatedGroupName, parentGroup1Id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.body().jsonPath().getString("name"), "Updated_API_Test_Group");
    }

    @Test(testName = "Groups-delete existing groups", priority = 2)
    void testDeleteAnExistingGroup() {

        response = GroupApis.deleteAnExistingGroup(parentGroup1Id);
        assertEquals(response.statusCode(), SC_OK);
        response = GroupApis.getSpecificGroup(parentGroup1Id);
        assertEquals(response.statusCode(), SC_NOT_FOUND);
        assertEquals(response.body().jsonPath().getString("message"), "Group not found with id: " + parentGroup1Id);


        response = GroupApis.deleteAnExistingGroup(parentGroup2Id);
        assertEquals(response.statusCode(), SC_OK);
        response = GroupApis.getSpecificGroup(parentGroup2Id);
        assertEquals(response.statusCode(), SC_NOT_FOUND);
        assertEquals(response.body().jsonPath().getString("message"), "Group not found with id: " + parentGroup2Id);
    }

    @Test(testName = "Groups-assign multiple users to a group", priority = 1)
    void testAssigningMultipleUsersToGroup() {
        Response response1 = UserApis.createNewUser(user1);
        assertEquals(response1.statusCode(), SC_OK);
        assertEquals(user1.getUsername(), response1.body().jsonPath().getString("username"));
        user1Id = response1.body().jsonPath().getString("id");

        Response response2 = UserApis.createNewUser(user2);
        assertEquals(response2.statusCode(), SC_OK);
        assertEquals(user2.getUsername(), response2.body().jsonPath().getString("username"));
        user2Id = response2.body().jsonPath().getString("id");
        System.out.println(user1Id);
        System.out.println(user2Id);

        Response assignMultipleUserToGroupResponse =
                GroupApis.assignMultipleUsersToGroup(childId, new Gson().toJson(List.of(user1Id, user2Id)));
        assertEquals(assignMultipleUserToGroupResponse.statusCode(), SC_OK);

        assertTrue(UserApis.getUserGroups(user1Id).body().jsonPath().getList("id").contains(childId));
        assertTrue(UserApis.getUserGroups(user2Id).body().jsonPath().getList("id").contains(childId));

        Response membersResponse =
                GroupApis.getGroupMembers(childId);

        assertEquals(membersResponse.statusCode(), SC_OK);
        List<String> ids = membersResponse.body().jsonPath().getList("id");
        List<String> usernames = membersResponse.body().jsonPath().getList("username");
        assertTrue(ids.containsAll(List.of(user1Id, user2Id)));
        assertTrue(usernames.containsAll(List.of(user1.getUsername(), user2.getUsername())));

        Response deleteResponse = UserApis.deleteUser(user1Id);
        assertEquals(deleteResponse.statusCode(), SC_OK);
        assertEquals(UserApis.getUserById(user1Id).statusCode(), SC_NOT_FOUND);

        deleteResponse = UserApis.deleteUser(user2Id);
        assertEquals(deleteResponse.statusCode(), SC_OK);
        assertEquals(UserApis.getUserById(user2Id).statusCode(), SC_NOT_FOUND);

    }

    @Test(testName = "Groups-assign role to a group", priority = 1)
    void testAssigningRoleToGroup() {
        String groupRoleRequestBody = """
                {
                    "name": "%s"
                }
                """;
        GroupApis.assignRoleToGroup(parentGroup1Id, String.format(groupRoleRequestBody, "offline_access"));
        Response res = GroupApis.assignRoleToGroup(parentGroup1Id, String.format(groupRoleRequestBody, "dh-doctor"));
        assertEquals(res.statusCode(), SC_OK);

        res = GroupApis.getGroupRoles(parentGroup1Id);
        assertEquals(res.statusCode(), SC_OK);
        List<String> roleNames = res.body().jsonPath().getList("name");
        System.out.println(roleNames);
        assertTrue(roleNames.stream().noneMatch(String::isEmpty));
        assertTrue(roleNames.containsAll(List.of("offline_access", "dh-doctor")));
    }

    @Test(testName = "Groups-delete group roles", priority = 1)
    void testDeletingGroupRoles() {
        List<String> roleNames = GroupApis.getGroupRoles(parentGroup1Id).body().jsonPath().getList("name");
        List<String> roleIds = GroupApis.getGroupRoles(parentGroup1Id).body().jsonPath().getList("id");

        String groupRoleRequestBody = """
                {
                    "id": "%s",
                    "name": "%s"
                }
                """;
        Response res = GroupApis.deleteGroupRole(parentGroup1Id, String.format(groupRoleRequestBody, roleIds.get(0), roleNames.get(0)));
        assertEquals(res.statusCode(), SC_OK);
        res = GroupApis.deleteGroupRole(parentGroup1Id, String.format(groupRoleRequestBody, roleIds.get(1), roleNames.get(1)));
        assertEquals(res.statusCode(), SC_OK);
    }

}

