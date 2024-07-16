package validations;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import mappers.booking.Booking;
import mappers.booking.BookingResponse;
import org.testng.Assert;

public class Validations {
    protected Response response;

    private Validations(Response response) {
        this.response = response;
    }

    public static Validations assertThat(Response response) {
        return new Validations(response);
    }

    public Validations statusCodeIs(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode);
        return this;
    }

    public Validations bodyContains(String value) {
        Assert.assertTrue(response.getBody().asString().contains(value));
        return this;
    }

    public Validations hasJsonSchema(String jsonSchemaFile) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(jsonSchemaFile));
        return this;
    }

    public Validations hasBooking(Booking booking) {
        BookingResponse bookingResponse = response.then().extract().response().as(BookingResponse.class);
        Assert.assertEquals(bookingResponse.getBooking(), booking);
        return this;
    }

    public Validations hasBookingId(long bookingId) {
        BookingResponse bookingResponse = response.then().extract().response().as(BookingResponse.class);
        Assert.assertEquals(bookingResponse.getBookingid(), bookingId);
        return this;
    }

    public Validations hasValidBookingId() {
        BookingResponse bookingResponse = response.then().extract().response().as(BookingResponse.class);
        Assert.assertTrue(bookingResponse.getBookingid() > 0);
        return this;
    }


    public Validations bookingIs(Booking booking) {
        Booking actualBooking = response.then().extract().response().as(Booking.class);
        Assert.assertEquals(actualBooking, booking);
        return this;
    }

}
