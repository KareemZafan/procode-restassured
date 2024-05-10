package services.booking;

import booking_apis.BookingBody;
import io.restassured.response.Response;

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
}
