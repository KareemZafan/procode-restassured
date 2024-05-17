package services.booking;

import booking_apis.BookingBody;
import io.restassured.response.Response;
import services.authentication.AuthenticationAPIs;

import static io.restassured.RestAssured.given;

public class BookingAPIs {

    public static Response createNewBooking(BookingBody bookingbody) {
        return given().header("Content-Type", "application/json")
                .body(bookingbody)
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .post("/booking")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response getBookingIds(){
        return given().header("Content-Type", "application/json")
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .get("/booking")
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response getBookingId(String id) {
        return given().header("Content-Type", "application/json")
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .get("/booking/"+id)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response updateEntireBooking(String id, BookingBody booking) {
        return given().header("Content-Type", "application/json")
                .header("Accept","application/json")
                .cookie("token", AuthenticationAPIs.getAccessToken())
                .baseUri("https://restful-booker.herokuapp.com")
                .body(booking)
                .when()
                .put("/booking/" + id)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response updateBookingPartially(String id, String requestBody ) {
        return given().header("Content-Type", "application/json")
                .header("Accept","application/json")
                .cookie("token", AuthenticationAPIs.getAccessToken())
                .baseUri("https://restful-booker.herokuapp.com")
                .body(requestBody)
                .when()
                .patch("/booking/" + id)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }

    public static Response deleteBooking(String id) {
        return given()
                .header("Content-Type", "application/json")
                .cookie("token", AuthenticationAPIs.getAccessToken())
                .baseUri("https://restful-booker.herokuapp.com")
                .when()
                .delete("/booking/" + id)
                .then()
                .log()
                .all()
                .extract()
                .response();
    }


}
