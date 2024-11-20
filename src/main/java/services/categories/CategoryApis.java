package services.categories;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.Category;

import static io.restassured.RestAssured.given;

public class CategoryApis {
    public static Response creatNewCategory(Category newCategory) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .body(newCategory)
                .when()
                .post(Config.CATEGORY_ENDPOINT);
    }

    public static Response getCategories() {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .get(Config.CATEGORY_ENDPOINT);
    }

    public static Response updateAnExistingCategory(Category newCategory, long categoryId) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .body(newCategory)
                .patch(Config.CATEGORY_ENDPOINT + "/" + categoryId);
    }

    public static Response deleteAnExistingCategory(long categoryId) {
        return given()
                .spec(BaseSpecs.getForApplicationAndCategories().build())
                .when()
                .delete(Config.CATEGORY_ENDPOINT + "/" + categoryId);
    }

}
