package api_testing;

import mappers.booking.Booking;
import mappers.booking.BookingResponse;
import mappers.booking.Bookingdates;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.authentication.AuthenticationAPIs;
import services.booking.BookingAPIs;

import static io.restassured.RestAssured.given;

public class BookingTests {

    private String id;
    private Booking requestBody;
    private BookingResponse response;

    @BeforeMethod
    void setUp(){
         requestBody = Booking
                .builder()
                .setFirstname("Mohamed")
                .setLastname("Farse")
                .setDepositpaid(true)
                .setAdditionalneeds("BreakFast")
                .setTotalprice(1000)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-02").setCheckout("2020-03-20").build())
                .build();

          response= BookingAPIs
                 .createNewBooking(requestBody)
                 .then()
                 .extract()
                 .response()
                 .as(BookingResponse.class);

        id = response.getBookingid()+"";
    }
    @Test
    void testAuthentication() {
        Assert.assertNotNull(AuthenticationAPIs.getAccessToken());
    }

    @Test
    void testCreatingNewBooking() {
        Assert.assertTrue(response.getBookingid()>0);
        Assert.assertEquals(response.getBooking(), requestBody);
    }

    @Test
    void testGettingAllBookingIds() {

        //Action

        Response res = BookingAPIs.getBookingIds();

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertTrue(res.body().asString().contains("bookingid"));
    }

    @Test
    void testGettingSpecificBooking() {

        //Action
        Response res = BookingAPIs.getBookingId(id);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);
        Assert.assertTrue(response.getBookingid()>0);
        Assert.assertEquals(response.getBooking(), requestBody);
    }

    @Test
    void testUpdateEntireBooking() {

        Booking newBooking = Booking
                .builder()
                .setFirstname("Mostafa")
                .setLastname("Gamal")
                .setDepositpaid(true)
                .setAdditionalneeds("Dinner")
                .setTotalprice(3500)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-02").setCheckout("2020-03-20").build())
                .build();
        //Action

        Response res = BookingAPIs.updateEntireBooking(id,newBooking);
        Booking updatedBooking = res.then().extract().response().as(Booking.class);

        // Assert
        Assert.assertEquals(res.statusCode(), 200);

        Assert.assertEquals(updatedBooking, newBooking);
    }

    @Test
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

    @AfterMethod
    void tearDown(){
        BookingAPIs.deleteBooking(id);
    }

}
