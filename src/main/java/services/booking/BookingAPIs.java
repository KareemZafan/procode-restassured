package services.booking;

import base_specs.BaseSpecs;
import config.Config;
import io.restassured.response.Response;
import mappers.booking.Booking;
import services.authentication.AuthenticationAPIs;

import static io.restassured.RestAssured.given;

public class BookingAPIs {

    public static Response createNewBooking(Booking booking) {
        return given()
                .spec(BaseSpecs.get().build())
                .body(booking)
                .log()
                .all()
                .when()
                .post(Config.BOOKING_ENDPOINT);
    }

    public static Response getBookingIds() {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(Config.BOOKING_ENDPOINT);
    }

    public static Response getBookingId(String id) {
        return given()
                .spec(BaseSpecs.get().build())
                .when()
                .get(String.format(Config.BOOKING_ENDPOINT_WITH_ID, id));
    }

    public static Response updateEntireBooking(String id, Booking booking) {
        return given()
                .spec(BaseSpecs.get(AuthenticationAPIs.getAccessToken()).build())
                .body(booking)
                .when()
                .put(String.format(Config.BOOKING_ENDPOINT_WITH_ID, id));
    }

    public static Response updateBookingPartially(String id, String requestBody) {
        return given()
                .spec(BaseSpecs.get(AuthenticationAPIs.getAccessToken()).build())
                .body(requestBody)
                .when()
                .patch(String.format(Config.BOOKING_ENDPOINT_WITH_ID, id));
    }

    public static Response deleteBooking(String id) {
        return given()
                .spec(BaseSpecs.get(AuthenticationAPIs.getAccessToken()).build())
                .when()
                .delete(String.format(Config.BOOKING_ENDPOINT_WITH_ID, id));
    }

}
