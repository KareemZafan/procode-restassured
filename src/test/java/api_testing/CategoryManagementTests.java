package api_testing;

import io.restassured.response.Response;
import mappers.Category;
import mappers.helpers.GroupRoleDTO;
import mappers.helpers.Image;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import services.categories.CategoryApis;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class CategoryManagementTests {
    private long id;
    private Response response;
    private Category requestBody;

    @BeforeClass
    void setUp() {
        requestBody = Category.builder().setName("api-test-category").setDescription("api-test-category-description").setCreatedBy("Kareem").setLastModifiedBy("Kareem").setUserGroups(List.of(new GroupRoleDTO("Category-id-1", "Category-name-1"), new GroupRoleDTO("Category-id-2", "Category-name-2"))).setApplicationsIds(List.of(1, 2)).setImageBase64(new Image("category.png", "image/png", "image_url")).build();
        response = CategoryApis.creatNewCategory(requestBody).then().extract().response();

        id = response.body().jsonPath().getLong("id");
        System.out.println(id);
    }

    @Test(testName = "Categories- create a new category")
    void testCreatingNewCategory() {
        assertEquals(response.statusCode(), SC_CREATED);
        assertNotEquals(id, 0);
        assertEquals(response.as(Category.class), requestBody);
    }

    @Test(dependsOnMethods = "testCreatingNewCategory", testName = "Categories-update a category", priority = 1)
    void testUpdatingAnExistingCategory() {
        requestBody.setName("Updated-Category-Name");
        requestBody.setDescription("Updated-Category-Description");
        requestBody.setActive(false);

//        In this case at postman api the body takes an application only there is no id ?
        response = CategoryApis.updateAnExistingCategory(requestBody, id);

        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.as(Category.class), requestBody);

    }

    @Test(testName = "Categories-delete a category", priority = 2)
    void testDeletingAnExistingCategory() {
        response = CategoryApis.deleteAnExistingCategory(id);
        assertEquals(response.statusCode(), SC_OK);
        response = CategoryApis.getCategories();
        assertEquals(response.statusCode(), SC_OK);
        System.out.println(response.body().jsonPath().getList("id"));
        assertFalse(response.body().jsonPath().getList("id").contains(id));
    }

    @Test(dependsOnMethods = "testCreatingNewCategory", testName = "Categories-get all categories")
    void testGettingAllCategories() {
        response = CategoryApis.getCategories();
        assertEquals(response.statusCode(), SC_OK);
        assertTrue(response.getBody().jsonPath().getList("userGroups").toArray().length > 0);
        assertNotNull(response.getBody().jsonPath().get("name"));
    }

}

