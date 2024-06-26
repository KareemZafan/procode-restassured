package booking_apis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
public class BookingBody {
    private String firstname;
    private String lastname;
    private long totalprice;
    private boolean depositpaid;
    private Bookingdates bookingdates;
    private String additionalneeds;
}
