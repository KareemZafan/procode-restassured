package api_testing;

import booking_apis.BookingBody;
import booking_apis.Bookingdates;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.booking.BookingAPIs;

import static io.restassured.RestAssured.given;

public class Booking1Tests {

    private String id;
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
                .setFirstname("Mohamed")
                .setLastname("Farse")
                .setDepositpaid(true)
                .setAdditionalneeds("BreakFast")
                .setTotalprice(1000)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-2").setCheckout("2020-03-20").build())
                .build();

        //Action

        Response res = BookingAPIs.createNewBooking(requestBody);
        id = res.jsonPath().get("bookingid").toString();
        System.out.println(id);
        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertTrue(res.jsonPath().getInt("bookingid") > 0);
        Assert.assertEquals(res.jsonPath().get("booking.lastname"), "Farse");

    }

    @Test
    void testGettingAllBookingIds() {

        //Action

        Response res = BookingAPIs.getBookingIds();

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertTrue(res.body().asString().contains("bookingid"));
    }

    @Test(dependsOnMethods ={"testCreatingNewBooking"})
    void testGettingSpecificBooking() {

        //Action

        Response res = BookingAPIs.getBookingId(id);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().get("firstname"),"Mohamed");
        Assert.assertEquals(res.jsonPath().get("lastname"),"Farse");
        Assert.assertEquals(res.jsonPath().getInt("totalprice"),1000);


    }

    @Test(dependsOnMethods ={"testCreatingNewBooking"})
    void testUpdateEntireBooking() {

        BookingBody newBooking = BookingBody
                .builder()
                .setFirstname("Mostafa")
                .setLastname("Gamal")
                .setDepositpaid(true)
                .setAdditionalneeds("Dinner")
                .setTotalprice(3500)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-2").setCheckout("2020-03-20").build())
                .build();
        //Action

        Response res = BookingAPIs.updateEntireBooking(id,newBooking);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().get("firstname"),"Mostafa");
        Assert.assertEquals(res.jsonPath().get("lastname"),"Gamal");
        Assert.assertEquals(res.jsonPath().getInt("totalprice"),3500);


    }

    @Test(dependsOnMethods ={"testCreatingNewBooking"})
    void testUpdatePartialBooking() {

        String requestBody = "{\n" +
                "  \"firstname\": \"Ashraf\",\n" +
                "  \"lastname\": \"Ahmed\",\n" +
                "  \"totalprice\": 5900\n" +
                "}";
        //Action

        Response res = BookingAPIs.updateBookingPartially(id,requestBody);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertEquals(res.jsonPath().get("firstname"),"Ashraf");
        Assert.assertEquals(res.jsonPath().get("lastname"),"Ahmed");
        Assert.assertEquals(res.jsonPath().getInt("totalprice"),5900);


    }

    @Test(dependsOnMethods = {"testCreatingNewBooking"})
    void testDeletingExistingBooking() {

        //Action
        Response res = BookingAPIs.deleteBooking(id);

        // Assert
        Assert.assertEquals(res.statusCode(), 201);

        res =BookingAPIs.getBookingId(id);
        Assert.assertEquals(res.statusCode(), 404);


    }


}
