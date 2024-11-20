package api_testing;

import io.restassured.response.Response;
import mappers.Application;
import mappers.helpers.GroupRoleDTO;
import mappers.helpers.Image;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.applications.ApplicationApis;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class ApplicationManagementTests {
    private long id;
    private Response response;
    private Application requestBody;

    @BeforeClass
    void setUp() {
        requestBody = Application
                .builder()
                .setName("api-test-application")
                .setDescription("api-test-application-description")
                .setLink("api-test-application-link")
                .setCategoryId(1)
                .setCreatedBy("Kareem")
                .setLastModifiedBy("Kareem")
                .setUserGroups(List.of(new GroupRoleDTO("Application-group-id-1", "Application-group-name-1"), new GroupRoleDTO("Application-id-2", "Application-name-2")))
                .setRoles(List.of(new GroupRoleDTO("Application-role-id-1", "Application-role-name-1"), new GroupRoleDTO("Application-role-id-2", "Application-role-name-2")))
                .setAssignedUsers(List.of("User-1", "User-2"))
                .setImageBase64(new Image("application.png", "image/png", "image_url")).build();
        response = ApplicationApis.creatNewApplication(requestBody).then().extract().response();

        id = response.body().jsonPath().getLong("id");
        System.out.println(id);
    }

    @Test(testName = "Applications- create a new application")
    void testCreatingNewApplication() {
        assertEquals(response.statusCode(), SC_CREATED);
        assertTrue(id > 0);
        assertEquals(requestBody, response.as(Application.class));
    }

    @Test(dependsOnMethods = "testCreatingNewApplication", testName = "Application-update an application", priority = 1)
    void testUpdatingAnExistingApplication() {
        requestBody.setName("Updated-Application-Name");
        requestBody.setDescription("Updated-Application-Description");

        response = ApplicationApis.updateAnExistingApplication(requestBody, id);

        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.as(Application.class), requestBody);
    }

    @Test(testName = "Applications-delete an application", priority = 2)
    void testDeletingAnExistingApplication() {
        response = ApplicationApis.deleteAnExistingApplication(id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(ApplicationApis.getSpecificApplication(requestBody.getId()).getStatusCode(), SC_NOT_FOUND);

    }

    @Test(dependsOnMethods = "testCreatingNewApplication", testName = "Applications-get application by id")
    void testGettingSpecificApplication() {
        response = ApplicationApis.getSpecificApplication(id);
        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.as(Application.class), requestBody);
    }

    @Test(dependsOnMethods = "testCreatingNewApplication", testName = "Applications-get all applications")
    void testGettingAllApplications() {
        response = ApplicationApis.getApplications();
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.getBody().jsonPath().getList("userGroups").toArray().length > 0);
        assertNotNull(response.getBody().jsonPath().get("name"));
    }

}

