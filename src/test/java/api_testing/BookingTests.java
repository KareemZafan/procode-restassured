package api_testing;

import io.restassured.response.Response;
import mappers.booking.Booking;
import mappers.booking.BookingResponse;
import mappers.booking.Bookingdates;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.authentication.AuthenticationAPIs;
import services.booking.BookingAPIs;
import validations.Validations;

public class BookingTests {

    private String id;
    private Booking requestBody;
    private BookingResponse response;

    @BeforeMethod
    void setUp() {
        requestBody = Booking
                .builder()
                .setFirstname("Mohamed")
                .setLastname("Farse")
                .setDepositpaid(true)
                .setAdditionalneeds("BreakFast")
                .setTotalprice(1000)
                .setBookingdates(Bookingdates.builder().setCheckin("2020-01-02").setCheckout("2020-03-20").build())
                .build();

        response = BookingAPIs
                .createNewBooking(requestBody)
                .then()
                .extract()
                .response()
                .as(BookingResponse.class);

        id = response.getBookingid() + "";
    }

    @Test
    void testAuthentication() {
        Assert.assertNotNull(AuthenticationAPIs.getAccessToken());
    }

    @Test
    void testCreatingNewBooking() {
        Response res = BookingAPIs
                .createNewBooking(requestBody);

        Validations
                .assertThat(res)
                .statusCodeIs(200)
                .hasJsonSchema("json-schemas/creating_booking_schema.json")
                .hasValidBookingId()
                .hasBooking(requestBody);


        Assert.assertTrue(response.getBookingid() > 0);
        Assert.assertEquals(response.getBooking(), requestBody);
    }

    @Test
    void testGettingAllBookingIds() {

        //Action

        Response res = BookingAPIs.getBookingIds();

        // Assert
        Validations.assertThat(res).statusCodeIs(200).bodyContains("bookingid");

    }

    @Test
    void testGettingSpecificBooking() {

        //Action
        Response res = BookingAPIs.getBookingId(id);

        // Assert
        Validations
                .assertThat(res)
                .statusCodeIs(200)
                .hasJsonSchema("json-schemas/booking-schema.json")
                .bookingIs(requestBody);
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

        Response res = BookingAPIs.updateEntireBooking(id, newBooking);

        //Assertions
        Validations.assertThat(res)
                .statusCodeIs(200)
                .hasJsonSchema("json-schemas/booking-schema.json")
                .bookingIs(newBooking);

    }

    @Test
    void testUpdatePartialBooking() {

        String requestBody1 = "{\n" +
                "  \"firstname\": \"Ashraf\",\n" +
                "  \"lastname\": \"Ahmed\",\n" +
                "  \"totalprice\": 5900\n" +
                "}";
        //Action

        requestBody.setFirstname("Ashraf");
        requestBody.setLastname("Ahmed");
        requestBody.setTotalprice(5900);


        Response res = BookingAPIs.updateBookingPartially(id, requestBody1);

        Validations
                .assertThat(res)
                .statusCodeIs(200)
                .hasJsonSchema("json-schemas/booking-schema.json")
                .bookingIs(requestBody);

    }

    @AfterMethod
    void tearDown() {
        BookingAPIs.deleteBooking(id);
    }

}
