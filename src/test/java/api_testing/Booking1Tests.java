package api_testing;

import booking_apis.BookingBody;
import booking_apis.Bookingdates;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.booking.BookingAPIs;

import static io.restassured.RestAssured.given;

public class Booking1Tests {


    @Test
    void testAuthentication() {
        String requestBody = "{\n" +
                "  \"username\": \"admin\",\n" +
                "  \"password\": \"password123\"\n" +
                "}";
        given()
                .header("Content-Type", "application/json")
                .baseUri("https://restful-booker.herokuapp.com")
                .body(requestBody).
                when()
                .post("/auth").
                then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response()
                .getBody().toString().contains("token");

    }

    @Test
    void testCreatingNewBooking() {

        // Arrange
        BookingBody requestBody = BookingBody
                .builder()
                .setFirstname("Mohmaed")
                .setLastname("Farse")
                .setDepositpaid(true)
                .setAdditionalneeds("BreakFast")
                .setTotalprice(1000)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-2").setCheckout("2020-03-20").build())
                .build();

        //Action

        Response res = BookingAPIs.createNewBooking(requestBody);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertTrue(res.jsonPath().getInt("bookingid") > 0);
        Assert.assertEquals(res.jsonPath().get("booking.lastname"), "Farse");

    }
}
